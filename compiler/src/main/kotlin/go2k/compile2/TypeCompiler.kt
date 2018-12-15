package go2k.compile2

import kastree.ast.Node

fun Context.compileType(v: GNode.Type): Node.Type = TODO()

fun Context.compileTypeMultiResult(fields: List<GNode.Field>): Node.Type? = TODO()

fun Context.compileTypeZeroExpr(v: GNode.Type): Node.Expr = TODO()