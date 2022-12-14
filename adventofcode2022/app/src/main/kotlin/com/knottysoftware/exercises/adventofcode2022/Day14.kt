package com.knottysoftware.exercises.adventofcode2022;

class Day14 : Exercise {
    private lateinit var tiles: MutableSet<Point>
    private var deepestTile = 0
    private final val START = Point(500, 0)

    override fun needReparse() = true

    override fun parse(lines: Sequence<String>) {
        val tiles = mutableSetOf<Point>()
        for (line in lines) {
            val coords = line.split(" -> ").map {
                val xy = it.split(',')
                require(xy.size == 2)
                Point(xy[0].toInt(), xy[1].toInt())
            }

            var prev: Point? = null
            for (coord in coords) {
                if (prev != null) {
                    val dir = prev.directionTo(coord)!!
                    var p: Point = prev
                    while (p != coord) {
                        tiles.add(p)
                        p = p.move(dir)
                    }
                    tiles.add(p)
                }
                prev = coord;
            }
        }
        deepestTile = tiles.map { it.y }.maxOrNull()!!
        this.tiles = tiles
    }

    // True if hit terminal (couldn't place sand)
    fun dropSand(infiniteFloor: Boolean): Boolean {
        var p = START
        while (true) {
            check(p.y < deepestTile + 2)  // Neither part should reach here.
            if (tiles.contains(START)) {
                // Start already filled. Terminal.
                return true
            } else if (!infiniteFloor && p.y == deepestTile) {
                // Fell off the world. Terminal.
                return true                
            } else if (infiniteFloor && p.y == deepestTile + 1) {
                // Settled on infinite floor.
                // Enforce the infinite floor rather than tiling width.
                tiles.add(p)
                return false
            }
            else if (!tiles.contains(p.move(Direction.DOWN))) p = p.move(Direction.DOWN)
            else if (!tiles.contains(p.move(Direction.DOWNLEFT))) p = p.move(Direction.DOWNLEFT)
            else if (!tiles.contains(p.move(Direction.DOWNRIGHT))) p = p.move(Direction.DOWNRIGHT)
            else {
                // Settled sand
                tiles.add(p)
                return false
            }
        }
    }

    override fun partOne(): Int {
        var sandCount = 0
        while (!dropSand(infiniteFloor = false)) ++sandCount
        return sandCount
    }

    override fun partTwo(): Int {
        var sandCount = 0
        while (!dropSand(infiniteFloor = true)) ++sandCount
        return sandCount
   }
}