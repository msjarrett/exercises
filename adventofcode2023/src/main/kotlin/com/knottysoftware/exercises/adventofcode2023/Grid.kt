package com.knottysoftware.exercises.adventofcode2023

/** A dense rectangular grid. */
interface Grid<T> {
    val maxX: Int
    val maxY: Int
    val width: Int
    val height: Int

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

        override val maxY = data.size - 1
        override val maxX = data[0].size - 1
        override val width = data.size
        override val height = data[0].size

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

        override fun equals(other: Any?): Boolean {
            if (other !is Grid<*>) return false
            return toList() == other.toList()
        }

        override fun hashCode(): Int {
            return toList().hashCode()
        }

        override fun toString(): String {
            if (data[0][0] is Char) {
                return data.map { it.joinToString("") }.joinToString("\n")
            }
            return toList().toString()
        }
    }
}

private fun charTransform(ch: Char): Char = ch

fun <T> gridFromStrings(lines: List<String>, transform: (ch: Char) -> T): Grid<T> = grid(lines.map {
    it.map(transform)
})

fun gridFromStrings(lines: List<String>): Grid<Char> = grid(lines.map {
    it.map(::charTransform)
})


fun <T> mutableGrid(items: List<MutableList<T>>): MutableGrid<T> {
    return object : MutableGrid<T>, Grid<T> by grid(items) {
        private val data = items

        override fun set(v: T, x: Int, y: Int) {
            require(x >= 0)
            require(y >= 0)
            require(x <= maxX)
            require(y <= maxY)
            data[y][x] = v
        }

        // We have to redefine Object methods... unless there's delegate magic?
        override fun toString(): String {
            if (data [0][0] is Char) {
                return data.map { it.joinToString("") }.joinToString("\n")
            }
            return toList().toString()
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Grid<*>) return false
            return toList() == other.toList()
        }

        override fun hashCode(): Int {
            return toList().hashCode()
        }
    }
}

fun <T> mutableGrid(width: Int, height: Int, init: (x: Int, y: Int) -> T) =
    mutableGrid(List(height) { y ->
        MutableList(width) { x ->
            init(x, y)
        }
    })


fun <T> Grid<T>.toMutableGrid(): MutableGrid<T> = mutableGrid(toList().map { it.toMutableList() })

fun Grid<*>.contains(p: Point) = p.x >= 0 && p.y >= 0 && p.x <= maxX && p.y <= maxY

fun <T> Grid<T>.at(p: Point): T = at(p.x, p.y)