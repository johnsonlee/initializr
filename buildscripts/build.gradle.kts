allprojects {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

tasks.register("cleanAll") {
    dependsOn(*subprojects.map {
        ":${it.name}:clean"
    }.toTypedArray())
}
