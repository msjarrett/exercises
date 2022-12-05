package com.knottysoftware.exercises.adventofcode2022;

import kotlin.collections.ArrayDeque

typealias Crate = Char

class Day5 : Exercise {
    private data class Move(var count: Int, var source: Int, var dest: Int)

    private lateinit var stacks: List<ArrayDeque<Crate>>
    private lateinit var moves: List<Move>

    private final val moveRegex = Regex("""move (\d+) from (\d+) to (\d+)""")
    private final val stackListRegex = Regex("""^( \d )(  \d )+""")

    override fun parse(lines: Sequence<String>) {
        val lineList = lines.toList()
        val iStacks = lineList.indexOfFirst { stackListRegex.matches(it) }
        require(iStacks > 0)
        val nStacks = (lineList[iStacks].length + 1) / 4

        // Parse stacks
        stacks = buildList<ArrayDeque<Crate>> {
            repeat(nStacks) { add(ArrayDeque()) }
        }
        for (iLine in (iStacks - 1) downTo 0) {
            val line = lineList[iLine]
            for (iStack in 0 until nStacks) {
                val iCrate = iStack * 4 + 1
                require(line.length > iCrate)
                if (line[iCrate] != ' ') {
                    stacks[iStack].addLast(line[iCrate])
                }
            }
        }

        // Parse moves
        moves = lineList.drop(iStacks + 2).map {
            val (moveN, moveFrom, moveTo) = moveRegex.matchEntire(it)!!.destructured
            Move(moveN.toInt(), moveFrom.toInt() - 1, moveTo.toInt() - 1)
        }
    }

    override fun partOne(): Long {
        // Execute moves.
        val stacks = this.stacks.map { ArrayDeque(it) }
        for ((count, source, dest) in moves) {
            repeat(count) { stacks[dest].addLast(stacks[source].removeLast()) }
        }

        println(stacks.map { it.last() }.joinToString(separator = ""))
        return 1
    }

    override fun partTwo(): Long {
        val stacks = this.stacks.map { ArrayDeque(it) }
        for ((count, source, dest) in moves) {
            stacks[dest].addAll(stacks[source].takeLast(count))
            repeat(count) { stacks[source].removeLast() }
        }

        println(stacks.map { it.last() }.joinToString(separator = ""))
        return 1
    }
}