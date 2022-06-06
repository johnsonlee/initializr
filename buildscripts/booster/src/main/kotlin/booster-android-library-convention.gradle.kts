plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.didiglobal.booster")
}

android {
    compileSdkVersion(config.compileSdkVersion)

    defaultConfig {
        minSdkVersion(config.minSdkVersion)
        targetSdkVersion(config.targetSdkVersion)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xskip-metadata-version-check")
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation(kotlin("bom"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(jetpack.annotation())

    testImplementation(kotlin("test"))
    testImplementation(junit())
}
