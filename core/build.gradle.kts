plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("io.johnsonlee.sonatype-publish-plugin")
}

val boosterVersion: String by extra

dependencies {
    implementation(kotlin("bom"))
    implementation(kotlin("stdlib"))
    compileOnly("com.didiglobal.booster:booster-android-api:${boosterVersion}")
}