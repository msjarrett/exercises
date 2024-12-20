package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

// Track route without cheats. The problem guarantees there's exactly one path
// so we can take all sorts of shortcuts.
fun trackRoute(grid: Grid<Char>, start: Point): List<Point> {
  // There's exactly one path, unless there's cheats.
  val queue = SearchQueue(listOf(start))
  for (path in queue) {
    val p = path.last()
    val last: Point? = if (path.size > 1) path[path.size - 2] else null
    if (grid.at(p) == 'E') return path

    val candidates =
        Direction.cardinalDirections
            .map { p.move(it) }
            .filter { grid.at(it) != '#' && (it != last) }
    check(candidates.size == 1)
    queue.addBFS(path.plus(candidates))
  }
  throw IllegalArgumentException("No path to end.")
}

suspend fun Day20a(lines: Flow<String>): Any = Day20(lines.toList(), 2)

suspend fun Day20b(lines: Flow<String>): Any = Day20(lines.toList(), 20)

private fun Day20(lines: List<String>, maxCheat: Int): Int {
  val grid = gridFromStrings(lines)
  val start = grid.find('S')!!
  val mainRoute = trackRoute(grid, start)

  // The REAL hint is that the cheats are start and end location, both on the route.
  return mainRoute
      .dropLast(1)
      .mapIndexed { i, p ->
        var goodCheats = 0
        for (j in (i + 1)..<mainRoute.size) {
          val dist = p.manhattanDistanceTo(mainRoute[j])
          if (dist <= maxCheat) {
            val savings = (j - i) - dist
            if (savings >= 100) {
              // println("Cheating saved $savings.")
              goodCheats++
            }
          }
        }
        goodCheats
      }
      .sum()
}
