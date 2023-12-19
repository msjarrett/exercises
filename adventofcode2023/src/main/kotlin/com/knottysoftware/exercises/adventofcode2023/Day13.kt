package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day13 : Exercise {
    private lateinit var grids: List<Grid<Char>>

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
        grids = lines.toList().splitOnBlank().map(::gridFromStrings)
    }

    fun reflectionScore(grid: Grid<Char>, ignoreScore: Int = 0): Int {
        // R is the column on the right side of the reflection.
        XRange@ for (r in 1 .. grid.maxX) {
            for (y in 0 .. grid.maxY) {
                for (x in r .. grid.maxX) {
                    val left = r - ((x - r) + 1)
                    if (left < 0) break
                    if (grid.at(x, y) != grid.at(left, y)) continue@XRange
                }
            }
            // Full match on column
            if (r != ignoreScore) {
                //println("Reflect column $r")
                return r
            }
        }

        // R is the row below the reflection.
        YRange@ for (r in 1 .. grid.maxY) {
            for (x in 0 .. grid.maxX) {
                for (y in r .. grid.maxY) {
                    val top = r - ((y - r) + 1)
                    if (top < 0) break
                    if (grid.at(x,y) != grid.at(x, top)) continue@YRange
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

    fun smudgeScore(grid: Grid<Char>): Int {
        val originalScore = reflectionScore(grid)
        val mgrid = grid.toMutableGrid()

        for (y in 0 .. mgrid.maxY) {
            for (x in 0 .. mgrid.maxX) {
                val origChar = mgrid.at(x,y)
                if (origChar == '#') mgrid.set('.', x, y)
                else if (origChar == '.') mgrid.set('#', x, y)
                else throw IllegalArgumentException()

                val score = reflectionScore(mgrid, originalScore)
                if (score != 0) {
                    //println("Smudge $x $y")
                    return score
                }
                mgrid.set(origChar, x, y)
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