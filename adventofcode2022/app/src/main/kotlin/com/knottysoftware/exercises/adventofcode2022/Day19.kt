package com.knottysoftware.exercises.adventofcode2022

import kotlin.math.max

class Day19 : Exercise {
  companion object {
    private final val REGEX =
        Regex(
            """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""")
  }

  private data class Counts(
      var ore: Int = 0,
      var clay: Int = 0,
      var obsidian: Int = 0,
      var geodes: Int = 0
  ) {
    // Normally I'd use operator+, but mutable is better than spamming the object heap.
    fun add(rhs: Counts) {
      ore += rhs.ore
      clay += rhs.clay
      obsidian += rhs.obsidian
      geodes += rhs.geodes
    }

    fun sub(rhs: Counts) {
      ore -= rhs.ore
      clay -= rhs.clay
      obsidian -= rhs.obsidian
      geodes -= rhs.geodes
    }

    fun has(rhs: Counts): Boolean =
        ore >= rhs.ore && clay >= rhs.clay && obsidian >= rhs.obsidian && geodes >= rhs.geodes

    override fun toString(): String = "(o=$ore c=$clay o=$obsidian g=$geodes)"
  }

  private class Blueprint(
      val id: Int,
      oreRobotOreCost: Int,
      clayRobotOreCost: Int,
      obsidianRobotOreCost: Int,
      obsidianRobotClayCost: Int,
      geodeRobotOreCost: Int,
      geodeRobotObsidianCost: Int
  ) {
    val oreRobotCost = Counts(ore = oreRobotOreCost)
    val clayRobotCost = Counts(ore = clayRobotOreCost)
    val obsidianRobotCost = Counts(ore = obsidianRobotOreCost, clay = obsidianRobotClayCost)
    val geodeRobotCost = Counts(ore = geodeRobotOreCost, obsidian = geodeRobotObsidianCost)
  }

  // Data classes can be copied, but it's shallow.
  private class Simulation(
      val blueprint: Blueprint,
      var time: Int = 1,
      val resources: Counts = Counts(),
      val robots: Counts = Counts(ore = 1),
      val history: MutableList<String> = mutableListOf<String>()
  ) {
    var newRobots = Counts()

    fun addOreRobot(): Simulation {
      check(resources.has(blueprint.oreRobotCost))
      newRobots.ore++
      resources.sub(blueprint.oreRobotCost)
      history.add("T=$time ORE")
      return this
    }

    fun turnsForOreRobot(): Int {
      if (resources.has(blueprint.oreRobotCost)) return 0
      return (blueprint.oreRobotCost.ore - resources.ore + robots.ore - 1) / robots.ore
    }

    fun addClayRobot(): Simulation {
      check(resources.has(blueprint.clayRobotCost))
      newRobots.clay++
      resources.sub(blueprint.clayRobotCost)
      history.add("T=$time CLAY")
      return this
    }

    fun turnsForClayRobot(): Int {
      if (resources.has(blueprint.clayRobotCost)) return 0
      return (blueprint.clayRobotCost.ore - resources.ore + robots.ore - 1) / robots.ore
    }

    fun addObsidianRobot(): Simulation {
      check(resources.has(blueprint.obsidianRobotCost))
      newRobots.obsidian++
      resources.sub(blueprint.obsidianRobotCost)
      history.add("T=$time OBSIDIAN")
      return this
    }

    fun turnsForObsidianRobot(): Int {
      if (robots.clay == 0) return 1000
      if (resources.has(blueprint.obsidianRobotCost)) return 0
      return max(
          (blueprint.obsidianRobotCost.ore - resources.ore + robots.ore - 1) / robots.ore,
          (blueprint.obsidianRobotCost.clay - resources.clay + robots.clay - 1) / robots.clay)
    }

    fun addGeodeRobot(): Simulation {
      check(resources.has(blueprint.geodeRobotCost))
      newRobots.geodes++
      resources.sub(blueprint.geodeRobotCost)
      history.add("T=$time GEODE")
      return this
    }

    fun turnsForGeodeRobot(): Int {
      if (robots.obsidian == 0) return 1000
      if (resources.has(blueprint.geodeRobotCost)) return 0
      return max(
          (blueprint.geodeRobotCost.ore - resources.ore + robots.ore - 1) / robots.ore,
          (blueprint.geodeRobotCost.obsidian - resources.obsidian + robots.obsidian - 1) /
              robots.obsidian)
    }

    fun endTurn() {
      // Order matters, don't add robots until after resources.
      resources.add(robots)
      robots.add(newRobots)
      newRobots = Counts()
      time++
    }

    fun fork(): Simulation {
      return Simulation(blueprint, time, resources.copy(), robots.copy(), history.toMutableList())
    }

    override fun toString() = "T=$time RES=$resources ROBOTS=$robots"
  }

