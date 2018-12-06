package go2k.compile

import go2k.compile.dumppb.*
import go2k.runtime.GoStruct
import go2k.runtime.Ops
import go2k.runtime.runMain
import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.max

@ExperimentalUnsignedTypes
open class Compiler {

    // All functions herein start with "compile" and are alphabetically ordered

    fun Context.compileArrayCreate(elemType: TypeConverter.Type, len: Int) = when {
        // Primitive is PrimArray(size), string is Array(size) { "" }, other is arrayOfNulls<Type>(size)
        elemType !is TypeConverter.Type.Primitive -> call(
            expr = "kotlin.collection.arrayOfNulls".toDottedExpr(),
            typeArgs = listOf(compileType(elemType.type)),
            args = listOf(valueArg(expr = len.toConst()))
        )
        elemType.cls == String::class -> call(
            expr = "kotlin.Array".toDottedExpr(),
            args = listOf(valueArg(expr = len.toConst())),
            lambda = trailLambda(stmts = listOf("".toStringTmpl().toStmt()))
        )
        else -> call(
            expr = elemType.cls.primitiveArrayClass()!!.qualifiedName!!.toDottedExpr(),
            args = listOf(valueArg(expr = len.toConst()))
        )
    }

    fun Context.compileArrayLiteral(type: TypeConverter.Type.Array, elems: List<Expr_>) =
        compileArrayLiteral(type.elemType, type.len.toInt(), elems)

    fun Context.compileArrayLiteral(
        elemType: TypeConverter.Type,
        explicitLen: Int?,
        elems: List<Expr_>
    ): Node.Expr {
        // There are two types of array literals here. One that has no indexes,
        // uses every slot, and the length of literals matches the given length
        // (or there is no given length). The other creates for a certain length
        // and then sets explicit indexes in an "apply" block.
        val simpleArrayLiteral = (explicitLen == null || elems.size == explicitLen) &&
            elems.all { it.expr !is Expr_.Expr.KeyValueExpr }
        return if (simpleArrayLiteral) {
            call(
                expr = when ((elemType as? TypeConverter.Type.Primitive)?.cls) {
                    Byte::class -> "kotlin.byteArrayOf"
                    Char::class -> "kotlin.charArrayOf"
                    Double::class -> "kotlin.doubleArrayOf"
                    Float::class -> "kotlin.floatArrayOf"
                    Int::class -> "kotlin.intArrayOf"
                    Long::class -> "kotlin.longArrayOf"
                    Short::class -> "kotlin.shortArrayOf"
                    UBYTE_CLASS -> "kotlin.ubyteArrayOf"
                    UINT_CLASS -> "kotlin.uintArrayOf"
                    ULONG_CLASS -> "kotlin.ulongArrayOf"
                    USHORT_CLASS -> "kotlin.ushortArrayOf"
                    else -> "kotlin.arrayOf"
                }.funcRef(),
                args = elems.map { valueArg(expr = compileExpr(it).convertType(it, elemType)) }
            )
        } else {
            // Get the pairs, in order, and the overall len
            val elemPairs = elems.fold(emptyList<Pair<Int, Node.Expr>>()) { pairs, elem ->
                pairs + if (elem.expr is Expr_.Expr.KeyValueExpr) {
                    val key = elem.expr.keyValueExpr.key?.expr?.typeRef?.typeConst?.constInt ?: error("Missing key")
                    key to compileExpr(elem.expr.keyValueExpr.value!!)
                } else {
                    val key = pairs.lastOrNull()?.first?.plus(1) ?: 0
                    key to compileExpr(elem)
                }
            }
            val len = explicitLen ?: elemPairs.fold(0) { len, (index, _) -> max(len, index + 1) }
            // Create the array and then set the specific indices in the apply block
            call(
                expr = compileArrayCreate(elemType, len).dot("apply".toName()),
                lambda = trailLambda(stmts = elemPairs.map { (index, elem) ->
                    call(
                        expr = "set".toName(),
                        args = listOf(valueArg(expr = index.toConst()), valueArg(expr = elem))
                    ).toStmt()
                })
            )
        }
    }

    fun Context.compileAssignDefineStmt(v: AssignStmt): List<Node.Stmt> {
        // Single defines are normal properties. Multi-defines as results of
        // functions are destructurings. Multi-defines with multi-rhs are just
        // one at a time.
        // Identifiers already defined and as a result of a function use temps
        val multiDefineSingleRhs = v.lhs.size > 1 && v.rhs.size == 1
        var identsUsingTemps = emptyList<String>()
        val idents = v.lhs.map {
            val ident = (it.expr as Expr_.Expr.Ident).ident
            if (multiDefineSingleRhs && ident.defTypeRef == null) {
                identsUsingTemps += ident.name.javaIdent
                ident.name.javaIdent + "\$temp"
            } else ident.name.javaIdent
        }
        val stmts = v.rhs.mapIndexed { index, expr ->
            when {
                // If the ident is an underscore, we only do the RHS
                idents[index] == "_" -> compileExpr(expr).toStmt()
                // If we're not a multi-on-single and we didn't define it here, just assign it
                !multiDefineSingleRhs && (v.lhs[index].expr as Expr_.Expr.Ident).ident.defTypeRef == null -> binaryOp(
                    lhs = idents[index].javaIdent.toName(),
                    op = Node.Expr.BinaryOp.Token.ASSN,
                    rhs = compileExpr(expr).convertType(expr, v.lhs[index])
                ).toStmt()
                // Otherwise, just a property
                else -> Node.Stmt.Decl(property(
                    vars = if (multiDefineSingleRhs) idents.map { propVar(it) } else listOf(propVar(idents[index])),
                    expr = compileExpr(expr).let {
                        if (multiDefineSingleRhs) it else it.convertType(expr, v.lhs[index])
                    }
                ))
            }
        }
        // Now assign the temps
        return stmts + identsUsingTemps.map {
            binaryOp(
                lhs = it.toName(),
                op = Node.Expr.BinaryOp.Token.ASSN,
                rhs = "$it\$temp".toName()
            ).toStmt()
        }
    }

