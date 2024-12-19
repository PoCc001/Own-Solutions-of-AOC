import java.util.*

fun isCompleteBarrier(b: Set<Coordinate>, range: Int): Boolean {
    if (Coordinate(0, 0) in b || Coordinate(range, range) in b) {
        return true
    }
    val onEdge = b.filter { it.x == 0 || it.x == range || it.y == 0 || it.y == range }
    return onEdge.any {
        (it.x != 0 || onEdge.any { c -> c.x != 0 && c.y != range }) && (it.y != range || onEdge.any { c -> c.y != range && c.x != 0 }) &&
                (it.x != range || onEdge.any { c -> c.x != range && c.y != 0 }) && (it.y != 0 || onEdge.any { c -> c.y != 0 && c.x != range })
    }
}

fun main() {
    aoc(18) {
        val range = if (test == 1) 6 else 70
        val byteAmount = if (test == 1) 12 else 1024
        val fallCoordinates = lines.map { it.split(",") }.map { Coordinate(it[0].toInt(), it[1].toInt()) }
        +part {
            val coordinates = generateSequence(0) {
                it + 1
            }.take(range + 1).cross(generateSequence(0) {
                it + 1
            }.take(range + 1)).map { Coordinate(it.first, it.second) }.toSet() - fallCoordinates.take(byteAmount).toSet()
            val start = Coordinate(0, 0)
            val end = Coordinate(range, range)
            var endLength = 0
            val queue: Queue<Pair<Coordinate, Int>> = LinkedList()
            queue += start to 0
            val visited: MutableSet<Coordinate> = mutableSetOf(start)
            while (queue.isNotEmpty()) {
                val curr = queue.poll()
                if (curr.first == end) {
                    endLength = curr.second
                    break
                }
                setOf(curr.first.up(), curr.first.right(), curr.first.down(), curr.first.left()).filter { it !in visited && it in coordinates }.forEach {
                    visited += it
                    queue += it to curr.second + 1
                }
            }
            result = endLength
        }

        +part {
            var acc: MutableSet<Coordinate> = fallCoordinates.take(byteAmount).toMutableSet()
            val rest = fallCoordinates.drop(byteAmount)
            var firstBlockCoord = Coordinate(0, 0)
            var finished = false
            for (b in rest) {
                acc += b
                val visited: MutableSet<Coordinate> = mutableSetOf(b)
                val queue: Queue<Coordinate> = LinkedList()
                val currs: MutableSet<Coordinate> = mutableSetOf()
                queue += b
                while (queue.isNotEmpty()) {
                    val curr = queue.poll()
                    currs += curr
                    if (isCompleteBarrier(currs, range)) {
                        firstBlockCoord = b
                        finished = true
                        break
                    }
                    setOf(curr.up(), curr.right(), curr.down(), curr.left(), curr.up().left(), curr.up().right(),
                          curr.down().left(), curr.down().right()).filter { it !in visited && it in acc }.forEach {
                        visited += it
                        queue += it
                    }
                }
                if (finished) {
                    break
                }
            }
            result = "${firstBlockCoord.x},${firstBlockCoord.y}"
        }
    }
}