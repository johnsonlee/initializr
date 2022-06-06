plugins {
    id("booster-extension-convention")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api("${group}:core")
    implementation(codegen("compiler"))
    implementation(codegen("mustache"))
    implementation(jetpack.annotation())
    implementation("com.github.spullara.mustache.java:compiler:0.9.10")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.0")
}