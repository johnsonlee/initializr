import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("io.johnsonlee.sonatype-publish-plugin")
}

dependencies {
    implementation(kotlin("bom"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    testImplementation(kotlin("test"))
    testImplementation(deps.junit())
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xskip-metadata-version-check")
        jvmTarget = "1.8"
    }
}