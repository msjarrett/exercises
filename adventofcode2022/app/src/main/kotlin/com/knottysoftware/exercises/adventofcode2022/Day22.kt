package com.knottysoftware.exercises.adventofcode2022

/** Dummy exercise to validate runExercise logic. */
class Day22 : Exercise {
  enum class Cell {
    VOID,
    OPEN,
    WALL
  }
  enum class Turn {
    NONE,
    LEFT,
    RIGHT
  }

  lateinit var map: List<List<Cell>>
  lateinit var moves: List<Pair<Turn, Int>>

  // How to do the cubemaps?
  // My first idea was List<x: IntRange, y: IntRange, dir: Direction, map: (Point) -> Point, newDir:
  // Direction>
  // ... but then I was like, let's just make a map of points! It's not THAT big.
  // Map<Pair<Point, Direction>, Pair<Point, Direction>>
  final val cubeMapSample =
      buildMap<Pair<Point, Direction>, Pair<Point, Direction>> {
        // Entering from p1(d2), exiting to p2(d2). We'll reverse the directions.
        fun putEdge(p1: Point, d1: Direction, p2: Point, d2: Direction) {
          put(p1 to d1, p2 to d2)
          put(p2 to d2.turnReverse(), p1 to d1.turnReverse())
        }

        for (i in 0..3) {
          // 1 up <-> 2
          putEdge(Point(8 + i, 0), Direction.UP, Point(3 - i, 4), Direction.DOWN)

          // 1 left <-> 3
          putEdge(Point(8, i), Direction.LEFT, Point(4 + i, 4), Direction.DOWN)

          // 1 right <-> 6
          putEdge(Point(11, i), Direction.RIGHT, Point(15, 11 - i), Direction.LEFT)

          // 2 left <-> 6
          putEdge(Point(0, 4 + i), Direction.LEFT, Point(15 - i, 11), Direction.UP)

          // 2 down <-> 5
          putEdge(Point(i, 7), Direction.DOWN, Point(11 - i, 11), Direction.UP)

          // 3 down <-> 5
          putEdge(Point(4 + i, 7), Direction.DOWN, Point(8, 11 - i), Direction.LEFT)

          // 4 right <-> 6
          putEdge(Point(11, 4 + i), Direction.RIGHT, Point(15 - i, 8), Direction.DOWN)
        }
      }

  final val cubeMapInput =
      buildMap<Pair<Point, Direction>, Pair<Point, Direction>> {
        fun putEdge(p1: Point, d1: Direction, p2: Point, d2: Direction) {
          put(p1 to d1, p2 to d2)
          put(p2 to d2.turnReverse(), p1 to d1.turnReverse())
        }

        //          [ 1 FR ] [ 2 RT ]
        //          [ 3 BT ]
        // [ 5 LT ] [ 4 BA ]
        // [ 6 TO ]

        for (i in 0..49) {
          // 1 up <-> 6
          putEdge(Point(50 + i, 0), Direction.UP, Point(0, 150 + i), Direction.RIGHT)

          // 1 left <-> 5
          putEdge(Point(50, i), Direction.LEFT, Point(0, 149 - i), Direction.RIGHT)

          // 2 up <-> 6
          putEdge(Point(100 + i, 0), Direction.UP, Point(i, 199), Direction.UP)

          // 2 right <-> 4
          putEdge(Point(149, i), Direction.RIGHT, Point(99, 149 - i), Direction.LEFT)

          // 2 down <-> 3
          putEdge(Point(100 + i, 49), Direction.DOWN, Point(99, 50 + i), Direction.LEFT)

          // 3 left <-> 5
          putEdge(Point(50, 50 + i), Direction.LEFT, Point(i, 100), Direction.DOWN)

          // 4 down <-> 6
          putEdge(Point(50 + i, 149), Direction.DOWN, Point(49, 150 + i), Direction.LEFT)
        }
      }

  override fun parse(lines: Sequence<String>) {
    // Can't double-consume the Sequence, so make a list.
    val linesList = lines.toList()
    map =
        linesList
            .takeWhile { it != "" }
            .map {
              it.map {
                when (it) {
                  ' ' -> Cell.VOID
                  '.' -> Cell.OPEN
                  '#' -> Cell.WALL
                  else -> throw IllegalArgumentException()
                }
              }
            }

    val moveLine = linesList.last()
    val moves = mutableListOf<Pair<Turn, Int>>()
    var lastTurn = Turn.NONE
    var lastStart = 0
    for (i in 0 until moveLine.length) {
      if (moveLine[i] == 'L' || moveLine[i] == 'R') {
        moves.add(Pair(lastTurn, moveLine.substring(lastStart, i).toInt()))
        lastStart = i + 1
        lastTurn =
            when (moveLine[i]) {
              'L' -> Turn.LEFT
              'R' -> Turn.RIGHT
              else -> throw IllegalArgumentException()
            }
      }
    }

    // Finish last move.
    moves.add(Pair(lastTurn, moveLine.substring(lastStart).toInt()))
    this.moves = moves
  }

  fun wrapFlat(pos: Point, dir: Direction): Pair<Point, Direction> {
    var x = pos.x
    var y = pos.y
    when (dir) {
      Direction.LEFT -> x = map[y].indexOfLast { it != Cell.VOID }
      Direction.RIGHT -> x = map[y].indexOfFirst { it != Cell.VOID }
      Direction.UP -> y = map.indexOfLast { it.size > x && it[x] != Cell.VOID }
      Direction.DOWN -> y = map.indexOfFirst { it.size > x && it[x] != Cell.VOID }
      else -> throw IllegalArgumentException()
    }

    check(x != -1)
    check(y != -1)
    return Point(x, y) to dir
  }

  fun wrapCube(pos: Point, dir: Direction): Pair<Point, Direction> {
    val cubeMap = if (map.size == 12) cubeMapSample else cubeMapInput
    return cubeMap[pos to dir]!!
  }

  // Traverse and calculate a score.
  fun traverse(wrap: (Point, Direction) -> Pair<Point, Direction>): Int {
    // Starting location is leftmost open cell on first row.
    var pos = Point(map[0].indexOf(Cell.OPEN), 0)
    var dir = Direction.RIGHT

    for ((turn, steps) in moves) {
      dir =
          when (turn) {
            Turn.LEFT -> dir.turnLeft()
            Turn.RIGHT -> dir.turnRight()
            Turn.NONE -> dir
          }

      repeat(steps) {
        check(map[pos.y][pos.x] == Cell.OPEN)
        var newPos = pos.move(dir)
        var newDir = dir
        if (newPos.x < 0 ||
            newPos.y < 0 ||
            newPos.y >= map.size ||
            newPos.x >= map[newPos.y].size ||
            map[newPos.y][newPos.x] == Cell.VOID) {
          val res = wrap(pos, dir)
          newPos = res.first
          newDir = res.second
        }

        // We can't break out of a repeat. Just smash our head into the wall repeatedly.
        if (map[newPos.y][newPos.x] != Cell.WALL) {
          pos = newPos
          dir = newDir
        }
      }
    }

    println("$pos $dir")
    return 1000 * (pos.y + 1) +
        4 * (pos.x + 1) +
        when (dir) {
          Direction.RIGHT -> 0
          Direction.DOWN -> 1
          Direction.LEFT -> 2
          Direction.UP -> 3
          else -> throw IllegalArgumentException()
        }
  }

  override fun partOne(): Int {
    return traverse(::wrapFlat)
  }

  override fun partTwo(): Int {
    return traverse(::wrapCube)
  }
}
