import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

const val BASE_PATH = "Input"

fun Int.toString(minDigits : Int) : String {
    val str = toString()
    return if (str[0] == '-') {
        "-" + "0".repeat(max(0, minDigits - str.length)) + str.substring(1)
    } else {
        "0".repeat(max(0, minDigits - str.length)) + str
    }
}

data class StringRange(val start : String, val end : String, val exclusive : Boolean)

operator fun String.rangeTo(that : String) = StringRange(this, that, false)
operator fun String.rangeUntil(that : String) = StringRange(this, that, true)

operator fun String.get(strRange : StringRange) : String {
    var startIndex = indexOf(strRange.start)
    if (startIndex == -1) {
        return ""
    }
    startIndex += strRange.start.length
    val endIndex = indexOf(strRange.end, startIndex)
    return if (endIndex == -1) {
        substring(startIndex)
    } else if (strRange.exclusive) {
        substring(startIndex, endIndex)
    } else {
        substring(startIndex, endIndex + strRange.end.length)
    }
}

object Input {
    var content : List<String> = listOf()
        set(sl) {
            if (field.isEmpty()) {
                field = sl
            }
        }
    var columns : List<String> = listOf()
        get() = if (field.isEmpty()) {
            val list = MutableList(content.minByOrNull { it.length }?.length ?: 0) {""}
            for (i in content.indices) {
                for (j in content[i].indices) {
                    list[j] = list[j] + content[i][j]
                }
            }
            field = list
            field
        } else {
            field
        }
        set(ignored) {

        }
    val sequence : Sequence<String>
        get() = content.asSequence()

    operator fun invoke(day : Int, testID : Int = 0) : Input {
        if (testID == 0) {
            val path = Path.of(BASE_PATH + day.toString(2) + ".txt")
            if (path.exists()) {
                content = Files.readAllLines(path)
            } else {
                error("File \"${path.name}\" does not exist!")
            }
        } else if (testID == 1) {
            val path = Path.of(BASE_PATH + day.toString(2) + "-Test.txt")
            if (path.exists()) {
                content = Files.readAllLines(path)
            } else {
                error("File \"${path.name}\" does not exist!")
            }
        } else {
            val path = Path.of(BASE_PATH + day.toString(2) + "-Test" + testID + ".txt")
            if (path.exists()) {
                content = Files.readAllLines(path)
            } else {
                error("File \"${path.name}\" does not exist!")
            }
        }
        return this
    }

    operator fun get(lineIndex : Int, emptyLine : () -> String = { "" }) =
        if (lineIndex >= 0 && lineIndex < content.size) content[lineIndex] else emptyLine()

    operator fun get(lineRange : IntRange, emptyLine : () -> String? = { null }) : Sequence<String> {
        val strings = mutableListOf<String>()
        for (i in lineRange.filter {it < 0}) {
            val str = emptyLine()
            if (str != null) {
                strings += str
            }
        }

        for (i in lineRange.filter {it >= 0 && it < content.size}) {
            strings += content[i]
        }

        for (i in lineRange.filter {it >= content.size}) {
            val str = emptyLine()
            if (str != null) {
                strings += str
            }
        }

        return strings.asSequence()
    }

    fun coordinatesOf(c : Char) : List<Coordinate> {
        val coords = mutableListOf<Coordinate>()
        for (i in content.indices) {
            for (j in content[i].indices) {
                if (content[i][j] == c) {
                    coords += Coordinate(j, i)
                }
            }
        }
        return coords
    }
}

data class AOC(val day : Int, val test : Int) {
    var result : Any? = null
        get() = if (field == null) error("No result available yet!") else field
        set(any) {
            field = any
        }
    private var latest : AOC.() -> Unit = {}
    private var secondToLast : AOC.() -> Unit = {}
    private var partCount = 0
    val input = Input(day, test)
    val lines : List<String>
        get() = input.content
    val rows : List<String>
        get() = lines
    val columns : List<String>
        get() = input.columns

