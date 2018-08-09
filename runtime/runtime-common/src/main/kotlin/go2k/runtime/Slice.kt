package go2k.runtime

@ExperimentalUnsignedTypes
interface Slice<T> {

    suspend fun append(other: Slice<T>): Slice<T>
    suspend fun cap(): Int
    suspend fun copyTo(slice: Slice<T>): Int
    suspend fun get(index: Int): T
    suspend fun len(): Int
    suspend fun slice(low: Int, high: Int?, max: Int?): Slice<T>

    interface Factory {
        fun <T> newObjectSlice(arr: Array<T>, low: Int, high: Int, max: Int): Slice<T>
        fun newByteSlice(arr: ByteArray, low: Int, high: Int, max: Int): Slice<Byte>
        fun newUByteSlice(arr: UByteArray, low: Int, high: Int, max: Int): Slice<UByte>
        fun newShortSlice(arr: ShortArray, low: Int, high: Int, max: Int): Slice<Short>
        fun newUShortSlice(arr: UShortArray, low: Int, high: Int, max: Int): Slice<UShort>
        fun newIntSlice(arr: IntArray, low: Int, high: Int, max: Int): Slice<Int>
        fun newUIntSlice(arr: UIntArray, low: Int, high: Int, max: Int): Slice<UInt>
        fun newLongSlice(arr: LongArray, low: Int, high: Int, max: Int): Slice<Long>
        fun newULongSlice(arr: ULongArray, low: Int, high: Int, max: Int): Slice<ULong>
        fun newFloatSlice(arr: FloatArray, low: Int, high: Int, max: Int): Slice<Float>
        fun newDoubleSlice(arr: DoubleArray, low: Int, high: Int, max: Int): Slice<Double>
        fun newBooleanSlice(arr: BooleanArray, low: Int, high: Int, max: Int): Slice<Boolean>
        fun newCharSlice(arr: CharArray, low: Int, high: Int, max: Int): Slice<Char>
    }

    abstract class ArrayBased<T, ARR : Any>(
        protected val array: ARR,
        protected val low: Int,
        protected var high: Int,
        protected val max: Int
    ) : Slice<T> {
        init {
            require(low in 0..high && high in 0..max && max <= arraySize)
        }

        protected abstract val arraySize: Int
        protected abstract fun newArray(size: Int): ARR
        protected abstract fun newInst(array: ARR, low: Int, high: Int, max: Int): ArrayBased<T, ARR>
        protected open fun arrayCopy(src: ARR, srcPos: Int, dest: ARR, destPos: Int, len: Int) {
            // TODO: this is no good for the unsigned ones, ref:
            //  https://github.com/Kotlin/KEEP/issues/135#issuecomment-410143162
            //  ...it's coming in the form of asWhateverArray
            Platform.arrayCopy(src, srcPos, dest, destPos, len)
        }

        override suspend fun append(other: Slice<T>): Slice<T> {
            val other = other as ArrayBased<T, ARR>
            val total = len + other.len
            if (total <= cap) {
                arrayCopy(other.array, other.low, array, high, other.len)
                high += other.len
                return this
            }
            val newSize = ((total * 3) / 2) + 1
            val newArrayInst = newArray(newSize)
            arrayCopy(array, low, newArrayInst, 0, len)
            arrayCopy(other.array, other.low, newArrayInst, len, other.len)
            return newInst(newArrayInst, 0, total, newSize)
        }

        protected inline val cap get() = max - low
        override suspend fun cap() = cap

        override suspend fun copyTo(slice: Slice<T>): Int {
            slice as ArrayBased<T, ARR>
            val amount = if (len < slice.len) len else slice.len
            arrayCopy(array, low, slice.array, slice.low, amount)
            return amount
        }

        protected inline val len get() = high - low
        override suspend fun len() = len

        override suspend fun slice(low: Int, high: Int?, max: Int?): Slice<T> {
            val high = high ?: len
            val max = max ?: cap
            require(low in 0..high && max in high..cap)
            return newInst(array, this.low + low, this.low + high, this.low + max)
        }
        
        companion object : Factory {
            override fun <T> newObjectSlice(arr: Array<T>, low: Int, high: Int, max: Int) =
                ObjectArr(arr, low, high, max)
            override fun newByteSlice(arr: ByteArray, low: Int, high: Int, max: Int) =
                ByteArr(arr, low, high, max)
            override fun newUByteSlice(arr: UByteArray, low: Int, high: Int, max: Int) =
                UByteArr(arr, low, high, max)
            override fun newShortSlice(arr: ShortArray, low: Int, high: Int, max: Int) =
                ShortArr(arr, low, high, max)
            override fun newUShortSlice(arr: UShortArray, low: Int, high: Int, max: Int) =
                UShortArr(arr, low, high, max)
            override fun newIntSlice(arr: IntArray, low: Int, high: Int, max: Int) =
                IntArr(arr, low, high, max)
            override fun newUIntSlice(arr: UIntArray, low: Int, high: Int, max: Int) =
                UIntArr(arr, low, high, max)
            override fun newLongSlice(arr: LongArray, low: Int, high: Int, max: Int) =
                LongArr(arr, low, high, max)
            override fun newULongSlice(arr: ULongArray, low: Int, high: Int, max: Int) =
                ULongArr(arr, low, high, max)
            override fun newFloatSlice(arr: FloatArray, low: Int, high: Int, max: Int) =
                FloatArr(arr, low, high, max)
            override fun newDoubleSlice(arr: DoubleArray, low: Int, high: Int, max: Int) =
                DoubleArr(arr, low, high, max)
            override fun newBooleanSlice(arr: BooleanArray, low: Int, high: Int, max: Int) =
                BooleanArr(arr, low, high, max)
            override fun newCharSlice(arr: CharArray, low: Int, high: Int, max: Int) =
                CharArr(arr, low, high, max)
        }
    }

