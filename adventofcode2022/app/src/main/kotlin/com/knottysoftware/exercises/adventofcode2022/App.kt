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

fun runExercise(exercise: Exercise, input: Sequence<String>, doPartOne: Boolean = true, doPartTwo: Boolean = true) {
    timedOperation("Parsing") { exercise.parse(input) }
    
    if (doPartOne) {
        timedOperation("Part One") {
            try {
                val result = exercise.partOne()
                println("Result: $result")
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
            } catch (e: NotImplementedError) {
                println("[Not implemented yet]")
            }
        }
    }
}

fun runOne(filename: String, exercise: Exercise) {
    val file = File(exercise.javaClass.classLoader.getResource(filename).toURI())
    if (exercise.needReparse()) {
        file.useLines() {lines -> runExercise(exercise, lines, doPartTwo = false) }
        file.useLines() {lines -> runExercise(exercise, lines, doPartOne = false) }
    } else {
        file.useLines {lines -> runExercise(exercise, lines) }
    }
}

fun main(args: Array<String>) {
    runOne(args[0] + "12" + ".txt", Day12())
}
