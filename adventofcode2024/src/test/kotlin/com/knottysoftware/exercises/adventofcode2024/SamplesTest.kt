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

        assertEquals(11, RunPuzzleWithText(2024, 1, "Sample A", ::Day1a, sample))
        assertEquals(31, RunPuzzleWithText(2024, 1, "Sample B", ::Day1b, sample))
    }

    @Test
    fun testDay3() {
        assertEquals(161L, RunPuzzleWithText(2024, 3, "Sample A", ::Day3a, """xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"""))
        assertEquals(48L, RunPuzzleWithText(2024, 3, "Sample B", ::Day3b, """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""))
    }
}