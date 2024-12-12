package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

// First = ID, or -1 for free. Second = length.
@JvmInline
value class Disk(val ranges: List<Pair<Int, Int>>) {
  override fun toString(): String {
    val s = StringBuilder()
    for (r in ranges.take(30)) {
      val c = if (r.first == -1) '.' else if (r.first > 9) 'X' else ((r.first + '0'.code).toChar())
      repeat(r.second) { s.append(c) }
    }
    return s.toString()
  }

  fun defragBlock(): Disk {
    val newRanges = mutableListOf<Pair<Int, Int>>()
    var left = 0
    var right = ranges.size - 1

    var leftConsumed = 0
    var rightConsumed = 0
    var freed = 0

    while (left < right) {
      val leftRange = ranges[left]
      val rightRange = ranges[right]

      if (leftRange.second == 0) {
        left++
      } else if (rightRange.second == 0) {
        right--
      } else if (leftRange.first != -1) {
        // Range on the left is use, just add.
        newRanges.add(leftRange)
        left++
      } else {
        if (ranges[right].first == -1) {
          // Right range is free, skip
          check(rightConsumed == 0)
          right--
          freed += ranges[right].second
        } else {
          // Left is free, right is used. Defrag.
          var leftRemains = leftRange.second - leftConsumed
          var rightRemains = rightRange.second - rightConsumed

          check(leftRemains != 0)
          check(rightRemains != 0)
          if (leftRemains == rightRemains) {
            newRanges.add(Pair(rightRange.first, leftRemains))
            leftConsumed = 0
            rightConsumed = 0
            left++
            right--
          } else if (leftRemains > rightRemains) {
            newRanges.add(Pair(rightRange.first, rightRemains))
            leftConsumed += rightRemains
            rightConsumed = 0
            right--
          } else if (rightRemains > leftRemains) {
            newRanges.add(Pair(rightRange.first, leftRemains))
            rightConsumed += leftRemains
            leftConsumed = 0
            left++

            // We're about to abort, but right has some remaining.
            if (left >= right) {
              freed += leftRemains // Hack, the freed below will miss it.
              newRanges.add(Pair(rightRange.first, rightRemains - leftRemains))
            }
          }
        }
      }
    }
    newRanges.add(Pair(-1, freed))
    return Disk(newRanges)
  }

  fun defragFile(): Disk {
    // We assume files are contiguous.
    val newRanges = ranges.toMutableList()

    // I don't want to test a disk with space at the front or back.
    check(newRanges.first().first != -1)
    check(newRanges.last().first != -1)

    var curId = newRanges.last().first
    var right = ranges.size - 1
    while (right > 0) {
      val rRight = newRanges[right]
      if (rRight.first == curId) {
        // Find a new left range.
        var left = 1
        while (left < right) {
          val rLeft = newRanges[left]
          if (rLeft.first == -1 && rLeft.second >= rRight.second) {
            newRanges[right] = Pair(-1, rRight.second)
            newRanges.add(left, rRight)
            newRanges[left + 1] = Pair(-1, rLeft.second - rRight.second)
            right++ // to undo the new element
            break
          }
          left++
        }
        curId--
        // println(Disk(newRanges))
      }
      right--
    }

    return Disk(newRanges.filter { it.second > 0 })
  }

  fun checksum(): Long {
    var checksum = 0L
    var block = 0
    for (r in ranges) {
      repeat(r.second) {
        if (r.first != -1) checksum += r.first * block
        block++
      }
    }
    return checksum
  }
}

fun diskFromEncoded(encoded: String): Disk {
  val nums = encoded.map { it.digitToInt() }

  return Disk(
      buildList {
        // List size could be even or odd.
        for (i in 0..<nums.size) {
          if (i % 2 == 0) add(Pair(i / 2, nums[i])) else add(Pair(-1, nums[i]))
        }
      })
}

suspend fun Day9a(lines: Flow<String>): Any {
  val disk = diskFromEncoded(lines.single())
  // println(disk)
  val defrag = disk.defragBlock()
  // println(defrag)
  return defrag.checksum()
}

suspend fun Day9b(lines: Flow<String>): Any {
  val disk = diskFromEncoded(lines.single())
  val defrag = disk.defragFile()
  return defrag.checksum()
}
