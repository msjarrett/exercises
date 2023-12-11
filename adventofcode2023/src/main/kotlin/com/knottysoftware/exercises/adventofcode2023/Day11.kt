package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day11 : Exercise {
    private lateinit var universe: List<Point>
    private var maxX = 0
    private var maxY = 0

    override val testInput = """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """.trimIndent()

    override val testResultPart1 = 374L

    override suspend fun parse(lines: Flow<String>) {
        universe = lines.toList().mapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                if (c == '#')
                    Point(x, y)
                else
                    null
            }
        }.flatten()
        maxX = universe.map { it.x }.max()
        maxY = universe.map { it.y }.max()
    }

    fun expandUniverse(u: List<Point>, factor: Int): List<Point> {
        val emptyX = mutableListOf<Int>()
        val emptyY = mutableListOf<Int>()

        for (x in 0 .. maxX) {
            if (u.none { it.x == x }) emptyX.add(x)
        }
        for (y in 0 .. maxY) {
            if (u.none { it.y == y }) emptyY.add(y)
        }

        return u.map {
            Point(
                it.x + emptyX.count { x -> x < it.x } * (factor - 1),
                it.y + emptyY.count { y -> y < it.y } * (factor - 1))
        }
    }

    fun distanceSum(factor: Int): Long {
        val expUni = expandUniverse(universe, factor)

        // Is there a pointwise merge operation in Kotlin? Meh, do it ourselves.
        val distances = mutableListOf<Long>()
        for (i in 0 ..< expUni.size) {
            for (j in (i + 1) ..< expUni.size) {
                distances.add(expUni[i].manhattanDistanceTo(expUni[j]).toLong())
            }
        }
        return distances.sum()
    }

    override suspend fun partOne() = distanceSum(2)

    // The data only has 7 empty cols and 8 empty rows, so individual points should still be well within Int range.
    // The sum though will need Long.
    override suspend fun partTwo() = distanceSum(1000000)
}