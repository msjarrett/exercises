package com.knottysoftware.exercises.adventofcode2024

import com.knottysoftware.exercises.adventofcode.common.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

enum class Opcode {
  // A DIV 2^CO -> A  (truncate to int)
  ADV,

  // B XOR LO -> B
  BXL,

  // CO % 8 -> B
  BST,

  // IF (A != 0) LO -> IP (don't increment)
  JNZ,

  // B XOR C -> B
  BXC,

  // CO % 8 -> OUT
  OUT,

  // A DIV 2^CO -> B  (truncate to int)
  BDV,

  // A DIV 2^CO -> C  (truncate to int)
  CDV,
}

enum class Operand {
  REGA,
  REGB,
  REGC,
  LIT0,
  LIT1,
  LIT2,
  LIT3,
  LIT4,
  LIT5,
  LIT6,
  LIT7,
}

data class Inst(val opcode: Opcode, val operand: Operand)

fun operandFromCombo(a: Int) =
    when (a) {
      0 -> Operand.LIT0
      1 -> Operand.LIT1
      2 -> Operand.LIT2
      3 -> Operand.LIT3
      4 -> Operand.REGA
      5 -> Operand.REGB
      6 -> Operand.REGC
      else -> throw IllegalArgumentException()
    }

fun operandFromLiteral(a: Int) =
    when (a) {
      0 -> Operand.LIT0
      1 -> Operand.LIT1
      2 -> Operand.LIT2
      3 -> Operand.LIT3
      4 -> Operand.LIT4
      5 -> Operand.LIT5
      6 -> Operand.LIT6
      7 -> Operand.LIT7
      else -> throw IllegalArgumentException()
    }

fun instFromVals(a: Int, b: Int): Inst {
  require(a < 8)
  require(b < 8)

  return when (a) {
    0 -> Inst(Opcode.ADV, operandFromCombo(b))
    1 -> Inst(Opcode.BXL, operandFromLiteral(b))
    2 -> Inst(Opcode.BST, operandFromCombo(b))
    3 -> Inst(Opcode.JNZ, operandFromLiteral(b))
    4 -> Inst(Opcode.BXC, operandFromLiteral(b))
    5 -> Inst(Opcode.OUT, operandFromCombo(b))
    6 -> Inst(Opcode.BDV, operandFromCombo(b))
    7 -> Inst(Opcode.CDV, operandFromCombo(b))
    else -> throw IllegalArgumentException()
  }
}

typealias OutputFunction = (Int) -> Boolean

class Computer(val program: List<Inst>, var a: Long, var b: Long, var c: Long) {
  var ip = 0

  fun run(outFun: OutputFunction) {
    while ((ip / 2) < program.size) {
      // check(ip % 2 == 0)
      // printState()
      // readLine()
      val inst = program[ip / 2]

      ip += 2 // Do FIRST, since JNZ can overwrite
      if (!doInst(inst, outFun)) return
    }
  }

  fun doOperand(operand: Operand): Long =
      when (operand) {
        Operand.LIT0 -> 0L
        Operand.LIT1 -> 1L
        Operand.LIT2 -> 2L
        Operand.LIT3 -> 3L
        Operand.LIT4 -> 4L
        Operand.LIT5 -> 5L
        Operand.LIT6 -> 6L
        Operand.LIT7 -> 7L
        Operand.REGA -> a
        Operand.REGB -> b
        Operand.REGC -> c
      }

  fun doInst(inst: Inst, outFun: OutputFunction): Boolean {
    val operand = doOperand(inst.operand)

    when (inst.opcode) {
      Opcode.ADV -> a = a shr operand.toInt()
      Opcode.BXL -> b = b xor operand
      Opcode.BST -> b = operand and 7L
      Opcode.JNZ -> if (a != 0L) ip = operand.toInt()
      Opcode.BXC -> b = b xor c
      Opcode.OUT -> return outFun(operand.toInt() and 7)
      Opcode.BDV -> b = a shr operand.toInt()
      Opcode.CDV -> c = a shr operand.toInt()
    }
    return true // No output
  }

  fun printState() {
    println(
        "IP = %1\$d  A = %2\$x  B = %3\$x  C = %4\$x    %5\$s".format(ip, a, b, c, program[ip / 2]))
  }

  override fun toString() = "A = $a  B = $b  C = $c  IP = $ip\n$program"
}

