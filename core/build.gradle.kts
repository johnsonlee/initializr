plugins {
    id("kotlin-library-plugin")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    compileOnly(deps.booster("booster-android-api"))
}