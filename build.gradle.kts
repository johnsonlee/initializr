tasks.register("clean") {
    dependsOn(gradle.includedBuild("buildscripts").task(":cleanAll"))
    dependsOn(gradle.includedBuild("core").task(":clean"))
    dependsOn(gradle.includedBuild("compiler").task(":clean"))
    dependsOn(gradle.includedBuild("runtime").task(":clean"))
    dependsOn(gradle.includedBuild("plugin").task(":clean"))
    dependsOn(":app:clean")
}

tasks.register("assemble") {
    dependsOn(gradle.includedBuild("core").task(":assemble"))
    dependsOn(gradle.includedBuild("compiler").task(":assemble"))
    dependsOn(gradle.includedBuild("runtime").task(":assemble"))
    dependsOn(gradle.includedBuild("plugin").task(":assemble"))
}

tasks.register("publishToMavenLocal") {
    dependsOn(gradle.includedBuild("core").task(":publishToMavenLocal"))
    dependsOn(gradle.includedBuild("compiler").task(":publishToMavenLocal"))
    dependsOn(gradle.includedBuild("runtime").task(":publishToMavenLocal"))
}

tasks.register("publishToSonatype") {
    dependsOn(gradle.includedBuild("core").task(":publishToSonatype"))
    dependsOn(gradle.includedBuild("compiler").task(":publishToSonatype"))
    dependsOn(gradle.includedBuild("runtime").task(":publishReleasePublicationToSonatypeRepository"))
}

tasks.register("publishToNexus") {
    dependsOn(gradle.includedBuild("core").task(":publishAllPublicationsToMavenRepository"))
    dependsOn(gradle.includedBuild("compiler").task(":publishAllPublicationsToMavenRepository"))
    dependsOn(gradle.includedBuild("runtime").task(":publishReleasePublicationToMavenRepository"))
}
