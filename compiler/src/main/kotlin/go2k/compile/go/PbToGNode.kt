package go2k.compile.go

import go2k.compile.go.dumppb.*

class PbToGNode(val pkg: Package) {
    val lazyTypes = arrayOfNulls<GNode.Type>(pkg.types.size)

    fun convertDecl(v: Decl_) = convertDecl(v.decl!!)
    fun convertDecl(v: Decl_.Decl) = when (v) {
        is Decl_.Decl.BadDecl -> error("Bad decl: $v")
        is Decl_.Decl.FuncDecl -> GNode.Decl.Func(
            recv = v.funcDecl.recv?.list.orEmpty().map(::convertField),
            name = v.funcDecl.name!!.name,
            type = convertExprFuncType(v.funcDecl.type!!),
            body = convertStmtBlock(v.funcDecl.body!!)
        )
        is Decl_.Decl.GenDecl -> when (v.genDecl.tok) {
            Token.CONST -> GNode.Decl.Const(v.genDecl.specs.map { convertSpec(it) as GNode.Spec.Value })
            Token.IMPORT -> GNode.Decl.Import(v.genDecl.specs.map { convertSpec(it) as GNode.Spec.Import })
            Token.TYPE -> GNode.Decl.Type(v.genDecl.specs.map { convertSpec(it) as GNode.Spec.Type })
            Token.VAR -> GNode.Decl.Var(v.genDecl.specs.map { convertSpec(it) as GNode.Spec.Value })
            else -> error("Unknown gen decl token ${v.genDecl.tok}")
        }
    }

