package com.knottysoftware.exercises.adventofcode2022

import kotlin.ranges.IntRange

class Day4 : Exercise {
  lateinit var elfPairs: Sequence<Pair<IntRange, IntRange>>

  override fun needReparse() = true

  override fun parse(lines: Sequence<String>) {
    fun rangeFromStr(str: String): IntRange {
      val ends = str.split('-')
      require(ends.size == 2)
      return ends[0].toInt()..ends[1].toInt()
    }

    elfPairs =
        lines.map {
          val elves = it.split(',')
          require(elves.size == 2)
          Pair(rangeFromStr(elves[0]), rangeFromStr(elves[1]))
        }
  }

  override fun partOne() =
      elfPairs
          .filter {
            (it.first.contains(it.second.start) && it.first.contains(it.second.endInclusive)) ||
                (it.second.contains(it.first.start) && it.second.contains(it.first.endInclusive))
          }
          .count()

  override fun partTwo() =
      elfPairs
          .filter {
            (it.first.contains(it.second.start) || it.first.contains(it.second.endInclusive)) ||
                (it.second.contains(it.first.start) || it.second.contains(it.first.endInclusive))
          }
          .count()
}
