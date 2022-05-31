package io.johnsonlee.initializr;

import android.app.Application;
import android.content.pm.ApplicationInfo;

import io.johnsonlee.initializr.internal.InitGraph;

/**
 * THIS CLASS IS MANIPULATED BY BUILD PLUGIN IN COMPILE TIME, PLEASE DO NOT MODIFY!!!
 */
class InitResolver {

    static String resolveBuildType(final Application app) {
        try {
            return Class.forName(app.getPackageName() + ".BuildConfig").getDeclaredField("BUILD_TYPE").get(null).toString();
        } catch (final Throwable t) {
            return (0 != (app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) ? "debug" : "release";
        }
    }

    static String resolveBuildFlavor(final Application app) {
        try {
            return Class.forName(app.getPackageName() + ".BuildConfig").getDeclaredField("FLAVOR").get(null).toString();
        } catch (final Throwable t) {
            return "";
        }
    }

    static InitGraph resolveInitGraph(final Application app) {
        throw new RuntimeException("Stub!");
    }

    private final Application app;

    InitResolver(final Application app) {
        this.app = app;
    }

    String getBuildType() {
        return resolveBuildType(app);
    }

    String getBuildFlavor() {
        return resolveBuildFlavor(app);
    }

    InitGraph getInitGraph() {
        return resolveInitGraph(app);
    }

}
