package go2k.compile

import com.intellij.openapi.util.Disposer
import kastree.ast.ExtrasMap
import kastree.ast.Node
import kastree.ast.Writer
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler
import org.jetbrains.kotlin.cli.jvm.config.JvmClasspathRoot
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.config.addKotlinSourceRoot
import org.jetbrains.kotlin.load.java.JvmAbi
import org.jetbrains.kotlin.utils.PathUtil
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

open class JvmCompiler(
    val baseTempDir: Path = Paths.get(System.getProperty("java.io.tmpdir")!!),
    val writer: (Node, ExtrasMap?) -> String = Writer.Companion::write
) {
    fun compile(pkg: Compiler.KotlinPackage) {
        // Create a temp dir with all the files then delete the dir
        val dir = Files.createTempDirectory(baseTempDir, "go2k-jvmcompile")
        try {
            // Put a bunch of temp files in there
            val files = pkg.files.map { (name, file) ->
                dir.resolve(name).also { it.toFile().writeText(writer(file, null)) }
            }
            compile(files)
        } finally {
            dir.toFile().deleteRecursively()
        }
    }

    fun compile(files: List<Path>) {
        val disposable = Disposer.newDisposable()
        try {
            val messageCollector = MessageCollector()
            // Create config
            val conf = CompilerConfiguration()
            conf.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
            // TODO: custom module names
            conf.put(CommonConfigurationKeys.MODULE_NAME, JvmAbi.DEFAULT_MODULE_NAME)
            files.forEach { conf.addKotlinSourceRoot(it.toString()) }
            // TODO: make classpath overridable
            val classPath = System.getProperty("java.class.path")!!
            conf.addJvmClasspathRoots(classPath.split(File.pathSeparatorChar).map(::File))

            // Create env
            val env = KotlinCoreEnvironment.createForProduction(disposable, conf,
                EnvironmentConfigFiles.JVM_CONFIG_FILES)

            require(KotlinToJVMBytecodeCompiler.compileBunchOfSources(env))
            require(!messageCollector.hasErrors())
            TODO()
        } finally {
            Disposer.dispose(disposable)
        }
    }

    class MessageCollector : org.jetbrains.kotlin.cli.common.messages.MessageCollector {
        var hasErrors = false

        override fun clear() { hasErrors = true }

        override fun hasErrors() = hasErrors

        override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageLocation?) {
            if (!hasErrors && severity.isError) hasErrors = true
            println("[LOG] " + MessageRenderer.PLAIN_RELATIVE_PATHS.render(severity, message, location))
        }

    }

    companion object : JvmCompiler() {
        init {
            // Ref: https://github.com/arturbosch/detekt/issues/630
            System.setProperty("idea.use.native.fs.for.win", "false")
        }
    }
}