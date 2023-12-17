package com.knottysoftware.exercises.adventofcode2023

import kotlin.time.TimeMark

class SearchQueue<T>() : Iterable<T> {
    companion object {
        private final val timeSource = kotlin.time.TimeSource.Monotonic
    }

    private val queue = mutableListOf<Pair<T, Int>>()
    private var startTime: TimeMark? = null
    private var lastStats: TimeMark? = null
    private var totalVisits: Long = 0
    private var lastVisits: Int = 0

    var doStats = false

    constructor(initialValue: T) : this() {
        // Likely the first thing removed, so scoring is irrelevant.
        addDFS(initialValue)
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            public override fun hasNext() = !queue.isEmpty()
            public override fun next(): T {
                // Timing starts when we remove the first element.
                if (startTime == null) {
                    startTime = timeSource.markNow()
                    lastStats = timeSource.markNow()
                }
                lastVisits++
                totalVisits++

                return queue.removeFirst().first
            }
        }
    }

    fun addBFS(value: T) {
        queue.add(Pair(value, Int.MAX_VALUE))
    }

    fun addDFS(value: T) {
        queue.add(0, Pair(value, Int.MIN_VALUE))
    }

    fun addScored(value: T, score: Int) {
        for (i in 0 ..< queue.size) {
            if (queue[i].second > score) {
                queue.add(i, Pair(value, score))
                return
            }
        }
        queue.add(Pair(value, score))
    }

    private fun doStats() {}
}
