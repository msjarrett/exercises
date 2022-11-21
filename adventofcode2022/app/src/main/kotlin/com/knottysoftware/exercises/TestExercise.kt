package com.knottysoftware.exercises.adventofcode2022;

/** Dummy exercise to validate runExercise logic. */
class TestExercise : Exercise {
    lateinit var lines : Sequence<String>

    override fun parse(lines: Sequence<String>) {
        this.lines = lines
    }

    override fun partOne(): Long {
        return lines.count().toLong()
    }
}