package com.knottysoftware.exercises.adventofcode2022

class Day21 : Exercise {
  companion object {
    val REGEX_YELL = Regex("""([a-z]{4}): (\d+)""")
    val REGEX_OP = Regex("""([a-z]{4}): ([a-z]{4}) ([+*/-]) ([a-z]{4})""")
  }

  data class Monkey(
      val name: String,
      val value: Int? = null,
      val first: String? = null,
      val op: Char? = null,
      val second: String? = null
  ) {
    val isOp: Boolean
      get() = op != null
    val isYell: Boolean
      get() = value != null
  }

  private lateinit var monkeys: Map<String, Monkey>

  override fun parse(lines: Sequence<String>) {
    monkeys =
        lines
            .map {
              val yell = REGEX_YELL.matchEntire(it)
              if (yell != null) {
                val (name, value) = yell.destructured
                Monkey(name, value = value.toInt())
              } else {
                val (name, first, op, second) = REGEX_OP.matchEntire(it)!!.destructured
                Monkey(name, first = first, op = op[0], second = second)
              }
            }
            .associateBy { it.name }
  }

  override fun partOne(): Long {
    // Which monkeys to re-evaluate
    val wakeUpTriggers = monkeys.keys.associateWith { mutableListOf<String>() }
    for (m in monkeys.values) {
      if (!m.isOp) continue
      wakeUpTriggers[m.first!!]!!.add(m.name)
      wakeUpTriggers[m.second!!]!!.add(m.name)
    }

    val values = mutableMapOf<String, Long>()
    val wakeQueue = SearchQueue<String>()
    // Map all the yells
    for (m in monkeys.values.filter { it.isYell }) {
      values[m.name] = m.value!!.toLong()
      for (n in wakeUpTriggers[m.name]!!) {
        wakeQueue.addDFS(n)
      }
    }

    // Keep waking up monkeys until we find root.
    for (name in wakeQueue) {
      val m = monkeys[name]!!
      check(m.isOp) // If we're not an op, how did we wake up?
      if (values.contains(m.name)) continue
      if (!values.contains(m.first!!) || !values.contains(m.second!!)) continue

      val p1 = values[m.first]!!
      val p2 = values[m.second]!!
      values[m.name] =
          when (m.op!!) {
            '+' -> p1 + p2
            '-' -> p1 - p2
            '*' -> p1 * p2
            '/' -> p1 / p2
            else -> throw IllegalArgumentException()
          }
      for (n in wakeUpTriggers[m.name]!!) {
        wakeQueue.addDFS(n)
      }
      if (m.name == "root") break
    }

    // Return root yell
    return values["root"]!!
  }

