package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.runner.RunPuzzleWithText
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

    @Test
    fun testDay5() {
        val sample = """
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
        """

        assertEquals(143, RunPuzzleWithText(::Day5a, sample))
        assertEquals(123, RunPuzzleWithText(::Day5b, sample))
    }

    @Test
    fun testDay6() {
        val sample = """
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
        """

        assertEquals(41, RunPuzzleWithText(::Day6a, sample))
        assertEquals(6, RunPuzzleWithText(::Day6b, sample))
    }

    @Test
    fun testDay7() {
        val sample = """
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
        """

        assertEquals(3749L, RunPuzzleWithText(::Day7a, sample))
        assertEquals(11387L, RunPuzzleWithText(::Day7b, sample))
    }

    @Test
    fun testDay8() {
        val sample = """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
        """

        assertEquals(14, RunPuzzleWithText(::Day8a, sample))
        assertEquals(34, RunPuzzleWithText(::Day8b, sample))
    }


}