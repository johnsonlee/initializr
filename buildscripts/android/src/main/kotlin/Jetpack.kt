object config {
    const val compileSdkVersion = 31
    const val minSdkVersion = 21
    const val targetSdkVersion = 31
}

object jetpack {

    fun androidx(component: String, module: String, version: String) = "androidx.${component}:${module}:${version}"

    object annotation {
        operator fun invoke() = androidx("annotation", "annotation", "1.0.0")
    }

    object tracing {
        operator fun invoke() = androidx("tracing", "tracing-ktx", "1.0.0")
    }
}