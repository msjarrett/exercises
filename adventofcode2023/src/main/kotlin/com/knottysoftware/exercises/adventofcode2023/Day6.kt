package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day6 : Exercise {
    private lateinit var races: List<Pair<Int, Int>>

    override val testInput = """
Time:      7  15   30
Distance:  9  40  200
    """.trimIndent()

    override val testResultPart1 = 288
    override val testResultPart2 = 71503

    override suspend fun parse(lines: Flow<String>) {
        val lineList = lines.toList()
        val times = lineList[0].substring(10).split(" ").filter { it != "" }.map(String::toInt)
        val distances = lineList[1].substring(10).split(" ").filter { it != "" }.map(String::toInt)

        assert(times.size == distances.size)

        val races = mutableListOf<Pair<Int, Int>>()
        for (i in 0 ..< times.size) {
            races.add(Pair(times[i], distances[i]))
        }
        this.races = races
    }

    fun raceWinsBruteForce(time: Int, minDistance: Long): Int {
        var wins = 0

        for (i in 1 ..< time) {
            if (i.toLong() * (time - i).toLong() > minDistance) wins++
        }
        return wins
    }

    override suspend fun partOne(): Int = races.map { raceWinsBruteForce(it.first, it.second.toLong()) }.reduce {acc, v -> acc * v}

    override suspend fun partTwo(): Int {
        val realTime = races.map { it.first }.joinToString("").toInt()
        val realDistance = races.map { it.second }.joinToString("").toLong()
        return raceWinsBruteForce(realTime, realDistance)
    }
}