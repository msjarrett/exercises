package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day14 : Exercise {
    private lateinit var grid: Grid<Char>

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
        grid = gridFromStrings(lines.toList())
    }

    fun tiltNorth(grid: Grid<Char>): Grid<Char> {
        val newGrid = mutableGrid(grid.width, grid.height) { _, _ -> '.' }

        for (x in 0 .. grid.maxX) {
            var nextY = 0
            for (y in 0 .. grid.maxY) {
                if (grid.at(x, y) == 'O') {
                    newGrid.set('O', x, nextY++)
                } else if (grid.at(x, y) == '#') {
                    newGrid.set('#', x, y)
                    nextY = y + 1
                }
            }
        }
        return newGrid
    }

    fun tiltSouth(grid: Grid<Char>): Grid<Char> {
        val newGrid = mutableGrid(grid.width, grid.height) { _, _ -> '.' }

        for (x in 0 .. grid.maxX) {
            var nextY = grid.maxY
            for (y in grid.maxY downTo 0) {
                if (grid.at(x, y) == 'O') {
                    newGrid.set('O', x, nextY--)
                } else if (grid.at(x, y) == '#') {
                    newGrid.set('#', x, y)
                    nextY = y - 1
                }
            }
        }
        return newGrid
    }

    fun tiltWest(grid: Grid<Char>): Grid<Char> {
        val newGrid = mutableGrid(grid.width, grid.height) { _, _ -> '.' }

        for (y in 0 .. grid.maxY) {
            var nextX = 0
            for (x in 0 .. grid.maxX) {
                if (grid.at(x, y) == 'O') {
                    newGrid.set('O', nextX++, y)
                } else if (grid.at(x, y) == '#') {
                    newGrid.set('#', x, y)
                    nextX = x + 1
                }
            }
        }

        return newGrid
    }

    fun tiltEast(grid: Grid<Char>): Grid<Char> {
        val newGrid = mutableGrid(grid.width, grid.height) { _, _ -> '.' }

        for (y in 0 .. grid.maxY) {
            var nextX = grid.maxX
            for (x in grid.maxX downTo 0) {
                if (grid.at(x, y) == 'O') {
                    newGrid.set('O', nextX--, y)
                } else if (grid.at(x, y) == '#') {
                    newGrid.set('#', x, y)
                    nextX = x - 1
                }
            }
        }

        return newGrid
    }

    fun tiltCycle(grid: Grid<Char>): Grid<Char> {
        return tiltEast(tiltSouth(tiltWest(tiltNorth(grid))))
    }

    fun northWeight(grid: Grid<Char>): Int {
        val rows = grid.maxY + 1

        return grid.toList().mapIndexed { y, row ->
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