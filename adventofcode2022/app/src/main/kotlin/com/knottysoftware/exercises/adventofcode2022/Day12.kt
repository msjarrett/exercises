package com.knottysoftware.exercises.adventofcode2022

import kotlin.collections.ArrayDeque

class Day12 : Exercise {
    private lateinit var map: List<List<Int>>

    private lateinit var start: Point
    private lateinit var end: Point
    private var width = 0
    private var height = 0

    override fun parse(lines: Sequence<String>) {
        val map = mutableListOf<List<Int>>()
        for (line in lines) {
            if (width == 0) width = line.length
            require(line.length == width)

            var x = line.indexOf('S')
            if (x != -1) start = Point(x, height)

            x = line.indexOf('E')
            if (x != -1) end = Point(x, height)

            map.add(line.map {
                when (it) {
                    'S' -> 0
                    'E' -> 25
                    else -> it.code - 'a'.code
                }
            })
            ++height
        }
        this.map = map
    }

    private fun pathLength(start: Point, testEnd: (Point) -> Boolean, testMove: (Point, Point) -> Boolean): Int {
        // BFS to victory, will give optimal route
        val visited = Array<Array<Boolean>>(height) { Array<Boolean>(width) { false } }
        
        val queue = ArrayDeque<Pair<Point, Int>>()
        queue.add(Pair(start, 0))

        while (!queue.isEmpty()) {
            val next = queue.removeFirst()
            val pos = next.first
            if (visited[pos.y][pos.x]) continue
            visited[pos.y][pos.x] = true  // Prevent immediate backtrack

            // Did we reach our destination?
            if (testEnd(pos)) return next.second

            // Explore
            val candidates = listOf<Point>(pos.move(Direction.LEFT), pos.move(Direction.RIGHT), pos.move(Direction.UP), pos.move(Direction.DOWN))
            for (c in candidates) {
                if (c.x < 0 || c.x >= width || c.y < 0 || c.y >= height) continue
                if (visited[c.y][c.x]) continue
                if (!testMove(c, pos)) continue

                queue.add(Pair(c, next.second + 1))
            }
        }

        throw IllegalArgumentException("No path")
    }

    override fun partOne(): Int = pathLength(start, { p -> p == end }, { c, p -> map[c.y][c.x] <= (map[p.y][p.x] + 1)})
    override fun partTwo(): Int = pathLength(end, { p -> map[p.y][p.x] == 0 }, { c, p -> map[c.y][c.x] >= (map[p.y][p.x] - 1)})
}