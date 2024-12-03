package com.knottysoftware.exercises.adventofcode

import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking

typealias Puzzle = suspend (Flow<String>) -> Any


// Borrowed from https://slack-chats.kotlinlang.org/t/454925/any-built-in-way-to-create-a-flow-of-lines-from-a-file-on-jv
private fun File.lineFlow(): Flow<String> {
    val reader = bufferedReader()
    return reader.buffered()
        .lineSequence()
        .asFlow()
        .onCompletion { reader.close() }
        .flowOn(Dispatchers.IO)
}

private fun RunPuzzle(lines: Flow<String>, puzzle: Puzzle): Any {
    println("=== START")
    val result = runBlocking {
        puzzle(lines)
    }
    println("RESULT $result")
    println("=== END")
    return result
}

fun RunPuzzleWithInput(puzzle: Puzzle, year: Int, day: Int) =
    RunPuzzle(
        File(GetPuzzleInput(year, day).toUri()).lineFlow(),
        puzzle)

fun RunPuzzleWithText(puzzle: Puzzle, input: String) =
    RunPuzzle(
        input.trimIndent().split("\n").asFlow(),
        puzzle)