    fun Context.compileAssignMultiStmt(v: AssignStmt): List<Node.Stmt> {
        // If the multiple assignment is a result of a function, we make a temp var later
        // and do the destructuring ourselves
        val rhsExprs = if (v.rhs.size == 1) v.lhs.indices.map {
            call("\$temp".toName().dot("component${it + 1}".toName()))
        } else v.rhs.map { compileExpr(it) }
        // For multi-assign we use our helpers. They are based on whether there needs to be
        // an eager LHS and what it is.
        val multiAssignCall = call(
            expr = "go2k.runtime.Assign.multi".toDottedExpr(),
            args = v.lhs.zip(rhsExprs) { lhs, rhsExpr ->
                val eagerLhsExpr: Node.Expr?
                val assignLambdaParams: List<List<String>>
                val assignLambdaLhsExpr: Node.Expr?
                when (lhs.expr) {
                    // For LHS selects, we eagerly eval the LHS
                    is Expr_.Expr.SelectorExpr -> {
                        eagerLhsExpr = compileExpr(lhs.expr.selectorExpr.x!!)
                        assignLambdaParams = listOf(listOf("\$lhs"), listOf("\$rhs"))
                        assignLambdaLhsExpr = "\$lhs".toName().dot(lhs.expr.selectorExpr.sel!!.name.javaIdent.toName())
                    }
                    // For indexes, we eagerly eval the LHS and the index and then reference
                    is Expr_.Expr.IndexExpr -> {
                        eagerLhsExpr = binaryOp(
                            lhs = compileExpr(lhs.expr.indexExpr.x!!),
                            op = Node.Expr.BinaryOp.Oper.Infix("to"),
                            rhs = compileExpr(lhs.expr.indexExpr.index!!)
                        )
                        assignLambdaParams = listOf(listOf("\$lhs", "\$index"), listOf("\$rhs"))
                        assignLambdaLhsExpr = "\$lhs".toName().index("\$index".toName())
                    }
                    // For the rest, there is no eager LHS
                    else -> {
                        eagerLhsExpr = null
                        assignLambdaParams = emptyList()
                        // If this is an underscore, there is no assignment
                        assignLambdaLhsExpr =
                            if ((lhs.expr as? Expr_.Expr.Ident)?.ident?.name == "_") null
                            else compileExpr(lhs.expr!!)
                    }
                }
                // Now that we have configured the assignment, call it
                var assignParams = emptyList<Node.Expr>()
                if (eagerLhsExpr != null) assignParams += eagerLhsExpr
                assignParams += Node.Expr.Brace(
                    params = assignLambdaParams.map {
                        Node.Expr.Brace.Param(it.map { Node.Decl.Property.Var(it, null) }, null)
                    },
                    block = assignLambdaLhsExpr?.let { lhs ->
                        Node.Block(listOf(
                            binaryOp(
                                lhs = lhs,
                                op = Node.Expr.BinaryOp.Token.ASSN,
                                rhs = (if (assignLambdaParams.isEmpty()) "it" else "\$rhs").toName()
                            ).toStmt()
                        ))
                    }
                )
                assignParams += Node.Expr.Brace(emptyList(), Node.Block(listOf(rhsExpr.toStmt())))
                valueArg(expr = call(
                    expr = "go2k.runtime.Assign.assign".toDottedExpr(),
                    args = assignParams.map { valueArg(expr = it) }
                ))
            }
        )
        // Wrap in a also if it's a function result instead of normal multi assign
        return if (v.rhs.size > 1) listOf(multiAssignCall.toStmt()) else listOf(call(
            expr = compileExpr(v.rhs.single()).dot("also".toName()),
            lambda = trailLambda(
                params = listOf(Node.Expr.Brace.Param(listOf(Node.Decl.Property.Var("\$temp", null)), null)),
                stmts = listOf(multiAssignCall.toStmt())
            )
        ).toStmt())
    }

    fun Context.compileAssignStmt(v: AssignStmt): List<Node.Stmt> {
        // Handle decls elsewhere
        if (v.tok == Token.DEFINE) return compileAssignDefineStmt(v)
        // Handle multi-assign elsewhere
        if (v.lhs.size > 1) return compileAssignMultiStmt(v)

        // For cases where we have non-arith-binary-op assigns, just unwrap it to regular assign
        fun unwrapRhs(newOp: Token) = Token.ASSIGN to v.lhs.zip(v.rhs) { lhs, rhs ->
            Expr_(Expr_.Expr.BinaryExpr(BinaryExpr(x = lhs, op = newOp, y = rhs)))
        }
        val (tok, rhs) = when (v.tok) {
            Token.AND_ASSIGN -> unwrapRhs(Token.AND)
            Token.OR_ASSIGN -> unwrapRhs(Token.OR)
            Token.XOR_ASSIGN -> unwrapRhs(Token.XOR)
            Token.SHL_ASSIGN -> unwrapRhs(Token.SHL)
            Token.SHR_ASSIGN -> unwrapRhs(Token.SHR)
            Token.AND_NOT_ASSIGN -> unwrapRhs(Token.AND_NOT)
            else -> v.tok to v.rhs
        }

        return listOf(Node.Stmt.Expr(binaryOp(
            lhs = compileExpr(v.lhs.single()),
            op = when (tok) {
                Token.ASSIGN -> Node.Expr.BinaryOp.Token.ASSN
                Token.ADD_ASSIGN -> Node.Expr.BinaryOp.Token.ADD_ASSN
                Token.SUB_ASSIGN -> Node.Expr.BinaryOp.Token.SUB_ASSN
                Token.MUL_ASSIGN -> Node.Expr.BinaryOp.Token.MUL_ASSN
                Token.QUO_ASSIGN -> Node.Expr.BinaryOp.Token.DIV_ASSN
                Token.REM_ASSIGN -> Node.Expr.BinaryOp.Token.MOD_ASSN
                else -> error("Unrecognized token: $tok")
            },
            rhs = compileExpr(rhs.single()).convertType(rhs.single(), v.lhs.single())
        )))
    }

    fun Context.compileBasicLit(v: BasicLit) =  when (v.kind) {
        Token.INT, Token.FLOAT -> compileConstantValue(v.typeRef?.typeConst!!)
        Token.IMAG -> TODO()
        Token.CHAR -> v.typeRef!!.namedType.convType().let { expectedType ->
            // Sometimes a char is used as an int (e.g. an array index), so if the type
            // is int, we have to treat it as such.
            if (expectedType is TypeConverter.Type.Primitive && expectedType.cls == Int::class)
                compileConstantValue(v.typeRef.typeConst!!)
            else (v.typeRef.typeConst?.constInt ?: error("Invalid const int")).let { int ->
                constExpr("'" + compileBasicLitChar(int.toChar()) + "'", Node.Expr.Const.Form.CHAR)
            }
        }
        Token.STRING -> (v.typeRef?.typeConst?.constString ?: error("Invalid const string")).let { str ->
            val raw = v.value.startsWith('`')
            str.fold("") { str, char ->
                str + compileBasicLitChar(char, str = true, raw = raw)
            }.toStringTmpl(raw)
        }
        else -> error("Unrecognized lit kind: ${v.kind}")
    }

    fun Context.compileBasicLitChar(v: Char, str: Boolean = false, raw: Boolean = false) =
        if (raw) { if (v == '$') "\${'\$'}" else v.toString() }
        else when (v) {
            '\\' -> "\\\\"
            '\t' -> "\\t"
            '\b' -> "\\b"
            '\n' -> "\\n"
            '\r' -> "\\r"
            '"' -> if (str) "\\\"" else v.toString()
            '\'' -> if (str) v.toString() else "\\'"
            '$' -> "\\\$"
            // Escape unprintables
            else ->
                if (raw || v >= ' ') v.toString()
                else "\\u00" + v.toInt().toString(16).toUpperCase().let { if (it.length == 2) it else "0$it" }
        }

