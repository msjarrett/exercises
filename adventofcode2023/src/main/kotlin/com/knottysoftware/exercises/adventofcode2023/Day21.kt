package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlin.math.max
import kotlin.math.min


fun coordPoint(p: Point, g: Grid<*>): Point {
    var x = if (p.x < 0) ((p.x - (g.width - 1)) / g.width) else p.x / g.width
    var y = if (p.y < 0) ((p.y - (g.height - 1)) / g.height) else p.y / g.height
    return Point(x, y)
}

fun modPoint(p: Point, g: Grid<*>): Point {
    var x = p.x % g.width
    var y = p.y % g.height
    if (x < 0) x += g.width
    if (y < 0) y += g.height
    return Point(x, y)
}

class Day21 : Exercise {
    private lateinit var grid: Grid<Char>
    private lateinit var start: Point

    override val testInput = """
...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
    """.trimIndent()

   override suspend fun parse(lines: Flow<String>) {
        grid = gridFromStrings(lines.toList())

        searchStart@ for (y in 0 .. grid.maxY) {
            for (x in 0 .. grid.maxX) {
                if (grid.at(x, y) == 'S') {
                    start = Point(x,y)
                    break@searchStart
                }
            }
        }
    }

    fun maxPlots(totalSteps: Int): Int {
        // So if we branch the search space and allow backtracking, that could be as many as 4^64 paths!
        // However, every point we can reach, we can backtrack to in two steps. So any plot we can reach on the same
        // mod2 of the steps, we can count as visited and not explore it unless we arrive with more steps.
        val visited = mutableGrid(grid.width, grid.height, { _, _ -> -1 })

        val queue = SearchQueue(Pair(start, totalSteps))
        for ((pos, steps) in queue) {
            if (!grid.contains(pos)) continue
            if (grid.at(pos) == '#') continue
            if (visited.at(pos) >= steps) continue

            visited.set(steps, pos.x, pos.y)

            if (steps == 0) continue

            for (d in Direction.cardinalDirections)
                queue.addBFS(Pair(pos.move(d), steps - 1))
        }

        var totalCount = 0
        val desiredMod = totalSteps % 2
        visited.visit { x, y, v ->
            if (v != -1 && (v % 2) == desiredMod)
                totalCount++
        }
        return totalCount
    }



    abstract class Universe (val coord: Point) {
        abstract fun entrySteps(): Int
        abstract fun totalVisited(normalMod: Int, maxSteps: Int): Int
        abstract fun printVisited(sub: Int)
    }


    fun generateReferenceUniverses(expLimit: Int): Map<Point, Universe> {
        class VisitedUniverse (coord: Point) : Universe(coord) {
            val visited = mutableGrid(grid.width, grid.height, { _, _ -> Int.MAX_VALUE })

            var cachedMax: Int? = null
            var cachedCount: Int? = null

            override fun entrySteps(): Int {

                val v = visited.toList().flatten().min()
                return v
            }
            override fun totalVisited(normalMod: Int, maxSteps: Int): Int {
                if (cachedMax == null) {
                    cachedMax = visited.toList().flatten().filter { it != Int.MAX_VALUE }.max()
                }
                if (maxSteps > cachedMax!!) { // None will filter on distance
                    if (cachedCount == null) {
                        cachedCount = visited.toList().flatten().count {
                            it != Int.MAX_VALUE && ((it % 2) == normalMod)
                        }
                    }
                    return cachedCount!!
                }

               return visited.toList().flatten().count {
                   it != Int.MAX_VALUE && it <= maxSteps && ((it % 2) == normalMod)
               }
            }

            override fun printVisited(sub: Int) {
                for (y in 0 .. visited.maxY) {
                    for (x in 0 .. visited.maxX) {
                        if (visited.at(x, y) == Int.MAX_VALUE)
                            print("XXXX ")
                        else
                            print("%04d ".format(visited.at(x, y) + sub))
                    }
                    println()
                }
            }
        }

        val universes = mutableMapOf<Point, Universe>()
        val queue = SearchQueue(Pair(start, 0))
        for ((pos, steps) in queue) {
            val coord = coordPoint(pos, grid)
            val upos = modPoint(pos, grid)

            if (coord.x > expLimit || coord.x < -expLimit || coord.y > expLimit || coord.y < -expLimit) continue

            if (!universes.containsKey(coord)) {
                universes[coord] = VisitedUniverse(coord)
            }
            val universe = universes[coord] as VisitedUniverse

            if (grid.at(upos) == '#') continue
            if (universe.visited.at(upos) <= steps) continue

            universe.visited.set(steps, upos.x, upos.y)

            for (d in Direction.cardinalDirections)
                queue.addBFS(Pair(pos.move(d), steps + 1))
        }
        return universes
    }

