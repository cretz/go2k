package go2k.compile

import com.intellij.openapi.util.Disposer
import kastree.ast.ExtrasMap
import kastree.ast.Node
import kastree.ast.Writer
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.config.addKotlinSourceRoot
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.output.writeAllTo
import org.jetbrains.kotlin.cli.jvm.compiler.CompileEnvironmentUtil
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler
import org.jetbrains.kotlin.cli.jvm.config.JvmClasspathRoot
import org.jetbrains.kotlin.cli.jvm.config.JvmModulePathRoot
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.codegen.GeneratedClassLoader
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.languageVersionSettings
import org.jetbrains.kotlin.fileClasses.JvmFileClassUtil
import org.jetbrains.kotlin.idea.MainFunctionDetector
import org.jetbrains.kotlin.load.java.JvmAbi
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@ExperimentalUnsignedTypes
open class JvmCompiler(
    val baseTempDir: Path = Paths.get(System.getProperty("java.io.tmpdir")!!),
    val writer: (Node, ExtrasMap?) -> String = Writer.Companion::write
) {
    fun compilePackages(pkgs: List<Compiler.KotlinPackage>): Compiled {
        // Create a temp dir with all the files then delete the dir
        val dir = Files.createTempDirectory(baseTempDir, "go2k-jvmcompile")
        try {
            // Use a bunch of temp files
            return compileFiles(pkgs.flatMap { pkg ->
                pkg.files.map { (name, file) ->
                    dir.resolve(name).also { it.toFile().writeText(writer(file, null)) }
                }
            })
        } finally {
            dir.toFile().deleteRecursively()
        }
    }

    fun compileFiles(files: List<Path>): Compiled {
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

            // Do compile
            return KotlinToJVMBytecodeCompiler.analyzeAndGenerate(env).let {
                require(!messageCollector.hasErrors()) { "Found one or more errors during compilation" }
                Compiled.FromKotlinCompiler(env, it ?: error("No state found"))
            }
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

    interface Compiled {
        val mainClassName: String?
        fun newClassLoader(parent: ClassLoader? = null): ClassLoader
        fun writeFiles(dir: Path)
        fun writeJar(path: Path, includeRuntime: Boolean = false)

        class FromKotlinCompiler(val env: KotlinCoreEnvironment, val state: GenerationState) : Compiled {
            val mainClassFqName by lazy {
                MainFunctionDetector(state.bindingContext, env.configuration.languageVersionSettings).let { detect ->
                    env.getSourceFiles().singleOrNull { detect.hasMain(it.declarations) }?.let {
                        JvmFileClassUtil.getFileClassInfoNoResolve(it).facadeClassFqName
                    }
                }
            }

            override val mainClassName get() = mainClassFqName?.asString()

            override fun newClassLoader(parent: ClassLoader?): ClassLoader {
                val parentLoader = parent ?: URLClassLoader(
                    env.configuration.getList(CLIConfigurationKeys.CONTENT_ROOTS).mapNotNull {
                        when (it) {
                            is JvmModulePathRoot -> it.file.toURI().toURL()
                            is JvmClasspathRoot -> it.file.toURI().toURL()
                            else -> null
                        }
                    }.toTypedArray(),
                    null
                )
                return GeneratedClassLoader(state.factory, parentLoader)
            }

            override fun writeFiles(dir: Path) = state.factory.writeAllTo(dir.toFile())

            override fun writeJar(path: Path, includeRuntime: Boolean) =
                CompileEnvironmentUtil.writeToJar(path.toFile(), includeRuntime, mainClassFqName, state.factory)
        }
    }

    companion object : JvmCompiler() {
        init {
            // Ref: https://github.com/arturbosch/detekt/issues/630
            System.setProperty("idea.use.native.fs.for.win", "false")
        }
    }
}