    fun Context.compileBinaryExpr(v: BinaryExpr) = when (v.op) {
        Token.EQL -> call(
            expr = Ops::class.ref().dot("eql".toName()),
            args = listOf(
                valueArg(expr = compileExpr(v.x!!)),
                valueArg(expr = compileExpr(v.y!!).convertType(v.y, v.x))
            )
        )
        Token.NEQ -> call(
            expr = Ops::class.ref().dot("neq".toName()),
            args = listOf(
                valueArg(expr = compileExpr(v.x!!)),
                valueArg(expr = compileExpr(v.y!!).convertType(v.y, v.x))
            )
        )
        Token.AND_NOT -> binaryOp(
            lhs = compileExpr(v.x!!),
            op = "and".toInfix(),
            rhs = call(compileExpr(v.y!!).dot("inv".toName())).convertType(v.y, v.x)
        )
        Token.AND_ASSIGN -> TODO()
        Token.OR_ASSIGN -> TODO()
        Token.XOR_ASSIGN -> TODO()
        Token.SHL_ASSIGN -> TODO()
        Token.SHR_ASSIGN -> TODO()
        Token.AND_NOT_ASSIGN -> TODO()
        Token.DEFINE -> TODO()
        else -> binaryOp(
            lhs = compileExpr(v.x!!),
            op = when (v.op) {
                Token.ADD -> Node.Expr.BinaryOp.Token.ADD.toOper()
                Token.SUB -> Node.Expr.BinaryOp.Token.SUB.toOper()
                Token.MUL -> Node.Expr.BinaryOp.Token.MUL.toOper()
                Token.QUO -> Node.Expr.BinaryOp.Token.DIV.toOper()
                Token.REM -> Node.Expr.BinaryOp.Token.MOD.toOper()
                Token.AND -> "and".toInfix()
                Token.OR -> "or".toInfix()
                Token.XOR -> "xor".toInfix()
                Token.SHL -> "shl".toInfix()
                Token.SHR -> "shr".toInfix()
                Token.ADD_ASSIGN -> Node.Expr.BinaryOp.Token.ADD_ASSN.toOper()
                Token.SUB_ASSIGN -> Node.Expr.BinaryOp.Token.SUB_ASSN.toOper()
                Token.MUL_ASSIGN -> Node.Expr.BinaryOp.Token.MUL_ASSN.toOper()
                Token.QUO_ASSIGN -> Node.Expr.BinaryOp.Token.DIV_ASSN.toOper()
                Token.REM_ASSIGN -> Node.Expr.BinaryOp.Token.MOD_ASSN.toOper()
                Token.LAND -> Node.Expr.BinaryOp.Token.AND.toOper()
                Token.LOR -> Node.Expr.BinaryOp.Token.OR.toOper()
                Token.LSS -> Node.Expr.BinaryOp.Token.LT.toOper()
                Token.GTR -> Node.Expr.BinaryOp.Token.GT.toOper()
                Token.ASSIGN -> Node.Expr.BinaryOp.Token.ASSN.toOper()
                Token.LEQ -> Node.Expr.BinaryOp.Token.LTE.toOper()
                Token.GEQ -> Node.Expr.BinaryOp.Token.GTE.toOper()
                else -> error("Unrecognized op ${v.op}")
            },
            rhs = compileExpr(v.y!!).convertType(v.y, v.x)
        )
    }

    fun Context.compileBlockStmt(v: BlockStmt) = Node.Block(v.list.flatMap { compileStmt(it) })

    fun Context.compileBranchStmt(v: BranchStmt): Node.Stmt {
        if (v.label != null) TODO()
        return when (v.tok) {
            Token.BREAK -> Node.Expr.Return(breakables.mark(), null).toStmt()
            Token.CONTINUE -> Node.Expr.Return(continuables.mark(), null).toStmt()
            else -> TODO()
        }
    }

    fun Context.compileCallExpr(v: CallExpr): Node.Expr {
        val funType = v.`fun`!!.expr!!.typeRef!!.namedType
        return when (funType.type) {
            // If the function is a const type, it's a conversion instead of a function call
            is Type_.Type.TypeConst -> {
                // Must be a singular arg
                val arg = v.args.singleOrNull() ?: error("Expecting single conversion arg")
                compileExpr(arg).convertType(arg, funType.type.typeConst.type!!.namedType)
            }
            is Type_.Type.TypeBuiltin -> when (funType.name) {
                "append" -> {
                    // First arg is the slice
                    val sliceType = v.args.first().expr!!.typeRef!!.namedType.convType() as TypeConverter.Type.Slice
                    // If ellipsis is present, only second arg is allowed and it's passed explicitly,
                    // otherwise a slice literal is created (TODO: kinda slow to create those unnecessarily)
                    val arg =
                        if (v.ellipsis > 0) compileExpr(v.args.apply { require(size == 2) }.last())
                        else compileSliceLiteral(sliceType, v.args.drop(1))
                    call(
                        expr = "go2k.runtime.builtin.append".funcRef(),
                        args = listOf(valueArg(expr = compileExpr(v.args.first())), valueArg(expr = arg))
                    )
                }
                "make" -> {
                    // First arg is the type to make
                    val argType = v.args.first().expr!!.typeRef!!.typeConst!!.type!!.namedType.convType()
                    when (argType) {
                        is TypeConverter.Type.Map -> {
                            // Primitive means the first arg is the zero val
                            val firstArgs =
                                if (argType.valType !is TypeConverter.Type.Primitive) emptyList()
                                else listOf(valueArg(expr = compileTypeZeroExpr(argType.valType.type)))
                            call(
                                expr = "go2k.runtime.builtin.makeMap".toDottedExpr(),
                                typeArgs = listOf(
                                    compileType(argType.keyType.type),
                                    compileType(argType.valType.type)
                                ),
                                args = firstArgs +
                                    if (v.args.size == 1) emptyList()
                                    else listOf(valueArg(name = "size", expr = compileExpr(v.args[1])))
                            )
                        }
                        is TypeConverter.Type.Slice -> {
                            val elemType = (argType.elemType as? TypeConverter.Type.Primitive)?.cls
                            val createSliceFnName = when (elemType) {
                                Byte::class -> "makeByteSlice"
                                Char::class -> "makeCharSlice"
                                Double::class -> "makeDoubleSlice"
                                Float::class -> "makeFloatSlice"
                                Int::class -> "makeIntSlice"
                                Long::class -> "makeLongSlice"
                                Short::class -> "makeShortSlice"
                                String::class -> "makeStringSlice"
                                UBYTE_CLASS -> "makeUByteSlice"
                                UINT_CLASS -> "makeUIntSlice"
                                ULONG_CLASS -> "makeULongSlice"
                                USHORT_CLASS -> "makeUShortSlice"
                                else -> "makeObjectSlice"
                            }
                            call(
                                expr = "go2k.runtime.builtin.$createSliceFnName".funcRef(),
                                args = v.args.drop(1).map { valueArg(expr = compileExpr(it)) }
                            )
                        }
                        else -> TODO()
                    }
                }
                else -> call(
                    expr = compileExpr(v.`fun`),
                    args = v.args.map { valueArg(expr = compileExpr(it)) }
                )
            }
            else -> call(
                expr = compileExpr(v.`fun`),
                // We choose to have vararg params be slices instead of supporting
                // Kotlin splats which only work on arrays
                args = (funType.convType() as TypeConverter.Type.Func).let { funConvType ->
                    v.args.mapIndexed { index, arg ->
                        val argType = funConvType.params.getOrNull(index)
                            ?: funConvType.params.lastOrNull()?.takeIf { funConvType.vararg }
                            ?: error("Missing arg type for index $index")
                        valueArg(expr = compileExpr(arg).convertType(arg, argType))
                    }
                }
            )
        }
    }