    fun maxPlotsInfinite(totalSteps: Int): Long {
        // The input is 131x131. So even/odd wraps in each universe.
        // Since the steps is odd, we will label the starter universe "normal" and the alternate universes "goofy"
        val normalMod = totalSteps % 2

        // Visit the home universes
        // It seems to stabilize after a few iterations.
        val expLimit = 5
        val universes = generateReferenceUniverses(expLimit)
        val deltaDistance = universes[Point(expLimit, expLimit)]!!.entrySteps() - universes[Point(expLimit - 1, expLimit)]!!.entrySteps()

        //val expandedUniverse = generateReferenceUniverses(expLimit)

        println("Visited ${universes.size} universes. Using delta $deltaDistance.")

        fun Point.isGoofy() = ((this.x % 2) == 0) != ((this.y % 2) == 0)

        fun referenceUniverse(p: Point): Point {
            var x = when {
                p.x > expLimit -> expLimit
                p.x < - expLimit -> -expLimit
                else -> p.x
            }

            var y = when {
                p.y > expLimit -> expLimit
                p.y < -expLimit -> -expLimit
                else -> p.y
            }

            if (Point(x, y).isGoofy() != p.isGoofy()) {
                if (x == expLimit) x--
                else if (x == -expLimit) x++
                else if (y == expLimit) y--
                else if (y == -expLimit) y++
                else throw IllegalArgumentException()
            }

            return Point(x, y)
        }

        fun cloneCells(coord: Point): Int {
            val refPos = referenceUniverse(coord)
            if (refPos == coord) return 0

            val ref = universes[refPos]!!
            val toAdd = ref.totalVisited(
                normalMod,
                totalSteps - deltaDistance * coord.manhattanDistanceTo(ref.coord))
            return toAdd
        }

        // Clone universes.
        val universePairCellCount = universes[Point(0, expLimit)]!!.totalVisited(normalMod, totalSteps) +
                universes[Point(0, expLimit - 1)]!!.totalVisited(normalMod, totalSteps)
        var clonedCells = 0L
        val cloneRange = (totalSteps / deltaDistance) + expLimit
        println("Clone range $cloneRange")
        for (absY in 0 .. cloneRange) {
            if ((absY % 1000) == 0) println("AbsY $absY")
            val oldYClonedCells = clonedCells
            // Calculate the centerline
            var absX = if (absY <= expLimit) expLimit + 1 else 0
            if (absY > expLimit) {
                if (absX == 0) {
                    clonedCells += cloneCells(Point(absX, absY))
                    clonedCells += cloneCells(Point(absX, -absY))
                    absX++
                }

                // Calculate -1 and -2 for top and bottom.
                val posDown = Point(0, absY)
                val posUp = Point(0, -absY)
                val refDown = universes[referenceUniverse(posDown)]!!
                val refUp = universes[referenceUniverse(posUp)]!!
                val stepsDown = refDown.entrySteps() + deltaDistance * posDown.manhattanDistanceTo(refDown.coord)
                val stepsUp = refUp.entrySteps() + deltaDistance * posUp.manhattanDistanceTo(refUp.coord)

                if (false) {
                    var xSteps = (totalSteps - max(stepsDown, stepsUp)) / (deltaDistance * 2) - 4
                    if (xSteps > 0) {
                        val toAdd = xSteps * universePairCellCount * 4
                        clonedCells += toAdd
                    }
                    absX += xSteps * 2
                }
            }

            while(absX <= cloneRange) {
                check (absX != 0)
                val oldXClonedCells = clonedCells
                clonedCells += cloneCells(Point(absX, absY))
                clonedCells += cloneCells(Point(-absX, absY))
                clonedCells += cloneCells(Point(absX + 1, absY))
                clonedCells += cloneCells(Point(-absX - 1, absY))
                if (absY != 0) {
                    clonedCells += cloneCells(Point(absX, -absY))
                    clonedCells += cloneCells(Point(-absX, -absY))
                    clonedCells += cloneCells(Point(absX + 1, -absY))
                    clonedCells += cloneCells(Point(-absX - 1, -absY))
                }
                if (oldXClonedCells == clonedCells) break
                absX += 2
            }

            //if (oldYClonedCells == clonedCells) break
        }

        println("Added $clonedCells clones")
        for (u in universes.values.sortedBy { it.coord.x * 100000 + it.coord.y}) {
            //println("${u.coord} ${u.entrySteps()} ${u.totalVisited(normalMod, totalSteps)}")
        }

        return universes.values.map {
            it.totalVisited(normalMod, totalSteps).toLong()
        }.sum() + clonedCells
    }

    override suspend fun partOne() = maxPlots(64)

    // 50218558876336 is way too low.
    override suspend fun partTwo() = maxPlotsInfinite(if (grid.width == 11) 5000 else 26501365)
}