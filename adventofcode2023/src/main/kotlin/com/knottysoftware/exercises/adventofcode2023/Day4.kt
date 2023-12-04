package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlin.math.pow

class Day4 : Exercise {
    data class Card(val num: Int, val wins: List<Int>, val nums: List<Int>)
    private lateinit var cards: List<Card>

    override val testInput = """
Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent()

    override val testResultPart1 = 13
    override val testResultPart2 = 30

    private val lineRegex = Regex("""Card\s+(\d+): ([^|]+) \| (.+)""")

    override suspend fun parse(lines: Flow<String>) {
        cards = lines.map {
            val match = lineRegex.matchEntire(it)!!.groups
            Card(
                match[1]!!.value.toInt(),
                match[2]!!.value.trim().split(" ").filter { it != ""}.map { it.toInt()},
                match[3]!!.value.trim().split(" ").filter { it != ""}.map { it.toInt()})
        }.toList()
    }

    override suspend fun partOne(): Any {
        return cards.map {
            val matches = it.wins.intersect(it.nums)
            if (matches.isEmpty())
                0
            else
                2f.pow(matches.size - 1).toInt()
        }.sum()
    }

    override suspend fun partTwo(): Any {
        val totalTickets = MutableList(cards.size, {1})
        for (c in cards) {
            val matches = c.wins.intersect(c.nums)
            for (i in c.num ..< c.num + matches.size) {
                if (i < totalTickets.size)
                totalTickets[i] += totalTickets[c.num - 1]
            }
        }
        return totalTickets.sum()
    }
}