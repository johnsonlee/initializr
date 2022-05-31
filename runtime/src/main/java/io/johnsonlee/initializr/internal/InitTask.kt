package io.johnsonlee.initializr.internal

import java.util.concurrent.RecursiveTask

internal class InitTask(
        private val context: ContextImpl,
        private val vertex: InitGraph.Vertex,
        private val graph: InitGraph
) : RecursiveTask<List<Throwable>>() {

    override fun compute(): List<Throwable> {
        val result = graph[vertex].map {
            InitTask(context, it, graph)
        }.let {
            invokeAll(it)
        }.mapNotNull(InitTask::join).flatten()

        if (result.isNotEmpty()) {
            return result
        }

        vertex.initializer.initialize(context)
        val value = vertex.initializer.once.value
        return if (value != null) {
            listOfNotNull(value.exceptionOrNull)
        } else emptyList()
    }
}