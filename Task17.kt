import java.math.BigInteger

fun combo(l: Int, regA: BigInteger, regB: BigInteger, regC: BigInteger) =
    if (l >= 0 && l <= 3) {
        l.toBigInteger()
    } else if (l == 4) {
        regA
    } else if (l == 5) {
        regB
    } else if (l == 6) {
        regC
    } else {
        error("Not a valid operand!")
    }

fun main() {
    aoc(17) {
        var regA = lines[0].substringAfterLast(" ").toBigInteger()
        var regB = lines[1].substringAfterLast(" ").toBigInteger()
        var regC = lines[2].substringAfterLast(" ").toBigInteger()

        val program = lines.last().substringAfter(" ").split(",").map { it.toInt() }
        +part {
            var ip = 0
            val out: MutableList<BigInteger> = mutableListOf()
            while (ip < program.size) {
                when(program[ip]) {
                    0 -> {
                        regA = regA shr (combo(program[ip + 1], regA, regB, regC).intValueExact())
                    }
                    1 -> {
                        regB = regB xor program[ip + 1].toBigInteger()
                    }
                    2 -> {
                        regB = combo(program[ip + 1], regA, regB, regC) and 7.toBigInteger()
                    }
                    3 -> {
                        if (regA.signum() != 0) {
                            ip = program[ip + 1] - 2
                        }
                    }
                    4 -> {
                        regB = regB xor regC
                    }
                    5 -> {
                        out += combo(program[ip + 1], regA, regB, regC) and 7.toBigInteger()
                    }
                    6 -> {
                        regB = regA shr (combo(program[ip + 1], regA, regB, regC).intValueExact())
                    }
                    7 -> {
                        regC = regA shr (combo(program[ip + 1], regA, regB, regC).intValueExact())
                    }
                }
                ip += 2
            }
            result = out.joinToString(",")
        }

        +part {
            // Double-checking program
            // The value was generated by the SMT solver Z3 using QF_BV
            val a = 0xF112687B039BL
            regA = BigInteger.valueOf(a)
            regB = lines[1].substringAfterLast(" ").toBigInteger()
            regC = lines[2].substringAfterLast(" ").toBigInteger()
            var ip = 0
            val out: MutableList<BigInteger> = mutableListOf()
            while (ip < program.size) {
                when(program[ip]) {
                    0 -> {
                        regA = regA shr (combo(program[ip + 1], regA, regB, regC).intValueExact())
                    }
                    1 -> {
                        regB = regB xor program[ip + 1].toBigInteger()
                    }
                    2 -> {
                        regB = combo(program[ip + 1], regA, regB, regC) and 7.toBigInteger()
                    }
                    3 -> {
                        if (regA.signum() != 0) {
                            ip = program[ip + 1] - 2
                        }
                    }
                    4 -> {
                        regB = regB xor regC
                    }
                    5 -> {
                        out += combo(program[ip + 1], regA, regB, regC) and 7.toBigInteger()
                    }
                    6 -> {
                        regB = regA shr (combo(program[ip + 1], regA, regB, regC).intValueExact())
                    }
                    7 -> {
                        regC = regA shr (combo(program[ip + 1], regA, regB, regC).intValueExact())
                    }
                }
                ip += 2
            }
            if (out.map { it.toInt() } != program) {
                error("Wrong value for regA!")
            }
            result = a
        }
    }
}