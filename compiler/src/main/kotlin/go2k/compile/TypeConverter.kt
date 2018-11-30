package go2k.compile

import go2k.compile.dumppb.Type_
import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

open class TypeConverter {

    fun Context.convertType(expr: Node.Expr, from: Type, to: Type): Node.Expr {
        //println("Converting local type from $from to $to (same? ${to == from})")
        return when (to) {
            from, Type.RawParamForBuiltIn -> expr
            is Type.Primitive -> when (from) {
                is Type.Primitive -> when (to.cls) {
                    from.cls -> expr
                    Byte::class -> call(expr.dot("toByte".javaName))
                    Short::class -> call(expr.dot("toShort".javaName))
                    Char::class -> call(expr.dot("toChar".javaName))
                    Int::class -> call(expr.dot("toInt".javaName))
                    Long::class -> call(expr.dot("toLong".javaName))
                    UBYTE_CLASS -> call(expr.dot("toUByte".javaName))
                    USHORT_CLASS -> call(expr.dot("toUShort".javaName))
                    UINT_CLASS -> call(expr.dot("toUInt".javaName))
                    ULONG_CLASS -> call(expr.dot("toULong".javaName))
                    BigInteger::class -> call(expr.dot("toBigInteger".javaName))
                    Float::class -> call(expr.dot("toFloat".javaName))
                    Double::class -> call(expr.dot("toDouble".javaName))
                    BigDecimal::class -> call(expr.dot("toBigDecimal".javaName))
                    else -> error("Unable to convert $from to $to")
                }
                is Type.Interface -> typeOp(
                    lhs = binaryOp(
                        lhs = expr.paren().dot("v".toName(), safe = true),
                        op = Node.Expr.BinaryOp.Token.ELVIS,
                        rhs = NullConst
                    ).paren(),
                    op = Node.Expr.TypeOp.Token.AS,
                    rhs = compileType(to.type)
                )
                else -> error("Unable to convert $from to $to")
            }
            is Type.Interface -> when (from) {
                is Type.Interface, is Type.Nil -> typeOp(
                    lhs = expr,
                    op = Node.Expr.TypeOp.Token.AS,
                    rhs = compileType(to.type)
                )
                is Type.Pointer, is Type.Primitive -> call(
                    expr = typeClassRef(to.type).dot("impl".toName()),
                    args = listOf(valueArg(expr = expr))
                )
                else -> error("Unable to convert $from to $to")
            }
            is Type.Nil -> expr
            is Type.Pointer -> {
                // For interfaces - expr?.v ?: null
                val lhs = if (from !is Type.Interface) expr else binaryOp(
                    lhs = expr.paren().dot("v".toName(), safe = true),
                    op = Node.Expr.BinaryOp.Token.ELVIS,
                    rhs = NullConst
                )
                typeOp(
                    lhs = lhs,
                    op = Node.Expr.TypeOp.Token.AS,
                    rhs = compileType(to.type)
                )
            }
            is Type.Slice -> when (from) {
                is Type.Nil -> typeOp(
                    lhs = expr,
                    op = Node.Expr.TypeOp.Token.AS,
                    rhs = compileType(to.type)
                )
                else -> TODO()
            }
            else -> error("Unable to convert $from to $to")
        }
    }

    fun Context.toConvType(v: Type_): Type = when (v.type) {
        is Type_.Type.TypeArray -> Type.Array(v, toConvType(v.type.typeArray.elem!!.namedType), v.type.typeArray.len)
        is Type_.Type.TypeBasic -> Type.Primitive(v, v.kotlinPrimitiveType() ?: error("Can't get primitive from $v"))
        is Type_.Type.TypeBuiltin -> when (v.name) {
            "cap", "len" ->
                Type.Func(v, null, listOf(Type.RawParamForBuiltIn), listOf(Type.Primitive(Type_(), Int::class)), false)
            "make" -> {
                Type.Func(v, null, listOf(Type.RawParamForBuiltIn), listOf(Type.Slice(Type_(), Type.RawParamForBuiltIn)), true)
            }
            "panic" -> Type.Func(v, null, listOf(Type.RawParamForBuiltIn), emptyList(), false)
            "println" -> Type.Func(v, null, listOf(Type.RawParamForBuiltIn), emptyList(), true)
            else -> error("Unknown built in '${v.name}'")
        }
        is Type_.Type.TypeConst -> {
            // Try primitive first
            v.kotlinPrimitiveType()?.let { Type.Primitive(v, it) } ?: toConvType(v.type.typeConst.type!!.namedType)
        }
        is Type_.Type.TypeFunc -> Type.Func(
            type = v,
            recv = v.type.typeFunc.recv?.namedType?.let { toConvType(it) },
            params = v.type.typeFunc.params.map { toConvType(it.namedType) },
            results = v.type.typeFunc.results.map { toConvType(it.namedType) },
            vararg = false
        )
        is Type_.Type.TypeInterface ->  Type.Interface(v)
        is Type_.Type.TypeMap ->
            Type.Map(v, toConvType(v.type.typeMap.key!!.namedType), toConvType(v.type.typeMap.elem!!.namedType))
        is Type_.Type.TypeName -> toConvType(v.type.typeName.namedType)
        is Type_.Type.TypeNil -> Type.Nil(v)
        is Type_.Type.TypePointer -> Type.Pointer(v)
        is Type_.Type.TypeSignature -> Type.Func(
            type = v,
            recv = v.type.typeSignature.recv?.namedType?.let { toConvType(it) },
            params = v.type.typeSignature.params.map { toConvType(it.namedType) },
            results = v.type.typeSignature.results.map { toConvType(it.namedType) },
            vararg = false
        )
        is Type_.Type.TypeSlice -> Type.Slice(v, toConvType(v.type.typeSlice.elem!!.namedType))
        is Type_.Type.TypeVar -> toConvType(v.type.typeVar.namedType)
        else -> TODO("Unknown type: $v")
    }

    sealed class Type {
        abstract val type: Type_

        // Can include string
        data class Primitive(override val type: Type_, val cls: KClass<*>) : Type()
        data class Interface(override val type: Type_) : Type()
        data class Nil(override val type: Type_) : Type()
        data class Pointer(override val type: Type_) : Type()
        data class Func(
            override val type: Type_,
            val recv: Type?,
            val params: List<Type>,
            val results: List<Type>,
            val vararg: Boolean
        ) : Type()
        data class Array(override val type: Type_, val elemType: Type, val len: Long) : Type()
        data class Slice(override val type: Type_, val elemType: Type) : Type()
        data class Map(override val type: Type_, val keyType: Type, val valType: Type) : Type()
        object RawParamForBuiltIn : Type() {
            override val type = Type_()
        }
    }

    companion object : TypeConverter()
}