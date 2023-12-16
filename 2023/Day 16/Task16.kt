import kotlin.math.max

data class DirCoordinate(val x : Int, val y : Int, val dir : Direction)

fun toString(rows : Int, cols : Int, energizedSet : Set<DirCoordinate>) : String {
    return buildString {
        for (i in 0..<rows) {
            for (j in 0..<cols) {
                val c = DirCoordinate(j, i, Direction.DOWN)
                if (energizedSet.filter {it.x == c.x && it.y == c.y}.isNotEmpty()) {
                    append('#')
                } else {
                    append('.')
                }
            }
            append('\n')
        }
    }
}

fun evaluate(startCoord : DirCoordinate, rows : List<String>, columns : List<String>) : Set<DirCoordinate> {
    val energizedSet = mutableSetOf<DirCoordinate>()
    val waitingToBeEnergizedList = mutableListOf<DirCoordinate>()
    waitingToBeEnergizedList += startCoord
    while (waitingToBeEnergizedList.isNotEmpty()) {
        val coord = waitingToBeEnergizedList.removeAt(waitingToBeEnergizedList.lastIndex)
        if (coord.x < 0 || coord.y < 0 || coord.x >= columns.size || coord.y >= rows.size) {
            continue
        }
        energizedSet += coord
        // pairs are overkill here lol
        val coordPair : Pair<DirCoordinate, DirCoordinate> = when (rows[coord.y][coord.x]) {
            '-' -> {
                when (coord.dir) {
                    Direction.RIGHT -> DirCoordinate(coord.x + 1, coord.y, coord.dir) to DirCoordinate(coord.x + 1, coord.y, coord.dir)
                    Direction.DOWN, Direction.UP -> DirCoordinate(coord.x - 1, coord.y, Direction.LEFT) to DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT)
                    Direction.LEFT -> DirCoordinate(coord.x - 1, coord.y, coord.dir) to DirCoordinate(coord.x - 1, coord.y, coord.dir)
                }
            }
            '|' -> {
                when (coord.dir) {
                    Direction.RIGHT, Direction.LEFT -> DirCoordinate(coord.x, coord.y - 1, Direction.UP) to DirCoordinate(coord.x, coord.y + 1, Direction.DOWN)
                    Direction.DOWN -> DirCoordinate(coord.x, coord.y + 1, coord.dir) to DirCoordinate(coord.x, coord.y + 1, coord.dir)
                    Direction.UP -> DirCoordinate(coord.x, coord.y - 1, coord.dir) to DirCoordinate(coord.x, coord.y - 1, coord.dir)
                }
            }
            '/' -> {
                when (coord.dir) {
                    Direction.RIGHT -> DirCoordinate(coord.x, coord.y - 1, Direction.UP) to DirCoordinate(coord.x, coord.y - 1, Direction.UP)
                    Direction.DOWN -> DirCoordinate(coord.x - 1, coord.y, Direction.LEFT) to DirCoordinate(coord.x - 1, coord.y, Direction.LEFT)
                    Direction.LEFT -> DirCoordinate(coord.x, coord.y + 1, Direction.DOWN) to DirCoordinate(coord.x, coord.y + 1, Direction.DOWN)
                    Direction.UP -> DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT) to DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT)
                }
            }
            '\\' -> {
                when (coord.dir) {
                    Direction.RIGHT -> DirCoordinate(coord.x, coord.y + 1, Direction.DOWN) to DirCoordinate(coord.x, coord.y + 1, Direction.DOWN)
                    Direction.DOWN -> DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT) to DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT)
                    Direction.LEFT -> DirCoordinate(coord.x, coord.y - 1, Direction.UP) to DirCoordinate(coord.x, coord.y - 1, Direction.UP)
                    Direction.UP -> DirCoordinate(coord.x - 1, coord.y, Direction.LEFT) to DirCoordinate(coord.x - 1, coord.y, Direction.LEFT)
                }
            }
            else -> {
                when (coord.dir) {
                    Direction.RIGHT -> DirCoordinate(coord.x + 1, coord.y, coord.dir) to DirCoordinate(coord.x + 1, coord.y, coord.dir)
                    Direction.DOWN -> DirCoordinate(coord.x, coord.y + 1, coord.dir) to DirCoordinate(coord.x, coord.y + 1, coord.dir)
                    Direction.LEFT -> DirCoordinate(coord.x - 1, coord.y, coord.dir) to DirCoordinate(coord.x - 1, coord.y, coord.dir)
                    Direction.UP -> DirCoordinate(coord.x, coord.y - 1, coord.dir) to DirCoordinate(coord.x, coord.y - 1, coord.dir)
                }
            }
        }
        if (!energizedSet.contains(coordPair.first)) {
            waitingToBeEnergizedList += coordPair.first
            if (coordPair.first != coordPair.second && !energizedSet.contains(coordPair.second)) {
                waitingToBeEnergizedList += coordPair.second
            }
        } else if (!energizedSet.contains(coordPair.second)) {
            waitingToBeEnergizedList += coordPair.second
            if (coordPair.first != coordPair.second && !energizedSet.contains(coordPair.first)) {
                waitingToBeEnergizedList += coordPair.first
            }
        }
    }
    return energizedSet
}

