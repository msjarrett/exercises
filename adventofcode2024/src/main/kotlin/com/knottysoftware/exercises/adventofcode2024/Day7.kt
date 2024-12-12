package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

// In the input, the test values get big (like 12+ digits), so use Long.
// Operands are only 3 digits, but meh, why fight types.
data class TestEquation(val testValue: Long, val operands: List<Long>)

private fun testEquation(line: String): TestEquation {
  val separator = line.indexOf(':')
  check(separator != -1)
  return TestEquation(
      line.substring(0..<separator).toLong(),
      line.substring(separator + 2).split(" ").map { it.toLong() })
}

fun numConcat(a: Long, b: Long): Long = (a.toString() + b.toString()).toLong()

fun TestEquation.possiblyTrue(useConcat: Boolean): Boolean {
  // Total so far, operations so far.
  // typealias SearchItem = Pair<Long, List<Char>>

  val queue = SearchQueue(Pair(operands[0], listOf<Char>()))
  for ((total, ops) in queue) {
    val nextIndex = ops.size + 1
    if (nextIndex == operands.size) {
      if (total == testValue) {
        // println(this.toString() + " = " + ops)
        return true
      }
    } else if (total <= testValue) { // Careful: a few rows end with * 1
      queue.addDFS(Pair(total * operands[nextIndex], ops.plus('*')))
      queue.addDFS(Pair(total + operands[nextIndex], ops.plus('+')))
      if (useConcat) queue.addDFS(Pair(numConcat(total, operands[nextIndex]), ops.plus('|')))
    }
  }
  // println(this.toString() + " no solution")
  return false
}

suspend fun Day7a(lines: Flow<String>): Any {
  return lines
      .map { testEquation(it) }
      .filter() { it.possiblyTrue(useConcat = false) }
      .map { it.testValue }
      .toList()
      .sum()
}

suspend fun Day7b(lines: Flow<String>): Any {
  return lines
      .map { testEquation(it) }
      .filter() { it.possiblyTrue(useConcat = true) }
      .map { it.testValue }
      .toList()
      .sum()
}
