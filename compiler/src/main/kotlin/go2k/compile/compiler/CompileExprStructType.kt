package go2k.compile.compiler

import go2k.compile.go.GNode
import go2k.runtime.GoStruct
import kastree.ast.Node

fun Context.compileExprStructType(name: String, v: GNode.Expr.StructType) =
    compileExprStructType(name, v.type.unnamedType() as GNode.Type.Struct)

fun Context.compileExprStructType(name: String, v: GNode.Type.Struct) = structured(
    mods = name.nameVisibilityMods(),
    name = name,
    primaryConstructor = primaryConstructor(
        params = v.fields.map { field ->
            param(
                mods = field.name.nameVisibilityMods(),
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
    members =
        compileExprStructTypeEmbedForwards(v) +
        compileExprStructTypeFieldValsMethod(v) +
        compileExprStructTypeCopyMethod(name, v)
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
    // Forwards are embed members that are not self
    return compileExprStructTypeGetEmbedMembers(v).filterNot { it.self }.map { member ->
        var embedFieldRef: Node.Expr = member.embedFieldName!!.toName()
        if (member.pointer != member.recvPointer) {
            if (member.pointer) embedFieldRef = embedFieldRef.ptrDeref()
            else embedFieldRef = compileExprUnaryAddressOfField(Node.Expr.This(null), member.name)
        }
        val mods = member.embedFieldName.nameVisibilityMods().takeIf { member.name.first().isLowerCase() }.orEmpty()
        if (member.params == null) {
            property(
                mods = mods,
                vars = listOf(propVar(member.name)),
                accessors = Node.Decl.Property.Accessors(
                    Node.Decl.Property.Accessor.Get(
                        mods = emptyList(),
                        type = null,
                        body = embedFieldRef.dot(member.name).toFuncBody()
                    ),
                    Node.Decl.Property.Accessor.Set(
                        mods = emptyList(),
                        paramMods = emptyList(),
                        paramName = "\$v",
                        paramType = null,
                        body = block(listOf(binaryOp(
                            lhs = embedFieldRef.dot(member.name),
                            op = Node.Expr.BinaryOp.Token.ASSN,
                            rhs = "\$v".toName()
                        ).toStmt())).toFuncBody()
                    )
                )
            ) as Node.Decl // TODO: why is this required?
        } else func(
            mods = mods + Node.Modifier.Keyword.SUSPEND.toMod(),
            name = member.name,
            params = member.params.map { (name, type) ->
                param(name = name, type = compileType(type))
            },
            body = call(
                expr = embedFieldRef.dot(member.name),
                args = member.params.map { (name, _) ->
                    valueArg(name.toName())
                }
            ).toFuncBody()
        )
    }
}

fun Context.compileExprStructTypeFieldValsMethod(v: GNode.Type.Struct) = func(
    mods = listOf(Node.Modifier.Keyword.OVERRIDE.toMod()),
    name = "\$fieldVals",
    body = call(
        expr = "kotlin.collections.mapOf".toDottedExpr(),
        typeArgs = listOf("kotlin.String".toDottedType(), "kotlin.Any".toDottedType().nullable()),
        args = v.fields.map { field ->
            valueArg(call(
                expr = "kotlin.Pair".toDottedExpr(),
                args = listOf(valueArg(field.name.toStringTmpl()), valueArg(field.name.toName()))
            ))
        }
    ).toFuncBody()
)

fun Context.compileExprStructTypeGetEmbedMembers(
    v: GNode.Type.Struct,
    structName: String? = null,
    pointer: Boolean = false,
    alreadySeen: Set<GNode.Type.Named> = emptySet()
): List<ExprStructTypeEmbedMember> {
    var alreadySeen = alreadySeen
    // Load all regular members
    val members = v.fields.map { field ->
        ExprStructTypeEmbedMember(
            embedFieldName = structName,
            pointer = pointer,
            name = field.name,
            recvPointer = field.type is GNode.Type.Pointer
        )
    } + v.packageMethods(this).map { method ->
        ExprStructTypeEmbedMember(
            embedFieldName = structName,
            pointer = pointer,
            name = method.name,
            recvPointer = method.recv.singleOrNull()?.type?.type.unnamedType() is GNode.Type.Pointer,
            params = method.type.params.flatMap { field ->
                field.names.map { it.name to field.type.type!! }
            }
        )
    }
    // Load all promoted
    val promotedMembers = v.fields.filter { it.embedded }.flatMap { field ->
        val (named, fieldPointer) =
            if (field.type is GNode.Type.Named) Pair(field.type as GNode.Type.Named, false)
            else Pair((field.type as GNode.Type.Pointer).elem as GNode.Type.Named, true)
        val fieldStructName = structName ?: named.name().name
        if (alreadySeen.contains(named)) emptyList() else {
            alreadySeen += named
            when (val type = named.underlying) {
                // Interfaces just promote all the methods
                is GNode.Type.Interface -> type.allEmbedded().flatMap { (_, embedded) ->
                    embedded.methods.map { method ->
                        ExprStructTypeEmbedMember(
                            embedFieldName = fieldStructName,
                            pointer = fieldPointer,
                            name = method.name,
                            recvPointer = false,
                            params = method.type.params.map { it.name to it.type }
                        )
                    }
                }
                // Structs call recursively
                is GNode.Type.Struct ->
                    compileExprStructTypeGetEmbedMembers(type, fieldStructName, fieldPointer, alreadySeen)
                else -> error("Not iface or strct")
            }
        }
    }
    // Return all members and all promoted that are not already in members or in promoted twice
    return members + promotedMembers.filter { promotedMember ->
        members.none { it.name == promotedMember.name } &&
            promotedMembers.count { it.name == promotedMember.name } < 2
    }
}

data class ExprStructTypeEmbedMember(
    // Null if self
    val embedFieldName: String?,
    // Null if self
    val pointer: Boolean,
    val name: String,
    val recvPointer: Boolean,
    // Null if field
    val params: List<Pair<String, GNode.Type>>? = null
) {
    val self get() = embedFieldName == null
}