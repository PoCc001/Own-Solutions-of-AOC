import java.util.*

fun main() {
    aoc(12) {
        val charCoords: Queue<MutableSet<Coordinate>> = LinkedList(lines.flatMap { it.toSet() }.map { input.coordinatesOf(it).toMutableSet() }.toSet())
        val regions: MutableSet<Set<Coordinate>> = mutableSetOf()
        while (charCoords.isNotEmpty()) {
            val set = charCoords.poll()
            while (set.isNotEmpty()) {
                val queue: Queue<Coordinate> = LinkedList()
                queue.add(set.first())
                set -= set.first()
                val region: MutableSet<Coordinate> = mutableSetOf()
                while (queue.isNotEmpty()) {
                    val curr = queue.poll()
                    region += curr
                    if (curr.left(1) in set) {
                        queue.add(curr.left(1))
                        set -= curr.left(1)
                    }
                    if (curr.right(1) in set) {
                        queue.add(curr.right(1))
                        set -= curr.right(1)
                    }
                    if (curr.up(1) in set) {
                        queue.add(curr.up(1))
                        set -= curr.up(1)
                    }
                    if (curr.down(1) in set) {
                        queue.add(curr.down(1))
                        set -= curr.down(1)
                    }
                }
                regions += region
            }
        }

        val areas: MutableList<Int> = mutableListOf()
        +part {
            val perimeters: MutableList<Int> = mutableListOf()
            for (region in regions) {
                areas += region.size
                perimeters += region.flatMap { setOf(it.left(1), it.right(1), it.up(1), it.down(1)).
                    filterNot { c -> region.contains(c) } }.size
            }

            result = areas.zip(perimeters).sumOf { it.first * it.second }
        }

        +part {
            val sides: MutableList<Int> = mutableListOf()
            for (region in regions) {
                val fenceCoords = region.flatMap { setOf(it.left(1), it.right(1), it.up(1), it.down(1)).
                    filterNot { c -> region.contains(c) } }.toSet()
                val innerLeft = region.filter { it.left(1) in fenceCoords }.sortedBy { it.y }.sortedBy { it.x }.toMutableList()
                var prevX = innerLeft.first().x
                var prevY = innerLeft.first().y
                var sideCount = 1
                innerLeft.removeFirst()
                for (l in innerLeft) {
                    prevY++
                    if (prevX != l.x || prevY != l.y) {
                        prevX = l.x
                        prevY = l.y
                        sideCount++
                    }
                }
                val innerRight = region.filter { it.right(1) in fenceCoords }.sortedBy { it.y }.sortedBy { it.x }.toMutableList()
                prevX = innerRight.first().x
                prevY = innerRight.first().y
                sideCount++
                innerRight.removeFirst()
                for (l in innerRight) {
                    prevY++
                    if (prevX != l.x || prevY != l.y) {
                        prevX = l.x
                        prevY = l.y
                        sideCount++
                    }
                }
                val innerUp = region.filter { it.up(1) in fenceCoords }.sortedBy { it.x }.sortedBy { it.y }.toMutableList()
                prevX = innerUp.first().x
                prevY = innerUp.first().y
                sideCount++
                innerUp.removeFirst()
                for (l in innerUp) {
                    prevX++
                    if (prevX != l.x || prevY != l.y) {
                        prevX = l.x
                        prevY = l.y
                        sideCount++
                    }
                }
                val innerDown = region.filter { it.down(1) in fenceCoords }.sortedBy { it.x }.sortedBy { it.y }.toMutableList()
                prevX = innerDown.first().x
                prevY = innerDown.first().y
                sideCount++
                innerDown.removeFirst()
                for (l in innerDown) {
                    prevX++
                    if (prevX != l.x || prevY != l.y) {
                        prevX = l.x
                        prevY = l.y
                        sideCount++
                    }
                }
                sides += sideCount
            }

            result = areas.zip(sides).sumOf { it.first * it.second }
        }
    }
}