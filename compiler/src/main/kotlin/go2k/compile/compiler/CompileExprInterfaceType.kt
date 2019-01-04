package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node

fun Context.compileExprInterfaceType(name: String, v: GNode.Expr.InterfaceType) =
    compileExprInterfaceType(name, v.type.nonEntityType() as GNode.Type.Interface)

fun Context.compileExprInterfaceType(name: String, v: GNode.Type.Interface) = structured(
    mods = name.nameVisibilityMods(),
    form = Node.Decl.Structured.Form.INTERFACE,
    name = name,
    parents = v.embeddeds.map {
        Node.Decl.Structured.Parent.Type(when (val ref = compileType(it).ref) {
            is Node.TypeRef.Nullable -> ref.type as Node.TypeRef.Simple
            else -> ref as Node.TypeRef.Simple
        }, null)
    } + Node.Decl.Structured.Parent.Type("go2k.runtime.GoInterface".toDottedType().ref as Node.TypeRef.Simple, null),
    members = v.methods.map { method ->
        func(
            mods = listOf(Node.Modifier.Keyword.SUSPEND.toMod()) + method.name.nameVisibilityMods(),
            name = method.name,
            params = method.type.params.mapIndexed { index, p ->
                param(
                    name = p.name.ifEmpty { "\$p${index + 1}" },
                    type = compileType(p.type)
                )
            },
            type = compileTypeMultiResultTypes(method.type.results.map { it.type })
        )
    } + compileExprInterfaceTypeImpl(name, v)
)

fun Context.compileExprInterfaceTypeImpl(name: String, v: GNode.Type.Interface): Node.Decl.Structured {
    val methods = v.allMethods()
    val genericType = "\$T".toDottedType()
    val methodLookupLambdaType = Node.Type(
        mods = emptyList(),
        ref = Node.TypeRef.Func(
            receiverType = null,
            params = listOf(Node.TypeRef.Func.Param(null, "kotlin.String".toDottedType())),
            type = "kotlin.Function".toDottedType(null).nullable()
        )
    )
    return structured(
        name = "Impl",
        typeParams = listOf(Node.TypeParam(emptyList(), "\$T", "kotlin.Any".toDottedType().nullable().ref)),
        parents = listOf(Node.Decl.Structured.Parent.Type(name.toDottedType().ref as Node.TypeRef.Simple, null)),
        primaryConstructor = primaryConstructor(
            params = listOf(
                param(
                    mods = listOf(Node.Modifier.Keyword.OVERRIDE.toMod()),
                    readOnly = true,
                    name = "\$v",
                    type = genericType
                ),
                param(
                    mods = listOf(Node.Modifier.Keyword.OVERRIDE.toMod()),
                    readOnly = true,
                    name = "\$methodLookup",
                    type = methodLookupLambdaType.nullable()
                )
            ) + methods.map { method ->
                param(
                    readOnly = true,
                    name = method.name + "\$delegate",
                    type = compileTypeSignature(method.type, genericType, false)
                )
            }
        ),
        members = methods.map { method ->
            func(
                mods = listOf(Node.Modifier.Keyword.OVERRIDE.toMod(), Node.Modifier.Keyword.SUSPEND.toMod()) +
                    method.name.nameVisibilityMods(),
                name = method.name,
                params = method.type.params.mapIndexed { index, p ->
                    param(
                        name = p.name.ifEmpty { "\$p${index + 1}" },
                        type = compileType(p.type)
                    )
                },
                type = compileTypeMultiResultTypes(method.type.results.map { it.type }),
                body = Node.Decl.Func.Body.Expr("\$v".toName().dot(call(
                    expr = (method.name + "\$delegate").toName(),
                    args = method.type.params.mapIndexed { index, p ->
                        valueArg(p.name.ifEmpty { "\$p${index + 1}" }.toName())
                    }
                )))
            )
        } + structured(
            form = Node.Decl.Structured.Form.COMPANION_OBJECT,
            name = "Companion",
            members = listOf(func(
                typeParams = listOf(Node.TypeParam(emptyList(), "\$T", "kotlin.Any".toDottedType().nullable().ref)),
                name = "\$fromOther",
                params = listOf(
                    param(name = "\$v", type = genericType),
                    param(name = "\$methodLookup", type = methodLookupLambdaType)
                ),
                type = name.toDottedType().nullable(),
                body = block(listOf(Node.Expr.Return(null, call(
                    expr = name.toDottedExpr().dot("Impl"),
                    args = listOf(valueArg("\$v".toName()), valueArg("\$methodLookup".toName())) +
                        methods.map { method ->
                            val params = method.type.params.map { compileType(it.type) }
                            val results = method.type.results.map { compileType(it.type) }
                            val nameAndSig = method.name + "::(" + params.joinToString { it.write() } +
                                ") -> (" + results.joinToString { it.write() } + ")"
                            valueArg(binaryOp(
                                lhs =  typeOp(
                                    lhs = call(
                                        expr = "\$methodLookup".toName(),
                                        args = listOf(valueArg(nameAndSig.toStringTmpl()))
                                    ),
                                    op = Node.Expr.TypeOp.Token.AS_SAFE,
                                    rhs = compileTypeSignature(method.type, genericType, false).paren()
                                ),
                                op = Node.Expr.BinaryOp.Token.ELVIS,
                                rhs = Node.Expr.Return(null, NullConst)
                            ))
                        }
                )).toStmt())).toFuncBody()
            ))
        )
    )
}