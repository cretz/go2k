package go2k.runtime

@ExperimentalUnsignedTypes
interface Slice<T> {

    suspend fun cap(): Int
    suspend fun copyTo(slice: Slice<T>): Int
    suspend fun len(): Int
    suspend fun slice(low: Int, high: Int?, max: Int?): Slice<T>

    interface Factory {
        fun <T> new(arr: Array<T>, low: Int, high: Int, max: Int): Slice<T>
        fun new(arr: ByteArray, low: Int, high: Int, max: Int): Slice<Byte>
        fun new(arr: UByteArray, low: Int, high: Int, max: Int): Slice<UByte>
        fun new(arr: ShortArray, low: Int, high: Int, max: Int): Slice<Short>
        fun new(arr: UShortArray, low: Int, high: Int, max: Int): Slice<UShort>
        fun new(arr: IntArray, low: Int, high: Int, max: Int): Slice<Int>
        fun new(arr: UIntArray, low: Int, high: Int, max: Int): Slice<UInt>
        fun new(arr: LongArray, low: Int, high: Int, max: Int): Slice<Long>
        fun new(arr: ULongArray, low: Int, high: Int, max: Int): Slice<ULong>
        fun new(arr: FloatArray, low: Int, high: Int, max: Int): Slice<Float>
        fun new(arr: DoubleArray, low: Int, high: Int, max: Int): Slice<Double>
        fun new(arr: BooleanArray, low: Int, high: Int, max: Int): Slice<Boolean>
        fun new(arr: CharArray, low: Int, high: Int, max: Int): Slice<Char>
    }

    abstract class ArrayBased<T>(
        protected val low: Int,
        protected val high: Int,
        protected val max: Int
    ) : Slice<T> {
        init {
            require(low in 0..high && high in 0..max && max <= arrSize)
        }

        protected abstract val arrObj: Any
        protected abstract val arrSize: Int
        protected abstract fun newInst(low: Int, high: Int, max: Int): ArrayBased<T>

        protected inline val cap get() = max - low
        override suspend fun cap() = cap

        override suspend fun copyTo(slice: Slice<T>): Int {
            slice as ArrayBased<T>
            val amount = if (len < slice.len) len else slice.len
            // TODO: this is no good for the unsigned ones, ref:
            //  https://github.com/Kotlin/KEEP/issues/135#issuecomment-410143162
            Platform.arrayCopy(arrObj, low, slice.arrObj, slice.low, amount)
            return amount
        }

        protected inline val len get() = high - low
        override suspend fun len() = len

        override suspend fun slice(low: Int, high: Int?, max: Int?): Slice<T> {
            val high = high ?: len
            val max = max ?: cap
            require(low in 0..high && max in high..cap)
            return newInst(this.low + low, this.low + high, this.low + max)
        }
        
        companion object : Factory {
            override fun <T> new(arr: Array<T>, low: Int, high: Int, max: Int) = ObjectArr(arr, low, high, max)
            override fun new(arr: ByteArray, low: Int, high: Int, max: Int) = ByteArr(arr, low, high, max)
            override fun new(arr: UByteArray, low: Int, high: Int, max: Int) = UByteArr(arr, low, high, max)
            override fun new(arr: ShortArray, low: Int, high: Int, max: Int) = ShortArr(arr, low, high, max)
            override fun new(arr: UShortArray, low: Int, high: Int, max: Int) = UShortArr(arr, low, high, max)
            override fun new(arr: IntArray, low: Int, high: Int, max: Int) = IntArr(arr, low, high, max)
            override fun new(arr: UIntArray, low: Int, high: Int, max: Int) = UIntArr(arr, low, high, max)
            override fun new(arr: LongArray, low: Int, high: Int, max: Int) = LongArr(arr, low, high, max)
            override fun new(arr: ULongArray, low: Int, high: Int, max: Int) = ULongArr(arr, low, high, max)
            override fun new(arr: FloatArray, low: Int, high: Int, max: Int) = FloatArr(arr, low, high, max)
            override fun new(arr: DoubleArray, low: Int, high: Int, max: Int) = DoubleArr(arr, low, high, max)
            override fun new(arr: BooleanArray, low: Int, high: Int, max: Int) = BooleanArr(arr, low, high, max)
            override fun new(arr: CharArray, low: Int, high: Int, max: Int) = CharArr(arr, low, high, max)
        }
    }

    open class ObjectArr<T>(val array: Array<T>, low: Int, high: Int, max: Int) : ArrayBased<T>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = ObjectArr(array, low, high, max)
    }

    open class ByteArr(val array: ByteArray, low: Int, high: Int, max: Int) : ArrayBased<Byte>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = ByteArr(array, low, high, max)
    }

    open class UByteArr(val array: UByteArray, low: Int, high: Int, max: Int) : ArrayBased<UByte>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = UByteArr(array, low, high, max)
    }

    open class ShortArr(val array: ShortArray, low: Int, high: Int, max: Int) : ArrayBased<Short>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = ShortArr(array, low, high, max)
    }

    open class UShortArr(val array: UShortArray, low: Int, high: Int, max: Int) : ArrayBased<UShort>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = UShortArr(array, low, high, max)
    }

    open class IntArr(val array: IntArray, low: Int, high: Int, max: Int) : ArrayBased<Int>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = IntArr(array, low, high, max)
    }

    open class UIntArr(val array: UIntArray, low: Int, high: Int, max: Int) : ArrayBased<UInt>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = UIntArr(array, low, high, max)
    }

    open class LongArr(val array: LongArray, low: Int, high: Int, max: Int) : ArrayBased<Long>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = LongArr(array, low, high, max)
    }

    open class ULongArr(val array: ULongArray, low: Int, high: Int, max: Int) : ArrayBased<ULong>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = ULongArr(array, low, high, max)
    }

    open class FloatArr(val array: FloatArray, low: Int, high: Int, max: Int) : ArrayBased<Float>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = FloatArr(array, low, high, max)
    }

    open class DoubleArr(val array: DoubleArray, low: Int, high: Int, max: Int) : ArrayBased<Double>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = DoubleArr(array, low, high, max)
    }

    open class BooleanArr(val array: BooleanArray, low: Int, high: Int, max: Int) : ArrayBased<Boolean>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = BooleanArr(array, low, high, max)
    }

    open class CharArr(val array: CharArray, low: Int, high: Int, max: Int) : ArrayBased<Char>(low, high, max) {
        override val arrObj get() = array
        override val arrSize get() = array.size
        override fun newInst(low: Int, high: Int, max: Int) = CharArr(array, low, high, max)
    }
}