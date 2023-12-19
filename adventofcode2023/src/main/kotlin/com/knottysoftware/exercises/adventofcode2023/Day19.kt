package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlin.math.max
import kotlin.math.min

class Day19 : Exercise {
    enum class Prop { X, M, A, S }
    data class Rule(val prop: Prop, var op: Op, val num: Int, val dest: String) {
        enum class Op {
            GT, LT, WILD
        }
    }
    data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun getProp(prop: Prop) = when (prop) {
            Prop.X -> x
            Prop.M -> m
            Prop.A -> a
            Prop.S -> s
        }
    }

    private lateinit var flows: Map<String, List<Rule>>
    private lateinit var parts: List<Part>

    override val testInput = """
px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}
    """.trimIndent()

    override val testResultPart1 = 19114
    override val testResultPart2 = 167409079868000L

    override suspend fun parse(lines: Flow<String>) {
        val flowRegex = Regex("""([a-z]+)\{(.+)}""")
        val ruleRegex = Regex("""([xmas])([<>])(\d+):([a-zAR]+)""")
        val partRegex = Regex("""\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""")

        val lists = lines.toList().splitOnBlank()
        flows = lists[0].map {
            val (name, rules) = flowRegex.matchEntire(it)!!.destructured
            name to rules.split(",").map {
                if (!it.contains(':')) {
                    // Final rule
                    Rule(Prop.X, Rule.Op.WILD, 0, it)
                } else {
                    val (prop, op, num, dest) = ruleRegex.matchEntire(it)!!.destructured
                    Rule(
                        when (prop[0]) {
                            'x' -> Prop.X
                            'm' -> Prop.M
                            'a' -> Prop.A
                            's' -> Prop.S
                            else -> throw IllegalArgumentException()
                        },
                        when (op[0]) {
                            '>' -> Rule.Op.GT
                            '<' -> Rule.Op.LT
                            else -> throw IllegalArgumentException()
                        },
                        num.toInt(),
                        dest)
                }
            }
        }.toMap()

        parts = lists[1].map {
            val (x, m, a, s) = partRegex.matchEntire(it)!!.destructured
            Part(x.toInt(), m.toInt(), a.toInt(), s.toInt())
        }
    }

    fun runPart(part: Part): String {
        var flow = "in"
        while (true) {
            val rules = flows[flow]!!
            for (rule in rules) {
                val testVal = part.getProp(rule.prop)
                val test = when (rule.op) {
                    Rule.Op.GT -> testVal > rule.num
                    Rule.Op.LT -> testVal < rule.num
                    Rule.Op.WILD -> true
                }

                if (test) {
                    if (rule.dest == "A" || rule.dest == "R")
                        return rule.dest
                    flow = rule.dest
                    break // don't evaluate more rules
                }
            }
        }
    }

    override suspend fun partOne() = parts.filter { runPart(it) == "A" }.map { it.x + it.m + it.a + it.s }.sum()

    override suspend fun partTwo(): Long {
        // Based on analysis of the data.
        // - There's multiple terminal nodes (both A's and R's).
        // - The workflow graph is acyclic: each workflow is only referenced by exactly one other workflow.

        data class PartRange (val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
            constructor() : this(1.. 4000, 1 .. 4000, 1 .. 4000, 1 .. 4000)

            fun translateRange(range: IntRange, rule: Rule) = when (rule.op) {
                Rule.Op.WILD -> range
                Rule.Op.GT -> max(range.start, rule.num + 1) .. range.endInclusive
                Rule.Op.LT -> range.start .. min(range.endInclusive, rule.num - 1)
            }

            fun translateRangeExclude(range: IntRange, rule: Rule) = when (rule.op) {
                Rule.Op.WILD -> IntRange.EMPTY
                Rule.Op.GT -> range.start .. min(range.endInclusive, rule.num)   // <=
                Rule.Op.LT -> max(range.start, rule.num) .. range.endInclusive
            }

            fun limitTo(rule: Rule): PartRange {
                if (rule.op == Rule.Op.WILD) return this
                when (rule.prop) {
                    Prop.X -> return PartRange(translateRange(x, rule), m, a, s)
                    Prop.M -> return PartRange(x, translateRange(m, rule), a, s)
                    Prop.A -> return PartRange(x, m, translateRange(a, rule), s)
                    Prop.S -> return PartRange(x, m, a, translateRange(s, rule))
                }
            }

            fun limitExclude(rule: Rule): PartRange {
                if (rule.op == Rule.Op.WILD) return PartRange(IntRange.EMPTY, IntRange.EMPTY, IntRange.EMPTY, IntRange.EMPTY)
                when (rule.prop) {
                    Prop.X -> return PartRange(translateRangeExclude(x, rule), m, a, s)
                    Prop.M-> return PartRange(x, translateRangeExclude(m, rule), a, s)
                    Prop.A -> return PartRange(x, m, translateRangeExclude(a, rule), s)
                    Prop.S -> return PartRange(x, m, a, translateRangeExclude(s, rule))
                }
            }
        }

        // Find the acceptable part ranges.
        val acceptedRanges = mutableListOf<PartRange>()
        val queue = SearchQueue(Pair("in", PartRange()))
        for ((flow, range) in queue) {
            var newRange = range
            for (rule in flows[flow]!!) {
                val thisRange = newRange.limitTo(rule)
                newRange = newRange.limitExclude(rule)
                if (rule.dest == "A") {
                    acceptedRanges.add(thisRange)
                } else if (rule.dest != "R") {
                    queue.addBFS(Pair(rule.dest, thisRange))
                }
            }
        }

        // Can we assume these are disjoint? I think so.
        return acceptedRanges.map {
            //println("$it")
            it.x.count().toLong() * it.m.count().toLong() * it.a.count().toLong() * it.s.count().toLong()
        }.sum()
    }
}