package com.knottysoftware.exercises.adventofcode2022

typealias Node = String

class Day16 : Exercise {
  companion object {
    private final val REGEX =
        Regex("""Valve ([A-Z][A-Z]) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)$""")
    private const val TIME_LIMIT = 30
    private const val START_NODE = "AA"
  }

  private lateinit var paths: Map<Node, List<Node>> // Canonical source of nodes
  private lateinit var pressure: Map<Node, Int> // Only non-zero pressures
  private lateinit var relevantNodes: Set<Node> // All nodes relevant to graphing.
  private var totalPressure = 0

  override fun parse(lines: Sequence<String>) {
    var paths = mutableMapOf<Node, List<Node>>()
    var pressure = mutableMapOf<Node, Int>()

    for (line in lines) {
      val (node, flow, neighbors) = REGEX.matchEntire(line)!!.destructured
      paths.put(node, neighbors.split(", "))

      // Add start node, even if it's zero pressure. This will help with graph-based
      // solutions.
      if (flow.toInt() != 0) pressure.put(node, flow.toInt())
    }
    this.paths = paths
    this.pressure = pressure
    this.relevantNodes =
        buildSet<Node> {
          addAll(pressure.keys)
          add(START_NODE)
        }
    this.totalPressure = pressure.values.sum()
  }

  inner class State
  private constructor(
      val elapsed: Int,
      val released: Int,
      val position: Node,
      val opened: Set<Node>
  ) {

    constructor() : this(0, 0, START_NODE, setOf<Node>())

    val pos: Node
      get() = position
    val visitedKey: Node
      get() = position

    fun moveTo(node: Node, cost: Int = 1): State = State(elapsed + cost, released, node, opened)

    fun openValve(): State {
      check(!opened.contains(position))
      check(pressure[position] != null)
      return State(
          elapsed + 1,
          released + (TIME_LIMIT - elapsed - 1) * pressure[position]!!,
          position,
          buildSet {
            addAll(opened)
            add(position)
          })
    }
  }

  inner class StateEx
  private constructor(
      val released: Int,
      val pos1: Node,
      val nextTurn1: Int,
      val pos2: Node,
      val nextTurn2: Int,
      val opened: Set<Node>,
      var remainingPressure: Int = totalPressure
  ) {

    constructor(
        startTime: Int
    ) : this(0, START_NODE, startTime, START_NODE, startTime, setOf<Node>())

    val pos: Node
      get() = if (!turn) pos1 else pos2
    val visitedKey: Node
      get() = if (pos1 < pos2) pos1 + pos2 else pos2 + pos1
    val elapsed: Int
      get() = if (turn) nextTurn2 else nextTurn1

    private val turn
      get() = nextTurn2 < nextTurn1

    fun moveTo(node: Node, cost: Int): StateEx =
        if (!turn) StateEx(released, node, nextTurn1 + cost, pos2, nextTurn2, opened)
        else StateEx(released, pos1, nextTurn1, node, nextTurn2 + cost, opened, remainingPressure)

    fun openValve(): StateEx {
      check(!opened.contains(pos))
      check(pressure[pos]!! != 0)
      return StateEx(
          released + (TIME_LIMIT - elapsed - 1) * pressure[pos]!!,
          pos1,
          if (!turn) nextTurn1 + 1 else nextTurn1,
          pos2,
          if (turn) nextTurn2 + 1 else nextTurn2,
          buildSet {
            addAll(opened)
            add(pos)
          },
          remainingPressure - pressure[pos]!!)
    }
  }

  override fun partOne() = partOneByGraphSearch()
  override fun partTwo() = partTwoByGraphSearch()

  fun generateDistanceMap() =
      relevantNodes.associateWith {
        // Dijstra explore the full graph
        val visited = mutableMapOf<Node, Int>()
        val queue = SearchQueue<Pair<Node, Int>>()
        queue.addBFS(Pair(it, 0))
        for ((n, cost) in queue) {
          // val (n, cost) = queue.removeFirst()
          if (visited.contains(n)) continue

          visited.put(n, cost)
          for (next in paths[n]!!.map { Pair(it, cost + 1) }) queue.addBFS(next)
        }

        visited.filterKeys { pressure[it] != null }
      }