    fun convertExpr(v: Expr_) = convertExpr(v.expr!!)
    fun convertExpr(v: Expr_.Expr): GNode.Expr = when (v) {
        is Expr_.Expr.ArrayType -> GNode.Expr.ArrayType(
            type = v.arrayType.typeRef?.let(::convertType),
            len = v.arrayType.len?.let(::convertExpr),
            elt = convertExpr(v.arrayType.elt!!)
        )
        is Expr_.Expr.BadExpr -> error("Bad expr: $v")
        is Expr_.Expr.BasicLit -> GNode.Expr.BasicLit(
            type = v.basicLit.typeRef?.let(::convertType),
            kind = when (v.basicLit.kind) {
                Token.CHAR -> GNode.Expr.BasicLit.Kind.CHAR
                Token.FLOAT -> GNode.Expr.BasicLit.Kind.FLOAT
                Token.IMAG -> GNode.Expr.BasicLit.Kind.IMAG
                Token.INT -> GNode.Expr.BasicLit.Kind.INT
                Token.STRING -> GNode.Expr.BasicLit.Kind.STRING
                else -> error("Unknown kind ${v.basicLit.kind}")
            },
            value = v.basicLit.value
        )
        is Expr_.Expr.BinaryExpr -> GNode.Expr.Binary(
            type = v.binaryExpr.typeRef?.let(::convertType),
            x = convertExpr(v.binaryExpr.x!!),
            op = when (v.binaryExpr.op) {
                Token.ADD -> GNode.Expr.Binary.Token.ADD
                Token.AND -> GNode.Expr.Binary.Token.AND
                Token.AND_NOT -> GNode.Expr.Binary.Token.AND_NOT
                Token.EQL -> GNode.Expr.Binary.Token.EQL
                Token.GEQ -> GNode.Expr.Binary.Token.GEQ
                Token.GTR -> GNode.Expr.Binary.Token.GTR
                Token.LAND -> GNode.Expr.Binary.Token.LAND
                Token.LEQ -> GNode.Expr.Binary.Token.LEQ
                Token.LOR -> GNode.Expr.Binary.Token.LOR
                Token.LSS -> GNode.Expr.Binary.Token.LSS
                Token.MUL -> GNode.Expr.Binary.Token.MUL
                Token.NEQ -> GNode.Expr.Binary.Token.NEQ
                Token.OR -> GNode.Expr.Binary.Token.OR
                Token.QUO -> GNode.Expr.Binary.Token.QUO
                Token.REM -> GNode.Expr.Binary.Token.REM
                Token.SHL -> GNode.Expr.Binary.Token.SHL
                Token.SHR -> GNode.Expr.Binary.Token.SHR
                Token.SUB -> GNode.Expr.Binary.Token.SUB
                Token.XOR -> GNode.Expr.Binary.Token.XOR
                else -> error("Unknown binary op ${v.binaryExpr.op}")
            },
            y = convertExpr(v.binaryExpr.y!!)
        )
        is Expr_.Expr.CallExpr -> convertExprCall(v.callExpr)
        is Expr_.Expr.ChanType -> GNode.Expr.ChanType(
            type = v.chanType.typeRef?.let(::convertType),
            value = convertExpr(v.chanType.value!!),
            canSend = v.chanType.sendDir,
            canRecv = v.chanType.recvDir
        )
        is Expr_.Expr.CompositeLit -> GNode.Expr.CompositeLit(
            type = v.compositeLit.typeRef?.let(::convertType),
            litType = v.compositeLit.type?.let(::convertExpr),
            elts = v.compositeLit.elts.map(::convertExpr)
        )
        is Expr_.Expr.Ellipsis -> GNode.Expr.Ellipsis(
            type = v.ellipsis.typeRef?.let(::convertType),
            elt = v.ellipsis.elt?.let(::convertExpr)
        )
        is Expr_.Expr.FuncLit -> GNode.Expr.FuncLit(
            type = v.funcLit.typeRef?.let(::convertType),
            funcType = convertExprFuncType(v.funcLit.type!!),
            body = convertStmtBlock(v.funcLit.body!!)
        )
        is Expr_.Expr.FuncType -> convertExprFuncType(v.funcType)
        is Expr_.Expr.Ident -> convertExprIdent(v.ident)
        is Expr_.Expr.IndexExpr -> GNode.Expr.Index(
            type = v.indexExpr.typeRef?.let(::convertType),
            x = convertExpr(v.indexExpr.x!!),
            index = convertExpr(v.indexExpr.index!!)
        )
        is Expr_.Expr.InterfaceType -> GNode.Expr.InterfaceType(
            type = v.interfaceType.typeRef?.let(::convertType),
            methods = v.interfaceType.methods?.list.orEmpty().map(::convertField)
        )
        is Expr_.Expr.KeyValueExpr -> GNode.Expr.KeyValue(
            type = v.keyValueExpr.typeRef?.let(::convertType),
            key = convertExpr(v.keyValueExpr.key!!),
            value = convertExpr(v.keyValueExpr.value!!)
        )
        is Expr_.Expr.MapType -> GNode.Expr.MapType(
            type = v.mapType.typeRef?.let(::convertType),
            key = convertExpr(v.mapType.key!!),
            value = convertExpr(v.mapType.value!!)
        )
        is Expr_.Expr.ParenExpr -> GNode.Expr.Paren(
            type = v.parenExpr.typeRef?.let(::convertType),
            x = convertExpr(v.parenExpr.x!!)
        )
        is Expr_.Expr.SelectorExpr -> GNode.Expr.Selector(
            type = v.selectorExpr.typeRef?.let(::convertType),
            x = convertExpr(v.selectorExpr.x!!),
            sel = convertExprIdent(v.selectorExpr.sel!!)
        )
        is Expr_.Expr.SliceExpr -> GNode.Expr.Slice(
            type = v.sliceExpr.typeRef?.let(::convertType),
            x = convertExpr(v.sliceExpr.x!!),
            low = v.sliceExpr.low?.let(::convertExpr),
            high = v.sliceExpr.high?.let(::convertExpr),
            max = v.sliceExpr.max?.let(::convertExpr),
            slice3 = v.sliceExpr.slice3
        )
        is Expr_.Expr.StarExpr -> GNode.Expr.Star(
            type = v.starExpr.typeRef?.let(::convertType),
            x = convertExpr(v.starExpr.x!!)
        )
        is Expr_.Expr.StructType -> GNode.Expr.StructType(
            type = v.structType.typeRef?.let(::convertType),
            fields = v.structType.fields?.list.orEmpty().map(::convertField)
        )
        is Expr_.Expr.TypeAssertExpr -> GNode.Expr.TypeAssert(
            type = v.typeAssertExpr.typeRef?.let(::convertType),
            x = convertExpr(v.typeAssertExpr.x!!),
            assertType = v.typeAssertExpr.type?.let(::convertExpr)
        )
        is Expr_.Expr.UnaryExpr -> GNode.Expr.Unary(
            type = v.unaryExpr.typeRef?.let(::convertType),
            token = when (v.unaryExpr.op) {
                Token.ADD -> GNode.Expr.Unary.Token.ADD
                Token.AND -> GNode.Expr.Unary.Token.AND
                Token.ARROW -> GNode.Expr.Unary.Token.ARROW
                Token.DEC -> GNode.Expr.Unary.Token.DEC
                Token.INC -> GNode.Expr.Unary.Token.INC
                Token.NOT -> GNode.Expr.Unary.Token.NOT
                Token.SUB -> GNode.Expr.Unary.Token.SUB
                Token.XOR -> GNode.Expr.Unary.Token.XOR
                else -> error("Unknown unary op ${v.unaryExpr.op}")
            },
            x = convertExpr(v.unaryExpr.x!!)
        )
    }

