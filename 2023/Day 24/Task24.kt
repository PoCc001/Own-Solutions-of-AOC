import java.math.BigInteger

enum class ResultKind {
    NO_RESULT, ONE_RESULT, INFINITE_RESULTS
}

fun main() {
    aoc(24) {
        data class Result(val kind : ResultKind, val scalingFactor : Fraction, val point : Pair<Fraction, Fraction>) {
            constructor(k : ResultKind, p : Pair<Fraction, Fraction>) : this(k, 0 over 1, p)
            override fun toString(): String {
                return when (kind) {
                    ResultKind.NO_RESULT -> "No result"
                    ResultKind.ONE_RESULT -> "Result: t = ${scalingFactor.toDouble()} (${point.first.toDouble()}; ${point.second.toDouble()})"
                    ResultKind.INFINITE_RESULTS -> "Infinitely many results"
                }
            }
        }

        data class Snowflake(val x : BigInteger, val y : BigInteger, val z : BigInteger, val dx : BigInteger, val dy : BigInteger, val dz : BigInteger) {
            fun cross(other : Snowflake) : Result {
                if (dy.signum() == 0) {
                    return if (other.dy.signum() == 0) {
                        if (dx == other.dx) Result(ResultKind.INFINITE_RESULTS, (0 over 1) to (0 over 1)) else
                            Result(ResultKind.NO_RESULT, (0 over 1) to (0 over 1))
                    } else {
                        val t = (y - other.y).toFraction() / other.dy.toFraction()
                        Result(ResultKind.ONE_RESULT, t, (other.x.toFraction() + (other.dx.toFraction() * t)) to y.toFraction())
                    }
                } else if (other.dy.signum() == 0) {
                    return if (dy.signum() == 0) {
                        if (dx == other.dx) Result(ResultKind.INFINITE_RESULTS, (0 over 1) to (0 over 1)) else
                            Result(ResultKind.NO_RESULT, (0 over 1) to (0 over 1))
                    } else {
                        val t = (other.y - y).toFraction() / dy.toFraction()
                        Result(ResultKind.ONE_RESULT, t, (x.toFraction() + (dx.toFraction() * t)) to other.y.toFraction())
                    }
                } else if (dx.signum() == 0) {
                    return if (other.dx.signum() == 0) {
                        if (dy == other.dy) Result(ResultKind.INFINITE_RESULTS, (0 over 1) to (0 over 1)) else
                            Result(ResultKind.NO_RESULT, (0 over 1) to (0 over 1))
                    } else {
                        val t = (x - other.x).toFraction() / other.dx.toFraction()
                        Result(ResultKind.ONE_RESULT, t, (x.toFraction()) to (other.y.toFraction() + (other.dy.toFraction() * t)))
                    }
                } else if (other.dx.signum() == 0) {
                    return if (dx.signum() == 0) {
                        if (dy == other.dy) Result(ResultKind.INFINITE_RESULTS, (0 over 1) to (0 over 1)) else
                            Result(ResultKind.NO_RESULT, (0 over 1) to (0 over 1))
                    } else {
                        val t = (other.x - x).toFraction() / dx.toFraction()
                        Result(ResultKind.ONE_RESULT, t, (other.x.toFraction()) to (y.toFraction() + (dy.toFraction() * t)))
                    }
                } else {
                    val fx = x * -dy
                    val fox = other.x * -dy
                    val fodx = other.dx * -dy
                    val fy = y * dx
                    val foy = other.y * dx
                    val fody = other.dy * dx
                    val nx = fx + fy
                    val nox = fox + foy
                    val nodx = fodx + fody
                    return if (nodx.signum() == 0) {
                        if (nx == nox) Result(ResultKind.INFINITE_RESULTS, (0 over 1) to (0 over 1)) else
                            Result(ResultKind.NO_RESULT, (0 over 1) to (0 over 1))
                    } else {
                        val t = (nx - nox).toFraction() / nodx.toFraction()
                        val rx = other.x.toFraction() + (other.dx.toFraction() * t)
                        val ry = other.y.toFraction() + (other.dy.toFraction() * t)
                        Result(ResultKind.ONE_RESULT, t, rx to ry)
                    }
                }
            }
        }
        +part {
            val snowflakes = rows.asSequence().map { it.split("@").map { l -> l.trim().split(", ").map { c -> c.trim() } } }.map {
                Snowflake(it[0][0].toBigInteger(), it[0][1].toBigInteger(), it[0][2].toBigInteger(),
                    it[1][0].toBigInteger(), it[1][1].toBigInteger(), it[1][2].toBigInteger())
            }
            val snowflakePairs = snowflakes.sqr().map { setOf(it.first, it.second) }.distinct().filter { it.size == 2 }.map { it.first() to it.last() }.toList()
            val lowerBound = if (test == 0) 200000000000000L else 7L
            val upperBound = if (test == 0) 400000000000000L else 27L
            result = snowflakePairs.asSequence().map { it.first to it.first.cross(it.second) }.filter { it.second.scalingFactor.signum() >= 0 &&
                    if (it.first.x.toFraction() < it.second.point.first) {
                        it.first.dx.signum() > 0
                    } else if (it.first.x.toFraction() > it.second.point.first) {
                        it.first.dx.signum() < 0
                    } else if (it.first.y.toFraction() < it.second.point.second) {
                        it.first.dy.signum() > 0
                    } else {
                        it.first.dy.signum() < 0
                    }
                    }.map { it.second }.count { it.kind == ResultKind.INFINITE_RESULTS ||
                    it.kind == ResultKind.ONE_RESULT && it.point.first >= lowerBound.toFraction() && it.point.first <= upperBound.toFraction() &&
                    it.point.second >= lowerBound.toFraction() && it.point.second <= upperBound.toFraction() }
        }

        part {

        }
    }
}
