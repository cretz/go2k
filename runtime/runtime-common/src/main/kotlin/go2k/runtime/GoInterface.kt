package go2k.runtime

interface GoInterface {
    val `$v`: Any?
    val `$methodLookup`: ((String) -> Function<*>?)?

    interface Empty : GoInterface {
        class Impl(override val `$v`: Any?, override val `$methodLookup`: ((String) -> Function<*>?)?) : Empty

        companion object {
            fun impl(v: Any?, methodLookup: ((String) -> Function<*>?)? = null): Empty = when (v) {
                is Empty -> v
                is GoInterface -> Impl(v.`$v`, v.`$methodLookup`)
                else -> Impl(v, methodLookup)
            }
        }
    }
}