    fun part(fn : AOC.() -> Unit) : AOC {
        partCount++
        this.secondToLast = this.latest
        this.latest = fn
        return this
    }

    operator fun unaryPlus() {
        result = null
        latest()
        println("Part $partCount: $result")
    }

    operator fun unaryMinus() {
        partCount--
    }

    operator fun not() {
        latest()
    }

    fun repeatPreviousPart() {
        secondToLast()
    }
}

fun aoc(day : Int, test : Int = 0, fn : AOC.() -> Unit) {
    val aocToday = AOC(day, test)
    aocToday.fn()
}

data class Coordinate(val x : Int, val y : Int) {
    constructor(str : String) : this(str.split(",")[0].trim().toInt(), str.split(",")[1].trim().toInt())
    fun up(amount : Int = 1) = Coordinate(x, y - amount)
    fun left(amount : Int = 1) = Coordinate(x - amount, y)
    fun down(amount : Int = 1) = Coordinate(x, y + amount)
    fun right(amount : Int = 1) = Coordinate(x + amount, y)
    fun move(direction : Direction, amount : Int = 1) = when (direction) {
        Direction.UP -> up(amount)
        Direction.RIGHT -> right(amount)
        Direction.DOWN -> down(amount)
        Direction.LEFT -> left(amount)
    }
    override fun toString() = "($x, $y)"

    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
    operator fun minus(other: Coordinate) = Coordinate(x - other.x, y - other.y)
}

enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    operator fun unaryMinus() =
        when (this) {
            UP -> DOWN
            RIGHT -> LEFT
            DOWN -> UP
            LEFT -> RIGHT
        }

    fun clockwise() =
        when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }

    fun counterClockwise() =
        when (this) {
            UP -> LEFT
            RIGHT -> UP
            DOWN -> RIGHT
            LEFT -> LEFT
        }
}

fun <T, R>Sequence<T>.cross(that : Sequence<R>) = flatMap { first -> that.flatMap { second -> sequenceOf(first to second) } }
fun <T>Sequence<T>.sqr() = this.cross(this)

class Fraction : Comparable<Fraction> {
    val numerator : BigInteger
    val denominator : BigInteger

    constructor(n : BigInteger, d : BigInteger) {
        if (d.signum() == 0) {
            error("Denominator is 0!")
        }
        val gcd = if (n.signum() != 0) n.gcd(d) else d.abs()
        numerator = if (n.signum() == d.signum()) {
            n.abs() / gcd
        } else {
            -(n.abs()) / gcd
        }
        denominator = d.abs() / gcd
    }

    constructor(n : BigInteger) : this(n, BigInteger.ONE)

    operator fun plus(other : Fraction) : Fraction {
        val d = denominator * other.denominator
        return Fraction(((numerator * other.denominator) + (other.numerator * denominator)), d)
    }

    operator fun unaryMinus() = Fraction(-numerator, denominator)

    operator fun minus(other : Fraction) = this + -other

    operator fun times(other : Fraction) = Fraction(numerator * other.numerator, denominator * other.denominator)

    fun reciprocal() : Fraction {
        if (numerator.signum() == 0) {
            error("Cannot invert 0!")
        }
        return Fraction(denominator, numerator)
    }

    operator fun div(other : Fraction) = this * other.reciprocal()

    override fun toString() = "($numerator / $denominator)"

    fun toDouble() = numerator.toDouble() / denominator.toDouble()

    fun signum() = numerator.signum()

    fun abs() = if (signum() == -1) -this else this

