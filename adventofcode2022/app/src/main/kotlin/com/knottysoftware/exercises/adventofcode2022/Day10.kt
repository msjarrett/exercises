package com.knottysoftware.exercises.adventofcode2022

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

    override fun partOne(): Long {
/*        var x = 1
        var cycle = 0
        var burnCycles = 0
        var signalSum: Long = 0

        var nextAssignCycle = 0
        var nextAssignValue = 0

        val iOp = opcodes.iterator()
        while (cycle < 220) {            
            cycle++
            // Process the instruction, unless we're burning cycles
            if (burnCycles > 0) {
                //println("$cycle burned")
                burnCycles--
            } else if (iOp.hasNext()) {                
                val op = iOp.next()
                if (op.first == Op.ADDX) {
                    //println("$cycle add %{op.second}")
                    nextAssignCycle = cycle + 1
                    nextAssignValue = x + op.second
                    burnCycles = 1
                }
            }

            // Check signal strength
            if (((cycle - 20) % 40) == 0) {
                signalSum += cycle * x
            }

            // Value is assigned at end of cycle
            if (cycle == nextAssignCycle) {
                x = nextAssignValue
                //println("$cycle END X = %x")
            }

        }*/
        var signalSum = 0
        execute { cycle: Int, x: Int ->
            if (((cycle - 20) % 40) == 0) {
                signalSum += cycle * x
            }            
        }

        return signalSum.toLong()
    }

    override fun partTwo(): Long {
        /*var cycle = 0
        var burnCycles = 0
        var nextAssignCycle = 0
        var nextAssignValue = 0
        var x = 1

        var line = ""

        val iOp = opcodes.iterator()
        while (iOp.hasNext()) {
            cycle++
            // Process the instruction, unless we're burning cycles
            if (burnCycles > 0) {
                burnCycles--
            } else if (iOp.hasNext()) {                
                val op = iOp.next()
                if (op.first == Op.ADDX) {
                    //println("$cycle add %{op.second}")
                    nextAssignCycle = cycle + 1
                    nextAssignValue = x + op.second
                    burnCycles = 1
                }
            }

            // Render a pixel
            if (((x - 1) .. (x + 1)).contains(line.length)) line = line + "#"
            else line = line + "."
            if (line.length == 40) {
                println(line)
                line = ""
            }


            // Value is assigned at end of cycle
            if (cycle == nextAssignCycle) {
                x = nextAssignValue
                //println("$cycle END X = %x")
            }
        }*/
        var line = ""
        execute { _: Int, x: Int ->
            if (((x - 1) .. (x + 1)).contains(line.length)) line = line + "#"
            else line = line + "."
            if (line.length == 40) {
                println(line)
                line = ""
            }
        }
        return 0
    }
}