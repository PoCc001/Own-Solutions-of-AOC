import java.math.BigInteger

fun findCombinations(a: Coordinate, b: Coordinate, p: Coordinate): List<Pair<Int, Int>> {
    val combinations: MutableList<Pair<Int, Int>> = mutableListOf()
    for (i in 0..100) {
        for (j in 0..100) {
            if (a.x * i + b.x * j == p.x && a.y * i + b.y * j == p.y) {
                combinations += i to j
            }
        }
    }
    return combinations
}

fun main() {
    aoc(13)  {
        val clawMachines: MutableList<Triple<Coordinate, Coordinate, Coordinate>> = mutableListOf()
        +part {
            for (i in lines.indices) {
                if (i % 4 == 0) {
                    clawMachines += Triple(Coordinate(lines[i].substring(12, lines[i].indexOf(',')).toInt(),
                        lines[i].substring(lines[i].indexOf(',') + 4).toInt()), Coordinate(-1, -1), Coordinate(-1, -1))
                } else if (i % 4 == 1) {
                    clawMachines[clawMachines.size - 1] = Triple(clawMachines[clawMachines.size - 1].first,
                        Coordinate(lines[i].substring(12, lines[i].indexOf(',')).toInt(),
                        lines[i].substring(lines[i].indexOf(',') + 4).toInt()), Coordinate(-1, -1))
                } else if (i % 4 == 2) {
                    clawMachines[clawMachines.size - 1] = Triple(clawMachines[clawMachines.size - 1].first, clawMachines[clawMachines.size - 1].second,
                        Coordinate(lines[i].substring(9, lines[i].indexOf(',')).toInt(), lines[i].substring(lines[i].indexOf(',') + 4).toInt()))
                }
            }

            var sum = 0L
            for (machine in clawMachines) {
                val solutions = findCombinations(machine.first, machine.second, machine.third)
                val minSolution = solutions.minByOrNull { it.first * 3 + it.second } ?: (0 to 0)
                sum  += minSolution.first * 3 + minSolution.second
            }

            result = sum
        }

        +part {
            val big = 10000000000000L
            val bigClawMachines = clawMachines.map { Triple(it.first, it.second, it.third.x.toLong() + big to it.third.y.toLong() + big) }
            var sum = 0L

            for (machine in bigClawMachines) {
                val ax = machine.first.x.toFraction()
                val ay = machine.first.y.toFraction()
                val bx = machine.second.x.toFraction()
                val by = machine.second.y.toFraction()
                val px = machine.third.first.toFraction()
                val py = machine.third.second.toFraction()
                val t = (ax * py - px * ay) / (ax * by - bx * ay)
                val s = (px - t * bx) / ax
                if (t.denominator == BigInteger.ONE && s.denominator == BigInteger.ONE) {
                    sum += s.numerator.toLong() * 3 + t.numerator.toLong()
                }
            }

            result = sum
        }
    }
}