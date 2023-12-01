package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class Day1 : Exercise {
    private lateinit var lines: List<String>

    override val testInput = """
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
    """.trimIndent()

    override val testInput2 = """
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
    """.trimIndent()

    override val testResultPart1 = 142
    override val testResultPart2 = 281

    override suspend fun parse(lines: Flow<String>) {
        this.lines = lines.toList()
    }

    override suspend fun partOne(): Any {
        return lines.map {
            var firstCh: Char = 'x'
            var lastCh: Char = 'x'
            for (c in it) {
                if (c >= '0' && c <= '9') {
                    firstCh = c
                    break
                }
            }
            for (c in it.reversed()) {
                if (c >= '0' && c <= '9') {
                    lastCh = c
                    break
                }
            }
            var str = "" + firstCh + lastCh
            str.toInt()
        }.sum()
    }

    override suspend fun partTwo(): Any {
        return lines.map {
            var firstCh: Char = 'x'
            var lastCh: Char = 'x'
            for (i in 0 ..< it.length ) {
                val ch = getNumCharAt(it, i)
                if (ch != null) {
                    firstCh = ch!!
                    break
                }
            }
            for (i in (it.length - 1)downTo 0) {
                val ch = getNumCharAt(it, i)
                if (ch != null) {
                    lastCh = ch!!
                    break
                }
            }
            var str = "" + firstCh + lastCh
            str.toInt()
        }.sum()
    }

    private fun getNumCharAt(s: String, i: Int): Char? {
        val charMappings = mapOf(
            "1" to '1',
            "2" to '2',
            "3" to '3',
            "4" to '4',
            "5" to '5',
            "6" to '6',
            "7" to '7',
            "8" to '8',
            "9" to '9',
            "one" to '1',
            "two" to '2',
            "three" to '3',
            "four" to '4',
            "five" to '5',
            "six" to '6',
            "seven" to '7',
            "eight" to '8',
            "nine" to '9',
        )
        charMappings.forEach {
            if (s.startsWith(it.key, i)) return it.value
        }
        return null
    }
}