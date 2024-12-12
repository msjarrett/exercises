package com.knottysoftware.exercises.adventofcode2024

import kotlin.math.abs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

fun IsSafe(levels: List<Int>): Boolean {
  val firstDiff = levels[1] - levels[0]
  for (i in 1..<levels.size) {
    val diff = levels[i] - levels[i - 1]
    if (((diff < 0) != (firstDiff < 0)) || abs(diff) < 1 || abs(diff) > 3) {
      return false
    }
  }
  return true
}

fun MetaIsSafe(levels: List<Int>): Boolean {
  // We probably don't have to check EVERY value, but the levels lists are very short, so
  // there is not really a point to not doing it.
  for (i in 0..<levels.size) {
    if (IsSafe(levels.filterIndexed { j, _ -> j != i })) return true
  }
  return false
}

suspend fun Day2a(lines: Flow<String>): Any {
  return lines
      .map {
        val levels = it.split(" ").map { it.toInt() }
        // println(levels)
        if (IsSafe(levels)) 1 else 0
      }
      .toList()
      .sum()
}

suspend fun Day2b(lines: Flow<String>): Any {
  return lines
      .map {
        val levels = it.split(" ").map { it.toInt() }
        if (IsSafe(levels) || MetaIsSafe(levels)) 1 else 0
      }
      .toList()
      .sum()
}
