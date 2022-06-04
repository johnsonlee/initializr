rootProject.name = "initializr"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
    includeBuild("buildscripts")
    includeBuild("plugin")
}

includeBuild("buildscripts")
includeBuild("compiler")
includeBuild("core")
includeBuild("plugin")
includeBuild("runtime")

include(":app")