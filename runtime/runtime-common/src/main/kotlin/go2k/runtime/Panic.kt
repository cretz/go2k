package go2k.runtime

import go2k.runtime.builtin.EmptyInterface

class Panic(
    val v: EmptyInterface?,
    cause: Throwable? = null,
    var previous: Panic? = null
) : RuntimeException("panic: $v", cause)