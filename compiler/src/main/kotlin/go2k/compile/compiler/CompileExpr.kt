package go2k.compile.compiler

import go2k.compile.go.GNode
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
    is GNode.Expr.CompositeLit -> compileExprCompositeLit(v)
    is GNode.Expr.Ellipsis -> TODO()
    is GNode.Expr.FuncLit -> compileExprFuncLit(v)
    is GNode.Expr.FuncType -> TODO()
    is GNode.Expr.Ident -> compileExprIdent(v)
    is GNode.Expr.Index -> compileExprIndex(v)
    is GNode.Expr.InterfaceType -> TODO()
    is GNode.Expr.KeyValue -> TODO()
    is GNode.Expr.MapType -> TODO()
    is GNode.Expr.Paren -> compileExprParen(v)
    is GNode.Expr.Selector -> compileExprSelector(v)
    is GNode.Expr.Slice -> compileExprSlice(v)
    is GNode.Expr.Star -> compileExprStar(v)
    is GNode.Expr.StructType -> TODO()
    is GNode.Expr.TypeAssert -> TODO()
    is GNode.Expr.Unary -> compileExprUnary(v)
}.let { expr ->
    // Coerce type (does nothing if not necessary)
    coerceType(v, expr, coerceTo, coerceToType)
}.let { expr ->
    // Copy type (does nothing if not necessary)
    if (!byValue) expr else coerceTypeForByValueCopy(v, expr)
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

fun Context.compileExprFuncLit(v: GNode.Expr.FuncLit): Node.Expr {
    // TODO: once https://youtrack.jetbrains.com/issue/KT-18346 is fixed, return Node.Expr.AnonFunc(decl)
    // In the meantime, we have to turn the function into a suspended lambda sadly
    val tempVar = currFunc.newTempVar("anonFunc")
    val inDefer = currFunc.deferDepth > 0
    pushFunc(v.funcType)
    // TODO: track returns to see if the return label is even used
    currFunc.returnLabelStack += tempVar
    currFunc.inDefer = inDefer
    val (params, resultType, stmts) = compileDeclFuncBody(v.funcType, v.body)
    popFunc()
    return call(
        expr = "go2k.runtime.anonFunc".toDottedExpr(),
        typeArgs = listOf(Node.Type(
            mods = listOf(Node.Modifier.Keyword.SUSPEND.toMod()),
            ref = Node.TypeRef.Func(
                receiverType = null,
                params = params.map { Node.TypeRef.Func.Param(null, it.type!!) },
                type = resultType ?: Unit::class.toType()
            )
        )),
        lambda = trailLambda(params = params.map { listOf(it.name) }, stmts = stmts, label = tempVar.labelIdent())
    )
}

fun Context.compileExprIdent(v: GNode.Expr.Ident) = when {
    v.name == "nil" -> NullConst
    v.name == "true" -> true.toConst()
    v.name == "false" -> false.toConst()
    v.type is GNode.Type.BuiltIn -> "go2k.runtime.builtin.${v.name}".toDottedExpr()
    varDefIsRef(v.name) -> v.name.toName().dot("\$v")
    else -> v.name.toName()
}

fun Context.compileExprIndex(v: GNode.Expr.Index) = Node.Expr.ArrayAccess(
    expr = compileExpr(v.x).nullDeref(),
    indices = listOf(compileExpr(v.index))
)

fun Context.compileExprParen(v: GNode.Expr.Paren) = Node.Expr.Paren(compileExpr(v.x))

fun Context.compileExprSelector(v: GNode.Expr.Selector): Node.Expr {
    // If the LHS is nullable, we have to do an unsafe deref
    var lhs = compileExpr(v.x)
    if (v.x.type.unnamedType()?.isNullable == true) lhs = lhs.nullDeref()
    return lhs.dot(compileExprIdent(v.sel))
}

fun Context.compileExprSlice(v: GNode.Expr.Slice) = when (val ut = v.x.type.unnamedType()) {
    is GNode.Type.Array, is GNode.Type.Basic, is GNode.Type.Slice -> {
        var subject = compileExpr(v.x)
        if (ut is GNode.Type.Slice) subject = subject.nullDeref()
        var args = listOf(valueArg(subject))
        if (v.low != null) args += valueArg(name = "low", expr = compileExpr(v.low))
        if (v.high != null) args += valueArg(name = "high", expr = compileExpr(v.high))
        if (v.max != null) args += valueArg(name = "max", expr = compileExpr(v.max))
        call(expr = "go2k.runtime.slice".toDottedExpr(), args = args)
    }
    else -> TODO()
}

fun Context.compileExprStar(v: GNode.Expr.Star) = compileExpr(v.x).nullDeref().dot("\$v")

fun Context.compileExprUnary(v: GNode.Expr.Unary) = when (v.token) {
    // An "AND" op is a pointer ref
    GNode.Expr.Unary.Token.AND -> compileExprUnaryAddressOf(v)
    // Receive from chan
    GNode.Expr.Unary.Token.ARROW -> call(
        // If the type is a tuple, it's the or-ok version
        expr = (v.type.unnamedType() is GNode.Type.Tuple).let { withOk ->
            if (withOk) "go2k.runtime.recvWithOk" else "go2k.runtime.recv"
        }.toDottedExpr(),
        args = listOf(
            valueArg(compileExpr(v.x)),
            // Needs zero value of chan element type
            valueArg(compileTypeZeroExpr((v.x.type.unnamedType() as GNode.Type.Chan).elem))
        )
    )
    // ^ is a bitwise complement
    GNode.Expr.Unary.Token.XOR -> call(compileExpr(v.x).dot("inv"))
    else -> unaryOp(
        expr = compileExpr(v.x),
        op = when (v.token) {
            GNode.Expr.Unary.Token.ADD -> Node.Expr.UnaryOp.Token.POS
            GNode.Expr.Unary.Token.DEC -> Node.Expr.UnaryOp.Token.DEC
            GNode.Expr.Unary.Token.INC -> Node.Expr.UnaryOp.Token.INC
            GNode.Expr.Unary.Token.NOT -> Node.Expr.UnaryOp.Token.NOT
            GNode.Expr.Unary.Token.SUB -> Node.Expr.UnaryOp.Token.NEG
            else -> error("Unrecognized unary op: ${v.token}")
        },
        prefix = v.token != GNode.Expr.Unary.Token.INC && v.token != GNode.Expr.Unary.Token.DEC
    )
}

fun Context.compileExprUnaryAddressOf(v: GNode.Expr.Unary) = when (v.x) {
    is GNode.Expr.Ident -> call(
        expr = GO_PTR_CLASS.ref().dot("ref"),
        args = listOf(valueArg(v.x.name.toName()))
    )
    is GNode.Expr.Index -> call(
        expr = GO_PTR_CLASS.ref().dot("index"),
        args = listOf(valueArg(compileExpr(v.x.x)), valueArg(compileExpr(v.x.index)))
    )
    else -> error("Not addressable expr")
}