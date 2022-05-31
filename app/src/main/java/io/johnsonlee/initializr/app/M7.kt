package io.johnsonlee.initializr.app

import androidx.annotation.WorkerThread
import io.johnsonlee.initializr.Context
import io.johnsonlee.initializr.Initializer
import io.johnsonlee.initializr.annotation.Initialization

@Initialization(
    name = "m7",
    dependencies = ["m6"]
)
class M7 : Initializer {

    @WorkerThread
    override fun initialize(context: Context) {
        try {
            Thread.sleep(600)
        } catch (e: InterruptedException) {
        }
    }

}