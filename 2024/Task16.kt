fun getAdjacentNodes(currCoord: Coordinate, currDir: Direction, pathCoords: Set<Pair<Coordinate, Direction>>) =
    when (currDir) {
        Direction.UP -> pathCoords.flatMap {
            if (it.first == currCoord.up() && it.second == currDir) {
                listOf(it to 1)
            } else if (it.first == currCoord && (it.second == Direction.LEFT || it.second == Direction.RIGHT)) {
                listOf(it to 1000)
            } else {
                listOf()
            }
        }
        Direction.RIGHT -> pathCoords.flatMap {
            if (it.first == currCoord.right() && it.second == currDir) {
                listOf(it to 1)
            } else if (it.first == currCoord && (it.second == Direction.UP || it.second == Direction.DOWN)) {
                listOf(it to 1000)
            } else {
                listOf()
            }
        }
        Direction.DOWN -> pathCoords.flatMap {
            if (it.first == currCoord.down() && it.second == currDir) {
                setOf(it to 1)
            } else if (it.first == currCoord && (it.second == Direction.RIGHT || it.second == Direction.LEFT)) {
                setOf(it to 1000)
            } else {
                setOf()
            }
        }
        Direction.LEFT -> pathCoords.flatMap {
            if (it.first == currCoord.left() && it.second == currDir) {
                listOf(it to 1)
            } else if (it.first == currCoord && (it.second == Direction.DOWN || it.second == Direction.UP)) {
                listOf(it to 1000)
            } else {
                listOf()
            }
        }
    }

fun main() {
    aoc(16) {
        val startCoord = input.coordinatesOf('S')[0] to Direction.RIGHT
        val endCoords = input.coordinatesOf('E').asSequence().cross(Direction.entries.asSequence()).toSet()
        val pathCoords = (input.coordinatesOf('.').asSequence().cross(Direction.entries.asSequence()).toSet() + endCoords +
                input.coordinatesOf('S').asSequence().cross(Direction.entries.asSequence()))
        val visited: MutableMap<Pair<Coordinate, Direction>, Int> = mutableMapOf()
        +part {
            val queueMap: MutableMap<Pair<Coordinate, Direction>, Int> = mutableMapOf(startCoord to 0)
            var endScore = Int.MAX_VALUE
            while (queueMap.isNotEmpty()) {
                val curr = queueMap.minBy { it.value }
                queueMap -= curr.key
                visited[curr.key] = curr.value
                if (curr.key in endCoords && endScore > curr.value) {
                    endScore = curr.value
                }
                getAdjacentNodes(curr.key.first, curr.key.second, pathCoords).forEach {
                    if (it.first !in visited && (queueMap[it.first] ?: Int.MAX_VALUE) > it.second + curr.value) {
                        queueMap[it.first] = it.second + curr.value
                    }
                }
            }

            result = endScore
        }

        +part {
            val visited2 = visited.map { (it.key.first to -it.key.second) to it.value }.toMap()
            val lowestEndScore = visited2.filter { it.key in endCoords }.minBy { it.value }.value
            val lowestEnds = visited2.filter { it.key in endCoords && it.value == lowestEndScore }.toList().
                map { Triple(it.first.first, it.first.second, it.second) }
            val queueList = lowestEnds.toMutableList()
            val pathCoords2 = pathCoords.map { it.first to -it.second }.toSet()
            val shortestPathCoords: MutableSet<Pair<Coordinate, Direction>> = mutableSetOf()
            while (queueList.isNotEmpty()) {
                val (currCoord, currDir, currScore) = queueList.removeLast()
                shortestPathCoords += currCoord to currDir
                getAdjacentNodes(currCoord, currDir, pathCoords2).forEach {
                    if (it.first.first to it.first.second !in shortestPathCoords && (visited2[it.first] ?: Int.MAX_VALUE) < currScore) {
                        queueList += Triple(it.first.first, it.first.second, visited2[it.first] ?: Int.MAX_VALUE)
                    }
                }
            }

            result = shortestPathCoords.map { it.first }.toSet().size
        }
    }
}