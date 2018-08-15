package go2k.compile.dumppb

data class ConstantValue(
    val value: Value? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ConstantValue> {
    sealed class Value {
        data class Unknown(val unknown: String = "") : Value()
        data class Bool(val bool: Boolean = false) : Value()
        data class String_(val string: String = "") : Value()
        data class Int_(val int: String = "") : Value()
        data class Float_(val float: String = "") : Value()
        data class Complex(val complex: String = "") : Value()
    }

    override operator fun plus(other: ConstantValue?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ConstantValue> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ConstantValue.protoUnmarshalImpl(u)
    }
}

data class TypeRef(
    val id: Int = 0,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeRef> {
    override operator fun plus(other: TypeRef?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeRef> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeRef.protoUnmarshalImpl(u)
    }
}

data class Type_(
    val `package`: String = "",
    val name: String = "",
    val type: Type? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Type_> {
    sealed class Type {
        data class TypeArray(val typeArray: go2k.compile.dumppb.TypeArray) : Type()
        data class TypeBasic(val typeBasic: go2k.compile.dumppb.TypeBasic) : Type()
        data class TypeBuiltin(val typeBuiltin: Boolean = false) : Type()
        data class TypeChan(val typeChan: go2k.compile.dumppb.TypeChan) : Type()
        data class TypeConst(val typeConst: go2k.compile.dumppb.TypeConst) : Type()
        data class TypeFunc(val typeFunc: go2k.compile.dumppb.TypeSignature) : Type()
        data class TypeInterface(val typeInterface: go2k.compile.dumppb.TypeInterface) : Type()
        data class TypeLabel(val typeLabel: go2k.compile.dumppb.TypeRef) : Type()
        data class TypeMap(val typeMap: go2k.compile.dumppb.TypeMap) : Type()
        data class TypeName(val typeName: go2k.compile.dumppb.TypeRef) : Type()
        data class TypeNamed(val typeNamed: go2k.compile.dumppb.TypeNamed) : Type()
        data class TypeNil(val typeNil: go2k.compile.dumppb.TypeRef) : Type()
        data class TypePackage(val typePackage: Boolean = false) : Type()
        data class TypePointer(val typePointer: go2k.compile.dumppb.TypePointer) : Type()
        data class TypeSignature(val typeSignature: go2k.compile.dumppb.TypeSignature) : Type()
        data class TypeSlice(val typeSlice: go2k.compile.dumppb.TypeSlice) : Type()
        data class TypeStruct(val typeStruct: go2k.compile.dumppb.TypeStruct) : Type()
        data class TypeTuple(val typeTuple: go2k.compile.dumppb.TypeTuple) : Type()
        data class TypeVar(val typeVar: go2k.compile.dumppb.TypeRef) : Type()
    }

    override operator fun plus(other: Type_?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Type_> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Type_.protoUnmarshalImpl(u)
    }
}

data class TypeArray(
    val elem: go2k.compile.dumppb.TypeRef? = null,
    val len: Long = 0L,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeArray> {
    override operator fun plus(other: TypeArray?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeArray> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeArray.protoUnmarshalImpl(u)
    }
}

data class TypeBasic(
    val flags: Int = 0,
    val kind: go2k.compile.dumppb.TypeBasic.Kind = go2k.compile.dumppb.TypeBasic.Kind.fromValue(0),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeBasic> {
    override operator fun plus(other: TypeBasic?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeBasic> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeBasic.protoUnmarshalImpl(u)
    }

    data class Kind(override val value: Int) : pbandk.Message.Enum {
        companion object : pbandk.Message.Enum.Companion<Kind> {
            val INVALID = Kind(0)
            val BOOL = Kind(1)
            val INT = Kind(2)
            val INT_8 = Kind(3)
            val INT_16 = Kind(4)
            val INT_32 = Kind(5)
            val INT_64 = Kind(6)
            val UINT = Kind(7)
            val UINT_8 = Kind(8)
            val UINT_16 = Kind(9)
            val UINT_32 = Kind(10)
            val UINT_64 = Kind(11)
            val UINT_PTR = Kind(12)
            val FLOAT_32 = Kind(13)
            val FLOAT_64 = Kind(14)
            val COMPLEX_64 = Kind(15)
            val COMPLEX_128 = Kind(16)
            val STRING = Kind(17)
            val UNSAFE_POINTER = Kind(18)
            val UNTYPED_BOOL = Kind(19)
            val UNTYPED_INT = Kind(20)
            val UNTYPED_RUNE = Kind(21)
            val UNTYPED_FLOAT = Kind(22)
            val UNTYPED_COMPLEX = Kind(23)
            val UNTYPED_STRING = Kind(24)
            val UNTYPED_NIL = Kind(25)

            override fun fromValue(value: Int) = when (value) {
                0 -> INVALID
                1 -> BOOL
                2 -> INT
                3 -> INT_8
                4 -> INT_16
                5 -> INT_32
                6 -> INT_64
                7 -> UINT
                8 -> UINT_8
                9 -> UINT_16
                10 -> UINT_32
                11 -> UINT_64
                12 -> UINT_PTR
                13 -> FLOAT_32
                14 -> FLOAT_64
                15 -> COMPLEX_64
                16 -> COMPLEX_128
                17 -> STRING
                18 -> UNSAFE_POINTER
                19 -> UNTYPED_BOOL
                20 -> UNTYPED_INT
                21 -> UNTYPED_RUNE
                22 -> UNTYPED_FLOAT
                23 -> UNTYPED_COMPLEX
                24 -> UNTYPED_STRING
                25 -> UNTYPED_NIL
                else -> Kind(value)
            }
        }
    }
}

data class TypeChan(
    val elem: go2k.compile.dumppb.TypeRef? = null,
    val sendDir: Boolean = false,
    val recvDir: Boolean = false,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeChan> {
    override operator fun plus(other: TypeChan?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeChan> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeChan.protoUnmarshalImpl(u)
    }
}

data class TypeConst(
    val type: go2k.compile.dumppb.TypeRef? = null,
    val value: go2k.compile.dumppb.ConstantValue? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeConst> {
    override operator fun plus(other: TypeConst?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeConst> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeConst.protoUnmarshalImpl(u)
    }
}

data class TypeInterface(
    val explicitMethods: List<go2k.compile.dumppb.TypeRef> = emptyList(),
    val embedded: List<go2k.compile.dumppb.TypeRef> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeInterface> {
    override operator fun plus(other: TypeInterface?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeInterface> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeInterface.protoUnmarshalImpl(u)
    }
}

data class TypeMap(
    val elem: go2k.compile.dumppb.TypeRef? = null,
    val key: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeMap> {
    override operator fun plus(other: TypeMap?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeMap> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeMap.protoUnmarshalImpl(u)
    }
}

data class TypeNamed(
    val typeName: go2k.compile.dumppb.TypeRef? = null,
    val type: go2k.compile.dumppb.TypeRef? = null,
    val methods: List<go2k.compile.dumppb.TypeRef> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeNamed> {
    override operator fun plus(other: TypeNamed?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeNamed> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeNamed.protoUnmarshalImpl(u)
    }
}

data class TypePointer(
    val elem: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypePointer> {
    override operator fun plus(other: TypePointer?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypePointer> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypePointer.protoUnmarshalImpl(u)
    }
}

data class TypeSignature(
    val recv: go2k.compile.dumppb.TypeRef? = null,
    val params: List<go2k.compile.dumppb.TypeRef> = emptyList(),
    val results: List<go2k.compile.dumppb.TypeRef> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeSignature> {
    override operator fun plus(other: TypeSignature?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeSignature> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeSignature.protoUnmarshalImpl(u)
    }
}

data class TypeSlice(
    val elem: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeSlice> {
    override operator fun plus(other: TypeSlice?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeSlice> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeSlice.protoUnmarshalImpl(u)
    }
}

data class TypeStruct(
    val fields: List<go2k.compile.dumppb.TypeRef> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeStruct> {
    override operator fun plus(other: TypeStruct?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeStruct> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeStruct.protoUnmarshalImpl(u)
    }
}

data class TypeTuple(
    val vars: List<go2k.compile.dumppb.TypeRef> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeTuple> {
    override operator fun plus(other: TypeTuple?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeTuple> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeTuple.protoUnmarshalImpl(u)
    }
}

private fun ConstantValue.protoMergeImpl(plus: ConstantValue?): ConstantValue = plus?.copy(
    value = plus.value ?: value,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ConstantValue.protoSizeImpl(): Int {
    var protoSize = 0
    when (value) {
        is ConstantValue.Value.Unknown -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(value.unknown)
        is ConstantValue.Value.Bool -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.boolSize(value.bool)
        is ConstantValue.Value.String_ -> protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(value.string)
        is ConstantValue.Value.Int_ -> protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.stringSize(value.int)
        is ConstantValue.Value.Float_ -> protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.stringSize(value.float)
        is ConstantValue.Value.Complex -> protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.stringSize(value.complex)
    }
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ConstantValue.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (value is ConstantValue.Value.Unknown) protoMarshal.writeTag(10).writeString(value.unknown)
    if (value is ConstantValue.Value.Bool) protoMarshal.writeTag(16).writeBool(value.bool)
    if (value is ConstantValue.Value.String_) protoMarshal.writeTag(26).writeString(value.string)
    if (value is ConstantValue.Value.Int_) protoMarshal.writeTag(34).writeString(value.int)
    if (value is ConstantValue.Value.Float_) protoMarshal.writeTag(42).writeString(value.float)
    if (value is ConstantValue.Value.Complex) protoMarshal.writeTag(50).writeString(value.complex)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ConstantValue.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ConstantValue {
    var value: ConstantValue.Value? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ConstantValue(value, protoUnmarshal.unknownFields())
        10 -> value = ConstantValue.Value.Unknown(protoUnmarshal.readString())
        16 -> value = ConstantValue.Value.Bool(protoUnmarshal.readBool())
        26 -> value = ConstantValue.Value.String_(protoUnmarshal.readString())
        34 -> value = ConstantValue.Value.Int_(protoUnmarshal.readString())
        42 -> value = ConstantValue.Value.Float_(protoUnmarshal.readString())
        50 -> value = ConstantValue.Value.Complex(protoUnmarshal.readString())
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeRef.protoMergeImpl(plus: TypeRef?): TypeRef = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeRef.protoSizeImpl(): Int {
    var protoSize = 0
    if (id != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.uInt32Size(id)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeRef.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (id != 0) protoMarshal.writeTag(8).writeUInt32(id)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeRef.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeRef {
    var id = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeRef(id, protoUnmarshal.unknownFields())
        8 -> id = protoUnmarshal.readUInt32()
        else -> protoUnmarshal.unknownField()
    }
}

private fun Type_.protoMergeImpl(plus: Type_?): Type_ = plus?.copy(
    type = when {
        type is Type_.Type.TypeArray && plus.type is Type_.Type.TypeArray ->
            Type_.Type.TypeArray(type.typeArray + plus.type.typeArray)
        type is Type_.Type.TypeBasic && plus.type is Type_.Type.TypeBasic ->
            Type_.Type.TypeBasic(type.typeBasic + plus.type.typeBasic)
        type is Type_.Type.TypeChan && plus.type is Type_.Type.TypeChan ->
            Type_.Type.TypeChan(type.typeChan + plus.type.typeChan)
        type is Type_.Type.TypeConst && plus.type is Type_.Type.TypeConst ->
            Type_.Type.TypeConst(type.typeConst + plus.type.typeConst)
        type is Type_.Type.TypeFunc && plus.type is Type_.Type.TypeFunc ->
            Type_.Type.TypeFunc(type.typeFunc + plus.type.typeFunc)
        type is Type_.Type.TypeInterface && plus.type is Type_.Type.TypeInterface ->
            Type_.Type.TypeInterface(type.typeInterface + plus.type.typeInterface)
        type is Type_.Type.TypeLabel && plus.type is Type_.Type.TypeLabel ->
            Type_.Type.TypeLabel(type.typeLabel + plus.type.typeLabel)
        type is Type_.Type.TypeMap && plus.type is Type_.Type.TypeMap ->
            Type_.Type.TypeMap(type.typeMap + plus.type.typeMap)
        type is Type_.Type.TypeName && plus.type is Type_.Type.TypeName ->
            Type_.Type.TypeName(type.typeName + plus.type.typeName)
        type is Type_.Type.TypeNamed && plus.type is Type_.Type.TypeNamed ->
            Type_.Type.TypeNamed(type.typeNamed + plus.type.typeNamed)
        type is Type_.Type.TypeNil && plus.type is Type_.Type.TypeNil ->
            Type_.Type.TypeNil(type.typeNil + plus.type.typeNil)
        type is Type_.Type.TypePointer && plus.type is Type_.Type.TypePointer ->
            Type_.Type.TypePointer(type.typePointer + plus.type.typePointer)
        type is Type_.Type.TypeSignature && plus.type is Type_.Type.TypeSignature ->
            Type_.Type.TypeSignature(type.typeSignature + plus.type.typeSignature)
        type is Type_.Type.TypeSlice && plus.type is Type_.Type.TypeSlice ->
            Type_.Type.TypeSlice(type.typeSlice + plus.type.typeSlice)
        type is Type_.Type.TypeStruct && plus.type is Type_.Type.TypeStruct ->
            Type_.Type.TypeStruct(type.typeStruct + plus.type.typeStruct)
        type is Type_.Type.TypeTuple && plus.type is Type_.Type.TypeTuple ->
            Type_.Type.TypeTuple(type.typeTuple + plus.type.typeTuple)
        type is Type_.Type.TypeVar && plus.type is Type_.Type.TypeVar ->
            Type_.Type.TypeVar(type.typeVar + plus.type.typeVar)
        else ->
            plus.type ?: type
    },
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Type_.protoSizeImpl(): Int {
    var protoSize = 0
    if (`package`.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(`package`)
    if (name.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(name)
    when (type) {
        is Type_.Type.TypeArray -> protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(type.typeArray)
        is Type_.Type.TypeBasic -> protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(type.typeBasic)
        is Type_.Type.TypeBuiltin -> protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.boolSize(type.typeBuiltin)
        is Type_.Type.TypeChan -> protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.messageSize(type.typeChan)
        is Type_.Type.TypeConst -> protoSize += pbandk.Sizer.tagSize(7) + pbandk.Sizer.messageSize(type.typeConst)
        is Type_.Type.TypeFunc -> protoSize += pbandk.Sizer.tagSize(8) + pbandk.Sizer.messageSize(type.typeFunc)
        is Type_.Type.TypeInterface -> protoSize += pbandk.Sizer.tagSize(9) + pbandk.Sizer.messageSize(type.typeInterface)
        is Type_.Type.TypeLabel -> protoSize += pbandk.Sizer.tagSize(10) + pbandk.Sizer.messageSize(type.typeLabel)
        is Type_.Type.TypeMap -> protoSize += pbandk.Sizer.tagSize(11) + pbandk.Sizer.messageSize(type.typeMap)
        is Type_.Type.TypeName -> protoSize += pbandk.Sizer.tagSize(12) + pbandk.Sizer.messageSize(type.typeName)
        is Type_.Type.TypeNamed -> protoSize += pbandk.Sizer.tagSize(13) + pbandk.Sizer.messageSize(type.typeNamed)
        is Type_.Type.TypeNil -> protoSize += pbandk.Sizer.tagSize(14) + pbandk.Sizer.messageSize(type.typeNil)
        is Type_.Type.TypePackage -> protoSize += pbandk.Sizer.tagSize(15) + pbandk.Sizer.boolSize(type.typePackage)
        is Type_.Type.TypePointer -> protoSize += pbandk.Sizer.tagSize(16) + pbandk.Sizer.messageSize(type.typePointer)
        is Type_.Type.TypeSignature -> protoSize += pbandk.Sizer.tagSize(17) + pbandk.Sizer.messageSize(type.typeSignature)
        is Type_.Type.TypeSlice -> protoSize += pbandk.Sizer.tagSize(18) + pbandk.Sizer.messageSize(type.typeSlice)
        is Type_.Type.TypeStruct -> protoSize += pbandk.Sizer.tagSize(19) + pbandk.Sizer.messageSize(type.typeStruct)
        is Type_.Type.TypeTuple -> protoSize += pbandk.Sizer.tagSize(20) + pbandk.Sizer.messageSize(type.typeTuple)
        is Type_.Type.TypeVar -> protoSize += pbandk.Sizer.tagSize(21) + pbandk.Sizer.messageSize(type.typeVar)
    }
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Type_.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (`package`.isNotEmpty()) protoMarshal.writeTag(10).writeString(`package`)
    if (name.isNotEmpty()) protoMarshal.writeTag(18).writeString(name)
    if (type is Type_.Type.TypeArray) protoMarshal.writeTag(26).writeMessage(type.typeArray)
    if (type is Type_.Type.TypeBasic) protoMarshal.writeTag(34).writeMessage(type.typeBasic)
    if (type is Type_.Type.TypeBuiltin) protoMarshal.writeTag(40).writeBool(type.typeBuiltin)
    if (type is Type_.Type.TypeChan) protoMarshal.writeTag(50).writeMessage(type.typeChan)
    if (type is Type_.Type.TypeConst) protoMarshal.writeTag(58).writeMessage(type.typeConst)
    if (type is Type_.Type.TypeFunc) protoMarshal.writeTag(66).writeMessage(type.typeFunc)
    if (type is Type_.Type.TypeInterface) protoMarshal.writeTag(74).writeMessage(type.typeInterface)
    if (type is Type_.Type.TypeLabel) protoMarshal.writeTag(82).writeMessage(type.typeLabel)
    if (type is Type_.Type.TypeMap) protoMarshal.writeTag(90).writeMessage(type.typeMap)
    if (type is Type_.Type.TypeName) protoMarshal.writeTag(98).writeMessage(type.typeName)
    if (type is Type_.Type.TypeNamed) protoMarshal.writeTag(106).writeMessage(type.typeNamed)
    if (type is Type_.Type.TypeNil) protoMarshal.writeTag(114).writeMessage(type.typeNil)
    if (type is Type_.Type.TypePackage) protoMarshal.writeTag(120).writeBool(type.typePackage)
    if (type is Type_.Type.TypePointer) protoMarshal.writeTag(130).writeMessage(type.typePointer)
    if (type is Type_.Type.TypeSignature) protoMarshal.writeTag(138).writeMessage(type.typeSignature)
    if (type is Type_.Type.TypeSlice) protoMarshal.writeTag(146).writeMessage(type.typeSlice)
    if (type is Type_.Type.TypeStruct) protoMarshal.writeTag(154).writeMessage(type.typeStruct)
    if (type is Type_.Type.TypeTuple) protoMarshal.writeTag(162).writeMessage(type.typeTuple)
    if (type is Type_.Type.TypeVar) protoMarshal.writeTag(170).writeMessage(type.typeVar)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Type_.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Type_ {
    var `package` = ""
    var name = ""
    var type: Type_.Type? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Type_(`package`, name, type, protoUnmarshal.unknownFields())
        10 -> `package` = protoUnmarshal.readString()
        18 -> name = protoUnmarshal.readString()
        26 -> type = Type_.Type.TypeArray(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeArray.Companion))
        34 -> type = Type_.Type.TypeBasic(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeBasic.Companion))
        40 -> type = Type_.Type.TypeBuiltin(protoUnmarshal.readBool())
        50 -> type = Type_.Type.TypeChan(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeChan.Companion))
        58 -> type = Type_.Type.TypeConst(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeConst.Companion))
        66 -> type = Type_.Type.TypeFunc(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeSignature.Companion))
        74 -> type = Type_.Type.TypeInterface(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInterface.Companion))
        82 -> type = Type_.Type.TypeLabel(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion))
        90 -> type = Type_.Type.TypeMap(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeMap.Companion))
        98 -> type = Type_.Type.TypeName(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion))
        106 -> type = Type_.Type.TypeNamed(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeNamed.Companion))
        114 -> type = Type_.Type.TypeNil(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion))
        120 -> type = Type_.Type.TypePackage(protoUnmarshal.readBool())
        130 -> type = Type_.Type.TypePointer(protoUnmarshal.readMessage(go2k.compile.dumppb.TypePointer.Companion))
        138 -> type = Type_.Type.TypeSignature(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeSignature.Companion))
        146 -> type = Type_.Type.TypeSlice(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeSlice.Companion))
        154 -> type = Type_.Type.TypeStruct(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeStruct.Companion))
        162 -> type = Type_.Type.TypeTuple(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeTuple.Companion))
        170 -> type = Type_.Type.TypeVar(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion))
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeArray.protoMergeImpl(plus: TypeArray?): TypeArray = plus?.copy(
    elem = elem?.plus(plus.elem) ?: plus.elem,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeArray.protoSizeImpl(): Int {
    var protoSize = 0
    if (elem != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(elem)
    if (len != 0L) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int64Size(len)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeArray.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (elem != null) protoMarshal.writeTag(10).writeMessage(elem)
    if (len != 0L) protoMarshal.writeTag(16).writeInt64(len)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeArray.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeArray {
    var elem: go2k.compile.dumppb.TypeRef? = null
    var len = 0L
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeArray(elem, len, protoUnmarshal.unknownFields())
        10 -> elem = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        16 -> len = protoUnmarshal.readInt64()
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeBasic.protoMergeImpl(plus: TypeBasic?): TypeBasic = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeBasic.protoSizeImpl(): Int {
    var protoSize = 0
    if (flags != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(flags)
    if (kind.value != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(kind)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeBasic.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (flags != 0) protoMarshal.writeTag(8).writeInt32(flags)
    if (kind.value != 0) protoMarshal.writeTag(16).writeEnum(kind)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeBasic.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeBasic {
    var flags = 0
    var kind: go2k.compile.dumppb.TypeBasic.Kind = go2k.compile.dumppb.TypeBasic.Kind.fromValue(0)
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeBasic(flags, kind, protoUnmarshal.unknownFields())
        8 -> flags = protoUnmarshal.readInt32()
        16 -> kind = protoUnmarshal.readEnum(go2k.compile.dumppb.TypeBasic.Kind.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeChan.protoMergeImpl(plus: TypeChan?): TypeChan = plus?.copy(
    elem = elem?.plus(plus.elem) ?: plus.elem,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeChan.protoSizeImpl(): Int {
    var protoSize = 0
    if (elem != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(elem)
    if (sendDir) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.boolSize(sendDir)
    if (recvDir) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.boolSize(recvDir)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeChan.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (elem != null) protoMarshal.writeTag(10).writeMessage(elem)
    if (sendDir) protoMarshal.writeTag(16).writeBool(sendDir)
    if (recvDir) protoMarshal.writeTag(24).writeBool(recvDir)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeChan.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeChan {
    var elem: go2k.compile.dumppb.TypeRef? = null
    var sendDir = false
    var recvDir = false
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeChan(elem, sendDir, recvDir, protoUnmarshal.unknownFields())
        10 -> elem = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        16 -> sendDir = protoUnmarshal.readBool()
        24 -> recvDir = protoUnmarshal.readBool()
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeConst.protoMergeImpl(plus: TypeConst?): TypeConst = plus?.copy(
    type = type?.plus(plus.type) ?: plus.type,
    value = value?.plus(plus.value) ?: plus.value,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeConst.protoSizeImpl(): Int {
    var protoSize = 0
    if (type != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(type)
    if (value != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(value)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeConst.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (type != null) protoMarshal.writeTag(10).writeMessage(type)
    if (value != null) protoMarshal.writeTag(18).writeMessage(value)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeConst.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeConst {
    var type: go2k.compile.dumppb.TypeRef? = null
    var value: go2k.compile.dumppb.ConstantValue? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeConst(type, value, protoUnmarshal.unknownFields())
        10 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        18 -> value = protoUnmarshal.readMessage(go2k.compile.dumppb.ConstantValue.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeInterface.protoMergeImpl(plus: TypeInterface?): TypeInterface = plus?.copy(
    explicitMethods = explicitMethods + plus.explicitMethods,
    embedded = embedded + plus.embedded,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeInterface.protoSizeImpl(): Int {
    var protoSize = 0
    if (explicitMethods.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(1) * explicitMethods.size) + explicitMethods.sumBy(pbandk.Sizer::messageSize)
    if (embedded.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * embedded.size) + embedded.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeInterface.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (explicitMethods.isNotEmpty()) explicitMethods.forEach { protoMarshal.writeTag(10).writeMessage(it) }
    if (embedded.isNotEmpty()) embedded.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeInterface.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeInterface {
    var explicitMethods: pbandk.ListWithSize.Builder<go2k.compile.dumppb.TypeRef>? = null
    var embedded: pbandk.ListWithSize.Builder<go2k.compile.dumppb.TypeRef>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeInterface(pbandk.ListWithSize.Builder.fixed(explicitMethods), pbandk.ListWithSize.Builder.fixed(embedded), protoUnmarshal.unknownFields())
        10 -> explicitMethods = protoUnmarshal.readRepeatedMessage(explicitMethods, go2k.compile.dumppb.TypeRef.Companion, true)
        18 -> embedded = protoUnmarshal.readRepeatedMessage(embedded, go2k.compile.dumppb.TypeRef.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeMap.protoMergeImpl(plus: TypeMap?): TypeMap = plus?.copy(
    elem = elem?.plus(plus.elem) ?: plus.elem,
    key = key?.plus(plus.key) ?: plus.key,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeMap.protoSizeImpl(): Int {
    var protoSize = 0
    if (elem != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(elem)
    if (key != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(key)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeMap.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (elem != null) protoMarshal.writeTag(10).writeMessage(elem)
    if (key != null) protoMarshal.writeTag(18).writeMessage(key)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeMap.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeMap {
    var elem: go2k.compile.dumppb.TypeRef? = null
    var key: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeMap(elem, key, protoUnmarshal.unknownFields())
        10 -> elem = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        18 -> key = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeNamed.protoMergeImpl(plus: TypeNamed?): TypeNamed = plus?.copy(
    typeName = typeName?.plus(plus.typeName) ?: plus.typeName,
    type = type?.plus(plus.type) ?: plus.type,
    methods = methods + plus.methods,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeNamed.protoSizeImpl(): Int {
    var protoSize = 0
    if (typeName != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(typeName)
    if (type != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(type)
    if (methods.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(3) * methods.size) + methods.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeNamed.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (typeName != null) protoMarshal.writeTag(10).writeMessage(typeName)
    if (type != null) protoMarshal.writeTag(18).writeMessage(type)
    if (methods.isNotEmpty()) methods.forEach { protoMarshal.writeTag(26).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeNamed.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeNamed {
    var typeName: go2k.compile.dumppb.TypeRef? = null
    var type: go2k.compile.dumppb.TypeRef? = null
    var methods: pbandk.ListWithSize.Builder<go2k.compile.dumppb.TypeRef>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeNamed(typeName, type, pbandk.ListWithSize.Builder.fixed(methods), protoUnmarshal.unknownFields())
        10 -> typeName = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        18 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        26 -> methods = protoUnmarshal.readRepeatedMessage(methods, go2k.compile.dumppb.TypeRef.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypePointer.protoMergeImpl(plus: TypePointer?): TypePointer = plus?.copy(
    elem = elem?.plus(plus.elem) ?: plus.elem,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypePointer.protoSizeImpl(): Int {
    var protoSize = 0
    if (elem != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(elem)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypePointer.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (elem != null) protoMarshal.writeTag(10).writeMessage(elem)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypePointer.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypePointer {
    var elem: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypePointer(elem, protoUnmarshal.unknownFields())
        10 -> elem = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeSignature.protoMergeImpl(plus: TypeSignature?): TypeSignature = plus?.copy(
    recv = recv?.plus(plus.recv) ?: plus.recv,
    params = params + plus.params,
    results = results + plus.results,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeSignature.protoSizeImpl(): Int {
    var protoSize = 0
    if (recv != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(recv)
    if (params.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * params.size) + params.sumBy(pbandk.Sizer::messageSize)
    if (results.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(3) * results.size) + results.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeSignature.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (recv != null) protoMarshal.writeTag(10).writeMessage(recv)
    if (params.isNotEmpty()) params.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (results.isNotEmpty()) results.forEach { protoMarshal.writeTag(26).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeSignature.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeSignature {
    var recv: go2k.compile.dumppb.TypeRef? = null
    var params: pbandk.ListWithSize.Builder<go2k.compile.dumppb.TypeRef>? = null
    var results: pbandk.ListWithSize.Builder<go2k.compile.dumppb.TypeRef>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeSignature(recv, pbandk.ListWithSize.Builder.fixed(params), pbandk.ListWithSize.Builder.fixed(results), protoUnmarshal.unknownFields())
        10 -> recv = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        18 -> params = protoUnmarshal.readRepeatedMessage(params, go2k.compile.dumppb.TypeRef.Companion, true)
        26 -> results = protoUnmarshal.readRepeatedMessage(results, go2k.compile.dumppb.TypeRef.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeSlice.protoMergeImpl(plus: TypeSlice?): TypeSlice = plus?.copy(
    elem = elem?.plus(plus.elem) ?: plus.elem,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeSlice.protoSizeImpl(): Int {
    var protoSize = 0
    if (elem != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(elem)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeSlice.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (elem != null) protoMarshal.writeTag(10).writeMessage(elem)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeSlice.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeSlice {
    var elem: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeSlice(elem, protoUnmarshal.unknownFields())
        10 -> elem = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeStruct.protoMergeImpl(plus: TypeStruct?): TypeStruct = plus?.copy(
    fields = fields + plus.fields,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeStruct.protoSizeImpl(): Int {
    var protoSize = 0
    if (fields.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(1) * fields.size) + fields.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeStruct.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (fields.isNotEmpty()) fields.forEach { protoMarshal.writeTag(10).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeStruct.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeStruct {
    var fields: pbandk.ListWithSize.Builder<go2k.compile.dumppb.TypeRef>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeStruct(pbandk.ListWithSize.Builder.fixed(fields), protoUnmarshal.unknownFields())
        10 -> fields = protoUnmarshal.readRepeatedMessage(fields, go2k.compile.dumppb.TypeRef.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeTuple.protoMergeImpl(plus: TypeTuple?): TypeTuple = plus?.copy(
    vars = vars + plus.vars,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeTuple.protoSizeImpl(): Int {
    var protoSize = 0
    if (vars.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(1) * vars.size) + vars.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeTuple.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (vars.isNotEmpty()) vars.forEach { protoMarshal.writeTag(10).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeTuple.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeTuple {
    var vars: pbandk.ListWithSize.Builder<go2k.compile.dumppb.TypeRef>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeTuple(pbandk.ListWithSize.Builder.fixed(vars), protoUnmarshal.unknownFields())
        10 -> vars = protoUnmarshal.readRepeatedMessage(vars, go2k.compile.dumppb.TypeRef.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}
