plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(deps.android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(deps.android.minSdkVersion)
        targetSdkVersion(deps.android.targetSdkVersion)
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

    dependencies {
        implementation(kotlin("bom"))
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))
        implementation(deps.jetpack.annotation())

        testImplementation(kotlin("test"))
        testImplementation(deps.junit())
    }

}
