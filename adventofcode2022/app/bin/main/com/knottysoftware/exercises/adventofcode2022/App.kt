package com.knottysoftware.exercises.adventofcode2022

import java.io.File
import kotlin.sequences.Sequence
import kotlin.time.measureTime

@OptIn(kotlin.time.ExperimentalTime::class)
fun timedOperation(name: String, op: ()->Unit) {
    println(">>> Start: $name <<<")
    val duration = measureTime { op() }
    println("<<< Done:  $name (elapsed time $duration) <<<")
    println("")
}

fun runExercise(exercise: Exercise, input: Sequence<String>) {
    timedOperation("Parsing") { exercise.parse(input) }
    timedOperation("Part One") {
        try {
            val result = exercise.partOne()
            println("Result: $result")
        } catch (e: NotImplementedError) {
            println("[Not implemented yet]")
        }
    }

    timedOperation("Part Two") {
        try {
            val result = exercise.partTwo()
            println("Result: $result")
        } catch (e: NotImplementedError) {
            println("[Not implemented yet]")
        }
    }
}

fun main() {
    val file = File("/mnt/d/Development/Github/exercises/adventofcode2022/input.txt")
    val exercise = TestExercise()
    file.useLines {lines -> runExercise(exercise, lines) }
}
