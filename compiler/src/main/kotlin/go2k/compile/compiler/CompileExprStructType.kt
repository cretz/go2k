package go2k.compile.compiler

import go2k.compile.go.GNode
import go2k.runtime.GoStruct
import kastree.ast.Node

fun Context.compileExprStructType(name: String, v: GNode.Expr.StructType) =
    compileExprStructType(name, v.type.unnamedType() as GNode.Type.Struct)

fun Context.compileExprStructType(name: String, v: GNode.Type.Struct) = structured(
    mods = if (name.first().isUpperCase()) emptyList() else listOf(Node.Modifier.Keyword.INTERNAL.toMod()),
    name = name,
    primaryConstructor = primaryConstructor(
        params = v.fields.map { field ->
            param(
                mods =
                    if (field.name.first().isUpperCase()) emptyList()
                    else listOf(Node.Modifier.Keyword.INTERNAL.toMod()),
                readOnly = false,
                name = field.name,
                type = compileType(field.type),
                default = compileTypeZeroExpr(field.type)
            )
        }
    ),
    parents = listOf(Node.Decl.Structured.Parent.Type(
        type = GoStruct::class.toType().ref as Node.TypeRef.Simple,
        by = null
    )),
    members = compileExprStructTypeEmbedForwards(v) + compileExprStructTypeCopyMethod(name, v)
)

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

fun Context.compileExprStructTypeEmbedForwards(v: GNode.Type.Struct): List<Node.Decl> {
    // We need to copy all embedded members that are not more-specifically defined in the package
    val definedMemberNames = v.fields.map { it.name }.toSet() + v.packageMethods(this).map { it.name }
    // Embedded fields are fields that are not on the current type but on only one of the embedded ones
    // First is this embedded var, second is the downstream field (var) or method (func)
    val seenEmbedNames = mutableListOf<String>()
    val embedMembers = v.embeddeds().map { embedType ->
        // All embedded types including this one
        val embeddedTypes = listOf(embedType) + when (val raw = embedType.underlying.derefed()) {
            is GNode.Type.Interface -> raw.recursiveEmbedded()
            is GNode.Type.Struct -> raw.embeddeds(recursive = true)
            else -> error("Unknown embed type $raw")
        }.filterNot { it == embedType }
        // Now go over fields and methods
        embedType to embeddedTypes.flatMap { subEmbedType ->
            when (val raw = subEmbedType.underlying.derefed()) {
                is GNode.Type.Interface -> raw.methods
                is GNode.Type.Struct -> raw.fields + raw.packageMethods(this)
                else -> error("Unknown embed type $raw")
            }
        }.filterNot {
            // Make sure not in defined member names and add to running name list
            definedMemberNames.contains((it as GNode.Type.NamedEntity).name.also { seenEmbedNames += it })
        }
    }.map { (embedField, embedMemberSet) ->
        // Remove any members that have names that have been seen more than once
        embedField to embedMemberSet.filterNot { embedMember ->
            embedMember as GNode.Type.NamedEntity
            seenEmbedNames.count { it == embedMember.name } > 1
        }
    }
    // Compile all the forwards as simple properties and methods
    return embedMembers.flatMap { (embedField, embedMemberSet) ->
        embedMemberSet.map { embedMember ->
            when (embedMember) {
                is GNode.Type.Func -> func(
                    mods =
                        if (embedMember.name.first().isUpperCase()) emptyList()
                        else listOf(Node.Modifier.Keyword.INTERNAL.toMod()),
                    name = embedMember.name,
                    params = embedMember.type.params.map { param(name = it.name, type = compileType(it.type)) },
                    body = call(
                        expr = embedField.name.name.toName().dot(embedMember.name),
                        args = embedMember.type.params.map { valueArg(it.name.toName()) }
                    ).toFuncBody()
                ) as Node.Decl // TODO: why is this cast needed?
                is GNode.Type.Var -> property(
                    mods =
                        if (embedMember.name.first().isUpperCase()) emptyList()
                        else listOf(Node.Modifier.Keyword.INTERNAL.toMod()),
                    vars = listOf(propVar(embedMember.name)),
                    accessors = Node.Decl.Property.Accessors(
                        Node.Decl.Property.Accessor.Get(
                            mods = listOf(Node.Modifier.Keyword.INLINE.toMod()),
                            type = null,
                            body = embedField.name.name.toName().dot(embedMember.name).toFuncBody()
                        ),
                        Node.Decl.Property.Accessor.Set(
                            mods = listOf(Node.Modifier.Keyword.INLINE.toMod()),
                            paramMods = emptyList(),
                            paramName = "\$v",
                            paramType = null,
                            body = block(listOf(binaryOp(
                                lhs = embedField.name.name.toName().dot(embedMember.name),
                                op = Node.Expr.BinaryOp.Token.ASSN,
                                rhs = "\$v".toName()
                            ).toStmt())).toFuncBody()
                        )
                    )
                )
                else -> error("Bad embed member: $embedMember")
            }
        }
    }
}