package io.johnsonlee.initializr

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Looper
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.tracing.trace
import io.johnsonlee.initializr.annotation.Initialization
import io.johnsonlee.initializr.internal.ContextImpl
import io.johnsonlee.initializr.internal.InitGraph
import io.johnsonlee.initializr.internal.InitResolver
import io.johnsonlee.initializr.internal.InitTask
import io.johnsonlee.initializr.internal.InitializerWrapper
import io.johnsonlee.initializr.internal.MainLooper
import io.johnsonlee.initializr.internal.resolveBuildFlavor
import io.johnsonlee.initializr.internal.resolveBuildType
import io.johnsonlee.once.Once
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

class Initiator {

    companion object {

        private val initialized = Once<Unit>()

        private val debuggable = Once<Boolean>()

        /**
         * Init with initializr compiler
         */
        fun init(app: Application) {
            trace("Initiator#init") {
                initialized {
                    doInit(app, InitResolver(app))
                }
            }
        }

        /**
         * Init without initializr compiler
         */
        fun init(app: Application, initializers: Set<Initializer>) {
            trace("Initiator#initWithInitializers") {
                initialized {
                    doInit(app, initializers)
                }
            }
        }

        private fun doInit(app: Application, initializers: Set<Initializer>) {
            trace("Initiator#doInitWithInitializers") {
                val initialize = Initializer::class.java.methods.first {
                    it.parameterTypes.size == 1 && arrayOf(Context::class.java).contentEquals(it.parameterTypes)
                }
                val specs = initializers.mapNotNull {
                    val clazz = it.javaClass
                    clazz.getAnnotation(Initialization::class.java)?.let { init ->
                        val m = clazz.declaredMethods.first { m -> m == initialize }
                        val thread = if (m.isAnnotationPresent(WorkerThread::class.java)) {
                            ThreadType.WORKER
                        } else {
                            ThreadType.MAIN
                        }
                        InitGraph.Vertex(init.name, InitializerWrapper(init.name, it), init.dependencies.toSet())
                    }
                }.toSet()
                doInit(app, resolveBuildType(app), resolveBuildFlavor(app), buildInitGraph(specs))
            }
        }

        private fun doInit(app: Application, resolver: InitResolver) {
            trace("Initiator#doInitWithResolver") {
                doInit(app, trace("InitResolver#resolveBuildType") {
                    resolver.buildType
                }, trace("InitResolver#resolveBuildFlavor") {
                    resolver.buildFlavor
                }, trace("InitResolver#resolveInitGraph") {
                    resolver.initGraph
                })
            }
        }

        private fun doInit(app: Application, variant: String, flavor: String, graph: InitGraph) {
            trace("Initiator#doInitWithArgs") {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    throw UnsupportedOperationException("Current thread ${Thread.currentThread()} is not main thread")
                }

                debuggable {
                    (app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
                }

                val looper = MainLooper()
                val context = ContextImpl(looper, app, variant, flavor)
                val executor = ForkJoinPool(
                        Runtime.getRuntime().availableProcessors(),
                        ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                        null,
                        false
                )
                val result = AtomicReference<Throwable?>()

                thread {
                    try {
                        graph.starts.map {
                            executor.submit(InitTask(context, it, graph))
                        }.map(ForkJoinTask<List<Throwable>>::join).flatten().distinct().takeIf {
                            it.isNotEmpty()
                        }?.fold(InitializationException()) { e, suppressed ->
                            e.addSuppressed(suppressed)
                            e
                        }?.let(result::set)
                    } finally {
                        executor.shutdown()
                        executor.awaitTermination(1, TimeUnit.MINUTES)
                    }
                    context.dispatcher.quit()
                }

                context.dispatcher.loop()
                result.get()?.let { e ->
                    log("Initialize failed", e)
                    throw e
                }
            }
        }

        private fun buildInitGraph(vertexes: Set<InitGraph.Vertex>): InitGraph = trace("Initiator#buildInitGraph") {
            val n2v = vertexes.associateBy(InitGraph.Vertex::name)

            val sentinel = InitGraph.Vertex("\$\$sentinel\$\$", InitializerWrapper("\$\$sentinel\$\$", object : Initializer {
                override fun initialize(context: Context) = Unit
                override val runningThreadType: ThreadType = ThreadType.WORKER
            }), emptySet())

            vertexes.fold(InitGraph.Builder()) { builder, u ->
                val vv = u.dependencies.mapNotNull(n2v::get).takeIf(List<InitGraph.Vertex>::isNotEmpty) ?: listOf(sentinel)
                vv.forEach { v ->
                    builder.addEdge(u, v)
                }
                builder
            }.build()
        }

        @JvmOverloads
        internal fun log(msg: String, e: Throwable? = null) {
            if (debuggable.value != true) return
            val logger: (String, String, Throwable?) -> Int = if (e == null) Log::i else Log::e
            logger("initializr", msg, e)
        }

    }

}