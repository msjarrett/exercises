package com.knottysoftware.exercises.adventofcode2022

import kotlin.math.abs

enum class Direction {
  UP,
  UPRIGHT,
  RIGHT,
  DOWNRIGHT,
  DOWN,
  DOWNLEFT,
  LEFT,
  UPLEFT
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
