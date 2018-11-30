package go2k.runtime

interface Map<K, V> : MutableMap<K, V> {
    override fun get(key: K): V { TODO() }

    interface Factory {
        fun <K, V> make(defaultValue: V, size: Int?): Map<K, V>
        fun <K, V> make(defaultValue: V, vararg pairs: Pair<K, V>): Map<K, V>
    }

    class WithDefault<K, V>(val underlying: MutableMap<K, V>, val defaultValue: V): Map<K, V>, MutableMap<K, V> by underlying {
        constructor(defaultValue: V, size: Int?) : this(if (size == null) HashMap() else HashMap(size), defaultValue)

        override fun get(key: K) = underlying[key] ?: defaultValue

        companion object : Factory {
            override inline fun <K, V> make(defaultValue: V, size: Int?) =
                WithDefault<K, V>(defaultValue, size)
            override inline fun <K, V> make(defaultValue: V, vararg pairs: Pair<K, V>) =
                WithDefault(hashMapOf(*pairs), defaultValue)
        }
    }
}