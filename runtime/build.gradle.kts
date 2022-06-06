plugins {
    id("android-library-convention")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api("${group}:core")
    implementation("io.johnsonlee:once:1.1.0")
    implementation(jetpack.tracing())
}