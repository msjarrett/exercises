package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class Day9 : Exercise {
    private lateinit var hists: Flow<List<Int>>

    override val testInput = """
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
    """.trimIndent()

    override val testResultPart1 = 114
    override val testResultPart2 = 2

    override suspend fun parse(lines: Flow<String>) {
        hists = lines.map {
            it.split(" ").map { it.toInt() }
        }
    }

    fun nextHistory(hist: List<Int>): Int {
        val seqs = mutableListOf(hist)

        while(!seqs.last().all { it == 0} ) {
            // TODO: There's a better map solution in Kotlin I bet.
            val prev = seqs.last()
            val cur = mutableListOf<Int>()
            for (i in 1 ..< prev.size) {
                cur.add(prev[i] - prev[i - 1])
            }
            seqs.add(cur)
        }

        var newItem = 0
        for (cur in seqs.reversed().drop(1)) {
            newItem = cur.last() + newItem
        }
        return newItem
    }

    fun prevHistory(hist: List<Int>): Int {
        val seqs = mutableListOf(hist)

        while(!seqs.last().all { it == 0} ) {
            // TODO: There's a better map solution in Kotlin I bet.
            val prev = seqs.last()
            val cur = mutableListOf<Int>()
            for (i in 1 ..< prev.size) {
                cur.add(prev[i] - prev[i - 1])
            }
            seqs.add(cur)
        }

        var newItem = 0
        for (cur in seqs.reversed().drop(1)) {
            newItem = cur.first() - newItem
        }
        return newItem
    }

    override suspend fun partOne() = hists.map(::nextHistory).toList().sum()

    override suspend fun partTwo() = hists.map(::prevHistory).toList().sum()
}