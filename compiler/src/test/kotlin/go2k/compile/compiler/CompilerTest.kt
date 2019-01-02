package go2k.compile.compiler

import go2k.compile.TestBase
import go2k.compile.TestUnit
import go2k.compile.go.Parser
import go2k.compile.jvm.JvmCompiler
import kastree.ast.Writer
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
@RunWith(Parameterized::class)
class CompilerTest(val unit: TestUnit) : TestBase() {

    @Test
    fun testCompiler() {
        debug { "Compiling ${unit.mainFilePath}" }
        debug { "Go output: ${unit.goRunOutput}" }
        // Parse
        val parsed = Parser.parse(unit.mainFilePath.toString())
        debug { "Parsed: $parsed" }
        // Compile to Kotlin
        val kotlinCompiled = parsed.packages.map {
            // Change the package name to a temp package so we don't conflict
            val overrideName = it.name + UUID.randomUUID().toString().replace("-", "")
            compilePackage(it, overrideName).also {
                it.files.forEach { (name, code) -> debug { "Code for $name:\n" + Writer.write(code) } }
            }
        }
        debug { "Compiled: $kotlinCompiled" }
        // Compile to JVM
        val jvmCompiler = if (unit.useExternalCompiler) externalCompiler else embeddedCompiler
        val jvmCompiled = jvmCompiler.compilePackages(kotlinCompiled)
        try {
            debug { "Main class: ${jvmCompiled.mainClassName}" }
            // Run and capture output
            val mainClass = jvmCompiled.newClassLoader().loadClass(jvmCompiled.mainClassName ?: error("No main class"))
            val method = mainClass.getMethod("main", Array<String>::class.java)
            val out = (System.out to System.err).let { (oldOut, oldErr) ->
                ByteArrayOutputStream().also {
                    PrintStream(it, true, "UTF-8").also { System.setOut(it); System.setErr(it) }
                    try {
                        method.invoke(null, emptyArray<String>())
                    } finally {
                        System.setOut(oldOut)
                        System.setErr(oldErr)
                    }
                }.toByteArray().toString(Charsets.UTF_8)
            }
            debug { "Kt output: $out" }
            assertEquals(unit.goRunOutput, out)
        } finally {
            jvmCompiled.cleanUp()
        }
    }

    companion object {
        val externalCompiler = JvmCompiler.External(printNonError = debug)
        val embeddedCompiler = JvmCompiler.Embedded(printNonError = debug)

        @JvmStatic @Parameterized.Parameters(name = "{0}")
        fun data() = TestUnit.localUnits//.filter { it.toString() == "type.go" }
    }
}