package go2k.compile.compiler

import go2k.compile.go.GNode
import go2k.compile.go.GNodeVisitor
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

fun GNode.childrenAreNewScope() = this is GNode.Expr.FuncLit || this is GNode.Stmt.Block ||
    this is GNode.Stmt.For || this is GNode.Stmt.If || this is GNode.Stmt.Range ||
    this is GNode.Stmt.Select.CommClause || this is GNode.Stmt.Switch || this is GNode.Stmt.Switch.CaseClause

// Gets all var defs in the child of this node (but not nested scopes) that need to
// be "refs" (i.e. their address is taken at any depth)
fun GNode.childVarDefsNeedingRefs(): Set<String> {
    val visitor = object : GNodeVisitor() {
        var varDefsInThisNode = emptySet<String>()
        var varDefsInOtherNodes = emptySet<String>()
        var varsNeedingRefsInThisNode = emptySet<String>()
        var first = true
        var inThisNode = true

        override fun visit(v: GNode, parent: GNode) {
            // Just skip types
            if (v is GNode.Type) return
            // Unary &'s on local vars are addresses needing refs
            if (v is GNode.Expr.Unary &&
                v.token == GNode.Expr.Unary.Token.AND &&
                v.x is GNode.Expr.Ident &&
                !varDefsInOtherNodes.contains(v.x.name) &&
                varDefsInThisNode.contains(v.x.name)) varsNeedingRefsInThisNode += v.x.name
            // Selectors where the LHS is a non-pointer struct and the RHS is a method with a pointer
            // receiver are vars needing refs
            if (v is GNode.Expr.Selector && v.x is GNode.Expr.Ident) {
                val lhsType = v.x.type.unnamedType() as? GNode.Type.Named
                val rhsIsPointer = (v.sel.type.unnamedType() as? GNode.Type.Signature)?.recv?.type is GNode.Type.Pointer
                if (lhsType?.underlying?.unnamedType() is GNode.Type.Struct && rhsIsPointer)
                    varsNeedingRefsInThisNode += v.x.name
            }
            // Local var defs need to be marked
            when (v) {
                is GNode.Decl.Func -> v.recv.flatMap { it.names }
                is GNode.Expr.FuncType -> v.params.flatMap { it.names } + v.results.flatMap { it.names }
                is GNode.Spec.Value -> v.names
                is GNode.Stmt.Assign -> v.lhs.takeIf { v.tok == GNode.Stmt.Assign.Token.DEFINE }.orEmpty().mapNotNull {
                    it as? GNode.Expr.Ident
                }
                is GNode.Stmt.Range ->
                    if (!v.define) emptyList()
                    else listOfNotNull(v.key as? GNode.Expr.Ident, v.value as? GNode.Expr.Ident)
                else -> emptyList()
            }.filter { it.defType != null }.forEach {
                if (inThisNode) varDefsInThisNode += it.name
                else varDefsInOtherNodes += it.name
            }
            // We have to reset some vals at the end if we enter a new scope.
            // Note, it's not a new scope if this is the first element
            val childIsNewScope = !first && v.childrenAreNewScope()
            if (first) first = false
            val currInTheseStmts = inThisNode
            val currVarDefsInOtherStmts = varDefsInOtherNodes
            if (childIsNewScope) inThisNode = false
            // Traverse children
            super.visit(v, parent)
            // Reset some vals
            if (childIsNewScope) {
                inThisNode = currInTheseStmts
                varDefsInOtherNodes = currVarDefsInOtherStmts
            }
        }
    }
    visitor.visit(this)
    // Return all var defs that happened here only if they needed refs
    return visitor.varDefsInThisNode.intersect(visitor.varsNeedingRefsInThisNode)
}

fun GNode.Package.defaultPackageName() = path.split('/').filter(String::isNotEmpty).let { pieces ->
    val first = pieces.first().let {
        val dotIndex = it.indexOf('.')
        if (dotIndex == -1) it else it.substring(dotIndex + 1) + '.' + it.substring(0, dotIndex)
    }
    (listOf(first) + pieces.drop(1)).joinToString(".")
}

fun GNode.Type.derefed() = if (this is GNode.Type.Pointer) this.elem else this

val GNode.Type.isArray get() = this is GNode.Type.Array
val GNode.Type.isJavaPrimitive get() = this is GNode.Type.Basic && kind != GNode.Type.Basic.Kind.STRING
val GNode.Type.isNullable get() = when (this) {
    is GNode.Type.Chan, is GNode.Type.Func, is GNode.Type.Interface,
    is GNode.Type.Map, is GNode.Type.Pointer, is GNode.Type.Slice -> true
    else -> false
}

fun GNode.Type.kotlinPrimitiveType(): KClass<*>? = when (this) {
    is GNode.Type.Basic -> kotlinPrimitiveType()
    is GNode.Type.Const -> kotlinPrimitiveType()
    is GNode.Type.Var -> type.kotlinPrimitiveType()
    else -> null
}

