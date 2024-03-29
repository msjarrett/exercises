package com.knottysoftware.exercises.adventofcode2022

import java.io.File
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

fun runExercise(
    exercise: Exercise,
    input: Sequence<String>,
    doPartOne: Boolean = true,
    doPartTwo: Boolean = true
): Pair<String, String> {
  exercise.parse(input)
  return Pair(
      if (doPartOne) exercise.partOne().toString() else "",
      if (doPartTwo) exercise.partTwo().toString() else "")
}

fun runPartOneOnly(filename: String, exercise: Exercise): String {
  val file = File(exercise.javaClass.classLoader.getResource(filename).toURI())
  var partOne = ""

  file.useLines { lines ->
    val (p1, _) = runExercise(exercise, lines, doPartTwo = false)
    partOne = p1
  }
  return partOne
}

fun runOne(filename: String, exercise: Exercise): Pair<String, String> {
  val file = File(exercise.javaClass.classLoader.getResource(filename).toURI())
  var partOne = ""
  var partTwo = ""

  if (exercise.needReparse()) {
    file.useLines() { lines ->
      val (p, _) = runExercise(exercise, lines, doPartTwo = false)
      partOne = p
    }
    file.useLines() { lines ->
      val (_, p) = runExercise(exercise, lines, doPartOne = false)
      partTwo = p
    }
  } else {
    file.useLines { lines ->
      val (p1, p2) = runExercise(exercise, lines)
      partOne = p1
      partTwo = p2
    }
  }
  return Pair(partOne, partTwo)
}

class AppTest {
  // Verify correct results from personal input.
  @Test fun day1() = assertEquals(runOne("input1.txt", Day1()), Pair("69795", "208437"))
  @Test fun day2() = assertEquals(runOne("input2.txt", Day2()), Pair("12740", "11980"))
  @Test fun day3() = assertEquals(runOne("input3.txt", Day3()), Pair("7863", "2488"))
  @Test fun day4() = assertEquals(runOne("input4.txt", Day4()), Pair("413", "806"))
  @Test fun day5() = assertEquals(runOne("input5.txt", Day5()), Pair("FRDSQRRCD", "HRFTQVWNN"))
  @Test fun day6() = assertEquals(runOne("input6.txt", Day6()), Pair("1816", "2625"))
  @Test fun day7() = assertEquals(runOne("input7.txt", Day7()), Pair("1611443", "2086088"))
  @Test fun day8() = assertEquals(runOne("input8.txt", Day8()), Pair("1688", "410400"))
  @Test fun day9() = assertEquals(runOne("input9.txt", Day9()), Pair("6256", "2665"))
  @Test fun day11() = assertEquals(runOne("input11.txt", Day11()), Pair("64032", "12729522272"))
  @Test fun day12() = assertEquals(runOne("input12.txt", Day12()), Pair("383", "377"))
  @Test fun day13() = assertEquals(runOne("input13.txt", Day13()), Pair("5806", "23600"))
  @Test fun day14() = assertEquals(runOne("input14.txt", Day14()), Pair("964", "32041"))

  // TODO: Find out why this OOMs.
  @Test
  fun day15() =
      assertEquals(runPartOneOnly("input15.txt", Day15()), "5878678") // partTwo = 11796491041245

  // Part 2 takes about 2 minutes to run.
  @Test fun day16() = assertEquals(runPartOneOnly("input16.txt", Day16()), "1488") // partTwo = 2111

  @Test fun day17() = assertEquals(runOne("input17.txt", Day17()), Pair("3186", "1566376811584"))
  @Test fun day18() = assertEquals(runOne("input18.txt", Day18()), Pair("4608", "2652"))

  // Day 19 is about 45s of compute.
  @Ignore @Test fun day19() = assertEquals(runOne("input19.txt", Day19()), Pair("1653", "4212"))

  @Test fun day20() = assertEquals(runOne("input20.txt", Day20()), Pair("9866", "12374299815791"))

  // Day21 returns an algebraic result.
  @Test
  fun day21() =
      assertEquals(
          runPartOneOnly("input21.txt", Day21()), "223971851179174") // partTwo = 3379022190351

  @Test fun day22() = assertEquals(runOne("input22.txt", Day22()), Pair("50412", "130068"))
  @Test fun day23() = assertEquals(runOne("input23.txt", Day23()), Pair("4068", "968"))
  @Test fun day24() = assertEquals(runOne("input24.txt", Day24()), Pair("281", "807"))
  @Test fun day25() = assertEquals(runPartOneOnly("input25.txt", Day25()), "2-20=01--0=0=0=2-120")
}
