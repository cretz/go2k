package go2k.runtime

interface GoInterface {
    val `$v`: Any?

    interface Empty : GoInterface {
        class Impl(override val `$v`: Any?) : Empty

        companion object {
            fun impl(v: Any?): Empty = when (v) {
                is Empty -> v
                is GoInterface -> Impl(v.`$v`)
                else -> Impl(v)
            }
        }
    }
}