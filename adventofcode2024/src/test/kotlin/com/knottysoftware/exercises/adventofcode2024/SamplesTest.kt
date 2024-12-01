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
}