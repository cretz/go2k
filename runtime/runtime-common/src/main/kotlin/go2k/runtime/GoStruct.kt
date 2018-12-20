package go2k.runtime

interface GoStruct {

    object Empty {
        fun `$copy`() = this
    }

    companion object {
        fun Empty() = Empty
    }
}