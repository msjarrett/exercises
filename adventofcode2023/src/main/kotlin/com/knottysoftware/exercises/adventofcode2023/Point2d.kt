// Imported from Advent of Code 2022.
package com.knottysoftware.exercises.adventofcode2023

import kotlin.math.abs

enum class Direction {
    UP {
        override fun turnLeft() = LEFT
        override fun turnRight() = RIGHT
        override fun turnReverse() = DOWN
    },
    UPRIGHT {
        override fun turnLeft() = UPLEFT
        override fun turnRight() = DOWNRIGHT
        override fun turnReverse() = DOWNLEFT
    },
    RIGHT {
        override fun turnLeft() = UP
        override fun turnRight() = DOWN
        override fun turnReverse() = LEFT
    },
    DOWNRIGHT {
        override fun turnLeft() = UPRIGHT
        override fun turnRight() = DOWNLEFT
        override fun turnReverse() = UPLEFT
    },
    DOWN {
        override fun turnLeft() = RIGHT
        override fun turnRight() = LEFT
        override fun turnReverse() = UP
    },
    DOWNLEFT {
        override fun turnLeft() = DOWNRIGHT
        override fun turnRight() = UPLEFT
        override fun turnReverse() = UPRIGHT
    },
    LEFT {
        override fun turnLeft() = DOWN
        override fun turnRight() = UP
        override fun turnReverse() = RIGHT
    },
    UPLEFT {
        override fun turnLeft() = DOWNLEFT
        override fun turnRight() = UPRIGHT
        override fun turnReverse() = DOWNLEFT
    };

    // Turn left 90'
    abstract fun turnLeft(): Direction
    // Turn right 90'
    abstract fun turnRight(): Direction
    // Turn right 90'
    abstract fun turnReverse(): Direction
}

data class Point(val x: Int = 0, val y: Int = 0) {
    fun move(dir: Direction): Point =
        when (dir) {
            Direction.UP -> Point(x, y - 1)
            Direction.UPRIGHT -> Point(x + 1, y - 1)
            Direction.RIGHT -> Point(x + 1, y)
            Direction.DOWNRIGHT -> Point(x + 1, y + 1)
            Direction.DOWN -> Point(x, y + 1)
            Direction.DOWNLEFT -> Point(x - 1, y + 1)
            Direction.LEFT -> Point(x - 1, y)
            Direction.UPLEFT -> Point(x - 1, y - 1)
        }

    fun directionTo(dest: Point): Direction? {
        if (dest.x == x) {
            if (dest.y < y) return Direction.UP else if (dest.y > y) return Direction.DOWN
        } else if (dest.y == y) {
            if (dest.x < x) return Direction.LEFT else if (dest.x > x) return Direction.RIGHT
        }
        return null
    }

    fun manhattanDistanceTo(dest: Point): Int = abs(x - dest.x) + abs(y - dest.y)
}
