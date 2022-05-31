package io.johnsonlee.initializr.internal

import java.util.concurrent.LinkedBlockingDeque

private val QUIT = Runnable {}

internal class MainLooper {

    private val queue = LinkedBlockingDeque<Runnable>()

    fun loop() {
        while (true) {
            val job = try {
                queue.take()
            } catch (e: InterruptedException) {
                null
            }
            if (QUIT === job) {
                break
            }
            job?.run()
        }
    }

    fun post(message: Runnable) {
        queue.put(message)
    }

    fun post(message: () -> Unit) {
        queue.put(Runnable {
            message()
        })
    }

    fun quit() {
        post(QUIT)
    }

}