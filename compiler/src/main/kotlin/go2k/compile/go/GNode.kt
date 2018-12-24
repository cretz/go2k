package go2k.compile.go

sealed class GNode {

    sealed class Const : GNode() {
        data class Boolean(val v: kotlin.Boolean) : Const()
        data class Complex(val v: kotlin.String) : Const()
        data class Float(val v: kotlin.String) : Const()
        data class Int(val v: kotlin.String) : Const()
        data class String(val v: kotlin.String) : Const()
    }

    sealed class Decl : GNode() {
        data class Const(val specs: List<Spec.Value>) : Decl()
        data class Func(
            val recv: List<Field>,
            val name: String,
            val type: Expr.FuncType,
            val body: Stmt.Block
        ) : Decl()
        data class Import(val specs: List<Spec.Import>) : Decl()
        data class Type(val specs: List<Spec.Type>) : Decl()
        data class Var(val specs: List<Spec.Value>) : Decl()
    }

    sealed class Expr : GNode() {
        abstract val type: Type?

        data class ArrayType(
            override val type: Type?,
            val len: Expr?,
            val elt: Expr
        ) : Expr()
        data class BasicLit(
            override val type: Type?,
            val kind: Kind,
            val value: String
        ) : Expr() {
            enum class Kind {
                CHAR, FLOAT, IMAG, INT, STRING
            }
        }
        data class Binary(
            override val type: Type?,
            val x: Expr,
            val op: Token,
            val y: Expr
        ) : Expr() {
            enum class Token {
                ADD, AND, AND_NOT, EQL, GEQ, GTR, LAND, LEQ, LOR, LSS, MUL, NEQ, OR, QUO, REM, SHL, SHR, SUB, XOR
            }
        }
        data class Call(
            override val type: Type?,
            val func: Expr,
            val args: List<Expr>
        ) : Expr()
        data class ChanType(
            override val type: Type?,
            val value: Expr,
            val canSend: Boolean,
            val canRecv: Boolean
        ) : Expr()
        data class CompositeLit(
            override val type: Type?,
            val litType: Expr?,
            val elts: List<Expr>
        ) : Expr()
        data class Ellipsis(
            override val type: Type?,
            val elt: Expr?
        ) : Expr()
        data class FuncLit(
            override val type: Type?,
            val funcType: FuncType,
            val body: Stmt.Block
        ) : Expr()
        data class FuncType(
            override val type: Type?,
            val params: List<Field>,
            val results: List<Field>
        ) : Expr()
        data class Ident(
            override val type: Type?,
            val name: String,
            val defType: Type?
        ) : Expr()
        data class Index(
            override val type: Type?,
            val x: Expr,
            val index: Expr
        ) : Expr()
        data class InterfaceType(
            override val type: Type?,
            val methods: List<Field>
        ) : Expr()
        data class KeyValue(
            override val type: Type?,
            val key: Expr,
            val value: Expr
        ) : Expr()
        data class MapType(
            override val type: Type?,
            val key: Expr,
            val value: Expr
        ) : Expr()
        data class Paren(
            override val type: Type?,
            val x: Expr
        ) : Expr()
        data class Selector(
            override val type: Type?,
            val x: Expr,
            val sel: Ident
        ) : Expr()
        data class Slice(
            override val type: Type?,
            val x: Expr,
            val low: Expr?,
            val high: Expr?,
            val max: Expr?,
            val slice3: Boolean
        ) : Expr()
        data class Star(
            override val type: Type?,
            val x: Expr
        ) : Expr()
        data class StructType(
            override val type: Type?,
            val fields: List<Field>
        ) : Expr()
        data class TypeAssert(
            override val type: Type?,
            val x: Expr,
            val assertType: Expr?
        ) : Expr()
        data class Unary(
            override val type: Type?,
            val token: Token,
            val x: Expr
        ) : Expr() {
            enum class Token {
                ADD, AND, ARROW, NOT, SUB, XOR
            }
        }
    }

    data class Field(
        val names: List<Expr.Ident>,
        val type: Expr,
        val tag: String?
    ) : GNode()

    data class File(
        val fileName: String,
        val packageName: String,
        val decls: List<Decl>
    ) : GNode()

    data class Package(
        val name: String,
        val path: String,
        val files: List<File>,
        val types: List<Type>,
        val varInitOrder: List<String>
    ) : GNode()

    sealed class Spec : GNode() {
        data class Import(val name: String?, val path: String) : Spec()
        data class Type(val name: String, val expr: Expr, val alias: Boolean) : Spec()
        data class Value(val names: List<Expr.Ident>, val type: Expr?, val values: List<Expr>) : Spec()
    }