suspend fun Day17a(lines: Flow<String>): Any {
  val registerRegex = Regex("""Register [ABC]: (\d+)""")
  val programRegex = Regex("""Program: (\d(?:,\d)*)""")
  val theLines = lines.toList()

  val compy =
      Computer(
          programRegex
              .matchEntire(theLines[4])!!
              .groupValues[1]
              .split(",")
              .map { it.toInt() }
              .chunked(2)
              .map { instFromVals(it[0], it[1]) },
          registerRegex.matchEntire(theLines[0])!!.groupValues[1].toLong(),
          registerRegex.matchEntire(theLines[1])!!.groupValues[1].toLong(),
          registerRegex.matchEntire(theLines[2])!!.groupValues[1].toLong())
  // println(compy)

  val outs = mutableListOf<Int>()
  compy.run {
    outs.add(it)
    true
  }

  return outs.joinToString(",")
}

suspend fun Day17b(lines: Flow<String>): Any {
  val registerRegex = Regex("""Register [ABC]: (\d+)""")
  val programRegex = Regex("""Program: (\d(?:,\d)*)""")
  val theLines = lines.toList()

  val programList =
      programRegex.matchEntire(theLines[4])!!.groupValues[1].split(",").map { it.toInt() }
  val program = programList.chunked(2).map { instFromVals(it[0], it[1]) }
  val b = registerRegex.matchEntire(theLines[1])!!.groupValues[1].toLong()
  val c = registerRegex.matchEntire(theLines[2])!!.groupValues[1].toLong()

  // println(program)
  val lowBits =
      listOf(
          0xe2bc11L,
          0xe2bc15L,
          0xe2bc25L,
          0xe2bc27L,
          0xe2bc28L,
          0xe2bc2dL,
          0xe2bc2fL,
          0xe2bd66L,
          0xe2bd68L,
          0xe2bd6eL,
          0xe2bd71L,
          0xe2bd76L,
          0xe2c191L,
          0xe6bc11L,
          0xe6bc15L,
          0xe6bc25L,
          0xe6bc27L,
          0xe6bc28L,
          0xe6bc2dL,
          0xe6bc2fL,
          0xe6bd66L,
          0xe6bd68L,
          0xe6bd6eL,
          0xe6bd71L,
          0xe6bd76L,
          0xe6c191L,
          0xeabc11L,
          0xeabc15L,
          0xeabc25L,
          0xeabc27L,
          0xeabc28L,
          0xeabc2dL,
          0xeabc2fL,
          0xeabd66L,
          0xeabd68L,
          0xeabd6eL,
          0xeabd71L,
          0xeabd76L,
          0xf2bc11L,
          0xf2bc15L,
          0xf2bc25L,
          0xf2bc27L,
          0xf2bc28L,
          0xf2bc2dL,
          0xf2bc2fL,
          0xf2bd66L,
          0xf2bd68L,
          0xf2bd6eL,
          0xf2bd71L,
          0xf2bd76L,
      )

  for (aHigh in 0L..0xFF_FFFF_FFFFL) {
    // if (aHigh % 0x100_0000L == 0L) println("Trying " + aHigh.toString(16))
    for (a in lowBits.map { (aHigh shl 24) xor it }) {
      val compy = Computer(program, a, b, c)
      var i = 0
      compy.run {
        if (it != programList[i]) false
        else {
          i++
          true
        }
      }
      // println (a.toString(16) +" --> $i")
      if (i == programList.size) return a
    }
  }

  return 0
}

suspend fun Day17c(lines: Flow<String>): Any {
  val registerRegex = Regex("""Register [ABC]: (\d+)""")
  val programRegex = Regex("""Program: (\d(?:,\d)*)""")
  val theLines = lines.toList()

  val programList =
      programRegex.matchEntire(theLines[4])!!.groupValues[1].split(",").map { it.toInt() }
  val program = programList.chunked(2).map { instFromVals(it[0], it[1]) }
  val b = registerRegex.matchEntire(theLines[1])!!.groupValues[1].toLong()
  val c = registerRegex.matchEntire(theLines[2])!!.groupValues[1].toLong()

  val goodNums = mutableSetOf<Long>()

  // println(program)
  for (a in 0L..0xFFFF_FFFFL) {
    val compy = Computer(program, a, b, c)
    var i = 0
    compy.run {
      if (it != programList[i]) false
      else {
        i++
        if (i > 10) goodNums.add(a and 0xFF_FFFFL)
        true
      }
    }
    if (i == programList.size) return a
  }

  for (n in goodNums) {
    // println(n.toString(16))
  }
  return 0
}
