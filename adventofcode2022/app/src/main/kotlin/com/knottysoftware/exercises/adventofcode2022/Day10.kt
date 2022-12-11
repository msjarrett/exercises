package com.knottysoftware.exercises.adventofcode2022

import kotlin.text.StringBuilder

class Day10 : Exercise {
    enum class Op {
        NOOP, ADDX
    }
    lateinit var opcodes: List<Pair<Op, Int>>

    override fun parse(lines: Sequence<String>) {
        opcodes = lines.map {
            val params = it.split(' ')
            when (params[0]) {
                "noop" -> Pair(Op.NOOP, 0)
                "addx" -> Pair(Op.ADDX, params[1].toInt())
                else -> throw IllegalArgumentException()
            }
        }.toList()
    }

    fun execute(cycleCallback: (cycle: Int, x: Int) -> Unit) {
        var x = 1
        var cycle = 0
        var burnCycles = 0

        var nextAssignCycle = 0
        var nextAssignValue = 0

        val iOp = opcodes.iterator()
        while (iOp.hasNext() || burnCycles > 0) {            
            cycle++
            // Process the instruction, unless we're burning cycles
            if (burnCycles > 0) {
                burnCycles--
            } else if (iOp.hasNext()) {                
                val op = iOp.next()
                if (op.first == Op.ADDX) {
                    nextAssignCycle = cycle + 1
                    nextAssignValue = x + op.second
                    burnCycles = 1
                }
            }

            cycleCallback(cycle, x)

            // Value is assigned at end of cycle
            if (cycle == nextAssignCycle) {
                x = nextAssignValue
            }

        }
    }

    override fun partOne(): Int {
        var signalSum = 0
        execute { cycle: Int, x: Int ->
            if (((cycle - 20) % 40) == 0) {
                signalSum += cycle * x
            }            
        }

        return signalSum
    }

    override fun partTwo(): String {
        val output = StringBuilder("\n")
        val line = StringBuilder()
        execute { _: Int, x: Int ->
            if (((x - 1) .. (x + 1)).contains(line.length)) line.append('#')
            else line.append('.')
            if (line.length == 40) {
                output.append(line)
                output.append('\n')
                line.clear()
            }
        }
        return output.toString()
    }
}