    fun convertExprCall(v: CallExpr) = GNode.Expr.Call(
        type = v.typeRef?.let(::convertType),
        func = convertExpr(v.`fun`!!),
        args = v.args.map(::convertExpr)
    )

    fun convertExprFuncType(v: FuncType) = GNode.Expr.FuncType(
        type = v.typeRef?.let(::convertType),
        params = v.params?.list.orEmpty().map(::convertField),
        results = v.results?.list.orEmpty().map(::convertField)
    )

    fun convertExprIdent(v: Ident) = GNode.Expr.Ident(
        type = v.typeRef?.let(::convertType),
        name = v.name,
        defType = v.defTypeRef?.let(::convertType)
    )

    fun convertField(v: Field) = GNode.Field(
        names = v.names.map(::convertExprIdent),
        type = convertExpr(v.type!!),
        tag = v.tag?.value
    )

    fun convertFile(v: File) = GNode.File(
        fileName = v.fileName,
        packageName = v.name!!.name,
        decls = v.decls.map(::convertDecl)
    )

    fun convertPackage() = GNode.Package(
        name = pkg.name,
        path = pkg.path,
        files = pkg.files.map(::convertFile),
        types = lazyTypes.filterNotNull(),
        varInitOrder = pkg.varInitOrder
    )
    
    fun convertSpec(v: Spec_) = convertSpec(v.spec!!)
    fun convertSpec(v: Spec_.Spec): GNode.Spec = when (v) {
        is Spec_.Spec.ImportSpec -> GNode.Spec.Import(
            name = v.importSpec.name?.name,
            path = v.importSpec.path!!.value
        )
        is Spec_.Spec.TypeSpec -> GNode.Spec.Type(
            name = v.typeSpec.name!!.name,
            expr = convertExpr(v.typeSpec.type!!),
            alias = v.typeSpec.assign > 0
        )
        is Spec_.Spec.ValueSpec -> GNode.Spec.Value(
            names = v.valueSpec.names.map(::convertExprIdent),
            type = v.valueSpec.type?.let(::convertExpr),
            values = v.valueSpec.values.map(::convertExpr)
        )
    }

