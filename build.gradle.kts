tasks.register("cleanAll") {
    dependsOn(gradle.includedBuild("buildscripts").task(":cleanAll"))
    dependsOn(gradle.includedBuild("core").task(":clean"))
    dependsOn(gradle.includedBuild("runtime").task(":clean"))
    dependsOn(gradle.includedBuild("compiler").task(":clean"))
    dependsOn(gradle.includedBuild("plugin").task(":clean"))
    dependsOn(":app:clean")
}

tasks.register("publishToMavenLocal") {
    dependsOn(gradle.includedBuild("core").task(":publishToMavenLocal"))
    dependsOn(gradle.includedBuild("runtime").task(":publishToMavenLocal"))
    dependsOn(gradle.includedBuild("compiler").task(":publishToMavenLocal"))
}

tasks.register("publishToSonatype") {
    dependsOn(gradle.includedBuild("core").task(":publishToSonatype"))
    dependsOn(gradle.includedBuild("runtime").task(":publishReleasePublicationToSonatypeRepository"))
    dependsOn(gradle.includedBuild("compiler").task(":publishToSonatype"))
}
