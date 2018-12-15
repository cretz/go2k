package go2k.compile2

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