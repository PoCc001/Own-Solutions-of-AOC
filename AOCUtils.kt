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
            val path = Path.of("Input" + day.toString(2) + ".txt")
            if (path.exists()) {
                content = Files.readAllLines(path)
            } else {
                error("File \"${path.name}\" does not exist!")
            }
        } else if (testID == 1) {
            val path = Path.of("Input" + day.toString(2) + "-Test.txt")
            if (path.exists()) {
                content = Files.readAllLines(path)
            } else {
                error("File \"${path.name}\" does not exist!")
            }
        } else {
            val path = Path.of("Input" + day.toString(2) + "-Test" + testID + ".txt")
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
    private var fn : AOC.() -> Unit = {}
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
        this.fn = fn
        return this
    }

    operator fun unaryPlus() {
        result = null
        fn()
        println("Result $partCount: $result")
    }

    operator fun unaryMinus() {
        partCount--
    }

    operator fun not() {
        fn()
    }
}

fun aoc(day : Int, test : Int = 0, fn : AOC.() -> Unit) {
    val aocToday = AOC(day, test)
    aocToday.fn()
}

data class Coordinate(val x : Int, val y : Int) {
    constructor(str : String) : this(str.split(",")[0].trim().toInt(), str.split(",")[1].trim().toInt())
    fun up(amount : Int = 1) = Coordinate(x, y - 1)
    fun left(amount : Int = 1) = Coordinate(x - 1, y)
    fun down(amount : Int = 1) = Coordinate(x, y + 1)
    fun right(amount : Int = 1) = Coordinate(x + 1, y)
    override fun toString() = "($x, $y)"
}

enum class Direction {
    UP, RIGHT, DOWN, LEFT
}

fun <T, R>Sequence<T>.cross(that : Sequence<R>) = flatMap { first -> that.flatMap { second -> sequenceOf(first to second) } }
