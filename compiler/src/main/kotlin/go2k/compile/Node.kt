package go2k.compile

import java.math.BigInteger

sealed class Node {
    data class Comment(
        val slash: Int,
        val text: String
    ) : Node()

    data class CommentGroup(
        val list: List<Comment>
    ) : Node()

    data class Field(
        val doc: CommentGroup?,
        val names: List<Expr.Ident>,
        val type: Expr,
        val tag: Expr.BasicLit?,
        val comment: CommentGroup?
    ) : Node()

    data class FieldList(
        val opening: Int,
        val list: List<Field>,
        val closing: Int
    ) : Node()

    data class File(
        val doc: CommentGroup?,
        val `package`: Int,
        val name: Expr.Ident,
        val decls: List<Decl>,
        val scope: Scope,
        val imports: List<Spec.ImportSpec>,
        val unresolved: List<Expr.Ident>,
        val comments: List<CommentGroup>
    ) : Node()

    data class Object(
        val kind: ObjKind,
        val name: String,
        val decl: Node?,
        val data: Any?,
        val type: Any?
    ) {
        enum class ObjKind { BAD, PKG, CON, TYP, VAR, FUN, LBL }
    }

    data class Package(
        val name: String,
        val scope: Scope?,
        val imports: Map<String, Node.Object>,
        val files: Map<String, File>
    ) : Node()

    data class Scope(
        val outer: Scope?,
        val objects: Map<String, Object>
    )

    sealed class Decl : Node() {
        data class BadDecl(
            val _ptr: BigInteger?,
            val from: Int,
            val to: Int
        ) : Decl()

        data class FuncDecl(
            val _ptr: BigInteger?,
            val doc: CommentGroup?,
            val recv: FieldList?,
            val name: Expr.Ident,
            val type: Expr.FuncType,
            val body: Stmt.BlockStmt?
        ) : Decl()

        data class GenDecl(
            val _ptr: BigInteger?,
            val doc: CommentGroup?,
            val tokPos: Int,
            val tok: Token,
            val lparen: Int,
            val specs: List<Spec>,
            val rparen: Int
        ) : Decl()

        data class Ref(
            val _ref: BigInteger
        ) : Decl()
    }

    sealed class Expr : Node() {
        data class ArrayType(
            val lbrack: Int,
            val len: Expr?,
            val elt: Expr
        ) : Expr()

        data class BadExpr(
            val from: Int,
            val to: Int
        ) : Expr()

        data class BasicLit(
            val valuePos: Int,
            val kind: Token,
            val value: String
        ) : Expr()

        data class BinaryExpr(
            val x: Expr,
            val opPos: Int,
            val op: Token,
            val y: Expr
        ) : Expr()

        data class CallExpr(
            val `fun`: Expr,
            val lparen: Int,
            val args: List<Expr>,
            val ellipsis: Int,
            val rparen: Int
        ) : Expr()

        data class ChanType(
            val begin: Int,
            val arrow: Int,
            val dir: Int,
            val value: Expr
        ) : Expr()

        data class CompositeLit(
            val type: Expr?,
            val lbrace: Int,
            val elts: List<Expr>,
            val rbrace: Int
        ) : Expr()

        data class Ellipsis(
            val ellipsis: Int,
            val elt: Expr?
        ) : Expr()

        data class FuncLit(
            val type: FuncType,
            val body: Stmt.BlockStmt
        ) : Expr()

        data class FuncType(
            val func: Int,
            val params: FieldList,
            val results: FieldList?
        ) : Expr()

        data class Ident(
            val namePos: Int,
            val name: String,
            val obj: Node.Object?
        ) : Expr()

        data class IndexExpr(
            val x: Expr,
            val lbrack: Int,
            val index: Expr,
            val rbrack: Int
        ) : Expr()

        data class InterfaceType(
            val `interface`: Int,
            val methods: FieldList,
            val incomplete: Boolean
        ) : Expr()

        data class KeyValueExpr(
            val key: Expr,
            val colon: Int,
            val value: Expr
        ) : Expr()

        data class MapType(
            val map: Int,
            val key: Expr,
            val value: Expr
        ) : Expr()

        data class ParenExpr(
            val lparen: Int,
            val x: Expr,
            val rparen: Int
        ) : Expr()

        data class SelectorExpr(
            val x: Expr,
            val sel: Ident
        ) : Expr()

        data class SliceExpr(
            val x: Expr,
            val lbrack: Int,
            val low: Expr?,
            val high: Expr?,
            val max: Expr?,
            val slice3: Boolean,
            val rbrack: Int
        ) : Expr()

        data class StarExpr(
            val star: Int,
            val x: Expr
        ) : Expr()

        data class StructType(
            val struct: Int,
            val fields: FieldList,
            val incomplete: Boolean
        ) : Expr()

        data class TypeAssertExpr(
            val x: Expr,
            val lparen: Int,
            val type: Expr?,
            val rparen: Int
        ) : Expr()

        data class UnaryExpr(
            val opPos: Int,
            val op: Token,
            val x: Expr
        ) : Expr()
    }

    sealed class Spec : Node() {
        data class ImportSpec(
            val doc: CommentGroup?,
            val name: Expr.Ident?,
            val path: Expr.BasicLit,
            val comment: CommentGroup?,
            val endPos: Int
        ) : Spec()

        data class TypeSpec(
            val doc: CommentGroup?,
            val name: Expr.Ident,
            val assign: Int,
            val type: Expr,
            val comment: CommentGroup?
        ) : Spec()

