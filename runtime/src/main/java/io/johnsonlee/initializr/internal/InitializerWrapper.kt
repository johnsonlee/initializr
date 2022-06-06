package io.johnsonlee.initializr.internal

import androidx.tracing.trace
import io.johnsonlee.initializr.Context
import io.johnsonlee.initializr.Initializer
import io.johnsonlee.initializr.Initiator.Companion.log
import io.johnsonlee.initializr.ThreadType
import io.johnsonlee.once.Once
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference

internal class InitializerWrapper(
    private val name: String,
    private val initializer: Initializer
) : Initializer {

    override val runningThreadType: ThreadType
        get() = initializer.runningThreadType

    internal val once = Once<Result<Unit>>()

    override fun initialize(context: Context) {
        once {
            trace("[${name}] ${initializer.javaClass.name}#initialize") {
                val start = System.currentTimeMillis()
                val result = doInitialize(context)
                val duration = System.currentTimeMillis() - start
                log("Initialize ${initializer.javaClass.name} ${if (result.exceptionOrNull == null) "success" else "failed"} in $runningThreadType thread: ${duration}ms")
                result
            }
        }
    }

    private fun doInitialize(context: Context) = when (runningThreadType) {
        ThreadType.MAIN -> {
            val signal = CountDownLatch(1)
            val result = AtomicReference<Result<Unit>>()

            (context as ContextImpl).dispatcher.post {
                result.set(Result.runCatching {
                    initializer.initialize(context)
                })
                signal.countDown()
            }

            try {
                signal.await()
                result.get()
            } catch (e: InterruptedException) {
                Result.failure<Unit>(e)
            }
        }
        else -> Result.runCatching {
            initializer.initialize(context)
        }
    }

}