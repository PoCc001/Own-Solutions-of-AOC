fun main() {
    aoc(11) {
        val stones: List<Long> = lines[0].split(" ").map { it.toLong() }
        +part {
            var mutableStones: List<Long> = stones
            repeat(25) {
                val tmpStones: MutableList<Long> = mutableListOf()
                for (stone in mutableStones) {
                    val s = stone.toString()
                    if (stone == 0L) {
                        tmpStones += 1L
                    } else if (s.length % 2 == 0) {
                        tmpStones += s.substring(0..<s.length / 2).toLong()
                        tmpStones += s.substring(s.length / 2).toLong()
                    } else {
                        tmpStones += stone * 2024L
                    }
                }
                mutableStones = tmpStones
            }

            result = mutableStones.size
        }

        +part {
            var mutableStones: Map<Long, Long> = stones.associateWith { 1L }
            repeat(75) {
                val tmpStones: MutableMap<Long, Long> = mutableMapOf()
                for ((stone, times) in mutableStones) {
                    val s = stone.toString()
                    if (stone == 0L) {
                        tmpStones += 1L to (tmpStones[1L] ?: 0) + times
                    } else if (s.length % 2 == 0) {
                        val left = s.substring(0..<s.length / 2).toLong()
                        val right = s.substring(s.length / 2).toLong()
                        tmpStones += left to (tmpStones[left] ?: 0) + times
                        tmpStones += right to (tmpStones[right] ?: 0) + times
                    } else {
                        tmpStones += stone * 2024L to (tmpStones[stone * 2024L] ?: 0) + times
                    }
                }
                mutableStones = tmpStones
            }

            result = mutableStones.values.sum()
        }
    }
}