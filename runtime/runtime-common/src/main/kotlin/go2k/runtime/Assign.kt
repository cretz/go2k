package go2k.runtime

sealed class Assign<R> {
    abstract val rhsFn: () -> R
    abstract fun run(rhs: R)
    class EagerLhs<L, R>(val eagerLhs: L, override val rhsFn: () -> R, val assignFn: (L, R) -> Unit) : Assign<R>() {
        override fun run(rhs: R) { assignFn(eagerLhs, rhs) }
    }
    class Simple<R>(override val rhsFn: () -> R, val assignFn: (R) -> Unit) : Assign<R>() {
        override fun run(rhs: R) { assignFn(rhs) }
    }

    companion object {
        fun <L, R> assign(eagerLhs: L, assignFn: (L, R) -> Unit, rhs: () -> R): Assign<R> =
            Assign.EagerLhs(eagerLhs, rhs, assignFn)

        fun <R> assign(assignFn: (R) -> Unit, rhs: () -> R): Assign<R> =
            Assign.Simple(rhs, assignFn)

        fun multi(vararg assns: Assign<*>) {
            // All LHS's have been eval'd already, now eval rhs's and do assignments
            assns.map { it.rhsFn() }.forEachIndexed { index, rhs -> (assns[index] as Assign<Any?>).run(rhs) }
        }
    }
}