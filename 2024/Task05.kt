fun main() {
    aoc(5) {
        val rules = lines.asSequence().filter { it.contains('|') }.map { it.split("|") }.map { it.map { s -> s.toInt() } }.toSet()
        val updates = lines.asSequence().filterNot { it.contains('|') || it.isEmpty() }.
        map { it.split(",") }.map { it.map { s -> s.toInt() } }.toList()
        +part {
            val correctlySorted: MutableList<List<Int>> = mutableListOf()
            for (update in updates) {
                var correct = true
                for (i in update.indices) {
                    for (j in i+1..<update.size) {
                        if (rules.contains(listOf(update[j], update[i]))) {
                            correct = false
                            break
                        }
                    }

                    if (!correct) {
                        break
                    }
                }

                if (correct) {
                    correctlySorted += listOf(update)
                }
            }

            result = correctlySorted.sumOf { it[it.size / 2] }
        }

        +part {
            val incorrectlySorted: MutableList<List<Int>> = mutableListOf()
            for (update in updates) {
                var correct = true
                for (i in update.indices) {
                    for (j in i+1..<update.size) {
                        if (rules.contains(listOf(update[j], update[i]))) {
                            correct = false
                            break
                        }
                    }

                    if (!correct) {
                        break
                    }
                }

                if (!correct) {
                    incorrectlySorted += listOf(update)
                }
            }

            result = incorrectlySorted.map { it.sortedWith { i1, i2 ->
                    if (i1 == i2) {
                        0
                    } else if (rules.contains(listOf(i1, i2))) {
                        -1
                    } else {
                        1
                    }
                }
            }.sumOf { it[it.size / 2] }
        }
    }
}