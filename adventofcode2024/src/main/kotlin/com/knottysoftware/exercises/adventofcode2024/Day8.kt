package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

suspend fun Day8a(lines: Flow<String>): Any {
  return Day8(lines.toList(), true)
}

suspend fun Day8b(lines: Flow<String>): Any {
  return Day8(lines.toList(), false)
}

private fun Day8(lines: List<String>, modeFirst: Boolean): Int {
  val grid = gridFromStrings(lines.toList())
  val antennas = mutableMapOf<Char, MutableSet<Point>>()
  grid.visit { x, y, v ->
    if (v != '.') {
      val freq: MutableSet<Point> = antennas.getOrPut(v, { mutableSetOf<Point>() })
      freq.add(Point(x, y))
    }
  }

  val antinodes = mutableSetOf<Point>()
  for (freq in antennas.keys) {
    // println ("CHECKING $freq")
    antinodes.addAll(findAntinodes(antennas[freq]!!.toList(), grid, modeFirst))
  }
  // println(antinodes)
  return antinodes.size
}

private fun findAntinodes(antennas: List<Point>, grid: Grid<*>, modeFirst: Boolean): Set<Point> {
  val nodes = mutableSetOf<Point>()

  for (i in 0..<antennas.size) {
    for (j in (i + 1)..<antennas.size) {
      val a = antennas[i]
      val b = antennas[j]

      // Technically for a real number line there's four antinodes, but this data is very...
      // "specially crafted".
      // Every line is on an angle that has no interior points (gcd(dx, dy) == 1).
      // So the antinodes are by definition always at dx,dy away from each antenna.
      val dx = b.x - a.x
      val dy = b.y - a.y

      if (modeFirst) {
        val zx = Point(a.x - dx, a.y - dy)
        val zy = Point(b.x + dx, b.y + dy)
        if (grid.contains(zx)) nodes.add(zx)
        if (grid.contains(zy)) nodes.add(zy)
      } else {
        var p = a
        while (grid.contains(p)) {
          nodes.add(p)
          p = Point(p.x - dx, p.y - dy)
        }

        p = b
        while (grid.contains(p)) {
          nodes.add(p)
          p = Point(p.x + dx, p.y + dy)
        }
      }
    }
  }
  return nodes
}
