import kotlin.math.min

const val REF = "XMAS"

fun diagonal1Right(l: List<String>, c: Coordinate): String {
    val sb = StringBuilder()
    for (i in 0..min(c.y, l[0].length - c.x - 1)) {
        sb.append(l[c.y - i][c.x + i])
    }
    return sb.toString()
}

fun diagonal2Right(l: List<String>, c: Coordinate): String {
    val sb = StringBuilder()
    for (i in 0..<min(l.size - c.y, l[0].length - c.x)) {
        sb.append(l[c.y + i][c.x + i])
    }
    return sb.toString()
}

fun getAllXs(l: List<String>): List<List<Char>> {
    val xs: MutableList<List<Char>> = mutableListOf()
    for (i in 1..<l.size-1) {
        for (j in 1..<l[i].length-1) {
            xs += listOf(l[i - 1][j - 1], l[i - 1][j + 1], l[i][j], l[i + 1][j - 1], l[i + 1][j + 1])
        }
    }
    return xs
}

fun main() {
    aoc(4) {
        +part {
            var count = lines.flatMap { Regex.fromLiteral(REF).findAll(it) }.count()
            count += lines.flatMap { Regex.fromLiteral(REF.reversed()).findAll(it) }.count()
            count += columns.flatMap { Regex.fromLiteral(REF).findAll(it) }.count()
            count += columns.flatMap { Regex.fromLiteral(REF.reversed()).findAll(it) }.count()
            count += List(lines.size) { it }.
                map { Coordinate(0, it) }.map { diagonal1Right(lines, it) }.flatMap { Regex.fromLiteral(REF).findAll(it) }.count()
            count += List(lines.size) { it }.
            map { Coordinate(it, lines.size - 1) }.filterNot { it == Coordinate(0, lines.size - 1) }.map { diagonal1Right(lines, it) }.
                flatMap { Regex.fromLiteral(REF).findAll(it) }.count()
            count += List(lines.size) { it }.
            map { Coordinate(0, it) }.map { diagonal1Right(lines, it) }.flatMap { Regex.fromLiteral(REF.reversed()).findAll(it) }.count()
            count += List(lines.size) { it }.
            map { Coordinate(it,lines.size - 1) }.filterNot { it == Coordinate(0, lines.size - 1) }.map { diagonal1Right(lines, it) }.
                flatMap { Regex.fromLiteral(REF.reversed()).findAll(it) }.count()
            count += List(lines[0].length) { it }.
            map { Coordinate(it, 0) }.map { diagonal2Right(lines, it) }.flatMap { Regex.fromLiteral(REF).findAll(it) }.count()
            count += List(lines[0].length) { it }.
            map { Coordinate(0, it) }.filterNot { it == Coordinate(0, 0) }.map { diagonal2Right(lines, it) }.
                flatMap { Regex.fromLiteral(REF).findAll(it) }.count()
            count += List(lines[0].length) { it }.
            map { Coordinate(it, 0) }.map { diagonal2Right(lines, it) }.flatMap { Regex.fromLiteral(REF.reversed()).findAll(it) }.count()
            count += List(lines[0].length) { it }.
            map { Coordinate(0, it) }.filterNot { it == Coordinate(0, 0) }.map { diagonal2Right(lines, it) }.
                flatMap { Regex.fromLiteral(REF.reversed()).findAll(it) }.count()
            result = count
        }

        +part {
            val allMass = getAllXs(lines).filterNot { it.contains('X') }.map { listOf(
                buildString {
                    append(it[0])
                    append(it[2])
                    append(it[4])
                },
                buildString {
                    append(it[1])
                    append(it[2])
                    append(it[3])
                }
            ) }

            result = allMass.count { x -> x.all { Regex.fromLiteral("MAS").matchEntire(it) != null ||
                        Regex.fromLiteral("SAM").matchEntire(it) != null } }
        }
    }
}
