package com.knottysoftware.exercises.adventofcode2022

class Day3 : Exercise {
  @JvmInline
  value class Item(val item: Char) {
    val priority: Int
      get() =
          when (item) {
            in 'a'..'z' -> 1 + item.code - 'a'.code
            in 'A'..'Z' -> 27 + item.code - 'A'.code
            else -> throw IllegalArgumentException()
          }
  }

  // Rucksacks -> Items. Container split done later.
  lateinit var rucksacks: List<List<Item>>

  override fun parse(lines: Sequence<String>) {
    rucksacks = lines.map { it.map { Item(it) }.toList() }.toList()
  }

  override fun partOne() =
      rucksacks
          .map {
            val left = it.subList(0, it.size / 2).distinct()
            val right = it.subList(it.size / 2, it.size).distinct()
            val overlap = left.intersect(right)
            overlap.single().priority
          }
          .sum()

  override fun partTwo() =
      rucksacks
          .chunked(3)
          .map {
            require(it.size == 3)
            var overlap = it.first().toSet()
            for (sack in it) {
              overlap = overlap.intersect(sack)
            }
            overlap.single().priority
          }
          .sum()
}
