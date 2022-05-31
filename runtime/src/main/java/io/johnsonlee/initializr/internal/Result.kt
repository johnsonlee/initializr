package io.johnsonlee.initializr.internal

private class Failure(val exception: Throwable)

internal class Result<V>(
        private val _value: Any?
) {

    val exceptionOrNull: Throwable?
        get() = when (_value) {
            is Failure -> _value.exception
            else -> null
        }

    val getOrNull: V?
        get() = when (_value) {
            is Failure -> null
            else -> _value as? V
        }

    companion object {

        fun <V> success(value: V): Result<V> = Result(value)

        fun <V> failure(value: Throwable): Result<V> = Result(Failure(value))

        fun <R> runCatching(block: () -> R): Result<R> {
            return try {
                success(block())
            } catch (e: Throwable) {
                failure(e)
            }
        }

    }

}