    fun Context.compileCompositeLit(v: CompositeLit) = when (val type = v.type?.expr) {
        is Expr_.Expr.ArrayType -> when (val convType = v.typeRef!!.namedType.convType()) {
            is TypeConverter.Type.Array -> compileArrayLiteral(convType, v.elts)
            is TypeConverter.Type.Slice -> compileSliceLiteral(convType, v.elts)
            else -> error("Unknown array type $convType")
        }
        is Expr_.Expr.MapType -> {
            val mapType = type.typeRef?.namedType?.convType() as? TypeConverter.Type.Map ?: error("Unknown value type")
            // Primitive types have defaults
            val firstArgs =
                if (mapType.valType !is TypeConverter.Type.Primitive) emptyList()
                else listOf(valueArg(expr = compileTypeZeroExpr(mapType.valType.type)))
            call(
                expr = "go2k.runtime.builtin.mapOf".toDottedExpr(),
                typeArgs = listOf(compileType(mapType.keyType.type), compileType(mapType.valType.type)),
                args = firstArgs + v.elts.map { elt ->
                    val kv = (elt.expr as Expr_.Expr.KeyValueExpr).keyValueExpr
                    valueArg(expr = call(
                        expr = "kotlin.Pair".toDottedExpr(),
                        args = listOf(valueArg(expr = compileExpr(kv.key!!)), valueArg(expr = compileExpr(kv.value!!)))
                    ))
                }
            )
        }
        else -> error("Unknown composite lit type: ${v.type}")
    }

    fun Context.compileConstantValue(v: TypeConst): Node.Expr {
        var constVal = v.value?.value ?: error("Missing const value")
        val basicType = v.type?.type as? Type_.Type.TypeBasic ?: error("Expected basic type, got ${v.type?.type}")
        // As a special case, if it's a const float masquerading as an int, treat as a float
        if (constVal is ConstantValue.Value.Int_ && basicType.typeBasic.kind == TypeBasic.Kind.UNTYPED_FLOAT) {
            constVal = ConstantValue.Value.Float_(float = constVal.int)
        }
        return compileConstantValue(basicType.typeBasic.kind, constVal)
    }

    fun Context.compileConstantValue(kind: TypeBasic.Kind, v: ConstantValue.Value) = when (v) {
        is ConstantValue.Value.Bool ->
            Node.Expr.Const(if (v.bool) "true" else "false", Node.Expr.Const.Form.BOOLEAN)
        is ConstantValue.Value.Int_ -> {
            when (kind) {
                TypeBasic.Kind.INT, TypeBasic.Kind.INT_32 -> v.int.toIntConst()
                TypeBasic.Kind.INT_8 -> call(v.int.toIntConst().dot("toByte".toName()))
                TypeBasic.Kind.INT_16 -> call(v.int.toIntConst().dot("toShort".toName()))
                TypeBasic.Kind.INT_64 -> (v.int + "L").toIntConst()
                TypeBasic.Kind.UINT, TypeBasic.Kind.UINT_32 -> (v.int + "u").toIntConst()
                TypeBasic.Kind.UINT_8 -> call((v.int + "u").toIntConst().dot("toUByte".toName()))
                TypeBasic.Kind.UINT_16 -> call((v.int + "u").toIntConst().dot("toUShort".toName()))
                TypeBasic.Kind.UINT_64 -> (v.int + "uL").toIntConst()
                TypeBasic.Kind.UNTYPED_INT ->
                    if (v.int.untypedIntClass() != BigInteger::class) v.int.toIntConst()
                    else call(
                        expr = "go2k.runtime.BigInt".classRef(),
                        args = listOf(valueArg(expr = v.int.toStringTmpl()))
                    )
                else -> error("Unrecognized basic int kind of $kind")
            }
        }
        is ConstantValue.Value.Float_ -> {
            // Due to representability rules, we have to sometimes round down if it's too big
            val reprClass = v.float.untypedFloatClass(includeFloatClass = true)
            val bigDecCall = if (reprClass != BigDecimal::class) null else call(
                expr = "go2k.runtime.BigDec".classRef(),
                args = listOf(valueArg(expr = v.float.toStringTmpl()))
            )
            val withDec = if (v.float.contains('.')) v.float else v.float + ".0"
            when (kind) {
                TypeBasic.Kind.FLOAT_32 -> when (reprClass) {
                    Float::class -> (withDec + "f").toFloatConst()
                    Double::class -> call(withDec.toFloatConst().dot("toFloat".toName()))
                    else -> call(bigDecCall!!.dot("toFloat".toName()))
                }
                TypeBasic.Kind.FLOAT_64 -> when (reprClass) {
                    Float::class, Double::class -> withDec.toFloatConst()
                    else -> call(bigDecCall!!.dot("toDouble".toName()))
                }
                TypeBasic.Kind.UNTYPED_FLOAT -> when (reprClass) {
                    Float::class, Double::class -> withDec.toFloatConst()
                    else -> bigDecCall!!
                }
                else -> error("Unrecognized basic float kind of $kind")
            }
        }
        is ConstantValue.Value.String_ -> v.string.toStringTmpl()
        else -> TODO("$v")
    }

    fun Context.compileDecl(v: Decl_.Decl, topLevel: Boolean) = when (v) {
        is Decl_.Decl.BadDecl -> error("Bad decl: $v")
        is Decl_.Decl.GenDecl -> compileGenDecl(v.genDecl, topLevel)
        is Decl_.Decl.FuncDecl -> listOf(compileFuncDecl(v.funcDecl, topLevel))
    }

    fun Context.compileDeclStmt(v: DeclStmt) = compileDecl(v.decl!!.decl!!, topLevel = false).map {
        // If it's a single property with the blank identifier, just extract the init expr
        val blank = (it as? Node.Decl.Property)?.takeIf { it.vars.singleOrNull()?.name == "_" }?.expr
        if (blank != null) Node.Stmt.Expr(blank) else Node.Stmt.Decl(it)
    }

    fun Context.compileExpr(v: Expr_) = compileExpr(v.expr!!)
    fun Context.compileExpr(v: Expr_.Expr): Node.Expr = when (v) {
        is Expr_.Expr.BadExpr -> TODO()
        is Expr_.Expr.Ident -> compileIdent(v.ident)
        is Expr_.Expr.Ellipsis -> TODO()
        is Expr_.Expr.BasicLit -> compileBasicLit(v.basicLit)
        is Expr_.Expr.FuncLit -> compileFuncLit(v.funcLit)
        is Expr_.Expr.CompositeLit -> compileCompositeLit(v.compositeLit)
        is Expr_.Expr.ParenExpr -> compileParenExpr(v.parenExpr)
        is Expr_.Expr.SelectorExpr -> TODO()
        is Expr_.Expr.IndexExpr -> compileIndexExpr(v.indexExpr)
        is Expr_.Expr.SliceExpr -> compileSliceExpr(v.sliceExpr)
        is Expr_.Expr.TypeAssertExpr -> TODO()
        is Expr_.Expr.CallExpr -> compileCallExpr(v.callExpr)
        is Expr_.Expr.StarExpr -> TODO()
        is Expr_.Expr.UnaryExpr -> compileUnaryExpr(v.unaryExpr)
        is Expr_.Expr.BinaryExpr -> compileBinaryExpr(v.binaryExpr)
        is Expr_.Expr.KeyValueExpr -> TODO()
        is Expr_.Expr.ArrayType -> TODO()
        is Expr_.Expr.StructType -> TODO()
        is Expr_.Expr.FuncType -> TODO()
        is Expr_.Expr.InterfaceType -> TODO()
        is Expr_.Expr.MapType -> TODO()
        is Expr_.Expr.ChanType -> TODO()
    }

