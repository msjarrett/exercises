package com.knottysoftware.exercises.adventofcode2023

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class Day21Test {
    val grid = grid(3, 3, '*')

    @Test fun coordNeutral() {
        assertEquals(Point(0, 0), coordPoint(Point(0, 0), grid))
        assertEquals(Point(0, 0), coordPoint(Point(2, 2), grid))
    }

    @Test fun coordDownRight() {
        assertEquals(Point(1, 1), coordPoint(Point(3, 3), grid))
        assertEquals(Point(1, 1), coordPoint(Point(5, 5), grid))
    }

    @Test fun coordUpLeft() {
        assertEquals(Point(-1, -1), coordPoint(Point(-1, -1), grid))
        assertEquals(Point(-1, -1), coordPoint(Point(-3, -3), grid))
    }

    @Test fun coordDoubleUpLeft() {
        assertEquals(Point(-2, -2), coordPoint(Point(-4, -4), grid))
    }

    @Test fun modCrossLeft() {
        var p = Point(0,0).move(Direction.LEFT)
        assertEquals(Point(2, 0), modPoint(p, grid))
    }

    @Test fun modCrossUp() {
        var p = Point(0,0).move(Direction.UP)
        assertEquals(Point(0, 2), modPoint(p, grid))
    }

    @Test fun modCrossUpLeftToOrigin() {
        var p = Point(0,0).move(Direction.UP, 3).move(Direction.LEFT, 3)
        assertEquals(Point(0, 0), modPoint(p, grid))
    }
}