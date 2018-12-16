package go2k.compile2

import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger

fun Context.compileConst(v: GNode.Type.Const) = compileConst((v.type as GNode.Type.Basic).kind, v.value!!)

fun Context.compileConst(kind: GNode.Type.Basic.Kind, v: GNode.Const): Node.Expr {
    var value = v
    // As a special case, if it's a const float masquerading as an int, treat as a float
    if (value is GNode.Const.Int && kind == GNode.Type.Basic.Kind.UNTYPED_FLOAT) {
        value = GNode.Const.Float(v = value.v)
    }
    return when (value) {
        is GNode.Const.Boolean -> value.v.toConst()
        is GNode.Const.Complex -> TODO()
        is GNode.Const.Float -> {
            // Due to representability rules, we have to sometimes round down if it's too big
            val reprClass = value.v.untypedFloatClass(includeFloatClass = true)
            val bigDecCall = if (reprClass != BigDecimal::class) null else call(
                expr = "go2k.runtime.BigDec".toDottedExpr(),
                args = listOf(valueArg(expr = value.v.toStringTmpl()))
            )
            val withDec = if (value.v.contains('.')) value.v else value.v + ".0"
            when (kind) {
                GNode.Type.Basic.Kind.FLOAT_32 -> when (reprClass) {
                    Float::class -> (withDec + "f").toFloatConst()
                    Double::class -> call(withDec.toFloatConst().dot("toFloat".toName()))
                    else -> call(bigDecCall!!.dot("toFloat".toName()))
                }
                GNode.Type.Basic.Kind.FLOAT_64 -> when (reprClass) {
                    Float::class, Double::class -> withDec.toFloatConst()
                    else -> call(bigDecCall!!.dot("toDouble".toName()))
                }
                GNode.Type.Basic.Kind.UNTYPED_FLOAT -> when (reprClass) {
                    Float::class, Double::class -> withDec.toFloatConst()
                    else -> bigDecCall!!
                }
                else -> error("Unrecognized basic float kind of $kind")
            }
        }
        is GNode.Const.Int -> when (kind) {
            GNode.Type.Basic.Kind.INT, GNode.Type.Basic.Kind.INT_32 -> value.v.toIntConst()
            GNode.Type.Basic.Kind.INT_8 -> call(value.v.toIntConst().dot("toByte".toName()))
            GNode.Type.Basic.Kind.INT_16 -> call(value.v.toIntConst().dot("toShort".toName()))
            GNode.Type.Basic.Kind.INT_64 -> (value.v + "L").toIntConst()
            GNode.Type.Basic.Kind.UINT, GNode.Type.Basic.Kind.UINT_32 -> (value.v + "u").toIntConst()
            GNode.Type.Basic.Kind.UINT_8 -> call((value.v + "u").toIntConst().dot("toUByte".toName()))
            GNode.Type.Basic.Kind.UINT_16 -> call((value.v + "u").toIntConst().dot("toUShort".toName()))
            GNode.Type.Basic.Kind.UINT_64 -> (value.v + "uL").toIntConst()
            GNode.Type.Basic.Kind.UNTYPED_INT ->
                if (value.v.untypedIntClass() != BigInteger::class) value.v.toIntConst()
                else call(
                    expr = "go2k.runtime.BigInt".toDottedExpr(),
                    args = listOf(valueArg(value.v.toStringTmpl()))
                )
            else -> error("Unrecognized basic int kind of $kind")
        }
        is GNode.Const.String -> value.v.toStringTmpl()
    }
}