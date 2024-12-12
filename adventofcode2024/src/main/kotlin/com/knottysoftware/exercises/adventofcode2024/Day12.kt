package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

import kotlin.collections.forEach

suspend fun Day12a(lines: Flow<String>): Any {
  val grid = gridFromStrings(lines.toList())
  val visited = mutableGrid(grid.width, grid.height, false)

  var cost = 0

  grid.visit { x, y, _ ->
    if (!visited.at(x, y)) {
        cost += costRegion(Point(x, y), grid, visited)
    }
  }

  return cost
}


suspend fun Day12b(lines: Flow<String>): Any {
  val grid = gridFromStrings(lines.toList())
  val visited = mutableGrid(grid.width, grid.height, false)

  var cost = 0

  grid.visit { x, y, _ ->
    if (!visited.at(x, y)) {
        cost += costRegion2(Point(x, y), grid, visited)
    }
  }

  return cost
}

private fun costRegion(start: Point, grid: Grid<Char>, visited: MutableGrid<Boolean>): Int {
  val plant = grid.at(start)
  var area = 0
  var perim = 0

  val queue = SearchQueue(start)
  for (p in queue) {
    if (!visited.contains(p)) continue
    if (visited.at(p)) continue
    if (grid.at(p) != plant) continue

    // New tile for our region.
    visited.set(true, p.x, p.y)
    area++

    if (p.x == 0) perim++
    if (p.x == grid.maxX) perim++
    if (p.y == 0) perim++
    if (p.y == grid.maxY) perim++

    if (p.x > 0 && grid.at(p.move(Direction.LEFT)) != plant) perim++
    if (p.x < grid.maxX && grid.at(p.move(Direction.RIGHT)) != plant) perim++
    if (p.y > 0 && grid.at(p.move(Direction.UP)) != plant) perim++
    if (p.y < grid.maxY && grid.at(p.move(Direction.DOWN)) != plant) perim++

    Direction.cardinalDirections.forEach { queue.addDFS(p.move(it)) }
  }

  val cost = area * perim
  //println("Region $plant ($start): area $area perim $perim = $cost")
  return cost
}

private fun costRegion2(start: Point, grid: Grid<Char>, visited: MutableGrid<Boolean>): Int {
  val plant = grid.at(start)
  var area = 0

  val edges = mutableSetOf<Pair<Point, Direction>>()

  // Map the region
  val queue = SearchQueue(start)
  for (p in queue) {
    if (!visited.contains(p)) continue
    if (visited.at(p)) continue
    if (grid.at(p) != plant) continue

    // New tile for our region.
    visited.set(true, p.x, p.y)
    area++

    if (p.x == 0) edges.add(Pair(p, Direction.LEFT))
    if (p.x == grid.maxX) edges.add(Pair(p, Direction.RIGHT))
    if (p.y == 0) edges.add(Pair(p, Direction.UP))
    if (p.y == grid.maxY) edges.add(Pair(p, Direction.DOWN))

    if (p.x > 0 && grid.at(p.move(Direction.LEFT)) != plant) edges.add(Pair(p, Direction.LEFT))
    if (p.x < grid.maxX && grid.at(p.move(Direction.RIGHT)) != plant) edges.add(Pair(p, Direction.RIGHT))
    if (p.y > 0 && grid.at(p.move(Direction.UP)) != plant) edges.add(Pair(p, Direction.UP))
    if (p.y < grid.maxY && grid.at(p.move(Direction.DOWN)) != plant) edges.add(Pair(p, Direction.DOWN))

    Direction.cardinalDirections.forEach { queue.addDFS(p.move(it)) }
  }

  // Map sides from edges
  var sides = 0
  val edgesVisited = mutableSetOf<Pair<Point, Direction>>()
  for (edgePair in edges) {
    if (edgesVisited.contains(edgePair)) continue
    sides++

    // Visit the rest of the side to the "left" and "right" relative to the side's direction.
    val dir = edgePair.second
    var p: Point

    val leftDir = dir.turnLeft()
    p = edgePair.first.move(leftDir)
    while (edges.contains(Pair(p, dir))) {
        edgesVisited.add(Pair(p, dir))
        p = p.move(leftDir)
    }

    val rightDir = dir.turnRight()
    p = edgePair.first.move(rightDir)
    while (edges.contains(Pair(p, dir))) {
        edgesVisited.add(Pair(p, dir))
        p = p.move(rightDir)
    }    
  }


  val cost = area * sides
  //println("Region $plant ($start): area $area sides $sides = $cost")
  return cost
}