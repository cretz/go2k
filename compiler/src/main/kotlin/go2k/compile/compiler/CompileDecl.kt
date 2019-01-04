package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger

fun Context.compileDecl(v: GNode.Decl, topLevel: Boolean = false): List<Node.Decl> = when (v) {
    is GNode.Decl.Const -> compileDeclConst(v)
    is GNode.Decl.Func -> listOfNotNull(compileDeclFunc(v), compileDeclFuncPointerVersion(v))
    is GNode.Decl.Import -> TODO()
    is GNode.Decl.Type -> compileDeclType(v, topLevel)
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

fun Context.compileDeclStruct(name: String, struct: GNode.Type.Struct, topLevel: Boolean): List<Node.Decl> {
    // Have a companion object with the method refs and pointer method refs
    val companion = if (!topLevel) null else compileDeclTypeCompanionObject(
        nonPointerRecvType = name.toDottedType(),
        nonPointerMethodNamesAndSigs = compileDeclStructGetMethodNamesAndSigs(name, struct, false),
        pointerRecvType = GO_PTR_CLASS.toType(name.toDottedType()).nullable(),
        pointerMethodNamesAndSigs = compileDeclStructGetMethodNamesAndSigs(name, struct, true)
    )

    // All non-pointer, non-self embed methods need to be forwarded the way the self ones are
    val pointerEmbedForwards = compileExprStructTypeGetEmbedMembers(struct).mapNotNull {
        if (it.self || it.params == null) return@mapNotNull null
        val anns: List<Node.Modifier> = methodNameClashes[Context.MethodNameClash(name, it.name)]?.let { jvmName ->
            listOf(ann("kotlin.jvm.JvmName", listOf(valueArg((jvmName + "Ptr").toStringTmpl()))).toSet())
        }.orEmpty()
        func(
            mods = (anns + Node.Modifier.Keyword.SUSPEND.toMod()) + it.name.nameVisibilityMods(name),
            receiverType = GO_PTR_CLASS.toType(name.toDottedType()).nullable(),
            name = it.name,
            params = it.params.map { (name, type) -> param(name = name, type = compileType(type)) },
            type = compileTypeMultiResultTypes(it.results!!),
            body = call(
                expr = Node.Expr.This(null).nullDeref().dot("\$v").dot(it.name),
                args = it.params.map { (name, _) -> valueArg(name.toName()) }
            ).toFuncBody()
        )
    }

    return listOf(compileExprStructType(name, struct).let {
        it.copy(members = it.members + listOfNotNull(companion))
    }) + pointerEmbedForwards
}

fun Context.compileDeclStructGetMethodNamesAndSigs(name: String, struct: GNode.Type.Struct, pointer: Boolean) =
    compileExprStructTypeGetEmbedMembers(struct, mustMatchStructName = name).mapNotNull { member ->
        when {
            // Must be method
            member.params == null || member.results == null -> null
            // If this is for non-pointer, receiver of self must be non-pointer
            member.self && member.recvPointer && !pointer -> null
            else ->
                // TODO: this is dumb, need all types to be fully qualified
                member.name to "(${member.params.joinToString { compileType(it.second).write() }}) -> " +
                    "(${member.results.joinToString { compileType(it).write() }})"
        }
    }

fun Context.compileDeclType(v: GNode.Decl.Type, topLevel: Boolean) = v.specs.flatMap { spec ->
    if (spec.alias) TODO()
    // We only want the non-named underlying type
    val underlying = spec.expr.type.namedUnderlyingType()
    when {
        // When it's a type of a type of struct, we just use the underlying struct
        spec.expr is GNode.Expr.Ident && underlying is GNode.Type.Struct -> compileDeclStruct(
            spec.name,
            (spec.expr.type.nonEntityType() as GNode.Type.Named).underlying as GNode.Type.Struct,
            topLevel
        )
        // Just a simple class wrapping the single value
        spec.expr is GNode.Expr.ArrayType || spec.expr is GNode.Expr.ChanType || spec.expr is GNode.Expr.FuncType ||
            spec.expr is GNode.Expr.Ident || spec.expr is GNode.Expr.MapType || spec.expr is GNode.Expr.Star ->
            listOf(compileDeclTypeSingle(spec, underlying!!))
        spec.expr is GNode.Expr.InterfaceType ->
            listOf(compileExprInterfaceType(spec.name, spec.expr))
        spec.expr is GNode.Expr.StructType ->
            compileDeclStruct(spec.name, spec.expr.type.nonEntityType() as GNode.Type.Struct, topLevel)
        else -> TODO(spec.expr.toString())
    }
}

fun Context.compileDeclTypeCompanionObject(
    nonPointerRecvType: Node.Type,
    nonPointerMethodNamesAndSigs: List<Pair<String, String>>,
    pointerRecvType: Node.Type,
    pointerMethodNamesAndSigs: List<Pair<String, String>>
) = structured(
    form = Node.Decl.Structured.Form.COMPANION_OBJECT,
    name = "Companion",
    parents = listOf(Node.Decl.Structured.Parent.Type(
        "go2k.runtime.GoStruct.MethodLookup".toDottedType().ref as Node.TypeRef.Simple, null
    )),
    members = listOf(
        compileDeclTypeMethodLookup(nonPointerRecvType, nonPointerMethodNamesAndSigs, false),
        compileDeclTypeMethodLookup(pointerRecvType, pointerMethodNamesAndSigs, true)
    )
)

fun Context.compileDeclTypeMethodLookup(
    recvType: Node.Type,
    methodNamesAndSigs: List<Pair<String, String>>,
    pointer: Boolean
) = func(
    mods = listOf(Node.Modifier.Keyword.OVERRIDE.toMod()),
    name = if (pointer) "lookupPointerMethod" else "lookupMethod",
    params = listOf(param(name = "nameAndSig", type = "kotlin.String".toDottedType())),
    type = "kotlin.Function".toDottedType(null).nullable(),
    body = Node.Expr.When(
        expr = "nameAndSig".toName(),
        entries = methodNamesAndSigs.map { (name, sig) ->
            Node.Expr.When.Entry(
                conds = listOf(Node.Expr.When.Cond.Expr("$name::$sig".toStringTmpl())),
                body = name.funcRef(recvType.ref)
            )
        } + Node.Expr.When.Entry(emptyList(), NullConst)
    ).toFuncBody()
)

fun Context.compileDeclTypeSingle(spec: GNode.Spec.Type, underlying: GNode.Type) = compileType(underlying).let { type ->
    // For nullable types, the wrapped item is not nullable
    val (properType, default) =
        if (type.ref !is Node.TypeRef.Nullable) type to compileTypeZeroExpr(underlying)
        else type.copy(ref = (type.ref as Node.TypeRef.Nullable).type) to null
    structured(
        // TODO: Can't be inline yet because we want our methods callable from Java which methods w/ inline class
        // params do not allow right now. Ref inline class docs and https://youtrack.jetbrains.com/issue/KT-28135
        mods = spec.name.nameVisibilityMods(),
        name = spec.name,
        primaryConstructor = primaryConstructor(
            params = listOf(param(
                // TODO: Cannot use parent interface right now due to https://youtrack.jetbrains.com/issue/KT-29075
                // mods = listOf(Node.Modifier.Keyword.OVERRIDE.toMod()),
                readOnly = true,
                name = "\$v",
                type = properType,
                default = default
            ))
        ),
        // TODO: See TODO for override above
        // parents = listOf(Node.Decl.Structured.Parent.Type(
        //     type = GoSingleType::class.toType(type).ref as Node.TypeRef.Simple,
        //     by = null
        // )),
        members = listOf(compileDeclTypeCompanionObject(
            nonPointerRecvType = spec.name.toDottedType().let {
                if (type.ref is Node.TypeRef.Nullable) it.nullable() else it
            },
            nonPointerMethodNamesAndSigs = compileDeclTypeSingleGetMethodNamesAndSigs(spec.name, false),
            pointerRecvType = GO_PTR_CLASS.toType(spec.name.toDottedType().let {
                if (underlying.isNullable) it.nullable() else it
            }).nullable(),
            pointerMethodNamesAndSigs = compileDeclTypeSingleGetMethodNamesAndSigs(spec.name, true)
        ))
    )
}

fun Context.compileDeclTypeSingleGetMethodNamesAndSigs(name: String, pointer: Boolean) = pkg.files.flatMap { file ->
    file.decls.mapNotNull { it as? GNode.Decl.Func }.filter { decl ->
        val namedType = when (val type = decl.recv.singleOrNull()?.type?.type.nonEntityType()) {
            is GNode.Type.Pointer -> if (!pointer) null else type.elem as? GNode.Type.Named
            else -> type as? GNode.Type.Named
        }
        namedType?.name?.invoke()?.name == name
    }.map {
        val params = it.type.params.flatMap { field -> field.names.map { compileType(field.type.type!!) } }
        val results = it.type.results.flatMap { field ->
            List(field.names.size.let { if (it == 0) 1 else it }) { compileType(field.type.type!!) }
        }
        it.name to "(${params.joinToString { it.write() }}) -> (${results.joinToString { it.write() }})"
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
                type = if (!topLevel && spec.type == null) null else compileType(type).let {
                    // If the type needs to be a ref, we have to wrap it
                    if (varDefWillBeRef(id.name)) "go2k.runtime.GoRef".toDottedType(it) else it
                }
            )),
            expr = when {
                needsLateinit -> null
                topLevel || value == null -> compileTypeZeroExpr(type)
                else -> compileExpr(value, coerceToType = type, byValue = true)
            }?.let { rhsExpr ->
                if (!markVarDef(id.name)) rhsExpr
                else call(expr = "go2k.runtime.GoRef".toDottedExpr(), args = listOf(valueArg(rhsExpr)))
            }
        )
    }
}