    fun convertStmt(v: Stmt_) = convertStmt(v.stmt!!)
    fun convertStmt(v: Stmt_.Stmt): GNode.Stmt = when (v) {
        is Stmt_.Stmt.AssignStmt -> GNode.Stmt.Assign(
            lhs = v.assignStmt.lhs.map(::convertExpr),
            tok = when (v.assignStmt.tok) {
                Token.ADD_ASSIGN -> GNode.Stmt.Assign.Token.ADD
                Token.AND_ASSIGN -> GNode.Stmt.Assign.Token.AND
                Token.AND_NOT_ASSIGN -> GNode.Stmt.Assign.Token.AND_NOT
                Token.ASSIGN -> GNode.Stmt.Assign.Token.ASSIGN
                Token.DEFINE -> GNode.Stmt.Assign.Token.DEFINE
                Token.MUL_ASSIGN -> GNode.Stmt.Assign.Token.MUL
                Token.OR_ASSIGN -> GNode.Stmt.Assign.Token.OR
                Token.QUO_ASSIGN -> GNode.Stmt.Assign.Token.QUO
                Token.REM_ASSIGN -> GNode.Stmt.Assign.Token.REM
                Token.SHL_ASSIGN -> GNode.Stmt.Assign.Token.SHL
                Token.SHR_ASSIGN -> GNode.Stmt.Assign.Token.SHR
                Token.SUB_ASSIGN -> GNode.Stmt.Assign.Token.SUB
                Token.XOR_ASSIGN -> GNode.Stmt.Assign.Token.XOR
                else -> error("Unknown assign token ${v.assignStmt.tok}")
            },
            rhs = v.assignStmt.rhs.map(::convertExpr)
        )
        is Stmt_.Stmt.BadStmt -> error("Bad stmt: $v")
        is Stmt_.Stmt.BlockStmt -> convertStmtBlock(v.blockStmt)
        is Stmt_.Stmt.BranchStmt -> GNode.Stmt.Branch(
            tok = when (v.branchStmt.tok) {
                Token.BREAK -> GNode.Stmt.Branch.Token.BREAK
                Token.CONTINUE -> GNode.Stmt.Branch.Token.CONTINUE
                Token.FALLTHROUGH -> GNode.Stmt.Branch.Token.FALLTHROUGH
                Token.GOTO -> GNode.Stmt.Branch.Token.GOTO
                else -> error("Unknown branch token ${v.branchStmt.tok}")
            },
            label = v.branchStmt.label?.let(::convertExprIdent)
        )
        is Stmt_.Stmt.CaseClause -> error("Case clauses aren't statements")
        is Stmt_.Stmt.CommClause -> error("Comm clauses aren't statements")
        is Stmt_.Stmt.DeclStmt -> GNode.Stmt.Decl(
            decl = convertDecl(v.declStmt.decl!!)
        )
        is Stmt_.Stmt.DeferStmt -> GNode.Stmt.Defer(
            call = convertExprCall(v.deferStmt.call!!)
        )
        is Stmt_.Stmt.EmptyStmt -> GNode.Stmt.Empty
        is Stmt_.Stmt.ExprStmt -> GNode.Stmt.Expr(
            x = convertExpr(v.exprStmt.x!!)
        )
        is Stmt_.Stmt.ForStmt -> GNode.Stmt.For(
            init = v.forStmt.init?.let(::convertStmt),
            cond = v.forStmt.cond?.let(::convertExpr),
            post = v.forStmt.post?.let(::convertStmt),
            body = convertStmtBlock(v.forStmt.body!!)
        )
        is Stmt_.Stmt.GoStmt -> GNode.Stmt.Go(
            call = convertExprCall(v.goStmt.call!!)
        )
        is Stmt_.Stmt.IfStmt -> GNode.Stmt.If(
            init = v.ifStmt.init?.let(::convertStmt),
            cond = convertExpr(v.ifStmt.cond!!),
            body = convertStmtBlock(v.ifStmt.body!!),
            elseStmt = v.ifStmt.`else`?.let(::convertStmt)
        )
        is Stmt_.Stmt.IncDecStmt -> GNode.Stmt.IncDec(
            x = convertExpr(v.incDecStmt.x!!),
            inc = v.incDecStmt.tok == Token.INC
        )
        is Stmt_.Stmt.LabeledStmt -> GNode.Stmt.Labeled(
            label = convertExprIdent(v.labeledStmt.label!!),
            stmt = convertStmt(v.labeledStmt.stmt!!)
        )
        is Stmt_.Stmt.RangeStmt -> GNode.Stmt.Range(
            key = v.rangeStmt.key?.let(::convertExpr),
            value = v.rangeStmt.value?.let(::convertExpr),
            define = v.rangeStmt.tok == Token.DEFINE,
            x = convertExpr(v.rangeStmt.x!!),
            body = convertStmtBlock(v.rangeStmt.body!!)
        )
        is Stmt_.Stmt.ReturnStmt -> GNode.Stmt.Return(
            results = v.returnStmt.results.map(::convertExpr)
        )
        is Stmt_.Stmt.SelectStmt -> GNode.Stmt.Select(
            cases = v.selectStmt.body!!.list.map {
                val clause = it.stmt as Stmt_.Stmt.CommClause
                GNode.Stmt.Select.CommClause(
                    comm = clause.commClause.comm?.let(::convertStmt),
                    body = clause.commClause.body.map(::convertStmt)
                )
            }
        )
        is Stmt_.Stmt.SendStmt -> GNode.Stmt.Send(
            chan = convertExpr(v.sendStmt.chan!!),
            value = convertExpr(v.sendStmt.value!!)
        )
        is Stmt_.Stmt.SwitchStmt -> GNode.Stmt.Switch(
            init = v.switchStmt.init?.let(::convertStmt),
            tag = v.switchStmt.tag?.let { GNode.Stmt.Expr(convertExpr(it)) },
            type = false,
            cases = convertStmtSwitchCaseBody(v.switchStmt.body!!)
        )
        is Stmt_.Stmt.TypeSwitchStmt -> GNode.Stmt.Switch(
            init = v.typeSwitchStmt.init?.let(::convertStmt),
            tag = convertStmt(v.typeSwitchStmt.assign!!),
            type = true,
            cases = convertStmtSwitchCaseBody(v.typeSwitchStmt.body!!)
        )
    }

