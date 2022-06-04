plugins {
    id("booster-kotlin-library-plugin")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api("${group}:core")
    implementation(deps.codegen("compiler"))
    implementation(deps.codegen("mustache"))
    implementation(deps.jetpack.annotation())
    implementation("com.github.spullara.mustache.java:compiler:0.9.10")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.0")
}