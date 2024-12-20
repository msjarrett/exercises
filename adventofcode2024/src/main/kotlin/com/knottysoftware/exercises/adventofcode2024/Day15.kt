package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

/** Returns grid, instructions, starting position. */
private suspend fun parse(
    lines: List<String>,
    remap: Boolean = false
): Triple<MutableGrid<Char>, List<Direction>, Point> {
  val separator = lines.indexOf("")
  val gridLines =
      if (!remap) lines.take(separator)
      else
          lines.take(separator).map {
            it.map {
                  when (it) {
                    '#' -> listOf('#', '#')
                    'O' -> listOf('[', ']')
                    '.' -> listOf('.', '.')
                    '@' -> listOf('@', '.')
                    else -> throw IllegalArgumentException()
                  }
                }
                .flatten()
                .joinToString("")
          }

  val grid = gridFromStrings(gridLines).toMutableGrid()
  var start: Point? = null
  grid.visit { x, y, v -> if (v == '@') start = Point(x, y) }

  check(start != null)
  grid.set('.', start!!.x, start!!.y)

  val instructions =
      lines
          .drop(separator + 1)
          .map {
            it.map { c ->
              when (c) {
                '^' -> Direction.UP
                'v' -> Direction.DOWN
                '<' -> Direction.LEFT
                '>' -> Direction.RIGHT
                else -> throw IllegalArgumentException()
              }
            }
          }
          .flatten()

  return Triple(grid, instructions, start!!)
}

private fun boxCoordinates(grid: Grid<Char>): Long {
  var sum = 0L
  grid.visit { x, y, v ->
    if (v == 'O' || v == '[') {
      sum += (100 * y + x).toLong()
    }
  }
  return sum
}

// Recursively build a list of box moves.
private fun tryMove(
    grid: Grid<Char>,
    box: Point,
    m: Direction,
    moves: MutableList<Point>
): Boolean {
  val addBox = if (grid.at(box) == ']') box.move(Direction.LEFT) else box
  check(grid.at(addBox) == '[')
  check(grid.at(addBox.move(Direction.RIGHT)) == ']')

  if (m == Direction.LEFT || m == Direction.RIGHT) {
    val n = box.move(m, 2)
    if (grid.at(n) == '#') {
      return false
    } else if (grid.at(n) == ']' || grid.at(n) == '[') {
      val recurseOk = tryMove(grid, n, m, moves)
      if (!recurseOk) return false
    }
    // Else it's an open space, ok.
  } else if (m == Direction.UP || m == Direction.DOWN) {
    val n = addBox.move(m)
    if (grid.at(n) == '#') {
      return false
    } else if (grid.at(n) == '[' || grid.at(n) == ']') {
      val recurseOk = tryMove(grid, n, m, moves)
      if (!recurseOk) return false
    }

    // Push an unaligned right box only
    val n2 = addBox.move(Direction.RIGHT).move(m)
    if (grid.at(n2) == '#') {
      return false
    } else if (grid.at(n2) == '[') {
      val recurseOk = tryMove(grid, n2, m, moves)
      if (!recurseOk) return false
    }
  }

  moves.add(addBox)
  return true
}

private fun applyMoves(grid: MutableGrid<Char>, moves: List<Direction>, start: Point) {
  val bCount = grid.count { it == '[' }
  var p = start
  for (m in moves) {
    if (false) {
      val moveChar =
          when (m) {
            Direction.UP -> '^'
            Direction.DOWN -> 'v'
            Direction.LEFT -> '<'
            Direction.RIGHT -> '>'
            else -> '@'
          }
      grid.set(moveChar, p.x, p.y)
      println(grid)
      grid.set('.', p.x, p.y)
      println("")
    }
    check(grid.count { it == '[' } == bCount)

    val c = p.move(m) // There's always walls, we don't have to check bounds.
    if (grid.at(c) == '.') { // Easy move
      p = c
    } else if (grid.at(c) == 'O') { // Shift some boxes
      // Is there a gap?
      var c2 = c
      while (grid.at(c2) == 'O') c2 = c2.move(m)
      if (grid.at(c2) == '.') {
        grid.set('O', c2.x, c2.y)
        grid.set('.', c.x, c.y)
        p = c
      }
    } else if (grid.at(c) == '[' || grid.at(c) == ']') {
      val boxMoves = mutableListOf<Point>()
      if (tryMove(grid, c, m, boxMoves)) {
        // So there's a potential diamond problem when pushing vertically.
        // We filter so that we only push the tip of the diamond once.
        for (b in boxMoves.distinct()) {
          val br = b.move(Direction.RIGHT)

          check(grid.at(b) == '[')
          check(grid.at(br) == ']')

          grid.set('.', b.x, b.y)
          grid.set('.', br.x, br.y)
          grid.set('[', b.move(m).x, b.move(m).y)
          grid.set(']', br.move(m).x, br.move(m).y)
        }
        p = c
      }
    }
  }
}

suspend fun Day15a(lines: Flow<String>): Any {
  val (grid, moves, start) = parse(lines.toList())
  applyMoves(grid, moves, start)
  return boxCoordinates(grid)
}

suspend fun Day15b(lines: Flow<String>): Any {
  val (grid, moves, start) = parse(lines.toList(), remap = true)
  applyMoves(grid, moves, start)
  return boxCoordinates(grid)
}
