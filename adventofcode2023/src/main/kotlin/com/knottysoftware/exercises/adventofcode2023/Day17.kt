package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day17 : Exercise {
    private lateinit var grid: Grid<Int>

    override val testInput = """
2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
    """.trimIndent()

    override val testResultPart1 = 102
    override val testResultPart2 = 94

    override suspend fun parse(lines: Flow<String>) {
        grid = gridFromStrings(lines.toList()) { it.code - '0'.code }
    }

    data class Node(val pos: Point, val dir: Direction, val stepsInDir: Int, val cost: Int)

    override suspend fun partOne() = routeCost(1, 3)
    override suspend fun partTwo() = routeCost(4, 10)

    fun routeCost(minSteps: Int, maxSteps: Int): Int {
        var bestSolution = Int.MAX_VALUE

        val cache = mutableMapOf<Point, MutableList<Node>>()
        val goal = Point(grid.maxX, grid.maxY)

        // Loop ignores any direction on first move.
        val queue = SearchQueue(Node(Point(0,0), Direction.UP, 0, 0))
        //queue.statsInterval = 5.seconds
        queueLoop@ for (n in queue) {
            check (grid.contains(n.pos))       // We don't add these below.
            check (n.stepsInDir <= maxSteps)   // We don't add these below.
            // We do add lesser steps because we want to calculate cost.

            if (cache.containsKey(n.pos)) {
                for (cachedNode in cache[n.pos]!!) {
                    // TODO: This is too strict!
                    if (cachedNode.dir == n.dir && cachedNode.stepsInDir == n.stepsInDir && cachedNode.cost <= n.cost) {
                        continue@queueLoop
                    }
                }
            } else {
                cache[n.pos] = mutableListOf()
            }

            // We are a valid node!
            cache[n.pos]!!.add(n)

            // Are we done?
            if (n.pos == goal && n.stepsInDir >= minSteps) {
                if (n.cost < bestSolution) {
                    bestSolution = n.cost
                }
                // TODO: It's A* so we can theoretically break here.
            }

            // Add new nodes.
            for (dir in listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)) {
                if (n.cost != 0) { // Can do anything on first step
                    if (dir == n.dir.turnReverse()) continue                 // Can't reverse
                    if (dir != n.dir && n.stepsInDir < minSteps) continue    // Must travel at least minSteps before turning
                    if (dir == n.dir && n.stepsInDir >= maxSteps) continue   // Can't travel more than maxSteps without turning
                }
                val newPos = n.pos.move(dir)
                if (!grid.contains(newPos)) continue

                val newNode = Node(newPos, dir, if (n.dir == dir) n.stepsInDir + 1 else 1, n.cost + grid.at(newPos))
                queue.addScored(newNode, newNode.cost + newNode.pos.manhattanDistanceTo(goal))
            }
        }

        return bestSolution
    }
}