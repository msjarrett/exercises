package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day16 : Exercise {
    data class Beam (val pos: Point, val dir: Direction) {
        fun reflect(newDir: Direction) = Beam(pos.move(newDir), newDir)
        fun proceed() = reflect(dir)
    }

    private lateinit var grid: Grid<Char>

    override val testInput = """
.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....
    """.trimIndent()

    override val testResultPart1 = 46
    override val testResultPart2 = 51

    override suspend fun parse(lines: Flow<String>) {
        grid = gridFromStrings(lines.toList())
    }

    fun beamCoverage(start: Beam): Int {
        val visited = mutableSetOf<Beam>()

        var beams = listOf(start)
        while (!beams.isEmpty())
        {
            val newBeams = mutableListOf<Beam>()
            for (beam in beams) {
                // Went off the grid.
                if (!grid.contains(beam.pos)) continue

                // We already found a beam in this spot and direction.
                if (visited.contains(beam)) continue

                // Track the new beam.
                visited.add(beam)

                // Add the new set of beams.
                when (grid.at(beam.pos)) {
                    '.' -> newBeams.add(beam.proceed())
                    '-' -> {
                        if (beam.dir == Direction.LEFT || beam.dir == Direction.RIGHT) {
                            newBeams.add(beam.proceed())
                        } else {
                            newBeams.add(beam.reflect(Direction.LEFT))
                            newBeams.add(beam.reflect(Direction.RIGHT))
                        }
                    }

                    '|' -> {
                        if (beam.dir == Direction.UP || beam.dir == Direction.DOWN) {
                            newBeams.add(beam.proceed())
                        } else {
                            newBeams.add(beam.reflect(Direction.UP))
                            newBeams.add(beam.reflect(Direction.DOWN))
                        }
                    }

                    '/' -> {
                        val newDir = when (beam.dir) {
                            Direction.UP -> Direction.RIGHT
                            Direction.DOWN -> Direction.LEFT
                            Direction.LEFT -> Direction.DOWN
                            Direction.RIGHT -> Direction.UP
                            else -> throw IllegalArgumentException()
                        }
                        newBeams.add(beam.reflect(newDir))
                    }

                    '\\' -> {
                        val newDir = when (beam.dir) {
                            Direction.UP -> Direction.LEFT
                            Direction.DOWN -> Direction.RIGHT
                            Direction.LEFT -> Direction.UP
                            Direction.RIGHT -> Direction.DOWN
                            else -> throw IllegalArgumentException()
                        }
                        newBeams.add(beam.reflect(newDir))
                    }
                }
            }
            beams = newBeams
        }

        return visited.map { it.pos }.toSet().count()
    }

    override suspend fun partOne() = beamCoverage(Beam(Point(0,0), Direction.RIGHT))

    override suspend fun partTwo(): Any {
        return ((0..grid.maxX).map { Beam(Point(it, 0), Direction.DOWN)} +
                (0..grid.maxX).map { Beam(Point(it, grid.maxY), Direction.UP)} +
                (0..grid.maxY).map { Beam(Point(0, it), Direction.RIGHT)} +
                (0..grid.maxY).map { Beam(Point(grid.maxX, it), Direction.LEFT)})
            .map { beamCoverage(it) }.max()
    }
}