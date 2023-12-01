package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow

interface Exercise {
    /** The test input string. Use a multiline string literal. */
    val testInput: String
    /** The test input string for part 2. If null (most exercises, will reuse testInput). */
    val testInput2: String get() = testInput
    /** Expected output for test data part 1. */
    val testResultPart1: Any? get() = null
    /** Expected output for test data part 2. */
    val testResultPart2: Any? get() = null
    /** If true, the exercise will parse before each of part 1 and part 2. */
    fun needReparse() = true
    /** Parse the input. */
    suspend fun parse(lines: Flow<String>)
    /** Run part 1. Return anything that can print as a string, but usually an Int. */
    suspend fun partOne(): Any = TODO()
    /** Run part 2. Return anything that can print as a string, but usually an Int. */
    suspend fun partTwo(): Any = TODO()
}