    fun Context.compileExprStmt(v: ExprStmt) = Node.Stmt.Expr(compileExpr(v.x!!))

    fun Context.compileFile(v: File) = v.decls.flatMap { compileDecl(it.decl!!, topLevel = true) }.let { decls ->
        Node.File(
            anns = emptyList(),
            // Package and imports are set at a higher level
            pkg = null,
            imports = emptyList(),
            decls = decls
        )
    }

    fun compileFile(pkg: Package, v: File) = Context(pkg).run { this to compileFile(v)  }

    fun Context.compileForStmt(v: ForStmt): Node.Stmt {
        // Non-range for loops, due to their requirement to support break/continue and that break/continue
        // may be in another run clause or something, we have to use our own style akin to what is explained
        // at https://stackoverflow.com/a/34642869/547546. So we have a run around everything that is where
        // a break breaks out of. Then we have a forLoop fn where a continue returns.

        // Compile the block and see if anything had break/continue
        // TODO: explicitly labeled
        breakables.push()
        continuables.push()
        val bodyStmts = compileBlockStmt(v.body!!).stmts
        val (breakLabel, breakCalled) = breakables.pop()
        val (continueLabel, continueCalled) = continuables.pop()

        var stmts = emptyList<Node.Stmt>()
        if (v.init != null) stmts += compileStmt(v.init)
        stmts += call(
            expr = "go2k.runtime.forLoop".toDottedExpr(),
            args = listOf(
                // Condition or "true"
                valueArg(expr = Node.Expr.Brace(emptyList(), Node.Block(
                    listOf((v.cond?.let { compileExpr(it) } ?: true.toConst()).toStmt())
                ))),
                // Post or nothing
                valueArg(expr = Node.Expr.Brace(emptyList(), Node.Block(
                    if (v.post == null) emptyList() else compileStmt(v.post)
                )))
            ),
            lambda = trailLambda(
                label = continueLabel.takeIf { continueCalled },
                stmts = bodyStmts
            )
        ).toStmt()
        // If there is an init or a break label, we have to wrap in a run
        return if (stmts.size == 1 && !breakCalled) stmts.single() else call(
            expr = "run".toName(),
            lambda = trailLambda(
                label = breakLabel.takeIf { breakCalled },
                stmts = stmts
            )
        ).toStmt()
    }

    fun Context.compileFuncDecl(v: FuncDecl, topLevel: Boolean): Node.Decl.Func {
        if (!topLevel) TODO()
        return compileFuncDecl(v.name?.name, v.type!!, v.body)
    }

    fun Context.compileFuncDecl(name: String?, type: FuncType, body: BlockStmt?): Node.Decl.Func {
        var mods = emptyList<Node.Modifier>()
        // TODO: Anon functions sadly can't be suspended yet, ref: https://youtrack.jetbrains.com/issue/KT-18346
        if (name != null) mods += Node.Modifier.Keyword.SUSPEND.toMod()
        if (name != null && name.first().isLowerCase()) mods += Node.Modifier.Keyword.INTERNAL.toMod()
        return func(
            mods = mods,
            name = name?.javaIdent,
            params = (type.params?.list ?: emptyList()).flatMap { field ->
                field.names.map { name ->
                    param(
                        name = name.name.javaIdent,
                        type = compileTypeRef(field.type!!.expr!!.typeRef!!)
                    )
                }
            },
            type = type.results?.let {
                if (it.list.size != 1 || it.list.single().names.size > 1) TODO()
                val id = (it.list.first().type!!.expr as Expr_.Expr.Ident).ident
                compileTypeRef(id.typeRef!!)
            },
            body = body?.let { Node.Decl.Func.Body.Block(compileBlockStmt(it)) }
        )
    }

    fun Context.compileFuncLit(v: FuncLit) = Node.Expr.AnonFunc(compileFuncDecl(null, v.type!!, v.body))

    fun Context.compileGenDecl(v: GenDecl, topLevel: Boolean) = v.specs.flatMap {
        compileSpec(it.spec!!, v.tok == Token.CONST, topLevel)
    }

    fun Context.compileIdent(v: Ident) = when {
        v.name == "nil" -> NullConst
        v.name == "true" -> Node.Expr.Const("true", Node.Expr.Const.Form.BOOLEAN)
        v.name == "false" -> Node.Expr.Const("false", Node.Expr.Const.Form.BOOLEAN)
        v.typeRef?.type is Type_.Type.TypeBuiltin -> "go2k.runtime.builtin.${v.name}".funcRef()
        else -> v.name.javaName
    }

    fun Context.compileIfStmt(v: IfStmt): Node.Stmt.Expr {
        var expr: Node.Expr = Node.Expr.If(
            expr = compileExpr(v.cond!!),
            body = Node.Expr.Brace(emptyList(), compileBlockStmt(v.body!!)),
            elseBody = v.`else`?.let {
                when (it.stmt) {
                    is Stmt_.Stmt.BlockStmt -> Node.Expr.Brace(emptyList(), compileBlockStmt(it.stmt.blockStmt))
                    is Stmt_.Stmt.IfStmt -> compileIfStmt(it.stmt.ifStmt).expr
                    else -> error("Unknown else statement type: ${it.stmt}")
                }
            }
        )
        // If there is an init, we are going to do it inside of a run block and then do the if
        if (v.init != null) expr = call(
            expr = "run".toName(),
            lambda = trailLambda(stmts = compileStmt(v.init) + expr.toStmt())
        )
        return expr.toStmt()
    }

    fun Context.compileIncDecStmt(v: IncDecStmt) = Node.Stmt.Expr(
        Node.Expr.UnaryOp(
            expr = compileExpr(v.x!!),
            oper = Node.Expr.UnaryOp.Oper(
                if (v.tok == Token.INC) Node.Expr.UnaryOp.Token.INC else Node.Expr.UnaryOp.Token.DEC
            ),
            prefix = false
        )
    )

    fun Context.compileIndexExpr(v: IndexExpr) = Node.Expr.ArrayAccess(
        expr = compileExpr(v.x!!).nullDeref(),
        indices = listOf(compileExpr(v.index!!))
    )

