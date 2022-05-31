package io.johnsonlee.initializr.internal

import androidx.tracing.trace
import io.johnsonlee.initializr.Context
import io.johnsonlee.initializr.Initializer
import io.johnsonlee.initializr.annotation.ThreadType
import io.johnsonlee.once.Once
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference

internal class InitializerWrapper(
        private val name: String,
        private val thread: ThreadType,
        private val initializer: Initializer
) : Initializer {

    internal val once = Once<Result<Unit>>()

    override fun initialize(context: Context) {
        once {
            trace("[${name}] ${initializer.javaClass.name}#initialize") {
                when (thread) {
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
        }
    }

}