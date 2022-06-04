plugins {
    `kotlin-dsl`
}

dependencies {
    api(project(":base"))
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:${embeddedKotlinVersion}")
}