package go2k.compile.compiler

import go2k.compile.go.GNode
import go2k.runtime.Ops
import kastree.ast.Node

fun Context.compileExpr(
    v: GNode.Expr,
    coerceTo: GNode.Expr? = null,
    coerceToType: GNode.Type? = null,
    byValue: Boolean = false,
    unfurl: Boolean = false
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
    // If requested, unfurl the wrapped type
    if (!unfurl) expr else v.type.nonEntityType().let { type ->
        if (type is GNode.Type.Named &&
            type.underlying !is GNode.Type.Struct &&
            type.underlying !is GNode.Type.Interface
        ) expr.dot("\$v", safe = type.isNullable)
        else expr
    }
}.let { expr ->
    // Coerce type (does nothing if not necessary)
    coerceType(v, expr, coerceTo, coerceToType)
}.let { expr ->
    // Copy type (does nothing if not necessary)
    if (!byValue) expr else coerceTypeForByValueCopy(v, expr)
}

fun Context.compileExprBasicLit(v: GNode.Expr.BasicLit) =  when (v.kind) {
    GNode.Expr.BasicLit.Kind.CHAR, GNode.Expr.BasicLit.Kind.FLOAT, GNode.Expr.BasicLit.Kind.INT ->
        compileConst(v.type as GNode.Type.Const)
    GNode.Expr.BasicLit.Kind.IMAG -> TODO()
    GNode.Expr.BasicLit.Kind.STRING -> {
        val raw = v.value.startsWith('`')
        val litExpr = ((v.type as GNode.Type.Const).value as GNode.Const.String).v.fold("") { str, char ->
            str + char.escape(str = true, raw = raw)
        }.toStringTmpl(raw)
        val expr = call(
            expr = "go2k.runtime.GoString".toDottedExpr(),
            args = listOf(valueArg(litExpr))
        )
        compileExprToNamed(expr, v.type)
    }
}

fun Context.compileExprBinary(v: GNode.Expr.Binary): Node.Expr {
    // For bytes and shorts, shifts are only on ints in Kotlin, so convert each arg to int and we'll
    // convert back later
    val isByteOrShortShift = (v.op == GNode.Expr.Binary.Token.SHL || v.op == GNode.Expr.Binary.Token.SHR) &&
        (v.type.nonEntityType().let { if (it is GNode.Type.Named) it.underlying else it }?.kotlinPrimitiveType()).let {
            it == Byte::class || it == Short::class || it == UBYTE_CLASS || it == USHORT_CLASS
        }
    val lhs: Node.Expr
    val rhs: Node.Expr
    if (!isByteOrShortShift) {
        lhs = compileExpr(v.x, unfurl = true)
        rhs = compileExpr(v.y, coerceTo = v.x, unfurl = true)
    } else {
        lhs = compileExpr(v.x, coerceToType = GNode.Type.Basic("int", GNode.Type.Basic.Kind.INT))
        rhs = compileExpr(v.y, coerceToType = GNode.Type.Basic("int", GNode.Type.Basic.Kind.INT))
    }
    var expr = when (v.op) {
        GNode.Expr.Binary.Token.AND_NOT -> binaryOp(
            lhs = lhs,
            op = "and".toInfix(),
            rhs = call(rhs.dot("inv"))
        )
        GNode.Expr.Binary.Token.EQL -> call(
            expr = Ops::class.ref().dot("eql"),
            args = listOf(
                valueArg(lhs),
                valueArg(rhs)
            )
        )
        GNode.Expr.Binary.Token.NEQ -> call(
            expr = Ops::class.ref().dot("neq"),
            args = listOf(
                valueArg(lhs),
                valueArg(rhs)
            )
        )
        else -> binaryOp(
            lhs = lhs,
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
            rhs = rhs
        )
    }
    // In Kotlin's case, something like short + short = int so we need that back as a short
    val isArithOp = v.op == GNode.Expr.Binary.Token.ADD || v.op == GNode.Expr.Binary.Token.MUL ||
        v.op == GNode.Expr.Binary.Token.QUO || v.op == GNode.Expr.Binary.Token.REM ||
        v.op == GNode.Expr.Binary.Token.SHL || v.op == GNode.Expr.Binary.Token.SHR ||
        v.op == GNode.Expr.Binary.Token.SUB
    if (isArithOp) expr = compileExprBinaryNarrowByteOrShort(expr, v.type)
    // TODO: Ambiguities arise on "<", change when https://youtrack.jetbrains.com/issue/KT-25204 fixed
    if (v.op == GNode.Expr.Binary.Token.LSS) expr = expr.paren()
    return compileExprToNamed(expr, v.type)
}

