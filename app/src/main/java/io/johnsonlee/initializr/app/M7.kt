package io.johnsonlee.initializr.app

import io.johnsonlee.initializr.AbstractInitializer
import io.johnsonlee.initializr.Context
import io.johnsonlee.initializr.ThreadType
import io.johnsonlee.initializr.annotation.Initialization

@Initialization(
    name = "m7",
    dependencies = ["m6"]
)
class M7 : AbstractInitializer() {

    override fun initialize(context: Context) {
        try {
            Thread.sleep(600)
        } catch (e: InterruptedException) {
        }
    }

    override val runningThreadType: ThreadType = ThreadType.WORKER

}