    open class ObjectArr<T>(array: Array<T>, low: Int, high: Int, max: Int) : ArrayBased<T, Array<T>>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = arrayOfNulls<Any?>(size) as Array<T>
        override fun newInst(array: Array<T>, low: Int, high: Int, max: Int) = ObjectArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class ByteArr(array: ByteArray, low: Int, high: Int, max: Int) :
        ArrayBased<Byte, ByteArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = ByteArray(size)
        override fun newInst(array: ByteArray, low: Int, high: Int, max: Int) = ByteArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class UByteArr(array: UByteArray, low: Int, high: Int, max: Int) :
        ArrayBased<UByte, UByteArray>(array, low, high, max) {
        override val arraySize get() = array.size
        // TODO: in a newer version of Kotlin, we'll be able to create a byte array of size and turn it
        override fun newArray(size: Int) = UByteArray(size) { 0.toUByte() }
        override fun newInst(array: UByteArray, low: Int, high: Int, max: Int) = UByteArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class ShortArr(array: ShortArray, low: Int, high: Int, max: Int) :
        ArrayBased<Short, ShortArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = ShortArray(size)
        override fun newInst(array: ShortArray, low: Int, high: Int, max: Int) = ShortArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class UShortArr(array: UShortArray, low: Int, high: Int, max: Int) :
        ArrayBased<UShort, UShortArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = UShortArray(size) { 0.toUShort() }
        override fun newInst(array: UShortArray, low: Int, high: Int, max: Int) = UShortArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class IntArr(array: IntArray, low: Int, high: Int, max: Int) :
        ArrayBased<Int, IntArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = IntArray(size)
        override fun newInst(array: IntArray, low: Int, high: Int, max: Int) = IntArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class UIntArr(array: UIntArray, low: Int, high: Int, max: Int) :
        ArrayBased<UInt, UIntArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = UIntArray(size) { 0.toUInt() }
        override fun newInst(array: UIntArray, low: Int, high: Int, max: Int) = UIntArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class LongArr(array: LongArray, low: Int, high: Int, max: Int) :
        ArrayBased<Long, LongArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = LongArray(size)
        override fun newInst(array: LongArray, low: Int, high: Int, max: Int) = LongArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class ULongArr(array: ULongArray, low: Int, high: Int, max: Int) :
        ArrayBased<ULong, ULongArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = ULongArray(size) { 0.toULong() }
        override fun newInst(array: ULongArray, low: Int, high: Int, max: Int) = ULongArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class FloatArr(array: FloatArray, low: Int, high: Int, max: Int) :
        ArrayBased<Float, FloatArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = FloatArray(size)
        override fun newInst(array: FloatArray, low: Int, high: Int, max: Int) = FloatArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class DoubleArr(array: DoubleArray, low: Int, high: Int, max: Int) :
        ArrayBased<Double, DoubleArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = DoubleArray(size)
        override fun newInst(array: DoubleArray, low: Int, high: Int, max: Int) = DoubleArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class BooleanArr(array: BooleanArray, low: Int, high: Int, max: Int) :
        ArrayBased<Boolean, BooleanArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = BooleanArray(size)
        override fun newInst(array: BooleanArray, low: Int, high: Int, max: Int) = BooleanArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }

    open class CharArr(array: CharArray, low: Int, high: Int, max: Int) :
        ArrayBased<Char, CharArray>(array, low, high, max) {
        override val arraySize get() = array.size
        override fun newArray(size: Int) = CharArray(size)
        override fun newInst(array: CharArray, low: Int, high: Int, max: Int) = CharArr(array, low, high, max)
        override suspend fun get(index: Int) = array[low + index]
    }
}