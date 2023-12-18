package com.knottysoftware.exercises.adventofcode2023

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * I really struggled to get all the edge cases of the vectorArea method, so adding some test cases based on
 * samples I used in development
 */
class Day18KtTest {
    @Test fun areaSquare() {
        assertEquals(16, vectorArea(setOf(
            Pair(Point(0, 0), Point(3, 0)),
            Pair(Point(3, 0), Point(3, 3)),
            Pair(Point(3, 3), Point(0, 3)),
            Pair(Point(0, 3), Point(0, 0)),
        )))
    }

    @Test fun areaH() {
        assertEquals(18, vectorArea(setOf(
            Pair(Point(0, 0), Point(1, 0)),
            Pair(Point(1, 0), Point(1, 1)),
            Pair(Point(1, 1), Point(3, 1)),
            Pair(Point(3, 1), Point(3, 0)),
            Pair(Point(3, 0), Point(4, 0)),
            Pair(Point(4, 0), Point(4, 3)),
            Pair(Point(4, 3), Point(3, 3)),
            Pair(Point(3, 3), Point(3, 2)),
            Pair(Point(3, 2), Point(1, 2)),
            Pair(Point(1, 2), Point(1, 3)),
            Pair(Point(1, 3), Point(0, 3)),
            Pair(Point(0, 3), Point(0, 0)),
        )))
    }

    @Test fun areaRightOverhang() {
        assertEquals(21, vectorArea(setOf(
            Pair(Point(0, 0), Point(4, 0)),
            Pair(Point(4, 0), Point(4, 2)),
            Pair(Point(4, 2), Point(2, 2)),
            Pair(Point(2, 2), Point(2, 4)),
            Pair(Point(2, 4), Point(0, 4)),
            Pair(Point(0, 4), Point(0, 0)),
        )))
    }
}
