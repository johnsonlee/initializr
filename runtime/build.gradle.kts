plugins {
    id("com.android.library")
    id("kotlin-android")
    id("io.johnsonlee.sonatype-publish-plugin")
}

android {
    compileSdkVersion(32)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(32)
        versionCode = 1
        versionName = "${project.version}"

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
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(kotlin("bom"))
    implementation(kotlin("stdlib"))
    implementation(project(":core"))
    implementation("io.johnsonlee:once:1.0.0")
    implementation("androidx.tracing:tracing-ktx:1.0.0")
    implementation("androidx.annotation:annotation:1.0.0")
    testImplementation("junit:junit:4.13.2")
}