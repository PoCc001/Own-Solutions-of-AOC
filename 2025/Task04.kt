fun main() {
    aoc(4) {
        val coords = input.coordinatesOf('@').toSet()
        +part {
            var count = 0

            for (coord in coords) {
                val adjacentCount = coords.count {
                    it == coord.up() || it == coord.down() || it == coord.left() ||
                            it == coord.right() || it == coord.up().right() || it == coord.down().right() ||
                            it == coord.down().left() || it == coord.up().left()
                }

                if (adjacentCount < 4) {
                    count++
                }
            }

            result = count
        }

        +part {
            var count = 0
            var removalCount = -1

            var mutableCoords = coords.toMutableSet()

            while (removalCount > 0 || count == 0) {
                removalCount = 0
                var removalSet = mutableSetOf<Coordinate>()
                for (coord in mutableCoords) {
                    val adjacentCount = mutableCoords.count {
                        it == coord.up() || it == coord.down() || it == coord.left() ||
                                it == coord.right() || it == coord.up().right() || it == coord.down().right() ||
                                it == coord.down().left() || it == coord.up().left()
                    }

                    if (adjacentCount < 4) {
                        removalSet += coord
                        count++
                        removalCount++
                    }
                }

                mutableCoords -= removalSet
            }

            result = count
        }
    }
}