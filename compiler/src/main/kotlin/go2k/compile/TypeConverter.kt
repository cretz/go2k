package go2k.compile

import go2k.compile.dumppb.Type_
import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

open class TypeConverter {

    fun convertType(ctx: Context, expr: Node.Expr, from: Type_, to: Type_) = ctx.run {
        // println("Converting type from $from to $to")
        convertType(expr, toConvType(from), toConvType(to))
    }

    fun Context.convertType(expr: Node.Expr, from: Type, to: Type): Node.Expr {
        // println("Converting local type from $from to $to")
        return when (to) {
            from -> expr
            is Type.Primitive -> when (from) {
                is Type.Primitive -> when (to.cls) {
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
                    else -> error("Unable to convert $to to $from")
                }
                else -> error("Invalid from type $from")
            }
            else -> error("Unable to convert $to to $from")
        }
    }

    fun Context.toConvType(v: Type_): Type = when (v.type) {
        is Type_.Type.TypeBasic, is Type_.Type.TypeConst -> Type.Primitive(v.kotlinPrimitiveType()!!)
        is Type_.Type.TypeVar -> toConvType(v.type.typeVar.namedType)
        else -> TODO("Unknown type: $v")
    }

    sealed class Type {
        // Can include string
        data class Primitive(val cls: KClass<*>) : Type()
    }

    companion object : TypeConverter()
}