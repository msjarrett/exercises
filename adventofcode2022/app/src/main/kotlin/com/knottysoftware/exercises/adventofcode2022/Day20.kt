package com.knottysoftware.exercises.adventofcode2022

class Day20 : Exercise {
  private lateinit var encrypted: IntArray

  override fun parse(lines: Sequence<String>) {
    encrypted = lines.map { it.toInt() }.toList().toIntArray()
  }

  class Node(val value: Int, var prev: Node? = null, var next: Node? = null) {
    fun getNth(n: Int): Node {
      var pos = this
      repeat(n) { pos = pos.next!! }
      return pos
    }
  }

  fun printList(head: Node) {
    var pos = head
    do {
      print(pos.value)
      if (pos.next != head) print(", ") else println("")
      pos = pos.next!!
    } while (pos != head)
  }

  fun mix(input: IntArray, rounds: Int = 1, key: Long = 1): Node {
    // Array math sucks. Let's use a doubly-linked list, where reorders are free!

    // These are the pointers to the original nodes, no matter where they move.
    val inputNodes = input.map { Node(it) }
    var zeroNode: Node? = null

    // Build a double-ended chain
    for (i in 1 until inputNodes.size) {
      inputNodes[i].prev = inputNodes[i - 1]
      if (inputNodes[i].value == 0) {
        require(zeroNode == null)
        zeroNode = inputNodes[i]
      }
    }
    for (i in 0 until inputNodes.size - 1) inputNodes[i].next = inputNodes[i + 1]
    inputNodes[0].prev = inputNodes[inputNodes.size - 1]
    inputNodes[inputNodes.size - 1].next = inputNodes[0]

    // Do the mix, based on original input.
    repeat(rounds) {
      for (node in inputNodes) {
        // - Modulo size - 1
        //   eg. consider 3, 4, 5, 6
        val shift: Int = ((node.value.toLong() * key) % (input.size - 1)).toInt()
        if (shift != 0) {
          // Remove the node. We won't wrap to ourselves.
          node.prev!!.next = node.next
          node.next!!.prev = node.prev

          var pos = node
          if (shift > 0) {
            repeat(shift) { pos = pos.next!! }
          } else {
            pos = pos.prev!! // So we always wire to pos
            repeat(-shift) { pos = pos.prev!! }
          }

          node.prev = pos
          node.next = pos.next
          node.next!!.prev = node
          node.prev!!.next = node
        }
      }
    }

    // Return conveniently indexed to zeroNode
    return zeroNode!!
  }

  override fun partOne(): Int {
    // println(encrypted.joinToString())
    val decrypted = mix(encrypted)
    // printList(decrypted)

    val p1 = decrypted.getNth(1000 % encrypted.size).value
    val p2 = decrypted.getNth(2000 % encrypted.size).value
    val p3 = decrypted.getNth(3000 % encrypted.size).value
    println("$p1 $p2 $p3")
    return p1 + p2 + p3
  }

  override fun partTwo(): Long {
    val key = 811589153L
    // println(encrypted.joinToString())
    val decrypted = mix(encrypted, 10, key)
    // printList(decrypted)

    val p1 = decrypted.getNth(1000 % encrypted.size).value
    val p2 = decrypted.getNth(2000 % encrypted.size).value
    val p3 = decrypted.getNth(3000 % encrypted.size).value
    println("$p1 $p2 $p3")
    return (p1 + p2 + p3) * key
  }
}
