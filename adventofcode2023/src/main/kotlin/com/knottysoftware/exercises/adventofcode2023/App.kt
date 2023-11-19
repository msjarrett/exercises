package com.knottysoftware.exercises.adventofcode2023

import java.io.File
import kotlin.time.measureTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking

suspend fun timedOperation(name: String, op: suspend () -> Unit) {
    println(">>> Start: $name <<<")
    val duration = measureTime { op() }
    println("<<< Done:  $name (elapsed time $duration) <<<")
    println("")
}

suspend fun runExercise(
    exercise: Exercise,
    input: Flow<String>,
    doPartOne: Boolean = true,
    doPartTwo: Boolean = true,
    expectedPart1: Any? = null,
    expectedPart2: Any? = null
) {
    timedOperation("Parsing") { exercise.parse(input) }

    if (doPartOne) {
        timedOperation("Part One") {
            try {
                val result = exercise.partOne()
                println("Result: $result")
                expectedPart1?.let {
                    if (result != it)
                        println("FAIL: expecting $it")
                    else
                        println("SUCCESS")
                }
            } catch (e: NotImplementedError) {
                println("[Not implemented yet]")
            }
        }
    }

    if (doPartTwo) {
        timedOperation("Part Two") {
            try {
                val result = exercise.partTwo()
                println("Result: $result")
                expectedPart2?.let {
                    if (result != it)
                        println("FAIL: expecting $it")
                    else
                        println("SUCCESS")
                }
            } catch (e: NotImplementedError) {
                println("[Not implemented yet]")
            }
        }
    }
}

// Borrowed from https://slack-chats.kotlinlang.org/t/454925/any-built-in-way-to-create-a-flow-of-lines-from-a-file-on-jv
fun File.lineFlow(): Flow<String> {
    val reader = bufferedReader()
    return reader.buffered()
        .lineSequence()
        .asFlow()
        .onCompletion { reader.close() }
        .flowOn(Dispatchers.IO)
}

suspend fun runOne(exercise: Exercise, filename: String) {
    val file = File(exercise.javaClass.classLoader.getResource(filename)!!.toURI())
    if (exercise.needReparse()) {
        runExercise(exercise, file.lineFlow(), doPartTwo = false)
        runExercise(exercise, file.lineFlow(), doPartOne = false)
    } else {
        runExercise(exercise, file.lineFlow())
    }
}

fun main(args: Array<String>) {
    val exercise = Day0()
    val file = /*args[0] + "25" + ".txt"*/"test.txt"

    runBlocking {
        if (args.size == 1 && args[0] == "test") {
            val testInput = exercise.testInput.split("\n").asFlow()
            runExercise(
                exercise,
                testInput,
                expectedPart1 = exercise.testResultPart1,
                expectedPart2 = exercise.testResultPart2
            )
        } else {
            runOne(exercise, file)
        }
    }
}
