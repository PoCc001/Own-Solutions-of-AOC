fun validEquation(eq: Pair<Long, List<Long>>): Boolean {
    if (eq.second.isEmpty()) {
        return eq.first == 0L
    } else {
        var valid = false
        if (eq.first % eq.second.last() == 0L) {
            valid = validEquation(eq.first / eq.second.last() to eq.second.dropLast(1))
        }

        if (!valid) {
            valid = validEquation(eq.first - eq.second.last() to eq.second.dropLast(1))
        }

        return valid
    }
}

fun getTenPower(l: Long): Long {
    var p = 0
    var exp = 1L
    while (l >= exp) {
        exp *= 10
        p++
    }

    return exp
}

fun validEquation2(eq: Pair<Long, List<Long>>): Boolean {
    if (eq.second.isEmpty()) {
        return eq.first == 0L
    } else {
        var valid = false
        if (eq.first % eq.second.last() == 0L) {
            valid = validEquation2(eq.first / eq.second.last() to eq.second.dropLast(1))
        }

        if (!valid && eq.second.last() <= eq.first) {
            valid = validEquation2(eq.first - eq.second.last() to eq.second.dropLast(1))
        }

        if (!valid && eq.second.size >= 2) {
            val exp = getTenPower(eq.second.last())
            if ((eq.first - eq.second.last()) % exp == 0L) {
                valid = validEquation2(eq.first / exp to eq.second.dropLast(1))
            }
        }

        return valid
    }
}

fun main() {
    aoc(7) {
        val equations = lines.map { it.split(": ") }.
            map { it[0].toLong() to it[1] }.map { it.first to it.second.split(" ").map { s -> s.toLong() } }
        +part {
            var sum = 0L

            for (eq in equations) {
                if (validEquation(eq)) {
                    sum += eq.first
                }
            }

            result = sum
        }

        +part {
            var sum = 0L

            for (eq in equations) {
                if (validEquation(eq) || validEquation2(eq)) {
                    sum += eq.first
                }
            }

            result = sum
        }
    }
}