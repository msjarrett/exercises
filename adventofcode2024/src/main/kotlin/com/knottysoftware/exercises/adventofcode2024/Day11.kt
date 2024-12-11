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
  return blinkUpdate(stones, 10).size
}

// Naively run the blink algorithm.
// The array increases geometrically. Iteration 25 is 180k big, but Iteration 30 is 1500k big. Never making it to 75.
fun blinkUpdate(initialStones: List<Long>, iterations: Int): List<Long> {
    // Presumably with so many updates, MutableList should be more efficient than recreating immutable lists.
    val stones = initialStones.toMutableList()
    repeat(iterations) { iter ->
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

        println("Iteration ${iter + 1} done. Size ${stones.size}.")
        println(stones)
    }
    return stones
}

// We're going to try and prune subproblems here.
fun blinkCounter(initialStones: List<Long>, iterations: Int): List<Long> {
    // Solve for zero.

    val stones = initialStones.toMutableList()
    repeat(iterations) { iter ->
        var countZero = 0
        var i = 0
        // Size can update as we go.
        while (i < stones.size) {
            val stoneInt = stones[i]
            val stoneString = stoneInt.toString()
            if (stoneInt == 0L) {
                stones[i++] = 1L
                countZero++
            } else if (stoneString.length % 2 == 0) {
                val strA = stoneString.substring(0 ..< (stoneString.length / 2))
                val strB = stoneString.substring((stoneString.length / 2) ..< stoneString.length)
                stones[i++] = strA.toLong()
                stones.add(i++, strB.toLong())
            } else {
                stones[i++] = stoneInt * 2024
            }
        }

        println("Iteration ${iter + 1} done. Size ${stones.size}. Zeroes $countZero.")
    }
    return stones
}