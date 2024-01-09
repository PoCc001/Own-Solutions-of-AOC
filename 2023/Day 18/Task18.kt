import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    aoc(18) {
        +part {
            val coordinates = mutableListOf<Coordinate>()
            coordinates += Coordinate(0, 0)
            for (r in rows) {
                when (r[0]) {
                    'U' -> {
                        for (i in 0..<r.split(" ")[1].toInt()) {
                            coordinates += coordinates.last().up()
                        }
                    }
                    'R' -> {
                        for (i in 0..<r.split(" ")[1].toInt()) {
                            coordinates += coordinates.last().right()
                        }
                    }
                    'D' -> {
                        for (i in 0..<r.split(" ")[1].toInt()) {
                            coordinates += coordinates.last().down()
                        }
                    }
                    'L' -> {
                        for (i in 0..<r.split(" ")[1].toInt()) {
                            coordinates += coordinates.last().left()
                        }
                    }
                }
            }
            val all = mutableListOf<Coordinate>()
            all += coordinates
            val fringe = mutableListOf<Coordinate>()
            fringe += Coordinate(1, 0)     // I have checked manually, that (1, 0) is inside
            while (fringe.isNotEmpty()) {
                val coord = fringe.removeAt(0)
                fringe += listOf(coord.up(), coord.right(), coord.down(), coord.left()).filterNot { it in fringe || it in all }
                all += coord
            }
            result = all.distinct().size
        }

        fun getCoordinatePairs(coordinate1 : Coordinate, coordinate2 : Coordinate, all : List<Coordinate>) : Pair<List<Pair<Coordinate, Coordinate>>, List<Pair<Coordinate, Coordinate>>> {
            val pairListX = mutableListOf<Pair<Coordinate, Coordinate>>()
            val pairListY = mutableListOf<Pair<Coordinate, Coordinate>>()
            val maxX = max(coordinate1.x, coordinate2.y)
            val minX = min(coordinate1.x, coordinate2.x)
            val maxY = max(coordinate1.y, coordinate2.y)
            val minY = min(coordinate1.y, coordinate2.y)
            for (i in 0..<all.size - 1) {
                val pair = all[i] to all[i + 1]
                if (pair.first.x == pair.second.x && pair.first.x < maxX && pair.first.x >= minX) {
                    pairListX += pair
                } else if (pair.first.y == pair.second.y && pair.first.y < maxY && pair.first.y >= minY) {
                    pairListY += pair
                }
            }
            return pairListX to pairListY
        }

        fun isInside(tested : Coordinate, all : List<Coordinate>) : Boolean {
            var count = 0
            val coordinate1 = Coordinate(tested.x, tested.y)
            val coordinate2 = Coordinate(all.maxOf { it.x }, tested.y)
            val maxX = max(coordinate1.x, coordinate2.y)
            val minX = min(coordinate1.x, coordinate2.x)
            val maxY = max(coordinate1.y, coordinate2.y)
            val minY = min(coordinate1.y, coordinate2.y)
            for (i in 0..<all.size - 1) {
                val pair = all[i] to all[i + 1]
                if (pair.first.x == pair.second.x && pair.first.x < maxX && pair.first.x >= minX &&
                    (pair.first.y < coordinate1.y && pair.second.y < coordinate1.y || pair.first.y > coordinate1.y && pair.second.y > coordinate1.y)) {
                    count++
                } else if (pair.first.y == pair.second.y && pair.first.y < maxY && pair.first.y >= minY &&
                    (pair.first.x < coordinate1.x && pair.second.x < coordinate1.x || pair.first.x > coordinate1.x && pair.second.x > coordinate1.x)) {
                    count++
                }
            }
            return count % 2 == 1
        }

        class Slice(val coordinates : MutableList<Coordinate>, val corner1 : Coordinate, val corner2 : Coordinate) {
            val subSlices : MutableList<Slice> = mutableListOf()
            fun roughArea() = abs(corner1.x - corner2.x).toLong() * abs(corner1.y - corner2.y)
            fun area() : Long {
                divide()
                println("$corner1 - $corner2")
                return if (subSlices.isEmpty()) {
                    println("inner")
                    if (isInside(Coordinate((corner1.x + corner2.y) / 2, (corner1.y + corner2.y) / 2), coordinates)) {
                        println("Area: ${abs(corner1.x - corner2.x).toLong() * abs(corner1.y - corner2.y)}")
                        abs(corner1.x - corner2.x).toLong() * abs(corner1.y - corner2.y)
                    } else {
                        0L
                    }

                } else {
                    var sum = subSlices.removeAt(1).area()
                    sum += subSlices.removeAt(0).area()
                    sum
                }
            }
            fun divide() {
                val pairs = getCoordinatePairs(corner1, corner2, coordinates)
                if (pairs.first.isNotEmpty()) {
                    val pair = pairs.first[pairs.first.size / 2]
                    if (pair.first.x == corner1.x || pair.first.x == corner2.x) {
                        return
                    }
                    val min = if (corner1.x < corner2.x) corner1 else corner2
                    val max = if (corner1.x < corner2.x) corner2 else corner1
                    subSlices += Slice(coordinates, min, Coordinate(pair.first.x, max.y))
                    subSlices += Slice(coordinates, Coordinate(pair.first.x, min.y), Coordinate(max.x, max.y))
                } else if (pairs.second.isNotEmpty()) {
                    val pair = pairs.second[pairs.second.size / 2]
                    if (pair.first.y == corner1.y || pair.first.y == corner2.y) {
                        return
                    }
                    val min = if (corner1.y < corner2.y) corner1 else corner2
                    val max = if (corner1.y < corner2.y) corner2 else corner1
                    subSlices += Slice(coordinates, min, Coordinate(max.x, pair.first.y))
                    subSlices += Slice(coordinates, Coordinate(min.x, pair.first.y), Coordinate(max.x, max.y))
                }
            }
        }

        part {
            
        }
    }
}
