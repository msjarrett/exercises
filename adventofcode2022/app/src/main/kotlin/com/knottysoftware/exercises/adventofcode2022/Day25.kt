package com.knottysoftware.exercises.adventofcode2022

class Day25 : Exercise {
  final val placeMap = mapOf('0' to 0, '1' to 1, '2' to 2, '-' to -1, '=' to -2)

  fun snafuToDecimal(snafu: String): Long {
    var place = 1L
    var total = 0L

    for (i in (snafu.length - 1) downTo 0) {
      total += placeMap[snafu[i]]!! * place
      place *= 5
    }
    println("$snafu -> $total")
    return total
  }

  fun decimalToSnafu(decimal: Long): String {
    val digits = mutableListOf<Char>()

    var total = decimal
    var place = 1
    var borrow = false
    while (total > 0 || borrow) {
      val cur = (total % 5).toInt() + if (borrow) 1 else 0
      var digit = 'X'
      when (cur) {
        0 -> {
          digit = '0'
          borrow = false
        }
        1 -> {
          digit = '1'
          borrow = false
        }
        2 -> {
          digit = '2'
          borrow = false
        }
        3 -> {
          digit = '='
          borrow = true
        }
        4 -> {
          digit = '-'
          borrow = true
        }
        5 -> {
          digit = '0'
          borrow = true
        }
      }
      digits.add(digit)
      total /= 5L
    }
    val ret = digits.reversed().joinToString("")
    println("$decimal -> $ret")
    return ret
  }

  private lateinit var lines: List<String>

  override fun parse(lines: Sequence<String>) {
    this.lines = lines.toList()
  }

  override fun partOne(): String {
    return decimalToSnafu(lines.map(::snafuToDecimal).sum())
  }
}
