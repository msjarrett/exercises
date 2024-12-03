package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.RunPuzzleWithInput
import org.junit.Test
import org.junit.Assert.assertEquals

class InputsTest {
    @Test
    fun testDay1() {
        assertEquals(2367773, RunPuzzleWithInput(2024, 1, "Part A", ::Day1a))
        assertEquals(21271939, RunPuzzleWithInput(2024, 1, "Part B", ::Day1b))
    }

    @Test
    fun testDay3() {
        assertEquals(191183308L, RunPuzzleWithInput(2024, 3, "Part A", ::Day3a))
        assertEquals(92082041L, RunPuzzleWithInput(2024, 3, "Part B", ::Day3b))
    }
}