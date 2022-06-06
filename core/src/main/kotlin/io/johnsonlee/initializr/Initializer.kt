package io.johnsonlee.initializr

/**
 * The interface for initialization
 *
 * ```kotlin
 * @Initialization(
 *     name = "MyInitializer",
 *     dependencies = []
 * )
 * class MyInitializer : Initializer {
 *     override fun initialize(context: Context) {
 *         doInit(context)
 *     }
 *
 *     override val runningThreadType = ThreadType.MAIN
 * }
 * ```
 */
interface Initializer {

    /**
     * Initializing with [Context]
     */
    fun initialize(context: Context)

    /**
     * The running thread type of this initializer
     */
    val runningThreadType: ThreadType

}

