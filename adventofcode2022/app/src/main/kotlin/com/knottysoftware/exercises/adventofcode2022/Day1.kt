package com.knottysoftware.exercises.adventofcode2022;

class Day1 : Exercise {
    lateinit var elves: List<List<Int>>

    override fun parse(lines: Sequence<String>) {
        val elves = mutableListOf<MutableList<Int>>()
        var nextElf = true
        for (line in lines.asIterable()) {
            if (line.isEmpty()) {
                nextElf = true
            } else {
                if (nextElf) {
                    nextElf = false
                    elves.add(mutableListOf<Int>())
                }
                elves.last().add(line.toInt())
            }
        }
        this.elves = elves    
    }

    override fun partOne() = elves.map { it.sum() }.maxOrNull()!!
    override fun partTwo() = elves.map { it.sum() }.sortedDescending().take(3).sum()
}