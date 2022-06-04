object deps {

    fun junit() = "${junit.id}:${junit.version}"

    object junit {
        const val id = "junit:junit"
        const val version = "4.13.2"
    }

    object android {
        const val compileSdkVersion = 31
        const val minSdkVersion = 21
        const val targetSdkVersion = 31

        const val gradlePluginVersion = "4.0.0"

        fun gradlePlugin() = "com.android.tools.build:gradle:${gradlePluginVersion}"
    }

    object jetpack {

        fun androidx(component: String, module: String, version: String) = "androidx.${component}:${module}:${version}"

        fun annotation() = androidx("annotation", "annotation", "1.0.0")
        fun tracing() = androidx("tracing", "tracing-ktx", "1.0.0")
    }

    fun booster(module: String) = "com.didiglobal.booster:${module}:4.10.0-rc1"

    fun codegen(module: String) = "io.johnsonlee.codegen:${module}:1.0.0"

}
