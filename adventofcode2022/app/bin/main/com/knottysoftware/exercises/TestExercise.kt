package com.knottysoftware.exercises.adventofcode2022;

class TestExercise : Exercise {
    lateinit val lines : Sequence<String>

    override fun parse(lines: Sequence<String>) {
        this.lines = lines
    }

    override fun partOne(): Long {
        return lines.count
    }
}