    fun compilePackage(pkg: Package, overrideName: String? = null): KotlinPackage {
        // Compile all files...
        var initCount = 0
        var files = pkg.files.map { pkgFile ->
            compileFile(pkg, pkgFile).let { (ctx, file) ->
                Triple(
                    pkgFile.fileName + ".kt",
                    ctx,
                    file.copy(
                        pkg = Node.Package(
                            mods = emptyList(),
                            names = (overrideName ?: ctx.namer.packageName(pkg.path, pkg.name)).split('.')
                        ),
                        // Change all init functions to start with dollar sign and numbered
                        decls = file.decls.map { decl ->
                            (decl as? Node.Decl.Func)?.takeIf { it.name == "init" }?.copy(
                                mods = listOf(Node.Modifier.Keyword.PRIVATE.toMod()),
                                name = "\$init${++initCount}"
                            ) ?: decl
                        }
                    )
                )
            }
        }

        // Add package initializer and main in last file if necessary
        files = files.dropLast(1) + files.last().let { (name, ctx, file) ->
            var extraDecls = emptyList<Node.Decl>()
            extraDecls += ctx.compilePackageInitializer(initCount)
            ctx.compilePackageMain()?.also { extraDecls += it }
            Triple(name, ctx, file.copy(decls = file.decls + extraDecls))
        }

        // Map by name and update imports
        return KotlinPackage(files.map { (name, ctx, file) ->
            name to file.copy(
                imports = ctx.imports.map { (importPath, alias) ->
                    Node.Import(names = importPath.split('.'), wildcard = false, alias = alias)
                }
            )
        }.toMap())
    }

    fun Context.compilePackageInitializer(initCount: Int): Node.Decl.Func {
        val topLevelValueSpecs = pkg.files.flatMap {
            it.decls.flatMap {
                (it.decl as? Decl_.Decl.GenDecl)?.genDecl?.takeIf { it.tok != Token.CONST }?.specs?.mapNotNull {
                    (it.spec as? Spec_.Spec.ValueSpec)?.valueSpec
                } ?: emptyList()
            }
        }
        val varInitStmtsByName = topLevelValueSpecs.flatMap {
            it.names.zip(it.values) { name, value ->
                name.name to Node.Stmt.Expr(
                    binaryOp(
                        lhs = compileIdent(name),
                        op = Node.Expr.BinaryOp.Token.ASSN,
                        rhs = compileExpr(value!!)
                    )
                )
            }
        }.toMap()

        // Make a suspendable public package init that does var inits and calls init funcs
        return func(
            mods = listOf(Node.Modifier.Keyword.SUSPEND.toMod()),
            name = "init",
            body = Node.Decl.Func.Body.Block(Node.Block(
                pkg.varInitOrder.mapNotNull {
                    varInitStmtsByName[it]
                } + (1..initCount).map { initNum -> Node.Stmt.Expr(call("\$init$initNum".javaName)) }
            ))
        )
    }

    fun Context.compilePackageMain(): Node.Decl.Func? {
        val hasMain = pkg.name == "main" && pkg.files.any {
            it.decls.any {
                (it.decl as? Decl_.Decl.FuncDecl)?.funcDecl.let {
                    it?.name?.name == "main" && it.recv == null
                }
            }
        }
        return if (!hasMain) null else func(
            name = "main",
            params = listOf(param(
                name = "args",
                type = arrayType(String::class)
            )),
            body = Node.Decl.Func.Body.Expr(call(
                expr = ::runMain.ref(),
                args = listOf(
                    valueArg(expr = "args".javaName),
                    valueArg(expr = Node.Expr.DoubleColonRef.Callable(recv = null, name = "init")),
                    valueArg(expr = Node.Expr.Brace(emptyList(), Node.Block(listOf(
                        Node.Stmt.Expr(call("main".javaName))
                    ))))
                )
            ))
        )
    }

    fun Context.compileParenExpr(v: ParenExpr) = Node.Expr.Paren(compileExpr(v.x!!))

    fun Context.compileRangeStmt(v: RangeStmt): Node.Stmt {
        // Range loops are first surrounded by a run for breaking and then labeled for continuing.
        // They are iterated via forEach and forEachIndexed depending upon what is ranged on.
        var keyParam = (v.key?.expr as? Expr_.Expr.Ident)?.ident?.name?.takeIf { it != "_" }
        var valParam = (v.value?.expr as? Expr_.Expr.Ident)?.ident?.name?.takeIf { it != "_" }
        val keyValDestructured: Boolean
        val forEachFnName: String
        val rangeExprType = v.x!!.expr!!.typeRef!!.namedType.convType()
        when (rangeExprType) {
            is TypeConverter.Type.Array, is TypeConverter.Type.Primitive, is TypeConverter.Type.Slice -> {
                if (rangeExprType is TypeConverter.Type.Primitive)
                    require(rangeExprType.cls == String::class) { "Unknown range type $rangeExprType" }
                forEachFnName = if (keyParam != null) "forEachIndexed" else "forEach"
                keyValDestructured = false
            }
            is TypeConverter.Type.Map -> {
                forEachFnName = "forEach"
                keyValDestructured = true
            }
            else -> error("Unknown range type $rangeExprType")
        }
        // Build lambda params
        var initStmts = emptyList<Node.Stmt>()
        // Define uses lambda params, otherwise we set from temp vars
        if (v.tok == Token.ASSIGN) {
            if (keyParam != null) {
                initStmts += binaryOp(keyParam.toName(), Node.Expr.BinaryOp.Token.ASSN, "\$tempKey".toName()).toStmt()
                keyParam = "\$tempKey"
            }
            if (valParam != null) {
                initStmts += binaryOp(valParam.toName(), Node.Expr.BinaryOp.Token.ASSN, "\$tempVal".toName()).toStmt()
                valParam = "\$tempVal"
            }
        }
        var params = emptyList<Node.Expr.Brace.Param>()
        if (keyValDestructured) {
            params += Node.Expr.Brace.Param(listOf(
                keyParam?.let { Node.Decl.Property.Var(it, null) },
                valParam?.let { Node.Decl.Property.Var(it, null) }
            ), null)
        } else {
            if (keyParam != null)
                params += Node.Expr.Brace.Param(listOf(Node.Decl.Property.Var(keyParam, null)), null)
            if (keyParam != null || valParam != null)
                params += Node.Expr.Brace.Param(listOf(valParam?.let { Node.Decl.Property.Var(it, null) }), null)
        }

        // Check body for any break/continue calls
        // TODO: explicit labels
        breakables.push()
        continuables.push()
        val bodyStmts = compileBlockStmt(v.body!!).stmts
        val (breakLabel, breakCalled) = breakables.pop()
        val (continueLabel, continueCalled) = continuables.pop()

        var stmt = call(
            expr = compileExpr(v.x).dot(forEachFnName.toName()),
            lambda = trailLambda(
                label = continueLabel.takeIf { continueCalled },
                params = params,
                stmts = initStmts + bodyStmts
            )
        ).toStmt()
        if (breakCalled) stmt = call(
            expr = "run".toName(),
            lambda = trailLambda(
                label = breakLabel,
                stmts = listOf(stmt)
            )
        ).toStmt()
        return stmt
    }

    fun Context.compileReturnStmt(v: ReturnStmt) = Node.Stmt.Expr(
        Node.Expr.Return(
            label = null,
            expr = v.results.let {
                if (it.size > 1) TODO()
                it.singleOrNull()?.let { compileExpr(it) }
            }
        )
    )

