package com.knottysoftware.exercises.adventofcode2024

import java.util.stream.Stream
import kotlin.math.abs

fun IsSafe(levels: List<Int>): Boolean {
    val firstDiff = levels[1] - levels[0]
    for (i in 1 ..< levels.size) {
        val diff = levels[i] - levels[i - 1]
        if (((diff < 0) != (firstDiff < 0)) ||
            abs(diff) < 1 || abs(diff) > 3) {
            return false
        }
    }
    return true
}

fun MetaIsSafe(levels: List<Int>): Boolean {
    // We probably don't have to check EVERY value, but the levels lists are very short, so
    // there is not really a point to not doing it.
    for (i in 0 ..< levels.size) {
        if (IsSafe(levels.filterIndexed { j, v ->
            j != i
        })) return true
    }
    return false
}

suspend fun Day2a(lines: Stream<String>): Any {
    return lines.mapToInt {
        val levels = it.split(" ").map { it.toInt() }
        //println(levels)
        if (IsSafe(levels)) 1 else 0
    }.sum()
}

suspend fun Day2b(lines: Stream<String>): Any {
    return lines.mapToInt {
        val levels = it.split(" ").map { it.toInt() }
        if (IsSafe(levels) || MetaIsSafe(levels)) 1 else 0
    }.sum()
}