    sealed class Stmt : GNode() {
        data class Assign(
            val lhs: List<GNode.Expr>,
            val tok: Token,
            val rhs: List<GNode.Expr>
        ) : Stmt() {
            enum class Token {
                ADD, AND, AND_NOT, ASSIGN, DEFINE, MUL, OR, QUO, REM, SHL, SHR, SUB, XOR
            }
        }
        data class Block(val stmts: List<Stmt>) : Stmt()
        data class Branch(val tok: Token, val label: GNode.Expr.Ident?) : Stmt() {
            enum class Token {
                BREAK, CONTINUE, FALLTHROUGH, GOTO
            }
        }
        data class Decl(val decl: GNode.Decl) : Stmt()
        data class Defer(val call: GNode.Expr.Call) : Stmt()
        object Empty : Stmt()
        data class Expr(val x: GNode.Expr) : Stmt()
        data class For(
            val init: Stmt?,
            val cond: GNode.Expr?,
            val post: Stmt?,
            val body: Block
        ) : Stmt()
        data class Go(val call: GNode.Expr.Call) : Stmt()
        data class If(
            val init: Stmt?,
            val cond: GNode.Expr,
            val body: Block,
            val elseStmt: Stmt?
        ) : Stmt()
        data class IncDec(val x: GNode.Expr, val inc: Boolean) : Stmt()
        data class Labeled(val label: GNode.Expr.Ident, val stmt: Stmt) : Stmt()
        data class Range(
            val key: GNode.Expr?,
            val value: GNode.Expr?,
            val define: Boolean,
            val x: GNode.Expr,
            val body: Block
        ) : Stmt()
        data class Return(val results: List<GNode.Expr>) : Stmt()
        data class Select(val cases: List<CommClause>) : Stmt() {
            data class CommClause(val comm: Stmt?, val body: List<Stmt>) : GNode()
        }
        data class Send(val chan: GNode.Expr, val value: GNode.Expr) : Stmt()
        data class Switch(
            val init: Stmt?,
            // Will be Stmt.Expr or null when type is false, not null stmt when type is true
            val tag: Stmt?,
            val type: Boolean,
            val cases: List<CaseClause>
        ) : Stmt() {
            data class CaseClause(val list: List<Expr>, val body: List<Stmt>) : GNode()
        }
    }

    sealed class Type : GNode() {
        interface NamedEntity {
            val pkg: String?
            val name: String
            val type: Type?
        }

        sealed class MaybeLazy<T: Type> {
            abstract operator fun invoke(): T
            data class Lazy<T: Type>(val type: () -> T) : MaybeLazy<T>() {
                override operator fun invoke() = type()
            }
            data class Eager<T: Type>(val type: T) : MaybeLazy<T>() {
                override operator fun invoke() = type
            }
        }

        data class Array(val elem: Type, val len: Long) : Type()
        data class Basic(val name: String, val kind: Kind) : Type() {
            enum class Kind {
                INVALID, BOOL, INT, INT_8, INT_16, INT_32, INT_64,
                UINT, UINT_8, UINT_16, UINT_32, UINT_64, UINT_PTR,
                FLOAT_32, FLOAT_64, COMPLEX_64, COMPLEX_128, STRING,
                UNSAFE_POINTER, UNTYPED_BOOL, UNTYPED_INT, UNTYPED_RUNE,
                UNTYPED_FLOAT, UNTYPED_COMPLEX, UNTYPED_STRING, UNTYPED_NIL
            }
        }
        data class BuiltIn(
            override val pkg: String?,
            override val name: String,
            override val type: Type?
        ) : Type(), NamedEntity
        data class Chan(val elem: Type, val canSend: Boolean, val canRecv: Boolean) : Type()
        data class Const(
            override val pkg: String?,
            override val name: String,
            override val type: Type,
            val value: GNode.Const?
        ) : Type(), NamedEntity
        data class Func(
            override val pkg: String?,
            override val name: String,
            override val type: Signature
        ) : Type(), NamedEntity
        data class Interface(val methods: List<Func>, val embeddeds: List<Type>) : Type()
        data class Label(
            override val pkg: String?,
            override val name: String,
            override val type: Type?
        ) : Type(), NamedEntity
        data class Map(val elem: Type, val key: Type) : Type()
        data class Named(val name: MaybeLazy<TypeName>, val underlying: Type, val methods: List<Func>) : Type()
        object Nil : Type()
        data class Package(val name: String) : Type()
        data class Pointer(val elem: Type) : Type()
        data class Signature(
            val recv: Var?,
            val params: List<Var>,
            val results: List<Var>,
            val variadic: Boolean
        ) : Type()
        data class Slice(val elem: Type) : Type()
        data class Struct(val fields: List<Var>, val tags: List<String>) : Type()
        data class Tuple(val vars: List<Var>) : Type()
        data class TypeName(
            override val pkg: String?,
            override val name: String,
            override val type: Type?
        ) : Type(), NamedEntity
        data class Var(
            override val pkg: String?,
            override val name: String,
            // Type is lazy due to self references (e.g. struct method w/ a param of itself)
            val lazyType: MaybeLazy<Type>,
            val embedded: Boolean
        ) : Type(), NamedEntity {
            override val type get() = lazyType()
        }
    }
}