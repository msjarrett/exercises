package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

suspend fun Day10a(lines: Flow<String>): Any {
  val map = gridFromStrings(lines.toList())
  val trailheads = buildList { map.visit { x, y, v -> if (v == '0') add(Point(x, y)) } }

  return trailheads.map { findNines(map, it) }.sum()
}

suspend fun Day10b(lines: Flow<String>): Any {
  val map = gridFromStrings(lines.toList())
  val trailheads = buildList { map.visit { x, y, v -> if (v == '0') add(Point(x, y)) } }

  return trailheads.map { rateTrail(map, it) }.sum()
}

private fun findNines(map: Grid<Char>, trailhead: Point): Int {
  val visited = mutableGrid<Boolean>(map.width, map.height, false)
  val queue = SearchQueue(trailhead)
  var count = 0
  for (p in queue) {
    // We pre-validate for bounds and height, but not for visited.
    if (!visited.at(p)) {
      visited.set(true, p.x, p.y)

      val gridValue = map.at(p)
      if (gridValue == '9') {
        count++
      } else {
        val candidates = Direction.cardinalDirections.map { p.move(it) }
        for (c in candidates) {
          if (map.contains(c) && map.at(c) == (gridValue + 1)) {
            queue.addDFS(c)
          }
        }
      }
    }
  }

  return count
}

private fun rateTrail(map: Grid<Char>, trailhead: Point): Int {
  val queue = SearchQueue(trailhead)
  var count = 0
  for (p in queue) {
    // Because trails can't backtrack, and overlapping segments are okay, we simply get rid of the
    // visited graph!
    val gridValue = map.at(p)
    if (gridValue == '9') {
      count++
    } else {
      val candidates = Direction.cardinalDirections.map { p.move(it) }
      for (c in candidates) {
        if (map.contains(c) && map.at(c) == (gridValue + 1)) {
          queue.addDFS(c)
        }
      }
    }
  }
  // println("$trailhead -> $count")
  return count
}