        data class ValueSpec(
            val doc: CommentGroup?,
            val names: List<Expr.Ident>,
            val type: Expr?,
            val values: List<Expr>,
            val comment: CommentGroup?
        ) : Spec()
    }

    sealed class Stmt : Node() {
        data class AssignStmt(
            val lhs: List<Expr>,
            val tokPos: Int,
            val tok: Token,
            val rhs: List<Expr>
        ) : Stmt()

        data class BadStmt(
            val from: Int,
            val to: Int
        ) : Stmt()

        data class BlockStmt(
            val lbrace: Int,
            val list: List<Stmt>,
            val rbrace: Int
        ) : Stmt()

        data class BranchStmt(
            val tokPos: Int,
            val tok: Token,
            val label: Expr.Ident?
        ) : Stmt()

        data class CaseClause(
            val case: Int,
            val list: List<Expr>,
            val colon: Int,
            val body: List<Stmt>
        ) : Stmt()

        data class CommClause(
            val case: Int,
            val comm: Stmt?,
            val colon: Int,
            val body: List<Stmt>
        ) : Stmt()

        data class DeclStmt(
            val decl: Decl
        ) : Stmt()

        data class DeferStmt(
            val defer: Int,
            val call: Expr.CallExpr
        ) : Stmt()

        data class EmptyStmt(
            val semicolon: Int,
            val implicit: Boolean
        ) : Stmt()

        data class ExprStmt(
            val x: Expr
        ) : Stmt()

        data class ForStmt(
            val `for`: Int,
            val init: Stmt?,
            val cond: Expr?,
            val post: Stmt?,
            val body: BlockStmt
        ) : Stmt()

        data class GoStmt(
            val go: Int,
            val call: Expr.CallExpr
        ) : Stmt()

        data class IfStmt(
            val `if`: Int,
            val init: Stmt?,
            val cond: Expr,
            val body: BlockStmt,
            val `else`: Stmt?
        ) : Stmt()

        data class IncDecStmt(
            val x: Expr,
            val tokPos: Int,
            val tok: Token
        ) : Stmt()

        data class LabeledStmt(
            val label: Expr.Ident,
            val colon: Int,
            val stmt: Stmt
        ) : Stmt()

        data class RangeStmt(
            val `for`: Int,
            val key: Expr?,
            val value: Expr?,
            val tokPos: Int,
            val tok: Token,
            val x: Expr,
            val body: BlockStmt
        ) : Stmt()

        data class ReturnStmt(
            val `return`: Int,
            val results: List<Expr>
        ) : Stmt()

        data class SelectStmt(
            val select: Int,
            val body: BlockStmt
        ) : Stmt()

        data class SendStmt(
            val chan: Expr,
            val arrow: Int,
            val value: Expr
        ) : Stmt()

        data class SwitchStmt(
            val switch: Int,
            val init: Stmt?,
            val tag: Expr?,
            val body: BlockStmt
        ) : Stmt()

        data class TypeSwitchStmt(
            val switch: Int,
            val init: Stmt?,
            val assign: Stmt,
            val body: BlockStmt
        ) : Stmt()
    }

    enum class Token(val string: String? = null) {
        // Special tokens
        ILLEGAL(),
        EOF(),
        COMMENT(),

        IGNORE_LITERAL_BEG(),
        IDENT(),
        INT(),
        FLOAT(),
        IMAG(),
        CHAR(),
        STRING(),
        IGNORE_LITERAL_END(),

        IGNORE_OPERATOR_BEG(),
        ADD("+"),
        SUB("-"),
        MUL("*"),
        QUO("/"),
        REM("%"),

        AND("&"),
        OR("|"),
        XOR("^"),
        SHL("<<"),
        SHR(">>"),
        AND_NOT("&^"),

        ADD_ASSIGN("+="),
        SUB_ASSIGN("-="),
        MUL_ASSIGN("*="),
        QUO_ASSIGN("/="),
        REM_ASSIGN("%="),

        AND_ASSIGN("&="),
        OR_ASSIGN("|="),
        XOR_ASSIGN("^="),
        SHL_ASSIGN("<<="),
        SHR_ASSIGN(">>="),
        AND_NOT_ASSIGN("&^="),

        LAND("&&"),
        LOR("||"),
        ARROW("<-"),
        INC("++"),
        DEC("--"),

        EQL("=="),
        LSS("<"),
        GTR(">"),
        ASSIGN("="),
        NOT("!"),

        NEQ("!="),
        LEQ("<="),
        GEQ(">="),
        DEFINE(":="),
        ELLIPSIS("..."),

        LPAREN("("),
        LBRACK("["),
        LBRACE("{"),
        COMMA(","),
        PERIOD("."),

        RPAREN(")"),
        RBRACK("]"),
        RBRACE("}"),
        SEMICOLON(";"),
        COLON(":"),
        IGNORE_OPERATOR_END(),

        IGNORE_KEYWORD_BEG(),
        BREAK("break"),
        CASE(),
        CHAN(),
        CONST(),
        CONTINUE("continue"),

        DEFAULT(),
        DEFER(),
        ELSE(),
        FALLTHROUGH("fallthrough"),
        FOR(),

        FUNC(),
        GO(),
        GOTO("goto"),
        IF(),
        IMPORT(),

        INTERFACE(),
        MAP(),
        PACKAGE(),
        RANGE(),
        RETURN(),

        SELECT(),
        STRUCT(),
        SWITCH(),
        TYPE(),
        VAR(),
        IGNORE_KEYWORD_END(),
    }
}