package go2k.compile2

import kastree.ast.Node

fun Context.compileExpr(v: GNode.Expr, coerceTo: GNode.Expr? = null, byValue: Boolean = false): Node.Expr = TODO()

fun Context.compileExprCall(v: GNode.Expr.Call): Node.Expr = TODO()