package go2k.runtime

suspend inline fun withDefers(fn: WithDefers.() -> Unit) {
    val defers = WithDefers()
    var err: Throwable? = null
    try { defers.fn() }
    catch (t: Throwable) { err = t }
    defers.`$runDefers`(err)
}

class Recoverable(internal var panic: Panic?) {
    fun recover() = panic?.v.also { panic = null }
}

internal class Defer<T>(val args: T, val fn: suspend Recoverable.(T) -> Unit) {
    suspend fun run(r: Recoverable) { r.fn(args) }
}

class WithDefers {
    internal val defers = mutableListOf<Defer<*>>()

    suspend fun <T> defer(args: T, fn: suspend Recoverable.(T) -> Unit) {
        defers += Defer(args, fn)
    }

    suspend fun `$runDefers`(t: Throwable?) {
        val r = Recoverable(t?.let { throwableToPanic(it) })
        defers.asReversed().forEach { defer ->
            try { defer.run(r) }
            catch (t: Throwable) { r.panic = throwableToPanic(t, r.panic) }
        }
        r.panic?.also { throw it }
    }

    internal fun throwableToPanic(t: Throwable, previous: Panic? = null) =
        (if (t is Panic) t else Panic(GoInterface.Empty.impl(t), t)).also { if (previous != null) it.previous = previous }
}

class Panic(
    val v: GoInterface.Empty?,
    cause: Throwable? = null,
    var previous: Panic? = null
) : RuntimeException("panic: $v", cause)