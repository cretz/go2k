package go2k.compile

import go2k.compile.dumppb.*
import kastree.ast.Node

open class Compiler(val conf: Conf = Conf()) {

    fun compileAssignStmt(v: AssignStmt): Node.Stmt {
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

        return Node.Stmt.Expr(binaryOp(
            lhs = compileExpr(v.lhs.let { it.singleOrNull() ?: TODO("multi assign") }),
            op = when (tok) {
                Token.ASSIGN -> Node.Expr.BinaryOp.Token.ASSN
                Token.ADD_ASSIGN -> Node.Expr.BinaryOp.Token.ADD_ASSN
                Token.SUB_ASSIGN -> Node.Expr.BinaryOp.Token.SUB_ASSN
                Token.MUL_ASSIGN -> Node.Expr.BinaryOp.Token.MUL_ASSN
                Token.QUO_ASSIGN -> Node.Expr.BinaryOp.Token.DIV_ASSN
                Token.REM_ASSIGN -> Node.Expr.BinaryOp.Token.MOD_ASSN
                else -> error("Unrecognized token: $tok")
            },
            rhs = compileExpr(rhs.let { it.singleOrNull() ?: TODO("multi assign") })
        ))
    }

    fun compileBasicLit(v: BasicLit) = when (v.kind) {
        Token.INT -> Node.Expr.Const(v.value, Node.Expr.Const.Form.INT)
        Token.FLOAT -> Node.Expr.Const(v.value, Node.Expr.Const.Form.FLOAT)
        Token.IMAG -> TODO()
        Token.CHAR -> Node.Expr.Const(v.value, Node.Expr.Const.Form.CHAR)
        Token.STRING -> ((v.typeInfo?.type as? TypeInfo.Type.TypeConst)?.
            typeConst?.value?.value as? ConstantValue.Value.String_)?.
            string?.toStringTmpl() ?: error("Invalid const string")
        else -> error("Unrecognized lit kind: ${v.kind}")
    }

    fun compileBinaryExpr(v: BinaryExpr) = binaryOp(
        lhs = compileExpr(v.x!!),
        op = when (v.op) {
            Token.ADD -> Node.Expr.BinaryOp.Token.ADD
            else -> error("Unrecognized op ${v.op}")
        },
        rhs = compileExpr(v.y!!)
    )

    fun compileBlockStmt(v: BlockStmt) = Node.Block(v.list.map { compileStmt(it) })

    fun compileCallExpr(v: CallExpr) = call(
        expr = compileExpr(v.`fun`!!),
        args = v.args.map {
            // We choose to have vararg params be slices instead of supporting
            // Kotlin splats which only work on arrays
            Node.ValueArg(name = null, expr = compileExpr(it), asterisk = false)
        }
    )

    fun compileDeclTopLevel(v: Decl_.Decl) = when (v) {
        is Decl_.Decl.BadDecl -> error("Bad decl: $v")
        is Decl_.Decl.GenDecl -> compileGenDeclTopLevel(v.genDecl)
        is Decl_.Decl.FuncDecl -> listOf(compileFuncDeclTopLevel(v.funcDecl))
    }

    fun compileExpr(v: Expr_) = compileExpr(v.expr!!)
    fun compileExpr(v: Expr_.Expr): Node.Expr = when (v) {
        is Expr_.Expr.BadExpr -> TODO()
        is Expr_.Expr.Ident -> compileIdent(v.ident)
        is Expr_.Expr.Ellipsis -> TODO()
        is Expr_.Expr.BasicLit -> compileBasicLit(v.basicLit)
        is Expr_.Expr.FuncLit -> TODO()
        is Expr_.Expr.CompositeLit -> TODO()
        is Expr_.Expr.ParenExpr -> TODO()
        is Expr_.Expr.SelectorExpr -> TODO()
        is Expr_.Expr.IndexExpr -> TODO()
        is Expr_.Expr.SliceExpr -> TODO()
        is Expr_.Expr.TypeAssertExpr -> TODO()
        is Expr_.Expr.CallExpr -> compileCallExpr(v.callExpr)
        is Expr_.Expr.StarExpr -> TODO()
        is Expr_.Expr.UnaryExpr -> TODO()
        is Expr_.Expr.BinaryExpr -> compileBinaryExpr(v.binaryExpr)
        is Expr_.Expr.KeyValueExpr -> TODO()
        is Expr_.Expr.ArrayType -> TODO()
        is Expr_.Expr.StructType -> TODO()
        is Expr_.Expr.FuncType -> TODO()
        is Expr_.Expr.InterfaceType -> TODO()
        is Expr_.Expr.MapType -> TODO()
        is Expr_.Expr.ChanType -> TODO()
    }

