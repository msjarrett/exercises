package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.runner.RunPuzzleWithInput
import org.junit.Test
import org.junit.Assert.assertEquals

class InputsTest {
    @Test
    fun testDay1() {
        assertEquals(2367773, RunPuzzleWithInput(::Day1a, 2024, 1))
        assertEquals(21271939, RunPuzzleWithInput(::Day1b, 2024, 1))
    }

    @Test
    fun testDay2() {
        assertEquals(220, RunPuzzleWithInput(::Day2a, 2024, 2))
        assertEquals(296, RunPuzzleWithInput(::Day2b, 2024, 2))
    }

    @Test
    fun testDay3() {
        assertEquals(191183308L, RunPuzzleWithInput(::Day3a, 2024, 3))
        assertEquals(92082041L, RunPuzzleWithInput(::Day3b, 2024, 3))
    }

    @Test
    fun testDay4() {
        assertEquals(2639, RunPuzzleWithInput(::Day4a, 2024, 4))
        assertEquals(2005, RunPuzzleWithInput(::Day4b, 2024, 4))
    }

    @Test
    fun testDay5() {
        assertEquals(4957, RunPuzzleWithInput(::Day5a, 2024, 5))
        assertEquals(6938, RunPuzzleWithInput(::Day5b, 2024, 5))
    }

    @Test
    fun testDay6() {
        assertEquals(5162, RunPuzzleWithInput(::Day6a, 2024, 6))
        assertEquals(1909, RunPuzzleWithInput(::Day6b, 2024, 6))
    }
}