package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.RunPuzzleWithInput
import com.knottysoftware.exercises.adventofcode.RunPuzzleWithText

fun main(args: Array<String>) {
    //val sample = """xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"""
    val sample = """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""
    //RunPuzzleWithText(2024, 3, "Sample A", ::Day3a, sample)
    //RunPuzzleWithInput(2024, 3, "Part A", ::Day3a)

    //RunPuzzleWithText(2024, 3, "Sample B", ::Day3b, sample)
    RunPuzzleWithInput(2024, 3, "Part B", ::Day3b)
}
