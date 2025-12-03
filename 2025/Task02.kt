import javax.management.Query.div
import kotlin.math.*

fun getAllDivisors(n : Int) : Set<Int> {
    var divs : MutableSet<Int> = mutableSetOf(n)

    for (i in 2..n / 2) {
        if (n % i == 0) {
            divs += i
        }
    }

    return divs
}

fun main() {
    aoc(2) {
        val idRanges = input.sequence.filter { it.isNotEmpty() }.
                flatMap { it.split(",").filter { s -> s.isNotBlank() }.
                map { r -> (r.substringBefore('-').toLong()) to (r.substringAfter('-').toLong()) } }.
                toList()
        +part {
            var sum = 0L

            val filteredRanges = idRanges.filterNot { (s, e) -> s.toString().length == e.toString().length && s.toString().length % 2 == 1 }

            for ((start, end) in filteredRanges) {
                val patternLengths = (start.toString().length..end.toString().length).map { it / 2 }.filterNot { it == 0 }.distinct()
                for (l in patternLengths) {
                    for (pattern in ((max(start, intPow(10L, (2 * l).toLong() - 1))).toString().substring(0, l).toLong()..
                            (min(end, intPow(10L, (2 * l).toLong()) - 1)).toString().substring(0, l).toLong())) {
                        val invalidId = (pattern.toString() + pattern.toString()).toLong()
                        if (invalidId >= start && invalidId <= end) {
                            sum += invalidId
                        }
                    }
                }
            }

            result = sum
        }

        +part {
            val invalidIds = mutableSetOf<Long>()

            for ((start, end) in idRanges) {
                val patternLengths = (start.toString().length..end.toString().length).
                    flatMap { getAllDivisors(it).map { d -> d to it / d } }.
                    distinct()
                for ((d, l) in patternLengths) {
                    for (pattern in ((max(start, intPow(10L, (d * l).toLong() - 1))).toString().substring(0, l).toLong()..
                            (min(end, intPow(10L, (d * l).toLong()) - 1)).toString().substring(0, l).toLong())) {
                        val invalidId = (pattern.toString().repeat(d)).toLong()
                        if (invalidId >= start && invalidId <= end && invalidId >= 10) {
                            invalidIds += invalidId
                        }
                    }
                }
            }

            result = invalidIds.sum()
        }
    }
}