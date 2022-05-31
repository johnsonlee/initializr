plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("io.johnsonlee.sonatype-publish-plugin")
}

val boosterVersion: String by extra
val codegenVersion: String by extra

dependencies {
    kapt("com.google.auto.service:auto-service:1.0")
    implementation("com.google.auto.service:auto-service:1.0")
    implementation(kotlin("bom"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(project(":core"))
    implementation("com.didiglobal.booster:booster-api:${boosterVersion}")
    implementation("com.didiglobal.booster:booster-graph:${boosterVersion}")
    implementation("com.didiglobal.booster:booster-transform-asm:${boosterVersion}")
    implementation("io.johnsonlee.codegen:compiler:${codegenVersion}")
    implementation("io.johnsonlee.codegen:mustache:${codegenVersion}")
    implementation("com.github.spullara.mustache.java:compiler:0.9.10")
    implementation("androidx.annotation:annotation:1.0.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.0")
    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.2")
}