    override infix fun compareTo(other: Fraction): Int {
        return if (signum() > other.signum()) {
            1
        } else if (signum() < other.signum()) {
            -1
        } else if (signum() == 0) {
            0
        } else if (signum() == 1) {
            val n = numerator * other.denominator
            val on = other.numerator * denominator
            return n compareTo on
        } else {
            val n = numerator * other.denominator
            val on = other.numerator * denominator
            return -(-n compareTo -on)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fraction

        if (numerator != other.numerator) return false
        return denominator == other.denominator
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

    companion object {
        val ZERO = Fraction(BigInteger.ZERO, BigInteger.ONE)
        val ONE = Fraction(BigInteger.ONE, BigInteger.ONE)
    }
}
infix fun BigInteger.over(that : BigInteger) = Fraction(this, that)
infix fun Long.over(that : Long) = BigInteger.valueOf(this) over BigInteger.valueOf(that)
infix fun Int.over(that : Int) = this.toLong() over that.toLong()
fun BigInteger.toFraction() = Fraction(this)
fun Long.toFraction() = this over 1L
fun Int.toFraction() = this over 1

fun mainDiagonalRight(l: List<CharSequence>, c: Coordinate): String {
    val sb = StringBuilder()
    for (i in 0..min(c.y, l[0].length - c.x - 1)) {
        sb.append(l[c.y - i][c.x + i])
    }
    return sb.toString()
}

fun secondDiagonalRight(l: List<CharSequence>, c: Coordinate): String {
    val sb = StringBuilder()
    for (i in 0..<min(l.size - c.y, l[0].length - c.x)) {
        sb.append(l[c.y + i][c.x + i])
    }
    return sb.toString()
}

fun Iterable<CharSequence>.horizontalMatches(s: String) = map { Regex.fromLiteral(s).findAll(it).toList() }
fun List<CharSequence>.verticalMatches(s: String): List<List<MatchResult>> {
    val list = MutableList(minByOrNull { it.length }?.length ?: 0) {""}
    for (i in indices) {
        for (j in indices) {
            list[j] = list[j] + this[i][j]
        }
    }
    return list.horizontalMatches(s)
}
fun List<CharSequence>.diagonalMatches(s: String, mainDiagonal: Boolean): List<List<MatchResult>> {
    return if (mainDiagonal) {
        List(size) { it }.map { Coordinate(0, it) }.map { mainDiagonalRight(this, it) }.map { Regex.fromLiteral(s).findAll(it) } +
                List(maxOf { it.length } - 1) { it + 1 }.
                map { Coordinate(it, size - 1) }.map { mainDiagonalRight(this, it) }.
                map { Regex.fromLiteral(s).findAll(it) }
    } else {
        List(size) { it }.map { Coordinate(0, it) }.map { secondDiagonalRight(this, it) }.map { Regex.fromLiteral(s).findAll(it) } +
                List(maxOf { it.length } - 1) { it + 1 }.
                map { Coordinate(it, 0) }.map { secondDiagonalRight(this, it) }.
                map { Regex.fromLiteral(s).findAll(it) }
    }.map { it.toList() }
}

fun <I, T: Iterable<I>>Iterable<T>.flatten() = flatMap { it }

fun eea(l1: Long, l2: Long): Triple<Long, Long, Long> {
    var g = l1
    var u = 1L
    var v = 0L
    var g2 = l2
    var u2 = 0L
    var v2 = 1L
    while (g2 != 0L) {
        val q = g / g2
        val tmpg2 = g - q * g2
        val tmpu2 = u - q * u2
        val tmpv2 = v - q * v2
        g = g2
        u = u2
        v = v2
        g2 = tmpg2
        u2 = tmpu2
        v2 = tmpv2
    }
    return Triple(g.absoluteValue, u * g.sign, v * g.sign)
}

fun lcm(l1: Long, l2: Long): Long {
    val (gcd, _, _) = eea(l1, l2)
    return (l1.absoluteValue / gcd) * (l2.absoluteValue / gcd)
}

fun diophantineEquation(a: Long, b: Long, c: Long): Optional<Pair<Pair<Long, Long>, Pair<Long, Long>>> {
    val (gcd, u, v) = eea(a, b)
    val u2 = b / gcd
    val v2 = -a / gcd
    val q = c / gcd
    return if (q * gcd != c) {
        Optional.empty()
    } else {
        Optional.of((q * u to q * v) to (u2 to v2))
    }
}
