package go2k.runtime

object Ops {
    fun eql(lhs: Any?, rhs: Any?): Boolean {
        if (lhs == rhs) return true
        if (lhs == null || rhs == null) return false
        // TODO: lots of checks required here...
        return false
    }

    inline fun neq(lhs: Any?, rhs: Any?) = !eql(lhs, rhs)
}