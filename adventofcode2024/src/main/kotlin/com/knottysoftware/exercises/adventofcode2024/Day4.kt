package com.knottysoftware.exercises.adventofcode2024

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList


suspend fun parseGrid(lines: Flow<String>): List<List<Char>> {    
    return lines.map {
        it.toCharArray().toList()
    }.toList()
}

suspend fun Day4a(lines: Flow<String>): Any {
    val grid = parseGrid(lines)
    val width = grid[0].size
    val height = grid.size
    
    var count = 0
    for (x in 0 ..< width) {
        for (y in 0 ..< height) {
            if (grid[y][x] != 'X') continue

            // Check for xmas in all eight directions.
            val options = buildList {
                // Left
                if (x >= 3) add(listOf(Pair(x, y), Pair(x - 1, y), Pair(x - 2, y), Pair(x - 3, y)))
                // Up-left
                if (x >= 3 && y >= 3) add(listOf(Pair(x, y), Pair(x - 1, y - 1), Pair(x - 2, y - 2), Pair(x - 3, y - 3)))
                // Up
                if (y >= 3) add(listOf(Pair(x, y), Pair(x, y - 1), Pair(x, y - 2), Pair(x, y - 3)))
                // Up-right
                if (x <= (width - 4) && y >= 3) add(listOf(Pair(x, y), Pair(x + 1, y - 1), Pair(x + 2, y - 2), Pair(x + 3, y - 3)))
                // Right
                if (x <= (width - 4)) add(listOf(Pair(x, y), Pair(x + 1, y), Pair(x + 2, y), Pair(x + 3, y)))
                // Down-right
                if (x <= (width - 4) && y <= (height - 4)) add(listOf(Pair(x, y), Pair(x + 1, y + 1), Pair(x + 2, y + 2), Pair(x + 3, y + 3)))
                // Down
                if (y <= (height - 4)) add(listOf(Pair(x, y), Pair(x, y + 1), Pair(x, y + 2), Pair(x, y + 3)))
                // Down-left
                if (x >= 3 && y <= (height - 4)) add(listOf(Pair(x, y), Pair(x - 1, y + 1), Pair(x - 2, y + 2), Pair(x - 3, y + 3)))
            }

            for (o in options) {
                if (grid[o[0].second][o[0].first] == 'X' &&
                    grid[o[1].second][o[1].first] == 'M' &&
                    grid[o[2].second][o[2].first] == 'A' &&
                    grid[o[3].second][o[3].first] == 'S')
                    count++
            }
        }
    }

    return count
}

suspend fun Day4b(lines: Flow<String>): Any {
    val grid = parseGrid(lines)
    val width = grid[0].size
    val height = grid.size
    
    var count = 0
    for (x in 1 ..< (width - 1)) {
        for (y in 1 ..< (height - 1)) {
            if (grid[y][x] != 'A') continue

            // Check for X-MAS.
            // First two points are the Ms, and last two are the S.
            // Since the shape is symmetric, we don't need bounds checks this time.
            val options = listOf(
                // Forward-forward
                listOf(Pair(x - 1, y - 1), Pair(x - 1, y + 1), Pair(x + 1, y - 1), Pair(x + 1, y + 1)),
                // Backward-backward
                listOf(Pair(x + 1, y - 1), Pair(x + 1, y + 1), Pair(x - 1, y - 1), Pair(x - 1, y + 1)),
                // Forward (top) backward (bottom)
                listOf(Pair(x - 1, y - 1), Pair(x + 1, y - 1), Pair(x + 1, y + 1), Pair(x - 1, y + 1)),
                // Backward (top) forward (bottom)
                listOf(Pair(x + 1, y + 1), Pair(x - 1, y + 1), Pair(x - 1, y - 1), Pair(x + 1, y - 1)),
            )

            for (o in options) {
                if (grid[o[0].second][o[0].first] == 'M' &&
                    grid[o[1].second][o[1].first] == 'M' &&
                    grid[o[2].second][o[2].first] == 'S' &&
                    grid[o[3].second][o[3].first] == 'S')
                    count++
            }
        }
    }

    return count
}