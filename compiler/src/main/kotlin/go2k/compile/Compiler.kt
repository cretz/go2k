package go2k.compile

import go2k.compile.dumppb.*
import go2k.runtime.GoStruct
import go2k.runtime.Ops
import go2k.runtime.builtin.sliceIntArray
import go2k.runtime.runMain
import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger

@ExperimentalUnsignedTypes
open class Compiler {

    fun Context.compileAssignStmt(v: AssignStmt): Node.Stmt {
        // For definitions, it's a property instead with no explicit type
        if (v.tok == Token.DEFINE) {
            val singleLhs = v.lhs.singleOrNull() ?: TODO("multi assign")
            val ident = (singleLhs.expr as? Expr_.Expr.Ident)?.ident ?: TODO("multi assign")
            val singleRhs = v.rhs.singleOrNull() ?: TODO("multi assign")
            return Node.Stmt.Decl(
                property(
                    vars = listOf(propVar(ident.name.javaIdent)),
                    expr = compileExpr(singleRhs).convertType(singleRhs, singleLhs)
                )
            )
        }

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

        val singleLhs = v.lhs.singleOrNull() ?: TODO("multi assign")
        val singleRhs = rhs.singleOrNull() ?: TODO("multi assign")

        return Node.Stmt.Expr(binaryOp(
            lhs = compileExpr(singleLhs),
            op = when (tok) {
                Token.ASSIGN -> Node.Expr.BinaryOp.Token.ASSN
                Token.ADD_ASSIGN -> Node.Expr.BinaryOp.Token.ADD_ASSN
                Token.SUB_ASSIGN -> Node.Expr.BinaryOp.Token.SUB_ASSN
                Token.MUL_ASSIGN -> Node.Expr.BinaryOp.Token.MUL_ASSN
                Token.QUO_ASSIGN -> Node.Expr.BinaryOp.Token.DIV_ASSN
                Token.REM_ASSIGN -> Node.Expr.BinaryOp.Token.MOD_ASSN
                else -> error("Unrecognized token: $tok")
            },
            rhs = compileExpr(singleRhs).convertType(singleRhs, singleLhs)
        ))
    }

    fun Context.compileBasicLit(v: BasicLit) =  when (v.kind) {
        Token.INT, Token.FLOAT -> compileConstantValue(v.typeRef?.typeConst!!)
        Token.IMAG -> TODO()
        Token.CHAR -> constExpr(v.value, Node.Expr.Const.Form.CHAR)
        Token.STRING -> v.typeRef?.typeConst?.constString?.toStringTmpl() ?: error("Invalid const string")
        else -> error("Unrecognized lit kind: ${v.kind}")
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

    fun Context.compileCallExpr(v: CallExpr): Node.Expr {
        val funType = v.`fun`!!.expr!!.typeRef!!.namedType
        // If the function is a const type, it's a conversion instead of a function call
        return if (funType.type is Type_.Type.TypeConst) {
            // Must be a singular arg
            val arg = v.args.singleOrNull() ?: error("Expecting single conversion arg")
            compileExpr(arg).convertType(arg, funType.type.typeConst.type!!.namedType)
        } else call(
            expr = compileExpr(v.`fun`),
            // We choose to have vararg params be slices instead of supporting
            // Kotlin splats which only work on arrays
            args = (funType.convType() as TypeConverter.Type.Func).let { funConvType ->
                v.args.mapIndexed { index, arg ->
                    val argType = funConvType.params.getOrNull(index) ?:
                        funConvType.params.lastOrNull()?.takeIf { funConvType.vararg } ?:
                        error("Missing arg type for index $index")
                    valueArg(expr = compileExpr(arg).convertType(arg, argType))
                }
            }
        )
    }

    fun Context.compileCompositeLit(v: CompositeLit) = when (v.type?.expr) {
        is Expr_.Expr.ArrayType -> {
            if (v.type.expr.arrayType.len != null) TODO("array lit")
            val sliceType = v.typeRef!!.namedType.convType() as TypeConverter.Type.Slice
            val (createArrayFun, sliceArrayFun) = when (sliceType.elemType) {
                is TypeConverter.Type.Primitive -> when (sliceType.elemType.cls) {
                    Int::class -> "kotlin.intArrayOf".funcRef() to "go2k.runtime.builtin.sliceIntArray".funcRef()
                    else -> TODO()
                }
                else -> TODO()
            }
            call(
                expr = sliceArrayFun,
                args = listOf(valueArg(expr = call(
                    expr = createArrayFun,
                    args = v.elts.map { valueArg(expr = compileExpr(it).convertType(it, sliceType.elemType)) }
                )))
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
        is Expr_.Expr.SliceExpr -> TODO()
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

    fun Context.compileIfStmt(v: IfStmt) = Node.Stmt.Expr(run {
        if (v.init != null) TODO()
        Node.Expr.If(
            expr = compileExpr(v.cond!!),
            body = Node.Expr.Brace(emptyList(), compileBlockStmt(v.body!!)),
            elseBody = v.`else`?.let { elseBody ->
                (compileStmt(elseBody).firstOrNull() as? Node.Stmt.Expr)?.expr ?: error("Expected single expr stmt")
            }
        )
    })

    fun Context.compileIncDecStmt(v: IncDecStmt) = Node.Stmt.Expr(
        Node.Expr.UnaryOp(
            expr = compileExpr(v.x!!),
            oper = Node.Expr.UnaryOp.Oper(
                if (v.tok == Token.INC) Node.Expr.UnaryOp.Token.INC else Node.Expr.UnaryOp.Token.DEC
            ),
            prefix = false
        )
    )

    fun Context.compileIndexExpr(v: IndexExpr): Node.Expr = TODO()

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
            // TODO: what about values that are not there or are zero?
            it.names.zip(it.values) { name, value ->
                name.name to Node.Stmt.Expr(
                    binaryOp(
                        lhs = compileIdent(name),
                        op = Node.Expr.BinaryOp.Token.ASSN,
                        rhs = compileExpr(value)
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

    fun Context.compileReturnStmt(v: ReturnStmt) = Node.Stmt.Expr(
        Node.Expr.Return(
            label = null,
            expr = v.results.let {
                if (it.size > 1) TODO()
                it.singleOrNull()?.let { compileExpr(it) }
            }
        )
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
        is Stmt_.Stmt.AssignStmt -> listOf(compileAssignStmt(v.assignStmt))
        is Stmt_.Stmt.GoStmt -> TODO()
        is Stmt_.Stmt.DeferStmt -> TODO()
        is Stmt_.Stmt.ReturnStmt -> listOf(compileReturnStmt(v.returnStmt))
        is Stmt_.Stmt.BranchStmt -> TODO()
        is Stmt_.Stmt.BlockStmt -> TODO()
        is Stmt_.Stmt.IfStmt -> listOf(compileIfStmt(v.ifStmt))
        is Stmt_.Stmt.CaseClause -> TODO()
        is Stmt_.Stmt.SwitchStmt -> TODO()
        is Stmt_.Stmt.TypeSwitchStmt -> TODO()
        is Stmt_.Stmt.CommClause -> TODO()
        is Stmt_.Stmt.SelectStmt -> TODO()
        is Stmt_.Stmt.ForStmt -> TODO()
        is Stmt_.Stmt.RangeStmt -> TODO()
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
        is Type_.Type.TypeArray -> TODO()
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
            val needsLateinit = topLevel && !type.isJavaPrimitive && !type.isNullable
            val value = v.values.getOrNull(index)
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