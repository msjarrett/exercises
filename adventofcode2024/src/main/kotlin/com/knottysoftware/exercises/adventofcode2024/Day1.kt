package com.knottysoftware.exercises.adventofcode2024

import kotlinx.coroutines.flow.Flow
import kotlin.math.abs

suspend fun Day1a(lines: Flow<String>): Any {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    lines.collect {
        val items = it.split(" ")
        list1.add(items.first().toInt())
        list2.add(items.last().toInt())
    }
    list1.sort()
    list2.sort()

    var delta = 0
    for (i in 0 ..< list1.size) {
        delta +=  abs(list1[i] - list2[i])
    }

    return delta
}

suspend fun Day1b(lines: Flow<String>): Any {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    lines.collect {
        val items = it.split(" ")
        list1.add(items.first().toInt())
        list2.add(items.last().toInt())
    }

    return list1.map { target ->
        target * list2.count { target == it }
    }.sum()
}