fun Context.compileExprBinaryNarrowByteOrShort(expr: Node.Expr, type: GNode.Type?): Node.Expr {
    val toType = type.nonEntityType().let { if (it is GNode.Type.Named) it.underlying else it }
    return when (toType?.kotlinPrimitiveType()) {
        Byte::class, Short::class ->
            // TODO: Fix Kastree's writer to automatically put parens around these
            coerceType(expr.paren(), GNode.Type.Basic("int", GNode.Type.Basic.Kind.INT), type)
        UBYTE_CLASS, USHORT_CLASS ->
            coerceType(expr.paren(), GNode.Type.Basic("uint", GNode.Type.Basic.Kind.UINT), type)
        else -> expr
    }
}

fun Context.compileExprFuncLit(v: GNode.Expr.FuncLit): Node.Expr = withVarDefSet(v.childVarDefsNeedingRefs()) {
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
    call(
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
    expr = compileExpr(v.x, unfurl = true).let { xExpr ->
        v.x.type.nonEntityType().let { xType ->
            // Need to deref the pointer and deref nulls
            when {
                xType is GNode.Type.Pointer -> xExpr.ptrDeref().let { xExpr ->
                    if (xType.elem.nonEntityType()?.isNullable == true) xExpr.nullDeref() else xExpr
                }
                xType?.isNullable == true -> xExpr.nullDeref()
                else -> xExpr
            }
        }
    },
    indices = listOf(compileExpr(v.index))
)

fun Context.compileExprParen(v: GNode.Expr.Paren) = Node.Expr.Paren(compileExpr(v.x))

fun Context.compileExprSelector(v: GNode.Expr.Selector): Node.Expr {
    val lhsIsPointer = v.x.type.nonEntityType().let {
        it is GNode.Type.Pointer || (it as? GNode.Type.Named)?.underlying is GNode.Type.Pointer
    }
    val lhs = when {
        // Take different approach for pointer receiver methods
        (v.sel.type.nonEntityType() as? GNode.Type.Signature)?.recv?.type is GNode.Type.Pointer ->
            // If the LHS is a non-pointer and the RHS is a method w/ a pointer receiver,
            // we have to wrap the LHS in a pointer.
            if (!lhsIsPointer) compileExprUnaryAddressOf(v.x)
            else compileExpr(v.x)
        // If the LHS is pointer we deref
        lhsIsPointer -> compileExpr(v.x, unfurl = true).ptrDeref()
        else -> compileExpr(v.x)
    }
    return lhs.dot(compileExprIdent(v.sel), safe = v.x.type?.isNullable == true)
}

fun Context.compileExprSlice(v: GNode.Expr.Slice): Node.Expr {
    val type = v.x.type.nonEntityType().let { if (it is GNode.Type.Named) it.underlying else it }
    val expr = when (type) {
        is GNode.Type.Array, is GNode.Type.Basic, is GNode.Type.Slice -> {
            var subject = compileExpr(v.x, unfurl = true)
            if (type is GNode.Type.Slice) subject = subject.nullDeref()
            var args = listOf(valueArg(subject))
            if (v.low != null) args += valueArg(name = "low", expr = compileExpr(v.low, unfurl = true))
            if (v.high != null) args += valueArg(name = "high", expr = compileExpr(v.high, unfurl = true))
            if (v.max != null) args += valueArg(name = "max", expr = compileExpr(v.max, unfurl = true))
            call(expr = "go2k.runtime.slice".toDottedExpr(), args = args)
        }
        else -> TODO()
    }
    return compileExprToNamed(expr, v.type)
}

fun Context.compileExprStar(v: GNode.Expr.Star) = compileExpr(v.x).nullDeref().dot("\$v")

// Does nothing if not named or if named struct
fun Context.compileExprToNamed(expr: Node.Expr, type: GNode.Type?) = type.nonEntityType().let { type ->
    if (type !is GNode.Type.Named || type.underlying is GNode.Type.Struct) expr
    else call(expr = type.name().name.toName(), args = listOf(valueArg(expr)))
}

fun Context.compileExprUnary(v: GNode.Expr.Unary): Node.Expr {
    val expr = when {
        // An "AND" op is a pointer ref
        v.token == GNode.Expr.Unary.Token.AND -> compileExprUnaryAddressOf(v.x)
        // Receive from chan
        v.token == GNode.Expr.Unary.Token.ARROW -> call(
            // If the type is a tuple, it's the or-ok version
            expr = (v.type.nonEntityType() is GNode.Type.Tuple).let { withOk ->
                if (withOk) "go2k.runtime.recvWithOk" else "go2k.runtime.recv"
            }.toDottedExpr(),
            args = listOf(
                valueArg(compileExpr(v.x, unfurl = true)),
                // Needs zero value of chan element type
                valueArg(compileTypeZeroExpr((v.x.type.nonEntityType() as GNode.Type.Chan).elem))
            )
        )
        // + (i.e. positive) on unsigned is a noop
        v.token == GNode.Expr.Unary.Token.ADD && v.type!!.isUnsigned -> compileExpr(v.x, unfurl = true)
        // - (i.e. negation) on unsigned doesn't exist in Kotlin, have to mimic with "0 - x"
        v.token == GNode.Expr.Unary.Token.SUB && v.type!!.isUnsigned -> compileExprBinaryNarrowByteOrShort(
            expr = binaryOp(
                lhs = coerceType(0.toConst(), GNode.Type.Basic("int", GNode.Type.Basic.Kind.INT), v.type),
                op = Node.Expr.BinaryOp.Token.SUB,
                rhs = compileExpr(v.x, unfurl = true)
            ),
            type = v.type
        )
        // ^ is a bitwise complement
        v.token == GNode.Expr.Unary.Token.XOR -> call(compileExpr(v.x, unfurl = true).dot("inv"))
        else -> unaryOp(
            expr = compileExpr(v.x, unfurl = true),
            op = when (v.token) {
                GNode.Expr.Unary.Token.ADD -> Node.Expr.UnaryOp.Token.POS
                GNode.Expr.Unary.Token.NOT -> Node.Expr.UnaryOp.Token.NOT
                GNode.Expr.Unary.Token.SUB -> Node.Expr.UnaryOp.Token.NEG
                else -> error("Unrecognized unary op: ${v.token}")
            },
            prefix = true
        )
    }
    return compileExprToNamed(expr, v.type)
}

fun Context.compileExprUnaryAddressOf(v: GNode.Expr): Node.Expr = when (v) {
    is GNode.Expr.CompositeLit -> call(
        expr = GO_PTR_CLASS.ref().dot("lit"),
        args = listOf(valueArg(compileExpr(v)))
    )
    is GNode.Expr.Ident -> call(
        expr = GO_PTR_CLASS.ref().dot("ref"),
        args = listOf(valueArg(v.name.toName()))
    )
    is GNode.Expr.Index -> call(
        expr = GO_PTR_CLASS.ref().dot("index"),
        args = listOf(valueArg(compileExpr(v.x)), valueArg(compileExpr(v.index)))
    )
    is GNode.Expr.Paren -> compileExprUnaryAddressOf(v.x)
    is GNode.Expr.Selector -> compileExprUnaryAddressOfField(compileExpr(v.x), v.sel.name)
    else -> error("Not addressable expr")
}

// We choose not to use property references here to keep the reflection lib out
fun Context.compileExprUnaryAddressOfField(subject: Node.Expr, fieldName: String) = call(
    expr = GO_PTR_CLASS.ref().dot("field"),
    args = listOf(
        valueArg(subject),
        valueArg(brace(
            params = listOf(listOf(propVar("\$s"))),
            stmts = listOf("\$s".toName().dot(fieldName).toStmt())
        )),
        valueArg(brace(
            params = listOf(listOf(propVar("\$s")), listOf(propVar("\$v"))),
            stmts = listOf(binaryOp(
                lhs = "\$s".toName().dot(fieldName),
                op = Node.Expr.BinaryOp.Token.ASSN,
                rhs = "\$v".toName()
            ).toStmt())
        ))
    )
)