package io.johnsonlee.initializr.app

import android.app.Application
import io.johnsonlee.initializr.Initiator

class ExampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Initiator.init(this)
    }

}