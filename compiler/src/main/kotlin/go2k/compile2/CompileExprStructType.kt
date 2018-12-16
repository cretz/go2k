package go2k.compile2

import go2k.runtime.GoStruct
import kastree.ast.Node

fun Context.compileExprStructType(name: String, v: GNode.Expr.StructType): Node.Decl.Structured {
    val type = v.type.unnamedType() as GNode.Type.Struct
    // TODO: more detail
    val fields = v.fields.flatMap { field -> field.names.map { it.name to field.type.type!! } }
    return structured(
        mods = if (name.first().isUpperCase()) emptyList() else listOf(Node.Modifier.Keyword.INTERNAL.toMod()),
        name = name,
        primaryConstructor = primaryConstructor(
            params = fields.map { (name, type) ->
                param(
                    mods =
                        if (name.first().isUpperCase()) emptyList()
                        else listOf(Node.Modifier.Keyword.INTERNAL.toMod()),
                    readOnly = false,
                    name = name,
                    type = compileType(type),
                    default = compileTypeZeroExpr(type)
                )
            }
        ),
        parents = listOf(Node.Decl.Structured.Parent.Type(
            type = GoStruct::class.toType().ref as Node.TypeRef.Simple,
            by = null
        )),
        members = listOf(compileExprStructTypeCopyMethod(name, type))
    )
}

fun Context.compileExprStructTypeCopyMethod(name: String, type: GNode.Type.Struct) = func(
    name = "\$copy",
    body = call(
        expr = name.toName(),
        args = type.fields.map { field ->
            valueArg(field.name.toName().let {
                // Deep copy if field type is struct
                val type = field.type.unnamedType()
                if (type is GNode.Type.Named && type.underlying is GNode.Type.Struct) call(it.dot("\$copy"))
                else it
            })
        }
    ).toFuncBody()
)