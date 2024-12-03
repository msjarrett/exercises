package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.RunPuzzleWithInput
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
}