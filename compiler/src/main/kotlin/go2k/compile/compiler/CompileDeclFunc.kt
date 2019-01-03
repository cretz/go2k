package go2k.compile.compiler

import go2k.compile.go.GNode
import go2k.runtime.Slice
import kastree.ast.Node

fun Context.compileDeclFunc(v: GNode.Decl.Func) = withFunc(v.type) {
    withVarDefSet(v.childVarDefsNeedingRefs()) {
        // Mark recv as defined and decl it if named
        val preStmts: List<Node.Stmt> = v.recv.flatMap { field ->
            field.names.map { name ->
                var expr: Node.Expr = Node.Expr.This(null)
                // Receiver is copied if necessary
                val recvType = field.type.type.nonEntityType()
                if (recvType is GNode.Type.Named && recvType.underlying is GNode.Type.Struct)
                    expr = call(expr.dot("\$copy"))
                // Receiver var becomes ref if necessary
                if (markVarDef(name.name)) expr = call(
                    expr = "go2k.runtime.GoRef".toDottedExpr(),
                    args = listOf(valueArg(expr))
                )
                property(vars = listOf(propVar(name.name)), expr = expr).toStmt()
            }
        }
        val anns: List<Node.Modifier> = v.clashableRecvTypeName()?.let { clashTypeName ->
            methodNameClashes[Context.MethodNameClash(clashTypeName, v.name)]?.let { jvmName ->
                listOf(ann("kotlin.jvm.JvmName", listOf(valueArg(jvmName.toStringTmpl()))).toSet())
            }
        }.orEmpty()
        val recvName = v.recv.singleOrNull()?.type?.type?.nonEntityType().let { recvType ->
            ((if (recvType is GNode.Type.Pointer) recvType.elem else recvType) as? GNode.Type.Named)?.
                name?.invoke()?.name
        }
        compileDeclFuncBody(v.type, v.body).let { (params, returnType, stmts) ->
            func(
                mods = (anns + Node.Modifier.Keyword.SUSPEND.toMod()) + v.name.nameVisibilityMods(recvName),
                receiverType = v.recv.singleOrNull()?.let { compileType(it.type.type!!) },
                name = v.name,
                params = params,
                type = returnType,
                body = block(preStmts + stmts).toFuncBody()
            )
        }
    }
}

fun Context.compileDeclFuncBody(type: GNode.Expr.FuncType, block: GNode.Stmt.Block): DeclFuncBody {
    // Mark all params/results as defined, making ones that need refs to be refs
    var preStmts: List<Node.Stmt> = (type.params + type.results).flatMap {
        it.names.mapNotNull {
            if (!markVarDef(it.name)) null else property(
                vars = listOf(propVar(it.name)),
                expr = call(expr = "go2k.runtime.GoRef".toDottedExpr(), args = listOf(valueArg(it.name.toName())))
            ).toStmt()
        }
    }
    // Named return idents need to be declared with zero vals up front
    preStmts += type.results.flatMap { field ->
        field.names.map { ident ->
            property(
                vars = listOf(propVar(ident.name)),
                expr = compileTypeZeroExpr(ident.type ?: ident.defType ?: error("No ident type"))
            ).toStmt()
        }
    }
    val (params, resultType) = compileDeclFuncSignature(type)
    return DeclFuncBody(
        params = params,
        resultType = resultType,
        // If there are defer statements, we need to wrap the body in a withDefers
        stmts = preStmts +
            if (!currFunc.hasDefer) compileStmtBlock(block).stmts
            else listOf(call(
                expr = "go2k.runtime.withDefers".toDottedExpr(),
                lambda = trailLambda(compileStmtBlock(block).stmts)
            ).toStmt())
    )
}

fun Context.compileDeclFuncPointerVersion(v: GNode.Decl.Func): Node.Decl.Func? {
    // Must have non-pointer receiver
    val recvType = v.recv.singleOrNull()?.type?.type?.nonEntityType()
    if (recvType == null || recvType is GNode.Type.Pointer) return null
    val recvName = (recvType as? GNode.Type.Named)?.name?.invoke()?.name
    // Same JVM overload but with Ptr added (TODO: clash issue)
    val anns: List<Node.Modifier> = v.clashableRecvTypeName()?.let { clashTypeName ->
        methodNameClashes[Context.MethodNameClash(clashTypeName, v.name)]?.let { jvmName ->
            listOf(ann("kotlin.jvm.JvmName", listOf(valueArg((jvmName + "Ptr").toStringTmpl()))).toSet())
        }
    }.orEmpty()
    val (params, resultType) = compileDeclFuncSignature(v.type)
    // Just defer to the pointer one
    return func(
        mods = (anns + Node.Modifier.Keyword.SUSPEND.toMod()) + v.name.nameVisibilityMods(recvName),
        receiverType = GO_PTR_CLASS.toType(compileType(recvType)).nullable(),
        name = v.name,
        params = params,
        type = resultType,
        body = call(
            expr = Node.Expr.This(null).nullDeref().dot("\$v").dot(v.name),
            args = params.map { valueArg(it.name.toName()) }
        ).toFuncBody()
    )
}

fun Context.compileDeclFuncSignature(type: GNode.Expr.FuncType) = Pair(
    type.params.flatMap { field ->
        field.names.map { name ->
            // An ellipsis expr means a slice vararg
            if (field.type is GNode.Expr.Ellipsis) param(
                name = name.name,
                type = Slice::class.toType(compileType(field.type.elt!!.type!!)).nullable(),
                default = NullConst
            ) else param(name = name.name, type = compileType(field.type.type!!))
        }
    },
    compileTypeMultiResult(type.results)
)

data class DeclFuncBody(
    val params: List<Node.Decl.Func.Param>,
    val resultType: Node.Type?,
    val stmts: List<Node.Stmt>
)