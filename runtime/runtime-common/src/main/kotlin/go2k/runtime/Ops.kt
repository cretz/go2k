package go2k.runtime

object Ops {
    fun eql(lhs: Any?, rhs: Any?): Boolean {
        // TODO: optimize of course
        if (lhs is GoInterface) return eql(lhs.v, rhs)
        if (rhs is GoInterface) return eql(lhs, rhs.v)
        if (lhs == rhs) return true
        if (lhs == null || rhs == null) return false
        // TODO: lots of checks required here...
        return false
    }

    inline fun neq(lhs: Any?, rhs: Any?) = !eql(lhs, rhs)
}