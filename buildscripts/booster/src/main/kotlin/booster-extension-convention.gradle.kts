import org.gradle.api.JavaVersion
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
    implementation(booster("booster-api"))
    implementation(booster("booster-cha"))
    implementation(booster("booster-graph"))
    implementation(booster("booster-transform-asm"))
    implementation("com.google.auto.service:auto-service:1.0")

    testImplementation(kotlin("test"))
    testImplementation(junit())
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xskip-metadata-version-check")
        jvmTarget = "1.8"
    }
}