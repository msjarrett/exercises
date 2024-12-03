package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.RunPuzzleWithInput
import com.knottysoftware.exercises.adventofcode.RunPuzzleWithText

fun main(args: Array<String>) {
    val sample = """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
    """
    RunPuzzleWithText(::Day2b, sample)
    //RunPuzzleWithInput(2024, 2, "Part B", ::Day2b)

    //RunPuzzleWithText(2024, 3, "Sample B", ::Day3b, sample)
    //RunPuzzleWithInput(2024, 3, "Part B", ::Day3b)
}
