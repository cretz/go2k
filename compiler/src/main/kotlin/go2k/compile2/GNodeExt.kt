package go2k.compile2

import java.math.BigDecimal
import java.math.BigInteger

fun GNode.Package.defaultPackageName() = path.split('/').filter(String::isNotEmpty).let { pieces ->
    val first = pieces.first().let {
        val dotIndex = it.indexOf('.')
        if (dotIndex == -1) it else it.substring(dotIndex + 1) + '.' + it.substring(0, dotIndex)
    }
    (listOf(first) + pieces.drop(1)).joinToString(".")
}

// Does not include nested functions
fun GNode.Stmt.visitStmts(fn: (GNode.Stmt) -> Unit) {
    fn(this)
    when (this) {
        is GNode.Stmt.Block -> stmts.forEach { it.visitStmts(fn) }
        is GNode.Stmt.For -> {
            init?.visitStmts(fn)
            post?.visitStmts(fn)
            body.visitStmts(fn)
        }
        is GNode.Stmt.If -> {
            init?.visitStmts(fn)
            body.visitStmts(fn)
            elseStmt?.visitStmts(fn)
        }
        is GNode.Stmt.Labeled -> stmt.visitStmts(fn)
        is GNode.Stmt.Range -> body.visitStmts(fn)
        is GNode.Stmt.Select -> cases.forEach {
            it.comm?.visitStmts(fn)
            it.body.forEach { it.visitStmts(fn) }
        }
        is GNode.Stmt.Switch -> {
            init?.visitStmts(fn)
            tag?.visitStmts(fn)
            cases.forEach { it.body.forEach { it.visitStmts(fn) } }
        }
    }
}

val GNode.Type.isArray get() = this is GNode.Type.Array
val GNode.Type.isJavaPrimitive get() = this is GNode.Type.Basic && kind != GNode.Type.Basic.Kind.STRING
val GNode.Type.isNullable get() = when (this) {
    is GNode.Type.Chan, is GNode.Type.Func, is GNode.Type.Interface,
        is GNode.Type.Map, is GNode.Type.Pointer, is GNode.Type.Slice -> true
    else -> false
}

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