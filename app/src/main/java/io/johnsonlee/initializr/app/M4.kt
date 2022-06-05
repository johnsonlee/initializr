package io.johnsonlee.initializr.app

import io.johnsonlee.initializr.AbstractInitializer
import io.johnsonlee.initializr.Context
import io.johnsonlee.initializr.annotation.Initialization

@Initialization(
    name = "m4",
    dependencies = ["m0", "m1", "m2"]
)
class M4 : AbstractInitializer() {

    override fun initialize(context: Context) {
        try {
            Thread.sleep(600)
        } catch (e: InterruptedException) {
        }
    }

}