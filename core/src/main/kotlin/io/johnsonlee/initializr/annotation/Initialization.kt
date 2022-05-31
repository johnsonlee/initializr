package io.johnsonlee.initializr.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Initialization(
    val name: String,
    vararg val dependencies: String = []
)

