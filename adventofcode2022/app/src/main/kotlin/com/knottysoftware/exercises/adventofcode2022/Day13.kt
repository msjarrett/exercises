package com.knottysoftware.exercises.adventofcode2022

class Day13 : Exercise {
    sealed interface Node
    data class ListNode(val children: List<Node>) : Node
    data class IntNode(val value: Int) : Node 

    private lateinit var packetPairs : List<Pair<ListNode, ListNode>>

    fun parseItem(line: String, start: Int): Pair<IntNode, Int> {
        var end = start
        while (line[end] != ',' && line[end] != ']') end++
        return Pair(IntNode(line.substring(start, end).toInt()), end)
    }

    fun parseList(line: String, start: Int): Pair<ListNode, Int> {
        //println("Parsing at $start $line")
        val children = mutableListOf<Node>()
        require(line[start] == '[')
        var i = start + 1

        loopy@ while (true) {
            //println (" -> $i")
            require(i < line.length)
            when (line[i]) {
                ']' -> break@loopy
                '[' -> {
                    val (newList, newPos) = parseList(line, i)
                    i = newPos
                    children.add(newList)
                }
                ',' -> { ++i }   // Continue parsing items
                else -> {
                    val (newItem, newPos) = parseItem(line, i)
                    i = newPos
                    children.add(newItem)
                }                
            }
        }

        return Pair(ListNode(children), i + 1)  // Skip our current closing bracket
    }

    override fun parse(lines: Sequence<String>) {
        packetPairs = lines.chunked(3).map {
            Pair(parseList(it[0], 0).first, parseList(it[1], 0).first)
        }.toList()
    }

    fun isInOrder(left: ListNode, right: ListNode): Boolean? {
        var i = 0

        // Check for using up elements
        while (true) {
            if (i == left.children.size && i == right.children.size) return null
            else if (i == left.children.size) return true
            else if (i == right.children.size) return false

            // Compare children.
            var leftChild = left.children[i]
            var rightChild = right.children[i]
            if (leftChild is IntNode && rightChild is IntNode)  {
                // COOL! Kotlin knows the type now!
                if (leftChild.value < rightChild.value) return true
                if (leftChild.value > rightChild.value) return false
            } else {
                // Convert both to lists
                if (leftChild is IntNode) leftChild = ListNode(listOf<Node>(leftChild))
                if (rightChild is IntNode) rightChild = ListNode(listOf<Node>(rightChild))
                val res = isInOrder(leftChild as ListNode, rightChild as ListNode)
                if (res != null) return res
            }
            i++
        }
    }

    override fun partOne(): Int = packetPairs.withIndex().map {
        if (isInOrder(it.value.first, it.value.second)!!) {
            //println("Match ${it.index + 1}")
            it.index + 1
        } else 0
    }.sum()

    override fun partTwo(): Int {
        val newList = mutableListOf<ListNode>()
        val separator1 = parseList("[[2]]", 0).first
        val separator2 = parseList("[[6]]", 0).first
        newList.add(separator1)
        newList.add(separator2)
        newList.addAll(packetPairs.map { it.toList() }.flatten())
        newList.sortWith(Comparator {x, y -> when(isInOrder(x, y)) {
            true -> -1
            false -> 1
            null -> 0
        }})

        var index1 = newList.indexOf(separator1) + 1
        var index2 = newList.indexOf(separator2) + 1
        println("Separators: $index1 $index2")
        return index1 * index2
    }
}