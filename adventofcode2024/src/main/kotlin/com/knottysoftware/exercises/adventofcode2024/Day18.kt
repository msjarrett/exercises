package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

fun countPath(width: Int, height: Int, blocked: Set<Point>): Int {
  val visited = mutableSetOf<Point>()
  val goal = Point(width - 1, height - 1)
  val queue = SearchQueue(Pair(Point(0, 0), 0))
  for ((p, l) in queue) {
    if (p.x >= width || p.y >= height || p.x < 0 || p.y < 0) continue // Bounds check
    if (visited.contains(p)) continue // Already here (and it's BFS, so don't need to redo).
    if (blocked.contains(p)) continue // Space is blocked

    visited.add(p)
    if (p == goal) return l

    for (d in Direction.cardinalDirections) {
      queue.addBFS(Pair(p.move(d), l + 1))
    }
  }

  return 0
}

suspend fun Day18a(lines: Flow<String>): Any {
  val points =
      lines
          .map {
            val split = it.split(",")
            Point(split[0].toInt(), split[1].toInt())
          }
          .toList()

  // Reconfigured for sample vs input
  val paramX = if (points.size > 30) 71 else 7
  val paramY = if (points.size > 30) 71 else 7
  val paramTurns = if (points.size > 30) 1024 else 12

  return countPath(paramX, paramY, points.take(paramTurns).toSet())
}

suspend fun Day18b(lines: Flow<String>): Any {
  val points =
      lines
          .map {
            val split = it.split(",")
            Point(split[0].toInt(), split[1].toInt())
          }
          .toList()

  val paramX = if (points.size > 30) 71 else 7
  val paramY = if (points.size > 30) 71 else 7

  // Binary search for the last turn
  var goodTurns = 0
  var badTurns = points.size - 1

  while (badTurns - goodTurns > 1) {
    val turns = (badTurns + goodTurns) / 2
    // Input only goes 11 iterations. Binary search is great.
    // print("GOOD $goodTurns < $turns < $badTurns BAD =")
    val len = countPath(paramX, paramY, points.take(turns).toSet())
    // println(len)
    if (len == 0) badTurns = turns else goodTurns = turns
  }

  // println("$goodTurns (${points[goodTurns - 1]}) < $badTurns (${points[badTurns - 1]})")
  val blocker = points[badTurns - 1]
  return "${blocker.x},${blocker.y}"
}
