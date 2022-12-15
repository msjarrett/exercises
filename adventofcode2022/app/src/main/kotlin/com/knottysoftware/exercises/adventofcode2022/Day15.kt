package com.knottysoftware.exercises.adventofcode2022

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day15 : Exercise {
  private lateinit var sensors: List<Pair<Point, Point>>
  private final val REGEX =
      Regex("""Sensor at x=([0-9-]+), y=([0-9-]+): closest beacon is at x=([0-9-]+), y=([0-9-]+)""")

  override fun parse(lines: Sequence<String>) {
    sensors =
        lines
            .map {
              val (sx, sy, bx, by) = REGEX.matchEntire(it)!!.destructured
              Pair(Point(sx.toInt(), sy.toInt()), Point(bx.toInt(), by.toInt()))
            }
            .toList()
  }

  override fun partOne(): Int {
    val INTEREST_ROW = 2000000
    // Beacons are excluded from the count (but sensors are not), so we shouldn't scan them.
    val beaconRow = sensors.filter { it.second.y == INTEREST_ROW }.map { it.second.y }.toSet()
    // X coordinates of scans on the interest row.
    val scanRow = mutableSetOf<Int>()

    for (sensor in sensors) {
      val distance = sensor.first.manhattanDistanceTo(sensor.second)
      val spread = distance - abs(sensor.first.y - INTEREST_ROW)
      if (spread >= 0) {
        for (x in (sensor.first.x - spread)..(sensor.first.x + spread)) {
          if (!beaconRow.contains(x)) scanRow.add(x)
        }
      }
    }
    return scanRow.size
  }

  override fun partTwo(): Long {
    val maxSize = 4000000

    // 4M^2, that's 16T; too much to track.
    // Track spans over 4M rows.
    // There's only 36 sensors, so that's at most 36 spans per row.
    println("Allocating")
    val rowSpans = Array<MutableList<IntRange>>(maxSize + 1) { mutableListOf<IntRange>() }

    // Scan the space
    println("Scanning")
    for (sensor in sensors) {
      // The sensor and beacon will scan themselves.
      val c = sensor.first
      val distance = c.manhattanDistanceTo(sensor.second)

      // Check bounds. All the sensors are inbounds... but whatever
      if (c.x - distance > maxSize ||
          c.y - distance > maxSize ||
          c.x + distance < 0 ||
          c.y + distance < 0)
          continue

      // println("Included sensor $c $distance")

      // Scan.
      for (i in (-distance)..distance) {
        val spread = distance - abs(i)
        val y = c.y + i
        if (y >= 0 && y <= maxSize)
            rowSpans[c.y + i].add((max(0, c.x - spread))..(min(maxSize, c.x + spread)))
      }
    }

    // Find the only missing point.
    // We will merge the ranges. If any range doesn't merge to full span, that's the row.
    println("Searching")
    for (y in 0..maxSize) {
      if (y % (maxSize / 10) == 0) println("Row $y")

      // TODO: a reduce would be lovely here, but misses at least once. Does runningFold help?
      val merged = rowSpans[y].sortedBy { it.start }
      var x = -1
      if (merged.first().start != 0) x = 0
      else {
        var biggest = merged[0].endInclusive
        for (i in 1 until merged.size) {
          if (biggest + 1 < merged[i].start) {
            x = merged[i].start - 1
            break
          }
          biggest = max(biggest, merged[i].endInclusive)
        }
        if (x == -1 && biggest != maxSize) x = maxSize
      }
      if (x != -1) {
        println("FOUND $x,$y")
        return x.toLong() * 4000000 + y
      }
    }

    throw IllegalArgumentException()
  }
}
