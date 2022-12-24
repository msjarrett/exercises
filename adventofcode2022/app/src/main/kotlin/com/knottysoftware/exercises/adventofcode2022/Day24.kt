package com.knottysoftware.exercises.adventofcode2022

class Day24 : Exercise {
  var width = 0
  var height = 0
  lateinit var blizzards: List<Pair<Point, Direction>>

  final val startPoint = Point(0, -1)
  lateinit var goalPoint: Point
  var goalDistance = 0

  override fun parse(lines: Sequence<String>) {
    // We ignore the walls, and simply try to travel from 0,-1, to w-1,h-1.
    // No input puts up/down on the first row.
    // We have to simulate the first and last moves.
    val blizzards = mutableListOf<Pair<Point, Direction>>()
    val map = lines.map { it.substring(1, it.length - 1) }.toList().drop(1).dropLast(1)
    height = map.size
    width = map[0].length
    goalPoint = Point(width - 1, height)
    goalDistance = startPoint.manhattanDistanceTo(goalPoint)
    for (y in 0 until height) {
      for (x in 0 until width) {
        when (map[y][x]) {
          '.' -> {}
          '<' -> blizzards.add(Pair(Point(x, y), Direction.LEFT))
          '>' -> blizzards.add(Pair(Point(x, y), Direction.RIGHT))
          '^' -> blizzards.add(Pair(Point(x, y), Direction.UP))
          'v' -> blizzards.add(Pair(Point(x, y), Direction.DOWN))
          else -> throw IllegalArgumentException()
        }
      }
    }

    this.blizzards = blizzards
  }

  fun renderOccupied(points: Set<Point>) {
    val flags = BooleanArray(width * height) { false }
    for (p in points) {
      flags[p.y * width + p.x] = true
    }
    for (y in 0 until height) {
      for (x in 0 until width) {
        if (flags[y * width + x]) print("*") else print(".")
      }
      println("")
    }
  }

  fun generateMap(turn: Int): Set<Point> {
    val occupied = mutableSetOf<Point>()
    for ((loc, dir) in blizzards) {
      var x = loc.x
      var y = loc.y
      when (dir) {
        Direction.LEFT -> {
          x -= turn % width
          if (x < 0) x += width
        }
        Direction.RIGHT -> {
          x += turn % width
          if (x >= width) x -= width
        }
        Direction.UP -> {
          y -= turn % height
          if (y < 0) y += height
        }
        Direction.DOWN -> {
          y += turn % height
          if (y >= height) y -= height
        }
        else -> throw IllegalArgumentException()
      }
      occupied.add(Point(x, y))
    }
    // println("TURN $turn")
    // renderOccupied(occupied)
    return occupied
  }

  // On what turn do we reach the goal?
  fun shortestRoute(startTurn: Int, start: Point, goal: Point): Int {
    val blizzardMaps = mutableMapOf<Int, Set<Point>>()
    val visitedMaps = mutableMapOf<Int, MutableSet<Point>>()

    // Search items are {current turn, last location}
    var bestComplete = Int.MAX_VALUE
    var closestSoFar = Int.MAX_VALUE
    val queue = SearchQueue(Pair(startTurn + 1, start))
    for ((turn, loc) in queue) {
      if (!blizzardMaps.contains(turn)) blizzardMaps[turn] = generateMap(turn)
      val blizzardMap = blizzardMaps[turn]!!

      if (!visitedMaps.contains(turn)) visitedMaps[turn] = mutableSetOf<Point>()
      val visited = visitedMaps[turn]!!

      // Are we at a goal state?
      // Since it's A*, we know we found the optimal route.
      if (loc == goal) {
        bestComplete = turn
        break
      }

      // Have we been here before?
      if (visited.contains(loc)) continue
      visited.add(loc)

      val remainingDistance = loc.manhattanDistanceTo(goalPoint)
      if (remainingDistance < closestSoFar) {
        closestSoFar = remainingDistance
        // println("TURN $turn, best $closestSoFar from exit")
      }

      // Generate new candidates.
      val candidates = mutableListOf<Point>()
      if (!blizzardMap.contains(loc))
          candidates.add(loc) // We can wait here, no blizzard lands on us.
      if (loc.x == 0 && loc.y == 0)
          candidates.add(loc.move(Direction.UP)) // We can move up to the entrance
      if (loc.x == width - 1 && loc.y == height - 1)
          candidates.add(loc.move(Direction.DOWN)) // We can move to the exit.

      val left = loc.move(Direction.LEFT)
      val right = loc.move(Direction.RIGHT)
      val up = loc.move(Direction.UP)
      val down = loc.move(Direction.DOWN)
      if (left.x >= 0 && left.y >= 0 && left.y < height && !blizzardMap.contains(left))
          candidates.add(left)
      if (right.x < width && right.y >= 0 && right.y < height && !blizzardMap.contains(right))
          candidates.add(right)
      if (up.y >= 0 && !blizzardMap.contains(up)) candidates.add(up)
      if (down.y < height && !blizzardMap.contains(down)) candidates.add(down)

      for (c in candidates) {
        val score = turn + c.manhattanDistanceTo(goalPoint)
        queue.addScored(Pair(turn + 1, c), score)
      }
    }
    return bestComplete - 1 // We started turn n+1 when we exited.
  }

  override fun partOne(): Int = shortestRoute(0, startPoint, goalPoint)

  override fun partTwo(): Int {
    var turn = 0
    turn = shortestRoute(turn, startPoint, goalPoint)
    turn = shortestRoute(turn, goalPoint, startPoint)
    turn = shortestRoute(turn, startPoint, goalPoint)
    return turn
  }
}
