package com.knottysoftware.exercises.adventofcode2022

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

fun runExercise(exercise: Exercise, input: Sequence<String>, doPartOne: Boolean = true, doPartTwo: Boolean = true): Pair<String, String> {
    exercise.parse(input)
    return Pair(
        if (doPartOne) exercise.partOne().toString() else "",
        if (doPartTwo) exercise.partTwo().toString() else ""
    )
}

fun runOne(filename: String, exercise: Exercise): Pair<String, String> {
    val file = File(exercise.javaClass.classLoader.getResource(filename).toURI())
    var partOne = ""
    var partTwo = ""

    if (exercise.needReparse()) {
        file.useLines() {lines -> val (p, _) = runExercise(exercise, lines, doPartTwo = false) ; partOne = p }
        file.useLines() {lines -> val (_, p) = runExercise(exercise, lines, doPartOne = false) ; partTwo = p}
    } else {
        file.useLines {lines -> val (p1, p2) = runExercise(exercise, lines) ; partOne = p1 ; partTwo = p2 }
    }
    return Pair(partOne, partTwo)
}

class AppTest {
    // Verify correct results from personal input.
    @Test fun day1() = assertEquals(runOne("input1.txt", Day1()), Pair("69795", "208437"))
    @Test fun day2() = assertEquals(runOne("input2.txt", Day2()), Pair("12740", "11980"))
    @Test fun day3() = assertEquals(runOne("input3.txt", Day3()), Pair("7863", "2488"))
    @Test fun day4() = assertEquals(runOne("input4.txt", Day4()), Pair("413", "806"))
    @Test fun day5() = assertEquals(runOne("input5.txt", Day5()), Pair("FRDSQRRCD", "HRFTQVWNN"))
}
