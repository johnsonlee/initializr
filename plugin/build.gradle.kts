plugins {
    `kotlin-dsl`
    id("io.johnsonlee.sonatype-publish-plugin") version "1.7.0"
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("${group}:booster")
    implementation("${group}:compiler")
}