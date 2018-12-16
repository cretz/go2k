package go2k.compile.go.dumppb

data class Token(override val value: Int) : pbandk.Message.Enum {
    companion object : pbandk.Message.Enum.Companion<Token> {
        val ILLEGAL = Token(0)
        val EOF = Token(1)
        val COMMENT = Token(2)
        val IDENT = Token(4)
        val INT = Token(5)
        val FLOAT = Token(6)
        val IMAG = Token(7)
        val CHAR = Token(8)
        val STRING = Token(9)
        val ADD = Token(12)
        val SUB = Token(13)
        val MUL = Token(14)
        val QUO = Token(15)
        val REM = Token(16)
        val AND = Token(17)
        val OR = Token(18)
        val XOR = Token(19)
        val SHL = Token(20)
        val SHR = Token(21)
        val AND_NOT = Token(22)
        val ADD_ASSIGN = Token(23)
        val SUB_ASSIGN = Token(24)
        val MUL_ASSIGN = Token(25)
        val QUO_ASSIGN = Token(26)
        val REM_ASSIGN = Token(27)
        val AND_ASSIGN = Token(28)
        val OR_ASSIGN = Token(29)
        val XOR_ASSIGN = Token(30)
        val SHL_ASSIGN = Token(31)
        val SHR_ASSIGN = Token(32)
        val AND_NOT_ASSIGN = Token(33)
        val LAND = Token(34)
        val LOR = Token(35)
        val ARROW = Token(36)
        val INC = Token(37)
        val DEC = Token(38)
        val EQL = Token(39)
        val LSS = Token(40)
        val GTR = Token(41)
        val ASSIGN = Token(42)
        val NOT = Token(43)
        val NEQ = Token(44)
        val LEQ = Token(45)
        val GEQ = Token(46)
        val DEFINE = Token(47)
        val ELLIPSIS = Token(48)
        val LPAREN = Token(49)
        val LBRACK = Token(50)
        val LBRACE = Token(51)
        val COMMA = Token(52)
        val PERIOD = Token(53)
        val RPAREN = Token(54)
        val RBRACK = Token(55)
        val RBRACE = Token(56)
        val SEMICOLON = Token(57)
        val COLON = Token(58)
        val BREAK = Token(61)
        val CASE = Token(62)
        val CHAN = Token(63)
        val CONST = Token(64)
        val CONTINUE = Token(65)
        val DEFAULT = Token(66)
        val DEFER = Token(67)
        val ELSE = Token(68)
        val FALLTHROUGH = Token(69)
        val FOR = Token(70)
        val FUNC = Token(71)
        val GO = Token(72)
        val GOTO = Token(73)
        val IF = Token(74)
        val IMPORT = Token(75)
        val INTERFACE = Token(76)
        val MAP = Token(77)
        val PACKAGE = Token(78)
        val RANGE = Token(79)
        val RETURN = Token(80)
        val SELECT = Token(81)
        val STRUCT = Token(82)
        val SWITCH = Token(83)
        val TYPE = Token(84)
        val VAR = Token(85)

        override fun fromValue(value: Int) = when (value) {
            0 -> ILLEGAL
            1 -> EOF
            2 -> COMMENT
            4 -> IDENT
            5 -> INT
            6 -> FLOAT
            7 -> IMAG
            8 -> CHAR
            9 -> STRING
            12 -> ADD
            13 -> SUB
            14 -> MUL
            15 -> QUO
            16 -> REM
            17 -> AND
            18 -> OR
            19 -> XOR
            20 -> SHL
            21 -> SHR
            22 -> AND_NOT
            23 -> ADD_ASSIGN
            24 -> SUB_ASSIGN
            25 -> MUL_ASSIGN
            26 -> QUO_ASSIGN
            27 -> REM_ASSIGN
            28 -> AND_ASSIGN
            29 -> OR_ASSIGN
            30 -> XOR_ASSIGN
            31 -> SHL_ASSIGN
            32 -> SHR_ASSIGN
            33 -> AND_NOT_ASSIGN
            34 -> LAND
            35 -> LOR
            36 -> ARROW
            37 -> INC
            38 -> DEC
            39 -> EQL
            40 -> LSS
            41 -> GTR
            42 -> ASSIGN
            43 -> NOT
            44 -> NEQ
            45 -> LEQ
            46 -> GEQ
            47 -> DEFINE
            48 -> ELLIPSIS
            49 -> LPAREN
            50 -> LBRACK
            51 -> LBRACE
            52 -> COMMA
            53 -> PERIOD
            54 -> RPAREN
            55 -> RBRACK
            56 -> RBRACE
            57 -> SEMICOLON
            58 -> COLON
            61 -> BREAK
            62 -> CASE
            63 -> CHAN
            64 -> CONST
            65 -> CONTINUE
            66 -> DEFAULT
            67 -> DEFER
            68 -> ELSE
            69 -> FALLTHROUGH
            70 -> FOR
            71 -> FUNC
            72 -> GO
            73 -> GOTO
            74 -> IF
            75 -> IMPORT
            76 -> INTERFACE
            77 -> MAP
            78 -> PACKAGE
            79 -> RANGE
            80 -> RETURN
            81 -> SELECT
            82 -> STRUCT
            83 -> SWITCH
            84 -> TYPE
            85 -> VAR
            else -> Token(value)
        }
    }
}
