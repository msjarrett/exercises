package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

suspend fun Day11a(lines: Flow<String>): Any {
  val stones = lines.single().split(" ").map { it.toLong() }
  return blinkUpdate(stones, 25).size
}

suspend fun Day11b(lines: Flow<String>): Any {
  val stones = lines.single().split(" ").map { it.toLong() }
  return blinkCounter(stones, 75)
}

// Naively run the blink algorithm.
// The array increases geometrically. Iteration 25 is 180k big, but Iteration 30 is 1500k big. Never making it to 75.
fun blinkUpdate(initialStones: List<Long>, iterations: Int): List<Long> {
    // Presumably with so many updates, MutableList should be more efficient than recreating immutable lists.
    val stones = initialStones.toMutableList()
    repeat(iterations) { //iter ->
        var i = 0
        // Size can update as we go.
        while (i < stones.size) {
            val stoneInt = stones[i]
            val stoneString = stoneInt.toString()
            if (stoneInt == 0L) {
                stones[i++] = 1L
            } else if (stoneString.length % 2 == 0) {
                val strA = stoneString.substring(0 ..< (stoneString.length / 2))
                val strB = stoneString.substring((stoneString.length / 2) ..< stoneString.length)
                stones[i++] = strA.toLong()
                stones.add(i++, strB.toLong())
            } else {
                stones[i++] = stoneInt * 2024
            }
        }

        //println("Iteration ${iter + 1} done. Size ${stones.size}.")
        //println(stones)
    }
    return stones
}

// We're going to try and prune subproblems here.
fun blinkCounter(initialStones: List<Long>, iterations: Int): Long {
    val memo = mutableMapOf<Pair<Long, Int>, Long>()
    return initialStones.map { blinkCounterDigit(it, iterations, memo) }.sum()
}

private fun blinkCounterDigit(stone: Long, iterations: Int, memo: MutableMap<Pair<Long, Int>, Long>): Long {
    if (iterations == 0) return 1

    val index = Pair(stone, iterations)
    if (memo.containsKey(index)) return memo[index]!!

    val stoneString = stone.toString()
    var result: Long

    if (stone == 0L) {
        result = blinkCounterDigit(1, iterations - 1, memo)
    } else if (stoneString.length % 2 == 0) {
        val strA = stoneString.substring(0 ..< (stoneString.length / 2))
        val strB = stoneString.substring((stoneString.length / 2) ..< stoneString.length)
        result = blinkCounterDigit(strA.toLong(), iterations - 1, memo) + blinkCounterDigit(strB.toLong(), iterations - 1, memo)
    } else {
        result = blinkCounterDigit(stone * 2024, iterations - 1, memo)
    }

    memo[index] = result
    return result
}