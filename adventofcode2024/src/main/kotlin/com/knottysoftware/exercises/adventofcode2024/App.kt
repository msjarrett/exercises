package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.RunPuzzleWithInput
import com.knottysoftware.exercises.adventofcode.RunPuzzleWithText

fun main(args: Array<String>) {
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
    //RunPuzzleWithText(::Day4b, sample)
    RunPuzzleWithInput(::Day4b, 2024, 4)

}
