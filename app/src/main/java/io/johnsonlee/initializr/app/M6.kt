package io.johnsonlee.initializr.app

import androidx.annotation.WorkerThread
import io.johnsonlee.initializr.Context
import io.johnsonlee.initializr.Initializer
import io.johnsonlee.initializr.annotation.Initialization

@Initialization(
    name = "m6",
    dependencies = ["m3", "m4", "m5"]
)
class M6 : Initializer {

    @WorkerThread
    override fun initialize(context: Context) {
        try {
            Thread.sleep(600)
        } catch (e: InterruptedException) {
        }
    }

}