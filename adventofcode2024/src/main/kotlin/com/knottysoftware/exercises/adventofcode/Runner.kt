package com.knottysoftware.exercises.adventofcode

import java.nio.file.Files
import java.util.Arrays
import java.util.stream.Stream
import kotlinx.coroutines.runBlocking

typealias Puzzle = suspend (Stream<String>) -> Any

fun RunPuzzle(lines: Stream<String>, name: String, puzzle: Puzzle): Any {
    println("=== START $name")
    val result = runBlocking {
        puzzle(lines)
    }
    println("RESULT $result")
    println("===   END $name")
    return result
}

fun RunPuzzleWithInput(year: Int, day: Int, part: String, puzzle: Puzzle) =
    RunPuzzle(
        Files.lines(GetPuzzleInput(year, day)),
        "Puzzle $year-$day ($part)",
        puzzle)

fun RunPuzzleWithText(year: Int, day: Int, part: String, puzzle: Puzzle, input: String) =
    RunPuzzle(
        input.trimIndent().split("\n").stream(),
        "Puzzle $year-$day ($part)",
        puzzle)
