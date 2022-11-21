package com.knottysoftware.exercises.adventofcode2022;

import kotlin.sequences.Sequence

/**
 * Standard format for an Advent of Code daily exercise, 2022.
 *
 * (Still preliminary, based on 2021 example)
 * - Each exercise will have test data, with matching expected output.
 * - Each exercise will have a (larger) input data.
 * - Exercises are divided into part 1 / part 2, which use the same input data.
 * - The expected result is always a single positive integer (rarely, a very LARGE integer).
 */
public interface Exercise {
  fun parse(lines: Sequence<String>)
  fun partOne(): Long = throw NotImplementedError()
  fun partTwo(): Long = throw NotImplementedError()
}