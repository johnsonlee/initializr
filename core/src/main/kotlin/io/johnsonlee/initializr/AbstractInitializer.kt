package io.johnsonlee.initializr

abstract class AbstractInitializer : Initializer {

    override val runningThreadType: ThreadType = ThreadType.MAIN

}