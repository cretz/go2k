package go2k.runtime

interface GoStruct {
    fun `$fieldVals`(): Map<String, Any?>

    fun `$eql`(other: GoStruct): Boolean {
        val myVals = `$fieldVals`()
        val otherVals = other.`$fieldVals`()
        return myVals.size == otherVals.size && myVals.all { (k, v) ->
            otherVals.containsKey(k) && Ops.eql(v, otherVals[k])
        }
    }

    object Empty {
        fun `$copy`() = this
    }

    companion object {
        fun Empty() = Empty
    }
}