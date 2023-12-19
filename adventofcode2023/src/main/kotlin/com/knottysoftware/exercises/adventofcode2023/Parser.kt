package com.knottysoftware.exercises.adventofcode2023

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun List<String>.splitOnBlank(): List<List<String>> {
    val lists = mutableListOf(mutableListOf<String>())
    for (line in this) {
        if (line == "") {
            lists.add(mutableListOf())
        } else {
            lists.last().add(line)
        }
    }
    return lists
}