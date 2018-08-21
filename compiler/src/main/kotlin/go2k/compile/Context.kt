package go2k.compile

import go2k.compile.dumppb.*
import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

class Context(
    val pkg: Package,
    val namer: Namer = Namer.Simple(),
    val typeConv: TypeConverter = TypeConverter
) {
    // Key is the full path, value is the alias value if necessary
    val imports = mutableMapOf<String, String?>()

    fun KClass<*>.ref() = qualifiedName!!.classRef()
    fun KFunction<*>.ref() = (javaMethod!!.declaringClass.`package`.name + ".$name").funcRef()

    fun Node.Expr.convertType(fromTypeOf: Expr_, toTypeOf: Expr_): Node.Expr {
        return convertType(
            fromTypeOf.expr?.typeRef?.namedType ?: return this,
            toTypeOf.expr?.typeRef?.namedType ?: return this
        )
    }

    fun Node.Expr.convertType(fromTypeOf: Expr_, to: Type_): Node.Expr {
        return convertType(fromTypeOf.expr?.typeRef?.namedType ?: return this, to)
    }

    fun Node.Expr.convertType(from: Type_, to: Type_) = typeConv.convertType(this@Context, this, from, to)

    val String.javaIdent get() = this
    val String.javaName get() = Node.Expr.Name(javaIdent)
    // Just fully qualified for now
    fun String.classRef() = toDottedExpr()
    fun String.funcRef() = toDottedExpr()

    fun Type_.kotlinPrimitiveType(): KClass<*>? = when (type) {
        is Type_.Type.TypeBasic -> type.typeBasic.kotlinPrimitiveType(name)
        is Type_.Type.TypeConst -> type.typeConst.kotlinPrimitiveType()
        is Type_.Type.TypeVar -> type.typeVar.namedType.kotlinPrimitiveType()
        else -> null
    }

    fun TypeConst.kotlinPrimitiveType() = type!!.namedType.kotlinPrimitiveType()?.let { primType ->
        // Untyped is based on value
        when (primType) {
            BigInteger::class -> (value!!.value as ConstantValue.Value.Int_).int.untypedIntClass()
            BigDecimal::class -> when (val v = value!!.value) {
                is ConstantValue.Value.Int_ -> v.int.untypedFloatClass()
                is ConstantValue.Value.Float_ -> v.float.untypedFloatClass()
                else -> error("Unknown float type of $v")
            }
            else -> primType
        }
    }

    fun TypeBasic.kotlinPrimitiveType(name: String) = when (kind) {
        TypeBasic.Kind.BOOL, TypeBasic.Kind.UNTYPED_BOOL -> Boolean::class
        TypeBasic.Kind.INT -> Int::class
        TypeBasic.Kind.INT_8 -> Byte::class
        TypeBasic.Kind.INT_16 -> Short::class
        TypeBasic.Kind.INT_32 -> if (name == "rune") Char::class else Int::class
        TypeBasic.Kind.INT_64 -> Long::class
        TypeBasic.Kind.UINT, TypeBasic.Kind.UINT_32 -> UINT_CLASS
        TypeBasic.Kind.UINT_8 -> UBYTE_CLASS
        TypeBasic.Kind.UINT_16 -> USHORT_CLASS
        // TODO: break this into two
        TypeBasic.Kind.UINT_64, TypeBasic.Kind.UINT_PTR -> ULONG_CLASS
        TypeBasic.Kind.FLOAT_32 -> Float::class
        TypeBasic.Kind.FLOAT_64 -> Double::class
        TypeBasic.Kind.COMPLEX_64 -> TODO()
        TypeBasic.Kind.COMPLEX_128, TypeBasic.Kind.UNTYPED_COMPLEX -> TODO()
        TypeBasic.Kind.STRING, TypeBasic.Kind.UNTYPED_STRING -> String::class
        TypeBasic.Kind.UNTYPED_INT -> BigInteger::class
        TypeBasic.Kind.UNTYPED_RUNE -> Char::class
        TypeBasic.Kind.UNTYPED_FLOAT -> BigDecimal::class
        TypeBasic.Kind.UNTYPED_NIL -> TODO()
        else -> error("Unrecognized type kind: $kind")
    }

    val TypeRef.name get() = namedType.name
    val TypeRef.namedType get() = pkg.types[id]
    val TypeRef.type get() = namedType.type
    val TypeRef.typeConst get() = (type as? Type_.Type.TypeConst)?.typeConst

    // Others...need to refactor these to somewhere cleaner
    val TypeConst.constString get() = (value?.value as? ConstantValue.Value.String_)?.string
}