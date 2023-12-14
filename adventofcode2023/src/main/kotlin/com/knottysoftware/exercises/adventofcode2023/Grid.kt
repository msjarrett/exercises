package com.knottysoftware.exercises.adventofcode2023

/** A dense rectangular grid. */
interface Grid<T> {
    val maxX: Int
    val maxY: Int

    fun at(x: Int, y: Int): T

    // Rows of cols of T
    fun toList(): List<List<T>>
}

/** A grid where values can be changed (but not the dimensions). */
interface MutableGrid<T> : Grid<T> {
    fun set(v: T, x: Int, y: Int)
}

fun <T> grid(items: List<List<T>>): Grid<T> {
    require(items.size > 0)
    require(items[0].size > 0)
    require(items.all { it.size == items[0].size })

    return object : Grid<T> {
        private val data = items

        override val maxY = items.size
        override val maxX = items[0].size

        override fun at(x: Int, y: Int): T {
            require(x >= 0)
            require(y >= 0)
            require(x <= maxX)
            require(y <= maxY)
            return data[y][x]
        }

        override fun toList(): List<List<T>> {
            return data
        }
    }
}

fun <T> gridFromStrings(lines: List<String>, transform: (ch: Char) -> T): Grid<T> = grid(lines.map {
    it.map(transform)
})
