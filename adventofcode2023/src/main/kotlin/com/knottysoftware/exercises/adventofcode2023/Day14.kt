package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class Day14 : Exercise {
    private lateinit var grid: Gridy

    override val testInput = """
O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
    """.trimIndent()

    override val testResultPart1 = 136
    override val testResultPart2 = 64

    override suspend fun parse(lines: Flow<String>) {
        grid = lines.map {
            it.toList()
        }.toList()
    }

    fun tiltNorth(grid: Gridy): Gridy {
        val newGrid = List(grid.size) {
            MutableList(grid[0].size) {
                '.'
            }
        }

        for (x in 0 ..< grid[0].size) {
            var nextY = 0
            for (y in 0 ..< grid.size) {
                if (grid[y][x] == 'O') {
                    newGrid[nextY++][x] = 'O'
                } else if (grid[y][x] == '#') {
                    newGrid[y][x] = '#'
                    nextY = y + 1
                }
            }
        }

        return newGrid
    }

    fun tiltSouth(grid: Gridy): Gridy {
        val newGrid = List(grid.size) {
            MutableList(grid[0].size) {
                '.'
            }
        }

        for (x in 0 ..< grid[0].size) {
            var nextY = grid.size - 1
            for (y in (grid.size - 1) downTo 0) {
                if (grid[y][x] == 'O') {
                    newGrid[nextY--][x] = 'O'
                } else if (grid[y][x] == '#') {
                    newGrid[y][x] = '#'
                    nextY = y - 1
                }
            }
        }

        return newGrid
    }

    fun tiltWest(grid: Gridy): Gridy {
        val newGrid = List(grid.size) {
            MutableList(grid[0].size) {
                '.'
            }
        }

        for (y in 0 ..< grid.size) {
            var nextX = 0
            for (x in 0 ..< grid[0].size) {
                if (grid[y][x] == 'O') {
                    newGrid[y][nextX++] = 'O'
                } else if (grid[y][x] == '#') {
                    newGrid[y][x] = '#'
                    nextX = x + 1
                }
            }
        }

        return newGrid
    }

    fun tiltEast(grid: Gridy): Gridy {
        val newGrid = List(grid.size) {
            MutableList(grid[0].size) {
                '.'
            }
        }

        for (y in 0 ..< grid.size) {
            var nextX = grid[0].size - 1
            for (x in (grid[0].size - 1) downTo 0) {
                if (grid[y][x] == 'O') {
                    newGrid[y][nextX--] = 'O'
                } else if (grid[y][x] == '#') {
                    newGrid[y][x] = '#'
                    nextX = x - 1
                }
            }
        }

        return newGrid
    }

    fun tiltCycle(grid: Gridy): Gridy {
        return tiltEast(tiltSouth(tiltWest(tiltNorth(grid))))
    }

    fun northWeight(grid: Gridy): Int {
        val rows = grid.size

        return grid.mapIndexed { y, row ->
            row.count { it == 'O' } * (rows - y)
        }.sum()
    }

    override suspend fun partOne() = northWeight(tiltNorth(grid))

    override suspend fun partTwo(): Int {
        var myGrid = grid
        val mimo = mutableMapOf(myGrid to 0)

        // Find the first cycle in the pattern.
        var iter = 0
        while (true) {
            iter++
            myGrid = tiltCycle(myGrid)
            if (mimo.containsKey(myGrid))  break
            mimo[myGrid] = iter
        }

        val oldIter = mimo[myGrid]!!
        //println("Found loop iter $oldIter -> $iter")
        val loop = iter - oldIter

        while (iter + loop < 1000000000) iter += loop

        while (iter != 1000000000) {
            iter++
            myGrid = tiltCycle(myGrid)
        }

        return northWeight(myGrid)
    }
}