fun main() {
    aoc(9) {
        val preReserved = lines[0].filterIndexed { i, _ -> i % 2 == 0 }.mapIndexed { i, c -> i to (c - '0') }
        val preFree = lines[0].filterIndexed { i, _ -> i % 2 == 1 }.map { it - '0' }
        +part {
            val reserved = preReserved.toMutableList()
            val free = preFree.toMutableList()
            val space: MutableList<Pair<Int, Int>> = mutableListOf()
            var endIndex = reserved.size - 1
            var freeIndex = 0
            var i = 0
            while (endIndex >= i / 2) {
                if (i % 2 == 0) {
                    space += reserved[i / 2]
                    i++
                } else {
                    val (id, n) = reserved[endIndex]
                    if (n > free[freeIndex]) {
                        space += id to free[freeIndex]
                        reserved[endIndex] = id to n - free[freeIndex]
                        freeIndex++
                        i++
                    } else {
                        space += id to n
                        endIndex--
                        if (n < free[freeIndex]) {
                            free[freeIndex] -= n
                        } else {
                            freeIndex++
                            i++
                        }
                    }
                }
            }

            space.removeLast()

            var checksum = 0L
            var index = 0
            for (p in space) {
                repeat(p.second) {
                    checksum += index * p.first
                    index++
                }
            }
            result = checksum
        }

        +part {
            val free = preFree.map { -1 to it }.toMutableList()
            if (lines[0].length % 2 == 1) {
                free += -1 to 0
            }
            val space = preReserved.zip(free).flatMap { listOf(it.first, it.second) }.toMutableList()
            var i = space.size - 2
            while (i >= 0) {
                if (space[i].first == -1) {
                    i--
                    continue
                }
                val leftIndex = space.indexOfFirst { it.first == -1 && it.second >= space[i].second }
                if (leftIndex != -1 && leftIndex < i) {
                    val n = space[leftIndex].second
                    space[leftIndex] = space[i]
                    if (n != space[leftIndex].second) {
                        space.add(leftIndex + 1, -1 to n - space[i].second)
                        space[i + 1] = -1 to space[i + 1].second
                    } else {
                        space[i] = -1 to n
                    }
                }
                i--
            }

            var checksum = 0L
            var index = 0
            for (p in space) {
                if (p.first == -1) {index += p.second; continue}
                repeat(p.second) {
                    checksum += index * p.first
                    index++
                }
            }
            result = checksum
        }
    }
}