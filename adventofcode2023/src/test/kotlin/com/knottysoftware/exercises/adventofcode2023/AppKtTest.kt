package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class AppKtTest {
    @Test fun day1() = assertDay(Day1(), "input1.txt", 53921, 54676)
    @Test fun day2() = assertDay(Day2(), "input2.txt", 3059, 65371)
    @Test fun day3() = assertDay(Day3(), "input3.txt", 526404, 84399773)
    @Test fun day4() = assertDay(Day4(), "input4.txt", 27059, 5744979)
    @Test fun day5() = assertDay(Day5(), "input5.txt", 26273516L, 34039469L)
    @Test fun day6() = assertDay(Day6(), "input6.txt", 227850, 42948149)
    @Test fun day7() = assertDay(Day7(), "input7.txt", 248422077, 249817836)
    @Test fun day8() = assertDay(Day8(), "input8.txt", 11309, 13740108158591L)
    @Test fun day9() = assertDay(Day9(), "input9.txt", 1898776583, 1100)
    @Test fun day10() = assertDay(Day10(), "input10.txt", 6856, 501)
    @Test fun day11() = assertDay(Day11(), "input11.txt", 9686930L, 630728425490L)

    private fun assertDay(e: Exercise, input: String, part1: Any, part2: Any) {
        runTest {
            val file = File(e.javaClass.classLoader.getResource(input)!!.toURI())
            e.parse(file.lineFlow())
            assertEquals(part1, e.partOne())
            if (e.needReparse())
                e.parse(file.lineFlow())
            assertEquals(part2, e.partTwo())
        }
    }
}