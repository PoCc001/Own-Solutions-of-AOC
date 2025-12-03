fun main() {
    aoc(1) {
        +part {
            result = input.sequence.filter { it.isNotEmpty() }.
                fold(listOf(50)) { l, i ->
                    if (i.startsWith('L')) {
                        l + ((l.last() - i.substring(1).toInt()) % 100)
                    } else {
                        l + ((l.last() + i.substring(1).toInt()) % 100)
                    } }.
                count { it == 0 }
        }

        +part {
            var count = 0
            var rotation = 50
            for (i in input.sequence.filter { it.isNotEmpty() }) {
                val clicks = i.substring(1).toInt()
                count += clicks / 100
                val extra = clicks % 100
                if (i.startsWith('L')) {
                    if (extra >= rotation && rotation != 0) {
                        count++
                    }
                    rotation = (rotation - extra) % 100
                    if (rotation < 0) {
                        rotation += 100
                    }
                } else {
                    if (rotation + extra >= 100) {
                        count++
                    }
                    rotation = (rotation + extra) % 100
                }
            }

            result = count
        }
    }
}
