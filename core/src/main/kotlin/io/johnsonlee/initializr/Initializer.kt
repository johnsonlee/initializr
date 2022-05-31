package io.johnsonlee.initializr

/**
 * The interface for initialization
 */
interface Initializer {

    /**
     * Initializing with [Context], it runs in main thread by default, the [thread annotations](https://developer.android.com/studio/write/annotations#thread-annotations)
     * can be used to specify the running thread, for example:
     *
     * ```kotlin
     * @Initialization(
     *     name = "MyInitializer",
     *     dependencies = []
     * )
     * class MyInitializer : Initializer {
     *     @WorkerThread
     *     override fun initialize(context: Context) {
     *         doInit(context)
     *     }
     * }
     * ```
     *
     * @see <a href="https://developer.android.com/reference/androidx/annotation/MainThread">@MainThread</a>
     * @see <a href="https://developer.android.com/reference/androidx/annotation/UiThread">@UiThread</a>
     * @see <a href="https://developer.android.com/reference/androidx/annotation/WorkerThread">@WorkerThread</a>
     * @see <a href="https://developer.android.com/reference/androidx/annotation/AnyThread">@AnyThread</a>
     */
    fun initialize(context: Context)

}

