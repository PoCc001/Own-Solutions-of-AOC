import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.math.max

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
            val list = MutableList(content.minBy { it.length }?.length ?: 0) {""}
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
            val path = Path.of(day.toString(2) + ".txt")
            if (path.exists()) {
                content = Files.readAllLines(path)
            } else {
                error("File \"${path.name}\" does not exist!")
            }
        } else if (testID == 1) {
            val path = Path.of(day.toString(2) + "-Test.txt")
            if (path.exists()) {
                content = Files.readAllLines(path)
            } else {
                error("File \"${path.name}\" does not exist!")
            }
        } else {
            val path = Path.of(day.toString(2) + "-Test" + testID + ".txt")
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
}

enum class Direction {
    UP, RIGHT, DOWN, LEFT
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
}
infix fun BigInteger.over(that : BigInteger) = Fraction(this, that)
infix fun Long.over(that : Long) = BigInteger.valueOf(this) over BigInteger.valueOf(that)
infix fun Int.over(that : Int) = this.toLong() over that.toLong()
fun BigInteger.toFraction() = Fraction(this)
fun Long.toFraction() = this over 1L
fun Int.toFraction() = this over 1
