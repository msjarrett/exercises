package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.RunPuzzleWithText
import org.junit.Test
import org.junit.Assert.assertEquals

class SamplesTest {
    @Test
    fun testDay0() {
        assertEquals(10, RunPuzzleWithText(::Day0,
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

        assertEquals(11, RunPuzzleWithText(::Day1a, sample))
        assertEquals(31, RunPuzzleWithText(::Day1b, sample))
    }

    @Test
    fun testDay2() {
        val sample = """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
        """

        assertEquals(2, RunPuzzleWithText(::Day2a, sample))
        assertEquals(4, RunPuzzleWithText(::Day2b, sample))
    }

    @Test
    fun testDay3() {
        assertEquals(161L, RunPuzzleWithText(::Day3a, """xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"""))
        assertEquals(48L, RunPuzzleWithText(::Day3b, """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""))
    }

    @Test
    fun testDay4() {
        val sample = """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
        """

        assertEquals(18, RunPuzzleWithText(::Day4a, sample))
        assertEquals(9, RunPuzzleWithText(::Day4b, sample))
    }
}