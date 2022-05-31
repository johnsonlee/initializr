package io.johnsonlee.initializr

class InitializationException @JvmOverloads constructor(
        override val message: String? = null
) : Exception(message)