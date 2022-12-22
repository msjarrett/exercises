package com.knottysoftware.exercises.adventofcode2022

fun SafeAdd(lhs: Long, rhs: Long): Long {
  if (rhs < 0) return SafeSub(lhs, -rhs)
  val ret = lhs + rhs
  check(ret > lhs)
  return ret
}

fun SafeSub(lhs: Long, rhs: Long): Long {
  if (rhs < 0) return SafeAdd(lhs, -rhs)
  val ret = lhs - rhs
  check(ret < lhs)
  return ret
}

fun SafeMul(lhs: Long, rhs: Long): Long {
  val ret = lhs * rhs
  if (lhs != 0L && rhs != 0L) {
    check(ret / lhs == rhs)
  }
  return ret
}

fun SafeDiv(lhs: Long, rhs: Long): Long {
  val ret = lhs / rhs
  return ret
}

fun Gcd(n1: Long, n2: Long): Long {
  // https://baeldung.com/java-greatest-common-divisor#euclid's%20algorithm
  if (n2 == 0L) return n1 else return Gcd(n2, n1 % n2)
}

fun Lcm(n1: Long, n2: Long): Long = SafeMul((n1 / Gcd(n1, n2)), n2)
