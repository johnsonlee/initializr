package io.johnsonlee.initializr

import com.didiglobal.booster.graph.Node
import io.johnsonlee.initializr.annotation.ThreadType

data class InitConfig(
        val name: String,
        val initializer: String,
        val dependencies: List<String>,
        val thread: ThreadType
) : Node {
    override fun hashCode(): Int {
        return initializer.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InitConfig) return false
        return initializer == other.initializer
    }
}
