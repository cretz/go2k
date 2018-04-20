package go2k.compile

import org.junit.jupiter.api.Test
import java.nio.file.Paths

class ParserTest {
    @Test
    fun testSimpleParse() {
        val parsed = Parser.FromGoProcess().parseDir(Paths.get("src/main/go"))
        // Let's make sure there's a jsonableCtx struct decl
        parsed.packages["main"]!!.
            files.values.first().decls.
            mapNotNull { it as? Node.Decl.GenDecl }.
            filter { it.tok == Node.Token.TYPE }.
            flatMap { it.specs }.
            mapNotNull { it as? Node.Spec.TypeSpec }.
            find { it.type is Node.Expr.StructType && it.name.name == "jsonableCtx" } ?: error("Struct not found")
    }
}