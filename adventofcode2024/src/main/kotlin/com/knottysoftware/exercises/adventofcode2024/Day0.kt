package com.knottysoftware.exercises.adventofcode2024

import java.util.stream.Stream

suspend fun Day0(lines: Stream<String>): Any {
    var i = 0
    lines.forEach {
        println(it + " " + it.length)
        i += it.length
    }
    return i
}