    fun compileExprStmt(v: ExprStmt) = Node.Stmt.Expr(compileExpr(v.x!!))

    fun compileFile(v: File) = Node.File(
        anns = emptyList(),
        pkg = null,
        imports = emptyList(),
        decls = v.decls.flatMap { compileDeclTopLevel(it.decl!!) }
    )

    fun compileFuncDeclTopLevel(v: FuncDecl): Node.Decl.Func {
        var mods = listOf(Node.Modifier.Keyword.SUSPEND.toMod())
        if (v.name!!.name.first().isLowerCase()) mods += Node.Modifier.Keyword.INTERNAL.toMod()
        return func(
            mods = mods,
            name = v.name.name.javaIdent,
            // TODO
            params = emptyList(),
            type = v.type!!.results?.let {
                if (it.list.size != 1 || it.list.single().names.size > 1) TODO()
                val id = (it.list.first().type!!.expr as Expr_.Expr.Ident).ident
                compileTypeInfo(id.typeInfo!!)
            },
            body = v.body?.let { Node.Decl.Func.Body.Block(compileBlockStmt(it)) }
        )
    }

    fun compileGenDeclTopLevel(v: GenDecl) = v.specs.flatMap {
        compileSpecTopLevel(it.spec!!, v.tok == Token.CONST)
    }

    // TODO: fix builtin's to refer to the proper package
    fun compileIdent(v: Ident): Node.Expr {
        if (v.typeInfo?.type is TypeInfo.Type.TypeBuiltin) {
            return "go2k.runtime.${v.name}".toDottedExpr()
        }
        return v.name.javaName
    }

    fun compileIncDecStmt(v: IncDecStmt) = Node.Stmt.Expr(
        Node.Expr.UnaryOp(
            expr = compileExpr(v.x!!),
            oper = Node.Expr.UnaryOp.Oper(
                if (v.tok == Token.INC) Node.Expr.UnaryOp.Token.INC else Node.Expr.UnaryOp.Token.DEC
            ),
            prefix = false
        )
    )

