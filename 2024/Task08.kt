fun getAntinodeCoords(p1: Coordinate, p2: Coordinate): Pair<Coordinate, Coordinate> {
    val d = p2 - p1
    return p1 - d to p2 + d
}

fun main() {
    aoc(8) {
        val coordinateMap = lines.flatMap { it.filterNot { c -> c == '.' }.map { c -> c to input.coordinatesOf(c) } }.toMap()
        +part {
            val antinodeCoordinates: MutableSet<Coordinate> = mutableSetOf()
            for (antennaCoords in coordinateMap.values) {
                for (a1 in antennaCoords) {
                    for (a2 in antennaCoords) {
                        if (a1 != a2) {
                            val c = getAntinodeCoords(a1, a2)
                            antinodeCoordinates += c.first
                            antinodeCoordinates += c.second
                        }
                    }
                }
            }

            result = antinodeCoordinates.count { it.x >= 0 && it.y >= 0 && it.x < lines[0].length && it.y < lines.size }
        }

        +part {
            val antinodeCoordinates: MutableSet<Coordinate> = mutableSetOf()
            for (antennaCoords in coordinateMap.values) {
                for (a1 in antennaCoords) {
                    for (a2 in antennaCoords) {
                        if (a1 != a2) {
                            val d = a1 - a2
                            antinodeCoordinates += generateSequence(a1) {
                                it + d
                            }.takeWhile { it.x >= 0 && it.x < lines[0].length && it.y >= 0 && it.y < lines.size }
                            antinodeCoordinates += generateSequence(a1) {
                                it - d
                            }.takeWhile { it.x >= 0 && it.x < lines[0].length && it.y >= 0 && it.y < lines.size }
                        }
                    }
                }
            }
            result = antinodeCoordinates.size
        }
    }
}