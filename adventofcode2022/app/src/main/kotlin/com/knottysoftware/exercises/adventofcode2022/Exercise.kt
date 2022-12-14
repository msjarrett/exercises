package com.knottysoftware.exercises.adventofcode2022

import kotlin.sequences.Sequence

/** Standard format for an Advent of Code daily exercise, 2022. */
public interface Exercise {
  fun parse(lines: Sequence<String>)

  // If true, the input will be read a second time (on the same object) for
  // part two.
  fun needReparse(): Boolean = false

  // Return anything with a toString() that can be printed.
  fun partOne(): Any = throw NotImplementedError()
  fun partTwo(): Any = throw NotImplementedError()
}
