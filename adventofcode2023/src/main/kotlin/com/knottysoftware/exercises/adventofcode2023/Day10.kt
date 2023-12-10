package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day10 : Exercise {
    enum class Pipe {
        START {
            override fun connections(cur: Point) = throw IllegalStateException()
            override fun leftAdjacent(cur: Point, last: Point) = throw IllegalStateException()
            override fun rightAdjacent(cur: Point, last: Point) = throw IllegalStateException()
        },
        UPDOWN {
            override fun connections(cur: Point) = listOf(cur.move(Direction.UP), cur.move(Direction.DOWN))
            override fun leftAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.DOWN -> listOf(cur.move(Direction.RIGHT))
                Direction.UP -> listOf(cur.move(Direction.LEFT))
                else -> throw IllegalArgumentException()
            }
            override fun rightAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.DOWN -> listOf(cur.move(Direction.LEFT))
                Direction.UP -> listOf(cur.move(Direction.RIGHT))
                else -> throw IllegalArgumentException()
            }
        },
        LEFTRIGHT {
            override fun connections(cur: Point) = listOf(cur.move(Direction.LEFT), cur.move(Direction.RIGHT))
            override fun leftAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.LEFT -> listOf(cur.move(Direction.DOWN))
                Direction.RIGHT -> listOf(cur.move(Direction.UP))
                else -> throw IllegalArgumentException()
            }
            override fun rightAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.LEFT -> listOf(cur.move(Direction.UP))
                Direction.RIGHT -> listOf(cur.move(Direction.DOWN))
                else -> throw IllegalArgumentException()
            }
        },
        UPLEFT {
            override fun connections(cur: Point) = listOf(cur.move(Direction.UP), cur.move(Direction.LEFT))
            override fun leftAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.RIGHT -> listOf()
                Direction.DOWN -> listOf(cur.move(Direction.DOWN), cur.move(Direction.RIGHT))
                else -> throw IllegalArgumentException()
            }
            override fun rightAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.RIGHT -> listOf(cur.move(Direction.DOWN), cur.move(Direction.RIGHT))
                Direction.DOWN -> listOf()
                else -> throw IllegalArgumentException()
            }
        },
        UPRIGHT {
            override fun connections(cur: Point) = listOf(cur.move(Direction.UP), cur.move(Direction.RIGHT))
            override fun leftAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.LEFT -> listOf(cur.move(Direction.DOWN), cur.move(Direction.LEFT))
                Direction.DOWN -> listOf()
                else -> throw IllegalArgumentException()
            }
            override fun rightAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.LEFT -> listOf()
                Direction.DOWN -> listOf(cur.move(Direction.DOWN), cur.move(Direction.LEFT))
                else -> throw IllegalArgumentException()
            }
        },
        DOWNLEFT {
            override fun connections(cur: Point) = listOf(cur.move(Direction.DOWN), cur.move(Direction.LEFT))
            override fun leftAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.RIGHT -> listOf(cur.move(Direction.UP), cur.move(Direction.RIGHT))
                Direction.UP -> listOf()
                else -> throw IllegalArgumentException()
            }
            override fun rightAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.RIGHT -> listOf()
                Direction.UP -> listOf(cur.move(Direction.UP), cur.move(Direction.RIGHT))
                else -> throw IllegalArgumentException()
            }
        },
        DOWNRIGHT {
            override fun connections(cur: Point) = listOf(cur.move(Direction.DOWN), cur.move(Direction.RIGHT))
            override fun leftAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.LEFT -> listOf()
                Direction.UP -> listOf(cur.move(Direction.UP), cur.move(Direction.LEFT))
                else -> throw IllegalArgumentException()
            }
            override fun rightAdjacent(cur: Point, last: Point) = when (last.directionTo(cur)) {
                Direction.LEFT -> listOf(cur.move(Direction.UP), cur.move(Direction.LEFT))
                Direction.UP -> listOf()
                else -> throw IllegalArgumentException()
            }
        };

        abstract fun connections(cur: Point): List<Point>
        abstract fun leftAdjacent(cur: Point, last: Point): List<Point>
        abstract fun rightAdjacent(cur: Point, last: Point): List<Point>

        fun nextConnection(cur: Point, last: Point) = connections(cur).minus(last).single()
    }
    private lateinit var grid: Map<Point, Pipe>
    private lateinit var start: Point
    private var maxX: Int = 0
    private var maxY: Int = 0

    override val testInput = """
7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ
    """.trimIndent()

    override val testInput2 = """
FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
    """.trimIndent()

    override val testResultPart1 = 8
    override val testResultPart2 = 10

    override suspend fun parse(lines: Flow<String>) {
        grid = lines.toList().mapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                if (c == '.') null
                else Point(x, y) to when (c) {
                    'S' -> {
                        start = Point(x, y)
                        Pipe.START
                    }
                    '|' -> Pipe.UPDOWN
                    '-' -> Pipe.LEFTRIGHT
                    'J' -> Pipe.UPLEFT
                    'F' -> Pipe.DOWNRIGHT
                    '7' -> Pipe.DOWNLEFT
                    'L' -> Pipe.UPRIGHT
                    else -> throw IllegalArgumentException()
                }
            }
        }.flatten().toMap()

        // Filter irrelevant nodes - both parts only care about the loop around start.
        // Inner irrelevant pipes might confuse part 2.
        val starts = listOf(
            start.move(Direction.UP), start.move(Direction.DOWN),
            start.move(Direction.LEFT), start.move(Direction.RIGHT)).filter {
                grid[it]?.connections(it)?.any { it == start } ?: false
            }
        assert(starts.size == 2)

        val newGrid = mutableMapOf(start to when {
            starts.contains(start.move(Direction.UP)) && starts.contains(start.move(Direction.DOWN)) -> Pipe.UPDOWN
            starts.contains(start.move(Direction.LEFT)) && starts.contains(start.move(Direction.RIGHT)) -> Pipe.LEFTRIGHT
            starts.contains(start.move(Direction.UP)) && starts.contains(start.move(Direction.LEFT)) -> Pipe.UPLEFT
            starts.contains(start.move(Direction.UP)) && starts.contains(start.move(Direction.RIGHT)) -> Pipe.UPRIGHT
            starts.contains(start.move(Direction.DOWN)) && starts.contains(start.move(Direction.LEFT)) -> Pipe.DOWNLEFT
            starts.contains(start.move(Direction.DOWN)) && starts.contains(start.move(Direction.RIGHT)) -> Pipe.DOWNRIGHT
            else -> throw IllegalStateException()
        })

        var cur = starts[0]
        var last = start
        while (cur != start) {
            newGrid.put(cur, grid[cur]!!)
            val next = grid[cur]!!.nextConnection(cur, last)
            last = cur
            cur = next
        }

        grid = newGrid
        maxX = grid.keys.map { it.x }.max()
        maxY = grid.keys.map { it.y }.max()
    }

    override suspend fun partOne(): Int {
        val paths = grid[start]!!.connections(start)

        var lastA = start
        var lastB = start
        var curA = paths[0]
        var curB = paths[1]
        var moves = 1

        while (curA != curB && curA != lastB) {
            //println("$moves : $curA $curB")
            moves++
            val newA = grid[curA]!!.nextConnection(curA, lastA)
            lastA = curA
            curA = newA

            val newB = grid[curB]!!.nextConnection(curB, lastB)
            lastB = curB
            curB = newB
        }


        return moves
    }

    fun exploreSpace(p: Point, points: MutableList<Point>): Boolean {
        val queue = mutableListOf(p)
        var isExt = false
        while (!queue.isEmpty()) {
            val cur = queue.removeFirst()
            if (grid.containsKey(cur)) continue // Collides with a pipe
            if (points.contains(cur)) continue // Already found it
            if (cur.x < 0 || cur.y < 0 || cur.x > maxX || cur.y > maxY) {
                isExt = true
                continue
            }

            // It's Valid!
            points.add(cur)

            queue.add(cur.move(Direction.UP))
            queue.add(cur.move(Direction.DOWN))
            queue.add(cur.move(Direction.LEFT))
            queue.add(cur.move(Direction.RIGHT))
        }

        return isExt
    }

    override suspend fun partTwo(): Any {
        val leftPoints = mutableListOf<Point>()
        val rightPoints = mutableListOf<Point>()

        var leftExterior = false
        var rightExterior = false

        var last = start
        var cur = grid[start]!!.connections(start)[0]

        //println("START $start = ${grid[start]} moving " + start.directionTo(cur))


        while (cur != start) {
            for (p in grid[cur]!!.leftAdjacent(cur, last)) {
                if (exploreSpace(p, leftPoints)) leftExterior = true
            }
            for (p in grid[cur]!!.rightAdjacent(cur, last)) {
                if (exploreSpace(p, rightPoints)) rightExterior = true
            }

            val next = grid[cur]!!.nextConnection(cur, last)
            last = cur
            cur = next
        }

        //println("LEFT  ${leftPoints.size} ext $leftExterior")
        //println("RIGHT ${rightPoints.size} ext $rightExterior")

        return if (leftExterior == false) leftPoints.size else if (rightExterior == false) rightPoints.size else throw IllegalStateException()
    }
}