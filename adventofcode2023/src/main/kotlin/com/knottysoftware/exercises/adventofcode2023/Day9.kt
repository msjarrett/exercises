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

    fun genSeqs(hist: List<Int>): List<List<Int>> {
        val seqs = mutableListOf(hist)
        while(!seqs.last().all { it == 0} ) {
            seqs.add(seqs.last().windowed(2, 1).map{ it[1] - it[0] })
        }
        return seqs
    }

    fun nextHistory(hist: List<Int>) = genSeqs(hist).reversed().drop(1).fold(0) { newVal, cur ->
            cur.last() + newVal
        }

    fun prevHistory(hist: List<Int>)= genSeqs(hist).reversed().drop(1).fold(0) { newVal, cur ->
        cur.first() - newVal
    }

    override suspend fun partOne() = hists.map(::nextHistory).toList().sum()

    override suspend fun partTwo() = hists.map(::prevHistory).toList().sum()
}