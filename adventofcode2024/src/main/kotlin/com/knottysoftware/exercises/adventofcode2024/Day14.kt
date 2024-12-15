package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

private val regex = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""")

private fun parseRobots(lines: Flow<String>): Flow<Pair<Point, Point>> =
    lines.map {
      val groups = regex.matchEntire(it)!!.groupValues
      Pair(Point(groups[1].toInt(), groups[2].toInt()), Point(groups[3].toInt(), groups[4].toInt()))
    }

// I'd bet part b is going to be to simulate for 10 billion seconds. Using prime number dimensions
// is another good hint something is amiss.
// But we'll play AoC's game and start with the naive solution and move them a second at a time.
// Since robots are independent of each other, we can map them in parallel.
private fun simulateRobots(robots: List<Pair<Point, Point>>, width: Int, height: Int, turns: Int) =
    robots.map {
      var pos = it.first
      val speed = it.second

      repeat(turns) { pos = pos.move(speed).wrap(width, height) }
      pos
    }

private fun safetyFactor(robots: List<Point>, width: Int, height: Int): Int {
  require(width % 2 == 1)
  require(height % 2 == 1)

  val midX = width / 2
  val midY = height / 2

  return robots.count { it.x < midX && it.y < midY } *
      robots.count { it.x > midX && it.y < midY } *
      robots.count { it.x < midX && it.y > midY } *
      robots.count { it.x > midX && it.y > midY }
}

private fun simulateSequential(robots: List<Pair<Point, Point>>, width: Int, height: Int): Int {
  val count = robots.size

  var newRobots = robots
  repeat(10000) { i ->
    val grid = mutableGrid<Char>(width, height, '.')
    for (r in newRobots) {
      grid.set('X', r.first.x, r.first.y)
    }
    var orphans = 0

    robotLoop@ for (r in newRobots) {
      for (d in Direction.allDirections) {
        val p = r.first.move(d)
        if (grid.contains(p) && grid.at(p) == 'X') continue@robotLoop
      }
      orphans++
    }
    // Final orphans of the solution was 25%.
    // println("Turn $i: orphans = ${orphans * 100 / count}%")
    if ((orphans * 3) < count) {
      // println(grid)
      // println("")
      // readLine()
      return i
    }

    newRobots = newRobots.map { Pair(it.first.move(it.second).wrap(width, height), it.second) }
  }
  return 0
}

suspend fun Day14a(lines: Flow<String>): Any {
  val robots = parseRobots(lines).toList()
  val width = 101
  val height = 103
  val turns = 100

  val finalPositions = simulateRobots(robots, width, height, turns)

  return safetyFactor(finalPositions, width, height)
}

suspend fun Day14b(lines: Flow<String>): Any {
  val robots = parseRobots(lines).toList()
  val width = 101
  val height = 103

  // Not sure what the xmas tree will look like.
  // Simulate a turn at a time until we see robots clustered together.
  return simulateSequential(robots, width, height)
}
