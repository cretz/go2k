package go2k.compile.go

open class GNodeVisitor {
    fun visit(v: GNode) = visit(v, v)

    protected open fun visit(v: GNode, parent: GNode) = v.run<GNode, Unit> {
        return when (this) {
            is GNode.Const -> { }
            is GNode.Decl.Const -> {
                visitChildren(specs)
            }
            is GNode.Decl.Func -> {
                visitChildren(recv)
                visitChildren(type)
                visitChildren(body)
            }
            is GNode.Decl.Import -> {
                visitChildren(specs)
            }
            is GNode.Decl.Type -> {
                visitChildren(specs)
            }
            is GNode.Decl.Var -> {
                visitChildren(specs)
            }
            is GNode.Expr.ArrayType -> {
                visitChildren(type)
                visitChildren(len)
                visitChildren(elt)
            }
            is GNode.Expr.BasicLit -> {
                visitChildren(type)
            }
            is GNode.Expr.Binary -> {
                visitChildren(type)
                visitChildren(x)
                visitChildren(y)
            }
            is GNode.Expr.Call -> {
                visitChildren(type)
                visitChildren(func)
                visitChildren(args)
            }
            is GNode.Expr.ChanType -> {
                visitChildren(type)
                visitChildren(value)
            }
            is GNode.Expr.CompositeLit -> {
                visitChildren(type)
                visitChildren(litType)
                visitChildren(elts)
            }
            is GNode.Expr.Ellipsis -> {
                visitChildren(type)
                visitChildren(elt)
            }
            is GNode.Expr.FuncLit -> {
                visitChildren(type)
                visitChildren(funcType)
                visitChildren(body)
            }
            is GNode.Expr.FuncType -> {
                visitChildren(type)
                visitChildren(params)
                visitChildren(results)
            }
            is GNode.Expr.Ident -> {
                visitChildren(type)
                visitChildren(defType)
            }
            is GNode.Expr.Index -> {
                visitChildren(type)
                visitChildren(x)
                visitChildren(index)
            }
            is GNode.Expr.InterfaceType -> {
                visitChildren(type)
                visitChildren(methods)
            }
            is GNode.Expr.KeyValue -> {
                visitChildren(type)
                visitChildren(key)
                visitChildren(value)
            }
            is GNode.Expr.MapType -> {
                visitChildren(type)
                visitChildren(key)
                visitChildren(value)
            }
            is GNode.Expr.Paren -> {
                visitChildren(type)
                visitChildren(x)
            }
            is GNode.Expr.Selector -> {
                visitChildren(type)
                visitChildren(x)
                visitChildren(sel)
            }
            is GNode.Expr.Slice -> {
                visitChildren(type)
                visitChildren(x)
                visitChildren(low)
                visitChildren(high)
                visitChildren(max)
            }
            is GNode.Expr.Star -> {
                visitChildren(type)
                visitChildren(x)
            }
            is GNode.Expr.StructType -> {
                visitChildren(type)
                visitChildren(fields)
            }
            is GNode.Expr.TypeAssert -> {
                visitChildren(type)
                visitChildren(x)
                visitChildren(assertType)
            }
            is GNode.Expr.Unary -> {
                visitChildren(type)
                visitChildren(x)
            }
            is GNode.Field -> {
                visitChildren(names)
                visitChildren(type)
            }
            is GNode.File -> {
                visitChildren(decls)
            }
            is GNode.Package -> {
                // We ignore the types here because they are just duplicated beneath
                visitChildren(files)
            }
            is GNode.Spec.Import -> { }
            is GNode.Spec.Type -> {
                visitChildren(expr)
            }
            is GNode.Spec.Value -> {
                visitChildren(names)
                visitChildren(type)
                visitChildren(values)
            }
            is GNode.Stmt.Assign -> {
                visitChildren(lhs)
                visitChildren(rhs)
            }
            is GNode.Stmt.Block -> {
                visitChildren(stmts)
            }
            is GNode.Stmt.Branch -> {
                visitChildren(label)
            }
            is GNode.Stmt.Decl -> {
                visitChildren(decl)
            }
            is GNode.Stmt.Defer -> {
                visitChildren(call)
            }
            GNode.Stmt.Empty -> { }
            is GNode.Stmt.Expr -> {
                visitChildren(x)
            }
            is GNode.Stmt.For -> {
                visitChildren(init)
                visitChildren(cond)
                visitChildren(post)
                visitChildren(body)
            }
            is GNode.Stmt.Go -> {
                visitChildren(call)
            }
            is GNode.Stmt.If -> {
                visitChildren(init)
                visitChildren(cond)
                visitChildren(body)
                visitChildren(elseStmt)
            }
            is GNode.Stmt.IncDec -> {
                visitChildren(x)
            }
            is GNode.Stmt.Labeled -> {
                visitChildren(label)
                visitChildren(stmt)
            }
            is GNode.Stmt.Range -> {
                visitChildren(key)
                visitChildren(value)
                visitChildren(x)
                visitChildren(body)
            }
            is GNode.Stmt.Return -> {
                visitChildren(results)
            }
            is GNode.Stmt.Select -> {
                visitChildren(cases)
            }
            is GNode.Stmt.Select.CommClause -> {
                visitChildren(comm)
                visitChildren(body)
            }
            is GNode.Stmt.Send -> {
                visitChildren(chan)
                visitChildren(value)
            }
            is GNode.Stmt.Switch -> {
                visitChildren(init)
                visitChildren(tag)
                visitChildren(cases)
            }
            is GNode.Stmt.Switch.CaseClause -> {
                visitChildren(list)
                visitChildren(body)
            }
            is GNode.Type.Array -> {
                visitChildren(elem)
            }
            is GNode.Type.Basic -> { }
            is GNode.Type.BuiltIn -> {
                visitChildren(type)
            }
            is GNode.Type.Chan -> {
                visitChildren(elem)
            }
            is GNode.Type.Const -> {
                visitChildren(type)
                visitChildren(value)
            }
            is GNode.Type.Func -> {
                visitChildren(type)
            }
            is GNode.Type.Interface -> {
                visitChildren(methods)
                visitChildren(embeddeds)
            }
            is GNode.Type.Label -> {
                visitChildren(type)
            }
            is GNode.Type.Map -> {
                visitChildren(elem)
                visitChildren(key)
            }
            is GNode.Type.Named -> {
                visitChildren(name)
                visitChildren(underlying)
                visitChildren(methods)
            }
            GNode.Type.Nil -> { }
            is GNode.Type.Package -> { }
            is GNode.Type.Pointer -> {
                visitChildren(elem)
            }
            is GNode.Type.Signature -> {
                visitChildren(recv)
                visitChildren(params)
                visitChildren(results)
            }
            is GNode.Type.Slice -> {
                visitChildren(elem)
            }
            is GNode.Type.Struct -> {
                visitChildren(fields)
            }
            is GNode.Type.Tuple -> {
                visitChildren(vars)
            }
            is GNode.Type.TypeName -> {
                visitChildren(type)
            }
            is GNode.Type.Var -> {
                visitChildren(type)
            }
        }
    }

    protected inline fun GNode.visitChildren(v: GNode?) = visitNodeChildren(this, v)
    protected open fun visitNodeChildren(self: GNode, v: GNode?) { if (v != null) visit(v, self) }

    protected inline fun GNode.visitChildren(v: List<GNode?>) = visitNodeChildren(this, v)
    protected open fun visitNodeChildren(self: GNode, v: List<GNode?>) { v.forEach { visitNodeChildren(self, it) } }

    companion object {
        fun visit(
            v: GNode,
            skipTypes: Boolean = true,
            fn: (v: GNode, parent: GNode) -> Boolean
        ) = object : GNodeVisitor() {
            override fun visit(v: GNode, parent: GNode) {
                if (fn(v, parent)) super.visit(v, parent)
            }

            override fun visitNodeChildren(self: GNode, v: GNode?) {
                if (!skipTypes || v !is GNode.Type) super.visitNodeChildren(self, v)
            }
        }.visit(v)
    }
}