    fun Context.compileSliceExpr(v: SliceExpr) = when (val type = v.x!!.expr!!.typeRef!!.namedType.convType()) {
        is TypeConverter.Type.Slice, is TypeConverter.Type.Array, is TypeConverter.Type.Primitive -> {
            var subject = compileExpr(v.x)
            if (type is TypeConverter.Type.Slice) subject = subject.nullDeref()
            var args = listOf(valueArg(expr = subject))
            if (v.low != null) args += valueArg(name = "low", expr = compileExpr(v.low))
            if (v.high != null) args += valueArg(name = "high", expr = compileExpr(v.high))
            if (v.max != null) args += valueArg(name = "max", expr = compileExpr(v.max))
            call(expr = "go2k.runtime.builtin.slice".funcRef(), args = args)
        }
        else -> TODO()
    }

    fun Context.compileSliceLiteral(type: TypeConverter.Type.Slice, elems: List<Expr_>) = call(
        expr = "go2k.runtime.builtin.slice".funcRef(),
        args = listOf(valueArg(expr = compileArrayLiteral(type.elemType, null, elems)))
    )

    fun Context.compileSpec(v: Spec_.Spec, const: Boolean, topLevel: Boolean) = when (v) {
        is Spec_.Spec.ImportSpec -> TODO()
        is Spec_.Spec.ValueSpec ->
            if (const) compileValueSpecConst(v.valueSpec, topLevel) else compileValueSpecVar(v.valueSpec, topLevel)
        is Spec_.Spec.TypeSpec -> listOf(compileTypeSpec(v.typeSpec))
    }

    fun Context.compileStmt(v: Stmt_) = compileStmt(v.stmt!!)
    fun Context.compileStmt(v: Stmt_.Stmt): List<Node.Stmt> = when (v) {
        is Stmt_.Stmt.BadStmt -> TODO()
        is Stmt_.Stmt.DeclStmt -> compileDeclStmt(v.declStmt)
        is Stmt_.Stmt.EmptyStmt -> TODO()
        is Stmt_.Stmt.LabeledStmt -> TODO()
        is Stmt_.Stmt.ExprStmt -> listOf(compileExprStmt(v.exprStmt))
        is Stmt_.Stmt.SendStmt -> TODO()
        is Stmt_.Stmt.IncDecStmt -> listOf(compileIncDecStmt(v.incDecStmt))
        is Stmt_.Stmt.AssignStmt -> compileAssignStmt(v.assignStmt)
        is Stmt_.Stmt.GoStmt -> TODO()
        is Stmt_.Stmt.DeferStmt -> TODO()
        is Stmt_.Stmt.ReturnStmt -> listOf(compileReturnStmt(v.returnStmt))
        is Stmt_.Stmt.BranchStmt -> listOf(compileBranchStmt(v.branchStmt))
        is Stmt_.Stmt.BlockStmt -> TODO()
        is Stmt_.Stmt.IfStmt -> listOf(compileIfStmt(v.ifStmt))
        is Stmt_.Stmt.CaseClause -> TODO()
        is Stmt_.Stmt.SwitchStmt -> listOf(compileSwitchStmt(v.switchStmt))
        is Stmt_.Stmt.TypeSwitchStmt -> TODO()
        is Stmt_.Stmt.CommClause -> TODO()
        is Stmt_.Stmt.SelectStmt -> TODO()
        is Stmt_.Stmt.ForStmt -> listOf(compileForStmt(v.forStmt))
        is Stmt_.Stmt.RangeStmt -> listOf(compileRangeStmt(v.rangeStmt))
    }

    // Name is empty string, is not given any visibility modifier one way or another
    fun Context.compileStructType(v: StructType): Node.Decl.Structured {
        // TODO: more detail
        return structured(
            primaryConstructor = primaryConstructor(
                params = (v.fields?.list ?: emptyList()).flatMap { field ->
                    val typeRef = field.type!!.expr!!.typeRef!!
                    field.names.map { name ->
                        param(
                            readOnly = false,
                            name = name.name.javaIdent,
                            type = compileTypeRef(typeRef),
                            default = compileTypeRefZeroExpr(typeRef)
                        )
                    }
                }
            ),
            parents = listOf(Node.Decl.Structured.Parent.Type(
                type = GoStruct::class.toType().ref as Node.TypeRef.Simple,
                by = null
            ))
        )
    }

    fun Context.compileSwitchStmt(v: SwitchStmt): Node.Stmt {
        // All cases are when entries
        val cases = (v.body?.list ?: emptyList()).map { (it.stmt as Stmt_.Stmt.CaseClause).caseClause }
        // Track break usage
        breakables.push()
        val entries = cases.mapIndexed { index, case ->
            // Body includes all fallthroughs as duplicate code in run blocks
            var body = listOf(case.body)
            for (i in index + 1 until cases.size) {
                val lastStmt = body.last().lastOrNull()?.stmt
                if ((lastStmt as? Stmt_.Stmt.BranchStmt)?.branchStmt?.tok != Token.FALLTHROUGH) break
                body = body.dropLast(1).plusElement(body.last().dropLast(1)).plusElement(cases[i].body)
            }
            Node.Expr.When.Entry(
                conds = case.list.map { compileExpr(it) }.let { conds ->
                    // If there is no expr, the expressions are separated with || in a single cond
                    if (v.tag != null || conds.isEmpty()) conds.map { Node.Expr.When.Cond.Expr(it) }
                    else listOf(Node.Expr.When.Cond.Expr(
                        conds.drop(1).fold(conds.first()) { lhs, rhs ->
                            binaryOp(lhs, Node.Expr.BinaryOp.Token.OR, rhs)
                        }
                    ))
                },
                body = Node.Expr.Brace(emptyList(), Node.Block(
                    body.first().flatMap { compileStmt(it) } + body.drop(1).map {
                        // Fallthrough
                        call(
                            expr = "run".toName(),
                            lambda = trailLambda(stmts = it.flatMap { compileStmt(it) })
                        ).toStmt()
                    }
                ))
            )
        }.toMutableList()
        val (breakLabel, breakCalled) = breakables.pop()
        // Put default at the end
        entries.indexOfFirst { it.conds.isEmpty() }.also { if (it >= 0) entries += entries.removeAt(it) }
        // Create the when
        var stmt = Node.Expr.When(
            expr = v.tag?.let { compileExpr(it) },
            entries = entries
        ).toStmt()
        // If there is an init or break, we do it in a run clause. Note, we cannot use when-with-subject-decl
        // here when it applies because Kotlin only allows "val" whereas Go allows mutable var.
        if (v.init != null || breakCalled) stmt = call(
            expr = "run".toName(),
            lambda = trailLambda(
                label = breakLabel.takeIf { breakCalled },
                stmts = (v.init?.let { compileStmt(it) } ?: emptyList()) + stmt
            )
        ).toStmt()
        return stmt
    }

    fun Context.compileTypeRefZeroExpr(v: TypeRef) = compileTypeZeroExpr(v.namedType)

    fun Context.compileTypeSpec(v: TypeSpec): Node.Decl {
        if (v.assign != 0) TODO("Aliases")
        return compileTypeSpecExpr(v.name!!, v.type!!.expr!!)
    }

    fun Context.compileTypeSpecExpr(name: Ident, v: Expr_.Expr) = when (v) {
        is Expr_.Expr.StructType -> compileTypeSpecStructType(name, v.structType)
        else -> error("Unknown type spec type $v")
    }

    fun Context.compileTypeSpecStructType(name: Ident, v: StructType) = compileStructType(v).copy(
        mods = if (name.isExposed) emptyList() else listOf(Node.Modifier.Keyword.INTERNAL.toMod()),
        name = name.name.javaIdent
    )

