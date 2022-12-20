package com.knottysoftware.exercises.adventofcode2022

class Day18 : Exercise {
  companion object {
    final val REGEX = Regex("""(\d+),(\d+),(\d+)""")
  }
  lateinit var points: Set<Point3d>

  override fun parse(lines: Sequence<String>) {
    points =
        lines
            .map {
              val (x, y, z) = REGEX.matchEntire(it)!!.destructured
              Point3d(x.toInt(), y.toInt(), z.toInt())
            }
            .toSet()
  }

  override fun partOne(): Int {
    val minX = points.map { it.x }.minOrNull()!!
    val maxX = points.map { it.x }.maxOrNull()!!
    val minY = points.map { it.y }.minOrNull()!!
    val maxY = points.map { it.y }.maxOrNull()!!
    val minZ = points.map { it.z }.minOrNull()!!
    val maxZ = points.map { it.z }.maxOrNull()!!
    println("X $minX $maxX Y $minY $maxY Z $minZ $maxZ")

    // Try the naive approach with the existing set.
    // 18ms... lol.
    var sideCount = 0
    for (p in points) {
      sideCount += p.neighbors().map() { if (!points.contains(it)) 1 else 0 }.sum()
    }
    return sideCount
  }

  override fun partTwo(): Int {
    // Oooo decisions! Do I find the points of exterior volume or interior volume?
    // I have to map the exterior volume anyways... so lets try it.

    // Use min/max axes to restrict the search space.
    val minX = points.map { it.x }.minOrNull()!!
    val maxX = points.map { it.x }.maxOrNull()!!
    val minY = points.map { it.y }.minOrNull()!!
    val maxY = points.map { it.y }.maxOrNull()!!
    val minZ = points.map { it.z }.minOrNull()!!
    val maxZ = points.map { it.z }.maxOrNull()!!

    val exteriorPoints = mutableSetOf<Point3d>()
    var surfaces = 0

    val start = Point3d(0, 0, 0)
    require(!points.contains(start))
    val queue = SearchQueue(start)
    for (p in queue) {
      // We're outside the search space (one space boundary around the shape extremities).
      if (p.x < (minX - 1) ||
          p.x > (maxX + 1) ||
          p.y < (minY - 1) ||
          p.y > (maxY + 1) ||
          p.z < (minZ - 1) ||
          p.z > (maxZ + 1))
          continue

      // We already visited here earlier in the queue.
      if (exteriorPoints.contains(p)) continue

      // Don't check for points, we already enforce that in enumeration (since we have to
      // count surfaces).

      // New exterior volume. Look for surfaces or more outside space.
      exteriorPoints.add(p)
      for (n in p.neighbors()) {
        if (points.contains(n)) surfaces++
        else {
          queue.addBFS(n)
        }
      }
    }
    return surfaces
  }
}
