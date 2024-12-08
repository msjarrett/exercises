package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

suspend fun parseGrid(lines: Flow<String>): List<List<Char>> {
  return lines.map { it.toCharArray().toList() }.toList()
}

suspend fun Day4a(lines: Flow<String>): Any {
  val grid = gridFromStrings(lines.toList())

  var count = 0
  grid.visit { x, y, v ->
    if (v == 'X') {
      val p = Point(x, y)
      val options = buildList {
        if (x >= 3) add(p.line(Direction.LEFT, 4))
        if (x >= 3 && y >= 3) add(p.line(Direction.UPLEFT, 4))
        if (y >= 3) add(p.line(Direction.UP, 4))
        if (x <= (grid.width - 4) && y >= 3) add(p.line(Direction.UPRIGHT, 4))
        if (x <= (grid.width - 4)) add(p.line(Direction.RIGHT, 4))
        if (x <= (grid.width - 4) && y <= (grid.height - 4)) add(p.line(Direction.DOWNRIGHT, 4))
        if (y <= (grid.height - 4)) add(p.line(Direction.DOWN, 4))
        if (x >= 3 && y <= (grid.height - 4)) add(p.line(Direction.DOWNLEFT, 4))
      }

      for (o in options) {
        if (grid.at(o[0]) == 'X' &&
            grid.at(o[1]) == 'M' &&
            grid.at(o[2]) == 'A' &&
            grid.at(o[3]) == 'S')
            count++
      }
    }
  }

  return count
}

suspend fun Day4b(lines: Flow<String>): Any {
  val grid = gridFromStrings(lines.toList())

  var count = 0
  grid.visit { x, y, v ->
    if (v == 'A' && x > 0 && x < grid.maxX && y > 0 && y < grid.maxY) {
      val p = Point(x, y)
      // Check for X-MAS.
      // First two points are the Ms, and last two are the S.
      // Since the shape is symmetric, we don't need bounds checks this time.
      val options =
          listOf(
              // Forward-forward
              listOf(
                  p.move(Direction.UPLEFT),
                  p.move(Direction.DOWNLEFT),
                  p.move(Direction.DOWNRIGHT),
                  p.move(Direction.UPRIGHT)),
              // Backward-backward
              listOf(
                  p.move(Direction.UPRIGHT),
                  p.move(Direction.DOWNRIGHT),
                  p.move(Direction.DOWNLEFT),
                  p.move(Direction.UPLEFT)),
              // Forward (top) backward (bottom)
              listOf(
                  p.move(Direction.UPLEFT),
                  p.move(Direction.UPRIGHT),
                  p.move(Direction.DOWNRIGHT),
                  p.move(Direction.DOWNLEFT)),
              // Backward (top) forward (bottom)
              listOf(
                  p.move(Direction.DOWNRIGHT),
                  p.move(Direction.DOWNLEFT),
                  p.move(Direction.UPLEFT),
                  p.move(Direction.UPRIGHT)),
          )
      for (o in options) {
        if (grid.at(o[0]) == 'M' &&
            grid.at(o[1]) == 'M' &&
            grid.at(o[2]) == 'S' &&
            grid.at(o[3]) == 'S')
            count++
      }
    }
  }

  return count
}
