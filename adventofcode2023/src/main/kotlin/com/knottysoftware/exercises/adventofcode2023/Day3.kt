package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day3 : Exercise {
    private lateinit var symbols: Map<Point, Char>
    private lateinit var nums: Map<Point, Int>

    override val testInput = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
    """.trimIndent()

    override val testResultPart1 = 4361
    override val testResultPart2 = 467835

    data class Point(val x: Int, val y: Int)

    override suspend fun parse(lines: Flow<String>) {
        val symbols = mutableMapOf<Point, Char>()
        val nums = mutableMapOf<Point, Int>()

        lines.toList().forEachIndexed { y, line ->
            var x = 0
            while (x < line.length) {
                if (line[x] == '.') {
                    // Nothing
                } else if (line[x].isDigit()) {
                    var x2 = x + 1
                    while (x2 < line.length && line[x2].isDigit()) x2++
                    nums[Point(x, y)] = line.substring(x, x2).toInt()
                    x = x2 - 1
                } else {
                    symbols[Point(x, y)] = line[x]
                }
                x++
            }
        }

        this.symbols = symbols
        this.nums = nums
    }

    override suspend fun partOne(): Int {
        return nums.filter {
            val p = it.key
            val len = it.value.toString().length
            if (symbols.containsKey(Point(p.x -1, p.y))) return@filter true
            if (symbols.containsKey(Point(p.x + len, p.y))) return@filter true
            for (x in (p.x - 1) .. (p.x + len)) {
                if (symbols.containsKey(Point(x, p.y - 1))) return@filter true
                if (symbols.containsKey(Point(x, p.y + 1))) return@filter true
            }
            return@filter false
        }.values.sum()
    }

    private fun findNums(p: Point): List<Int> {
        return nums.filter {
            val y = it.key.y
            val x1 = it.key.x
            val x2 = it.key.x + it.value.toString().length - 1

            y >= (p.y - 1) && y <= (p.y + 1) && x1 <= (p.x + 1) && x2 >= (p.x - 1)
        }.values.toList()
    }

    override suspend fun partTwo(): Any {
       return symbols.filter {
            it.value == '*'
        }.map {
            val nearNums = findNums(it.key)
            if (nearNums.size == 2)
                nearNums[0] * nearNums[1]
            else
                0
        }.sum()
    }
}