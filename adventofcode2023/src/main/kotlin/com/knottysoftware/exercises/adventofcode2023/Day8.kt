package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day8 : Exercise {

    private lateinit var steps: List<Char>
    private lateinit var edges: Map<String, Pair<String, String>>

    override val testInput = """
LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
    """.trimIndent()

    override val testInput2 = """
LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
    """.trimIndent()
    override val testResultPart1 = 6
    override val testResultPart2 = 6L

    override suspend fun parse(lines: Flow<String>) {
        val lineList = lines.toList()
        steps = lineList[0].toList()

        val edgesRegex = Regex("""(\w+) = \((\w+), (\w+)\)""")
        edges = lineList.drop(2).map {
            val (node, left, right) = edgesRegex.matchEntire(it)!!.destructured
            node to Pair(left, right)
        }.toMap()
    }

    override suspend fun partOne(): Int {
        var cur = "AAA"
        var iStep = 0
        var moves = 0

        while (cur != "ZZZ") {
            moves++
            val step = steps[iStep++]
            if (iStep == steps.size) iStep = 0
            if (step == 'L') cur = edges[cur]!!.first
            else if (step == 'R') cur = edges[cur]!!.second
        }
        return moves
    }

    override suspend fun partTwo(): Long {
        // We max out Int after a few minutes, so brute force is likely out. A peer said their answer was 13 digits.
        // The A's match the Z's, and my input has six of each.
        // The graph is shallow, but nodes can be reached by multiple paths on each input.
        // BUT there is definitely a distinct structure to the graph. It's always a loop, and you always reach a Z
        // at the end of the steps path.
        //   STEPS 263
        //   VBA -> DVZ in 16043 -> DVZ in 32086
        //   TVA -> XKZ in 20777 -> XKZ in 41554
        //   DVA -> HSZ in 13939 -> HSZ in 27878
        //   VPA -> GGZ in 18673 -> GGZ in 37346
        //   AAA -> ZZZ in 11309 -> ZZZ in 22618
        //   DTA -> HLZ in 17621 -> HLZ in 35242

        //println(" STEPS ${steps.size}")

        // Every A loops to a unique Z on whole multiples of the path.
        // Find the loop length.
        val loops = edges.keys.filter { it[2] == 'A'}.map {
            var cur = it
            var iStep = 0
            var moves = 0

            while (cur[2] != 'Z') {
                moves++
                val step = steps[iStep++]
                if (iStep == steps.size) iStep = 0
                if (step == 'L') cur = edges[cur]!!.first
                else if (step == 'R') cur = edges[cur]!!.second
            }
            //println("$it -> $cur in $moves (${moves / steps.size})")
            (moves / steps.size).toLong()
        }

        // If all the loops are prime (they are) then we can just multiply them.
        return loops.reduce {acc, v -> acc * v} * steps.size
    }
}