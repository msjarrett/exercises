package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.runner.RunPuzzleWithInput
import com.knottysoftware.exercises.adventofcode.runner.RunPuzzleWithText

fun main(args: Array<String>) {
  val sample = "2333133121414131402"
  val year = 2024
  val day = 9
  val method = ::Day9b

  when (args[0]) {
    "sample" -> RunPuzzleWithText(method, sample)
    "input" -> RunPuzzleWithInput(method, year, day)
    else -> throw IllegalArgumentException("Must be 'sample' or 'input'.")
  }
}
