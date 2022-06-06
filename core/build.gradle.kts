plugins {
    id("booster-kotlin-library-convention")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    compileOnly(booster("booster-android-api"))
}