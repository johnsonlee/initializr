plugins {
    id("io.johnsonlee.initializr")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    kapt("${group}:compiler")

    implementation("${group}:runtime")
    implementation(deps.jetpack.androidx("core", "core-ktx", "1.6.0"))
    implementation(deps.jetpack.androidx("appcompat", "appcompat", "1.3.1"))
    implementation(deps.jetpack.androidx("activity", "activity-ktx", "1.3.1"))
    implementation(deps.jetpack.androidx("fragment", "fragment-ktx", "1.3.6"))
    implementation(deps.jetpack.androidx("lifecycle", "lifecycle-common", "2.3.1"))
    implementation(deps.jetpack.androidx("constraintlayout", "constraintlayout", "2.1.1"))
}