fun main() {
    aoc(16) {
        +part {
            val startCoord = DirCoordinate(0, 0, Direction.RIGHT)
            val energizedSet = mutableSetOf<DirCoordinate>()
            val waitingToBeEnergizedList = mutableListOf<DirCoordinate>()
            waitingToBeEnergizedList += startCoord
            while (waitingToBeEnergizedList.isNotEmpty()) {
                val coord = waitingToBeEnergizedList.removeAt(waitingToBeEnergizedList.lastIndex)
                if (coord.x < 0 || coord.y < 0 || coord.x >= columns.size || coord.y >= rows.size) {
                    continue
                }
                if (energizedSet.filter {c -> c.x == coord.x && c.y == coord.y}.isEmpty()) {
                    energizedSet += coord
                }
                // pairs are overkill here lol
                val coordPair : Pair<DirCoordinate, DirCoordinate> = when (rows[coord.y][coord.x]) {
                    '-' -> {
                        when (coord.dir) {
                            Direction.RIGHT -> DirCoordinate(coord.x + 1, coord.y, coord.dir) to DirCoordinate(coord.x + 1, coord.y, coord.dir)
                            Direction.DOWN, Direction.UP -> DirCoordinate(coord.x - 1, coord.y, Direction.LEFT) to DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT)
                            Direction.LEFT -> DirCoordinate(coord.x - 1, coord.y, coord.dir) to DirCoordinate(coord.x - 1, coord.y, coord.dir)
                        }
                    }
                    '|' -> {
                        when (coord.dir) {
                            Direction.RIGHT, Direction.LEFT -> DirCoordinate(coord.x, coord.y - 1, Direction.UP) to DirCoordinate(coord.x, coord.y + 1, Direction.DOWN)
                            Direction.DOWN -> DirCoordinate(coord.x, coord.y + 1, coord.dir) to DirCoordinate(coord.x, coord.y + 1, coord.dir)
                            Direction.UP -> DirCoordinate(coord.x, coord.y - 1, coord.dir) to DirCoordinate(coord.x, coord.y - 1, coord.dir)
                        }
                    }
                    '/' -> {
                        when (coord.dir) {
                            Direction.RIGHT -> DirCoordinate(coord.x, coord.y - 1, Direction.UP) to DirCoordinate(coord.x, coord.y - 1, Direction.UP)
                            Direction.DOWN -> DirCoordinate(coord.x - 1, coord.y, Direction.LEFT) to DirCoordinate(coord.x - 1, coord.y, Direction.LEFT)
                            Direction.LEFT -> DirCoordinate(coord.x, coord.y + 1, Direction.DOWN) to DirCoordinate(coord.x, coord.y + 1, Direction.DOWN)
                            Direction.UP -> DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT) to DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT)
                        }
                    }
                    '\\' -> {
                        when (coord.dir) {
                            Direction.RIGHT -> DirCoordinate(coord.x, coord.y + 1, Direction.DOWN) to DirCoordinate(coord.x, coord.y + 1, Direction.DOWN)
                            Direction.DOWN -> DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT) to DirCoordinate(coord.x + 1, coord.y, Direction.RIGHT)
                            Direction.LEFT -> DirCoordinate(coord.x, coord.y - 1, Direction.UP) to DirCoordinate(coord.x, coord.y - 1, Direction.UP)
                            Direction.UP -> DirCoordinate(coord.x - 1, coord.y, Direction.LEFT) to DirCoordinate(coord.x - 1, coord.y, Direction.LEFT)
                        }
                    }
                    else -> {
                        when (coord.dir) {
                            Direction.RIGHT -> DirCoordinate(coord.x + 1, coord.y, coord.dir) to DirCoordinate(coord.x + 1, coord.y, coord.dir)
                            Direction.DOWN -> DirCoordinate(coord.x, coord.y + 1, coord.dir) to DirCoordinate(coord.x, coord.y + 1, coord.dir)
                            Direction.LEFT -> DirCoordinate(coord.x - 1, coord.y, coord.dir) to DirCoordinate(coord.x - 1, coord.y, coord.dir)
                            Direction.UP -> DirCoordinate(coord.x, coord.y - 1, coord.dir) to DirCoordinate(coord.x, coord.y - 1, coord.dir)
                        }
                    }
                }
                if (!energizedSet.contains(coordPair.first)) {
                    waitingToBeEnergizedList += coordPair.first
                    if (coordPair.first != coordPair.second && !energizedSet.contains(coordPair.second)) {
                        waitingToBeEnergizedList += coordPair.second
                    }
                } else if (!energizedSet.contains(coordPair.second)) {
                    waitingToBeEnergizedList += coordPair.second
                    if (coordPair.first != coordPair.second && !energizedSet.contains(coordPair.first)) {
                        waitingToBeEnergizedList += coordPair.first
                    }
                }
            }
            result = energizedSet.size
        }

        +part {
            val coordList = columns.indices.asSequence().flatMap {first ->
                rows.indices.asSequence().flatMap { second -> sequenceOf(first to second) }
            }.
                filter { it.first == 0 || it.second == 0 || it.first == columns.size - 1 || it.second == rows.size - 1}.distinct().map { Coordinate(it.first, it.second) }.toList()
            val visited : MutableSet<DirCoordinate> = mutableSetOf()
            val dirCoordList = coordList.flatMap {
                val list = mutableListOf<DirCoordinate>()
                if (it.x == 0) list += DirCoordinate(it.x, it.y, Direction.RIGHT)
                if (it.y == 0) list += DirCoordinate(it.x, it.y, Direction.DOWN)
                if (it.x == columns.size - 1) list += DirCoordinate(it.x, it.y, Direction.LEFT)
                if (it.y == rows.size - 1) list += DirCoordinate(it.x, it.y, Direction.UP)
                list
            }.distinct()
            var max = 0
            for (coord in dirCoordList) {
                if (visited.contains(coord)) continue       // completely useless apparently :-(
                val set = evaluate(coord, rows, columns)
                visited.addAll(set)
                max = max(max, set.asSequence().map { Coordinate(it.x, it.y) }.distinct().count())
            }
            result = max
        }
    }
}
