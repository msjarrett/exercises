package com.knottysoftware.exercises.adventofcode2022;

import kotlin.ranges.IntRange

fun rangeFromStr(str: String): IntRange {
    val ends = str.split('-')
    require(ends.size == 2)
    return ends[0].toInt() .. ends[1].toInt()
}

class Day4 : Exercise {
    lateinit var elfPairs: List<Pair<IntRange, IntRange>>

    override fun parse(lines: Sequence<String>) {
        elfPairs = lines.map {
            val elves = it.split(',')
            require(elves.size == 2)
            Pair(rangeFromStr(elves[0]), rangeFromStr(elves[1]))
        }.toList()
    }

    override fun partOne() = elfPairs.filter {
        (it.first.contains(it.second.start) && it.first.contains(it.second.endInclusive)) ||
        (it.second.contains(it.first.start) && it.second.contains(it.first.endInclusive))
    }.count().toLong()

    override fun partTwo() = elfPairs.filter {
        (it.first.contains(it.second.start) || it.first.contains(it.second.endInclusive)) ||
        (it.second.contains(it.first.start) || it.second.contains(it.first.endInclusive))
    }.count().toLong()
}