package com.knottysoftware.exercises.adventofcode2024

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

// Conveniently already named "first" and "second"
private typealias Rule = Pair<Int, Int>
private typealias PageList = List<Int>

private suspend fun parsePageRules(lines: Flow<String>): Pair<List<Rule>, List<PageList>> {
    val rules = mutableListOf<Rule>()
    val pageLists = mutableListOf<PageList>()

    var blankFound = false
    lines.collect {
        if (it.isEmpty()) {
            blankFound = true;
        }
        else if (!blankFound) {
            val r = it.split("|")
            require(r.size == 2)
            rules.add(Pair(r[0].toInt(), r[1].toInt()))
        } else {
            pageLists.add(it.split(",").map { s -> s.toInt()}.toList())
        }
    }
    return Pair(rules, pageLists)
}

private fun isValid(pages: PageList, rules: List<Rule>): Boolean {
    for (after in 1 ..< pages.size) {
        for (before in 0 ..< after) {
            // There must not be a rule that says after|before.
            for (r in rules) {
                if (r.first == pages[after] && r.second == pages[before]) return false
            }
        }
    }
    return true
}

private fun fixOrder(pages: PageList, rules: List<Rule>): PageList {
    val newPages = pages.toMutableList()

    // If two items are out of order, we move the second item to immediately before the first, and restart.
    // Swaps might be more efficient but I don't want to prove it works.
    var swapped = false
    do {
        swapped = false
        for (j in 1 ..< newPages.size) {
            for (i in 0 ..< j) {
                for (r in rules) {
                    if (r.first == newPages[j] && r.second == newPages[i]) {
                        newPages.add(i, newPages.removeAt(j))
                        swapped = true
                        continue
                    }
                }
            }
        }
    } while (swapped)

    println("$pages -> $newPages")
    check(isValid(newPages, rules))
    return newPages
}

suspend fun Day5a(lines: Flow<String>): Any {
    val (rules, pageLists) = parsePageRules(lines)

    return pageLists.map { pages ->
        if (isValid(pages, rules)) pages[pages.size / 2] else 0
    }.sum()
}

suspend fun Day5b(lines: Flow<String>): Any {
    val (rules, pageLists) = parsePageRules(lines)

    return pageLists.map { pages ->
        if (isValid(pages, rules)) 0 else fixOrder(pages, rules)[pages.size / 2]
    }.sum()
}