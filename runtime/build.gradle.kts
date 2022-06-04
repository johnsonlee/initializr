plugins {
    id("android-library-plugin")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api("${group}:core")
    implementation("io.johnsonlee:once:1.1.0")
    implementation(deps.jetpack.tracing())
}