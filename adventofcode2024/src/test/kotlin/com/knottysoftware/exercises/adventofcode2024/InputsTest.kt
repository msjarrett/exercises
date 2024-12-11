package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.runner.RunPuzzleWithInput
import org.junit.Assert.assertEquals
import org.junit.Test

class InputsTest {
  @Test
  fun testDay01() {
    assertEquals(2367773, RunPuzzleWithInput(::Day1a, 2024, 1))
    assertEquals(21271939, RunPuzzleWithInput(::Day1b, 2024, 1))
  }

  @Test
  fun testDay02() {
    assertEquals(220, RunPuzzleWithInput(::Day2a, 2024, 2))
    assertEquals(296, RunPuzzleWithInput(::Day2b, 2024, 2))
  }

  @Test
  fun testDay03() {
    assertEquals(191183308L, RunPuzzleWithInput(::Day3a, 2024, 3))
    assertEquals(92082041L, RunPuzzleWithInput(::Day3b, 2024, 3))
  }

  @Test
  fun testDay04() {
    assertEquals(2639, RunPuzzleWithInput(::Day4a, 2024, 4))
    assertEquals(2005, RunPuzzleWithInput(::Day4b, 2024, 4))
  }

  @Test
  fun testDay05() {
    assertEquals(4957, RunPuzzleWithInput(::Day5a, 2024, 5))
    assertEquals(6938, RunPuzzleWithInput(::Day5b, 2024, 5))
  }

  @Test
  fun testDay06() {
    assertEquals(5162, RunPuzzleWithInput(::Day6a, 2024, 6))
    assertEquals(1909, RunPuzzleWithInput(::Day6b, 2024, 6))
  }

  @Test
  fun testDay07() {
    assertEquals(303876485655L, RunPuzzleWithInput(::Day7a, 2024, 7))
    assertEquals(146111650210682L, RunPuzzleWithInput(::Day7b, 2024, 7))
  }

  @Test
  fun testDay08() {
    assertEquals(381, RunPuzzleWithInput(::Day8a, 2024, 8))
    assertEquals(1184, RunPuzzleWithInput(::Day8b, 2024, 8))
  }

  @Test
  fun testDay09() {
    assertEquals(6386640365805L, RunPuzzleWithInput(::Day9a, 2024, 9))
    assertEquals(6423258376982L, RunPuzzleWithInput(::Day9b, 2024, 9))
  }

  @Test
  fun testDay10() {
    assertEquals(778, RunPuzzleWithInput(::Day10a, 2024, 10))
    assertEquals(1925, RunPuzzleWithInput(::Day10b, 2024, 10))
  }

  @Test
  fun testDay11() {
    assertEquals(186203, RunPuzzleWithInput(::Day11a, 2024, 11))
    assertEquals(221291560078593L, RunPuzzleWithInput(::Day11b, 2024, 11))
  }
}
