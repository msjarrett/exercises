package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

data class ClawMachine(val prize: Point, val dA: Point, val dB: Point)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
suspend fun parseClawMachinesEx(lines: Flow<String>): List<ClawMachine> {
  val buttonRegex = Regex("""Button (?:A|B): X\+(\d+), Y\+(\d+)""")
  val prizeRegex = Regex("""Prize: X\=(\d+), Y=(\d+)""")

  return lines
      .chunked(4)
      .map {
        val matchA = buttonRegex.matchEntire(it[0])!!
        val matchB = buttonRegex.matchEntire(it[1])!!
        val matchPrize = prizeRegex.matchEntire(it[2])!!

        ClawMachine(
            Point(matchPrize.groupValues[1].toInt(), matchPrize.groupValues[2].toInt()),
            Point(matchA.groupValues[1].toInt(), matchA.groupValues[2].toInt()),
            Point(matchB.groupValues[1].toInt(), matchB.groupValues[2].toInt()),
        )
      }
      .toList()
}

// Return 0 if machine can't win
fun ClawMachine.minWin(): Int {
  var aPress = 0

  var dx = prize.x
  var dy = prize.y

  // We can push in any order, so we push A the minimum number of times to get a multiple of B.
  while (aPress < 100 && dx > 0 && dy > 0) {
    aPress++
    dx -= dA.x
    dy -= dA.y

    if (dx % dB.x == 0 && dy % dB.y == 0 && (dx / dB.x == dy / dB.y)) {
      val bPress = dx / dB.x
      println("$this A=$aPress B=$bPress")
      return aPress * 3 + bPress
    }
  }

  println("$this No solution")
  return 0
}

// Return 0 if machine can't win
fun ClawMachine.minWinBig(): Long {
  // Well duh. It's a 2-variable linear system. There is EXACTLY one solution.
  val Px = 10000000000000L + prize.x
  val Py = 10000000000000L + prize.y
  val dAx = dA.x.toLong()
  val dAy = dA.y.toLong()
  val dBx = dB.x.toLong()
  val dBy = dB.y.toLong()

  // If there are any intermediate divisions, even a double won't have enough precision.
  val aPresses = (Px * dBy - Py * dBx) / (dAx * dBy - dAy * dBx)

  val remX = Px - dAx * aPresses
  val remY = Py - dAy * aPresses

  val bPresses = remX / dBx

  if (remY == bPresses * dBy) {
    check(aPresses * dAx + bPresses * dBx == Px)
    check(aPresses * dAy + bPresses * dBy == Py)
    // println("$this A = $aPresses B = $bPresses")
    return aPresses * 3 + bPresses
  }

  // println("$this No solution")
  return 0
}

suspend fun Day13a(lines: Flow<String>): Any {
  val machines = parseClawMachinesEx(lines)

  return machines.map { it.minWin() }.sum()
}

suspend fun Day13b(lines: Flow<String>): Any {
  val machines = parseClawMachinesEx(lines)

  return machines.map { it.minWinBig() }.sum()
}
