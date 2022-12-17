package com.knottysoftware.exercises.adventofcode2022

class Day17 : Exercise {
  companion object {
    const val WIDTH = 7
    final val SHAPES = listOf(
      listOf( Point(2,0), Point(3,0), Point(4,0), Point(5,0)),  // ---
      listOf( Point(3,0), Point(2,1), Point(3,1), Point(4,1), Point(3,2)),  // +
      listOf( Point(4,2), Point(4,1), Point(4,0), Point(3,0), Point(2,0)),  // Backwards L
      listOf( Point(2,0), Point(2,1), Point(2,2), Point(2,3)),  // |
      listOf( Point(2,0), Point(2,1), Point(3,0), Point(3,1)),  // o
    )
  }


  private lateinit var blows: List<Direction>

  // A shape.
  // y is inverted; shapes fall up, so 0,0 is the bottom-left edge and all points are +ve.
  private class Shape(private val points: List<Point>, yStart: Int) {
    private var offset = Point(0, yStart)

    fun points() = points.map { Point(it.x + offset.x, it.y + offset.y) }
    fun testFor(dir: Direction, test: (p: Point) -> Boolean) = points.map { it.move(dir) }.map { Point(it.x + offset.x, it.y + offset.y)}.all { test(it) }

    fun move(dir: Direction) { offset = offset.move(dir) }
  }

  override fun parse(lines: Sequence<String>) {
    blows = lines.single().map { when(it) {
      '>' -> Direction.RIGHT
      '<' -> Direction.LEFT
      else -> throw IllegalArgumentException()
    }}
  }

  inner class State(val space: MutableList<BooleanArray> = mutableListOf<BooleanArray>(), var iShape: Int = 0, var iBlow: Int = 0) {
    val size: Int get() = space.size
    var dropped = 0

    fun run(count: Int) {
      fun isValidPoint(p: Point): Boolean {
        if (p.x < 0) return false
        if (p.x >= WIDTH) return false
        if (p.y < 0) return false
        if (space.size > p.y && space[p.y][p.x]) return false
        return true
      }

      repeat (count) {
        dropped++

        // Last occupied row is space.size - 1.
        val shape = Shape(SHAPES[iShape++], space.size + 3)
        iShape %= SHAPES.size

        while (true) {
          // Blow by jet
          val blow = blows[iBlow++]
          iBlow %= blows.size
          if (shape.testFor(blow) { isValidPoint(it)}) shape.move(blow)

          // Drop or settle
          if (shape.testFor(Direction.UP) { isValidPoint(it) }) {
            shape.move(Direction.UP)
          } else {
            for (p in shape.points()) {
              while (space.size <= p.y) space.add(BooleanArray(WIDTH))
              space[p.y][p.x] = true
            }
            break
          }
        }
      }
    }

    fun print(rows: Int = space.size) {
      println("")
      for (i in (size-1) downTo (size - rows)) {
        print("|")
        for (s in space[i]) {
          if (s) print("#") else print(" ")
        }
        println("|")
      }
      println("|-------|")
    }
  }

  override fun partOne(): Int {
    val state = State()
    state.run(2022)
    return state.size
  }

  fun simulate1() {
    val state = State()
    
    var lastBlowIndex = 0;
    repeat (20) {
      while (true) {
        state.run(SHAPES.size)

        if (state.iBlow < lastBlowIndex) {
          lastBlowIndex = state.iBlow
          break
        }
        lastBlowIndex = state.iBlow
      }
      println("iBlow wrapped: value = $lastBlowIndex  rocks = ${state.dropped}")
    }
    
  }

  fun simulate2() {
    val state = State()
    
    var lastHeight = 0
    repeat(20) {
      val blockSize = 1755
      state.run(blockSize)
      val curHeight = state.size - lastHeight
      lastHeight = state.size
      println("blockSize $blockSize height + $curHeight")
    }
  }

  override fun partTwo(): Long {
    // Test data: 1 000 000 000 000 rocks. -> 1 514 285 714 288 rows.
    println ("BLOWS = ${blows.size}  SHAPES = ${SHAPES.size}")
    //simulate1()
    //return 0

    val state = State()
    val blockSize = 1725

    println("Simulating first ten blocks.")

    // We assume there's a repeating pattern after the first block, but the first block could be larger.
    state.run(6895)
    println("BLOCK 1")
    state.print(10)
    val firstBlockHeight = state.size
    
    state.run(blockSize)
    println("BLOCK 2")
    state.print(10)
    val secondBlockHeight = state.size - firstBlockHeight

    // Simulate 8 more to confirm a valid block size.
    var lastHeight = state.size
    repeat (8) {
      state.run(blockSize)
      val nextSize = state.size - lastHeight
      lastHeight = state.size
      println("BLOCK size = $nextSize")
      check(nextSize == secondBlockHeight)
    }

    //
    println("Mathing; firstBlock = $firstBlockHeight  secondBlockHeight = $secondBlockHeight")
    var remaining: Long = 1000000000000L - state.dropped
    var height: Long = state.size.toLong()

    val secondBlockCount: Long = remaining / blockSize
    remaining -= secondBlockCount * blockSize
    height += secondBlockCount * secondBlockHeight

    // Simulate the remaining rockfalls on the existing grid
    println("Simulating $remaining falls")
    lastHeight = state.size
    state.run(remaining.toInt())
    height += state.size - lastHeight

    return height
  }
}
