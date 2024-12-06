fun turnRight(d: Direction) =
    when(d) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
    }

fun main() {
    aoc(6) {
        +part {
            var guardPosition = input.coordinatesOf('^')[0]
            val positionSet: MutableSet<Coordinate> = mutableSetOf(guardPosition)
            var guardDirection = Direction.UP
            val obstaclePositions = input.coordinatesOf('#').toSet()

            while (guardPosition.x >= 0 && guardPosition.y >= 0 && guardPosition.y < lines.size && guardPosition.x < lines[guardPosition.y].length) {
                val nextPos = guardPosition.move(guardDirection)
                if (nextPos in obstaclePositions) {
                    guardDirection = turnRight(guardDirection)
                } else {
                    guardPosition = nextPos
                    positionSet += guardPosition
                }
            }

            result = positionSet.size - 1
        }

        +part {
            var guardPosition = input.coordinatesOf('^')[0]
            val positionList: MutableList<Pair<Coordinate, Direction>> = mutableListOf(guardPosition to Direction.UP)
            var guardDirection = Direction.UP
            val obstaclePositions = input.coordinatesOf('#').toSet()

            while (guardPosition.x >= 0 && guardPosition.y >= 0 && guardPosition.y < lines.size && guardPosition.x < lines[guardPosition.y].length) {
                val nextPos = guardPosition.move(guardDirection)
                if (nextPos in obstaclePositions) {
                    guardDirection = turnRight(guardDirection)
                } else {
                    guardPosition = nextPos
                    if (guardPosition.x >= 0 && guardPosition.y >= 0 && guardPosition.y < lines.size && guardPosition.x < lines[guardPosition.y].length) {
                        positionList += guardPosition to guardDirection
                    }
                }
            }

            val potentialObstaclePositions = positionList.filterNot { it.first == input.coordinatesOf('^')[0] }.map { it.first }.distinct()
            var posCount = 0
            // it's a bit slow, but gets the job done
            for (pos in potentialObstaclePositions) {
                val trace = positionList.takeWhile { it.first != pos }.toMutableList()
                guardDirection = trace.last().second
                while (trace.last().first.x >= 0 && trace.last().first.y >= 0 && trace.last().first.y < lines.size &&
                    trace.last().first.x < lines[trace.last().first.y].length) {
                    val nextPos = trace.last().first.move(guardDirection)
                    if (nextPos in obstaclePositions || nextPos == pos) {
                        guardDirection = turnRight(guardDirection)
                    } else {
                        if (nextPos to guardDirection in trace) {
                            posCount++
                            break
                        } else {
                            trace += nextPos to guardDirection
                        }
                    }
                }
            }

            result = posCount
        }
    }
}