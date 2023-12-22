package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlin.math.min
import kotlin.time.TimeSource

enum class Direction3 {
    UP,     // z++
    DOWN,   // z--
    LEFT,   // x--
    RIGHT,  // x++
    IN,     // y--
    OUT,    // y++
}

data class Point3 (val x: Int, val y: Int, val z: Int) {
    fun move(dir: Direction3, dist: Int = 1) = when (dir) {
        Direction3.UP -> Point3(x, y, z + dist)
        Direction3.DOWN -> Point3(x, y, z - dist)
        Direction3.LEFT -> Point3(x - dist, y, z)
        Direction3.RIGHT -> Point3(x + dist, y, z)
        Direction3.IN -> Point3(x, y - dist, z)
        Direction3.OUT -> Point3(x, y + dist, z)
    }

    fun directionTo(dest: Point3): Direction3? {
        if (x == dest.x && y == dest.y && z == dest.z) {
            return null
        } else if (y == dest.y && z == dest.z) {
            if (x < dest.x) return Direction3.RIGHT else return Direction3.LEFT
        } else if (x == dest.x && z == dest.z) {
            if (y < dest.y) return Direction3.OUT else return Direction3.IN
        } else if (x == dest.x && y == dest.y) {
            if (z < dest.z) return Direction3.UP else return Direction3.DOWN
        } else {
            return null
        }
    }

    override fun toString() = "($x,$y,$z)"
}

data class Brick (val pA: Point3, val pB: Point3) {
    val dir = pA.directionTo(pB)

    init {
        check (pA == pB || dir != null)     // Unless it's a single cube, it must have direction
    }

    fun move(dir: Direction3, dist: Int = 1) = Brick(pA.move(dir, dist), pB.move(dir, dist))

    fun points() = when (dir) {
        null -> listOf(pA)
        else -> buildList {
            var p = pA
            while (p != pB) {
                add(p)
                p = p.move(dir)
            }
            add(p)  // Add endpoint
        }
    }

    fun intersect (other: Brick): Boolean {
        // TODO: Bounding box optimization
        return !points().intersect(other.points()).isEmpty()
    }

    override fun toString(): String = "{$pA ~ $pB}"
}

// Settle the bricks to rest on top of z=0 infinite plane.
// The order of bricks is preserved.
fun fallBricks(startBricks: List<Brick>): List<Brick> {
    val liveBricks = startBricks.toMutableList()
    val liveBlocked = startBricks.map { it.points() }.flatten().toMutableSet()

    var changes
    do {
        changes = 0

        for (i in 0 ..< liveBricks.size) {
            val b = liveBricks[i]
            liveBlocked.removeAll(b.points())
            var moveZ = 0
            while (true) {
                val newB = b.move(Direction3.DOWN, moveZ + 1)
                if (min(newB.pA.z, newB.pB.z) <= 0) break   // Hit the floor
                /*if (liveBricks.any {
                        it != b && it.intersect(newB)
                    }) break  // Intersected another brick*/
                if (newB.points().any { liveBlocked.contains(it) }) break

                moveZ++ // The move is good!
            }

            if (moveZ > 0) {
                changes++
                liveBricks[i] = b.move(Direction3.DOWN, moveZ)
            }

            liveBlocked.addAll(liveBricks[i].points())
        }
    } while (changes > 0)

    return liveBricks.toList()
}

// Copy-pasta of original fall function, just checks if a fall CAN happen, don't care how far.
fun canFallBricks(startBricks: List<Brick>): Boolean {
    val liveBlocked = startBricks.map { it.points() }.flatten().toMutableSet()
    for (b in startBricks) {
        val newB = b.move(Direction3.DOWN)

        if (min(newB.pA.z, newB.pB.z) <= 0) continue   // Hit the floor

        liveBlocked.removeAll(b.points())
        val isBlocked = newB.points().any { liveBlocked.contains(it) }
        liveBlocked.addAll(b.points())

        // Moved one: true!
        if (!isBlocked) return true
    }
    return false
}

class Day22 : Exercise {
    private lateinit var bricks: Flow<Brick>

    override val testInput = """
1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9
    """.trimIndent()

    override val testResultPart1 = 5
    override val testResultPart2 = 7

    override suspend fun parse(lines: Flow<String>) {
        val regex = Regex("""(\d+),(\d+),(\d+)~(\d+),(\d+),(\d+)""")
        bricks = lines.map {
            val (x1, y1, z1, x2, y2, z2) = regex.matchEntire(it)!!.destructured
            Brick(Point3(x1.toInt(), y1.toInt(), z1.toInt()), Point3(x2.toInt(), y2.toInt(), z2.toInt()))
        }
    }

    override suspend fun partOne(): Any {
        val timeSource = TimeSource.Monotonic
        var startTime = timeSource.markNow()
        val settledBricks = fallBricks(bricks.toList())
        //println("Settled ${settledBricks.size} bricks in ${startTime.elapsedNow()}")
        //println(settledBricks)
        return settledBricks.count {
            val newBricks = settledBricks.minus(it)
            val startTime = timeSource.markNow()
            val newFall = canFallBricks(newBricks)
            //rintln("Settled new in ${startTime.elapsedNow()}")
            !newFall
        }
    }

    override suspend fun partTwo(): Any {
        val timeSource = TimeSource.Monotonic
        val settledBricks = fallBricks(bricks.toList())
        return settledBricks.map {
            val newBricks = settledBricks.minus(it)
            val startTime = timeSource.markNow()
            val newSettled = fallBricks(newBricks)
            //println("Settled new in ${startTime.elapsedNow()}")

            var moved = 0
            // Compare pointwise
            for (i in 0 ..< newBricks.size) {
                if (newBricks[i] != newSettled[i]) moved++
            }
            moved
        }.sum()
    }
}