package com.knottysoftware.exercises.adventofcode2022;

import kotlin.math.max

class Day8 : Exercise {
    private lateinit var trees: Array<IntArray>
    private var width = 0
    private var height = 0

    fun inRange(x: Int, y: Int) = x >= 0 && y >= 0 && x < width && y < height
    
    fun explore(x_: Int, y_: Int, dx: Int, dy: Int, process: (x: Int, y: Int, value: Int) -> Boolean) {
        var x = x_
        var y = y_
        while (inRange(x,y)) {
            if (!process(x, y, trees[y][x])) break
            x += dx
            y += dy
        }
    }

    override fun parse(lines: Sequence<String>) {
        val trees = mutableListOf<IntArray>()
        for (line in lines) {
            if (width == 0) width = line.length
            require(line.length == width)
            trees.add(line.map { it.code - '0'.code }.toIntArray())
        }
        this.trees = trees.toTypedArray()
        this.width = width
        this.height = trees.size
    }

    override fun partOne(): Long {
        val visible = Array<BooleanArray>(height) { BooleanArray(width) { false }}

        fun findVisible(x: Int, y: Int, dx: Int, dy: Int) {
            var biggest = -1
            explore(x, y, dx, dy) { x_: Int, y_: Int, value: Int -> 
                if (value > biggest) {
                    visible[y_][x_] = true
                    biggest = value
                }
                value < 9
            }
        }

        for (y in 0 until height) {
            findVisible(0, y, 1, 0)  // Approach from left 
            findVisible(width - 1, y, -1, 0)  // Approach from right
        }

        for (x in 0 until width) {
            findVisible(x, 0, 0, 1)  // Approach from top
            findVisible(x, height - 1, 0, -1)  // Approach from bottom
        }
        
        return visible.sumOf { it.count { it == true } }.toLong()
    }

    override fun partTwo(): Long {
        var score = 0

        // Brute force...
        for (x in 0 until width) {
            for (y in 0 until height) {
                val treeHeight = trees[y][x]
                
                var treesUp = 0
                explore(x, y - 1, 0, -1) { _: Int, _: Int, value: Int -> 
                    treesUp++
                    value < treeHeight
                }
                
                var treesDown = 0
                explore(x, y + 1, 0, 1) { _: Int, _: Int, value: Int -> 
                    treesDown++
                    value < treeHeight
                }

                var treesLeft = 0
                explore(x - 1, y, -1, 0) { _: Int, _: Int, value: Int -> 
                    treesLeft++
                    value < treeHeight
                }
                
                var treesRight = 0
                explore(x + 1, y, 1, 0) { _: Int, _: Int, value: Int -> 
                    treesRight++
                    value < treeHeight
                }

                score = max(score, treesUp * treesDown * treesLeft * treesRight)
            }
        }
        return score.toLong()
    }
}