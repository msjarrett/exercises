package com.knottysoftware.exercises.adventofcode2022;

class Day2 : Exercise {
    private enum class Result { WIN, LOSE, DRAW }
    private enum class Move { ROCK, PAPER, SCISSORS }

    private fun winner(move: Move, response: Move) =
        when (move) {
            Move.ROCK -> when (response) {
                Move.SCISSORS -> Result.LOSE
                Move.ROCK -> Result.DRAW
                Move.PAPER -> Result.WIN
            }
            Move.PAPER -> when (response) {
                Move.ROCK -> Result.LOSE
                Move.PAPER -> Result.DRAW
                Move.SCISSORS -> Result.WIN
            }
            Move.SCISSORS -> when (response) {
                Move.PAPER -> Result.LOSE
                Move.SCISSORS -> Result.DRAW
                Move.ROCK -> Result.WIN
            }
        }

    private fun moveForChar(move: Char) = when (move) {
        'A' -> Move.ROCK
        'B' -> Move.PAPER
        'C' -> Move.SCISSORS
        'X' -> Move.ROCK
        'Y' -> Move.PAPER
        'Z' -> Move.SCISSORS
        else -> throw IllegalArgumentException()   
    }

    private fun resultForChar(result: Char) = when (result) {
        'X' -> Result.LOSE
        'Y' -> Result.DRAW
        'Z' -> Result.WIN
        else -> throw IllegalArgumentException()
    }

    // The score for a single round is the score for the shape you selected (1 for Rock, 2 for Paper, and 3
    // for Scissors) plus the score for the outcome of the round (0 if you lost, 3 if the round was a draw,
    // and 6 if you won).
    private fun score(move: Move, response: Move) =
        when (response) {
            Move.ROCK -> 1
            Move.PAPER -> 2
            Move.SCISSORS -> 3
        } + when (winner(move, response)) {
            Result.LOSE -> 0
            Result.DRAW -> 3
            Result.WIN -> 6
        }


    private lateinit var strategies: List<Pair<Char, Char>>

    override fun parse(lines: Sequence<String>) {
        strategies = lines.map { Pair(it[0], it[2]) }.toList()
    }

    override fun partOne() = strategies
        .map { Pair(moveForChar(it.first), moveForChar(it.second)) }
        .map { score(it.first, it.second) }
        .sum()

    override fun partTwo() = strategies
        .map { Pair(moveForChar(it.first), resultForChar(it.second)) }
        .map {
            Pair(it.first, when(it.second) {
                Result.LOSE -> when (it.first) {
                    Move.ROCK -> Move.SCISSORS
                    Move.PAPER -> Move.ROCK
                    Move.SCISSORS -> Move.PAPER
                }
                Result.DRAW -> it.first
                Result.WIN -> when (it.first) {
                    Move.ROCK -> Move.PAPER
                    Move.PAPER -> Move.SCISSORS
                    Move.SCISSORS -> Move.ROCK
                }
            })
        }.map { score(it.first, it.second) }.sum()
}