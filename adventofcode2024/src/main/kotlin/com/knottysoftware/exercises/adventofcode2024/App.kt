package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.runner.RunPuzzleWithInput
import com.knottysoftware.exercises.adventofcode.runner.RunPuzzleWithText

fun main(args: Array<String>) {
  val sample =
      """
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
  """
  val year = 2024
  val day = 12
  val method = ::Day12b

  when (args[0]) {
    "sample" -> RunPuzzleWithText(method, sample)
    "input" -> RunPuzzleWithInput(method, year, day)
    else -> throw IllegalArgumentException("Must be 'sample' or 'input'.")
  }
}