  private lateinit var blueprints: List<Blueprint>

  override fun parse(lines: Sequence<String>) {
    blueprints =
        lines
            .map {
              val (
                  id,
                  oreRobotOreCost,
                  clayRobotOreCost,
                  obsidianRobotOreCost,
                  obsidianRobotClayCost,
                  geodeRobotOreCost,
                  geodeRobotObsidianCost) =
                  REGEX.matchEntire(it)!!.destructured
              Blueprint(
                  id.toInt(),
                  oreRobotOreCost.toInt(),
                  clayRobotOreCost.toInt(),
                  obsidianRobotOreCost.toInt(),
                  obsidianRobotClayCost.toInt(),
                  geodeRobotOreCost.toInt(),
                  geodeRobotObsidianCost.toInt())
            }
            .toList()
  }

  private fun simulate(bp: Blueprint, timeOut: Int): Simulation {
    var best = Simulation(bp)
    var visited = 0
    var pruned = 0

    val queue = SearchQueue(Simulation(bp))
    for (s in queue) {
      val time = s.time

      if ((visited++ % 1000) == 0) print("CUR $s   BEST $best  ($pruned pruned)     \r")

      // Simulation done?
      if (time == timeOut + 1) {
        if (best.resources.geodes < s.resources.geodes) best = s
        continue
      }

      // Is it worth following this path?
      // Pessimistic filter: assume we can add geode robots every turn, would it be enough to beat
      // best?
      //
      // This relies on a fairly targeted search, we need to find good solutions quickly to filter
      // more.
      var bestGeodes = s.resources.geodes + s.robots.geodes * (timeOut + 1 - time)
      for (i in 1..(timeOut - time)) bestGeodes += i
      if (bestGeodes < best.resources.geodes) {
        pruned++
        continue
      }

      // Search new candidates. It's too slow to simulate turn-by-turn, so we just simulate waiting
      // for the next robot purchase.
      val newNodes = mutableListOf<Simulation>()
      if ((time + s.turnsForOreRobot()) < timeOut) {
        val n = s.fork()
        repeat(s.turnsForOreRobot()) { n.endTurn() }
        n.addOreRobot()
        newNodes.add(n)
      }
      if ((time + s.turnsForClayRobot()) < timeOut) {
        val n = s.fork()
        repeat(s.turnsForClayRobot()) { n.endTurn() }
        n.addClayRobot()
        newNodes.add(n)
      }
      if ((time + s.turnsForObsidianRobot()) < timeOut) {
        val n = s.fork()
        repeat(s.turnsForObsidianRobot()) { n.endTurn() }
        n.addObsidianRobot()
        newNodes.add(n)
      }
      if ((time + s.turnsForGeodeRobot()) < timeOut) {
        val n = s.fork()
        repeat(s.turnsForGeodeRobot()) { n.endTurn() }
        n.addGeodeRobot()
        newNodes.add(n)
      }
      if (newNodes.isEmpty()) {
        repeat(timeOut - time) { s.endTurn() }
        newNodes.add(s)
      }

      for (n in newNodes) {
        n.endTurn()
        val score =
            n.time -
                (10 * n.robots.geodes +
                    5 * n.robots.obsidian +
                    2 * n.robots.clay +
                    1 * n.robots.ore)
        queue.addScored(n, score)
      }
    }
    println("                                                             ")

    println("BP ${bp.id} produces $best")
    // println(best.history.joinToString("\n"))
    return best
  }

  override fun partOne() =
      blueprints.map { simulate(it, 24) }.map { it.resources.geodes * it.blueprint.id }.sum()

  override fun partTwo() =
      blueprints
          .take(3)
          .map { simulate(it, 32) }
          .map { it.resources.geodes }
          .reduce { acc, next -> acc * next }
}