    fun convertStmtBlock(v: BlockStmt) = GNode.Stmt.Block(v.list.map(::convertStmt))

    fun convertStmtSwitchCaseBody(v: BlockStmt) = v.list.map {
        (it.stmt as Stmt_.Stmt.CaseClause).caseClause.let {
            GNode.Stmt.Switch.CaseClause(
                list = it.list.map(::convertExpr),
                body = it.body.map(::convertStmt)
            )
        }
    }
    
    fun convertType(v: TypeRef) = convertTypeId(v.id)
    fun convertTypeId(v: Int) = lazyTypes[v] ?: convertType(pkg.types[v]).also { lazyTypes[v] = it }

    fun convertType(v: Type_): GNode.Type = when (val t = v.type!!) {
        is Type_.Type.TypeArray -> GNode.Type.Array(
            elem = convertType(t.typeArray.elem!!),
            len = t.typeArray.len
        )
        is Type_.Type.TypeBasic -> GNode.Type.Basic(v.name, when (t.typeBasic.kind) {
            TypeBasic.Kind.INVALID -> GNode.Type.Basic.Kind.INVALID
            TypeBasic.Kind.BOOL -> GNode.Type.Basic.Kind.BOOL
            TypeBasic.Kind.INT -> GNode.Type.Basic.Kind.INT
            TypeBasic.Kind.INT_8 -> GNode.Type.Basic.Kind.INT_8
            TypeBasic.Kind.INT_16 -> GNode.Type.Basic.Kind.INT_16
            TypeBasic.Kind.INT_32 -> GNode.Type.Basic.Kind.INT_32
            TypeBasic.Kind.INT_64 -> GNode.Type.Basic.Kind.INT_64
            TypeBasic.Kind.UINT -> GNode.Type.Basic.Kind.UINT
            TypeBasic.Kind.UINT_8 -> GNode.Type.Basic.Kind.UINT_8
            TypeBasic.Kind.UINT_16 -> GNode.Type.Basic.Kind.UINT_16
            TypeBasic.Kind.UINT_32 -> GNode.Type.Basic.Kind.UINT_32
            TypeBasic.Kind.UINT_64 -> GNode.Type.Basic.Kind.UINT_64
            TypeBasic.Kind.UINT_PTR -> GNode.Type.Basic.Kind.UINT_PTR
            TypeBasic.Kind.FLOAT_32 -> GNode.Type.Basic.Kind.FLOAT_32
            TypeBasic.Kind.FLOAT_64 -> GNode.Type.Basic.Kind.FLOAT_64
            TypeBasic.Kind.COMPLEX_64 -> GNode.Type.Basic.Kind.COMPLEX_64
            TypeBasic.Kind.COMPLEX_128 -> GNode.Type.Basic.Kind.COMPLEX_128
            TypeBasic.Kind.STRING -> GNode.Type.Basic.Kind.STRING
            TypeBasic.Kind.UNSAFE_POINTER -> GNode.Type.Basic.Kind.UNSAFE_POINTER
            TypeBasic.Kind.UNTYPED_BOOL -> GNode.Type.Basic.Kind.UNTYPED_BOOL
            TypeBasic.Kind.UNTYPED_INT -> GNode.Type.Basic.Kind.UNTYPED_INT
            TypeBasic.Kind.UNTYPED_RUNE -> GNode.Type.Basic.Kind.UNTYPED_RUNE
            TypeBasic.Kind.UNTYPED_FLOAT -> GNode.Type.Basic.Kind.UNTYPED_FLOAT
            TypeBasic.Kind.UNTYPED_COMPLEX -> GNode.Type.Basic.Kind.UNTYPED_COMPLEX
            TypeBasic.Kind.UNTYPED_STRING -> GNode.Type.Basic.Kind.UNTYPED_STRING
            TypeBasic.Kind.UNTYPED_NIL -> GNode.Type.Basic.Kind.UNTYPED_NIL
            else -> error("Invalid kind ${t.typeBasic.kind}")
        })
        is Type_.Type.TypeBuiltin -> GNode.Type.BuiltIn(
            pkg = v.pkg,
            name = v.name,
            type = null
        )
        is Type_.Type.TypeChan -> GNode.Type.Chan(
            elem = convertType(t.typeChan.elem!!),
            canSend = t.typeChan.sendDir,
            canRecv = t.typeChan.recvDir
        )
        is Type_.Type.TypeConst -> GNode.Type.Const(
            pkg = v.pkg,
            name = v.name,
            type = t.typeConst.type?.let(::convertType) ?: GNode.Type.Nil,
            value = when (val value = t.typeConst.value?.value) {
                null -> null
                is ConstantValue.Value.Unknown -> error("Unknown const")
                is ConstantValue.Value.Bool -> GNode.Const.Boolean(value.bool)
                is ConstantValue.Value.Complex -> GNode.Const.Complex(value.complex)
                is ConstantValue.Value.Float_ -> GNode.Const.Float(value.float)
                is ConstantValue.Value.Int_ -> GNode.Const.Int(value.int)
                is ConstantValue.Value.String_ -> GNode.Const.String(value.string)
            }
        )
        is Type_.Type.TypeFunc -> GNode.Type.Func(
            pkg = v.pkg,
            name = v.name,
            type = convertTypeSignature(t.typeFunc)
        )
        is Type_.Type.TypeInterface -> GNode.Type.Interface(
            methods = t.typeInterface.explicitMethods.map { convertType(it) as GNode.Type.Func },
            embeddeds = t.typeInterface.embedded.map(::convertType)
        )
        is Type_.Type.TypeLabel -> GNode.Type.Label(
            pkg = v.pkg,
            name = v.name,
            type = null
        )
        is Type_.Type.TypeMap -> GNode.Type.Map(
            elem = convertType(t.typeMap.elem!!),
            key = convertType(t.typeMap.key!!)
        )
        is Type_.Type.TypeName -> GNode.Type.TypeName(
            pkg = v.pkg,
            name = v.name,
            type = convertType(t.typeName)
        )
        is Type_.Type.TypeNamed -> GNode.Type.Named(
            // Has recursion
            name = convertTypeMaybeLazy(t.typeNamed.typeName!!) as GNode.Type.MaybeLazy<GNode.Type.TypeName>,
            underlying = convertType(t.typeNamed.type!!),
            methods = t.typeNamed.methods.map { convertType(it) as GNode.Type.Func }
        )
        is Type_.Type.TypeNil -> GNode.Type.Nil
        is Type_.Type.TypePackage -> GNode.Type.Package(
            name = v.name
        )
        is Type_.Type.TypePointer -> GNode.Type.Pointer(
            elem = convertType(t.typePointer.elem!!)
        )
        is Type_.Type.TypeSignature -> convertTypeSignature(t.typeSignature)
        is Type_.Type.TypeSlice -> GNode.Type.Slice(
            elem = convertType(t.typeSlice.elem!!)
        )
        is Type_.Type.TypeStruct -> GNode.Type.Struct(
            fields = t.typeStruct.fields.map { convertType(it) as GNode.Type.Var },
            tags = emptyList() // TODO
        )
        is Type_.Type.TypeTuple -> GNode.Type.Tuple(
            vars = t.typeTuple.vars.map { convertType(it) as GNode.Type.Var }
        )
        is Type_.Type.TypeVar -> GNode.Type.Var(
            pkg = v.pkg,
            name = v.name,
            lazyType = convertTypeMaybeLazy(t.typeVar.type!!),
            embedded = t.typeVar.embedded
        )
    }

    fun convertTypeMaybeLazy(v: TypeRef): GNode.Type.MaybeLazy<GNode.Type> {
        val maybeType = lazyTypes[v.id]
        return if (maybeType != null) GNode.Type.MaybeLazy.Eager(maybeType)
            else GNode.Type.MaybeLazy.Lazy { convertTypeId(v.id) }
    }

    fun convertTypeSignature(v: TypeSignature) = GNode.Type.Signature(
        // Has recursion
        recv = v.recv?.let { convertType(it) as GNode.Type.Var },
        params = v.params.map { convertType(it) as GNode.Type.Var },
        results = v.results.map { convertType(it) as GNode.Type.Var },
        variadic = v.variadic
    )

    val Type_.pkg get() = `package`.takeIf { it.isNotEmpty() }

    companion object {
        fun convertPackage(v: Package) = PbToGNode(v).convertPackage()
    }
}