  class Algebraic
  private constructor(
      val value: Long? = null,
      val name: String? = null,
      val left: Algebraic? = null,
      val op: Char? = null,
      val right: Algebraic? = null
  ) {
    constructor(literal: Long) : this(value = literal)
    constructor(variable: String) : this(name = variable)
    constructor(
        leftOp: Algebraic,
        opOp: Char,
        rightOp: Algebraic
    ) : this(left = leftOp, op = opOp, right = rightOp)

    val isValue: Boolean
      get() = value != null || (op == '/' && left!!.isValue && right!!.isValue)
    val isOp: Boolean
      get() = op != null

    operator fun plus(rhs: Algebraic) = Algebraic(this, '+', rhs).reduce()
    operator fun minus(rhs: Algebraic) = Algebraic(this, '-', rhs).reduce()
    operator fun times(rhs: Algebraic) = Algebraic(this, '*', rhs).reduce()
    operator fun div(rhs: Algebraic) = Algebraic(this, '/', rhs).reduce()

    public fun reduce(): Algebraic {
      if (op == null) return this // Can't reduce values or variables
      check(left != null)
      check(right != null)

      // Merge two value types.
      if (left.isValue && right.isValue) {
        // If they're integers, do real math!
        if (left.value != null && right.value != null) {
          if (op == '/' && (left.value % right.value) != 0L) {
            // Reduce to avoid excessively big numbers.
            // I'm not going to do GCD, but I'll take out the first two primes.
            val gcd = Gcd(left.value, right.value)
            if (gcd == 1L) return this
            return Algebraic(Algebraic(left.value / gcd), '/', Algebraic(right.value / gcd))
          }
          return Algebraic(
              when (op) {
                '+' -> SafeAdd(left.value, right.value)
                '-' -> SafeSub(left.value, right.value)
                '*' -> SafeMul(left.value, right.value)
                '/' -> SafeDiv(left.value, right.value)
                else -> throw IllegalArgumentException()
              })
        }

        // Reduce any combination of fraction and value to two fractions and recurse.
        if (left.value != null) {
          return Algebraic(Algebraic(left, '/', Algebraic(1)), op, right).reduce()
        }
        if (right.value != null) {
          return Algebraic(
                  left,
                  op,
                  Algebraic(right, '/', Algebraic(1)),
              )
              .reduce()
        }

        // Both are fractions, do fraction math
        val leftNum = left.left!!.value!!
        val leftDem = left.right!!.value!!
        val rightNum = right.left!!.value!!
        val rightDem = right.right!!.value!!

        when (op) {
          '*' ->
              return Algebraic(
                      Algebraic(SafeMul(leftNum, rightNum)),
                      '/',
                      Algebraic(SafeMul(leftDem, rightDem)))
                  .reduce()
          '/' ->
              return Algebraic(
                      Algebraic(SafeMul(leftNum, rightDem)),
                      '/',
                      Algebraic(SafeMul(leftDem, rightNum)))
                  .reduce()
          '+' -> {
            val lcm = Lcm(leftDem, rightDem)
            return Algebraic(
                    Algebraic(
                        SafeAdd(
                            SafeMul(leftNum, lcm / leftDem), SafeMul(rightNum, lcm / rightDem))),
                    '/',
                    Algebraic(lcm))
                .reduce()
          }
          '-' -> {
            val lcm = Lcm(leftDem, rightDem)
            return Algebraic(
                    Algebraic(
                        SafeSub(
                            SafeMul(leftNum, lcm / leftDem), SafeMul(rightNum, lcm / rightDem))),
                    '/',
                    Algebraic(lcm))
                .reduce()
          }
        }
      } // Both values

      // If both sides are complex, nothing else to do.
      if (!left.isValue && !right.isValue) return this

      val valueOp = if (left.isValue) left else right
      val complexOp = if (left.isValue) right else left

      // Simplify multiplication
      if (op == '*') {
        // Distribute across addition or subtraction.
        if (complexOp.op == '+' || complexOp.op == '-') {
          return Algebraic(
              Algebraic(complexOp.left!!, '*', valueOp).reduce(),
              complexOp.op,
              Algebraic(complexOp.right!!, '*', valueOp).reduce())
        }

        // Chain multiplication values
        if (complexOp.op == '*' && complexOp.left!!.isValue) {
          return Algebraic(
              complexOp.right!!, complexOp.op, Algebraic(complexOp.left, '*', valueOp).reduce())
        }
        if (complexOp.op == '*' && complexOp.right!!.isValue) {
          return Algebraic(
              complexOp.left!!, complexOp.op, Algebraic(complexOp.right, '*', valueOp).reduce())
        }

        // Across division (numerator only)
        if (complexOp.op == '/' && complexOp.left!!.isValue) {
          return Algebraic(
              Algebraic(complexOp.left, '*', valueOp).reduce(), complexOp.op, complexOp.right!!)
        }
      }

      // Simplify a value denominator
      if (op == '/' && right.isValue) {
        // Across addition or subtraction,
        if (complexOp.op == '+' || complexOp.op == '-') {
          println("REDUCING: $this")
          println(
              "TO " +
                  Algebraic(
                          Algebraic(complexOp.left!!, '/', valueOp).reduce(),
                          complexOp.op,
                          Algebraic(complexOp.right!!, '/', valueOp).reduce())
                      .toString())
          /*
          return Algebraic(
            Algebraic(complexOp.left!!, '/', valueOp).reduce(),
            complexOp.op,
            Algebraic(complexOp.right!!, '/', valueOp).reduce()
          )*/
        }

        // Chain multiplication if there is a value.
        if (complexOp.op == '*' && complexOp.left!!.isValue) {
          return Algebraic(
              complexOp.right!!, complexOp.op, Algebraic(complexOp.left, '/', valueOp).reduce())
        }
        if (complexOp.op == '*' && complexOp.right!!.isValue) {
          return Algebraic(
              complexOp.left!!, complexOp.op, Algebraic(complexOp.right, '/', valueOp).reduce())
        }

        // Merge division (denominator only), convert to '*', which must reduce.
        if (complexOp.op == '/' && complexOp.right!!.isValue) {
          return Algebraic(
              complexOp.left!!, complexOp.op, Algebraic(complexOp.right, '*', valueOp).reduce())
        }
      }

      // Chain addition or subtraction off left.
      if ((op == '+' || op == '-') &&
          (complexOp.op == '+' || complexOp.op == '-') &&
          complexOp.left!!.isValue) {
        return Algebraic(
            Algebraic(complexOp.left, op, valueOp).reduce(), complexOp.op, complexOp.right!!)
      }

      // Chain addition or subtraction off right +.
      if ((op == '+' || op == '-') && (complexOp.op == '+') && complexOp.right!!.isValue) {
        return Algebraic(
            complexOp.left!!,
            complexOp.op,
            Algebraic(complexOp.right, op, valueOp).reduce(),
        )
      }

      // Chain reversed math off right.
      if ((op == '+' || op == '-') && (complexOp.op == '-') && complexOp.right!!.isValue) {
        return Algebraic(
            complexOp.left!!,
            complexOp.op,
            Algebraic(complexOp.right, if (op == '+') '-' else '+', valueOp).reduce(),
        )
      }

      // Nope, nothing reduces.
      return this
    }

    public override fun toString(): String =
        when {
          value != null -> value.toString()
          name != null -> name
          op == '/' && left!!.value != null && right!!.value != null -> "$left/$right"
          op != null -> "($left $op $right)"
          else -> throw IllegalArgumentException()
        }
  }

