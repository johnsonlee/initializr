import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("io.johnsonlee.sonatype-publish-plugin")
}

dependencies {
    kapt("com.google.auto.service:auto-service:1.0")

    implementation(gradleApi())
    implementation(kotlin("bom"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(deps.booster("booster-api"))
    implementation(deps.booster("booster-cha"))
    implementation(deps.booster("booster-graph"))
    implementation(deps.booster("booster-transform-asm"))
    implementation("com.google.auto.service:auto-service:1.0")

    testImplementation(kotlin("test"))
    testImplementation(deps.junit())
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xskip-metadata-version-check")
        jvmTarget = "1.8"
    }
}