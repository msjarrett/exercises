package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

class Day15 : Exercise {
    private lateinit var items: List<String>

    override val testInput = """
rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
    """.trimIndent()

    override val testResultPart1 = 1320
    override val testResultPart2 = 145

    override suspend fun parse(lines: Flow<String>) {
        items = lines.single().split(',')
    }

    fun myHash(s: String): Int {
        var hash = 0
        for (c in s) {
            hash += c.code
            hash *= 17
            hash %= 256
        }
        return hash
    }

    override suspend fun partOne() = items.map(::myHash).sum()

    override suspend fun partTwo(): Int {
        val boxes = List(256) { mutableListOf<Pair<String, Int>>() }

        for (inst in items) {
            val equalPos = inst.indexOf('=')
            val lens = if (equalPos == -1) inst.substring(0, inst.length - 1) else inst.substring(0, equalPos)
            val focus = if (equalPos != -1) inst.substring(equalPos + 1).toInt() else 0
            val box = boxes[myHash(lens)]

            // Remove lens
            if (equalPos == -1) {
                // There will be at most one (we can't add duplicates)
                box.removeAll {
                    it.first == lens
                }
            }
            // Add lens
            else {
                val replaceLens = box.indexOfFirst { it.first == lens }
                if (replaceLens == -1) {
                    box.add(Pair(lens, focus))
                } else {
                    box.set(replaceLens, Pair(lens, focus))
                }
            }
        }

        return boxes.mapIndexed { box, lenses ->
            lenses.mapIndexed { slot, lens ->
                (box + 1) * (slot + 1) * lens.second
            }.sum()
        }.sum()
    }
}