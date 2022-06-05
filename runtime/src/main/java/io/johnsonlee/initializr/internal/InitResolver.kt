package io.johnsonlee.initializr.internal

import android.app.Application
import android.content.pm.ApplicationInfo
import io.johnsonlee.initializr.internal.InitGraph

internal class InitResolver(app: Application) {

    val buildType: String by lazy {
        resolveBuildType(app)
    }

    val buildFlavor: String by lazy {
        resolveBuildFlavor(app)
    }

    val initGraph: InitGraph by lazy {
        resolveInitGraph(app)
    }

}

internal fun resolveBuildType(app: Application): String {
    return try {
        Class.forName("${app.packageName}.BuildConfig").getDeclaredField("BUILD_TYPE").get(null) as? String
    } catch (t: Throwable) {
        null
    } ?: if (0 != app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) "debug" else "release"
}

internal fun resolveBuildFlavor(app: Application): String {
    return try {
        Class.forName("${app.packageName}.BuildConfig").getDeclaredField("FLAVOR").get(null) as? String
    } catch (t: Throwable) {
        null
    } ?: ""
}

internal fun resolveInitGraph(app: Application): InitGraph {
    TODO("Stub!")
}
