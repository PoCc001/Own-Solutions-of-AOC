fun main() {
    aoc(19) {
        data class Part(val x : Int, val m : Int, val a : Int, val s : Int) {
            operator fun get(str : String) : Int {
                return when(str) {
                    "x" -> x
                    "m" -> m
                    "a" -> a
                    "s" -> s
                    else -> error("$str is not allowed!")
                }
            }
        }
        data class Condition(val prop : String, val number : Int, val lt : Boolean, val alwaysTrue : Boolean) {
            operator fun invoke(part : Part) = if (alwaysTrue) true else {
                if (lt) part[prop] < number else part[prop] > number
            }
        }
        data class Rule(val name : String) {
            val subRules = mutableMapOf<Condition, Rule>()

            operator fun invoke(part : Part) : Boolean {
                if (name == "A") {
                    return true
                } else if (name == "R") {
                    return false
                } else {
                    for (cr in subRules) {
                        if (cr.key(part)) {
                            return cr.value(part)
                        }
                    }
                    error("Not allowed!")
                }
            }


        }
        val ruleMap = mutableMapOf<String, Rule>()
        +part {
            val accept = Rule("A")
            val reject = Rule("R")
            ruleMap["A"] = accept
            ruleMap["R"] = reject
            for (i in 0..<rows.indexOfFirst { it.isEmpty() }) {
                val name = rows[i].substring(0, rows[i].indexOfFirst { it == '{' })
                ruleMap[name] = Rule(name)
            }
            for (rule in ruleMap.values.filterNot { it == accept || it == reject }) {
                val str = rows.filter { it.startsWith(rule.name + "{") }[0]
                val ruleString = str.substring(str.indexOf('{') + 1, str.lastIndexOf('}'))
                val subRules = ruleString.split(",")
                for (subRule in subRules) {
                    if (subRule.contains("<")) {
                        val condition = Condition(subRule.substring(0, subRule.indexOf('<')),
                            subRule.substring(subRule.indexOf('<') + 1, subRule.indexOf(':')).toInt(), true, false)
                        rule.subRules[condition] = ruleMap[subRule.substring(subRule.indexOf(':') + 1)] ?:
                            error("No rule called ${subRule.substring(subRule.indexOf(':') + 1)}")
                    } else if (subRule.contains(">")) {
                        val condition = Condition(subRule.substring(0, subRule.indexOf('>')),
                            subRule.substring(subRule.indexOf('>') + 1, subRule.indexOf(':')).toInt(), false, false)
                        rule.subRules[condition] = ruleMap[subRule.substring(subRule.indexOf(':') + 1)] ?:
                                error("No rule called ${subRule.substring(subRule.indexOf(':') + 1)}")
                    } else {
                        val condition = Condition("", 0, false, true)
                        rule.subRules[condition] = ruleMap[subRule] ?:
                            error("No rule called ${subRule.substring(subRule.indexOf(':') + 1)}")
                    }
                }
            }
            val inRule = ruleMap["in"] ?: error("no in rule!")
            val partList = mutableListOf<Part>()
            val acceptedSet = mutableSetOf<Part>()
            for (i in (rows.indexOfFirst { it.isEmpty() } + 1)..<rows.size) {
                val split = rows[i].split(",")
                val x = split[0].substring(split[0].indexOf('=') + 1).toInt()
                val m = split[1].substring(split[1].indexOf('=') + 1).toInt()
                val a = split[2].substring(split[2].indexOf('=') + 1).toInt()
                val s = split[3].substring(split[3].indexOf('=') + 1, split[3].length - 1).toInt()
                val part = Part(x, m, a, s)
                partList += part

                if (inRule(part)) {
                    acceptedSet += part
                }
            }

            result = acceptedSet.asSequence().map { it.x + it.m + it.a + it.s }.sum()
        }

        part {

        }
    }
}
