package com.knottysoftware.exercises.adventofcode2024

import java.util.stream.Stream

suspend fun Day3a(lines: Stream<String>): Any {
    val pattern = Regex("""mul\((\d+),(\d+)\)""")

    return lines.mapToLong {
        println (it)
        pattern.findAll(it as String).map { match ->
            //println(match.value)
            val g = match.groupValues
            g[1].toLong() * g[2].toLong()
        }.sum()
    }.sum()
}

suspend fun Day3b(lines: Stream<String>): Any {
    val pattern = Regex("""(mul\((\d+),(\d+)\))|(do\(\))|(don't\(\))""")

    val opsList = buildList() {
        lines.forEach {
            addAll(pattern.findAll(it))
        }
    }

    var enabled = true
    var sum = 0L
    for (m in opsList) {
        if (m.value == "don't()") enabled = false
        else if (m.value == "do()") enabled = true
        else if (enabled) {
            //println("m.value = (${m.groupValues[2]} * ${m.groupValues[3]})")
            sum += (m.groupValues[2].toLong() * m.groupValues[3].toLong())
        }
    }

    return sum

}