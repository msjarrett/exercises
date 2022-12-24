package com.knottysoftware.exercises.adventofcode2022

class Day23 : Exercise {
  lateinit var startElves: Set<Point>

  override fun parse(lines: Sequence<String>) {
    val elves = mutableSetOf<Point>()
    var y = 0
    for (line in lines) {
      var x = 0
      for (ch in line) {
        if (ch == '#') {
          elves.add(Point(x, y))
        }
        x++
      }
      y++
    }
    startElves = elves
  }

  // First of row is direct
  final val clearDirs =
      listOf(
          Direction.UP,
          Direction.UPLEFT,
          Direction.UPRIGHT, // First turn
          Direction.DOWN,
          Direction.DOWNLEFT,
          Direction.DOWNRIGHT, // Second turn
          Direction.LEFT,
          Direction.UPLEFT,
          Direction.DOWNLEFT, // Third turn
          Direction.RIGHT,
          Direction.UPRIGHT,
          Direction.DOWNRIGHT, // Fourth turn
      )

  fun findMove(elves: Set<Point>, cur: Point, round: Int): Point? {
    var modTurn = round % 4
    val isClear = clearDirs.map { if (elves.contains(cur.move(it))) false else true }.toList()

    // If entire surrounding is clear, don't move.
    if (isClear.all { it }) return null

    // Consider possible moves
    repeat(4) {
      if (isClear[3 * modTurn] && isClear[3 * modTurn + 1] && isClear[3 * modTurn + 2])
          return cur.move(clearDirs[3 * modTurn])
      modTurn = (modTurn + 1) % 4
    }

    // Nowhere to go.
    return null
  }

  fun simulate(elves: Set<Point>, round: Int): Pair<Set<Point>, Int> {
    // First half: proposes!
    val proposals = mutableMapOf<Point, Int>()
    for (elf in elves) {
      val move = findMove(elves, elf, round)
      if (move != null) {
        proposals[move] = proposals.getOrDefault(move, 0) + 1
      }
    }

    // Second half: moves!
    var moves = 0
    val newElves = mutableSetOf<Point>()
    for (elf in elves) {
      val move = findMove(elves, elf, round)
      if (move == null || proposals[move]!! > 1) {
        newElves.add(elf)
      } else {
        moves++
        newElves.add(move)
      }
    }

    return newElves to moves
  }

  fun display(elves: Set<Point>) {
    val minX = elves.map { it.x }.minOrNull()!!
    val maxX = elves.map { it.x }.maxOrNull()!!
    val minY = elves.map { it.y }.minOrNull()!!
    val maxY = elves.map { it.y }.maxOrNull()!!

    println("X [$minX, $maxX]  Y [$minY, $maxY]")
    for (y in minY..maxY) {
      for (x in minX..maxX) {
        if (elves.contains(Point(x, y))) print("#") else print(".")
      }
      println("")
    }
    println("")
  }

  override fun partOne(): Int {
    var elves = startElves
    var roundIndex = 0

    repeat(10) {
      val (newElves, _) = simulate(elves, roundIndex++)
      elves = newElves
    }

    // Find bounding box
    val minX = elves.map { it.x }.minOrNull()!!
    val maxX = elves.map { it.x }.maxOrNull()!! + 1
    val minY = elves.map { it.y }.minOrNull()!!
    val maxY = elves.map { it.y }.maxOrNull()!! + 1
    return (maxY - minY) * (maxX - minX) - elves.size
  }

  override fun partTwo(): Int {
    var elves = startElves
    var roundIndex = 0

    while (true) {
      val (newElves, moves) = simulate(elves, roundIndex++)
      elves = newElves
      if (moves == 0) break
      if (roundIndex % 20 == 0) println("Round $roundIndex moves $moves")
    }
    return roundIndex // Zero-indexed, but we post-incremented
  }
}
