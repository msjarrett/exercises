package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlin.math.max
import kotlin.math.min

fun vectorArea(vectors: Set<Pair<Point, Point>>): Long {
    val distinctY = vectors.map { listOf(it.first.y, it.second.y) }.flatten().distinct().sorted()

    // All the vertical lines, in x order, with the lowest y point on top.
    val verticalVectors = vectors.filter { it.first.x == it.second.x }. map {
        if (it.first.y > it.second.y)
            Pair(it.second, it.first)
        else
            it
    }.sortedBy { it.first.x }

    // Find the total area, including boundaries.
    var totalArea = 0L
    for (i in 0 ..< (distinctY.size - 1)) {
        val y = distinctY[i]

        val xSegments = verticalVectors.filter {
            // We don't care about bottom points, as they are lines going up. They won't contribute to the
            // cross-section from now on.
            y >= it.first.y && y < it.second.y
        }.map { it.first.x }
        check((xSegments.size % 2) == 0)

        var xSection = 0L
        for (j in 0 ..< xSegments.size step 2) {
            xSection += xSegments[j + 1] - xSegments[j] + 1 // Include both vertical edges
        }

        // Excludes  the first row of the next range.
        // Add one last row for the final row (which are all bottom edges.
        val yRange = (distinctY[i + 1] - y).toLong() + if (i == (distinctY.size - 2)) 1 else 0
        totalArea += xSection * yRange

        // Is there a line segment we missed?
        totalArea += vectors
            .filter { it.first.y == y && it.second.y == y }
            .map {
                // Is this contained in any cross section?
                val x1 = min(it.first.x, it.second.x)
                val x2 = max(it.first.x, it.second.x)
                var isIncluded = false
                for (j in 0 ..< xSegments.size step 2) {
                    if (x1 >= xSegments[j] &&  x2 <= xSegments[j + 1]) isIncluded = true
                }
                if (isIncluded)
                    0
                else {
                    var len = x2 - x1 + 1
                    for (j in 0 ..< xSegments.size step 2) {
                        if (x2 == xSegments[j]) len--
                    }
                    for (j in 1 ..< xSegments.size step 2) {
                        if (x1 == xSegments[j]) len--
                    }
                    len
                }
            }.sum()
    }

    return totalArea
}

class Day18 : Exercise {
    private lateinit var insts: Flow<Triple<Direction, Int, String>>

    override val testInput = """
R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
    """.trimIndent()

    override val testResultPart1 = 62
    override val testResultPart2 = 952408144115L  // 952,408,144,115

    override suspend fun parse(lines: Flow<String>) {
        val lineRegex = Regex("""([LURD]) (\d+) \(#([0-9a-f]{6})\)""")
        insts = lines.map {
            val (dir, count, color) = lineRegex.matchEntire(it)!!.destructured
            Triple(
                when (dir) {
                    "U" -> Direction.UP
                    "D" -> Direction.DOWN
                    "L" -> Direction.LEFT
                    "R" -> Direction.RIGHT
                    else -> throw IllegalArgumentException()
                },
                count.toInt(),
                color
            )
        }
    }

    override suspend fun partOne(): Int {
        // Find the border
        val border = mutableMapOf<Point, String>()
        val interior = mutableSetOf<Point>()
        var curPos = Point(0,0)
        for (i in insts.toList()) {
            repeat (i.second) {
                curPos = curPos.move(i.first)
                check(!border.containsKey(curPos))
                border[curPos] = i.third
            }
        }
        check(curPos == Point(0,0))

        // Find the interior
        check(!border.containsKey(Point(1, 1)))
        val queue = SearchQueue(Point(1, 1))
        for (p in queue) {
            if (border.containsKey(p)) continue
            if (interior.contains(p)) continue

            interior.add(p)
            for (d in Direction.cardinalDirections) {
                queue.addBFS(p.move(d))
            }
        }

        return border.size + interior.size
    }

    override suspend fun partTwo(): Long {
        // Find the border
        val vectors = mutableSetOf<Pair<Point, Point>>()
        var curPos = Point(0,0)
        for (i in insts.toList().map { it.third }) {
            val dist = i.substring(0, 5).toInt(16)
            val dir = when (i.substring(5)) {
                "0" -> Direction.RIGHT
                "1" -> Direction.DOWN
                "2" -> Direction.LEFT
                "3" -> Direction.UP
                else -> throw IllegalArgumentException()
            }

            // No, points don't overflow Int bounds (we checked)
            val newPos = curPos.move(dir, dist)
            vectors.add(Pair(curPos, newPos))
            curPos = newPos
        }
        check(curPos == Point(0,0))

        // Find the interior.
        // Test area was 952,408,144,115 / 1,407,374,123,584
        // Input area is 232,802,571,753,752 = 232 trillion!!
        // However, the final input only has DistinctX 262  DistinctY 265.
        // So we're going to try and do cross-sections to add up big areas.
        return vectorArea(vectors)
    }
}