package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

typealias Grid = List<List<Char>>

class Day13 : Exercise {
    private lateinit var grids: List<Grid>

    override val testInput = """
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
    """.trimIndent()

    override val testResultPart1 = 405
    override val testResultPart2 = 400

    override suspend fun parse(lines: Flow<String>) {
        val grids = mutableListOf<Grid>()
        var grid = mutableListOf<List<Char>>()
        for (line in lines.toList()) {
            if (line == "") {
                grids.add(grid)
                grid = mutableListOf()
            } else {
                grid.add(line.toList())
            }
        }
        grids.add(grid)
        this.grids = grids
    }

    fun reflectionScore(grid: Grid, ignoreScore: Int = 0): Int {
        val maxY = grid.size - 1
        val maxX = grid[0].size - 1

        // R is the column on the right side of the reflection.
        XRange@ for (r in 1 .. maxX) {
            for (y in 0 .. maxY) {
                for (x in r .. maxX) {
                    val left = r - ((x - r) + 1)
                    if (left < 0) break
                    if (grid[y][x] != grid[y][left]) continue@XRange
                }
            }
            // Full match on column
            if (r != ignoreScore) {
                //println("Reflect column $r")
                return r
            }
        }

        // R is the row below the reflection.
        YRange@ for (r in 1 .. maxY) {
            for (x in 0 .. maxX) {
                for (y in r .. maxY) {
                    val top = r - ((y - r) + 1)
                    if (top < 0) break
                    if (grid[y][x] != grid[top][x]) continue@YRange
                }
            }

            // Full match on row
            if ((r * 100) != ignoreScore) {
                //println("Reflect row $r")
                return r * 100
            }
        }

        // Return 0, it's faster than catching for part 2.
        return 0
    }

    fun smudgeScore(grid: Grid): Int {
        val originalScore = reflectionScore(grid)
        val mgrid = grid.map { it.toMutableList() }
        for (y in 0 ..< mgrid.size) {
            for (x in 0 ..< mgrid[0].size) {
                val origChar = mgrid[y][x]
                if (origChar == '#') mgrid[y][x] = '.'
                else if (origChar == '.') mgrid[y][x] = '#'
                else throw IllegalArgumentException()

                val score = reflectionScore(mgrid, originalScore)
                if (score != 0) {
                    //println("Smudge $x $y")
                    return score
                }
                mgrid[y][x] = origChar
            }
        }
        return 0
    }

    override suspend fun partOne(): Int {
        return grids.map {
            val res = reflectionScore(it)
            if (res ==  0) throw IllegalArgumentException("No reflection")
            res
        }.sum()
    }

    override suspend fun partTwo(): Int {
        return grids.map {
            val res = smudgeScore(it)
            if (res ==  0) throw IllegalArgumentException("No reflection")
            res
        }.sum()
    }
}