package io.johnsonlee.initializr

import android.app.Application

/**
 * The context of initialization
 */
interface Context {

    /**
     * The app instance
     */
    val app: Application

    /**
     * The build type
     */
    val buildType: String

    /**
     * The build flavor
     */
    val buildFlavor: String

}