package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.RunPuzzleWithText
import org.junit.Test
import org.junit.Assert.assertEquals

class SamplesTest {
    @Test
    fun testDay0() {
        assertEquals(10, RunPuzzleWithText(2024, 1, "sample", ::Day0,
        """
            meep
            beep
            ok
        """))
    }

    @Test
    fun testDay1() {
        val sample = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """

        RunPuzzleWithText(2024, 1, "Sample A", ::Day1a, sample)
        RunPuzzleWithText(2024, 1, "Sample B", ::Day1b, sample)
    }

}