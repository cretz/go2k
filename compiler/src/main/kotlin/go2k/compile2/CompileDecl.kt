package go2k.compile2

import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger

fun Context.compileDecl(v: GNode.Decl, topLevel: Boolean = false): List<Node.Decl> = when (v) {
    is GNode.Decl.Const -> compileDeclConst(v)
    is GNode.Decl.Func -> listOf(compileDeclFunc(v))
    is GNode.Decl.Import -> TODO()
    is GNode.Decl.Type -> compileDeclType(v)
    is GNode.Decl.Var -> compileDeclVar(v, topLevel)
}

fun Context.compileDeclConst(v: GNode.Decl.Const) = v.specs.flatMap { spec ->
    spec.names.zip(spec.values) { name, value ->
        // Consts are always declared inline, and use a const val if possible
        val const = value.type as? GNode.Type.Const ?: error("Type not const")
        val basic = const.type as? GNode.Type.Basic ?: error("Const not basic")
        val constVal = when (basic.kind) {
            // Floats that are not so big to be big decimals are consts
            GNode.Type.Basic.Kind.UNTYPED_FLOAT, GNode.Type.Basic.Kind.FLOAT_32, GNode.Type.Basic.Kind.FLOAT_64 ->
                (const.value as GNode.Const.Float).v.untypedFloatClass() != BigDecimal::class
            // Ints that are not so big to be big decimals are consts
            GNode.Type.Basic.Kind.UNTYPED_INT ->
                (const.value as GNode.Const.Int).v.untypedIntClass() != BigInteger::class
            // TODO: exclude other non-consts
            else -> true
        }
        property(
            mods = if (constVal) listOf(Node.Modifier.Keyword.CONST.toMod()) else emptyList(),
            readOnly = true,
            vars = listOf(propVar(name.name)),
            expr = compileConst(const)
        )
    }
}

fun Context.compileDeclType(v: GNode.Decl.Type) = v.specs.map { spec ->
    if (spec.alias) TODO()
    when (spec.expr) {
        is GNode.Expr.StructType -> compileExprStructType(spec.name, spec.expr)
        else -> TODO()
    }
}

fun Context.compileDeclVar(v: GNode.Decl.Var, topLevel: Boolean) = v.specs.flatMap { spec ->
    spec.names.mapIndexed { index, id ->
        val type = (id.defType as? GNode.Type.Var)?.type ?: error("Can't find var type")
        // Top level vars are never inited on their own. Instead they are inited in a separate init area. Therefore,
        // we must mark 'em lateinit. But lateinit is only for non-primitive, non-null types. Otherwise we just init
        // to the 0 value.
        var needsLateinit = topLevel && !type.isJavaPrimitive && !type.isNullable
        val value = spec.values.getOrNull(index)
        // It also doesn't need late init if it's an array w/ no value
        if (needsLateinit && type.isArray && value == null) needsLateinit = false
        property(
            mods = listOfNotNull(Node.Modifier.Keyword.LATEINIT.takeIf { needsLateinit }?.toMod()),
            // We only put the type if it's top level or explicitly specified
            vars = listOf(propVar(
                name = id.name,
                type = if (topLevel || spec.type != null) compileType(type) else null
            )),
            expr = when {
                needsLateinit -> null
                topLevel || value == null -> compileTypeZeroExpr(type)
                else -> compileExpr(value)
            }
        )
    }
}