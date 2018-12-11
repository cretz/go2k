package go2k.compile

import kastree.ast.Writer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*

@ExperimentalUnsignedTypes
class CompilerTest : TestBase() {
    @ParameterizedTest(name = "{0}")
    @MethodSource("unitProvider")
    fun testCompiler(unit: TestUnit) {
        debug { "Compiling ${unit.mainFilePath}" }
        debug { "Go output: ${unit.goRunOutput}" }
        // Parse
        val parsed = Parser.parse(unit.mainFilePath.toString())
        debug { "Parsed: $parsed" }
        // Compile
        val compiled = parsed.packages.packages.map {
            // Change the package name to a temp package so we don't conflict
            val overrideName = it.name + UUID.randomUUID().toString().replace("-", "")
            Compiler.compilePackage(it, overrideName).also {
                it.files.forEach { (name, code) -> debug { "Code for $name:\n" + Writer.write(code) } }
            }
        }
        debug { "Compiled: $compiled" }
        val compiler = if (unit.useExternalCompiler) externalCompiler else embeddedCompiler
        val jvmCompiled = compiler.compilePackages(compiled)
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

        @JvmStatic
        @Suppress("unused")
        fun unitProvider() = TestUnit.localUnits//.filter { it.toString() == "chan.go" }
    }
}