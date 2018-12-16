package go2k.compile2

import go2k.runtime.Slice
import go2k.runtime.builtin.EmptyInterface
import kastree.ast.Node
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

fun Context.compileType(v: GNode.Type): Node.Type = when (v) {
    is GNode.Type.Array -> compileTypeArray(v)
    is GNode.Type.Basic -> compileTypeBasic(v)
    is GNode.Type.BuiltIn -> TODO()
    is GNode.Type.Chan -> compileTypeChan(v)
    is GNode.Type.Const -> compileTypeConst(v)
    is GNode.Type.Func -> TODO()
    is GNode.Type.Interface -> compileTypeInterface(v)
    is GNode.Type.Label -> TODO()
    is GNode.Type.Map -> TODO()
    is GNode.Type.Named -> compileTypeNamed(v)
    GNode.Type.Nil -> TODO()
    is GNode.Type.Package -> TODO()
    is GNode.Type.Pointer -> compileTypePointer(v)
    is GNode.Type.Signature -> compileTypeSignature(v)
    is GNode.Type.Slice -> compileTypeSlice(v)
    is GNode.Type.Struct -> TODO()
    is GNode.Type.Tuple -> TODO()
    is GNode.Type.TypeName -> compileTypeTypeName(v)
    is GNode.Type.Var -> compileTypeVar(v)
}

fun Context.compileTypeArray(v: GNode.Type.Array): Node.Type {
    // Primitive array class or general array w/ type arg
    val primArrayClass = (v.elem as? GNode.Type.Basic)?.kotlinPrimitiveType()?.primitiveArrayClass()
    return primArrayClass?.toType() ?: "kotlin.Array".toDottedType(compileType(v.elem))
}

fun Context.compileTypeBasic(v: GNode.Type.Basic) = v.kotlinPrimitiveType().toType()

fun Context.compileTypeChan(v: GNode.Type.Chan) = when {
    !v.canSend -> ReceiveChannel::class
    !v.canRecv -> SendChannel::class
    else -> Channel::class
}.toType(compileType(v.elem)).nullable()

fun Context.compileTypeConst(v: GNode.Type.Const) = compileType(v.type)

fun Context.compileTypeInterface(v: GNode.Type.Interface): Node.Type {
    if (v.embeddeds.isNotEmpty() || v.methods.isNotEmpty()) {
        // For anon interfaces in types, we have to create top-level interfaces (or reuse one created earlier
        // in the same package that matches?)
        TODO()
    }
    return EmptyInterface::class.toType().nullable()
}

fun Context.compileTypeMultiResult(fields: List<GNode.Field>): Node.Type? {
    val types = fields.flatMap {
        val type = compileType(it.type.type!!)
        List(if (it.names.isEmpty()) 1 else it.names.size) { type }
    }
    // Just use TupleN if N > 1
    return when (types.size) {
        0, 1 -> types.singleOrNull()
        else -> "go2k.runtime.Tuple${types.size}".toDottedType(*types.toTypedArray())
    }
}

fun Context.compileTypeNamed(v: GNode.Type.Named) = v.name.name.toDottedType()

fun Context.compileTypePointer(v: GNode.Type.Pointer) = compileType(v.elem).let { type ->
    // Basically compile the underlying type, and if it's already nullable, this is nested
    if (type.ref !is Node.TypeRef.Nullable) type.nullable()
    else NESTED_PTR_CLASS.toType(type)
}

fun Context.compileTypeRefExpr(v: GNode.Type): Node.Expr = when (v) {
    is GNode.Type.Interface -> {
        if (v.embeddeds.isNotEmpty() || v.methods.isNotEmpty()) {
            // For anon interfaces in types, we have to create top-level interfaces (or reuse one created earlier
            // in the same package that matches?)
            TODO()
        }
        EmptyInterface::class.ref()
    }
    else -> TODO()
}

fun Context.compileTypeSignature(v: GNode.Type.Signature) = Node.Type(
    mods = listOf(Node.Modifier.Keyword.SUSPEND.toMod()),
    ref = Node.TypeRef.Func(
        receiverType = null,
        params = v.params.map {
            // TODO: could check if namedType is Named and use the name in the type here if present
            Node.TypeRef.Func.Param(null, compileType(it))
        },
        type = v.results.let {
            if (it.isEmpty()) Unit::class.toType()
            else compileType(it.singleOrNull() ?: TODO())
        }
    )
)

fun Context.compileTypeSlice(v: GNode.Type.Slice) = Slice::class.toType(compileType(v.elem)).nullable()

fun Context.compileTypeTypeName(v: GNode.Type.TypeName) = compileType(v.type!!)

fun Context.compileTypeVar(v: GNode.Type.Var) = compileType(v.type)

fun Context.compileTypeZeroExpr(v: GNode.Type): Node.Expr = when (v) {
    is GNode.Type.Array -> compileExprCompositeLitArrayCreate(v.elem, v.len.toInt())
    is GNode.Type.Basic -> when (v.kind) {
        GNode.Type.Basic.Kind.BOOL, GNode.Type.Basic.Kind.UNTYPED_BOOL ->
            compileConst(v.kind, GNode.Const.Boolean(false))
        GNode.Type.Basic.Kind.INT, GNode.Type.Basic.Kind.INT_8, GNode.Type.Basic.Kind.INT_16, GNode.Type.Basic.Kind.INT_32,
        GNode.Type.Basic.Kind.INT_64, GNode.Type.Basic.Kind.UINT, GNode.Type.Basic.Kind.UINT_8, GNode.Type.Basic.Kind.UINT_16,
        GNode.Type.Basic.Kind.UINT_32, GNode.Type.Basic.Kind.UINT_64, GNode.Type.Basic.Kind.UINT_PTR,
        GNode.Type.Basic.Kind.UNTYPED_INT, GNode.Type.Basic.Kind.UNTYPED_RUNE ->
            compileConst(v.kind, GNode.Const.Int("0"))
        GNode.Type.Basic.Kind.FLOAT_32, GNode.Type.Basic.Kind.FLOAT_64, GNode.Type.Basic.Kind.UNTYPED_FLOAT ->
            compileConst(v.kind, GNode.Const.Float("0.0"))
        GNode.Type.Basic.Kind.STRING, GNode.Type.Basic.Kind.UNTYPED_STRING ->
            compileConst(v.kind, GNode.Const.String(""))
        GNode.Type.Basic.Kind.UNTYPED_NIL ->
            NullConst
        else ->
            error("Unrecognized type kind: ${v.kind}")
    }
    is GNode.Type.Chan -> NullConst
    is GNode.Type.Const -> compileTypeZeroExpr(v.type)
    is GNode.Type.Func -> NullConst
    is GNode.Type.Interface -> NullConst
    is GNode.Type.Label -> compileTypeZeroExpr(v.type!!)
    is GNode.Type.Map -> NullConst
    // TODO: qualify
    is GNode.Type.Named -> call(v.name.name.toName())
    is GNode.Type.Nil -> NullConst
    is GNode.Type.Pointer -> NullConst
    is GNode.Type.Slice -> NullConst
    is GNode.Type.TypeName -> compileTypeZeroExpr(v.type!!)
    is GNode.Type.Var -> compileTypeZeroExpr(v.type)
    else -> error("No zero expr for type $v")
}