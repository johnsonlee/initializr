plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    kotlin("jvm") version "1.5.30"
    kotlin("kapt") version "1.5.30"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    kapt("com.google.auto.service:auto-service:1.0")
    implementation("com.google.auto.service:auto-service:1.0")
    implementation("com.didiglobal.booster:booster-gradle-plugin:4.0.0")
    implementation("com.didiglobal.booster:booster-api:4.0.0")
    implementation("com.didiglobal.booster:booster-transform-asm:4.0.0")
    compileOnly("com.android.tools.build:gradle:4.0.0")
    implementation(kotlin("bom"))
    implementation(kotlin("stdlib"))
}

sourceSets {
    main {
        java {
            srcDirs("../plugin/src/main/java")
        }
    }
}

gradlePlugin {
    plugins {
        create("TemplatePlugin") {
            id = "io.johnsonlee.template-gradle-plugin"
            implementationClass = "io.johnsonlee.template.gradle.TemplatePlugin"
        }
    }
}