    fun Context.compileTypeZeroExpr(v: Type_): Node.Expr = when (v.type) {
        null -> error("No type")
        is Type_.Type.TypeArray ->
            compileArrayCreate(v.type.typeArray.elem!!.namedType.convType(), v.type.typeArray.len.toInt())
        is Type_.Type.TypeBasic -> when (v.type.typeBasic.kind) {
            TypeBasic.Kind.BOOL, TypeBasic.Kind.UNTYPED_BOOL ->
                compileConstantValue(v.type.typeBasic.kind, ConstantValue.Value.Bool(false))
            TypeBasic.Kind.INT, TypeBasic.Kind.INT_8, TypeBasic.Kind.INT_16, TypeBasic.Kind.INT_32,
                TypeBasic.Kind.INT_64, TypeBasic.Kind.UINT, TypeBasic.Kind.UINT_8, TypeBasic.Kind.UINT_16,
                TypeBasic.Kind.UINT_32, TypeBasic.Kind.UINT_64, TypeBasic.Kind.UINT_PTR,
                TypeBasic.Kind.UNTYPED_INT, TypeBasic.Kind.UNTYPED_RUNE ->
                compileConstantValue(v.type.typeBasic.kind, ConstantValue.Value.Int_("0"))
            TypeBasic.Kind.FLOAT_32, TypeBasic.Kind.FLOAT_64, TypeBasic.Kind.UNTYPED_FLOAT ->
                compileConstantValue(v.type.typeBasic.kind, ConstantValue.Value.Float_("0.0"))
            TypeBasic.Kind.STRING, TypeBasic.Kind.UNTYPED_STRING ->
                compileConstantValue(v.type.typeBasic.kind, ConstantValue.Value.String_(""))
            TypeBasic.Kind.UNTYPED_NIL ->
                NullConst
            else ->
                error("Unrecognized type kind: ${v.type.typeBasic.kind}")
        }
        is Type_.Type.TypeBuiltin -> TODO()
        is Type_.Type.TypeChan, is Type_.Type.TypeFunc, is Type_.Type.TypeInterface,
            is Type_.Type.TypeMap, is Type_.Type.TypeNil, is Type_.Type.TypePointer,
            is Type_.Type.TypeSlice ->
            NullConst
        is Type_.Type.TypeConst -> compileTypeRefZeroExpr(v.type.typeConst.type!!)
        is Type_.Type.TypeLabel -> compileTypeRefZeroExpr(v.type.typeLabel)
        is Type_.Type.TypeName -> compileTypeRefZeroExpr(v.type.typeName)
        is Type_.Type.TypeNamed -> compileTypeRefZeroExpr(v.type.typeNamed.type!!)
        is Type_.Type.TypePackage -> TODO()
        is Type_.Type.TypeSignature -> TODO()
        is Type_.Type.TypeStruct -> TODO()
        is Type_.Type.TypeTuple -> TODO()
        is Type_.Type.TypeVar -> compileTypeRefZeroExpr(v.type.typeVar)
    }

    fun Context.compileUnaryExpr(v: UnaryExpr) = compileExpr(v.x!!).let { xExpr ->
        // An "AND" op is a pointer deref
        if (v.op == Token.AND) {
            // Compile type of expr, and if it's not nullable, a simple "as" nullable form works.
            // Otherwise, it's a NestedPtr
            val xType = compileTypeRef(v.x.expr!!.typeRef!!)
            if (xType.ref !is Node.TypeRef.Nullable) typeOp(
                lhs = xExpr,
                op = Node.Expr.TypeOp.Token.AS,
                rhs = xType.nullable()
            ) else call(
                expr = NESTED_PTR_CLASS.ref(),
                args = listOf(valueArg(expr = xExpr))
            )
        } else if (v.op == Token.XOR) {
            // ^ is a bitwise complement
            call(expr = xExpr.dot("inv".toName()))
        } else unaryOp(
            expr = xExpr,
            op = when (v.op) {
                Token.ADD -> Node.Expr.UnaryOp.Token.POS
                Token.SUB -> Node.Expr.UnaryOp.Token.NEG
                Token.INC -> Node.Expr.UnaryOp.Token.INC
                Token.DEC -> Node.Expr.UnaryOp.Token.DEC
                Token.NOT -> Node.Expr.UnaryOp.Token.NOT
                else -> error("Unrecognized op: ${v.op}")
            },
            prefix = v.op != Token.INC && v.op != Token.DEC
        )
    }

    fun Context.compileValueSpecConst(v: ValueSpec, topLevel: Boolean) = v.names.zip(v.values) { id, value ->
        // Consts are always declared inline, and use a const val if possible
        val typeConst = (value.expr?.typeRef?.type as? Type_.Type.TypeConst)?.typeConst
        typeConst ?: error("Unable to find type const")
        val basicKind = (typeConst.type?.type as? Type_.Type.TypeBasic)?.typeBasic?.kind
        basicKind ?: error("Unable to find basic kind")
        val constVal = when (basicKind) {
            TypeBasic.Kind.UNTYPED_FLOAT, TypeBasic.Kind.FLOAT_32, TypeBasic.Kind.FLOAT_64 ->
                (typeConst.value?.value as ConstantValue.Value.Float_).
                    float.untypedFloatClass() != BigDecimal::class
            TypeBasic.Kind.UNTYPED_INT ->
                (typeConst.value?.value as ConstantValue.Value.Int_).int.untypedIntClass() != BigInteger::class
            // TODO: others
            else ->
                true
        }
        property(
            mods = if (constVal) listOf(Node.Modifier.Keyword.CONST.toMod()) else emptyList(),
            readOnly = true,
            vars = listOf(Node.Decl.Property.Var(id.name.javaIdent, null)),
            expr = compileConstantValue(typeConst)
        )
    }

    fun Context.compileValueSpecVar(v: ValueSpec, topLevel: Boolean): List<Node.Decl> {
        return v.names.mapIndexed { index, id ->
            val type = (id.defTypeRef?.type as? Type_.Type.TypeVar)?.typeVar?.namedType ?: error("Can't find var type")
            // Top level vars are never inited on their own. Instead they are inited in a separate init area. Therefore,
            // we must mark 'em lateinit. But lateinit is only for non-primitive, non-null types. Otherwise we just init
            // to the 0 value.
            var needsLateinit = topLevel && !type.isJavaPrimitive && !type.isNullable
            val value = v.values.getOrNull(index)
            // It also doesn't need late init if it's an array w/ no value
            if (needsLateinit && type.isArray && value == null) needsLateinit = false
            property(
                mods = if (needsLateinit) listOf(Node.Modifier.Keyword.LATEINIT.toMod()) else emptyList(),
                // We only put the type if it's top level or explicitly specified
                vars = listOf(Node.Decl.Property.Var(
                    name = id.name.javaIdent,
                    type = if (topLevel || v.type != null) compileType(type) else null
                )),
                expr =
                    if (needsLateinit) null
                    else if (topLevel || value == null) compileTypeZeroExpr(type)
                    else compileExpr(value)
            )
        }
    }

    data class KotlinPackage(
        val files: Map<String, Node.File>
    )

    companion object : Compiler()
}