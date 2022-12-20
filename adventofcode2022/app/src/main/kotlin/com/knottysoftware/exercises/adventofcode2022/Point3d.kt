package com.knottysoftware.exercises.adventofcode2022

data class Point3d(val x: Int, val y: Int, val z: Int) {
  fun neighbors(): List<Point3d> {
    return listOf(
        Point3d(x - 1, y, z),
        Point3d(x + 1, y, z),
        Point3d(x, y - 1, z),
        Point3d(x, y + 1, z),
        Point3d(x, y, z - 1),
        Point3d(x, y, z + 1),
    )
  }
}
