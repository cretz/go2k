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
    val type: pbandk.wkt.Any? = null,
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

data class BadExpr(
    val from: Int = 0,
    val to: Int = 0,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
    val defTypeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val elt: pbandk.wkt.Any? = null,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val type: pbandk.wkt.Any? = null,
    val lbrace: Int = 0,
    val elts: List<pbandk.wkt.Any> = emptyList(),
    val rbrace: Int = 0,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val x: pbandk.wkt.Any? = null,
    val rparen: Int = 0,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val x: pbandk.wkt.Any? = null,
    val sel: go2k.compile.dumppb.Ident? = null,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val x: pbandk.wkt.Any? = null,
    val lbrack: Int = 0,
    val index: pbandk.wkt.Any? = null,
    val rbrack: Int = 0,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val x: pbandk.wkt.Any? = null,
    val lbrack: Int = 0,
    val low: pbandk.wkt.Any? = null,
    val high: pbandk.wkt.Any? = null,
    val max: pbandk.wkt.Any? = null,
    val slice3: Boolean = false,
    val rbrack: Int = 0,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val x: pbandk.wkt.Any? = null,
    val lparen: Int = 0,
    val type: pbandk.wkt.Any? = null,
    val rparen: Int = 0,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val `fun`: pbandk.wkt.Any? = null,
    val lparen: Int = 0,
    val args: List<pbandk.wkt.Any> = emptyList(),
    val ellipsis: Int = 0,
    val rparen: Int = 0,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val x: pbandk.wkt.Any? = null,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val x: pbandk.wkt.Any? = null,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val x: pbandk.wkt.Any? = null,
    val opPos: Int = 0,
    val op: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val y: pbandk.wkt.Any? = null,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val key: pbandk.wkt.Any? = null,
    val colon: Int = 0,
    val value: pbandk.wkt.Any? = null,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val len: pbandk.wkt.Any? = null,
    val elt: pbandk.wkt.Any? = null,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val key: pbandk.wkt.Any? = null,
    val value: pbandk.wkt.Any? = null,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
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
    val value: pbandk.wkt.Any? = null,
    val typeInfo: go2k.compile.dumppb.TypeInfo? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<ChanType> {
    override operator fun plus(other: ChanType?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<ChanType> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = ChanType.protoUnmarshalImpl(u)
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
    val decl: pbandk.wkt.Any? = null,
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
    val stmt: pbandk.wkt.Any? = null,
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
    val x: pbandk.wkt.Any? = null,
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
    val chan: pbandk.wkt.Any? = null,
    val arrow: Int = 0,
    val value: pbandk.wkt.Any? = null,
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
    val x: pbandk.wkt.Any? = null,
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
    val lhs: List<pbandk.wkt.Any> = emptyList(),
    val tokPos: Int = 0,
    val tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val rhs: List<pbandk.wkt.Any> = emptyList(),
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
    val results: List<pbandk.wkt.Any> = emptyList(),
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
    val list: List<pbandk.wkt.Any> = emptyList(),
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
    val init: pbandk.wkt.Any? = null,
    val cond: pbandk.wkt.Any? = null,
    val body: go2k.compile.dumppb.BlockStmt? = null,
    val `else`: pbandk.wkt.Any? = null,
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
    val list: List<pbandk.wkt.Any> = emptyList(),
    val colon: Int = 0,
    val body: List<pbandk.wkt.Any> = emptyList(),
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
    val init: pbandk.wkt.Any? = null,
    val tag: pbandk.wkt.Any? = null,
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
    val init: pbandk.wkt.Any? = null,
    val assign: pbandk.wkt.Any? = null,
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
    val comm: pbandk.wkt.Any? = null,
    val colon: Int = 0,
    val body: List<pbandk.wkt.Any> = emptyList(),
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
    val init: pbandk.wkt.Any? = null,
    val cond: pbandk.wkt.Any? = null,
    val post: pbandk.wkt.Any? = null,
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
    val key: pbandk.wkt.Any? = null,
    val value: pbandk.wkt.Any? = null,
    val tokPos: Int = 0,
    val tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0),
    val x: pbandk.wkt.Any? = null,
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
    val type: pbandk.wkt.Any? = null,
    val values: List<pbandk.wkt.Any> = emptyList(),
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
    val type: pbandk.wkt.Any? = null,
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
    val specs: List<pbandk.wkt.Any> = emptyList(),
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
    val doc: go2k.compile.dumppb.CommentGroup? = null,
    val `package`: Int = 0,
    val name: go2k.compile.dumppb.Ident? = null,
    val decls: List<pbandk.wkt.Any> = emptyList(),
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
    val files: Map<String, go2k.compile.dumppb.File?> = emptyMap(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Package> {
    override operator fun plus(other: Package?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Package> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Package.protoUnmarshalImpl(u)
    }

    data class FilesEntry(
        override val key: String = "",
        override val value: go2k.compile.dumppb.File? = null,
        val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
    ) : pbandk.Message<FilesEntry>, Map.Entry<String, go2k.compile.dumppb.File?> {
        override operator fun plus(other: FilesEntry?) = protoMergeImpl(other)
        override val protoSize by lazy { protoSizeImpl() }
        override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
        companion object : pbandk.Message.Companion<FilesEntry> {
            override fun protoUnmarshal(u: pbandk.Unmarshaller) = FilesEntry.protoUnmarshalImpl(u)
        }
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
    var type: pbandk.wkt.Any? = null
    var tag: go2k.compile.dumppb.BasicLit? = null
    var comment: go2k.compile.dumppb.CommentGroup? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Field(doc, pbandk.ListWithSize.Builder.fixed(names), type, tag,
            comment, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        18 -> names = protoUnmarshal.readRepeatedMessage(names, go2k.compile.dumppb.Ident.Companion, true)
        26 -> type = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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

private fun BadExpr.protoMergeImpl(plus: BadExpr?): BadExpr = plus?.copy(
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BadExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (from != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(from)
    if (to != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(to)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BadExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (from != 0) protoMarshal.writeTag(8).writeInt32(from)
    if (to != 0) protoMarshal.writeTag(16).writeInt32(to)
    if (typeInfo != null) protoMarshal.writeTag(26).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BadExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BadExpr {
    var from = 0
    var to = 0
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BadExpr(from, to, typeInfo, protoUnmarshal.unknownFields())
        8 -> from = protoUnmarshal.readInt32()
        16 -> to = protoUnmarshal.readInt32()
        26 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Ident.protoMergeImpl(plus: Ident?): Ident = plus?.copy(
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    defTypeInfo = defTypeInfo?.plus(plus.defTypeInfo) ?: plus.defTypeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Ident.protoSizeImpl(): Int {
    var protoSize = 0
    if (namePos != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(namePos)
    if (name.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(name)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeInfo)
    if (defTypeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(defTypeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Ident.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (namePos != 0) protoMarshal.writeTag(8).writeInt32(namePos)
    if (name.isNotEmpty()) protoMarshal.writeTag(18).writeString(name)
    if (typeInfo != null) protoMarshal.writeTag(26).writeMessage(typeInfo)
    if (defTypeInfo != null) protoMarshal.writeTag(34).writeMessage(defTypeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Ident.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Ident {
    var namePos = 0
    var name = ""
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    var defTypeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Ident(namePos, name, typeInfo, defTypeInfo, protoUnmarshal.unknownFields())
        8 -> namePos = protoUnmarshal.readInt32()
        18 -> name = protoUnmarshal.readString()
        26 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        34 -> defTypeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Ellipsis.protoMergeImpl(plus: Ellipsis?): Ellipsis = plus?.copy(
    elt = elt?.plus(plus.elt) ?: plus.elt,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Ellipsis.protoSizeImpl(): Int {
    var protoSize = 0
    if (ellipsis != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(ellipsis)
    if (elt != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(elt)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Ellipsis.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (ellipsis != 0) protoMarshal.writeTag(8).writeInt32(ellipsis)
    if (elt != null) protoMarshal.writeTag(18).writeMessage(elt)
    if (typeInfo != null) protoMarshal.writeTag(26).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Ellipsis.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Ellipsis {
    var ellipsis = 0
    var elt: pbandk.wkt.Any? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Ellipsis(ellipsis, elt, typeInfo, protoUnmarshal.unknownFields())
        8 -> ellipsis = protoUnmarshal.readInt32()
        18 -> elt = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        26 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun BasicLit.protoMergeImpl(plus: BasicLit?): BasicLit = plus?.copy(
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BasicLit.protoSizeImpl(): Int {
    var protoSize = 0
    if (valuePos != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(valuePos)
    if (kind.value != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(kind)
    if (value.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(value)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BasicLit.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (valuePos != 0) protoMarshal.writeTag(8).writeInt32(valuePos)
    if (kind.value != 0) protoMarshal.writeTag(16).writeEnum(kind)
    if (value.isNotEmpty()) protoMarshal.writeTag(26).writeString(value)
    if (typeInfo != null) protoMarshal.writeTag(34).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BasicLit.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BasicLit {
    var valuePos = 0
    var kind: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var value = ""
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BasicLit(valuePos, kind, value, typeInfo, protoUnmarshal.unknownFields())
        8 -> valuePos = protoUnmarshal.readInt32()
        16 -> kind = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        26 -> value = protoUnmarshal.readString()
        34 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun FuncLit.protoMergeImpl(plus: FuncLit?): FuncLit = plus?.copy(
    type = type?.plus(plus.type) ?: plus.type,
    body = body?.plus(plus.body) ?: plus.body,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun FuncLit.protoSizeImpl(): Int {
    var protoSize = 0
    if (type != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(type)
    if (body != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(body)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun FuncLit.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (type != null) protoMarshal.writeTag(10).writeMessage(type)
    if (body != null) protoMarshal.writeTag(18).writeMessage(body)
    if (typeInfo != null) protoMarshal.writeTag(26).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun FuncLit.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): FuncLit {
    var type: go2k.compile.dumppb.FuncType? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return FuncLit(type, body, typeInfo, protoUnmarshal.unknownFields())
        10 -> type = protoUnmarshal.readMessage(go2k.compile.dumppb.FuncType.Companion)
        18 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        26 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun CompositeLit.protoMergeImpl(plus: CompositeLit?): CompositeLit = plus?.copy(
    type = type?.plus(plus.type) ?: plus.type,
    elts = elts + plus.elts,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun CompositeLit.protoSizeImpl(): Int {
    var protoSize = 0
    if (type != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(type)
    if (lbrace != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(lbrace)
    if (elts.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(3) * elts.size) + elts.sumBy(pbandk.Sizer::messageSize)
    if (rbrace != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(rbrace)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun CompositeLit.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (type != null) protoMarshal.writeTag(10).writeMessage(type)
    if (lbrace != 0) protoMarshal.writeTag(16).writeInt32(lbrace)
    if (elts.isNotEmpty()) elts.forEach { protoMarshal.writeTag(26).writeMessage(it) }
    if (rbrace != 0) protoMarshal.writeTag(32).writeInt32(rbrace)
    if (typeInfo != null) protoMarshal.writeTag(42).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun CompositeLit.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): CompositeLit {
    var type: pbandk.wkt.Any? = null
    var lbrace = 0
    var elts: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    var rbrace = 0
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CompositeLit(type, lbrace, pbandk.ListWithSize.Builder.fixed(elts), rbrace,
            typeInfo, protoUnmarshal.unknownFields())
        10 -> type = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        16 -> lbrace = protoUnmarshal.readInt32()
        26 -> elts = protoUnmarshal.readRepeatedMessage(elts, pbandk.wkt.Any.Companion, true)
        32 -> rbrace = protoUnmarshal.readInt32()
        42 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun ParenExpr.protoMergeImpl(plus: ParenExpr?): ParenExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ParenExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (lparen != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(lparen)
    if (x != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(x)
    if (rparen != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.int32Size(rparen)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ParenExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (lparen != 0) protoMarshal.writeTag(8).writeInt32(lparen)
    if (x != null) protoMarshal.writeTag(18).writeMessage(x)
    if (rparen != 0) protoMarshal.writeTag(24).writeInt32(rparen)
    if (typeInfo != null) protoMarshal.writeTag(34).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ParenExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ParenExpr {
    var lparen = 0
    var x: pbandk.wkt.Any? = null
    var rparen = 0
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ParenExpr(lparen, x, rparen, typeInfo, protoUnmarshal.unknownFields())
        8 -> lparen = protoUnmarshal.readInt32()
        18 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        24 -> rparen = protoUnmarshal.readInt32()
        34 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun SelectorExpr.protoMergeImpl(plus: SelectorExpr?): SelectorExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    sel = sel?.plus(plus.sel) ?: plus.sel,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun SelectorExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (sel != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(sel)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun SelectorExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (sel != null) protoMarshal.writeTag(18).writeMessage(sel)
    if (typeInfo != null) protoMarshal.writeTag(26).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun SelectorExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): SelectorExpr {
    var x: pbandk.wkt.Any? = null
    var sel: go2k.compile.dumppb.Ident? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return SelectorExpr(x, sel, typeInfo, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        18 -> sel = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        26 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun IndexExpr.protoMergeImpl(plus: IndexExpr?): IndexExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    index = index?.plus(plus.index) ?: plus.index,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun IndexExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (lbrack != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(lbrack)
    if (index != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(index)
    if (rbrack != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(rbrack)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun IndexExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (lbrack != 0) protoMarshal.writeTag(16).writeInt32(lbrack)
    if (index != null) protoMarshal.writeTag(26).writeMessage(index)
    if (rbrack != 0) protoMarshal.writeTag(32).writeInt32(rbrack)
    if (typeInfo != null) protoMarshal.writeTag(42).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun IndexExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): IndexExpr {
    var x: pbandk.wkt.Any? = null
    var lbrack = 0
    var index: pbandk.wkt.Any? = null
    var rbrack = 0
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return IndexExpr(x, lbrack, index, rbrack,
            typeInfo, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        16 -> lbrack = protoUnmarshal.readInt32()
        26 -> index = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        32 -> rbrack = protoUnmarshal.readInt32()
        42 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun SliceExpr.protoMergeImpl(plus: SliceExpr?): SliceExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    low = low?.plus(plus.low) ?: plus.low,
    high = high?.plus(plus.high) ?: plus.high,
    max = max?.plus(plus.max) ?: plus.max,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
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
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(8) + pbandk.Sizer.messageSize(typeInfo)
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
    if (typeInfo != null) protoMarshal.writeTag(66).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun SliceExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): SliceExpr {
    var x: pbandk.wkt.Any? = null
    var lbrack = 0
    var low: pbandk.wkt.Any? = null
    var high: pbandk.wkt.Any? = null
    var max: pbandk.wkt.Any? = null
    var slice3 = false
    var rbrack = 0
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return SliceExpr(x, lbrack, low, high,
            max, slice3, rbrack, typeInfo, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        16 -> lbrack = protoUnmarshal.readInt32()
        26 -> low = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        34 -> high = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        42 -> max = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        48 -> slice3 = protoUnmarshal.readBool()
        56 -> rbrack = protoUnmarshal.readInt32()
        66 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun TypeAssertExpr.protoMergeImpl(plus: TypeAssertExpr?): TypeAssertExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    type = type?.plus(plus.type) ?: plus.type,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun TypeAssertExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (lparen != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(lparen)
    if (type != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(type)
    if (rparen != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(rparen)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun TypeAssertExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (lparen != 0) protoMarshal.writeTag(16).writeInt32(lparen)
    if (type != null) protoMarshal.writeTag(26).writeMessage(type)
    if (rparen != 0) protoMarshal.writeTag(32).writeInt32(rparen)
    if (typeInfo != null) protoMarshal.writeTag(42).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun TypeAssertExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): TypeAssertExpr {
    var x: pbandk.wkt.Any? = null
    var lparen = 0
    var type: pbandk.wkt.Any? = null
    var rparen = 0
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeAssertExpr(x, lparen, type, rparen,
            typeInfo, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        16 -> lparen = protoUnmarshal.readInt32()
        26 -> type = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        32 -> rparen = protoUnmarshal.readInt32()
        42 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun CallExpr.protoMergeImpl(plus: CallExpr?): CallExpr = plus?.copy(
    `fun` = `fun`?.plus(plus.`fun`) ?: plus.`fun`,
    args = args + plus.args,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun CallExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (`fun` != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(`fun`)
    if (lparen != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(lparen)
    if (args.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(3) * args.size) + args.sumBy(pbandk.Sizer::messageSize)
    if (ellipsis != 0) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.int32Size(ellipsis)
    if (rparen != 0) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.int32Size(rparen)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun CallExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (`fun` != null) protoMarshal.writeTag(10).writeMessage(`fun`)
    if (lparen != 0) protoMarshal.writeTag(16).writeInt32(lparen)
    if (args.isNotEmpty()) args.forEach { protoMarshal.writeTag(26).writeMessage(it) }
    if (ellipsis != 0) protoMarshal.writeTag(32).writeInt32(ellipsis)
    if (rparen != 0) protoMarshal.writeTag(40).writeInt32(rparen)
    if (typeInfo != null) protoMarshal.writeTag(50).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun CallExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): CallExpr {
    var `fun`: pbandk.wkt.Any? = null
    var lparen = 0
    var args: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    var ellipsis = 0
    var rparen = 0
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CallExpr(`fun`, lparen, pbandk.ListWithSize.Builder.fixed(args), ellipsis,
            rparen, typeInfo, protoUnmarshal.unknownFields())
        10 -> `fun` = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        16 -> lparen = protoUnmarshal.readInt32()
        26 -> args = protoUnmarshal.readRepeatedMessage(args, pbandk.wkt.Any.Companion, true)
        32 -> ellipsis = protoUnmarshal.readInt32()
        40 -> rparen = protoUnmarshal.readInt32()
        50 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun StarExpr.protoMergeImpl(plus: StarExpr?): StarExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun StarExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (star != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(star)
    if (x != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(x)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun StarExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (star != 0) protoMarshal.writeTag(8).writeInt32(star)
    if (x != null) protoMarshal.writeTag(18).writeMessage(x)
    if (typeInfo != null) protoMarshal.writeTag(26).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun StarExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): StarExpr {
    var star = 0
    var x: pbandk.wkt.Any? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return StarExpr(star, x, typeInfo, protoUnmarshal.unknownFields())
        8 -> star = protoUnmarshal.readInt32()
        18 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        26 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun UnaryExpr.protoMergeImpl(plus: UnaryExpr?): UnaryExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun UnaryExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (opPos != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(opPos)
    if (op.value != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(op)
    if (x != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(x)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun UnaryExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (opPos != 0) protoMarshal.writeTag(8).writeInt32(opPos)
    if (op.value != 0) protoMarshal.writeTag(16).writeEnum(op)
    if (x != null) protoMarshal.writeTag(26).writeMessage(x)
    if (typeInfo != null) protoMarshal.writeTag(34).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun UnaryExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): UnaryExpr {
    var opPos = 0
    var op: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var x: pbandk.wkt.Any? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return UnaryExpr(opPos, op, x, typeInfo, protoUnmarshal.unknownFields())
        8 -> opPos = protoUnmarshal.readInt32()
        16 -> op = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        26 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        34 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun BinaryExpr.protoMergeImpl(plus: BinaryExpr?): BinaryExpr = plus?.copy(
    x = x?.plus(plus.x) ?: plus.x,
    y = y?.plus(plus.y) ?: plus.y,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun BinaryExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (x != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(x)
    if (opPos != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(opPos)
    if (op.value != 0) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.enumSize(op)
    if (y != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(y)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun BinaryExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (x != null) protoMarshal.writeTag(10).writeMessage(x)
    if (opPos != 0) protoMarshal.writeTag(16).writeInt32(opPos)
    if (op.value != 0) protoMarshal.writeTag(24).writeEnum(op)
    if (y != null) protoMarshal.writeTag(34).writeMessage(y)
    if (typeInfo != null) protoMarshal.writeTag(42).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun BinaryExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): BinaryExpr {
    var x: pbandk.wkt.Any? = null
    var opPos = 0
    var op: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var y: pbandk.wkt.Any? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BinaryExpr(x, opPos, op, y,
            typeInfo, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        16 -> opPos = protoUnmarshal.readInt32()
        24 -> op = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        34 -> y = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        42 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun KeyValueExpr.protoMergeImpl(plus: KeyValueExpr?): KeyValueExpr = plus?.copy(
    key = key?.plus(plus.key) ?: plus.key,
    value = value?.plus(plus.value) ?: plus.value,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun KeyValueExpr.protoSizeImpl(): Int {
    var protoSize = 0
    if (key != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(key)
    if (colon != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(colon)
    if (value != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(value)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun KeyValueExpr.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (key != null) protoMarshal.writeTag(10).writeMessage(key)
    if (colon != 0) protoMarshal.writeTag(16).writeInt32(colon)
    if (value != null) protoMarshal.writeTag(26).writeMessage(value)
    if (typeInfo != null) protoMarshal.writeTag(34).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun KeyValueExpr.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): KeyValueExpr {
    var key: pbandk.wkt.Any? = null
    var colon = 0
    var value: pbandk.wkt.Any? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return KeyValueExpr(key, colon, value, typeInfo, protoUnmarshal.unknownFields())
        10 -> key = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        16 -> colon = protoUnmarshal.readInt32()
        26 -> value = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        34 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun ArrayType.protoMergeImpl(plus: ArrayType?): ArrayType = plus?.copy(
    len = len?.plus(plus.len) ?: plus.len,
    elt = elt?.plus(plus.elt) ?: plus.elt,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ArrayType.protoSizeImpl(): Int {
    var protoSize = 0
    if (lbrack != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(lbrack)
    if (len != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(len)
    if (elt != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(elt)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ArrayType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (lbrack != 0) protoMarshal.writeTag(8).writeInt32(lbrack)
    if (len != null) protoMarshal.writeTag(18).writeMessage(len)
    if (elt != null) protoMarshal.writeTag(26).writeMessage(elt)
    if (typeInfo != null) protoMarshal.writeTag(34).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ArrayType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ArrayType {
    var lbrack = 0
    var len: pbandk.wkt.Any? = null
    var elt: pbandk.wkt.Any? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ArrayType(lbrack, len, elt, typeInfo, protoUnmarshal.unknownFields())
        8 -> lbrack = protoUnmarshal.readInt32()
        18 -> len = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        26 -> elt = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        34 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun StructType.protoMergeImpl(plus: StructType?): StructType = plus?.copy(
    fields = fields?.plus(plus.fields) ?: plus.fields,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun StructType.protoSizeImpl(): Int {
    var protoSize = 0
    if (struct != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(struct)
    if (fields != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(fields)
    if (incomplete) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.boolSize(incomplete)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun StructType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (struct != 0) protoMarshal.writeTag(8).writeInt32(struct)
    if (fields != null) protoMarshal.writeTag(18).writeMessage(fields)
    if (incomplete) protoMarshal.writeTag(24).writeBool(incomplete)
    if (typeInfo != null) protoMarshal.writeTag(34).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun StructType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): StructType {
    var struct = 0
    var fields: go2k.compile.dumppb.FieldList? = null
    var incomplete = false
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return StructType(struct, fields, incomplete, typeInfo, protoUnmarshal.unknownFields())
        8 -> struct = protoUnmarshal.readInt32()
        18 -> fields = protoUnmarshal.readMessage(go2k.compile.dumppb.FieldList.Companion)
        24 -> incomplete = protoUnmarshal.readBool()
        34 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun FuncType.protoMergeImpl(plus: FuncType?): FuncType = plus?.copy(
    params = params?.plus(plus.params) ?: plus.params,
    results = results?.plus(plus.results) ?: plus.results,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun FuncType.protoSizeImpl(): Int {
    var protoSize = 0
    if (func != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(func)
    if (params != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(params)
    if (results != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(results)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun FuncType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (func != 0) protoMarshal.writeTag(8).writeInt32(func)
    if (params != null) protoMarshal.writeTag(18).writeMessage(params)
    if (results != null) protoMarshal.writeTag(26).writeMessage(results)
    if (typeInfo != null) protoMarshal.writeTag(34).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun FuncType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): FuncType {
    var func = 0
    var params: go2k.compile.dumppb.FieldList? = null
    var results: go2k.compile.dumppb.FieldList? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return FuncType(func, params, results, typeInfo, protoUnmarshal.unknownFields())
        8 -> func = protoUnmarshal.readInt32()
        18 -> params = protoUnmarshal.readMessage(go2k.compile.dumppb.FieldList.Companion)
        26 -> results = protoUnmarshal.readMessage(go2k.compile.dumppb.FieldList.Companion)
        34 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun InterfaceType.protoMergeImpl(plus: InterfaceType?): InterfaceType = plus?.copy(
    methods = methods?.plus(plus.methods) ?: plus.methods,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun InterfaceType.protoSizeImpl(): Int {
    var protoSize = 0
    if (`interface` != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(`interface`)
    if (methods != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(methods)
    if (incomplete) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.boolSize(incomplete)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun InterfaceType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (`interface` != 0) protoMarshal.writeTag(8).writeInt32(`interface`)
    if (methods != null) protoMarshal.writeTag(18).writeMessage(methods)
    if (incomplete) protoMarshal.writeTag(24).writeBool(incomplete)
    if (typeInfo != null) protoMarshal.writeTag(34).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun InterfaceType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): InterfaceType {
    var `interface` = 0
    var methods: go2k.compile.dumppb.FieldList? = null
    var incomplete = false
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return InterfaceType(`interface`, methods, incomplete, typeInfo, protoUnmarshal.unknownFields())
        8 -> `interface` = protoUnmarshal.readInt32()
        18 -> methods = protoUnmarshal.readMessage(go2k.compile.dumppb.FieldList.Companion)
        24 -> incomplete = protoUnmarshal.readBool()
        34 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun MapType.protoMergeImpl(plus: MapType?): MapType = plus?.copy(
    key = key?.plus(plus.key) ?: plus.key,
    value = value?.plus(plus.value) ?: plus.value,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun MapType.protoSizeImpl(): Int {
    var protoSize = 0
    if (map != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(map)
    if (key != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(key)
    if (value != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(value)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun MapType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (map != 0) protoMarshal.writeTag(8).writeInt32(map)
    if (key != null) protoMarshal.writeTag(18).writeMessage(key)
    if (value != null) protoMarshal.writeTag(26).writeMessage(value)
    if (typeInfo != null) protoMarshal.writeTag(34).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun MapType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): MapType {
    var map = 0
    var key: pbandk.wkt.Any? = null
    var value: pbandk.wkt.Any? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return MapType(map, key, value, typeInfo, protoUnmarshal.unknownFields())
        8 -> map = protoUnmarshal.readInt32()
        18 -> key = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        26 -> value = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        34 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun ChanType.protoMergeImpl(plus: ChanType?): ChanType = plus?.copy(
    value = value?.plus(plus.value) ?: plus.value,
    typeInfo = typeInfo?.plus(plus.typeInfo) ?: plus.typeInfo,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun ChanType.protoSizeImpl(): Int {
    var protoSize = 0
    if (begin != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.int32Size(begin)
    if (arrow != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(arrow)
    if (sendDir) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.boolSize(sendDir)
    if (recvDir) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.boolSize(recvDir)
    if (value != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(value)
    if (typeInfo != null) protoSize += pbandk.Sizer.tagSize(6) + pbandk.Sizer.messageSize(typeInfo)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun ChanType.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (begin != 0) protoMarshal.writeTag(8).writeInt32(begin)
    if (arrow != 0) protoMarshal.writeTag(16).writeInt32(arrow)
    if (sendDir) protoMarshal.writeTag(24).writeBool(sendDir)
    if (recvDir) protoMarshal.writeTag(32).writeBool(recvDir)
    if (value != null) protoMarshal.writeTag(42).writeMessage(value)
    if (typeInfo != null) protoMarshal.writeTag(50).writeMessage(typeInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun ChanType.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): ChanType {
    var begin = 0
    var arrow = 0
    var sendDir = false
    var recvDir = false
    var value: pbandk.wkt.Any? = null
    var typeInfo: go2k.compile.dumppb.TypeInfo? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ChanType(begin, arrow, sendDir, recvDir,
            value, typeInfo, protoUnmarshal.unknownFields())
        8 -> begin = protoUnmarshal.readInt32()
        16 -> arrow = protoUnmarshal.readInt32()
        24 -> sendDir = protoUnmarshal.readBool()
        32 -> recvDir = protoUnmarshal.readBool()
        42 -> value = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        50 -> typeInfo = protoUnmarshal.readMessage(go2k.compile.dumppb.TypeInfo.Companion)
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
    var decl: pbandk.wkt.Any? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return DeclStmt(decl, protoUnmarshal.unknownFields())
        10 -> decl = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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
    var stmt: pbandk.wkt.Any? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return LabeledStmt(label, colon, stmt, protoUnmarshal.unknownFields())
        10 -> label = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        16 -> colon = protoUnmarshal.readInt32()
        26 -> stmt = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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
    var x: pbandk.wkt.Any? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ExprStmt(x, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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
    var chan: pbandk.wkt.Any? = null
    var arrow = 0
    var value: pbandk.wkt.Any? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return SendStmt(chan, arrow, value, protoUnmarshal.unknownFields())
        10 -> chan = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        16 -> arrow = protoUnmarshal.readInt32()
        26 -> value = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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
    var x: pbandk.wkt.Any? = null
    var tokPos = 0
    var tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return IncDecStmt(x, tokPos, tok, protoUnmarshal.unknownFields())
        10 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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
    var lhs: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    var tokPos = 0
    var tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var rhs: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return AssignStmt(pbandk.ListWithSize.Builder.fixed(lhs), tokPos, tok, pbandk.ListWithSize.Builder.fixed(rhs), protoUnmarshal.unknownFields())
        10 -> lhs = protoUnmarshal.readRepeatedMessage(lhs, pbandk.wkt.Any.Companion, true)
        16 -> tokPos = protoUnmarshal.readInt32()
        24 -> tok = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        34 -> rhs = protoUnmarshal.readRepeatedMessage(rhs, pbandk.wkt.Any.Companion, true)
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
    var results: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ReturnStmt(`return`, pbandk.ListWithSize.Builder.fixed(results), protoUnmarshal.unknownFields())
        8 -> `return` = protoUnmarshal.readInt32()
        18 -> results = protoUnmarshal.readRepeatedMessage(results, pbandk.wkt.Any.Companion, true)
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
    var list: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    var rbrace = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return BlockStmt(lbrace, pbandk.ListWithSize.Builder.fixed(list), rbrace, protoUnmarshal.unknownFields())
        8 -> lbrace = protoUnmarshal.readInt32()
        18 -> list = protoUnmarshal.readRepeatedMessage(list, pbandk.wkt.Any.Companion, true)
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
    var init: pbandk.wkt.Any? = null
    var cond: pbandk.wkt.Any? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    var `else`: pbandk.wkt.Any? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return IfStmt(`if`, init, cond, body,
            `else`, protoUnmarshal.unknownFields())
        8 -> `if` = protoUnmarshal.readInt32()
        18 -> init = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        26 -> cond = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        34 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
        42 -> `else` = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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
    var list: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    var colon = 0
    var body: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CaseClause(case, pbandk.ListWithSize.Builder.fixed(list), colon, pbandk.ListWithSize.Builder.fixed(body), protoUnmarshal.unknownFields())
        8 -> case = protoUnmarshal.readInt32()
        18 -> list = protoUnmarshal.readRepeatedMessage(list, pbandk.wkt.Any.Companion, true)
        24 -> colon = protoUnmarshal.readInt32()
        34 -> body = protoUnmarshal.readRepeatedMessage(body, pbandk.wkt.Any.Companion, true)
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
    var init: pbandk.wkt.Any? = null
    var tag: pbandk.wkt.Any? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return SwitchStmt(switch, init, tag, body, protoUnmarshal.unknownFields())
        8 -> switch = protoUnmarshal.readInt32()
        18 -> init = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        26 -> tag = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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
    var init: pbandk.wkt.Any? = null
    var assign: pbandk.wkt.Any? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeSwitchStmt(switch, init, assign, body, protoUnmarshal.unknownFields())
        8 -> switch = protoUnmarshal.readInt32()
        18 -> init = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        26 -> assign = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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
    var comm: pbandk.wkt.Any? = null
    var colon = 0
    var body: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return CommClause(case, comm, colon, pbandk.ListWithSize.Builder.fixed(body), protoUnmarshal.unknownFields())
        8 -> case = protoUnmarshal.readInt32()
        18 -> comm = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        24 -> colon = protoUnmarshal.readInt32()
        34 -> body = protoUnmarshal.readRepeatedMessage(body, pbandk.wkt.Any.Companion, true)
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
    var init: pbandk.wkt.Any? = null
    var cond: pbandk.wkt.Any? = null
    var post: pbandk.wkt.Any? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ForStmt(`for`, init, cond, post,
            body, protoUnmarshal.unknownFields())
        8 -> `for` = protoUnmarshal.readInt32()
        18 -> init = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        26 -> cond = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        34 -> post = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
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
    var key: pbandk.wkt.Any? = null
    var value: pbandk.wkt.Any? = null
    var tokPos = 0
    var tok: go2k.compile.dumppb.Token = go2k.compile.dumppb.Token.fromValue(0)
    var x: pbandk.wkt.Any? = null
    var body: go2k.compile.dumppb.BlockStmt? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return RangeStmt(`for`, key, value, tokPos,
            tok, x, body, protoUnmarshal.unknownFields())
        8 -> `for` = protoUnmarshal.readInt32()
        18 -> key = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        26 -> value = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        32 -> tokPos = protoUnmarshal.readInt32()
        40 -> tok = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        50 -> x = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        58 -> body = protoUnmarshal.readMessage(go2k.compile.dumppb.BlockStmt.Companion)
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
    var type: pbandk.wkt.Any? = null
    var values: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    var comment: go2k.compile.dumppb.CommentGroup? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return ValueSpec(doc, pbandk.ListWithSize.Builder.fixed(names), type, pbandk.ListWithSize.Builder.fixed(values),
            comment, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        18 -> names = protoUnmarshal.readRepeatedMessage(names, go2k.compile.dumppb.Ident.Companion, true)
        26 -> type = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        34 -> values = protoUnmarshal.readRepeatedMessage(values, pbandk.wkt.Any.Companion, true)
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
    var type: pbandk.wkt.Any? = null
    var comment: go2k.compile.dumppb.CommentGroup? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return TypeSpec(doc, name, assign, type,
            comment, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        18 -> name = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        24 -> assign = protoUnmarshal.readInt32()
        34 -> type = protoUnmarshal.readMessage(pbandk.wkt.Any.Companion)
        42 -> comment = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
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
    var specs: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    var rparen = 0
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return GenDecl(doc, tokPos, tok, lparen,
            pbandk.ListWithSize.Builder.fixed(specs), rparen, protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        16 -> tokPos = protoUnmarshal.readInt32()
        24 -> tok = protoUnmarshal.readEnum(go2k.compile.dumppb.Token.Companion)
        32 -> lparen = protoUnmarshal.readInt32()
        42 -> specs = protoUnmarshal.readRepeatedMessage(specs, pbandk.wkt.Any.Companion, true)
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
    if (doc != null) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(doc)
    if (`package` != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(`package`)
    if (name != null) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(name)
    if (decls.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(4) * decls.size) + decls.sumBy(pbandk.Sizer::messageSize)
    if (imports.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(5) * imports.size) + imports.sumBy(pbandk.Sizer::messageSize)
    if (unresolved.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(6) * unresolved.size) + unresolved.sumBy(pbandk.Sizer::messageSize)
    if (comments.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(7) * comments.size) + comments.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun File.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (doc != null) protoMarshal.writeTag(10).writeMessage(doc)
    if (`package` != 0) protoMarshal.writeTag(16).writeInt32(`package`)
    if (name != null) protoMarshal.writeTag(26).writeMessage(name)
    if (decls.isNotEmpty()) decls.forEach { protoMarshal.writeTag(34).writeMessage(it) }
    if (imports.isNotEmpty()) imports.forEach { protoMarshal.writeTag(42).writeMessage(it) }
    if (unresolved.isNotEmpty()) unresolved.forEach { protoMarshal.writeTag(50).writeMessage(it) }
    if (comments.isNotEmpty()) comments.forEach { protoMarshal.writeTag(58).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun File.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): File {
    var doc: go2k.compile.dumppb.CommentGroup? = null
    var `package` = 0
    var name: go2k.compile.dumppb.Ident? = null
    var decls: pbandk.ListWithSize.Builder<pbandk.wkt.Any>? = null
    var imports: pbandk.ListWithSize.Builder<go2k.compile.dumppb.ImportSpec>? = null
    var unresolved: pbandk.ListWithSize.Builder<go2k.compile.dumppb.Ident>? = null
    var comments: pbandk.ListWithSize.Builder<go2k.compile.dumppb.CommentGroup>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return File(doc, `package`, name, pbandk.ListWithSize.Builder.fixed(decls),
            pbandk.ListWithSize.Builder.fixed(imports), pbandk.ListWithSize.Builder.fixed(unresolved), pbandk.ListWithSize.Builder.fixed(comments), protoUnmarshal.unknownFields())
        10 -> doc = protoUnmarshal.readMessage(go2k.compile.dumppb.CommentGroup.Companion)
        16 -> `package` = protoUnmarshal.readInt32()
        26 -> name = protoUnmarshal.readMessage(go2k.compile.dumppb.Ident.Companion)
        34 -> decls = protoUnmarshal.readRepeatedMessage(decls, pbandk.wkt.Any.Companion, true)
        42 -> imports = protoUnmarshal.readRepeatedMessage(imports, go2k.compile.dumppb.ImportSpec.Companion, true)
        50 -> unresolved = protoUnmarshal.readRepeatedMessage(unresolved, go2k.compile.dumppb.Ident.Companion, true)
        58 -> comments = protoUnmarshal.readRepeatedMessage(comments, go2k.compile.dumppb.CommentGroup.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Package.protoMergeImpl(plus: Package?): Package = plus?.copy(
    files = files + plus.files,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Package.protoSizeImpl(): Int {
    var protoSize = 0
    if (name.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(name)
    if (files.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.mapSize(files, go2k.compile.dumppb.Package::FilesEntry)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Package.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (name.isNotEmpty()) protoMarshal.writeTag(10).writeString(name)
    if (files.isNotEmpty()) protoMarshal.writeTag(18).writeMap(files, go2k.compile.dumppb.Package::FilesEntry)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Package.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Package {
    var name = ""
    var files: pbandk.MapWithSize.Builder<String, go2k.compile.dumppb.File?>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Package(name, pbandk.MapWithSize.Builder.fixed(files), protoUnmarshal.unknownFields())
        10 -> name = protoUnmarshal.readString()
        18 -> files = protoUnmarshal.readMap(files, go2k.compile.dumppb.Package.FilesEntry.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Package.FilesEntry.protoMergeImpl(plus: Package.FilesEntry?): Package.FilesEntry = plus?.copy(
    value = value?.plus(plus.value) ?: plus.value,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Package.FilesEntry.protoSizeImpl(): Int {
    var protoSize = 0
    if (key.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(key)
    if (value != null) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(value)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Package.FilesEntry.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (key.isNotEmpty()) protoMarshal.writeTag(10).writeString(key)
    if (value != null) protoMarshal.writeTag(18).writeMessage(value)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Package.FilesEntry.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Package.FilesEntry {
    var key = ""
    var value: go2k.compile.dumppb.File? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Package.FilesEntry(key, value, protoUnmarshal.unknownFields())
        10 -> key = protoUnmarshal.readString()
        18 -> value = protoUnmarshal.readMessage(go2k.compile.dumppb.File.Companion)
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
