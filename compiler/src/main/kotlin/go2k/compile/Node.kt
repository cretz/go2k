package go2k.compile

sealed class Node {

    data class Comment(
        val slash: Int,
        val text: String
    ) : Node()

    data class CommentGroup(
        val list: List<Comment>
    ) : Node()

    sealed class Decl : Node() {
        data class Bad(
            val from: Int,
            val to: Int
        ) : Decl()
    }

    sealed class Expr : Node() {
        data class ArrayType(
            val lbrack: Int,
            val len: Expr?,
            val elt: Expr
        ) : Expr()

        data class Bad(
            val from: Int,
            val to: Int
        ) : Expr()

        data class BasicLit(
            val valuePos: Int,
            val kind: Token,
            val value: String
        ) : Expr()

        data class Binary(
            val x: Expr,
            val opPos: Int,
            val op: Token,
            val y: Expr
        ) : Expr()

        data class Call(
            val fun_: Expr,
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

        data class ParenExpr(
            val lparen: Int,
            val x: Expr,
            val rparen: Int
        ) : Expr()

        data class Ident : Expr()
    }

    sealed class Spec : Node() {

    }

    sealed class Stmt : Node() {
        data class Assign(
            val lhs: List<Expr>,
            val tokPos: Int,
            val tok: Token,
            val rhs: List<Expr>
        ) : Stmt()

        data class Bad(
            val from: Int,
            val to: Int
        ) : Stmt()

        data class Block(
            val lbrace: Int,
            val list: List<Stmt>,
            val rbrace: Int
        ) : Stmt()

        data class Branch(
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
    }

    enum class Token(val string: String? = null) {
        // Special tokens
        ILLEGAL(),
        EOF(),
        COMMENT(),

        IDENT(),
        INT(),
        FLOAT(),
        IMAG(),
        CHAR(),
        STRING(),

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
    }
}