fun GNode.Type.pointerIsBoxed() = when (val type = unnamedType()) {
    is GNode.Type.Named -> type.underlying !is GNode.Type.Struct
    is GNode.Type.Struct -> false
    else -> true
}

// The type not specific to the current name
fun GNode.Type?.unnamedType(): GNode.Type? =
    if (this is GNode.Type.NamedEntity) type?.unnamedType() else this

fun GNode.Type.Basic.kotlinPrimitiveType() = when (kind) {
    GNode.Type.Basic.Kind.BOOL, GNode.Type.Basic.Kind.UNTYPED_BOOL -> Boolean::class
    GNode.Type.Basic.Kind.INT -> Int::class
    GNode.Type.Basic.Kind.INT_8 -> Byte::class
    GNode.Type.Basic.Kind.INT_16 -> Short::class
    GNode.Type.Basic.Kind.INT_32 -> if (name == "rune") Char::class else Int::class
    GNode.Type.Basic.Kind.INT_64 -> Long::class
    GNode.Type.Basic.Kind.UINT, GNode.Type.Basic.Kind.UINT_32 -> UINT_CLASS
    GNode.Type.Basic.Kind.UINT_8 -> UBYTE_CLASS
    GNode.Type.Basic.Kind.UINT_16 -> USHORT_CLASS
    GNode.Type.Basic.Kind.UINT_64, GNode.Type.Basic.Kind.UINT_PTR -> ULONG_CLASS
    GNode.Type.Basic.Kind.FLOAT_32 -> Float::class
    GNode.Type.Basic.Kind.FLOAT_64 -> Double::class
    GNode.Type.Basic.Kind.COMPLEX_64 -> TODO()
    GNode.Type.Basic.Kind.COMPLEX_128, GNode.Type.Basic.Kind.UNTYPED_COMPLEX -> TODO()
    GNode.Type.Basic.Kind.STRING, GNode.Type.Basic.Kind.UNTYPED_STRING -> String::class
    GNode.Type.Basic.Kind.UNTYPED_INT -> BigInteger::class
    GNode.Type.Basic.Kind.UNTYPED_RUNE -> Char::class
    GNode.Type.Basic.Kind.UNTYPED_FLOAT -> BigDecimal::class
    GNode.Type.Basic.Kind.UNTYPED_NIL -> TODO()
    else -> error("Unrecognized type kind: $kind")
}

fun GNode.Type.Const.kotlinPrimitiveType() = type.kotlinPrimitiveType()?.let { primType ->
    // Untyped is based on value
    when (primType) {
        BigInteger::class -> (value as GNode.Const.Int).v.untypedIntClass()
        BigDecimal::class -> when (value) {
            is GNode.Const.Int -> value.v.untypedFloatClass()
            is GNode.Const.Float -> value.v.untypedFloatClass()
            else -> error("Unknown float type of $value")
        }
        else -> primType
    }
}

// No dupes on recursive
fun GNode.Type.Interface.recursiveEmbedded(): List<GNode.Type.Named> {
    fun embeds(v: GNode.Type, list: MutableList<GNode.Type.Named>) {
        when (v) {
            is GNode.Type.Interface -> v.embeddeds.forEach { embeds(it, list) }
            is GNode.Type.Named -> if (!list.contains(v)) {
                list.add(v)
                embeds(v.underlying, list)
            }
        }
    }
    return mutableListOf<GNode.Type.Named>().also { embeds(this, it) }
}

fun GNode.Type.Struct.toAnonType(): Context.AnonStructType = Context.AnonStructType(
    fields = fields.map { field ->
        field.name to field.type.let { type ->
            if (type is GNode.Type.Struct) Context.AnonStructType.FieldType.Anon(type.toAnonType())
            else Context.AnonStructType.FieldType.Known(type)
        }
    }
).also { it.raw = this }

// Dereferences pointer as necessary, no dupes on recursive
fun GNode.Type.Struct.embeddeds(recursive: Boolean = false): List<GNode.Type.Named> {
    fun embeds(v: GNode.Type, list: MutableList<GNode.Type.Named>) {
        when (v) {
            is GNode.Type.Interface -> v.embeddeds.forEach { embeds(it, list) }
            is GNode.Type.Named -> if (!list.contains(v)) {
                list.add(v)
                if (recursive) embeds(v.underlying, list)
            }
            is GNode.Type.Pointer -> embeds(v.elem, list)
            is GNode.Type.Struct -> v.fields.forEach { if (it.embedded) embeds(it.type, list) }
        }
    }
    return mutableListOf<GNode.Type.Named>().also { embeds(this, it) }
}

// Any available in package, doesn't include embedded ones
fun GNode.Type.Struct.packageMethods(ctx: Context) = ctx.pkg.files.flatMap { file ->
    file.decls.mapNotNull { it as? GNode.Decl.Func }.filter { decl ->
        var type = decl.recv.singleOrNull()?.type?.type
        if (type is GNode.Type.Pointer) type = type.elem
        if (type is GNode.Type.TypeName) type = type.type
        type == this
    }
}