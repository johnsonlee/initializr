package io.johnsonlee.initializr.internal

import android.app.Application
import io.johnsonlee.initializr.Context

internal class ContextImpl(
        val dispatcher: MainLooper,
        override val app: Application,
        override val buildType: String,
        override val buildFlavor: String
) : Context
