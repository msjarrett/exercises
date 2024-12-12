package com.knottysoftware.exercises.adventofcode.common

import kotlin.math.abs
import kotlin.math.min

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

  companion object {
    val cardinalDirections = listOf(UP, DOWN, LEFT, RIGHT)
  }
}

data class Point(val x: Int = 0, val y: Int = 0) {
  fun move(dir: Direction, dist: Int = 1): Point =
      when (dir) {
        Direction.UP -> Point(x, y - dist)
        Direction.UPRIGHT -> Point(x + dist, y - dist)
        Direction.RIGHT -> Point(x + dist, y)
        Direction.DOWNRIGHT -> Point(x + dist, y + dist)
        Direction.DOWN -> Point(x, y + dist)
        Direction.DOWNLEFT -> Point(x - dist, y + dist)
        Direction.LEFT -> Point(x - dist, y)
        Direction.UPLEFT -> Point(x - dist, y - dist)
      }

  fun line(dir: Direction, dist: Int): List<Point> = buildList {
    var p = this@Point
    repeat(dist) {
      add(p)
      p = p.move(dir)
    }
  }

  fun directionTo(dest: Point): Direction? {
    if (dest.x == x) {
      if (dest.y < y) return Direction.UP else if (dest.y > y) return Direction.DOWN
    } else if (dest.y == y) {
      if (dest.x < x) return Direction.LEFT else if (dest.x > x) return Direction.RIGHT
    }
    return null
  }

  fun slopeTo(dest: Point): Point {
    val dx = dest.x - x
    val dy = dest.y - y

    if (dx == 0 && dy == 0) return Point(0, 0)
    else if (dx == 0) return Point(0, if (dy < 0) -1 else 1)
    else if (dy == 0) return Point(if (dx < 0) -1 else 1, 0)
    else {
      val g = gcd(dx, dy)
      return Point(dx / g, dy / g)
    }
  }

  fun manhattanDistanceTo(dest: Point): Int = abs(x - dest.x) + abs(y - dest.y)
}

private fun gcd(a: Int, b: Int): Int {
  var absA = abs(a)
  var absB = abs(b)
  val checkTo = min(absA, absB)

  var best = 1

  // TODO: Better this
  for (i in 2..checkTo) {
    if (absA % i == 0 && absB % i == 0) best = i
  }
  return best
}
