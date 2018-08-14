package go2k.compile.dumppb

data class Comment(
    val slash: Int = 0,
    val text: String = "",
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Comment> {
    override operator fun plus(other: Comment?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Comment> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Comment.protoUnmarshalImpl(u)
    }
}

data class CommentGroup(
    val list: List<go2k.compile.dumppb.Comment> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<CommentGroup> {
    override operator fun plus(other: CommentGroup?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<CommentGroup> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = CommentGroup.protoUnmarshalImpl(u)
    }
}

data class Field(
    val doc: go2k.compile.dumppb.CommentGroup? = null,
    val names: List<go2k.compile.dumppb.Ident> = emptyList(),
    val type: go2k.compile.dumppb.Expr_? = null,
    val tag: go2k.compile.dumppb.BasicLit? = null,
    val comment: go2k.compile.dumppb.CommentGroup? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Field> {
    override operator fun plus(other: Field?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Field> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Field.protoUnmarshalImpl(u)
    }
}

data class FieldList(
    val opening: Int = 0,
    val list: List<go2k.compile.dumppb.Field> = emptyList(),
    val closing: Int = 0,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<FieldList> {
    override operator fun plus(other: FieldList?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<FieldList> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = FieldList.protoUnmarshalImpl(u)
    }
}

data class Expr_(
    val expr: Expr? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Expr_> {
    sealed class Expr {
        data class BadExpr(val badExpr: go2k.compile.dumppb.BadExpr) : Expr()
        data class Ident(val ident: go2k.compile.dumppb.Ident) : Expr()
        data class Ellipsis(val ellipsis: go2k.compile.dumppb.Ellipsis) : Expr()
        data class BasicLit(val basicLit: go2k.compile.dumppb.BasicLit) : Expr()
        data class FuncLit(val funcLit: go2k.compile.dumppb.FuncLit) : Expr()
        data class CompositeLit(val compositeLit: go2k.compile.dumppb.CompositeLit) : Expr()
        data class ParenExpr(val parenExpr: go2k.compile.dumppb.ParenExpr) : Expr()
        data class SelectorExpr(val selectorExpr: go2k.compile.dumppb.SelectorExpr) : Expr()
        data class IndexExpr(val indexExpr: go2k.compile.dumppb.IndexExpr) : Expr()
        data class SliceExpr(val sliceExpr: go2k.compile.dumppb.SliceExpr) : Expr()
        data class TypeAssertExpr(val typeAssertExpr: go2k.compile.dumppb.TypeAssertExpr) : Expr()
        data class CallExpr(val callExpr: go2k.compile.dumppb.CallExpr) : Expr()
        data class StarExpr(val starExpr: go2k.compile.dumppb.StarExpr) : Expr()
        data class UnaryExpr(val unaryExpr: go2k.compile.dumppb.UnaryExpr) : Expr()
        data class BinaryExpr(val binaryExpr: go2k.compile.dumppb.BinaryExpr) : Expr()
        data class KeyValueExpr(val keyValueExpr: go2k.compile.dumppb.KeyValueExpr) : Expr()
        data class ArrayType(val arrayType: go2k.compile.dumppb.ArrayType) : Expr()
        data class StructType(val structType: go2k.compile.dumppb.StructType) : Expr()
        data class FuncType(val funcType: go2k.compile.dumppb.FuncType) : Expr()
        data class InterfaceType(val interfaceType: go2k.compile.dumppb.InterfaceType) : Expr()
        data class MapType(val mapType: go2k.compile.dumppb.MapType) : Expr()
        data class ChanType(val chanType: go2k.compile.dumppb.ChanType) : Expr()
    }

    override operator fun plus(other: Expr_?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Expr_> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Expr_.protoUnmarshalImpl(u)
    }
}

data class BadExpr(
    val from: Int = 0,
    val to: Int = 0,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<BadExpr> {
    override operator fun plus(other: BadExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<BadExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = BadExpr.protoUnmarshalImpl(u)
    }
}

data class Ident(
    val namePos: Int = 0,
    val name: String = "",
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val defTypeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Ident> {
    override operator fun plus(other: Ident?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Ident> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Ident.protoUnmarshalImpl(u)
    }
}

data class Ellipsis(
    val ellipsis: Int = 0,
    val elt: go2k.compile.dumppb.Expr_? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Ellipsis> {
    override operator fun plus(other: Ellipsis?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Ellipsis> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Ellipsis.protoUnmarshalImpl(u)
    }
}

data class BasicLit(
    val valuePos: Int = 0,
    val kind: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val value: String = "",
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<BasicLit> {
    override operator fun plus(other: BasicLit?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<BasicLit> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = BasicLit.protoUnmarshalImpl(u)
    }
}

data class FuncLit(
    val type: go2k.compile.dumppb.FuncType? = null,
    val body: go2k.compile.dumppb.BlockStmt? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<FuncLit> {
    override operator fun plus(other: FuncLit?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<FuncLit> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = FuncLit.protoUnmarshalImpl(u)
    }
}

data class CompositeLit(
    val type: go2k.compile.dumppb.Expr_? = null,
    val lbrace: Int = 0,
    val elts: List<go2k.compile.dumppb.Expr_> = emptyList(),
    val rbrace: Int = 0,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<CompositeLit> {
    override operator fun plus(other: CompositeLit?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<CompositeLit> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = CompositeLit.protoUnmarshalImpl(u)
    }
}

data class ParenExpr(
    val lparen: Int = 0,
    val x: go2k.compile.dumppb.Expr_? = null,
    val rparen: Int = 0,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ParenExpr> {
    override operator fun plus(other: ParenExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ParenExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ParenExpr.protoUnmarshalImpl(u)
    }
}

data class SelectorExpr(
    val x: go2k.compile.dumppb.Expr_? = null,
    val sel: go2k.compile.dumppb.Ident? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<SelectorExpr> {
    override operator fun plus(other: SelectorExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<SelectorExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = SelectorExpr.protoUnmarshalImpl(u)
    }
}

data class IndexExpr(
    val x: go2k.compile.dumppb.Expr_? = null,
    val lbrack: Int = 0,
    val index: go2k.compile.dumppb.Expr_? = null,
    val rbrack: Int = 0,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<IndexExpr> {
    override operator fun plus(other: IndexExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<IndexExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = IndexExpr.protoUnmarshalImpl(u)
    }
}

data class SliceExpr(
    val x: go2k.compile.dumppb.Expr_? = null,
    val lbrack: Int = 0,
    val low: go2k.compile.dumppb.Expr_? = null,
    val high: go2k.compile.dumppb.Expr_? = null,
    val max: go2k.compile.dumppb.Expr_? = null,
    val slice3: Boolean = false,
    val rbrack: Int = 0,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<SliceExpr> {
    override operator fun plus(other: SliceExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<SliceExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = SliceExpr.protoUnmarshalImpl(u)
    }
}

data class TypeAssertExpr(
    val x: go2k.compile.dumppb.Expr_? = null,
    val lparen: Int = 0,
    val type: go2k.compile.dumppb.Expr_? = null,
    val rparen: Int = 0,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeAssertExpr> {
    override operator fun plus(other: TypeAssertExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeAssertExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeAssertExpr.protoUnmarshalImpl(u)
    }
}

data class CallExpr(
    val `fun`: go2k.compile.dumppb.Expr_? = null,
    val lparen: Int = 0,
    val args: List<go2k.compile.dumppb.Expr_> = emptyList(),
    val ellipsis: Int = 0,
    val rparen: Int = 0,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<CallExpr> {
    override operator fun plus(other: CallExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<CallExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = CallExpr.protoUnmarshalImpl(u)
    }
}

data class StarExpr(
    val star: Int = 0,
    val x: go2k.compile.dumppb.Expr_? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<StarExpr> {
    override operator fun plus(other: StarExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<StarExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = StarExpr.protoUnmarshalImpl(u)
    }
}

data class UnaryExpr(
    val opPos: Int = 0,
    val op: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val x: go2k.compile.dumppb.Expr_? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<UnaryExpr> {
    override operator fun plus(other: UnaryExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<UnaryExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = UnaryExpr.protoUnmarshalImpl(u)
    }
}

data class BinaryExpr(
    val x: go2k.compile.dumppb.Expr_? = null,
    val opPos: Int = 0,
    val op: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val y: go2k.compile.dumppb.Expr_? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<BinaryExpr> {
    override operator fun plus(other: BinaryExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<BinaryExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = BinaryExpr.protoUnmarshalImpl(u)
    }
}

data class KeyValueExpr(
    val key: go2k.compile.dumppb.Expr_? = null,
    val colon: Int = 0,
    val value: go2k.compile.dumppb.Expr_? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<KeyValueExpr> {
    override operator fun plus(other: KeyValueExpr?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<KeyValueExpr> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = KeyValueExpr.protoUnmarshalImpl(u)
    }
}

data class ArrayType(
    val lbrack: Int = 0,
    val len: go2k.compile.dumppb.Expr_? = null,
    val elt: go2k.compile.dumppb.Expr_? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ArrayType> {
    override operator fun plus(other: ArrayType?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ArrayType> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ArrayType.protoUnmarshalImpl(u)
    }
}

data class StructType(
    val struct: Int = 0,
    val fields: go2k.compile.dumppb.FieldList? = null,
    val incomplete: Boolean = false,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<StructType> {
    override operator fun plus(other: StructType?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<StructType> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = StructType.protoUnmarshalImpl(u)
    }
}

data class FuncType(
    val func: Int = 0,
    val params: go2k.compile.dumppb.FieldList? = null,
    val results: go2k.compile.dumppb.FieldList? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<FuncType> {
    override operator fun plus(other: FuncType?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<FuncType> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = FuncType.protoUnmarshalImpl(u)
    }
}

data class InterfaceType(
    val `interface`: Int = 0,
    val methods: go2k.compile.dumppb.FieldList? = null,
    val incomplete: Boolean = false,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<InterfaceType> {
    override operator fun plus(other: InterfaceType?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<InterfaceType> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = InterfaceType.protoUnmarshalImpl(u)
    }
}

data class MapType(
    val map: Int = 0,
    val key: go2k.compile.dumppb.Expr_? = null,
    val value: go2k.compile.dumppb.Expr_? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<MapType> {
    override operator fun plus(other: MapType?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<MapType> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = MapType.protoUnmarshalImpl(u)
    }
}

data class ChanType(
    val begin: Int = 0,
    val arrow: Int = 0,
    val sendDir: Boolean = false,
    val recvDir: Boolean = false,
    val value: go2k.compile.dumppb.Expr_? = null,
    val typeRef: go2k.compile.dumppb.TypeRef? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ChanType> {
    override operator fun plus(other: ChanType?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ChanType> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ChanType.protoUnmarshalImpl(u)
    }
}

data class Stmt_(
    val stmt: Stmt? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Stmt_> {
    sealed class Stmt {
        data class BadStmt(val badStmt: go2k.compile.dumppb.BadStmt) : Stmt()
        data class DeclStmt(val declStmt: go2k.compile.dumppb.DeclStmt) : Stmt()
        data class EmptyStmt(val emptyStmt: go2k.compile.dumppb.EmptyStmt) : Stmt()
        data class LabeledStmt(val labeledStmt: go2k.compile.dumppb.LabeledStmt) : Stmt()
        data class ExprStmt(val exprStmt: go2k.compile.dumppb.ExprStmt) : Stmt()
        data class SendStmt(val sendStmt: go2k.compile.dumppb.SendStmt) : Stmt()
        data class IncDecStmt(val incDecStmt: go2k.compile.dumppb.IncDecStmt) : Stmt()
        data class AssignStmt(val assignStmt: go2k.compile.dumppb.AssignStmt) : Stmt()
        data class GoStmt(val goStmt: go2k.compile.dumppb.GoStmt) : Stmt()
        data class DeferStmt(val deferStmt: go2k.compile.dumppb.DeferStmt) : Stmt()
        data class ReturnStmt(val returnStmt: go2k.compile.dumppb.ReturnStmt) : Stmt()
        data class BranchStmt(val branchStmt: go2k.compile.dumppb.BranchStmt) : Stmt()
        data class BlockStmt(val blockStmt: go2k.compile.dumppb.BlockStmt) : Stmt()
        data class IfStmt(val ifStmt: go2k.compile.dumppb.IfStmt) : Stmt()
        data class CaseClause(val caseClause: go2k.compile.dumppb.CaseClause) : Stmt()
        data class SwitchStmt(val switchStmt: go2k.compile.dumppb.SwitchStmt) : Stmt()
        data class TypeSwitchStmt(val typeSwitchStmt: go2k.compile.dumppb.TypeSwitchStmt) : Stmt()
        data class CommClause(val commClause: go2k.compile.dumppb.CommClause) : Stmt()
        data class SelectStmt(val selectStmt: go2k.compile.dumppb.SelectStmt) : Stmt()
        data class ForStmt(val forStmt: go2k.compile.dumppb.ForStmt) : Stmt()
        data class RangeStmt(val rangeStmt: go2k.compile.dumppb.RangeStmt) : Stmt()
    }

    override operator fun plus(other: Stmt_?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Stmt_> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Stmt_.protoUnmarshalImpl(u)
    }
}

data class BadStmt(
    val from: Int = 0,
    val to: Int = 0,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<BadStmt> {
    override operator fun plus(other: BadStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<BadStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = BadStmt.protoUnmarshalImpl(u)
    }
}

data class DeclStmt(
    val decl: go2k.compile.dumppb.Decl_? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<DeclStmt> {
    override operator fun plus(other: DeclStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<DeclStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = DeclStmt.protoUnmarshalImpl(u)
    }
}

data class EmptyStmt(
    val semicolon: Int = 0,
    val implicit: Boolean = false,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<EmptyStmt> {
    override operator fun plus(other: EmptyStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<EmptyStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = EmptyStmt.protoUnmarshalImpl(u)
    }
}

data class LabeledStmt(
    val label: go2k.compile.dumppb.Ident? = null,
    val colon: Int = 0,
    val stmt: go2k.compile.dumppb.Stmt_? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<LabeledStmt> {
    override operator fun plus(other: LabeledStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<LabeledStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = LabeledStmt.protoUnmarshalImpl(u)
    }
}

data class ExprStmt(
    val x: go2k.compile.dumppb.Expr_? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ExprStmt> {
    override operator fun plus(other: ExprStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ExprStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ExprStmt.protoUnmarshalImpl(u)
    }
}

data class SendStmt(
    val chan: go2k.compile.dumppb.Expr_? = null,
    val arrow: Int = 0,
    val value: go2k.compile.dumppb.Expr_? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<SendStmt> {
    override operator fun plus(other: SendStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<SendStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = SendStmt.protoUnmarshalImpl(u)
    }
}

data class IncDecStmt(
    val x: go2k.compile.dumppb.Expr_? = null,
    val tokPos: Int = 0,
    val tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<IncDecStmt> {
    override operator fun plus(other: IncDecStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<IncDecStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = IncDecStmt.protoUnmarshalImpl(u)
    }
}

data class AssignStmt(
    val lhs: List<go2k.compile.dumppb.Expr_> = emptyList(),
    val tokPos: Int = 0,
    val tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val rhs: List<go2k.compile.dumppb.Expr_> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<AssignStmt> {
    override operator fun plus(other: AssignStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<AssignStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = AssignStmt.protoUnmarshalImpl(u)
    }
}

data class GoStmt(
    val go: Int = 0,
    val call: go2k.compile.dumppb.CallExpr? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<GoStmt> {
    override operator fun plus(other: GoStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<GoStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = GoStmt.protoUnmarshalImpl(u)
    }
}

data class DeferStmt(
    val defer: Int = 0,
    val call: go2k.compile.dumppb.CallExpr? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<DeferStmt> {
    override operator fun plus(other: DeferStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<DeferStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = DeferStmt.protoUnmarshalImpl(u)
    }
}

data class ReturnStmt(
    val `return`: Int = 0,
    val results: List<go2k.compile.dumppb.Expr_> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ReturnStmt> {
    override operator fun plus(other: ReturnStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ReturnStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ReturnStmt.protoUnmarshalImpl(u)
    }
}

data class BranchStmt(
    val tokPos: Int = 0,
    val tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val label: go2k.compile.dumppb.Ident? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<BranchStmt> {
    override operator fun plus(other: BranchStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<BranchStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = BranchStmt.protoUnmarshalImpl(u)
    }
}

data class BlockStmt(
    val lbrace: Int = 0,
    val list: List<go2k.compile.dumppb.Stmt_> = emptyList(),
    val rbrace: Int = 0,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<BlockStmt> {
    override operator fun plus(other: BlockStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<BlockStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = BlockStmt.protoUnmarshalImpl(u)
    }
}

data class IfStmt(
    val `if`: Int = 0,
    val init: go2k.compile.dumppb.Stmt_? = null,
    val cond: go2k.compile.dumppb.Expr_? = null,
    val body: go2k.compile.dumppb.BlockStmt? = null,
    val `else`: go2k.compile.dumppb.Stmt_? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<IfStmt> {
    override operator fun plus(other: IfStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<IfStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = IfStmt.protoUnmarshalImpl(u)
    }
}

data class CaseClause(
    val case: Int = 0,
    val list: List<go2k.compile.dumppb.Expr_> = emptyList(),
    val colon: Int = 0,
    val body: List<go2k.compile.dumppb.Stmt_> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<CaseClause> {
    override operator fun plus(other: CaseClause?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<CaseClause> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = CaseClause.protoUnmarshalImpl(u)
    }
}

data class SwitchStmt(
    val switch: Int = 0,
    val init: go2k.compile.dumppb.Stmt_? = null,
    val tag: go2k.compile.dumppb.Expr_? = null,
    val body: go2k.compile.dumppb.BlockStmt? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<SwitchStmt> {
    override operator fun plus(other: SwitchStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<SwitchStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = SwitchStmt.protoUnmarshalImpl(u)
    }
}

data class TypeSwitchStmt(
    val switch: Int = 0,
    val init: go2k.compile.dumppb.Stmt_? = null,
    val assign: go2k.compile.dumppb.Stmt_? = null,
    val body: go2k.compile.dumppb.BlockStmt? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeSwitchStmt> {
    override operator fun plus(other: TypeSwitchStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeSwitchStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeSwitchStmt.protoUnmarshalImpl(u)
    }
}

data class CommClause(
    val case: Int = 0,
    val comm: go2k.compile.dumppb.Stmt_? = null,
    val colon: Int = 0,
    val body: List<go2k.compile.dumppb.Stmt_> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<CommClause> {
    override operator fun plus(other: CommClause?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<CommClause> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = CommClause.protoUnmarshalImpl(u)
    }
}

data class SelectStmt(
    val select: Int = 0,
    val body: go2k.compile.dumppb.BlockStmt? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<SelectStmt> {
    override operator fun plus(other: SelectStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<SelectStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = SelectStmt.protoUnmarshalImpl(u)
    }
}

data class ForStmt(
    val `for`: Int = 0,
    val init: go2k.compile.dumppb.Stmt_? = null,
    val cond: go2k.compile.dumppb.Expr_? = null,
    val post: go2k.compile.dumppb.Stmt_? = null,
    val body: go2k.compile.dumppb.BlockStmt? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ForStmt> {
    override operator fun plus(other: ForStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ForStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ForStmt.protoUnmarshalImpl(u)
    }
}

data class RangeStmt(
    val `for`: Int = 0,
    val key: go2k.compile.dumppb.Expr_? = null,
    val value: go2k.compile.dumppb.Expr_? = null,
    val tokPos: Int = 0,
    val tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val x: go2k.compile.dumppb.Expr_? = null,
    val body: go2k.compile.dumppb.BlockStmt? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<RangeStmt> {
    override operator fun plus(other: RangeStmt?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<RangeStmt> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = RangeStmt.protoUnmarshalImpl(u)
    }
}

data class Spec_(
    val spec: Spec? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Spec_> {
    sealed class Spec {
        data class ImportSpec(val importSpec: go2k.compile.dumppb.ImportSpec) : Spec()
        data class ValueSpec(val valueSpec: go2k.compile.dumppb.ValueSpec) : Spec()
        data class TypeSpec(val typeSpec: go2k.compile.dumppb.TypeSpec) : Spec()
    }

    override operator fun plus(other: Spec_?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Spec_> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Spec_.protoUnmarshalImpl(u)
    }
}

data class ImportSpec(
    val doc: go2k.compile.dumppb.CommentGroup? = null,
    val name: go2k.compile.dumppb.Ident? = null,
    val path: go2k.compile.dumppb.BasicLit? = null,
    val comment: go2k.compile.dumppb.CommentGroup? = null,
    val endPos: Int = 0,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ImportSpec> {
    override operator fun plus(other: ImportSpec?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ImportSpec> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ImportSpec.protoUnmarshalImpl(u)
    }
}

data class ValueSpec(
    val doc: go2k.compile.dumppb.CommentGroup? = null,
    val names: List<go2k.compile.dumppb.Ident> = emptyList(),
    val type: go2k.compile.dumppb.Expr_? = null,
    val values: List<go2k.compile.dumppb.Expr_> = emptyList(),
    val comment: go2k.compile.dumppb.CommentGroup? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ValueSpec> {
    override operator fun plus(other: ValueSpec?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ValueSpec> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ValueSpec.protoUnmarshalImpl(u)
    }
}

data class TypeSpec(
    val doc: go2k.compile.dumppb.CommentGroup? = null,
    val name: go2k.compile.dumppb.Ident? = null,
    val assign: Int = 0,
    val type: go2k.compile.dumppb.Expr_? = null,
    val comment: go2k.compile.dumppb.CommentGroup? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<TypeSpec> {
    override operator fun plus(other: TypeSpec?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<TypeSpec> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = TypeSpec.protoUnmarshalImpl(u)
    }
}

data class Decl_(
    val decl: Decl? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Decl_> {
    sealed class Decl {
        data class BadDecl(val badDecl: go2k.compile.dumppb.BadDecl) : Decl()
        data class GenDecl(val genDecl: go2k.compile.dumppb.GenDecl) : Decl()
        data class FuncDecl(val funcDecl: go2k.compile.dumppb.FuncDecl) : Decl()
    }

    override operator fun plus(other: Decl_?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Decl_> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Decl_.protoUnmarshalImpl(u)
    }
}

data class BadDecl(
    val from: Int = 0,
    val to: Int = 0,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<BadDecl> {
    override operator fun plus(other: BadDecl?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<BadDecl> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = BadDecl.protoUnmarshalImpl(u)
    }
}

data class GenDecl(
    val doc: go2k.compile.dumppb.CommentGroup? = null,
    val tokPos: Int = 0,
    val tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val lparen: Int = 0,
    val specs: List<go2k.compile.dumppb.Spec_> = emptyList(),
    val rparen: Int = 0,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<GenDecl> {
    override operator fun plus(other: GenDecl?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<GenDecl> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = GenDecl.protoUnmarshalImpl(u)
    }
}

data class FuncDecl(
    val doc: go2k.compile.dumppb.CommentGroup? = null,
    val recv: go2k.compile.dumppb.FieldList? = null,
    val name: go2k.compile.dumppb.Ident? = null,
    val type: go2k.compile.dumppb.FuncType? = null,
    val body: go2k.compile.dumppb.BlockStmt? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<FuncDecl> {
    override operator fun plus(other: FuncDecl?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<FuncDecl> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = FuncDecl.protoUnmarshalImpl(u)
    }
}

data class File(
    val fileName: String = "",
    val doc: go2k.compile.dumppb.CommentGroup? = null,
    val `package`: Int = 0,
    val name: go2k.compile.dumppb.Ident? = null,
    val decls: List<go2k.compile.dumppb.Decl_> = emptyList(),
    val imports: List<go2k.compile.dumppb.ImportSpec> = emptyList(),
    val unresolved: List<go2k.compile.dumppb.Ident> = emptyList(),
    val comments: List<go2k.compile.dumppb.CommentGroup> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<File> {
    override operator fun plus(other: File?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<File> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = File.protoUnmarshalImpl(u)
    }
}

data class Package(
    val name: String = "",
    val path: String = "",
    val files: List<go2k.compile.dumppb.File> = emptyList(),
    val types: List<go2k.compile.dumppb.Type_> = emptyList(),
    val varInitOrder: List<String> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Package> {
    override operator fun plus(other: Package?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Package> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Package.protoUnmarshalImpl(u)
    }
}

data class Packages(
    val packages: List<go2k.compile.dumppb.Package> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Packages> {
    override operator fun plus(other: Packages?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Packages> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Packages.protoUnmarshalImpl(u)
    }
}

private fun Comment.protoMergeImpl(plus: Comment?): Comment = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Comment.protoSizeImpl(): Int {
    var protoSize = 0
    if (slash != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(slash)
    if (text.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(text)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Comment.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (slash != 0) protoMarshal.writeTag(8).writeInt32(slash)
    if (text.isNotEmpty()) protoMarshal.writeTag(18).writeString(text)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Comment.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Comment {
    var slash = 0
    var text = ""
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Comment(slash, text, protoUnmarshal.unknownFields())
        8 -> slash = protoUnmarshal.readInt32()
        18 -> text = protoUnmarshal.readString()
        else -> protoUnmarshal.unknownField()
    }
}

private fun CommentGroup.protoMergeImpl(plus: CommentGroup?): CommentGroup = plus?.copy(
    list = list + plus.list,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun CommentGroup.protoSizeImpl(): Int {
    var protoSize = 0
    if (list.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(1) * list.size) + list.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun CommentGroup.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (list.isNotEmpty()) list.forEach { protoMarshal.writeTag(10).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun CommentGroup.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): CommentGroup {
    var list: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Comment>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CommentGroup(pbandk.ListWithSize.Builder.fixed(list), protoUnmarshal.unknownFields())
        10 -> list = protoUnmarshal.readRepeatedMessage(list, go2k.compile.dumppb.Comment.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Field.protoMergeImpl(plus: Field?): Field = plus?.copy(
    doc = doc?.plus(plus.doc) ?: plus.doc,
    names = names + plus.names,
    type = type?.plus(plus.type) ?: plus.type,
    tag = tag?.plus(plus.tag) ?: plus.tag,
    comment = comment?.plus(plus.comment) ?: plus.comment,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Field.protoSizeImpl(): Int {
    var protoSize = 0
    if (doc != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(doc)
    if (names.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * names.size) + names.sumBy(pbandk.Sizer::messageSize)
    if (type != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(type)
    if (tag != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(tag)
    if (comment != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(comment)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Field.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (doc != null) protoMarshal.writeTag(10).writeMessage(doc)
    if (names.isNotEmpty()) names.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (type != null) protoMarshal.writeTag(26).writeMessage(type)
    if (tag != null) protoMarshal.writeTag(34).writeMessage(tag)
    if (comment != null) protoMarshal.writeTag(42).writeMessage(comment)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Field.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Field {
    var doc: go2k.compile.dumppb.CommentGroup? = null
    var names: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Ident>? = null
    var type: go2k.compile.dumppb.Expr_? = null
    var tag: go2k.compile.dumppb.BasicLit? = null
    var comment: go2k.compile.dumppb.CommentGroup? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Field(doc, pbandk.ListWithSize.Builder.fixed(names), type, tag,
            comment, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        18 -> names = protoUnmarshal.readRepeatedMessage(names, go2k.compile.dumppb.Ident.Companion, true)
        26 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> tag = protoUnmarshal.readMessage(go2k.compile.dumppb.BasicLit.Companion)
        42 -> comment = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun FieldList.protoMergeImpl(plus: FieldList?): FieldList = plus?.copy(
    list = list + plus.list,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun FieldList.protoSizeImpl(): Int {
    var protoSize = 0
    if (opening != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(opening)
    if (list.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * list.size) + list.sumBy(pbandk.Sizer::messageSize)
    if (closing != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int32Size(closing)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun FieldList.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (opening != 0) protoMarshal.writeTag(8).writeInt32(opening)
    if (list.isNotEmpty()) list.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (closing != 0) protoMarshal.writeTag(24).writeInt32(closing)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun FieldList.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): FieldList {
    var opening = 0
    var list: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Field>? = null
    var closing = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return FieldList(opening, pbandk.ListWithSize.Builder.fixed(list), closing, protoUnmarshal.unknownFields())
        8 -> opening = protoUnmarshal.readInt32()
        18 -> list = protoUnmarshal.readRepeatedMessage(list, go2k.compile.dumppb.Field.Companion, true)
        24 -> closing = protoUnmarshal.readInt32()
        else -> protoUnmarshal.unknownField()
    }
}

private fun Expr_.protoMergeImpl(plus: Expr_?): Expr_ = plus?.copy(
    expr = when {
        expr is Expr_.Expr.BadExpr && plus.expr is Expr_.Expr.BadExpr ->
            Expr_.Expr.BadExpr(expr.badExpr + plus.expr.badExpr)
        expr is Expr_.Expr.Ident && plus.expr is Expr_.Expr.Ident ->
            Expr_.Expr.Ident(expr.ident + plus.expr.ident)
        expr is Expr_.Expr.Ellipsis && plus.expr is Expr_.Expr.Ellipsis ->
            Expr_.Expr.Ellipsis(expr.ellipsis + plus.expr.ellipsis)
        expr is Expr_.Expr.BasicLit && plus.expr is Expr_.Expr.BasicLit ->
            Expr_.Expr.BasicLit(expr.basicLit + plus.expr.basicLit)
        expr is Expr_.Expr.FuncLit && plus.expr is Expr_.Expr.FuncLit ->
            Expr_.Expr.FuncLit(expr.funcLit + plus.expr.funcLit)
        expr is Expr_.Expr.CompositeLit && plus.expr is Expr_.Expr.CompositeLit ->
            Expr_.Expr.CompositeLit(expr.compositeLit + plus.expr.compositeLit)
        expr is Expr_.Expr.ParenExpr && plus.expr is Expr_.Expr.ParenExpr ->
            Expr_.Expr.ParenExpr(expr.parenExpr + plus.expr.parenExpr)
        expr is Expr_.Expr.SelectorExpr && plus.expr is Expr_.Expr.SelectorExpr ->
            Expr_.Expr.SelectorExpr(expr.selectorExpr + plus.expr.selectorExpr)
        expr is Expr_.Expr.IndexExpr && plus.expr is Expr_.Expr.IndexExpr ->
            Expr_.Expr.IndexExpr(expr.indexExpr + plus.expr.indexExpr)
        expr is Expr_.Expr.SliceExpr && plus.expr is Expr_.Expr.SliceExpr ->
            Expr_.Expr.SliceExpr(expr.sliceExpr + plus.expr.sliceExpr)
        expr is Expr_.Expr.TypeAssertExpr && plus.expr is Expr_.Expr.TypeAssertExpr ->
            Expr_.Expr.TypeAssertExpr(expr.typeAssertExpr + plus.expr.typeAssertExpr)
        expr is Expr_.Expr.CallExpr && plus.expr is Expr_.Expr.CallExpr ->
            Expr_.Expr.CallExpr(expr.callExpr + plus.expr.callExpr)
        expr is Expr_.Expr.StarExpr && plus.expr is Expr_.Expr.StarExpr ->
            Expr_.Expr.StarExpr(expr.starExpr + plus.expr.starExpr)
        expr is Expr_.Expr.UnaryExpr && plus.expr is Expr_.Expr.UnaryExpr ->
            Expr_.Expr.UnaryExpr(expr.unaryExpr + plus.expr.unaryExpr)
        expr is Expr_.Expr.BinaryExpr && plus.expr is Expr_.Expr.BinaryExpr ->
            Expr_.Expr.BinaryExpr(expr.binaryExpr + plus.expr.binaryExpr)
        expr is Expr_.Expr.KeyValueExpr && plus.expr is Expr_.Expr.KeyValueExpr ->
            Expr_.Expr.KeyValueExpr(expr.keyValueExpr + plus.expr.keyValueExpr)
        expr is Expr_.Expr.ArrayType && plus.expr is Expr_.Expr.ArrayType ->
            Expr_.Expr.ArrayType(expr.arrayType + plus.expr.arrayType)
        expr is Expr_.Expr.StructType && plus.expr is Expr_.Expr.StructType ->
            Expr_.Expr.StructType(expr.structType + plus.expr.structType)
        expr is Expr_.Expr.FuncType && plus.expr is Expr_.Expr.FuncType ->
            Expr_.Expr.FuncType(expr.funcType + plus.expr.funcType)
        expr is Expr_.Expr.InterfaceType && plus.expr is Expr_.Expr.InterfaceType ->
            Expr_.Expr.InterfaceType(expr.interfaceType + plus.expr.interfaceType)
        expr is Expr_.Expr.MapType && plus.expr is Expr_.Expr.MapType ->
            Expr_.Expr.MapType(expr.mapType + plus.expr.mapType)
        expr is Expr_.Expr.ChanType && plus.expr is Expr_.Expr.ChanType ->
            Expr_.Expr.ChanType(expr.chanType + plus.expr.chanType)
        else ->
            plus.expr ?: expr
    },
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Expr_.protoSizeImpl(): Int {
    var protoSize = 0
    when (expr) {
        is Expr_.Expr.BadExpr -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(expr.badExpr)
        is Expr_.Expr.Ident -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(expr.ident)
        is Expr_.Expr.Ellipsis -> protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(expr.ellipsis)
        is Expr_.Expr.BasicLit -> protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(expr.basicLit)
        is Expr_.Expr.FuncLit -> protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(expr.funcLit)
        is Expr_.Expr.CompositeLit -> protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.messageSize(expr.compositeLit)
        is Expr_.Expr.ParenExpr -> protoSize += pbandk.Sizer.tagSize(7) + pbandk.Sizer.messageSize(expr.parenExpr)
        is Expr_.Expr.SelectorExpr -> protoSize += pbandk.Sizer.tagSize(8) + pbandk.Sizer.messageSize(expr.selectorExpr)
        is Expr_.Expr.IndexExpr -> protoSize += pbandk.Sizer.tagSize(9) + pbandk.Sizer.messageSize(expr.indexExpr)
        is Expr_.Expr.SliceExpr -> protoSize += pbandk.Sizer.tagSize(10) + pbandk.Sizer.messageSize(expr.sliceExpr)
        is Expr_.Expr.TypeAssertExpr -> protoSize += pbandk.Sizer.tagSize(11) + pbandk.Sizer.messageSize(expr.typeAssertExpr)
        is Expr_.Expr.CallExpr -> protoSize += pbandk.Sizer.tagSize(12) + pbandk.Sizer.messageSize(expr.callExpr)
        is Expr_.Expr.StarExpr -> protoSize += pbandk.Sizer.tagSize(13) + pbandk.Sizer.messageSize(expr.starExpr)
        is Expr_.Expr.UnaryExpr -> protoSize += pbandk.Sizer.tagSize(14) + pbandk.Sizer.messageSize(expr.unaryExpr)
        is Expr_.Expr.BinaryExpr -> protoSize += pbandk.Sizer.tagSize(15) + pbandk.Sizer.messageSize(expr.binaryExpr)
        is Expr_.Expr.KeyValueExpr -> protoSize += pbandk.Sizer.tagSize(16) + pbandk.Sizer.messageSize(expr.keyValueExpr)
        is Expr_.Expr.ArrayType -> protoSize += pbandk.Sizer.tagSize(17) + pbandk.Sizer.messageSize(expr.arrayType)
        is Expr_.Expr.StructType -> protoSize += pbandk.Sizer.tagSize(18) + pbandk.Sizer.messageSize(expr.structType)
        is Expr_.Expr.FuncType -> protoSize += pbandk.Sizer.tagSize(19) + pbandk.Sizer.messageSize(expr.funcType)
        is Expr_.Expr.InterfaceType -> protoSize += pbandk.Sizer.tagSize(20) + pbandk.Sizer.messageSize(expr.interfaceType)
        is Expr_.Expr.MapType -> protoSize += pbandk.Sizer.tagSize(21) + pbandk.Sizer.messageSize(expr.mapType)
        is Expr_.Expr.ChanType -> protoSize += pbandk.Sizer.tagSize(22) + pbandk.Sizer.messageSize(expr.chanType)
    }
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Expr_.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (expr is Expr_.Expr.BadExpr) protoMarshal.writeTag(10).writeMessage(expr.badExpr)
    if (expr is Expr_.Expr.Ident) protoMarshal.writeTag(18).writeMessage(expr.ident)
    if (expr is Expr_.Expr.Ellipsis) protoMarshal.writeTag(26).writeMessage(expr.ellipsis)
    if (expr is Expr_.Expr.BasicLit) protoMarshal.writeTag(34).writeMessage(expr.basicLit)
    if (expr is Expr_.Expr.FuncLit) protoMarshal.writeTag(42).writeMessage(expr.funcLit)
    if (expr is Expr_.Expr.CompositeLit) protoMarshal.writeTag(50).writeMessage(expr.compositeLit)
    if (expr is Expr_.Expr.ParenExpr) protoMarshal.writeTag(58).writeMessage(expr.parenExpr)
    if (expr is Expr_.Expr.SelectorExpr) protoMarshal.writeTag(66).writeMessage(expr.selectorExpr)
    if (expr is Expr_.Expr.IndexExpr) protoMarshal.writeTag(74).writeMessage(expr.indexExpr)
    if (expr is Expr_.Expr.SliceExpr) protoMarshal.writeTag(82).writeMessage(expr.sliceExpr)
    if (expr is Expr_.Expr.TypeAssertExpr) protoMarshal.writeTag(90).writeMessage(expr.typeAssertExpr)
    if (expr is Expr_.Expr.CallExpr) protoMarshal.writeTag(98).writeMessage(expr.callExpr)
    if (expr is Expr_.Expr.StarExpr) protoMarshal.writeTag(106).writeMessage(expr.starExpr)
    if (expr is Expr_.Expr.UnaryExpr) protoMarshal.writeTag(114).writeMessage(expr.unaryExpr)
    if (expr is Expr_.Expr.BinaryExpr) protoMarshal.writeTag(122).writeMessage(expr.binaryExpr)
    if (expr is Expr_.Expr.KeyValueExpr) protoMarshal.writeTag(130).writeMessage(expr.keyValueExpr)
    if (expr is Expr_.Expr.ArrayType) protoMarshal.writeTag(138).writeMessage(expr.arrayType)
    if (expr is Expr_.Expr.StructType) protoMarshal.writeTag(146).writeMessage(expr.structType)
    if (expr is Expr_.Expr.FuncType) protoMarshal.writeTag(154).writeMessage(expr.funcType)
    if (expr is Expr_.Expr.InterfaceType) protoMarshal.writeTag(162).writeMessage(expr.interfaceType)
    if (expr is Expr_.Expr.MapType) protoMarshal.writeTag(170).writeMessage(expr.mapType)
    if (expr is Expr_.Expr.ChanType) protoMarshal.writeTag(178).writeMessage(expr.chanType)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Expr_.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Expr_ {
    var expr: Expr_.Expr? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Expr_(expr, protoUnmarshal.unknownFields())
        10 -> expr = Expr_.Expr.BadExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.BadExpr.Companion))
        18 -> expr = Expr_.Expr.Ident(protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion))
        26 -> expr = Expr_.Expr.Ellipsis(protoUnmarshal.readMessage(go2k.compile.dumppb.Ellipsis.Companion))
        34 -> expr = Expr_.Expr.BasicLit(protoUnmarshal.readMessage(go2k.compile.dumppb.BasicLit.Companion))
        42 -> expr = Expr_.Expr.FuncLit(protoUnmarshal.readMessage(go2k.compile.dumppb.FuncLit.Companion))
        50 -> expr = Expr_.Expr.CompositeLit(protoUnmarshal.readMessage(go2k.compile.dumppb.CompositeLit.Companion))
        58 -> expr = Expr_.Expr.ParenExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.ParenExpr.Companion))
        66 -> expr = Expr_.Expr.SelectorExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.SelectorExpr.Companion))
        74 -> expr = Expr_.Expr.IndexExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.IndexExpr.Companion))
        82 -> expr = Expr_.Expr.SliceExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.SliceExpr.Companion))
        90 -> expr = Expr_.Expr.TypeAssertExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeAssertExpr.Companion))
        98 -> expr = Expr_.Expr.CallExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.CallExpr.Companion))
        106 -> expr = Expr_.Expr.StarExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.StarExpr.Companion))
        114 -> expr = Expr_.Expr.UnaryExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.UnaryExpr.Companion))
        122 -> expr = Expr_.Expr.BinaryExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.BinaryExpr.Companion))
        130 -> expr = Expr_.Expr.KeyValueExpr(protoUnmarshal.readMessage(go2k.compile.dumppb.KeyValueExpr.Companion))
        138 -> expr = Expr_.Expr.ArrayType(protoUnmarshal.readMessage(go2k.compile.dumppb.ArrayType.Companion))
        146 -> expr = Expr_.Expr.StructType(protoUnmarshal.readMessage(go2k.compile.dumppb.StructType.Companion))
        154 -> expr = Expr_.Expr.FuncType(protoUnmarshal.readMessage(go2k.compile.dumppb.FuncType.Companion))
        162 -> expr = Expr_.Expr.InterfaceType(protoUnmarshal.readMessage(go2k.compile.dumppb.InterfaceType.Companion))
        170 -> expr = Expr_.Expr.MapType(protoUnmarshal.readMessage(go2k.compile.dumppb.MapType.Companion))
        178 -> expr = Expr_.Expr.ChanType(protoUnmarshal.readMessage(go2k.compile.dumppb.ChanType.Companion))
        else -> protoUnmarshal.unknownField()
    }
}

private fun BadExpr.protoMergeImpl(plus: BadExpr?): BadExpr = plus?.copy(
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BadExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (from != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(from)
    if (to != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(to)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BadExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (from != 0) protoMarshal.writeTag(8).writeInt32(from)
    if (to != 0) protoMarshal.writeTag(16).writeInt32(to)
    if (typeRef != null) protoMarshal.writeTag(26).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BadExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BadExpr {
    var from = 0
    var to = 0
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BadExpr(from, to, typeRef, protoUnmarshal.unknownFields())
        8 -> from = protoUnmarshal.readInt32()
        16 -> to = protoUnmarshal.readInt32()
        26 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Ident.protoMergeImpl(plus: Ident?): Ident = plus?.copy(
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    defTypeRef = defTypeRef?.plus(plus.defTypeRef) ?: plus.defTypeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Ident.protoSizeImpl(): Int {
    var protoSize = 0
    if (namePos != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(namePos)
    if (name.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(name)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeRef)
    if (defTypeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(defTypeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Ident.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (namePos != 0) protoMarshal.writeTag(8).writeInt32(namePos)
    if (name.isNotEmpty()) protoMarshal.writeTag(18).writeString(name)
    if (typeRef != null) protoMarshal.writeTag(26).writeMessage(typeRef)
    if (defTypeRef != null) protoMarshal.writeTag(34).writeMessage(defTypeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Ident.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Ident {
    var namePos = 0
    var name = ""
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    var defTypeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Ident(namePos, name, typeRef, defTypeRef, protoUnmarshal.unknownFields())
        8 -> namePos = protoUnmarshal.readInt32()
        18 -> name = protoUnmarshal.readString()
        26 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        34 -> defTypeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Ellipsis.protoMergeImpl(plus: Ellipsis?): Ellipsis = plus?.copy(
    elt = elt?.plus(plus.elt) ?: plus.elt,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Ellipsis.protoSizeImpl(): Int {
    var protoSize = 0
    if (ellipsis != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(ellipsis)
    if (elt != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(elt)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Ellipsis.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (ellipsis != 0) protoMarshal.writeTag(8).writeInt32(ellipsis)
    if (elt != null) protoMarshal.writeTag(18).writeMessage(elt)
    if (typeRef != null) protoMarshal.writeTag(26).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Ellipsis.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Ellipsis {
    var ellipsis = 0
    var elt: go2k.compile.dumppb.Expr_? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Ellipsis(ellipsis, elt, typeRef, protoUnmarshal.unknownFields())
        8 -> ellipsis = protoUnmarshal.readInt32()
        18 -> elt = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        26 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun BasicLit.protoMergeImpl(plus: BasicLit?): BasicLit = plus?.copy(
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BasicLit.protoSizeImpl(): Int {
    var protoSize = 0
    if (valuePos != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(valuePos)
    if (kind.value != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(kind)
    if (value.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(value)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BasicLit.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (valuePos != 0) protoMarshal.writeTag(8).writeInt32(valuePos)
    if (kind.value != 0) protoMarshal.writeTag(16).writeEnum(kind)
    if (value.isNotEmpty()) protoMarshal.writeTag(26).writeString(value)
    if (typeRef != null) protoMarshal.writeTag(34).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BasicLit.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BasicLit {
    var valuePos = 0
    var kind: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var value = ""
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BasicLit(valuePos, kind, value, typeRef, protoUnmarshal.unknownFields())
        8 -> valuePos = protoUnmarshal.readInt32()
        16 -> kind = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        26 -> value = protoUnmarshal.readString()
        34 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun FuncLit.protoMergeImpl(plus: FuncLit?): FuncLit = plus?.copy(
    type = type?.plus(plus.type) ?: plus.type,
    body = body?.plus(plus.body) ?: plus.body,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun FuncLit.protoSizeImpl(): Int {
    var protoSize = 0
    if (type != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(type)
    if (body != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(body)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun FuncLit.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (type != null) protoMarshal.writeTag(10).writeMessage(type)
    if (body != null) protoMarshal.writeTag(18).writeMessage(body)
    if (typeRef != null) protoMarshal.writeTag(26).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun FuncLit.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): FuncLit {
    var type: go2k.compile.dumppb.FuncType? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return FuncLit(type, body, typeRef, protoUnmarshal.unknownFields())
        10 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.FuncType.Companion)
        18 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        26 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun CompositeLit.protoMergeImpl(plus: CompositeLit?): CompositeLit = plus?.copy(
    type = type?.plus(plus.type) ?: plus.type,
    elts = elts + plus.elts,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun CompositeLit.protoSizeImpl(): Int {
    var protoSize = 0
    if (type != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(type)
    if (lbrace != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(lbrace)
    if (elts.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(3) * elts.size) + elts.sumBy(pbandk.Sizer::messageSize)
    if (rbrace != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(rbrace)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun CompositeLit.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (type != null) protoMarshal.writeTag(10).writeMessage(type)
    if (lbrace != 0) protoMarshal.writeTag(16).writeInt32(lbrace)
    if (elts.isNotEmpty()) elts.forEach { protoMarshal.writeTag(26).writeMessage(it) }
    if (rbrace != 0) protoMarshal.writeTag(32).writeInt32(rbrace)
    if (typeRef != null) protoMarshal.writeTag(42).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun CompositeLit.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): CompositeLit {
    var type: go2k.compile.dumppb.Expr_? = null
    var lbrace = 0
    var elts: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Expr_>? = null
    var rbrace = 0
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CompositeLit(type, lbrace, pbandk.ListWithSize.Builder.fixed(elts), rbrace,
            typeRef, protoUnmarshal.unknownFields())
        10 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        16 -> lbrace = protoUnmarshal.readInt32()
        26 -> elts = protoUnmarshal.readRepeatedMessage(elts, go2k.compile.dumppb.Expr_.Companion, true)
        32 -> rbrace = protoUnmarshal.readInt32()
        42 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun ParenExpr.protoMergeImpl(plus: ParenExpr?): ParenExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ParenExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (lparen != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(lparen)
    if (x != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(x)
    if (rparen != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int32Size(rparen)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ParenExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (lparen != 0) protoMarshal.writeTag(8).writeInt32(lparen)
    if (x != null) protoMarshal.writeTag(18).writeMessage(x)
    if (rparen != 0) protoMarshal.writeTag(24).writeInt32(rparen)
    if (typeRef != null) protoMarshal.writeTag(34).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ParenExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ParenExpr {
    var lparen = 0
    var x: go2k.compile.dumppb.Expr_? = null
    var rparen = 0
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ParenExpr(lparen, x, rparen, typeRef, protoUnmarshal.unknownFields())
        8 -> lparen = protoUnmarshal.readInt32()
        18 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        24 -> rparen = protoUnmarshal.readInt32()
        34 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun SelectorExpr.protoMergeImpl(plus: SelectorExpr?): SelectorExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    sel = sel?.plus(plus.sel) ?: plus.sel,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun SelectorExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (sel != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(sel)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun SelectorExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (sel != null) protoMarshal.writeTag(18).writeMessage(sel)
    if (typeRef != null) protoMarshal.writeTag(26).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun SelectorExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): SelectorExpr {
    var x: go2k.compile.dumppb.Expr_? = null
    var sel: go2k.compile.dumppb.Ident? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return SelectorExpr(x, sel, typeRef, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        18 -> sel = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        26 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun IndexExpr.protoMergeImpl(plus: IndexExpr?): IndexExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    index = index?.plus(plus.index) ?: plus.index,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun IndexExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (lbrack != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(lbrack)
    if (index != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(index)
    if (rbrack != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(rbrack)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun IndexExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (lbrack != 0) protoMarshal.writeTag(16).writeInt32(lbrack)
    if (index != null) protoMarshal.writeTag(26).writeMessage(index)
    if (rbrack != 0) protoMarshal.writeTag(32).writeInt32(rbrack)
    if (typeRef != null) protoMarshal.writeTag(42).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun IndexExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): IndexExpr {
    var x: go2k.compile.dumppb.Expr_? = null
    var lbrack = 0
    var index: go2k.compile.dumppb.Expr_? = null
    var rbrack = 0
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return IndexExpr(x, lbrack, index, rbrack,
            typeRef, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        16 -> lbrack = protoUnmarshal.readInt32()
        26 -> index = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        32 -> rbrack = protoUnmarshal.readInt32()
        42 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun SliceExpr.protoMergeImpl(plus: SliceExpr?): SliceExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    low = low?.plus(plus.low) ?: plus.low,
    high = high?.plus(plus.high) ?: plus.high,
    max = max?.plus(plus.max) ?: plus.max,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun SliceExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (lbrack != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(lbrack)
    if (low != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(low)
    if (high != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(high)
    if (max != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(max)
    if (slice3) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.boolSize(slice3)
    if (rbrack != 0) protoSize += pbandk.Sizer.tagSize(7) + pbandk.Sizer.int32Size(rbrack)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(8) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun SliceExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (lbrack != 0) protoMarshal.writeTag(16).writeInt32(lbrack)
    if (low != null) protoMarshal.writeTag(26).writeMessage(low)
    if (high != null) protoMarshal.writeTag(34).writeMessage(high)
    if (max != null) protoMarshal.writeTag(42).writeMessage(max)
    if (slice3) protoMarshal.writeTag(48).writeBool(slice3)
    if (rbrack != 0) protoMarshal.writeTag(56).writeInt32(rbrack)
    if (typeRef != null) protoMarshal.writeTag(66).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun SliceExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): SliceExpr {
    var x: go2k.compile.dumppb.Expr_? = null
    var lbrack = 0
    var low: go2k.compile.dumppb.Expr_? = null
    var high: go2k.compile.dumppb.Expr_? = null
    var max: go2k.compile.dumppb.Expr_? = null
    var slice3 = false
    var rbrack = 0
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return SliceExpr(x, lbrack, low, high,
            max, slice3, rbrack, typeRef, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        16 -> lbrack = protoUnmarshal.readInt32()
        26 -> low = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> high = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        42 -> max = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        48 -> slice3 = protoUnmarshal.readBool()
        56 -> rbrack = protoUnmarshal.readInt32()
        66 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeAssertExpr.protoMergeImpl(plus: TypeAssertExpr?): TypeAssertExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    type = type?.plus(plus.type) ?: plus.type,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeAssertExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (lparen != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(lparen)
    if (type != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(type)
    if (rparen != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(rparen)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeAssertExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (lparen != 0) protoMarshal.writeTag(16).writeInt32(lparen)
    if (type != null) protoMarshal.writeTag(26).writeMessage(type)
    if (rparen != 0) protoMarshal.writeTag(32).writeInt32(rparen)
    if (typeRef != null) protoMarshal.writeTag(42).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeAssertExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeAssertExpr {
    var x: go2k.compile.dumppb.Expr_? = null
    var lparen = 0
    var type: go2k.compile.dumppb.Expr_? = null
    var rparen = 0
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeAssertExpr(x, lparen, type, rparen,
            typeRef, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        16 -> lparen = protoUnmarshal.readInt32()
        26 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        32 -> rparen = protoUnmarshal.readInt32()
        42 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun CallExpr.protoMergeImpl(plus: CallExpr?): CallExpr = plus?.copy(
    `fun` = `fun`?.plus(plus.`fun`) ?: plus.`fun`,
    args = args + plus.args,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun CallExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (`fun` != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(`fun`)
    if (lparen != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(lparen)
    if (args.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(3) * args.size) + args.sumBy(pbandk.Sizer::messageSize)
    if (ellipsis != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(ellipsis)
    if (rparen != 0) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.int32Size(rparen)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun CallExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (`fun` != null) protoMarshal.writeTag(10).writeMessage(`fun`)
    if (lparen != 0) protoMarshal.writeTag(16).writeInt32(lparen)
    if (args.isNotEmpty()) args.forEach { protoMarshal.writeTag(26).writeMessage(it) }
    if (ellipsis != 0) protoMarshal.writeTag(32).writeInt32(ellipsis)
    if (rparen != 0) protoMarshal.writeTag(40).writeInt32(rparen)
    if (typeRef != null) protoMarshal.writeTag(50).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun CallExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): CallExpr {
    var `fun`: go2k.compile.dumppb.Expr_? = null
    var lparen = 0
    var args: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Expr_>? = null
    var ellipsis = 0
    var rparen = 0
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CallExpr(`fun`, lparen, pbandk.ListWithSize.Builder.fixed(args), ellipsis,
            rparen, typeRef, protoUnmarshal.unknownFields())
        10 -> `fun` = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        16 -> lparen = protoUnmarshal.readInt32()
        26 -> args = protoUnmarshal.readRepeatedMessage(args, go2k.compile.dumppb.Expr_.Companion, true)
        32 -> ellipsis = protoUnmarshal.readInt32()
        40 -> rparen = protoUnmarshal.readInt32()
        50 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun StarExpr.protoMergeImpl(plus: StarExpr?): StarExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun StarExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (star != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(star)
    if (x != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(x)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun StarExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (star != 0) protoMarshal.writeTag(8).writeInt32(star)
    if (x != null) protoMarshal.writeTag(18).writeMessage(x)
    if (typeRef != null) protoMarshal.writeTag(26).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun StarExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): StarExpr {
    var star = 0
    var x: go2k.compile.dumppb.Expr_? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return StarExpr(star, x, typeRef, protoUnmarshal.unknownFields())
        8 -> star = protoUnmarshal.readInt32()
        18 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        26 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun UnaryExpr.protoMergeImpl(plus: UnaryExpr?): UnaryExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun UnaryExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (opPos != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(opPos)
    if (op.value != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(op)
    if (x != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(x)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun UnaryExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (opPos != 0) protoMarshal.writeTag(8).writeInt32(opPos)
    if (op.value != 0) protoMarshal.writeTag(16).writeEnum(op)
    if (x != null) protoMarshal.writeTag(26).writeMessage(x)
    if (typeRef != null) protoMarshal.writeTag(34).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun UnaryExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): UnaryExpr {
    var opPos = 0
    var op: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var x: go2k.compile.dumppb.Expr_? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return UnaryExpr(opPos, op, x, typeRef, protoUnmarshal.unknownFields())
        8 -> opPos = protoUnmarshal.readInt32()
        16 -> op = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        26 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun BinaryExpr.protoMergeImpl(plus: BinaryExpr?): BinaryExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    y = y?.plus(plus.y) ?: plus.y,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BinaryExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (opPos != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(opPos)
    if (op.value != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.enumSize(op)
    if (y != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(y)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BinaryExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (opPos != 0) protoMarshal.writeTag(16).writeInt32(opPos)
    if (op.value != 0) protoMarshal.writeTag(24).writeEnum(op)
    if (y != null) protoMarshal.writeTag(34).writeMessage(y)
    if (typeRef != null) protoMarshal.writeTag(42).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BinaryExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BinaryExpr {
    var x: go2k.compile.dumppb.Expr_? = null
    var opPos = 0
    var op: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var y: go2k.compile.dumppb.Expr_? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BinaryExpr(x, opPos, op, y,
            typeRef, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        16 -> opPos = protoUnmarshal.readInt32()
        24 -> op = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        34 -> y = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        42 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun KeyValueExpr.protoMergeImpl(plus: KeyValueExpr?): KeyValueExpr = plus?.copy(
    key = key?.plus(plus.key) ?: plus.key,
    value = value?.plus(plus.value) ?: plus.value,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun KeyValueExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (key != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(key)
    if (colon != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(colon)
    if (value != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(value)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun KeyValueExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (key != null) protoMarshal.writeTag(10).writeMessage(key)
    if (colon != 0) protoMarshal.writeTag(16).writeInt32(colon)
    if (value != null) protoMarshal.writeTag(26).writeMessage(value)
    if (typeRef != null) protoMarshal.writeTag(34).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun KeyValueExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): KeyValueExpr {
    var key: go2k.compile.dumppb.Expr_? = null
    var colon = 0
    var value: go2k.compile.dumppb.Expr_? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return KeyValueExpr(key, colon, value, typeRef, protoUnmarshal.unknownFields())
        10 -> key = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        16 -> colon = protoUnmarshal.readInt32()
        26 -> value = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun ArrayType.protoMergeImpl(plus: ArrayType?): ArrayType = plus?.copy(
    len = len?.plus(plus.len) ?: plus.len,
    elt = elt?.plus(plus.elt) ?: plus.elt,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ArrayType.protoSizeImpl(): Int {
    var protoSize = 0
    if (lbrack != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(lbrack)
    if (len != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(len)
    if (elt != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(elt)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ArrayType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (lbrack != 0) protoMarshal.writeTag(8).writeInt32(lbrack)
    if (len != null) protoMarshal.writeTag(18).writeMessage(len)
    if (elt != null) protoMarshal.writeTag(26).writeMessage(elt)
    if (typeRef != null) protoMarshal.writeTag(34).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ArrayType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ArrayType {
    var lbrack = 0
    var len: go2k.compile.dumppb.Expr_? = null
    var elt: go2k.compile.dumppb.Expr_? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ArrayType(lbrack, len, elt, typeRef, protoUnmarshal.unknownFields())
        8 -> lbrack = protoUnmarshal.readInt32()
        18 -> len = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        26 -> elt = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun StructType.protoMergeImpl(plus: StructType?): StructType = plus?.copy(
    fields = fields?.plus(plus.fields) ?: plus.fields,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun StructType.protoSizeImpl(): Int {
    var protoSize = 0
    if (struct != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(struct)
    if (fields != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(fields)
    if (incomplete) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.boolSize(incomplete)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun StructType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (struct != 0) protoMarshal.writeTag(8).writeInt32(struct)
    if (fields != null) protoMarshal.writeTag(18).writeMessage(fields)
    if (incomplete) protoMarshal.writeTag(24).writeBool(incomplete)
    if (typeRef != null) protoMarshal.writeTag(34).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun StructType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): StructType {
    var struct = 0
    var fields: go2k.compile.dumppb.FieldList? = null
    var incomplete = false
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return StructType(struct, fields, incomplete, typeRef, protoUnmarshal.unknownFields())
        8 -> struct = protoUnmarshal.readInt32()
        18 -> fields = protoUnmarshal.readMessage(go2k.compile.dumppb.FieldList.Companion)
        24 -> incomplete = protoUnmarshal.readBool()
        34 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun FuncType.protoMergeImpl(plus: FuncType?): FuncType = plus?.copy(
    params = params?.plus(plus.params) ?: plus.params,
    results = results?.plus(plus.results) ?: plus.results,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun FuncType.protoSizeImpl(): Int {
    var protoSize = 0
    if (func != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(func)
    if (params != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(params)
    if (results != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(results)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun FuncType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (func != 0) protoMarshal.writeTag(8).writeInt32(func)
    if (params != null) protoMarshal.writeTag(18).writeMessage(params)
    if (results != null) protoMarshal.writeTag(26).writeMessage(results)
    if (typeRef != null) protoMarshal.writeTag(34).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun FuncType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): FuncType {
    var func = 0
    var params: go2k.compile.dumppb.FieldList? = null
    var results: go2k.compile.dumppb.FieldList? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return FuncType(func, params, results, typeRef, protoUnmarshal.unknownFields())
        8 -> func = protoUnmarshal.readInt32()
        18 -> params = protoUnmarshal.readMessage(go2k.compile.dumppb.FieldList.Companion)
        26 -> results = protoUnmarshal.readMessage(go2k.compile.dumppb.FieldList.Companion)
        34 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun InterfaceType.protoMergeImpl(plus: InterfaceType?): InterfaceType = plus?.copy(
    methods = methods?.plus(plus.methods) ?: plus.methods,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun InterfaceType.protoSizeImpl(): Int {
    var protoSize = 0
    if (`interface` != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(`interface`)
    if (methods != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(methods)
    if (incomplete) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.boolSize(incomplete)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun InterfaceType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (`interface` != 0) protoMarshal.writeTag(8).writeInt32(`interface`)
    if (methods != null) protoMarshal.writeTag(18).writeMessage(methods)
    if (incomplete) protoMarshal.writeTag(24).writeBool(incomplete)
    if (typeRef != null) protoMarshal.writeTag(34).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun InterfaceType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): InterfaceType {
    var `interface` = 0
    var methods: go2k.compile.dumppb.FieldList? = null
    var incomplete = false
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return InterfaceType(`interface`, methods, incomplete, typeRef, protoUnmarshal.unknownFields())
        8 -> `interface` = protoUnmarshal.readInt32()
        18 -> methods = protoUnmarshal.readMessage(go2k.compile.dumppb.FieldList.Companion)
        24 -> incomplete = protoUnmarshal.readBool()
        34 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun MapType.protoMergeImpl(plus: MapType?): MapType = plus?.copy(
    key = key?.plus(plus.key) ?: plus.key,
    value = value?.plus(plus.value) ?: plus.value,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun MapType.protoSizeImpl(): Int {
    var protoSize = 0
    if (map != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(map)
    if (key != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(key)
    if (value != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(value)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun MapType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (map != 0) protoMarshal.writeTag(8).writeInt32(map)
    if (key != null) protoMarshal.writeTag(18).writeMessage(key)
    if (value != null) protoMarshal.writeTag(26).writeMessage(value)
    if (typeRef != null) protoMarshal.writeTag(34).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun MapType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): MapType {
    var map = 0
    var key: go2k.compile.dumppb.Expr_? = null
    var value: go2k.compile.dumppb.Expr_? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return MapType(map, key, value, typeRef, protoUnmarshal.unknownFields())
        8 -> map = protoUnmarshal.readInt32()
        18 -> key = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        26 -> value = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun ChanType.protoMergeImpl(plus: ChanType?): ChanType = plus?.copy(
    value = value?.plus(plus.value) ?: plus.value,
    typeRef = typeRef?.plus(plus.typeRef) ?: plus.typeRef,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ChanType.protoSizeImpl(): Int {
    var protoSize = 0
    if (begin != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(begin)
    if (arrow != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(arrow)
    if (sendDir) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.boolSize(sendDir)
    if (recvDir) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.boolSize(recvDir)
    if (value != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(value)
    if (typeRef != null) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.messageSize(typeRef)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ChanType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (begin != 0) protoMarshal.writeTag(8).writeInt32(begin)
    if (arrow != 0) protoMarshal.writeTag(16).writeInt32(arrow)
    if (sendDir) protoMarshal.writeTag(24).writeBool(sendDir)
    if (recvDir) protoMarshal.writeTag(32).writeBool(recvDir)
    if (value != null) protoMarshal.writeTag(42).writeMessage(value)
    if (typeRef != null) protoMarshal.writeTag(50).writeMessage(typeRef)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ChanType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ChanType {
    var begin = 0
    var arrow = 0
    var sendDir = false
    var recvDir = false
    var value: go2k.compile.dumppb.Expr_? = null
    var typeRef: go2k.compile.dumppb.TypeRef? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ChanType(begin, arrow, sendDir, recvDir,
            value, typeRef, protoUnmarshal.unknownFields())
        8 -> begin = protoUnmarshal.readInt32()
        16 -> arrow = protoUnmarshal.readInt32()
        24 -> sendDir = protoUnmarshal.readBool()
        32 -> recvDir = protoUnmarshal.readBool()
        42 -> value = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        50 -> typeRef = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeRef.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Stmt_.protoMergeImpl(plus: Stmt_?): Stmt_ = plus?.copy(
    stmt = when {
        stmt is Stmt_.Stmt.BadStmt && plus.stmt is Stmt_.Stmt.BadStmt ->
            Stmt_.Stmt.BadStmt(stmt.badStmt + plus.stmt.badStmt)
        stmt is Stmt_.Stmt.DeclStmt && plus.stmt is Stmt_.Stmt.DeclStmt ->
            Stmt_.Stmt.DeclStmt(stmt.declStmt + plus.stmt.declStmt)
        stmt is Stmt_.Stmt.EmptyStmt && plus.stmt is Stmt_.Stmt.EmptyStmt ->
            Stmt_.Stmt.EmptyStmt(stmt.emptyStmt + plus.stmt.emptyStmt)
        stmt is Stmt_.Stmt.LabeledStmt && plus.stmt is Stmt_.Stmt.LabeledStmt ->
            Stmt_.Stmt.LabeledStmt(stmt.labeledStmt + plus.stmt.labeledStmt)
        stmt is Stmt_.Stmt.ExprStmt && plus.stmt is Stmt_.Stmt.ExprStmt ->
            Stmt_.Stmt.ExprStmt(stmt.exprStmt + plus.stmt.exprStmt)
        stmt is Stmt_.Stmt.SendStmt && plus.stmt is Stmt_.Stmt.SendStmt ->
            Stmt_.Stmt.SendStmt(stmt.sendStmt + plus.stmt.sendStmt)
        stmt is Stmt_.Stmt.IncDecStmt && plus.stmt is Stmt_.Stmt.IncDecStmt ->
            Stmt_.Stmt.IncDecStmt(stmt.incDecStmt + plus.stmt.incDecStmt)
        stmt is Stmt_.Stmt.AssignStmt && plus.stmt is Stmt_.Stmt.AssignStmt ->
            Stmt_.Stmt.AssignStmt(stmt.assignStmt + plus.stmt.assignStmt)
        stmt is Stmt_.Stmt.GoStmt && plus.stmt is Stmt_.Stmt.GoStmt ->
            Stmt_.Stmt.GoStmt(stmt.goStmt + plus.stmt.goStmt)
        stmt is Stmt_.Stmt.DeferStmt && plus.stmt is Stmt_.Stmt.DeferStmt ->
            Stmt_.Stmt.DeferStmt(stmt.deferStmt + plus.stmt.deferStmt)
        stmt is Stmt_.Stmt.ReturnStmt && plus.stmt is Stmt_.Stmt.ReturnStmt ->
            Stmt_.Stmt.ReturnStmt(stmt.returnStmt + plus.stmt.returnStmt)
        stmt is Stmt_.Stmt.BranchStmt && plus.stmt is Stmt_.Stmt.BranchStmt ->
            Stmt_.Stmt.BranchStmt(stmt.branchStmt + plus.stmt.branchStmt)
        stmt is Stmt_.Stmt.BlockStmt && plus.stmt is Stmt_.Stmt.BlockStmt ->
            Stmt_.Stmt.BlockStmt(stmt.blockStmt + plus.stmt.blockStmt)
        stmt is Stmt_.Stmt.IfStmt && plus.stmt is Stmt_.Stmt.IfStmt ->
            Stmt_.Stmt.IfStmt(stmt.ifStmt + plus.stmt.ifStmt)
        stmt is Stmt_.Stmt.CaseClause && plus.stmt is Stmt_.Stmt.CaseClause ->
            Stmt_.Stmt.CaseClause(stmt.caseClause + plus.stmt.caseClause)
        stmt is Stmt_.Stmt.SwitchStmt && plus.stmt is Stmt_.Stmt.SwitchStmt ->
            Stmt_.Stmt.SwitchStmt(stmt.switchStmt + plus.stmt.switchStmt)
        stmt is Stmt_.Stmt.TypeSwitchStmt && plus.stmt is Stmt_.Stmt.TypeSwitchStmt ->
            Stmt_.Stmt.TypeSwitchStmt(stmt.typeSwitchStmt + plus.stmt.typeSwitchStmt)
        stmt is Stmt_.Stmt.CommClause && plus.stmt is Stmt_.Stmt.CommClause ->
            Stmt_.Stmt.CommClause(stmt.commClause + plus.stmt.commClause)
        stmt is Stmt_.Stmt.SelectStmt && plus.stmt is Stmt_.Stmt.SelectStmt ->
            Stmt_.Stmt.SelectStmt(stmt.selectStmt + plus.stmt.selectStmt)
        stmt is Stmt_.Stmt.ForStmt && plus.stmt is Stmt_.Stmt.ForStmt ->
            Stmt_.Stmt.ForStmt(stmt.forStmt + plus.stmt.forStmt)
        stmt is Stmt_.Stmt.RangeStmt && plus.stmt is Stmt_.Stmt.RangeStmt ->
            Stmt_.Stmt.RangeStmt(stmt.rangeStmt + plus.stmt.rangeStmt)
        else ->
            plus.stmt ?: stmt
    },
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Stmt_.protoSizeImpl(): Int {
    var protoSize = 0
    when (stmt) {
        is Stmt_.Stmt.BadStmt -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(stmt.badStmt)
        is Stmt_.Stmt.DeclStmt -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(stmt.declStmt)
        is Stmt_.Stmt.EmptyStmt -> protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(stmt.emptyStmt)
        is Stmt_.Stmt.LabeledStmt -> protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(stmt.labeledStmt)
        is Stmt_.Stmt.ExprStmt -> protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(stmt.exprStmt)
        is Stmt_.Stmt.SendStmt -> protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.messageSize(stmt.sendStmt)
        is Stmt_.Stmt.IncDecStmt -> protoSize += pbandk.Sizer.tagSize(7) + pbandk.Sizer.messageSize(stmt.incDecStmt)
        is Stmt_.Stmt.AssignStmt -> protoSize += pbandk.Sizer.tagSize(8) + pbandk.Sizer.messageSize(stmt.assignStmt)
        is Stmt_.Stmt.GoStmt -> protoSize += pbandk.Sizer.tagSize(9) + pbandk.Sizer.messageSize(stmt.goStmt)
        is Stmt_.Stmt.DeferStmt -> protoSize += pbandk.Sizer.tagSize(10) + pbandk.Sizer.messageSize(stmt.deferStmt)
        is Stmt_.Stmt.ReturnStmt -> protoSize += pbandk.Sizer.tagSize(11) + pbandk.Sizer.messageSize(stmt.returnStmt)
        is Stmt_.Stmt.BranchStmt -> protoSize += pbandk.Sizer.tagSize(12) + pbandk.Sizer.messageSize(stmt.branchStmt)
        is Stmt_.Stmt.BlockStmt -> protoSize += pbandk.Sizer.tagSize(13) + pbandk.Sizer.messageSize(stmt.blockStmt)
        is Stmt_.Stmt.IfStmt -> protoSize += pbandk.Sizer.tagSize(14) + pbandk.Sizer.messageSize(stmt.ifStmt)
        is Stmt_.Stmt.CaseClause -> protoSize += pbandk.Sizer.tagSize(15) + pbandk.Sizer.messageSize(stmt.caseClause)
        is Stmt_.Stmt.SwitchStmt -> protoSize += pbandk.Sizer.tagSize(16) + pbandk.Sizer.messageSize(stmt.switchStmt)
        is Stmt_.Stmt.TypeSwitchStmt -> protoSize += pbandk.Sizer.tagSize(17) + pbandk.Sizer.messageSize(stmt.typeSwitchStmt)
        is Stmt_.Stmt.CommClause -> protoSize += pbandk.Sizer.tagSize(18) + pbandk.Sizer.messageSize(stmt.commClause)
        is Stmt_.Stmt.SelectStmt -> protoSize += pbandk.Sizer.tagSize(19) + pbandk.Sizer.messageSize(stmt.selectStmt)
        is Stmt_.Stmt.ForStmt -> protoSize += pbandk.Sizer.tagSize(20) + pbandk.Sizer.messageSize(stmt.forStmt)
        is Stmt_.Stmt.RangeStmt -> protoSize += pbandk.Sizer.tagSize(21) + pbandk.Sizer.messageSize(stmt.rangeStmt)
    }
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Stmt_.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (stmt is Stmt_.Stmt.BadStmt) protoMarshal.writeTag(10).writeMessage(stmt.badStmt)
    if (stmt is Stmt_.Stmt.DeclStmt) protoMarshal.writeTag(18).writeMessage(stmt.declStmt)
    if (stmt is Stmt_.Stmt.EmptyStmt) protoMarshal.writeTag(26).writeMessage(stmt.emptyStmt)
    if (stmt is Stmt_.Stmt.LabeledStmt) protoMarshal.writeTag(34).writeMessage(stmt.labeledStmt)
    if (stmt is Stmt_.Stmt.ExprStmt) protoMarshal.writeTag(42).writeMessage(stmt.exprStmt)
    if (stmt is Stmt_.Stmt.SendStmt) protoMarshal.writeTag(50).writeMessage(stmt.sendStmt)
    if (stmt is Stmt_.Stmt.IncDecStmt) protoMarshal.writeTag(58).writeMessage(stmt.incDecStmt)
    if (stmt is Stmt_.Stmt.AssignStmt) protoMarshal.writeTag(66).writeMessage(stmt.assignStmt)
    if (stmt is Stmt_.Stmt.GoStmt) protoMarshal.writeTag(74).writeMessage(stmt.goStmt)
    if (stmt is Stmt_.Stmt.DeferStmt) protoMarshal.writeTag(82).writeMessage(stmt.deferStmt)
    if (stmt is Stmt_.Stmt.ReturnStmt) protoMarshal.writeTag(90).writeMessage(stmt.returnStmt)
    if (stmt is Stmt_.Stmt.BranchStmt) protoMarshal.writeTag(98).writeMessage(stmt.branchStmt)
    if (stmt is Stmt_.Stmt.BlockStmt) protoMarshal.writeTag(106).writeMessage(stmt.blockStmt)
    if (stmt is Stmt_.Stmt.IfStmt) protoMarshal.writeTag(114).writeMessage(stmt.ifStmt)
    if (stmt is Stmt_.Stmt.CaseClause) protoMarshal.writeTag(122).writeMessage(stmt.caseClause)
    if (stmt is Stmt_.Stmt.SwitchStmt) protoMarshal.writeTag(130).writeMessage(stmt.switchStmt)
    if (stmt is Stmt_.Stmt.TypeSwitchStmt) protoMarshal.writeTag(138).writeMessage(stmt.typeSwitchStmt)
    if (stmt is Stmt_.Stmt.CommClause) protoMarshal.writeTag(146).writeMessage(stmt.commClause)
    if (stmt is Stmt_.Stmt.SelectStmt) protoMarshal.writeTag(154).writeMessage(stmt.selectStmt)
    if (stmt is Stmt_.Stmt.ForStmt) protoMarshal.writeTag(162).writeMessage(stmt.forStmt)
    if (stmt is Stmt_.Stmt.RangeStmt) protoMarshal.writeTag(170).writeMessage(stmt.rangeStmt)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Stmt_.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Stmt_ {
    var stmt: Stmt_.Stmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Stmt_(stmt, protoUnmarshal.unknownFields())
        10 -> stmt = Stmt_.Stmt.BadStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.BadStmt.Companion))
        18 -> stmt = Stmt_.Stmt.DeclStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.DeclStmt.Companion))
        26 -> stmt = Stmt_.Stmt.EmptyStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.EmptyStmt.Companion))
        34 -> stmt = Stmt_.Stmt.LabeledStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.LabeledStmt.Companion))
        42 -> stmt = Stmt_.Stmt.ExprStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.ExprStmt.Companion))
        50 -> stmt = Stmt_.Stmt.SendStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.SendStmt.Companion))
        58 -> stmt = Stmt_.Stmt.IncDecStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.IncDecStmt.Companion))
        66 -> stmt = Stmt_.Stmt.AssignStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.AssignStmt.Companion))
        74 -> stmt = Stmt_.Stmt.GoStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.GoStmt.Companion))
        82 -> stmt = Stmt_.Stmt.DeferStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.DeferStmt.Companion))
        90 -> stmt = Stmt_.Stmt.ReturnStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.ReturnStmt.Companion))
        98 -> stmt = Stmt_.Stmt.BranchStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.BranchStmt.Companion))
        106 -> stmt = Stmt_.Stmt.BlockStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion))
        114 -> stmt = Stmt_.Stmt.IfStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.IfStmt.Companion))
        122 -> stmt = Stmt_.Stmt.CaseClause(protoUnmarshal.readMessage(go2k.compile.dumppb.CaseClause.Companion))
        130 -> stmt = Stmt_.Stmt.SwitchStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.SwitchStmt.Companion))
        138 -> stmt = Stmt_.Stmt.TypeSwitchStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeSwitchStmt.Companion))
        146 -> stmt = Stmt_.Stmt.CommClause(protoUnmarshal.readMessage(go2k.compile.dumppb.CommClause.Companion))
        154 -> stmt = Stmt_.Stmt.SelectStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.SelectStmt.Companion))
        162 -> stmt = Stmt_.Stmt.ForStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.ForStmt.Companion))
        170 -> stmt = Stmt_.Stmt.RangeStmt(protoUnmarshal.readMessage(go2k.compile.dumppb.RangeStmt.Companion))
        else -> protoUnmarshal.unknownField()
    }
}

private fun BadStmt.protoMergeImpl(plus: BadStmt?): BadStmt = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BadStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (from != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(from)
    if (to != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(to)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BadStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (from != 0) protoMarshal.writeTag(8).writeInt32(from)
    if (to != 0) protoMarshal.writeTag(16).writeInt32(to)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BadStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BadStmt {
    var from = 0
    var to = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BadStmt(from, to, protoUnmarshal.unknownFields())
        8 -> from = protoUnmarshal.readInt32()
        16 -> to = protoUnmarshal.readInt32()
        else -> protoUnmarshal.unknownField()
    }
}

private fun DeclStmt.protoMergeImpl(plus: DeclStmt?): DeclStmt = plus?.copy(
    decl = decl?.plus(plus.decl) ?: plus.decl,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun DeclStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (decl != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(decl)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun DeclStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (decl != null) protoMarshal.writeTag(10).writeMessage(decl)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun DeclStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): DeclStmt {
    var decl: go2k.compile.dumppb.Decl_? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return DeclStmt(decl, protoUnmarshal.unknownFields())
        10 -> decl = protoUnmarshal.readMessage(go2k.compile.dumppb.Decl_.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun EmptyStmt.protoMergeImpl(plus: EmptyStmt?): EmptyStmt = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun EmptyStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (semicolon != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(semicolon)
    if (implicit) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.boolSize(implicit)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun EmptyStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (semicolon != 0) protoMarshal.writeTag(8).writeInt32(semicolon)
    if (implicit) protoMarshal.writeTag(16).writeBool(implicit)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun EmptyStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): EmptyStmt {
    var semicolon = 0
    var implicit = false
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return EmptyStmt(semicolon, implicit, protoUnmarshal.unknownFields())
        8 -> semicolon = protoUnmarshal.readInt32()
        16 -> implicit = protoUnmarshal.readBool()
        else -> protoUnmarshal.unknownField()
    }
}

private fun LabeledStmt.protoMergeImpl(plus: LabeledStmt?): LabeledStmt = plus?.copy(
    label = label?.plus(plus.label) ?: plus.label,
    stmt = stmt?.plus(plus.stmt) ?: plus.stmt,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun LabeledStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (label != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(label)
    if (colon != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(colon)
    if (stmt != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(stmt)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun LabeledStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (label != null) protoMarshal.writeTag(10).writeMessage(label)
    if (colon != 0) protoMarshal.writeTag(16).writeInt32(colon)
    if (stmt != null) protoMarshal.writeTag(26).writeMessage(stmt)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun LabeledStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): LabeledStmt {
    var label: go2k.compile.dumppb.Ident? = null
    var colon = 0
    var stmt: go2k.compile.dumppb.Stmt_? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return LabeledStmt(label, colon, stmt, protoUnmarshal.unknownFields())
        10 -> label = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        16 -> colon = protoUnmarshal.readInt32()
        26 -> stmt = protoUnmarshal.readMessage(go2k.compile.dumppb.Stmt_.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun ExprStmt.protoMergeImpl(plus: ExprStmt?): ExprStmt = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ExprStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ExprStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ExprStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ExprStmt {
    var x: go2k.compile.dumppb.Expr_? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ExprStmt(x, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun SendStmt.protoMergeImpl(plus: SendStmt?): SendStmt = plus?.copy(
    chan = chan?.plus(plus.chan) ?: plus.chan,
    value = value?.plus(plus.value) ?: plus.value,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun SendStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (chan != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(chan)
    if (arrow != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(arrow)
    if (value != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(value)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun SendStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (chan != null) protoMarshal.writeTag(10).writeMessage(chan)
    if (arrow != 0) protoMarshal.writeTag(16).writeInt32(arrow)
    if (value != null) protoMarshal.writeTag(26).writeMessage(value)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun SendStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): SendStmt {
    var chan: go2k.compile.dumppb.Expr_? = null
    var arrow = 0
    var value: go2k.compile.dumppb.Expr_? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return SendStmt(chan, arrow, value, protoUnmarshal.unknownFields())
        10 -> chan = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        16 -> arrow = protoUnmarshal.readInt32()
        26 -> value = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun IncDecStmt.protoMergeImpl(plus: IncDecStmt?): IncDecStmt = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun IncDecStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (tokPos != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(tokPos)
    if (tok.value != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.enumSize(tok)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun IncDecStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (tokPos != 0) protoMarshal.writeTag(16).writeInt32(tokPos)
    if (tok.value != 0) protoMarshal.writeTag(24).writeEnum(tok)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun IncDecStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): IncDecStmt {
    var x: go2k.compile.dumppb.Expr_? = null
    var tokPos = 0
    var tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return IncDecStmt(x, tokPos, tok, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        16 -> tokPos = protoUnmarshal.readInt32()
        24 -> tok = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun AssignStmt.protoMergeImpl(plus: AssignStmt?): AssignStmt = plus?.copy(
    lhs = lhs + plus.lhs,
    rhs = rhs + plus.rhs,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun AssignStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (lhs.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(1) * lhs.size) + lhs.sumBy(pbandk.Sizer::messageSize)
    if (tokPos != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(tokPos)
    if (tok.value != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.enumSize(tok)
    if (rhs.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(4) * rhs.size) + rhs.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun AssignStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (lhs.isNotEmpty()) lhs.forEach { protoMarshal.writeTag(10).writeMessage(it) }
    if (tokPos != 0) protoMarshal.writeTag(16).writeInt32(tokPos)
    if (tok.value != 0) protoMarshal.writeTag(24).writeEnum(tok)
    if (rhs.isNotEmpty()) rhs.forEach { protoMarshal.writeTag(34).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun AssignStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): AssignStmt {
    var lhs: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Expr_>? = null
    var tokPos = 0
    var tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var rhs: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Expr_>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return AssignStmt(pbandk.ListWithSize.Builder.fixed(lhs), tokPos, tok, pbandk.ListWithSize.Builder.fixed(rhs), protoUnmarshal.unknownFields())
        10 -> lhs = protoUnmarshal.readRepeatedMessage(lhs, go2k.compile.dumppb.Expr_.Companion, true)
        16 -> tokPos = protoUnmarshal.readInt32()
        24 -> tok = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        34 -> rhs = protoUnmarshal.readRepeatedMessage(rhs, go2k.compile.dumppb.Expr_.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun GoStmt.protoMergeImpl(plus: GoStmt?): GoStmt = plus?.copy(
    call = call?.plus(plus.call) ?: plus.call,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun GoStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (go != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(go)
    if (call != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(call)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun GoStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (go != 0) protoMarshal.writeTag(8).writeInt32(go)
    if (call != null) protoMarshal.writeTag(18).writeMessage(call)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun GoStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): GoStmt {
    var go = 0
    var call: go2k.compile.dumppb.CallExpr? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return GoStmt(go, call, protoUnmarshal.unknownFields())
        8 -> go = protoUnmarshal.readInt32()
        18 -> call = protoUnmarshal.readMessage(go2k.compile.dumppb.CallExpr.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun DeferStmt.protoMergeImpl(plus: DeferStmt?): DeferStmt = plus?.copy(
    call = call?.plus(plus.call) ?: plus.call,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun DeferStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (defer != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(defer)
    if (call != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(call)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun DeferStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (defer != 0) protoMarshal.writeTag(8).writeInt32(defer)
    if (call != null) protoMarshal.writeTag(18).writeMessage(call)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun DeferStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): DeferStmt {
    var defer = 0
    var call: go2k.compile.dumppb.CallExpr? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return DeferStmt(defer, call, protoUnmarshal.unknownFields())
        8 -> defer = protoUnmarshal.readInt32()
        18 -> call = protoUnmarshal.readMessage(go2k.compile.dumppb.CallExpr.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun ReturnStmt.protoMergeImpl(plus: ReturnStmt?): ReturnStmt = plus?.copy(
    results = results + plus.results,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ReturnStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (`return` != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(`return`)
    if (results.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * results.size) + results.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ReturnStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (`return` != 0) protoMarshal.writeTag(8).writeInt32(`return`)
    if (results.isNotEmpty()) results.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ReturnStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ReturnStmt {
    var `return` = 0
    var results: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Expr_>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ReturnStmt(`return`, pbandk.ListWithSize.Builder.fixed(results), protoUnmarshal.unknownFields())
        8 -> `return` = protoUnmarshal.readInt32()
        18 -> results = protoUnmarshal.readRepeatedMessage(results, go2k.compile.dumppb.Expr_.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun BranchStmt.protoMergeImpl(plus: BranchStmt?): BranchStmt = plus?.copy(
    label = label?.plus(plus.label) ?: plus.label,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BranchStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (tokPos != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(tokPos)
    if (tok.value != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(tok)
    if (label != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(label)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BranchStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (tokPos != 0) protoMarshal.writeTag(8).writeInt32(tokPos)
    if (tok.value != 0) protoMarshal.writeTag(16).writeEnum(tok)
    if (label != null) protoMarshal.writeTag(26).writeMessage(label)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BranchStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BranchStmt {
    var tokPos = 0
    var tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var label: go2k.compile.dumppb.Ident? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BranchStmt(tokPos, tok, label, protoUnmarshal.unknownFields())
        8 -> tokPos = protoUnmarshal.readInt32()
        16 -> tok = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        26 -> label = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun BlockStmt.protoMergeImpl(plus: BlockStmt?): BlockStmt = plus?.copy(
    list = list + plus.list,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BlockStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (lbrace != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(lbrace)
    if (list.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * list.size) + list.sumBy(pbandk.Sizer::messageSize)
    if (rbrace != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int32Size(rbrace)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BlockStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (lbrace != 0) protoMarshal.writeTag(8).writeInt32(lbrace)
    if (list.isNotEmpty()) list.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (rbrace != 0) protoMarshal.writeTag(24).writeInt32(rbrace)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BlockStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BlockStmt {
    var lbrace = 0
    var list: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Stmt_>? = null
    var rbrace = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BlockStmt(lbrace, pbandk.ListWithSize.Builder.fixed(list), rbrace, protoUnmarshal.unknownFields())
        8 -> lbrace = protoUnmarshal.readInt32()
        18 -> list = protoUnmarshal.readRepeatedMessage(list, go2k.compile.dumppb.Stmt_.Companion, true)
        24 -> rbrace = protoUnmarshal.readInt32()
        else -> protoUnmarshal.unknownField()
    }
}

private fun IfStmt.protoMergeImpl(plus: IfStmt?): IfStmt = plus?.copy(
    init = init?.plus(plus.init) ?: plus.init,
    cond = cond?.plus(plus.cond) ?: plus.cond,
    body = body?.plus(plus.body) ?: plus.body,
    `else` = `else`?.plus(plus.`else`) ?: plus.`else`,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun IfStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (`if` != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(`if`)
    if (init != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(init)
    if (cond != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(cond)
    if (body != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(body)
    if (`else` != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(`else`)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun IfStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (`if` != 0) protoMarshal.writeTag(8).writeInt32(`if`)
    if (init != null) protoMarshal.writeTag(18).writeMessage(init)
    if (cond != null) protoMarshal.writeTag(26).writeMessage(cond)
    if (body != null) protoMarshal.writeTag(34).writeMessage(body)
    if (`else` != null) protoMarshal.writeTag(42).writeMessage(`else`)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun IfStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): IfStmt {
    var `if` = 0
    var init: go2k.compile.dumppb.Stmt_? = null
    var cond: go2k.compile.dumppb.Expr_? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    var `else`: go2k.compile.dumppb.Stmt_? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return IfStmt(`if`, init, cond, body,
            `else`, protoUnmarshal.unknownFields())
        8 -> `if` = protoUnmarshal.readInt32()
        18 -> init = protoUnmarshal.readMessage(go2k.compile.dumppb.Stmt_.Companion)
        26 -> cond = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        42 -> `else` = protoUnmarshal.readMessage(go2k.compile.dumppb.Stmt_.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun CaseClause.protoMergeImpl(plus: CaseClause?): CaseClause = plus?.copy(
    list = list + plus.list,
    body = body + plus.body,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun CaseClause.protoSizeImpl(): Int {
    var protoSize = 0
    if (case != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(case)
    if (list.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * list.size) + list.sumBy(pbandk.Sizer::messageSize)
    if (colon != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int32Size(colon)
    if (body.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(4) * body.size) + body.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun CaseClause.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (case != 0) protoMarshal.writeTag(8).writeInt32(case)
    if (list.isNotEmpty()) list.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (colon != 0) protoMarshal.writeTag(24).writeInt32(colon)
    if (body.isNotEmpty()) body.forEach { protoMarshal.writeTag(34).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun CaseClause.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): CaseClause {
    var case = 0
    var list: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Expr_>? = null
    var colon = 0
    var body: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Stmt_>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CaseClause(case, pbandk.ListWithSize.Builder.fixed(list), colon, pbandk.ListWithSize.Builder.fixed(body), protoUnmarshal.unknownFields())
        8 -> case = protoUnmarshal.readInt32()
        18 -> list = protoUnmarshal.readRepeatedMessage(list, go2k.compile.dumppb.Expr_.Companion, true)
        24 -> colon = protoUnmarshal.readInt32()
        34 -> body = protoUnmarshal.readRepeatedMessage(body, go2k.compile.dumppb.Stmt_.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun SwitchStmt.protoMergeImpl(plus: SwitchStmt?): SwitchStmt = plus?.copy(
    init = init?.plus(plus.init) ?: plus.init,
    tag = tag?.plus(plus.tag) ?: plus.tag,
    body = body?.plus(plus.body) ?: plus.body,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun SwitchStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (switch != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(switch)
    if (init != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(init)
    if (tag != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(tag)
    if (body != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(body)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun SwitchStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (switch != 0) protoMarshal.writeTag(8).writeInt32(switch)
    if (init != null) protoMarshal.writeTag(18).writeMessage(init)
    if (tag != null) protoMarshal.writeTag(26).writeMessage(tag)
    if (body != null) protoMarshal.writeTag(34).writeMessage(body)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun SwitchStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): SwitchStmt {
    var switch = 0
    var init: go2k.compile.dumppb.Stmt_? = null
    var tag: go2k.compile.dumppb.Expr_? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return SwitchStmt(switch, init, tag, body, protoUnmarshal.unknownFields())
        8 -> switch = protoUnmarshal.readInt32()
        18 -> init = protoUnmarshal.readMessage(go2k.compile.dumppb.Stmt_.Companion)
        26 -> tag = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeSwitchStmt.protoMergeImpl(plus: TypeSwitchStmt?): TypeSwitchStmt = plus?.copy(
    init = init?.plus(plus.init) ?: plus.init,
    assign = assign?.plus(plus.assign) ?: plus.assign,
    body = body?.plus(plus.body) ?: plus.body,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeSwitchStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (switch != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(switch)
    if (init != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(init)
    if (assign != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(assign)
    if (body != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(body)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeSwitchStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (switch != 0) protoMarshal.writeTag(8).writeInt32(switch)
    if (init != null) protoMarshal.writeTag(18).writeMessage(init)
    if (assign != null) protoMarshal.writeTag(26).writeMessage(assign)
    if (body != null) protoMarshal.writeTag(34).writeMessage(body)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeSwitchStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeSwitchStmt {
    var switch = 0
    var init: go2k.compile.dumppb.Stmt_? = null
    var assign: go2k.compile.dumppb.Stmt_? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeSwitchStmt(switch, init, assign, body, protoUnmarshal.unknownFields())
        8 -> switch = protoUnmarshal.readInt32()
        18 -> init = protoUnmarshal.readMessage(go2k.compile.dumppb.Stmt_.Companion)
        26 -> assign = protoUnmarshal.readMessage(go2k.compile.dumppb.Stmt_.Companion)
        34 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun CommClause.protoMergeImpl(plus: CommClause?): CommClause = plus?.copy(
    comm = comm?.plus(plus.comm) ?: plus.comm,
    body = body + plus.body,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun CommClause.protoSizeImpl(): Int {
    var protoSize = 0
    if (case != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(case)
    if (comm != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(comm)
    if (colon != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int32Size(colon)
    if (body.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(4) * body.size) + body.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun CommClause.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (case != 0) protoMarshal.writeTag(8).writeInt32(case)
    if (comm != null) protoMarshal.writeTag(18).writeMessage(comm)
    if (colon != 0) protoMarshal.writeTag(24).writeInt32(colon)
    if (body.isNotEmpty()) body.forEach { protoMarshal.writeTag(34).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun CommClause.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): CommClause {
    var case = 0
    var comm: go2k.compile.dumppb.Stmt_? = null
    var colon = 0
    var body: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Stmt_>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CommClause(case, comm, colon, pbandk.ListWithSize.Builder.fixed(body), protoUnmarshal.unknownFields())
        8 -> case = protoUnmarshal.readInt32()
        18 -> comm = protoUnmarshal.readMessage(go2k.compile.dumppb.Stmt_.Companion)
        24 -> colon = protoUnmarshal.readInt32()
        34 -> body = protoUnmarshal.readRepeatedMessage(body, go2k.compile.dumppb.Stmt_.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun SelectStmt.protoMergeImpl(plus: SelectStmt?): SelectStmt = plus?.copy(
    body = body?.plus(plus.body) ?: plus.body,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun SelectStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (select != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(select)
    if (body != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(body)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun SelectStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (select != 0) protoMarshal.writeTag(8).writeInt32(select)
    if (body != null) protoMarshal.writeTag(18).writeMessage(body)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun SelectStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): SelectStmt {
    var select = 0
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return SelectStmt(select, body, protoUnmarshal.unknownFields())
        8 -> select = protoUnmarshal.readInt32()
        18 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun ForStmt.protoMergeImpl(plus: ForStmt?): ForStmt = plus?.copy(
    init = init?.plus(plus.init) ?: plus.init,
    cond = cond?.plus(plus.cond) ?: plus.cond,
    post = post?.plus(plus.post) ?: plus.post,
    body = body?.plus(plus.body) ?: plus.body,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ForStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (`for` != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(`for`)
    if (init != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(init)
    if (cond != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(cond)
    if (post != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(post)
    if (body != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(body)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ForStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (`for` != 0) protoMarshal.writeTag(8).writeInt32(`for`)
    if (init != null) protoMarshal.writeTag(18).writeMessage(init)
    if (cond != null) protoMarshal.writeTag(26).writeMessage(cond)
    if (post != null) protoMarshal.writeTag(34).writeMessage(post)
    if (body != null) protoMarshal.writeTag(42).writeMessage(body)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ForStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ForStmt {
    var `for` = 0
    var init: go2k.compile.dumppb.Stmt_? = null
    var cond: go2k.compile.dumppb.Expr_? = null
    var post: go2k.compile.dumppb.Stmt_? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ForStmt(`for`, init, cond, post,
            body, protoUnmarshal.unknownFields())
        8 -> `for` = protoUnmarshal.readInt32()
        18 -> init = protoUnmarshal.readMessage(go2k.compile.dumppb.Stmt_.Companion)
        26 -> cond = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> post = protoUnmarshal.readMessage(go2k.compile.dumppb.Stmt_.Companion)
        42 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun RangeStmt.protoMergeImpl(plus: RangeStmt?): RangeStmt = plus?.copy(
    key = key?.plus(plus.key) ?: plus.key,
    value = value?.plus(plus.value) ?: plus.value,
    x = x?.plus(plus.x) ?: plus.x,
    body = body?.plus(plus.body) ?: plus.body,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun RangeStmt.protoSizeImpl(): Int {
    var protoSize = 0
    if (`for` != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(`for`)
    if (key != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(key)
    if (value != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(value)
    if (tokPos != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(tokPos)
    if (tok.value != 0) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.enumSize(tok)
    if (x != null) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.messageSize(x)
    if (body != null) protoSize += pbandk.Sizer.tagSize(7) + pbandk.Sizer.messageSize(body)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun RangeStmt.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (`for` != 0) protoMarshal.writeTag(8).writeInt32(`for`)
    if (key != null) protoMarshal.writeTag(18).writeMessage(key)
    if (value != null) protoMarshal.writeTag(26).writeMessage(value)
    if (tokPos != 0) protoMarshal.writeTag(32).writeInt32(tokPos)
    if (tok.value != 0) protoMarshal.writeTag(40).writeEnum(tok)
    if (x != null) protoMarshal.writeTag(50).writeMessage(x)
    if (body != null) protoMarshal.writeTag(58).writeMessage(body)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun RangeStmt.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): RangeStmt {
    var `for` = 0
    var key: go2k.compile.dumppb.Expr_? = null
    var value: go2k.compile.dumppb.Expr_? = null
    var tokPos = 0
    var tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var x: go2k.compile.dumppb.Expr_? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return RangeStmt(`for`, key, value, tokPos,
            tok, x, body, protoUnmarshal.unknownFields())
        8 -> `for` = protoUnmarshal.readInt32()
        18 -> key = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        26 -> value = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        32 -> tokPos = protoUnmarshal.readInt32()
        40 -> tok = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        50 -> x = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        58 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Spec_.protoMergeImpl(plus: Spec_?): Spec_ = plus?.copy(
    spec = when {
        spec is Spec_.Spec.ImportSpec && plus.spec is Spec_.Spec.ImportSpec ->
            Spec_.Spec.ImportSpec(spec.importSpec + plus.spec.importSpec)
        spec is Spec_.Spec.ValueSpec && plus.spec is Spec_.Spec.ValueSpec ->
            Spec_.Spec.ValueSpec(spec.valueSpec + plus.spec.valueSpec)
        spec is Spec_.Spec.TypeSpec && plus.spec is Spec_.Spec.TypeSpec ->
            Spec_.Spec.TypeSpec(spec.typeSpec + plus.spec.typeSpec)
        else ->
            plus.spec ?: spec
    },
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Spec_.protoSizeImpl(): Int {
    var protoSize = 0
    when (spec) {
        is Spec_.Spec.ImportSpec -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(spec.importSpec)
        is Spec_.Spec.ValueSpec -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(spec.valueSpec)
        is Spec_.Spec.TypeSpec -> protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(spec.typeSpec)
    }
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Spec_.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (spec is Spec_.Spec.ImportSpec) protoMarshal.writeTag(10).writeMessage(spec.importSpec)
    if (spec is Spec_.Spec.ValueSpec) protoMarshal.writeTag(18).writeMessage(spec.valueSpec)
    if (spec is Spec_.Spec.TypeSpec) protoMarshal.writeTag(26).writeMessage(spec.typeSpec)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Spec_.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Spec_ {
    var spec: Spec_.Spec? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Spec_(spec, protoUnmarshal.unknownFields())
        10 -> spec = Spec_.Spec.ImportSpec(protoUnmarshal.readMessage(go2k.compile.dumppb.ImportSpec.Companion))
        18 -> spec = Spec_.Spec.ValueSpec(protoUnmarshal.readMessage(go2k.compile.dumppb.ValueSpec.Companion))
        26 -> spec = Spec_.Spec.TypeSpec(protoUnmarshal.readMessage(go2k.compile.dumppb.TypeSpec.Companion))
        else -> protoUnmarshal.unknownField()
    }
}

private fun ImportSpec.protoMergeImpl(plus: ImportSpec?): ImportSpec = plus?.copy(
    doc = doc?.plus(plus.doc) ?: plus.doc,
    name = name?.plus(plus.name) ?: plus.name,
    path = path?.plus(plus.path) ?: plus.path,
    comment = comment?.plus(plus.comment) ?: plus.comment,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ImportSpec.protoSizeImpl(): Int {
    var protoSize = 0
    if (doc != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(doc)
    if (name != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(name)
    if (path != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(path)
    if (comment != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(comment)
    if (endPos != 0) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.int32Size(endPos)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ImportSpec.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (doc != null) protoMarshal.writeTag(10).writeMessage(doc)
    if (name != null) protoMarshal.writeTag(18).writeMessage(name)
    if (path != null) protoMarshal.writeTag(26).writeMessage(path)
    if (comment != null) protoMarshal.writeTag(34).writeMessage(comment)
    if (endPos != 0) protoMarshal.writeTag(40).writeInt32(endPos)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ImportSpec.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ImportSpec {
    var doc: go2k.compile.dumppb.CommentGroup? = null
    var name: go2k.compile.dumppb.Ident? = null
    var path: go2k.compile.dumppb.BasicLit? = null
    var comment: go2k.compile.dumppb.CommentGroup? = null
    var endPos = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ImportSpec(doc, name, path, comment,
            endPos, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        18 -> name = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        26 -> path = protoUnmarshal.readMessage(go2k.compile.dumppb.BasicLit.Companion)
        34 -> comment = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        40 -> endPos = protoUnmarshal.readInt32()
        else -> protoUnmarshal.unknownField()
    }
}

private fun ValueSpec.protoMergeImpl(plus: ValueSpec?): ValueSpec = plus?.copy(
    doc = doc?.plus(plus.doc) ?: plus.doc,
    names = names + plus.names,
    type = type?.plus(plus.type) ?: plus.type,
    values = values + plus.values,
    comment = comment?.plus(plus.comment) ?: plus.comment,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ValueSpec.protoSizeImpl(): Int {
    var protoSize = 0
    if (doc != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(doc)
    if (names.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * names.size) + names.sumBy(pbandk.Sizer::messageSize)
    if (type != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(type)
    if (values.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(4) * values.size) + values.sumBy(pbandk.Sizer::messageSize)
    if (comment != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(comment)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ValueSpec.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (doc != null) protoMarshal.writeTag(10).writeMessage(doc)
    if (names.isNotEmpty()) names.forEach { protoMarshal.writeTag(18).writeMessage(it) }
    if (type != null) protoMarshal.writeTag(26).writeMessage(type)
    if (values.isNotEmpty()) values.forEach { protoMarshal.writeTag(34).writeMessage(it) }
    if (comment != null) protoMarshal.writeTag(42).writeMessage(comment)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ValueSpec.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ValueSpec {
    var doc: go2k.compile.dumppb.CommentGroup? = null
    var names: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Ident>? = null
    var type: go2k.compile.dumppb.Expr_? = null
    var values: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Expr_>? = null
    var comment: go2k.compile.dumppb.CommentGroup? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ValueSpec(doc, pbandk.ListWithSize.Builder.fixed(names), type, pbandk.ListWithSize.Builder.fixed(values),
            comment, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        18 -> names = protoUnmarshal.readRepeatedMessage(names, go2k.compile.dumppb.Ident.Companion, true)
        26 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        34 -> values = protoUnmarshal.readRepeatedMessage(values, go2k.compile.dumppb.Expr_.Companion, true)
        42 -> comment = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeSpec.protoMergeImpl(plus: TypeSpec?): TypeSpec = plus?.copy(
    doc = doc?.plus(plus.doc) ?: plus.doc,
    name = name?.plus(plus.name) ?: plus.name,
    type = type?.plus(plus.type) ?: plus.type,
    comment = comment?.plus(plus.comment) ?: plus.comment,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeSpec.protoSizeImpl(): Int {
    var protoSize = 0
    if (doc != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(doc)
    if (name != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(name)
    if (assign != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int32Size(assign)
    if (type != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(type)
    if (comment != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(comment)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeSpec.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (doc != null) protoMarshal.writeTag(10).writeMessage(doc)
    if (name != null) protoMarshal.writeTag(18).writeMessage(name)
    if (assign != 0) protoMarshal.writeTag(24).writeInt32(assign)
    if (type != null) protoMarshal.writeTag(34).writeMessage(type)
    if (comment != null) protoMarshal.writeTag(42).writeMessage(comment)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeSpec.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeSpec {
    var doc: go2k.compile.dumppb.CommentGroup? = null
    var name: go2k.compile.dumppb.Ident? = null
    var assign = 0
    var type: go2k.compile.dumppb.Expr_? = null
    var comment: go2k.compile.dumppb.CommentGroup? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeSpec(doc, name, assign, type,
            comment, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        18 -> name = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        24 -> assign = protoUnmarshal.readInt32()
        34 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.Expr_.Companion)
        42 -> comment = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Decl_.protoMergeImpl(plus: Decl_?): Decl_ = plus?.copy(
    decl = when {
        decl is Decl_.Decl.BadDecl && plus.decl is Decl_.Decl.BadDecl ->
            Decl_.Decl.BadDecl(decl.badDecl + plus.decl.badDecl)
        decl is Decl_.Decl.GenDecl && plus.decl is Decl_.Decl.GenDecl ->
            Decl_.Decl.GenDecl(decl.genDecl + plus.decl.genDecl)
        decl is Decl_.Decl.FuncDecl && plus.decl is Decl_.Decl.FuncDecl ->
            Decl_.Decl.FuncDecl(decl.funcDecl + plus.decl.funcDecl)
        else ->
            plus.decl ?: decl
    },
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Decl_.protoSizeImpl(): Int {
    var protoSize = 0
    when (decl) {
        is Decl_.Decl.BadDecl -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(decl.badDecl)
        is Decl_.Decl.GenDecl -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(decl.genDecl)
        is Decl_.Decl.FuncDecl -> protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(decl.funcDecl)
    }
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Decl_.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (decl is Decl_.Decl.BadDecl) protoMarshal.writeTag(10).writeMessage(decl.badDecl)
    if (decl is Decl_.Decl.GenDecl) protoMarshal.writeTag(18).writeMessage(decl.genDecl)
    if (decl is Decl_.Decl.FuncDecl) protoMarshal.writeTag(26).writeMessage(decl.funcDecl)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Decl_.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Decl_ {
    var decl: Decl_.Decl? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Decl_(decl, protoUnmarshal.unknownFields())
        10 -> decl = Decl_.Decl.BadDecl(protoUnmarshal.readMessage(go2k.compile.dumppb.BadDecl.Companion))
        18 -> decl = Decl_.Decl.GenDecl(protoUnmarshal.readMessage(go2k.compile.dumppb.GenDecl.Companion))
        26 -> decl = Decl_.Decl.FuncDecl(protoUnmarshal.readMessage(go2k.compile.dumppb.FuncDecl.Companion))
        else -> protoUnmarshal.unknownField()
    }
}

private fun BadDecl.protoMergeImpl(plus: BadDecl?): BadDecl = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BadDecl.protoSizeImpl(): Int {
    var protoSize = 0
    if (from != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(from)
    if (to != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(to)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BadDecl.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (from != 0) protoMarshal.writeTag(8).writeInt32(from)
    if (to != 0) protoMarshal.writeTag(16).writeInt32(to)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BadDecl.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BadDecl {
    var from = 0
    var to = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BadDecl(from, to, protoUnmarshal.unknownFields())
        8 -> from = protoUnmarshal.readInt32()
        16 -> to = protoUnmarshal.readInt32()
        else -> protoUnmarshal.unknownField()
    }
}

private fun GenDecl.protoMergeImpl(plus: GenDecl?): GenDecl = plus?.copy(
    doc = doc?.plus(plus.doc) ?: plus.doc,
    specs = specs + plus.specs,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun GenDecl.protoSizeImpl(): Int {
    var protoSize = 0
    if (doc != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(doc)
    if (tokPos != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(tokPos)
    if (tok.value != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.enumSize(tok)
    if (lparen != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(lparen)
    if (specs.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(5) * specs.size) + specs.sumBy(pbandk.Sizer::messageSize)
    if (rparen != 0) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.int32Size(rparen)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun GenDecl.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (doc != null) protoMarshal.writeTag(10).writeMessage(doc)
    if (tokPos != 0) protoMarshal.writeTag(16).writeInt32(tokPos)
    if (tok.value != 0) protoMarshal.writeTag(24).writeEnum(tok)
    if (lparen != 0) protoMarshal.writeTag(32).writeInt32(lparen)
    if (specs.isNotEmpty()) specs.forEach { protoMarshal.writeTag(42).writeMessage(it) }
    if (rparen != 0) protoMarshal.writeTag(48).writeInt32(rparen)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun GenDecl.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): GenDecl {
    var doc: go2k.compile.dumppb.CommentGroup? = null
    var tokPos = 0
    var tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var lparen = 0
    var specs: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Spec_>? = null
    var rparen = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return GenDecl(doc, tokPos, tok, lparen,
            pbandk.ListWithSize.Builder.fixed(specs), rparen, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        16 -> tokPos = protoUnmarshal.readInt32()
        24 -> tok = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        32 -> lparen = protoUnmarshal.readInt32()
        42 -> specs = protoUnmarshal.readRepeatedMessage(specs, go2k.compile.dumppb.Spec_.Companion, true)
        48 -> rparen = protoUnmarshal.readInt32()
        else -> protoUnmarshal.unknownField()
    }
}

private fun FuncDecl.protoMergeImpl(plus: FuncDecl?): FuncDecl = plus?.copy(
    doc = doc?.plus(plus.doc) ?: plus.doc,
    recv = recv?.plus(plus.recv) ?: plus.recv,
    name = name?.plus(plus.name) ?: plus.name,
    type = type?.plus(plus.type) ?: plus.type,
    body = body?.plus(plus.body) ?: plus.body,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun FuncDecl.protoSizeImpl(): Int {
    var protoSize = 0
    if (doc != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(doc)
    if (recv != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(recv)
    if (name != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(name)
    if (type != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(type)
    if (body != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(body)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun FuncDecl.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (doc != null) protoMarshal.writeTag(10).writeMessage(doc)
    if (recv != null) protoMarshal.writeTag(18).writeMessage(recv)
    if (name != null) protoMarshal.writeTag(26).writeMessage(name)
    if (type != null) protoMarshal.writeTag(34).writeMessage(type)
    if (body != null) protoMarshal.writeTag(42).writeMessage(body)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun FuncDecl.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): FuncDecl {
    var doc: go2k.compile.dumppb.CommentGroup? = null
    var recv: go2k.compile.dumppb.FieldList? = null
    var name: go2k.compile.dumppb.Ident? = null
    var type: go2k.compile.dumppb.FuncType? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return FuncDecl(doc, recv, name, type,
            body, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        18 -> recv = protoUnmarshal.readMessage(go2k.compile.dumppb.FieldList.Companion)
        26 -> name = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        34 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.FuncType.Companion)
        42 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun File.protoMergeImpl(plus: File?): File = plus?.copy(
    doc = doc?.plus(plus.doc) ?: plus.doc,
    name = name?.plus(plus.name) ?: plus.name,
    decls = decls + plus.decls,
    imports = imports + plus.imports,
    unresolved = unresolved + plus.unresolved,
    comments = comments + plus.comments,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun File.protoSizeImpl(): Int {
    var protoSize = 0
    if (fileName.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(fileName)
    if (doc != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(doc)
    if (`package` != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int32Size(`package`)
    if (name != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(name)
    if (decls.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(5) * decls.size) + decls.sumBy(pbandk.Sizer::messageSize)
    if (imports.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(6) * imports.size) + imports.sumBy(pbandk.Sizer::messageSize)
    if (unresolved.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(7) * unresolved.size) + unresolved.sumBy(pbandk.Sizer::messageSize)
    if (comments.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(8) * comments.size) + comments.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun File.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (fileName.isNotEmpty()) protoMarshal.writeTag(10).writeString(fileName)
    if (doc != null) protoMarshal.writeTag(18).writeMessage(doc)
    if (`package` != 0) protoMarshal.writeTag(24).writeInt32(`package`)
    if (name != null) protoMarshal.writeTag(34).writeMessage(name)
    if (decls.isNotEmpty()) decls.forEach { protoMarshal.writeTag(42).writeMessage(it) }
    if (imports.isNotEmpty()) imports.forEach { protoMarshal.writeTag(50).writeMessage(it) }
    if (unresolved.isNotEmpty()) unresolved.forEach { protoMarshal.writeTag(58).writeMessage(it) }
    if (comments.isNotEmpty()) comments.forEach { protoMarshal.writeTag(66).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun File.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): File {
    var fileName = ""
    var doc: go2k.compile.dumppb.CommentGroup? = null
    var `package` = 0
    var name: go2k.compile.dumppb.Ident? = null
    var decls: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Decl_>? = null
    var imports: pbandk.ListWithSize.Builder<go2k.compile.dumppb.ImportSpec>? = null
    var unresolved: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Ident>? = null
    var comments: pbandk.ListWithSize.Builder<go2k.compile.dumppb.CommentGroup>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return File(fileName, doc, `package`, name,
            pbandk.ListWithSize.Builder.fixed(decls), pbandk.ListWithSize.Builder.fixed(imports), pbandk.ListWithSize.Builder.fixed(unresolved), pbandk.ListWithSize.Builder.fixed(comments), protoUnmarshal.unknownFields())
        10 -> fileName = protoUnmarshal.readString()
        18 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        24 -> `package` = protoUnmarshal.readInt32()
        34 -> name = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        42 -> decls = protoUnmarshal.readRepeatedMessage(decls, go2k.compile.dumppb.Decl_.Companion, true)
        50 -> imports = protoUnmarshal.readRepeatedMessage(imports, go2k.compile.dumppb.ImportSpec.Companion, true)
        58 -> unresolved = protoUnmarshal.readRepeatedMessage(unresolved, go2k.compile.dumppb.Ident.Companion, true)
        66 -> comments = protoUnmarshal.readRepeatedMessage(comments, go2k.compile.dumppb.CommentGroup.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Package.protoMergeImpl(plus: Package?): Package = plus?.copy(
    files = files + plus.files,
    types = types + plus.types,
    varInitOrder = varInitOrder + plus.varInitOrder,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Package.protoSizeImpl(): Int {
    var protoSize = 0
    if (name.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(name)
    if (path.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(path)
    if (files.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(3) * files.size) + files.sumBy(pbandk.Sizer::messageSize)
    if (types.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(4) * types.size) + types.sumBy(pbandk.Sizer::messageSize)
    if (varInitOrder.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(5) * varInitOrder.size) + varInitOrder.sumBy(pbandk.Sizer::stringSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Package.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (name.isNotEmpty()) protoMarshal.writeTag(10).writeString(name)
    if (path.isNotEmpty()) protoMarshal.writeTag(18).writeString(path)
    if (files.isNotEmpty()) files.forEach { protoMarshal.writeTag(26).writeMessage(it) }
    if (types.isNotEmpty()) types.forEach { protoMarshal.writeTag(34).writeMessage(it) }
    if (varInitOrder.isNotEmpty()) varInitOrder.forEach { protoMarshal.writeTag(42).writeString(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Package.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Package {
    var name = ""
    var path = ""
    var files: pbandk.ListWithSize.Builder<go2k.compile.dumppb.File>? = null
    var types: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Type_>? = null
    var varInitOrder: pbandk.ListWithSize.Builder<String>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Package(name, path, pbandk.ListWithSize.Builder.fixed(files), pbandk.ListWithSize.Builder.fixed(types),
            pbandk.ListWithSize.Builder.fixed(varInitOrder), protoUnmarshal.unknownFields())
        10 -> name = protoUnmarshal.readString()
        18 -> path = protoUnmarshal.readString()
        26 -> files = protoUnmarshal.readRepeatedMessage(files, go2k.compile.dumppb.File.Companion, true)
        34 -> types = protoUnmarshal.readRepeatedMessage(types, go2k.compile.dumppb.Type_.Companion, true)
        42 -> varInitOrder = protoUnmarshal.readRepeated(varInitOrder, protoUnmarshal::readString, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Packages.protoMergeImpl(plus: Packages?): Packages = plus?.copy(
    packages = packages + plus.packages,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Packages.protoSizeImpl(): Int {
    var protoSize = 0
    if (packages.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(1) * packages.size) + packages.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Packages.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (packages.isNotEmpty()) packages.forEach { protoMarshal.writeTag(10).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Packages.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Packages {
    var packages: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Package>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Packages(pbandk.ListWithSize.Builder.fixed(packages), protoUnmarshal.unknownFields())
        10 -> packages = protoUnmarshal.readRepeatedMessage(packages, go2k.compile.dumppb.Package.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}
