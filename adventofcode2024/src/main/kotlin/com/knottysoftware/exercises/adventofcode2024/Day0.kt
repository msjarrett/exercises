package com.knottysoftware.exercises.adventofcode2024

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import com.knottysoftware.exercises.adventofcode.common.*

suspend fun Day0(lines: Flow<String>): Any {
    return lines.map {
        it.length
    }.toList().sum()
}