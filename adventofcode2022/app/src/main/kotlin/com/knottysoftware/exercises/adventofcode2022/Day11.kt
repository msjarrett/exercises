package com.knottysoftware.exercises.adventofcode2022

class Day11 : Exercise {
    @JvmInline private value class MonkeyId(val id: Int)
    @JvmInline private value class Item(val worry: Long)
    private enum class OpType { MUL, ADD }

    // Hack: -1 = "old"
    private class Operation (val opType: OpType, val operand: Int) {
        public fun execute(old: Item): Item {
            val param = if (operand == -1) old.worry else operand.toLong()
            val ret =  when (opType) {
                OpType.MUL -> Item(old.worry * param)
                OpType.ADD -> Item(old.worry + param)
            }
            check(old.worry < ret.worry)  // Catch overflow
            return ret
        }

        public override fun toString() = "old $opType $operand"
    }

    private class Test(val divisor: Int, val trueMonkey: MonkeyId, val falseMonkey: MonkeyId) {
        // Which monkey is this thrown to?
        public fun doThrow(item: Item): MonkeyId =  if (item.worry % divisor == 0L) trueMonkey else falseMonkey

        public override fun toString() = "% $divisor ? $trueMonkey : $falseMonkey"
    }

    private inner class Monkey (val items: MutableList<Item>, val op: Operation, val test: Test) {
         var inspectCount: Int = 0
            private set

        public fun turn(worryReduce: Boolean): List<Pair<MonkeyId, Item>> {
            inspectCount += items.size
            val moves = items
                .map { op.execute(it) }
                .map { if (worryReduce) Item(it.worry / 3) else Item(it.worry % lcm) }
                .map { Pair<MonkeyId, Item>(test.doThrow(it), it) }
            items.clear()
            return moves
        }

        public fun add(item: Item) {
            items.add(item)
        }

        public override fun toString() = "({" + items.joinToString() + "}, $op, $test)"
    }

    private lateinit var monkeys: List<Monkey>
    private var lcm: Int = 0
 
    private final val itemRegex = Regex("""Starting items: (.*)$""")  // TODO capture individual items
    private final val opRegex = Regex("""Operation: new = old ([*+]) (old|\d+)""")
    private final val testRegex = Regex("""Test: divisible by (\d+)""")
    private final val truefalseRegex = Regex("""If (true|false): throw to monkey (\d+)""")

    override fun parse(lines: Sequence<String>) {
        var lcm : Int = 1
        val monkeys = mutableListOf<Monkey>()
        val lineIt = lines.iterator()
        while (lineIt.hasNext()) {
            // Parse monkey name
            lineIt.next()

            // Parse starting items
            val items = itemRegex.find(lineIt.next())!!.groupValues[1].split(", ").map { Item(it.toLong()) }.toMutableList()

            // Parse operation
            val opMatch = opRegex.find(lineIt.next())!!.groupValues
            val op = Operation(
                when (opMatch[1]) {
                    "*" -> OpType.MUL
                    "+" -> OpType.ADD
                    else -> throw IllegalArgumentException()
                },
                when (opMatch[2]) {
                    "old" -> -1
                    else -> opMatch[2].toInt()
                })
            
            // Parse test
            val divisor = testRegex.find(lineIt.next())!!.groupValues[1].toInt()
            lcm *= divisor
            val trueMonkey = MonkeyId(truefalseRegex.find(lineIt.next())!!.groupValues[2].toInt())
            val falseMonkey = MonkeyId(truefalseRegex.find(lineIt.next())!!.groupValues[2].toInt())
            val test = Test(divisor, trueMonkey, falseMonkey)

            monkeys.add(Monkey(items, op, test))
            //println(monkeys.last())

            // Consume blank
            if (lineIt.hasNext()) lineIt.next()            
        }
        this.monkeys = monkeys
        this.lcm = lcm
        println("lcm = $lcm")
    }


    override fun partOne(): Long {
        return 0
        repeat (20) {
            for (monkey in monkeys) {
                val doThrows = monkey.turn(worryReduce = true)
                for (doThrow in doThrows) {
                    monkeys[doThrow.first.id].add(doThrow.second)
                }
            }
        }
        return monkeys.map { it.inspectCount }.sortedDescending().take(2).fold(1) { acc, value -> acc * value }.toLong()
    }

    override fun partTwo(): Long {
        repeat (10000) {
            for (monkey in monkeys) {
                val doThrows = monkey.turn(worryReduce = false)
                for (doThrow in doThrows) {
                    monkeys[doThrow.first.id].add(doThrow.second)
                }
            }
        }

        return monkeys.map { it.inspectCount.toLong() }.sortedDescending().take(2).fold(1L) { acc, value -> acc * value }.toLong()
    }
}