fun parseNumber(s: String, first: Boolean): Pair<Long, Int> {
    var v = 0L
    var i = 0
    while (i < s.length && Character.isDigit(s[i])) {
        v *= 10
        v += s[i] - '0'
        i++
    }
    return if (i < s.length) {
        if (first && s[i] == ',' || !first && s[i] == ')') {
            v to i + 1
        } else {
            -1L to -1
        }
    } else {
        -1L to -1
    }
}

fun main() {
    aoc(3) {
        val oneLineInput = lines.joinToString("")
        val numberTriples: MutableList<Triple<Int, Long, Long>> = mutableListOf()
        +part {
            var startIndex = 0
            while (startIndex + 4 < oneLineInput.length) {
                if (oneLineInput[startIndex] != 'm') {
                    startIndex++
                } else if (oneLineInput[startIndex + 1] != 'u') {
                    startIndex++
                } else if (oneLineInput[startIndex + 2] != 'l') {
                    startIndex += 2
                } else if (oneLineInput[startIndex + 3] != '(') {
                    startIndex += 3
                } else {
                    val p1 = parseNumber(oneLineInput.substring(startIndex + 4), true)
                    if (p1.second != -1) {
                        val p2 = parseNumber(oneLineInput.substring(startIndex + 4 + p1.second), false)
                        if (p2.second != -1) {
                            numberTriples += Triple(startIndex, p1.first, p2.first)
                            startIndex += 4 + p1.second + p2.second
                        } else {
                            startIndex += 4 + p1.second
                        }
                    } else {
                        startIndex += 4
                    }
                }
            }
            result = numberTriples.sumOf { it.second * it.third }
        }

        +part {
            var startIndex = 0
            val dos: MutableList<Int> = mutableListOf()
            val donts: MutableList<Int> = mutableListOf()
            while (startIndex + 3 < oneLineInput.length) {
                if (oneLineInput[startIndex] != 'd') {
                    startIndex++
                } else if (oneLineInput[startIndex + 1] != 'o') {
                    startIndex++
                } else {
                    if (oneLineInput[startIndex + 2] == '(') {
                        if (oneLineInput[startIndex + 3] == ')') {
                            dos += startIndex
                            startIndex += 4
                        } else {
                            startIndex += 3
                        }
                    } else if (oneLineInput[startIndex + 2] == 'n') {
                        if (oneLineInput[startIndex + 3] != '\'') {
                            startIndex += 3
                        } else if (startIndex + 4 < oneLineInput.length && oneLineInput[startIndex + 4] != 't') {
                            startIndex += 4
                        } else if (startIndex + 5 < oneLineInput.length && oneLineInput[startIndex + 5] != '(') {
                            startIndex += 5
                        } else if (startIndex + 6 < oneLineInput.length && oneLineInput[startIndex + 6] != ')') {
                            startIndex += 6
                        } else {
                            donts += startIndex
                            startIndex += 7
                        }
                    } else {
                        startIndex += 2
                    }
                }
            }

            var sum = 0L
            for ((i, n1, n2) in numberTriples) {
                val maxDo = dos.filter { it < i }.maxOrNull()
                val maxDont = donts.filter { it < i }.maxOrNull()
                if (maxDont == null || maxDo != null && maxDo > maxDont) {
                    sum += n1 * n2
                }
            }

            result = sum
        }
    }
}