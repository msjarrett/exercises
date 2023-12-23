package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList


class Day23 : Exercise {
    private lateinit var grid:  Grid<Char>
    private lateinit var start: Point
    private lateinit var end: Point

    override val testInput = """
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
    """.trimIndent()

    override val testResultPart1 = 94
    override val testResultPart2 = 154

    override suspend fun parse(lines: Flow<String>) {
        grid = gridFromStrings(lines.toList())
        check(grid.at(1,0) == '.')
        start = Point(1,0)
        check(grid.at(grid.maxX - 1, grid.maxY) == '.')
        end = Point(grid.maxX - 1, grid.maxY)
    }

    fun longestPathWithSlopes(): Int {
        val queue = SearchQueue(Pair(start, listOf<Point>()))
        var longestPath = 0
        var longestSoFar = 0

        for ((pos, path) in queue) {
            if (path.size > (longestSoFar + 10)) {
                longestSoFar = path.size
                //println("Exploring paths len $longestSoFar solution $longestPath")
            }
            if (pos == end) {
                if (longestPath < path.count()) {
                    longestPath = path.count()
                }
                continue    // At the end, can't continue (well, because the end is not a node).
            }

            check (pos.y >= 0) // Don't leave by the entrance
            check (grid.at(pos) != '#')

            if (path.contains(pos)) continue // Don't retrace steps.

            // Except for the entry, the grid is enclosed.
            // Add only valid paths.
            val newDirs = mutableListOf<Direction>()

            when (grid.at(pos)) {
                '.' -> newDirs.addAll(Direction.cardinalDirections)
                '^' -> newDirs.add(Direction.UP)
                'v' -> newDirs.add(Direction.DOWN)
                '<' -> newDirs.add(Direction.LEFT)
                '>' -> newDirs.add(Direction.RIGHT)
                else -> throw IllegalArgumentException()
            }

            newDirs.removeAll {
                val newPos = pos.move(it)
                if (newPos.y <= 0) true
                else if (path.isNotEmpty() && newPos == path.last()) true // Don't  backtrack (makes dead check easier)
                else if (grid.at(newPos) == '#') true
                else false
            }

            val newPath = path.plus(pos)
            for (d in newDirs) {
                queue.addDFS(Pair(pos.move(d), newPath))
            }
        }
        return longestPath
    }

    fun buildGraph(): Map<Point, Map<Point, Int>> {
        // Reduce the map to a graph.
        val graph = mutableMapOf(start to mutableMapOf<Point, Int>(), end to mutableMapOf())

        data class Explore(val cur: Point, val prev: Point, val lastNode: Point, val cost: Int)

        val queue = SearchQueue(Explore(start.move(Direction.DOWN), start, start, 1))
        for ((cur, prev, lastNode, cost) in queue) {
            check (cur != lastNode)  // We could handle self edges... but lets not

            if (graph.containsKey(cur)) {    // We're at a new node
                val curNode = graph[cur]!!
                val prevNode = graph[lastNode]!!
                // We're at an existing node, add both links and bail.
                check (!curNode.containsKey(lastNode) || curNode[lastNode] == cost)
                curNode[lastNode] = cost
                prevNode[cur] = cost
                continue
            }

            val newDirs = Direction.cardinalDirections.toMutableList()
            newDirs.removeAll {
                val newPos = cur.move(it)
                if (newPos == prev) true // Don't backtrack
                else if (newPos.y <= 0) true // Don't walk out the entrance
                else if (grid.at(newPos) == '#') true
                else false
            }

            // Are we at an intersection?
            if (newDirs.size > 1) {
                graph[cur] = mutableMapOf()
                graph[cur]!![lastNode] = cost
                check(!graph[lastNode]!!.containsKey(cur))
                graph[lastNode]!![cur] = cost
                for (d in newDirs) {
                    queue.addBFS(Explore(cur.move(d), cur, cur, 1))
                }
            } else {
                // single() will throw if we have a dead end.
                queue.addBFS(Explore(cur.move(newDirs.single()), cur, lastNode, cost + 1))
            }
        }

        return graph
    }

    fun longestPathGraphDpValue(path: List<Point>, graph: Map<Point, Map<Point, Int>>): Int? {
        if (path.last() == end) {
            return 0
        }

        val bestCost = graph[path.last()]!!.filter {
            !path.contains(it.key)
        }.mapNotNull {
            longestPathGraphDpValue(path.plus(it.key), graph)?.let { dist -> it.value + dist }
        }.maxOrNull()

        return bestCost
    }

    fun longestPathGraphDp() = longestPathGraphDpValue(listOf(start), buildGraph())!!

    // Trying to find if the graph is pruneable.
    fun findExtraEdges(): Int {
        val graph = buildGraph()
        println("Examining graph of size ${graph.size}")
        val deleteEdges = mutableListOf<Pair<Point, Point>>()
        for (root in graph.keys) {
            if (root == start) continue
            for (rootPrev in graph[root]!!.keys) {
                if (rootPrev == end) continue

                val visited = mutableSetOf<Point>()
                val queue = SearchQueue(root)
                for (n in queue) {
                    if (n in visited) continue
                    visited.add(n)
                    for (next in graph[n]!!.keys) {
                        if (n == root && next == rootPrev) continue
                        queue.addBFS(next)
                    }
                }
                if (!visited.contains(end)) {
                    deleteEdges.add(rootPrev to root)
                }
            }
        }
        return 0
    }

    // Simple BFS search works for part 1. The slopes make for few paths.
    override suspend fun partOne() = longestPathWithSlopes()

    // - Part 1 search was too slow.
    // - We optimized by switching to a graph representation, which is only 36 nodes.
    //   2 for start and end, 34 interior (3 or 4 weighted edges).
    //     - No self cycles, which would be possible by making an interior space.
    //     - No dead ends (except start/finish)
    //     - No isolated sub-graphs that can be pruned.
    // - Searching the graph was much faster but still too slow.
    // - DP seems equally slow. Caches don't seem to work.
    override suspend fun partTwo() = longestPathGraphDp()
}