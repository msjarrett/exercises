package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class AppKtTest {
    @Test fun day1() = assertDay(Day1(), "input1.txt", 53921, 54676)

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