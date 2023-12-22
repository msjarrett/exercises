package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class Day20 : Exercise {
    enum class Pulse {
        LOW, HIGH;
        override fun toString() = when (this) {
            LOW -> "0"
            HIGH -> "1"
        }
    }

    abstract class Module (val name: String, val dests: List<String>) {
        var signalCount = 0L

        /** Given a signal, update the state, and return what output to signal to each dest module. */
        abstract fun signal(pulse: Pulse, from: String): Pulse?
    }

    // broadcast
    class Broadcast (dests: List<String>) : Module("broadcaster", dests) {
        override fun signal(pulse: Pulse, from: String): Pulse {
            signalCount++
            return pulse
        }
    }

    // %module
    class FlipFlop(name: String, dests: List<String>) : Module(name, dests) {
        // Low is "off", High is "on"
        var state = Pulse.LOW

        override fun signal(pulse: Pulse, from: String): Pulse? {
            signalCount++
            if (pulse == Pulse.HIGH)
                return null
            if (state == Pulse.LOW)
                state = Pulse.HIGH
            else
                state = Pulse.LOW
            return state
        }
    }

    // &module
    class Conjunction(name: String, dests: List<String>, inputs: List<String>) : Module(name, dests) {
        val memory = mutableMapOf<String, Pulse>()

        init {
            // Is there a more functional way to build this?
            for (input in inputs) {
                memory[input] = Pulse.LOW
            }
        }

        override fun signal(pulse: Pulse, from: String): Pulse {
            signalCount++
            check(memory.containsKey(from))
            memory[from] = pulse
            if (memory.values.all { it == Pulse.HIGH })
                return Pulse.LOW
            else
                return Pulse.HIGH
        }

        override fun toString() = "$name=" + memory.map { (input, pulse) -> "$input=$pulse" }
    }

    private lateinit var modules: Map<String, Module>
    private lateinit var flops: List<FlipFlop>
    private lateinit var cons: List<Conjunction>

    // We must reparse - modules is stateful.
    override fun needReparse() = true

 /*   override val testInput = """
broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
    """.trimIndent()
    override val testResultPart1 = 32000000L */

    override val testInput = """
broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
    """.trimIndent()
    override val testResultPart1 = 11687500L

    override suspend fun parse(lines: Flow<String>) {
        // We need the parsed list to find the inputs to conjunctions.
        val parsedList = lines.toList().map {
            val parts = it.split(" -> ")
            parts[0] to parts[1].split(", ")
        }

        modules = parsedList.map {
            if (it.first == "broadcaster") {
                Pair(it.first, Broadcast(it.second))
            } else if (it.first[0] == '%') {
                Pair(it.first.substring(1), FlipFlop(it.first.substring(1), it.second))
            } else if (it.first[0] == '&') {
                val name = it.first.substring(1)
                val inputs = parsedList.filter { input -> input.second.contains(name) }.map { input -> input.first.substring(1) }
                Pair(name, Conjunction(name, it.second, inputs))
            } else {
                throw IllegalArgumentException("Invalid gate")
            }
        }.toMap()

        flops = modules.values.mapNotNull { it as? FlipFlop }
        cons = modules.values.mapNotNull { it as? Conjunction }
    }

    fun pushButton(observer: (String, Pulse) -> Unit) {
        // First signal is from button -low-> broadcaster.
        val queue = SearchQueue(Triple("button", "broadcaster", Pulse.LOW))
        for ((from, to, pulse) in queue) {
            observer(to, pulse)

            //println("$from  -- $pulse -- > $to")
            val module = modules[to]
            if (module == null) {
                // This can happen - eg. the "output" module from the example.
                // We counted receiving the signal, but do nothing.
                continue
            }
            val newPulse = module.signal(pulse, from)
            if (newPulse != null) {
                // Always process in order of arrival
                for (dest in module.dests) {
                    queue.addBFS(Triple(module.name, dest, newPulse))
                }
            }
        }
    }

    override suspend fun partOne(): Long {
        var countLow = 0L
        var countHigh = 0L
        repeat (1000) {
            pushButton { _, pulse ->
                if (pulse == Pulse.LOW)
                    countLow++
                else
                    countHigh++
            }

        }
        return countLow * countHigh
    }

    override suspend fun partTwo(): Long {
        // We can't brute this. 178,000,000 iterations was not enough.
        //
        // Is there a pattern in the input?
        // Backwards:
        //   There's only 9 conjunctions, but 48 flops. The conjunctions all chain into RX through inverters.
        //   So ultimately we care when a series of 27 flops have all signalled high.
        //   But there's no apparent pattern in the signaling, they are all signalled multiple times per round,
        //   and there's no clear pattern I can see.
        //
        // rx low -> dn -> {dd, fh, xp, fc} -> {&rs, &bd, &pm, &cc} must all have signaled low.
        // These also reset the broadcast flops. Is that relevant? Doesn't seem so.
        //
        // Forwards:
        //   broadcast-> { gz, xg, cd, sg } flops.
        //
        //
        //

        for (i in 0 .. 1000000) {
            pushButton { name, pulse ->
            }
        }
        return 0
    }

    /*
    override suspend fun partTwo(): Long {
        var rxLows = 0
        var pushes = 0L
        while (rxLows == 0) {
            pushes++
            if ((pushes % 1000000) == 0L) {
                println("Pushed $pushes")
            }
            pushButton { name, pulse ->
                if (name == "rx" && pulse == Pulse.LOW)
                    rxLows++
            }
        }
        return pushes
    }*/
}