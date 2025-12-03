fun main() {
    aoc(3) {
        val banks = input.sequence.filter { it.isNotEmpty() }.
            map { it.split("").filter { d -> d.isNotEmpty() }.map{ d -> d.toInt() }.toList() }.
            toList()
        +part {
            var sum = 0

            for (bank in banks) {
                var first = 0
                var firstIndex = 0
                for (d in (1..9).reversed()) {
                    firstIndex = bank.indexOfFirst { it == d }
                    if (firstIndex >= 0 && firstIndex < bank.size - 1) {
                        first = d
                        break
                    }
                }

                var second = 0
                var secondIndex = 0
                for (d in (1..9).reversed()) {
                    secondIndex = bank.drop(firstIndex + 1).indexOfFirst { it == d } + firstIndex + 1
                    if (secondIndex > firstIndex && secondIndex < bank.size) {
                        second = d
                        break
                    }
                }

                sum += first * 10 + second
            }

            result = sum
        }

        +part {
            var sum = 0L

            for (bank in banks) {
                var number = 0L
                var nthIndex = 0
                var prevIndex = -1
                for (n in 1..12) {
                    for (d in (1..9).reversed()) {
                        nthIndex = bank.drop(prevIndex + 1).indexOfFirst { it == d } + prevIndex + 1
                        if (nthIndex > prevIndex && nthIndex < bank.size - (12 - n)) {
                            number *= 10L
                            number += d
                            prevIndex = nthIndex
                            break
                        }
                    }
                }

                sum += number
            }

            result = sum
        }
    }
}