package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlin.math.max

/** Sample to test the 2023 exercise infrastructure. */
class Day2 : Exercise {
    val redRegex = Regex("""(\d+) red""")
    val greenRegex = Regex("""(\d+) green""")
    val blueRegex = Regex("""(\d+) blue""")

    data class Game (val num: Int, val pulls: List<Pull>) {
    }

    data class Pull (
        val red: Int,
        val green: Int,
        val blue: Int
    )

    fun parseGame(s: String): Game {
        val colonSplit = s.split(':')

        return Game(
            colonSplit[0].substring(5).toInt(),
            colonSplit[1].split(';').map {
                var red = 0
                var green = 0
                var blue = 0

                redRegex.find(it)?.let { red = it.groups[1]!!.value.toInt() }
                greenRegex.find(it)?.let { green = it.groups[1]!!.value.toInt() }
                blueRegex.find(it)?.let { blue = it.groups[1]!!.value.toInt() }

                Pull(red, green, blue)
            }
        )
    }

    private lateinit var games: Flow<Game>

    override val testInput = """
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()

    override val testResultPart1 = 8
    override val testResultPart2 = 2286

    override suspend fun parse(lines: Flow<String>) {
        games = lines.map (this::parseGame)
    }

    override suspend fun partOne(): Any {
        return games.filter {
            it.pulls.all {
                it.red <= 12 && it.green <= 13 && it.blue <= 14
            }
        }.map { it.num }.toList().sum()
    }

    override suspend fun partTwo(): Any {
        return games.map {
            // Find the minimum cubes.
            it.pulls.reduce { prev, cur ->
                Pull(max(prev.red, cur.red), max(prev.green, cur.green), max(prev.blue, cur.blue))
            }
        }.map {
            // Turn a pull into a power
            it.red * it.green * it.blue
        }.toList().sum()
    }
}