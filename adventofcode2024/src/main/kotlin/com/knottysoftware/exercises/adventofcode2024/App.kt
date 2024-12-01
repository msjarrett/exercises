package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.RunPuzzleWithInput
import com.knottysoftware.exercises.adventofcode.RunPuzzleWithText

fun main(args: Array<String>) {
    val sample =         """
3   4
4   3
2   5
1   3
3   9
3   3
        """

    RunPuzzleWithText(2024, 1, "Sample A", ::Day1a, sample)
    RunPuzzleWithInput(2024, 1, "Part A", ::Day1a)

    RunPuzzleWithText(2024, 1, "Sample B", ::Day1b, sample)
    RunPuzzleWithInput(2024, 1, "Part B", ::Day1b)
}
