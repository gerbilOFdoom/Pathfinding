package technology.zim.data

enum class Directions(val dir: Int) {
    NONE(0),
    UP(1),
    DOWN(2),
    LEFT(4),
    RIGHT(8);

    companion object {
        fun opposite(dir: Directions): Directions {
            return when(dir) {
                UP -> DOWN
                DOWN -> UP
                LEFT -> RIGHT
                RIGHT -> LEFT
                else -> NONE
            }
        }
        fun getModifier(dir: Directions): Long {
            return when(dir) {
                UP -> NORTH
                DOWN -> SOUTH
                LEFT -> WEST
                RIGHT -> EAST
                else -> 0L
            }
        }

        fun convertModifier(mod: Long): Directions {
            return when(mod) {
                NORTH -> UP
                SOUTH -> DOWN
                WEST -> LEFT
                EAST -> RIGHT
                else -> NONE
            }
        }

        const val SOUTH: Long = 0x1L
        const val NORTH: Long = -0x1L
        const val WEST: Long = -0x100000000L
        const val EAST: Long = 0x100000000L
        val ALL = arrayOf(UP, DOWN, LEFT, RIGHT)
        const val NONEMOD = 0L
    }

    fun inv(): Int {
        return dir.inv()
    }
}