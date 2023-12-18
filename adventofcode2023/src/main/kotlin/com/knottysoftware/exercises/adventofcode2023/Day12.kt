package com.knottysoftware.exercises.adventofcode2023

import kotlin.time.measureTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class Day12 : Exercise {
    private lateinit var rows: Flow<Pair<String, List<Int>>>

    override val testInput = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
    """.trimIndent()

    override val testResultPart1 = 21L
    override val testResultPart2 = 525152L

    override suspend fun parse(lines: Flow<String>) {
        rows = lines.map {
            val pieces = it.split(' ')
            check(pieces.size == 2)
            Pair(pieces[0], pieces[1].split(',').map { it.toInt() })
        }
    }

    /**
     * The original approach to the problem. Good enough for part 1 but too slow for part 2.
     * Maybe there's some creative pruning that could help, but that type of caching results much better suits DP.
     */
    fun arrangements(row: Pair<String, List<Int>>): Int {
        val validArrangements = mutableSetOf<String>()

        // Items = springs, remaining groups, first possible character.
        println("${row.first}  ${row.second}")
        val elapsedTime = measureTime {
            val queue = mutableListOf(Triple(row.first, row.second, 0))
            while (!queue.isEmpty()) {
                // DFS is way faster for some reason.
                val (springs, groups, nextIndex) = queue.removeLast()
                if (groups.size == 0) {
                    // If there's any remaining damaged springs, we burned too many groups.
                    if (springs.indexOf('#', nextIndex) != -1) continue

                    // Add as valid after removing any trailing ?'s after last group (leading ?'s would be stripped).
                    validArrangements.add(springs.replace('?', '.'))
                    continue
                }

                // Find the next spot that fits group
                val nextGroupSize = groups[0]
                var mustFit = false
                Indexes@ for (i in nextIndex..(springs.length - nextGroupSize)) {
                    // We can't skip a #, so if this is a #, we have to try and fit it.
                    if (mustFit) break
                    if (springs[i] == '#') mustFit = true

                    if (i > 0 && springs[i - 1] == '#') continue   // Can't merge with previous group.
                    for (j in i..<(i + nextGroupSize)) {
                        if (springs[j] == '.') continue@Indexes  // Can't have working spring in the group.
                    }
                    // Can't follow group with a damaged spring.
                    if ((i + nextGroupSize) < springs.length && springs[i + nextGroupSize] == '#') continue

                    // VALID! Build a new queue.
                    val newSprings = springs.toCharArray()
                    // Remove prior ?'s
                    for (j in 0..<i) {
                        if (newSprings[j] == '?') newSprings[j] = '.'
                    }
                    // Fill current block.
                    for (j in i..<(i + nextGroupSize)) {
                        newSprings[j] = '#'
                    }
                    // Clear trailer
                    if ((i + nextGroupSize) < newSprings.size) newSprings[i + nextGroupSize] = '.'

                    queue.add(Triple(newSprings.joinToString(""), groups.drop(1), i + nextGroupSize))
                    if ((validArrangements.size % 1000) == 0) print("FOUND ${validArrangements.size}\r")
                }
            }
        }

        //println("  -> ${validArrangements.size} ($elapsedTime)")
        return validArrangements.size
    }

    /**
     * Dynamic programming solution with memoization. It's SOOOOO much faster even without the cache, but with the
     * cache it powers through the problem in milliseconds.
     */
    fun arrangementsDp(row: Pair<String, List<Int>>, cache: MutableMap<Pair<Int, Int>, Long>): Long {
        val cacheKey = Pair(row.first.length, row.second.size)
        cache[cacheKey]?.let {
            return it
        }

        var arrCount = 0L
        val springs = row.first

        if (row.second.isEmpty()) {
            if (springs.contains('#')) return 0  // We have a # we can't resolve to a group.
            else return 1  // All remaining ? maps to .
        }

        val group = row.second.first()

        var mustMatch = false
        Indexes@ for (i in 0 .. (springs.length - group)) {
            if (mustMatch) break   // We tried to match a #
            if (springs[i] == '#') mustMatch = true

            for (j in i ..< (i + group)) {   // Can we satisfy the potential block?
                if (springs[j] == '.') continue@Indexes
            }

            if ((i + group) < springs.length && springs[i + group] == '#') continue  // Too big a grouping

            // Find the next possible variance point. Skip one character after group because it must be .
            var nextPos = i + group + 1
            while (nextPos < springs.length && springs[nextPos] == '.') nextPos++
            if (nextPos > springs.length) nextPos = springs.length

            // Solve the subproblem
            arrCount += arrangementsDp(Pair(springs.substring(nextPos), row.second.drop(1)), cache)
        }

        //println("  $row -> $arrCount")
        cache[cacheKey] = arrCount
        return arrCount
    }


    override suspend fun partOne() = rows.map {
        val cnt = arrangementsDp(it, mutableMapOf())
        //println("$it -> $cnt")
        cnt
    }.toList().sum()

   override suspend fun partTwo() = rows.map {
        Pair(it.first + '?' + it.first + '?' + it.first + '?' + it.first + '?' + it.first,
             buildList {
                 addAll(it.second)
                 addAll(it.second)
                 addAll(it.second)
                 addAll(it.second)
                 addAll(it.second)
             })
        }.map {
           val cnt = arrangementsDp(it, mutableMapOf())
           //println("$it -> $cnt")
           cnt
        }.toList().sum()
}