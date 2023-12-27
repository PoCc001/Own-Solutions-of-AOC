fun main() {
    data class Brick(val id : Int) {
        var x = 0
        var y = 0
        var z = 0
        var width = 0
        var depth = 0
        var height = 0
        val holdingBricks = mutableListOf<Brick>()
        val heldBricks = mutableListOf<Brick>()

        fun setParameters(str : String) {
            val first = str.split("~")[0].split(",").map { it.toInt() }
            val second = str.split("~")[1].split(",").map { it.toInt() }
            x = first[0]
            y = first[1]
            z = first[2]
            width = second[0] - x + 1
            depth = second[1] - y + 1
            height = second[2] - z + 1
        }

        fun down() {
            z--
        }

        fun disintegrate() : Int {
            val brickFringe = mutableListOf<Brick>()
            var count = 0
            brickFringe += this

            while (brickFringe.isNotEmpty()) {
                val brick = brickFringe.removeAt(0)
                brick.heldBricks.forEach {
                    it.holdingBricks.remove(brick)
                    if (it.holdingBricks.isEmpty()) {
                        brickFringe += it
                        count++
                    }
                }
            }

            return count
        }
    }

    aoc(22) {
        var bricks = mutableListOf<Brick>()
        +part {
            bricks = MutableList(rows.size) { Brick(it) }
            bricks.forEachIndexed { index, b -> b.setParameters(rows[index]) }
            bricks.sortBy { it.z }
            for (brick in bricks) {
                val bricksBelow = bricks.filter { it.z < brick.z }
                if (bricksBelow.isEmpty()) {
                    brick.z = 1
                } else {
                    while (brick.z > 1) {
                        val supportingBricks = bricksBelow.filter { brick.z == it.z + it.height &&
                                brick.x < it.x + it.width && brick.x + brick.width > it.x && brick.y < it.y + it.depth && brick.y + brick.depth > it.y }
                        if (supportingBricks.isNotEmpty()) {
                            brick.holdingBricks += supportingBricks
                            supportingBricks.forEach { it.heldBricks += brick }
                            break
                        }
                        brick.down()
                    }
                }
            }

            var count = 0
            for (brick in bricks) {
                if (brick.heldBricks.none { it.holdingBricks.size == 1 }) {
                    count++
                }
            }
            result = count
        }

        +part {
            var count = 0
            val indices = bricks.indices
            for (i in indices) {
                // use this to refresh the bricks, because they are modified in order to determine the number of falling bricks (this takes some time!)
                repeatPreviousPart()
                val brick = bricks[i]
                count += brick.disintegrate()
            }
            result = count
        }
    }
}
