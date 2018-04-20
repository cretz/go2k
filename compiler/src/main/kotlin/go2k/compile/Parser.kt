package go2k.compile

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.KotlinJsonAdapterFactory
import java.lang.reflect.Type
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass

interface Parser {
    fun parsePackage(qualifiedPackage: String): ParseResult {
        val goPath = System.getenv("GOPATH") ?: error("Missing GOPATH env var")
        return parseDir(Paths.get(goPath, "src", qualifiedPackage))
    }

    fun parseDir(fullPath: Path): ParseResult

    data class ParseResult(val packages: Map<String, Node.Package>)

    class FromGoProcess : Parser {
        override fun parseDir(fullPath: Path): ParseResult {
            if (!Files.isDirectory(fullPath)) error("Not a directory: $fullPath")
            // Get dumpast path
            val goPath = System.getenv("GOPATH") ?: error("Missing GOPATH env var")
            val dumpAstPath = Paths.get(goPath, "src/github.com/cretz/go2k/compiler/src/main/go/dumpast.go")
            if (!Files.isRegularFile(dumpAstPath)) error("Not a file: $dumpAstPath")
            // Run proc
            val pb = ProcessBuilder("go", "run", dumpAstPath.toString(), fullPath.toString()).redirectErrorStream(true)
            val proc = pb.start()
            val output = proc.inputStream.use { it.readBytes() }.toString(Charsets.UTF_8)
            require(proc.exitValue() == 0, { "Unexpected exit value of ${proc.exitValue()}, output: $output" })
            // Parse JSON
            return jsonAdapter.fromJson(output) ?: error("JSON object not returned")
        }

        companion object {
            val moshi = Moshi.Builder().
                    add(SealedClassJsonAdapterFactory).
                    add(EnumOrdinalJsonAdapterFactory).
                    add(NullEmptyCollectionJsonAdapterFactory).
                    add(KotlinJsonAdapterFactory()).
                    add(BigInteger::class.java, BigIntegerJsonAdapter).
                    build()!!
            val jsonAdapter = moshi.adapter(ParseResult::class.java)!!
        }
    }

    object SealedClassJsonAdapterFactory : JsonAdapter.Factory {
        override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
            val cls = (type as? Class<*>)?.kotlin?.takeIf { it.isSealed } ?: return null
            fun allNestedClasses(cls: KClass<*>): Set<KClass<*>> {
                return cls.nestedClasses.toSet() + cls.nestedClasses.flatMap(::allNestedClasses)
            }
            return Adapter(allNestedClasses(cls).map { nestedClass ->
                nestedClass.simpleName!! to moshi.adapter(nestedClass.java)
            }.toMap())
        }

        class Adapter(val map: Map<String, JsonAdapter<*>>) : JsonAdapter<Any?>() {
            override fun fromJson(reader: JsonReader): Any? {
                // TODO: this is heavy because we can't peek properties. Consider creating a new JsonReader that
                // delegates to the existing JSON reader but "prepends" a beginObject
                @Suppress("UNCHECKED_CAST")
                val value = (reader.readJsonValue() ?: return null) as? MutableMap<Any, Any> ?: error("Invalid value")
                val adapter = map[value["_type"] as? String ?:
                    error("Missing _type at path ${reader.path}")] ?: error("Missing adapter at path ${reader.path}")
                return adapter.fromJsonValue(value)
            }

            override fun toJson(writer: JsonWriter, value: Any?) { throw UnsupportedOperationException("Only reads") }
        }
    }

    object EnumOrdinalJsonAdapterFactory : JsonAdapter.Factory {
        override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
            val enumClass = (type as? Class<*>)?.takeIf { it.isEnum } ?: return null
            val next = moshi.nextAdapter<Any?>(this, type, annotations)
            return Adapter(enumClass.enumConstants, next)
        }

        class Adapter(val values: Array<out Any>, val next: JsonAdapter<*>) : JsonAdapter<Any?>() {
            override fun fromJson(reader: JsonReader): Any? {
                if (reader.peek() == JsonReader.Token.NUMBER) return values[reader.nextInt()]
                return next.fromJson(reader)
            }
            override fun toJson(writer: JsonWriter, value: Any?) { throw UnsupportedOperationException("Only reads") }
        }
    }

    object BigIntegerJsonAdapter : JsonAdapter<BigInteger>() {
        override fun fromJson(reader: JsonReader): BigInteger? {
            if (reader.peek() == JsonReader.Token.NULL) return null
            return BigInteger(reader.nextString())
        }

        override fun toJson(writer: JsonWriter?, value: BigInteger?) {
            throw UnsupportedOperationException("Only reads")
        }
    }

    object NullEmptyCollectionJsonAdapterFactory : JsonAdapter.Factory {
        override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
            val rawType = Types.getRawType(type)
            if (rawType != List::class.java && rawType != Set::class.java && rawType != Collection::class.java)
                return null
            return Adapter(moshi.nextAdapter<Any?>(this, type, annotations))
        }

        class Adapter(val next: JsonAdapter<*>) : JsonAdapter<Any?>() {
            override fun fromJson(reader: JsonReader): Any? {
                if (reader.peek() == JsonReader.Token.NULL) return next.fromJsonValue(emptyList<Any?>())
                return next.fromJson(reader)
            }

            override fun toJson(writer: JsonWriter?, value: Any?) { throw UnsupportedOperationException("Only reads") }
        }
    }
}