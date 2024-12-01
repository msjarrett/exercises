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
}