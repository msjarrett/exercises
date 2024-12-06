package com.knottysoftware.exercises.adventofcode2024

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import com.knottysoftware.exercises.adventofcode.common.*

suspend fun Day6a(lines: Flow<String>): Any {
    val grid = gridFromStrings(lines.toList())
    var guard = Point(-1, 1)
    var guardDir = Direction.UP

    // Find the guard
    grid.visit { x, y, v ->
        if (v == '^')   // The guard can only start facing up.
            guard = Point(x, y)
    }
    check (guard.x != -1)

    // Run the grid
    val visited = mutableGrid(grid.width, grid.height, false)
    while (grid.contains(guard)) {
        //println("$guard $guardDir")
        visited.set(true, guard.x, guard.y)
        val next = guard.move(guardDir)

        if (grid.contains(next) && grid.at(next) == '#') guardDir = guardDir.turnRight()
        else guard = next
    }

    return visited.count { it }
}


fun createsLoop(grid: Grid<Char>, guardStart: Point, obstruction: Point): Boolean {
    val visited = mutableGrid<MutableList<Direction>>(grid.width, grid.height) { x, y ->
        // We need a NEW MutableList for each tile.
        mutableListOf<Direction>()
    }
    

    var guard = guardStart
    var guardDir = Direction.UP

    while (grid.contains(guard)) {
        val dirsVisited = visited.at(guard)
        if (dirsVisited.contains(guardDir)) break
        dirsVisited.add(guardDir)

        val next = guard.move(guardDir)

        if (next == obstruction || (grid.contains(next) && grid.at(next) == '#')) guardDir = guardDir.turnRight()
        else guard = next
    }

    return grid.contains(guard)
}

suspend fun Day6b(lines: Flow<String>): Any {
    val grid = gridFromStrings(lines.toList())
    var guardStart = Point(-1, 1)

    // Find the guard
    grid.visit { x, y, v ->
        if (v == '^')   // The guard can only start facing up.
            guardStart = Point(x, y)
    }

    // We only need to evaluate tiles that the unobstructed path vists.
    val visited = mutableGrid(grid.width, grid.height, false)
    var guard = guardStart
    var guardDir = Direction.UP
    while (grid.contains(guard)) {
        visited.set(true, guard.x, guard.y)
        val next = guard.move(guardDir)

        if (grid.contains(next) && grid.at(next) == '#') guardDir = guardDir.turnRight()
        else guard = next
    }

    var blocks = 0
    visited.visit { x, y, v -> 
        if (v) {
            if (createsLoop(grid, guardStart, Point(x, y))) blocks++
        }
    }

    return blocks
}