  fun partOneByNodeSearch(): Int {
    // We CAN backtrack, and delaying opening nodes CAN be good.

    // So I just added hack after hack to reduce this space.
    // - The visited filter. Barely enough for the sample, not enough for input.
    //   But it IS really important...
    // - The score filter, solved the sample, but did nothing for part 1. I cut it.
    // - Don't open zero nodes. That helped the best.
    var bestState = State()
    var lastElapsed = -1

    // Don't come back unless we did something.
    val visited =
        buildMap<Node, MutableSet<Pair<Int, Set<Node>>>> {
          for (node in paths.keys) {
            put(node, mutableSetOf<Pair<Int, Set<Node>>>())
          }
        }

    val queue = ArrayDeque<State>()
    queue.add(bestState)

    while (!queue.isEmpty()) {
      val state = queue.removeFirst()
      val pos = state.pos

      // Are we the best state?
      if (state.released > bestState.released) bestState = state
      if (lastElapsed != state.elapsed) {
        lastElapsed = state.elapsed
        println("T = $lastElapsed  SCORE = ${bestState.released}")
      }

      // Filter to avoid future exploration.
      // - Even if we open a valve now, we're done.
      if (state.elapsed == TIME_LIMIT) continue

      // - We've opened all the valves.
      if (state.opened.size == pressure.size) continue

      // - We came here before.
      if (visited[state.visitedKey]!!.contains(Pair(state.released, state.opened))) continue

      // VISIT
      visited[state.visitedKey]!!.add(Pair(state.released, state.opened))

      // Add new nodes
      if (!state.opened.contains(pos) && pressure[pos] != null) {
        queue.addLast(state.openValve())
      }
      for (next in paths[pos]!!) {
        queue.addLast(state.moveTo(next))
      }
    } // while (!queue.isEmpty())
    return bestState.released
  }

  fun partOneByGraphSearch(): Int {
    // So this is how it should have gone the entire time.
    // Rather than wander the space, directly visit the non-zero pressure nodes (there aren't many)
    // with edge costs.

    // Calculate the distance between nodes.
    println("Mapping")
    val distanceMap = generateDistanceMap()

    // Search the space for optimal route.
    println("Searching")
    var lastElapsed = -1

    val visited = mutableMapOf<Node, MutableSet<Pair<Int, Set<Node>>>>()

    var bestState = State()
    val queue = SearchQueue<State>(bestState)
    for (state in queue) {
      val pos = state.pos

      // Are we the best state?
      // Don't care if the move put us over time limit, since
      // move doesn't impact released, and openValve accounts for
      // time.
      if (state.released > bestState.released) bestState = state
      if (lastElapsed < state.elapsed) {
        lastElapsed = state.elapsed
        println("T = $lastElapsed  SCORE = ${bestState.released}")
      }

      // Filter to avoid future exploration.
      // - Even if we open a valve now, we're done.
      if (state.elapsed >= TIME_LIMIT) continue

      // - We've opened all the valves.
      if (state.opened.size == pressure.size) continue

      // - We came here before.
      if (visited[state.visitedKey] == null)
          visited.put(state.visitedKey, mutableSetOf<Pair<Int, Set<Node>>>())
      if (visited[state.visitedKey]!!.contains(Pair(state.released, state.opened))) continue

      // VISIT
      visited[state.visitedKey]!!.add(Pair(state.released, state.opened))
      if (!state.opened.contains(pos) && pressure[pos] != null) {
        // If this is not the starting node and we haven't opened it, open now.
        // We wouldn't come here unless we were going to open it, so it's always the next state.
        val newState = state.openValve()
        queue.addScored(newState, -newState.released)
      } else {
        // Already open, find nodes we haven't visited.
        for (next in distanceMap[pos]!!.keys.filter { !state.opened.contains(it) }) {
          queue.addScored(state.moveTo(next, distanceMap[pos]!![next]!!), -state.released)
        }
      }
    } // for (state in queue)
    return bestState.released
  }

  fun partTwoByGraphSearch(): Int {
    println("Mapping")
    val distanceMap = generateDistanceMap()

    // Search the space for optimal route.
    // DFS seems to put less memory pressure, can run for a long time.
    println("Searching ${pressure.size} nodes")

    var bestState = StateEx(4)
    val queue = SearchQueue<StateEx>(bestState)
    for (state in queue) {
      val pos = state.pos

      // Are we the best state?
      // Don't care if the move put us over time limit, since
      // move doesn't impact released, and openValve accounts for
      // time.
      if (state.released > bestState.released) {
        bestState = state
        println(
            "T = ${bestState.elapsed}  SCORE = ${bestState.released}  OPENED = " +
                bestState.opened.size)
      }

      // Filter to avoid future exploration.
      // - Even if we open a valve now, we're done.
      if (state.elapsed >= TIME_LIMIT) continue

      // - We've opened all the valves.
      if (state.opened.size == pressure.size) continue

      // - Not enough pressure left to matter.
      val perfectRelease = state.released + state.remainingPressure * (TIME_LIMIT - state.elapsed)
      if (perfectRelease < bestState.released) continue

      // VISIT
      if (!state.opened.contains(pos) && pressure[pos] != null) {
        // If this is not the starting node and we haven't opened it, open now.
        // We wouldn't come here unless we were going to open it, so it's always the next state.
        val newState = state.openValve()
        queue.addScored(newState, -newState.released)
      } else {
        // Already open, find nodes we haven't visited.
        for (next in distanceMap[pos]!!.keys.filter { !state.opened.contains(it) }) {
          queue.addScored(state.moveTo(next, distanceMap[pos]!![next]!!), -state.released)
        }
      }
    } // for (state in queue)
    return bestState.released
  }
}
