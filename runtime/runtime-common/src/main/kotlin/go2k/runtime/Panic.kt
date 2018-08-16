package go2k.runtime

class Panic(val v: Any?) : RuntimeException("panic: $v")