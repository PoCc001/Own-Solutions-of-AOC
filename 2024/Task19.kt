fun validateDesign(design: String, patterns: Set<String>): Boolean {
    if (design == "") {
        return true
    }
    return patterns.any {
        design.length >= it.length &&
        it == design.substring(0, it.length) &&
                validateDesign(design.substring(it.length), patterns)
    }
}

fun main() {
    aoc(19) {
        val patterns = lines[0].split(", ").toSet()
        val designs = lines.drop(2)
        val validDesigns: MutableSet<String> = mutableSetOf()
        +part {
            var count = 0
            for (design in designs) {
                if (validateDesign(design, patterns)) {
                    count++
                    validDesigns += design
                }
            }
            result = count
        }

        +part {
            val restPairs: MutableMap<String, Long> = validDesigns.associateWith { validDesigns.count { s -> it == s }.toLong() }.toMutableMap()
            // takes maybe a minute or smth
            while (restPairs.size > 1) {
                val (design, m) = restPairs.filterKeys { it.isNotEmpty() }.minBy { it.value }
                restPairs -= design
                for (s in patterns.filter { design.length >= it.length && it == design.substring(0, it.length) }) {
                    val restString = design.substring(s.length)
                    restPairs[restString] = (restPairs[restString] ?: 0) + m
                }
            }

            result = restPairs[""]
        }
    }
}