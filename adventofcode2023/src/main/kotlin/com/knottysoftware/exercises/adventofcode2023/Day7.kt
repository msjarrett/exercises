package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class Day7 : Exercise {
    class Hand (hand: String) {
        val cards = hand.toCharArray()

        init {
            check(cards.size == 5)
        }

        enum class Type (val rank: Int){
            FIVE_KIND(7),
            FOUR_KIND(6),
            FULL_HOUSE(5),
            THREE_KIND(4),
            TWO_PAIR(3),
            ONE_PAIR(2),
            HIGH_CARD(1),
        }


        fun type(joker: Boolean = false): Type {
            val distinctCounts = cards.distinct().map { dc -> cards.count { c -> c == dc }}.sortedDescending()
            val jokerCount = if (joker) cards.count { it == 'J' } else 0

            return when (distinctCounts.size) {
                1 -> Type.FIVE_KIND
                2 -> {
                    if (jokerCount > 0) Type.FIVE_KIND
                    else if (distinctCounts[0] == 4) Type.FOUR_KIND
                    else Type.FULL_HOUSE
                }
                3 -> {
                    if (jokerCount == 3 || jokerCount == 2 || (jokerCount == 1 && distinctCounts[0] == 3))
                        Type.FOUR_KIND
                    else if (jokerCount == 1) Type.FULL_HOUSE
                    else if (distinctCounts[0] == 3) Type.THREE_KIND
                    else Type.TWO_PAIR
                }
                4 -> {
                    if (jokerCount > 0) Type.THREE_KIND
                    else Type.ONE_PAIR
                }
                5 -> {
                    if (jokerCount > 0) Type.ONE_PAIR
                    else Type.HIGH_CARD
                }
                else -> throw IllegalArgumentException()
            }
        }

        fun cardRank(c: Char, joker: Boolean = false) = when(c) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> if (joker) 1 else 11
            'T' -> 10
            '9' -> 9
            '8' -> 8
            '7' -> 7
            '6' -> 6
            '5' -> 5
            '4' -> 4
            '3' -> 3
            '2' -> 2
            else -> throw IllegalArgumentException("Invalid card $c")
        }

        fun totalScore(joker: Boolean = false) =
            type(joker).rank * 100000000 +
            cardRank(cards[0], joker) * 0x10000 +
            cardRank(cards[1], joker) * 0x1000 +
            cardRank(cards[2], joker) * 0x100 +
            cardRank(cards[3], joker) * 0x10 +
            cardRank(cards[4], joker)

        override fun toString() = cards.joinToString("")
    }

    private lateinit var hands: List<Pair<Hand, Int>>

    override val testInput = """
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
    """.trimIndent()

    override val testResultPart1 = 6440
    override val testResultPart2 = 5905

    override suspend fun parse(lines: Flow<String>) {
        hands = lines.map {
            val parts = it.split(" ")
            Pair(Hand(parts[0]), parts[1].toInt())
        }.toList()
    }

    override suspend fun partOne(): Int {
        val sortedList = hands.sortedWith { a, b ->
            b.first.totalScore() - a.first.totalScore()
        }

        val maxMul = sortedList.size
        return sortedList.mapIndexed {i, handBid ->
            handBid.second * (maxMul - i)
        }.sum()
    }

    override suspend fun partTwo(): Int {
        val sortedList = hands.sortedWith { a, b ->
            b.first.totalScore(true) - a.first.totalScore(true)
        }

        val maxMul = sortedList.size
        return sortedList.mapIndexed {i, handBid ->
            handBid.second * (maxMul - i)
        }.sum()
    }
}