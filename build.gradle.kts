plugins {
    id("io.johnsonlee.sonatype-publish-plugin") version "1.7.0"
}

tasks.register("cleanAll") {
    dependsOn(gradle.includedBuild("buildscripts").task(":cleanAll"))
    dependsOn(gradle.includedBuild("core").task(":clean"))
    dependsOn(gradle.includedBuild("compiler").task(":clean"))
    dependsOn(gradle.includedBuild("runtime").task(":clean"))
    dependsOn(gradle.includedBuild("plugin").task(":clean"))
    dependsOn(":app:clean")
}

tasks.register("publishAllToMavenLocal") {
    dependsOn(gradle.includedBuild("core").task(":publishToMavenLocal"))
    dependsOn(gradle.includedBuild("compiler").task(":publishToMavenLocal"))
    dependsOn(gradle.includedBuild("runtime").task(":publishToMavenLocal"))
}

tasks.register("publishAllToSonatype") {
    dependsOn(gradle.includedBuild("core").task(":publishToSonatype"))
    dependsOn(gradle.includedBuild("compiler").task(":publishToSonatype"))
    dependsOn(gradle.includedBuild("runtime").task(":publishReleasePublicationToSonatypeRepository"))
}
