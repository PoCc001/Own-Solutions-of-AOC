import java.util.*
import kotlin.streams.toList

fun main() {
    aoc(10) {
        val heads = input.coordinatesOf('0')
        val numCoords = lines.flatMap { it.chars().toList() }.map { it.toChar() }.associateWith { input.coordinatesOf(it).toSet() }
        +part {
            val coordBuffer: Queue<Coordinate> = LinkedList()
            val ends: MutableMap<Coordinate, MutableSet<Coordinate>> = mutableMapOf()
            for (head in heads) {
                coordBuffer.add(head)
                ends += head to mutableSetOf()
                while (coordBuffer.isNotEmpty()) {
                    val current = coordBuffer.poll()
                    val currentChar = input[current.y][current.x]
                    if (currentChar == '9') {
                        ends[head] ?: mutableSetOf() += current
                    } else {
                        val nextChar = currentChar + 1
                        coordBuffer.addAll(numCoords[nextChar].orEmpty().filter {
                            it.left(1) == current ||
                                    it.right(1) == current || it.up(1) == current || it.down(1) == current
                        })
                    }
                }
            }

            result = ends.flatMap { it.value }.size
        }

        +part {
            val coordBuffer: Queue<Coordinate> = LinkedList()
            var ratings = 0
            for (head in heads) {
                var rating = 1
                coordBuffer.add(head)
                while (coordBuffer.isNotEmpty()) {
                    val current = coordBuffer.poll()
                    val currentChar = input[current.y][current.x]
                    if (currentChar == '9') {
                        continue
                    } else {
                        val nextChar = currentChar + 1
                        val nextCoords = numCoords[nextChar].orEmpty().filter {
                            it.left(1) == current ||
                                    it.right(1) == current || it.up(1) == current || it.down(1) == current
                        }
                        rating += nextCoords.size - 1
                        coordBuffer.addAll(nextCoords)
                    }
                }
                ratings += rating
            }

            result = ratings
        }
    }
}