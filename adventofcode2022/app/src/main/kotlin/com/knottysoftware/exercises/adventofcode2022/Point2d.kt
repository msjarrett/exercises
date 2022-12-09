package com.knottysoftware.exercises.adventofcode2022

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

data class Point(val x: Int = 0, val y: Int = 0) {
    fun move(dir: Direction): Point =  when (dir) {
        Direction.UP -> Point(x, y - 1)
        Direction.DOWN -> Point(x, y + 1)
        Direction.LEFT -> Point(x - 1, y)
        Direction.RIGHT -> Point(x + 1, y)
    }
}