package com.knottysoftware.exercises.adventofcode2024

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

suspend fun Day3a(lines: Flow<String>): Any {
  val pattern = Regex("""mul\((\d+),(\d+)\)""")

  return lines
      .map {
        // println(it)
        pattern
            .findAll(it)
            .map { match ->
              val g = match.groupValues
              g[1].toLong() * g[2].toLong()
            }
            .sum()
      }
      .toList()
      .sum()
}

suspend fun Day3b(lines: Flow<String>): Any {
  val pattern = Regex("""(mul\((\d+),(\d+)\))|(do\(\))|(don't\(\))""")

  val opsList = lines.map { pattern.findAll(it) }.toList().flatMap { it.toList() }

  var enabled = true
  var sum = 0L
  for (m in opsList) {
    if (m.value == "don't()") enabled = false
    else if (m.value == "do()") enabled = true
    else if (enabled) {
      // println("m.value = (${m.groupValues[2]} * ${m.groupValues[3]})")
      sum += (m.groupValues[2].toLong() * m.groupValues[3].toLong())
    }
  }

  return sum
}
