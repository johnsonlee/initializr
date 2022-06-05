package io.johnsonlee.initializr

/**
 * The running thread of initializer
 */
enum class ThreadType {
    /**
     * Running in main thread
     */
    MAIN,

    /**
     * Running in worker thread
     */
    WORKER
}
