package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlin.math.min

class Day5 : Exercise {
    /** Rather than fight unsigned ranges, use Long. The data looks (BARELY) under 2^32, but I don't care to confirm. */
    data class RangeMap(val source: Long, val dest: Long, val len: Long) {
        fun map(v: Long): Long? {
            if (v >= source && v < (source + len))
                return v - source + dest
            return null
        }

        // Last element, inclusive
        val lastSource get() = source + len - 1
    }

    private lateinit var seeds: List<Long>
    private lateinit var rangeMapChain: List<List<RangeMap>>

    override val testInput = """
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
    """.trimIndent()

    override val testResultPart1 = 35L
    override val testResultPart2 = 46L

    override suspend fun parse(lines: Flow<String>) {
        val lists = lines.toList().splitOnBlank()

        seeds = lists[0].single().substring(7).split(' ').map { it.toLong() }
        rangeMapChain = lists.drop(1).map { // Drop the seeds
            listOf(RangeMap(1,2,3))
            it.drop(1).map {  // Drop the (irrelevant) title. They're all in order.
                val nums = it.split(' ').map { it.toLong() }
                RangeMap(nums[1], nums[0], nums[2])
            }.sortedBy { it.source }
        }
    }

    override suspend fun partOne()= seeds.map(this::mapOneSeed).min()

    fun mapOneSeed(s: Long) = rangeMapChain.fold(s) { cur, m ->
        var newVal: Long? = null
        for (r in m) {
            newVal = r.map(cur)
            if (newVal != null) {
                break
            }
        }
        newVal ?: cur
    }

    override suspend fun partTwo() = mapSeedsByRanges()

    fun mapSeedsByRanges(): Long {
        // Pair of [start, end)
        var curRanges: MutableList<Pair<Long, Long>>
        var ranges: List<Pair<Long, Long>>

        // Build ranges from the seeds
        ranges = seeds.chunked(2) { Pair(it[0], it[0] + it[1]) }

        for (rangeMap in rangeMapChain) {
            curRanges = mutableListOf()

            for (r in ranges) {
                var pos = r.first
                var mapIndex = 0

                while (pos < r.second) {
                    // No more relevant mappings, pass through the rest.
                    if (mapIndex == rangeMap.size || rangeMap[mapIndex].source >= (r.second)) {
                        curRanges.add(Pair(pos, r.second))
                        pos = r.second  // Will end the loop,
                    }
                    // The entire mapping is below us. Try the next
                    else if (rangeMap[mapIndex].lastSource < pos) {
                        mapIndex++
                    }
                    // The mapping includes something after our start. Passthrough the beginning.
                    else if (rangeMap[mapIndex].source > pos) {
                        // My input data never hits this case!
                        curRanges.add(Pair(pos, rangeMap[mapIndex].source))
                        pos = rangeMap[mapIndex].source
                    }
                    // The mapping includes our starting position.
                    else {
                        val curMap = rangeMap[mapIndex]
                        val mapLen = min(r.second - pos, curMap.len - (pos - curMap.source))

                        val newStart = pos - curMap.source + curMap.dest
                        curRanges.add(Pair(newStart, newStart + mapLen))
                        pos += mapLen
                    }
                }
            }

            ranges = curRanges
        }

        return ranges.map { it.first }.min()
    }
}