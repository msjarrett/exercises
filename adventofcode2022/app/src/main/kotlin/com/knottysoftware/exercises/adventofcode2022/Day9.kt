package com.knottysoftware.exercises.adventofcode2022

class Day9 : Exercise {
    private lateinit var moves : List<Pair<Direction, Int>>
    
    override fun parse(lines: Sequence<String>) {
        val moves = mutableListOf<Pair<Direction, Int>>()
        for (line in lines) {
            moves.add(Pair(
                when(line[0]) {
                    'U' -> Direction.UP
                    'D' -> Direction.DOWN
                    'L' -> Direction.LEFT
                    'R' -> Direction.RIGHT
                    else -> throw IllegalArgumentException()
                },
            line.substring(2).toInt()))
        }
        this.moves = moves
    }

    fun moveTail(head: Point, _tail: Point): Point {
        var tail = _tail
        val dx = head.x - tail.x
        val dy = head.y - tail.y

        if (dx > 1) {                    
            tail = tail.move(Direction.RIGHT)
            if (dy > 0) tail = tail.move(Direction.DOWN)
            if (dy < 0) tail = tail.move(Direction.UP)
        } else if (dx < -1) {
            tail = tail.move(Direction.LEFT)
            if (dy > 0) tail = tail.move(Direction.DOWN)
            if (dy < 0) tail = tail.move(Direction.UP)
        } else if (dy > 1) {
            tail = tail.move(Direction.DOWN)
            if (dx > 0) tail = tail.move(Direction.RIGHT)
            if (dx < 0) tail = tail.move(Direction.LEFT)
        } else if (dy < -1) {
            tail = tail.move(Direction.UP)
            if (dx > 0) tail = tail.move(Direction.RIGHT)
            if (dx < 0) tail = tail.move(Direction.LEFT)
        }
        return tail    
    }

    override fun partOne(): Int {
        var head = Point()
        var tail = Point()
        val visited = mutableSetOf<Point>()

        for (move in moves) {
            repeat (move.second) {
                head = head.move(move.first)
                tail = moveTail(head, tail)
                //println("head $head -> tail $tail")
                visited.add(tail)
            }
        }
        return visited.size
    }

    override fun partTwo(): Int {
        val rope = MutableList(10) { Point() }
        val visited = mutableSetOf<Point>()

        for (move in moves) {
            repeat (move.second) {
                rope[0] = rope[0].move(move.first)
                for (i in 1 until rope.size) {
                    rope[i] = moveTail(rope[i - 1], rope[i])
                }
                //println(rope)
                visited.add(rope.last())
            }
        }
        return visited.size
    }
}