  fun testMe(root: Algebraic) {
    // Find the root
    val queue = SearchQueue(listOf(root))
    var solveList: List<Algebraic>? = null
    for (l in queue) {
      if (l.last().name == "humn") {
        solveList = l
        break
      }

      if (l.last().isOp) {
        queue.addBFS(
            buildList {
              addAll(l)
              add(l.last().left!!)
            })
        queue.addBFS(
            buildList {
              addAll(l)
              add(l.last().right!!)
            })
      }
    }

    var sum = Algebraic(3379022190351)
    for (i in (solveList!!.size - 2) downTo 0) {
      if (solveList[i].left === solveList[i + 1])
          sum = Algebraic(sum, solveList[i].op!!, solveList[i].right!!).reduce()
      else if (solveList[i].right === solveList[i + 1])
          sum = Algebraic(solveList[i].left!!, solveList[i].op!!, sum).reduce()
      else {
        check(false)
      }
    }

    println("TEST $sum = 0")
  }

  override fun partTwo(): Long {
    // Which monkeys to re-evaluate
    val wakeUpTriggers = monkeys.keys.associateWith { mutableListOf<String>() }
    for (m in monkeys.values) {
      if (!m.isOp) continue
      wakeUpTriggers[m.first!!]!!.add(m.name)
      wakeUpTriggers[m.second!!]!!.add(m.name)
    }

    val values = mutableMapOf<String, Algebraic>()
    val wakeQueue = SearchQueue<String>()
    // Map all the yells
    for (m in monkeys.values.filter { it.isYell }) {
      // Part 2 override: humn is a variable
      if (m.name == "humn") {
        values[m.name] = Algebraic("humn") // Needs to be 3379022190351
      } else {
        values[m.name] = Algebraic(m.value!!.toLong())
      }
      for (n in wakeUpTriggers[m.name]!!) {
        wakeQueue.addDFS(n)
      }
    }

    // Keep waking up monkeys until we find root.
    for (name in wakeQueue) {
      val m = monkeys[name]!!
      check(m.isOp) // If we're not an op, how did we wake up?
      if (values.contains(m.name)) continue
      if (!values.contains(m.first!!) || !values.contains(m.second!!)) continue

      // Part 2 override - operation for root is always equality test.
      // But root is ready.
      if (m.name == "root") break

      val p1 = values[m.first]!!
      val p2 = values[m.second]!!
      values[m.name] =
          when (m.op!!) {
            '+' -> p1 + p2
            '-' -> p1 - p2
            '*' -> p1 * p2
            '/' -> p1 / p2
            else -> throw IllegalArgumentException()
          }
      for (n in wakeUpTriggers[m.name]!!) {
        wakeQueue.addDFS(n)
      }
    }

    val result = values[monkeys["root"]!!.first]!! - values[monkeys["root"]!!.second]!!
    println("$result = 0")

    testMe(result)

    return 0L
  }
}
