package go2k.compile2

import go2k.runtime.Ops
import kastree.ast.Node

fun Context.compileExpr(
    v: GNode.Expr,
    coerceTo: GNode.Expr? = null,
    coerceToType: GNode.Type? = null,
    byValue: Boolean = false
): Node.Expr = when (v) {
    is GNode.Expr.ArrayType -> TODO()
    is GNode.Expr.BasicLit -> compileExprBasicLit(v)
    is GNode.Expr.Binary -> compileExprBinary(v)
    is GNode.Expr.Call -> compileExprCall(v)
    is GNode.Expr.ChanType -> TODO()
    is GNode.Expr.CompositeLit -> TODO()
    is GNode.Expr.Ellipsis -> TODO()
    is GNode.Expr.FuncLit -> TODO()
    is GNode.Expr.FuncType -> TODO()
    is GNode.Expr.Ident -> compileExprIdent(v)
    is GNode.Expr.Index -> TODO()
    is GNode.Expr.InterfaceType -> TODO()
    is GNode.Expr.KeyValue -> TODO()
    is GNode.Expr.MapType -> TODO()
    is GNode.Expr.Paren -> TODO()
    is GNode.Expr.Selector -> TODO()
    is GNode.Expr.Slice -> TODO()
    is GNode.Expr.Star -> TODO()
    is GNode.Expr.StructType -> TODO()
    is GNode.Expr.TypeAssert -> TODO()
    is GNode.Expr.Unary -> TODO()
}

fun Context.compileExprBasicLit(v: GNode.Expr.BasicLit) =  when (v.kind) {
    GNode.Expr.BasicLit.Kind.CHAR -> {
        // Sometimes a char is used as an int (e.g. an array index), so if the type
        // is int, we have to treat it as such.
        val constType = v.type as GNode.Type.Const
        val expectsInt = constType.type is GNode.Type.Basic &&
            (constType.type.kind == GNode.Type.Basic.Kind.INT ||
                constType.type.kind == GNode.Type.Basic.Kind.UNTYPED_INT)
        if (expectsInt) compileConst(constType)
        else (constType.value as GNode.Const.Int).v.toInt().toChar().toConst()
    }
    GNode.Expr.BasicLit.Kind.FLOAT, GNode.Expr.BasicLit.Kind.INT -> compileConst(v.type as GNode.Type.Const)
    GNode.Expr.BasicLit.Kind.IMAG -> TODO()
    GNode.Expr.BasicLit.Kind.STRING -> {
        val raw = v.value.startsWith('`')
        ((v.type as GNode.Type.Const).value as GNode.Const.String).v.fold("") { str, char ->
            str + char.escape(str = true, raw = raw)
        }.toStringTmpl(raw)
    }
}

fun Context.compileExprBinary(v: GNode.Expr.Binary) = when (v.op) {
    GNode.Expr.Binary.Token.AND_NOT -> binaryOp(
        lhs = compileExpr(v.x),
        op = "and".toInfix(),
        rhs = call(compileExpr(v.y, coerceTo = v.x).dot("inv"))
    )
    GNode.Expr.Binary.Token.EQL -> call(
        expr = Ops::class.ref().dot("eql"),
        args = listOf(valueArg(compileExpr(v.x)), valueArg(compileExpr(v.y, coerceTo = v.x)))
    )
    GNode.Expr.Binary.Token.NEQ -> call(
        expr = Ops::class.ref().dot("neq"),
        args = listOf(valueArg(compileExpr(v.x)), valueArg(compileExpr(v.y, coerceTo = v.x)))
    )
    else -> binaryOp(
        lhs = compileExpr(v.x),
        op = when (v.op) {
            GNode.Expr.Binary.Token.ADD -> Node.Expr.BinaryOp.Token.ADD.toOper()
            GNode.Expr.Binary.Token.AND -> "and".toInfix()
            GNode.Expr.Binary.Token.GEQ -> Node.Expr.BinaryOp.Token.GTE.toOper()
            GNode.Expr.Binary.Token.GTR -> Node.Expr.BinaryOp.Token.GT.toOper()
            GNode.Expr.Binary.Token.LAND -> Node.Expr.BinaryOp.Token.AND.toOper()
            GNode.Expr.Binary.Token.LEQ -> Node.Expr.BinaryOp.Token.LTE.toOper()
            GNode.Expr.Binary.Token.LOR -> Node.Expr.BinaryOp.Token.OR.toOper()
            GNode.Expr.Binary.Token.LSS -> Node.Expr.BinaryOp.Token.LT.toOper()
            GNode.Expr.Binary.Token.MUL -> Node.Expr.BinaryOp.Token.MUL.toOper()
            GNode.Expr.Binary.Token.OR -> "or".toInfix()
            GNode.Expr.Binary.Token.QUO -> Node.Expr.BinaryOp.Token.DIV.toOper()
            GNode.Expr.Binary.Token.REM -> Node.Expr.BinaryOp.Token.MOD.toOper()
            GNode.Expr.Binary.Token.SHL -> "shl".toInfix()
            GNode.Expr.Binary.Token.SHR -> "shr".toInfix()
            GNode.Expr.Binary.Token.SUB -> Node.Expr.BinaryOp.Token.SUB.toOper()
            GNode.Expr.Binary.Token.XOR -> "xor".toInfix()
            // Handled above
            GNode.Expr.Binary.Token.AND_NOT, GNode.Expr.Binary.Token.EQL, GNode.Expr.Binary.Token.NEQ -> TODO()
        },
        rhs = compileExpr(v.y, coerceTo = v.x)
    )
}

fun Context.compileExprIdent(v: GNode.Expr.Ident) = when {
    v.name == "nil" -> NullConst
    v.name == "true" -> true.toConst()
    v.name == "false" -> false.toConst()
    v.type is GNode.Type.BuiltIn -> "go2k.runtime.builtin.${v.name}".toDottedExpr()
    else -> v.name.toName()
}