    fun compilePackage(v: Package): KotlinPackage {
        // Compile all files...
        var initCount = 0
        val files = v.files.map {
            it.fileName.removeSuffix(".go") + ".kt" to compileFile(it).let {
                it.copy(
                    pkg = Node.Package(emptyList(), conf.namer.packageName(v.path, v.name).split('.')),
                    // Change all init functions to start with dollar sign and numbered
                    decls = it.decls.map { decl ->
                        (decl as? Node.Decl.Func)?.takeIf { it.name == "init" }?.copy(
                            mods = listOf(Node.Modifier.Keyword.PRIVATE.toMod()),
                            name = "\$init${++initCount}"
                        ) ?: decl
                    }
                )
            }
        }

        // Create initializers for the top level vars
        val topLevelValueSpecs = v.files.flatMap {
            it.decls.flatMap {
                (it.decl as? Decl_.Decl.GenDecl)?.genDecl?.specs?.mapNotNull {
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

        // Make a suspendable public package init in the last file that does var inits and calls init funcs
        val packageInit = func(
            mods = listOf(Node.Modifier.Keyword.SUSPEND.toMod()),
            name = "init",
            body = Node.Decl.Func.Body.Block(Node.Block(
                v.varInitOrder.mapNotNull {
                    varInitStmtsByName[it]
                } + (1..initCount).map { initNum -> Node.Stmt.Expr(call("\$init$initNum".javaName)) }
            ))
        )

        return KotlinPackage(
            files = (files.dropLast(1) + files.last().let {
                it.copy(second = it.second.copy(decls = it.second.decls + packageInit))
            }).toMap()
        )
    }

    fun compileReturnStmt(v: ReturnStmt) = Node.Stmt.Expr(
        Node.Expr.Return(
            label = null,
            expr = v.results.let {
                if (it.size > 1) TODO()
                it.singleOrNull()?.let { compileExpr(it) }
            }
        )
    )

    fun compileSpecTopLevel(v: Spec_.Spec, const: Boolean = false) = when (v) {
        is Spec_.Spec.ImportSpec -> TODO()
        is Spec_.Spec.ValueSpec -> compileValueSpecTopLevel(v.valueSpec, const)
        is Spec_.Spec.TypeSpec -> TODO()
    }

    fun compileStmt(v: Stmt_) = compileStmt(v.stmt!!)
    fun compileStmt(v: Stmt_.Stmt): Node.Stmt = when (v) {
        is Stmt_.Stmt.BadStmt -> TODO()
        is Stmt_.Stmt.DeclStmt -> TODO()
        is Stmt_.Stmt.EmptyStmt -> TODO()
        is Stmt_.Stmt.LabeledStmt -> TODO()
        is Stmt_.Stmt.ExprStmt -> compileExprStmt(v.exprStmt)
        is Stmt_.Stmt.SendStmt -> TODO()
        is Stmt_.Stmt.IncDecStmt -> compileIncDecStmt(v.incDecStmt)
        is Stmt_.Stmt.AssignStmt -> compileAssignStmt(v.assignStmt)
        is Stmt_.Stmt.GoStmt -> TODO()
        is Stmt_.Stmt.DeferStmt -> TODO()
        is Stmt_.Stmt.ReturnStmt -> compileReturnStmt(v.returnStmt)
        is Stmt_.Stmt.BranchStmt -> TODO()
        is Stmt_.Stmt.BlockStmt -> TODO()
        is Stmt_.Stmt.IfStmt -> TODO()
        is Stmt_.Stmt.CaseClause -> TODO()
        is Stmt_.Stmt.SwitchStmt -> TODO()
        is Stmt_.Stmt.TypeSwitchStmt -> TODO()
        is Stmt_.Stmt.CommClause -> TODO()
        is Stmt_.Stmt.SelectStmt -> TODO()
        is Stmt_.Stmt.ForStmt -> TODO()
        is Stmt_.Stmt.RangeStmt -> TODO()
    }

    fun compileTypeInfo(v: TypeInfo): Node.Type = when (v.type) {
        null -> TODO()
        is TypeInfo.Type.TypeArray -> TODO()
        is TypeInfo.Type.TypeBasic -> when (v.type.typeBasic.kind) {
            TypeBasic.Kind.BOOL, TypeBasic.Kind.UNTYPED_BOOL -> Boolean::class.toType()
            TypeBasic.Kind.INT -> Int::class.toType()
            TypeBasic.Kind.INT_8 -> Byte::class.toType()
            TypeBasic.Kind.INT_16 -> Short::class.toType()
            TypeBasic.Kind.INT_32 -> if (v.name == "rune") Char::class.toType() else Int::class.toType()
            TypeBasic.Kind.INT_64 -> Long::class.toType()
            TypeBasic.Kind.UINT, TypeBasic.Kind.UINT_32 -> UInt::class.toType()
            TypeBasic.Kind.UINT_8 -> UByte::class.toType()
            TypeBasic.Kind.UINT_16 -> UShort::class.toType()
            TypeBasic.Kind.UINT_64, TypeBasic.Kind.UINT_PTR -> ULong::class.toType()
            TypeBasic.Kind.FLOAT_32 -> Float::class.toType()
            TypeBasic.Kind.FLOAT_64 -> Double::class.toType()
            TypeBasic.Kind.COMPLEX_64 -> TODO()
            TypeBasic.Kind.COMPLEX_128, TypeBasic.Kind.UNTYPED_COMPLEX -> TODO()
            TypeBasic.Kind.STRING, TypeBasic.Kind.UNTYPED_STRING -> String::class.toType()
            TypeBasic.Kind.UNTYPED_INT -> TODO("Figure out how to handle untyped int, ideally based on value")
            TypeBasic.Kind.UNTYPED_RUNE -> Char::class.toType()
            TypeBasic.Kind.UNTYPED_FLOAT -> TODO("Figure out how to handle untyped int, ideally based on value")
            TypeBasic.Kind.UNTYPED_NIL -> TODO()
            else -> error("Unrecognized type kind: ${v.type.typeBasic.kind}")
        }
        is TypeInfo.Type.TypeBuiltin -> TODO()
        is TypeInfo.Type.TypeChan -> TODO()
        is TypeInfo.Type.TypeConst -> TODO()
        is TypeInfo.Type.TypeFunc -> TODO()
        is TypeInfo.Type.TypeInterface -> TODO()
        is TypeInfo.Type.TypeLabel -> TODO()
        is TypeInfo.Type.TypeMap -> TODO()
        is TypeInfo.Type.TypeName -> compileTypeInfo(v.type.typeName)
        is TypeInfo.Type.TypeNamed -> TODO()
        is TypeInfo.Type.TypeNil -> TODO()
        is TypeInfo.Type.TypePackage -> TODO()
        is TypeInfo.Type.TypePointer -> TODO()
        is TypeInfo.Type.TypeSignature -> TODO()
        is TypeInfo.Type.TypeSlice -> TODO()
        is TypeInfo.Type.TypeStruct -> TODO()
        is TypeInfo.Type.TypeTuple -> TODO()
        is TypeInfo.Type.TypeVar -> compileTypeInfo(v.type.typeVar)
    }

    fun compileTypeInfoZeroExpr(v: TypeInfo): Node.Expr = when (v.type) {
        null -> error("No type")
        is TypeInfo.Type.TypeArray -> TODO()
        is TypeInfo.Type.TypeBasic -> when (v.type.typeBasic.kind) {
            TypeBasic.Kind.BOOL, TypeBasic.Kind.UNTYPED_BOOL ->
                false.toConst()
            TypeBasic.Kind.INT, TypeBasic.Kind.INT_8, TypeBasic.Kind.INT_16, TypeBasic.Kind.INT_32,
                TypeBasic.Kind.INT_64, TypeBasic.Kind.UINT, TypeBasic.Kind.UINT_8, TypeBasic.Kind.UINT_16,
                TypeBasic.Kind.UINT_32, TypeBasic.Kind.UINT_64, TypeBasic.Kind.UINT_PTR, TypeBasic.Kind.UNTYPED_INT ->
                0.toConst()
            TypeBasic.Kind.FLOAT_32, TypeBasic.Kind.FLOAT_64, TypeBasic.Kind.UNTYPED_FLOAT ->
                0.0.toConst()
            TypeBasic.Kind.STRING, TypeBasic.Kind.UNTYPED_STRING ->
                "".toStringTmpl()
            TypeBasic.Kind.UNTYPED_RUNE ->
                0.toChar().toConst()
            TypeBasic.Kind.UNTYPED_NIL ->
                NullConst
            else ->
                error("Unrecognized type kind: ${v.type.typeBasic.kind}")
        }
        is TypeInfo.Type.TypeBuiltin -> TODO()
        is TypeInfo.Type.TypeChan, is TypeInfo.Type.TypeFunc, is TypeInfo.Type.TypeInterface,
            is TypeInfo.Type.TypeMap, is TypeInfo.Type.TypeNil, is TypeInfo.Type.TypePointer,
            is TypeInfo.Type.TypeSlice ->
            NullConst
        is TypeInfo.Type.TypeConst -> compileTypeInfoZeroExpr(v.type.typeConst.type!!)
        is TypeInfo.Type.TypeLabel -> compileTypeInfoZeroExpr(v.type.typeLabel)
        is TypeInfo.Type.TypeName -> compileTypeInfoZeroExpr(v.type.typeName)
        is TypeInfo.Type.TypeNamed -> compileTypeInfoZeroExpr(v.type.typeNamed.type!!)
        is TypeInfo.Type.TypePackage -> TODO()
        is TypeInfo.Type.TypeSignature -> TODO()
        is TypeInfo.Type.TypeStruct -> TODO()
        is TypeInfo.Type.TypeTuple -> TODO()
        is TypeInfo.Type.TypeVar -> compileTypeInfoZeroExpr(v.type.typeVar)
    }

    fun compileValueSpecTopLevel(v: ValueSpec, const: Boolean): List<Node.Decl> {
        if (const) TODO()
        return v.names.map { id ->
            val type = (id.defTypeInfo?.type as? TypeInfo.Type.TypeVar)?.typeVar ?: error("Can't find var type")
            // Vars are never inited on their own. Instead they are inited in a separate init area. Therefore, we
            // must mark 'em lateinit. But lateinit is only for non-primitive, non-null types. Otherwise we just
            // init to the 0 value.
            val needsLateinit = !type.isJavaPrimitive && !type.isNullable
            property(
                mods = if (needsLateinit) listOf(Node.Modifier.Keyword.LATEINIT.toMod()) else emptyList(),
                vars = listOf(Node.Decl.Property.Var(id.name.javaIdent, compileTypeInfo(type))),
                expr = if (needsLateinit) null else compileTypeInfoZeroExpr(type)
            )
        }
    }

    // TODO
    val String.javaIdent get() = this
    val String.javaName get() = Node.Expr.Name(javaIdent)

    data class KotlinPackage(
        val files: Map<String, Node.File>
    )

    data class Conf(
        val namer: Namer = Namer.Simple()
    )

    companion object : Compiler()
}