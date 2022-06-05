package io.johnsonlee.initializr.internal

import java.util.Collections

internal class InitGraph private constructor(
        private val _edges: Map<Vertex, Set<Vertex>>
) : Iterable<InitGraph.Edge> {

    private val edges: Set<Edge> by lazy {
        _edges.entries.fold(mutableSetOf<Edge>()) { acc, entry ->
            entry.value.forEach { v ->
                acc.add(Edge(entry.key, v))
            }
            acc
        }
    }

    private val us: Set<Vertex> by lazy {
        _edges.keys.toSet()
    }

    private val vs: Set<Vertex> by lazy {
        _edges.values.fold(mutableSetOf<Vertex>()) { acc, vv ->
            acc.addAll(vv)
            acc
        }
    }

    val starts: Set<Vertex> by lazy {
        us - vs
    }

    val ends: Set<Vertex> by lazy {
        vs - us
    }

    override fun iterator(): Iterator<Edge> = edges.asSequence().map { (u, v) ->
        Edge(u, v)
    }.iterator()

    operator fun get(u: Vertex): Set<Vertex> {
        return _edges[u]?.let(Collections::unmodifiableSet) ?: emptySet()
    }

    class Builder {

        private val edges = mutableMapOf<Vertex, MutableSet<Vertex>>()

        fun addEdge(u: Vertex, v: Vertex) = apply {
            edges.getOrPut(u) {
                mutableSetOf()
            }.add(v)
        }

        fun build(): InitGraph = InitGraph(Collections.unmodifiableMap(edges))

    }

    data class Edge(
            val from: Vertex,
            val to: Vertex
    )

    data class Vertex(
            val name: String,
            val initializer: InitializerWrapper,
            val dependencies: Set<String>
    ) {
        override fun equals(other: Any?): Boolean {
            if (other === this) return true
            if (other !is Vertex) return false
            val l = arrayOf(name, initializer.javaClass, dependencies)
            val r = arrayOf(other.name, other.initializer.javaClass, other.dependencies)
            return l.contentEquals(r)
        }

        override fun hashCode(): Int {
            return arrayOf(name, initializer.javaClass, dependencies).contentHashCode()
        }
    }

}
