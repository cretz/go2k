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
        Node.Decl.Structured.Parent.Type(compileType(it).ref as Node.TypeRef.Simple, null)
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
    return structured(
        name = "Impl",
        typeParams = listOf(Node.TypeParam(emptyList(), "\$T", "kotlin.Any".toDottedType().nullable().ref)),
        parents = listOf(Node.Decl.Structured.Parent.Type(name.toDottedType().ref as Node.TypeRef.Simple, null)),
        primaryConstructor = primaryConstructor(
            params = listOf(param(
                mods = listOf(Node.Modifier.Keyword.OVERRIDE.toMod()),
                readOnly = true,
                name = "\$v",
                type = genericType
            )) + methods.map { method ->
                param(
                    mods = listOf(Node.Modifier.Keyword.PRIVATE.toMod()),
                    readOnly = true,
                    name = method.name + "\$delegate",
                    type = compileTypeSignature(method.type, genericType, false)
                )
            }
        ),
        members = v.methods.map { method ->
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
        }
    )
}