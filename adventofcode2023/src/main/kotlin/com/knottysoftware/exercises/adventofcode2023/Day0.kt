package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

/** Sample to test the 2023 exercise infrastructure. */
class Day0 : Exercise {
    private lateinit var nums: Flow<Int>

    override val testInput = """
       1
       2
       3
    """.trimIndent()

    override val testResultPart1 = 6
    override val testResultPart2 = 2

    override suspend fun parse(lines: Flow<String>) {
        nums = lines.map {
            it.toInt()
        }
    }

    override suspend fun partOne(): Any {
        return nums.toList().sum()
    }

    override suspend fun partTwo(): Any {
        val list = nums.toList()
        return list.sum() / list.count()
    }
}