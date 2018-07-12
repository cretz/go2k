package go2k.compile

import com.squareup.kotlinpoet.*
import go2k.compile.dumppb.*

open class Compiler {
    fun compile(v: Package): List<FileSpec> {
        return v.files.map { compile(it.key, it.value!!) }
    }

    fun compile(fileName: String, v: File): FileSpec {
        val ctx = FileContext()
        val bld = FileSpec.builder("somepkg", "somefile")
        v.decls.forEach {
            AstAny.Decl.fromAny(it).also {
                when (it) {
                    is AstAny.Decl.FuncDecl -> bld.addFunction(compileFun(ctx, it.v))
                    is AstAny.Decl.GenDecl -> when (it.v.tok) {
                        Token.CONST -> compileConst(ctx, it.v).forEach { bld.addProperty(it) }
                        Token.VAR -> bld.addProperty(compileVar(ctx, it.v))
                        else -> error("Unsupported decl token: ${it.v.tok}")
                    }
                }
            }
        }
        return bld.build()
    }

    fun compileConst(ctx: FileContext, v: GenDecl): List<PropertySpec> {
        println("Const decl: $v")
        return v.specs.flatMap {
            AstAny.Spec.fromAny(it).let {
                if (it is AstAny.Spec.ValueSpec) compileConst(ctx, it.v)
                else error("Unrecognized spec: $it")
            }
        }
    }

    fun compileConst(ctx: FileContext, v: ValueSpec): List<PropertySpec> {
        println("Const spec: $v")
        return v.names.mapIndexed { index, name ->
            val value = AstAny.Expr.fromAny(v.values.getOrElse(index) { error("No value for const $it") })
            println("Value: $value")
            compileExpr(ctx, value).let { (code, typeName) ->
                PropertySpec.builder(name.name, typeName, KModifier.CONST).initializer(code).build()
            }
        }
    }

    fun compileExpr(ctx: FileContext, v: AstAny.Expr): Pair<CodeBlock, TypeName> {
        return when (v) {
            is AstAny.Expr.BasicLit -> v.v.kind.let {
                when (it) {
                    Token.STRING -> CodeBlock.of("%S", v.v.value) to String::class.asTypeName()
                    else -> error("Unrecognized token: $it")
                }
            }
            else -> error("Unrecognized expression: $v")
        }
    }

    fun compileFun(ctx: FileContext, v: FuncDecl): FunSpec { TODO() }

    fun compileVar(ctx: FileContext, v: GenDecl): PropertySpec {
        TODO()
    }

    class FileContext {

    }

    companion object : Compiler()
}