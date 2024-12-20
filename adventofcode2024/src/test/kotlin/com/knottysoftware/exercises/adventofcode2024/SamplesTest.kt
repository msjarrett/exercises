package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.runner.RunPuzzleWithText
import org.junit.Assert.assertEquals
import org.junit.Test

class SamplesTest {
  @Test
  fun testDay00() {
    assertEquals(
        10,
        RunPuzzleWithText(
            ::Day0,
            """
            meep
            beep
            ok
        """))
  }

  @Test
  fun testDay01() {
    val sample =
        """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """

    assertEquals(11, RunPuzzleWithText(::Day1a, sample))
    assertEquals(31, RunPuzzleWithText(::Day1b, sample))
  }

  @Test
  fun testDay02() {
    val sample =
        """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
        """

    assertEquals(2, RunPuzzleWithText(::Day2a, sample))
    assertEquals(4, RunPuzzleWithText(::Day2b, sample))
  }

  @Test
  fun testDay03() {
    assertEquals(
        161L,
        RunPuzzleWithText(
            ::Day3a, """xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"""))
    assertEquals(
        48L,
        RunPuzzleWithText(
            ::Day3b,
            """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""))
  }

  @Test
  fun testDay04() {
    val sample =
        """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
        """

    assertEquals(18, RunPuzzleWithText(::Day4a, sample))
    assertEquals(9, RunPuzzleWithText(::Day4b, sample))
  }

  @Test
  fun testDay05() {
    val sample =
        """
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
        """

    assertEquals(143, RunPuzzleWithText(::Day5a, sample))
    assertEquals(123, RunPuzzleWithText(::Day5b, sample))
  }

  @Test
  fun testDay06() {
    val sample =
        """
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
        """

    assertEquals(41, RunPuzzleWithText(::Day6a, sample))
    assertEquals(6, RunPuzzleWithText(::Day6b, sample))
  }

  @Test
  fun testDay07() {
    val sample =
        """
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
        """

    assertEquals(3749L, RunPuzzleWithText(::Day7a, sample))
    assertEquals(11387L, RunPuzzleWithText(::Day7b, sample))
  }

  @Test
  fun testDay08() {
    val sample =
        """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
        """

    assertEquals(14, RunPuzzleWithText(::Day8a, sample))
    assertEquals(34, RunPuzzleWithText(::Day8b, sample))
  }

  @Test
  fun testDay09() {
    val sample = "2333133121414131402"

    assertEquals(1928L, RunPuzzleWithText(::Day9a, sample))
    assertEquals(2858L, RunPuzzleWithText(::Day9b, sample))
  }

  @Test
  fun testDay10() {
    val sample =
        """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
        """

    assertEquals(36, RunPuzzleWithText(::Day10a, sample))
    assertEquals(81, RunPuzzleWithText(::Day10b, sample))
  }

  @Test
  fun testDay11() {
    assertEquals(55312, RunPuzzleWithText(::Day11a, "125 17"))
  }

  @Test
  fun testDay12() {
    val sample =
        """
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
        """

    assertEquals(1930, RunPuzzleWithText(::Day12a, sample))
    assertEquals(1206, RunPuzzleWithText(::Day12b, sample))
  }

  @Test
  fun testDay13() {
    val sample =
        """
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279
  """
    assertEquals(480, RunPuzzleWithText(::Day13a, sample))
  }

  @Test
  fun testDay15() {
    val sample =
        """
##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
  """
    assertEquals(10092L, RunPuzzleWithText(::Day15a, sample))
    assertEquals(9021L, RunPuzzleWithText(::Day15b, sample))
  }

  @Test
  fun testDay18() {
    val sample =
        """
5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0
  """
    assertEquals(22, RunPuzzleWithText(::Day18a, sample))
    assertEquals("6,1", RunPuzzleWithText(::Day18b, sample))
  }
}
