package com.knottysoftware.exercises.adventofcode2022

public class SearchQueue<T>() : Iterable<T> {
  private val queue = mutableListOf<Pair<T, Int>>()

  constructor(initialValue: T) : this() {
    // Likely the first thing removed, so scoring is irrelevant.
    addDFS(initialValue)
  }

  public override fun iterator(): Iterator<T> {
    return object : Iterator<T> {
      public override fun hasNext() = !queue.isEmpty()
      public override fun next(): T = queue.removeFirst().first
    }
  }

  public fun addBFS(value: T) {
    queue.add(Pair(value, Int.MAX_VALUE))
  }

  public fun addDFS(value: T) {
    queue.add(0, Pair(value, Int.MIN_VALUE))
  }

  public fun addScored(value: T, score: Int) {
    for (i in 0 until queue.size) {
      if (queue[i].second > score) {
        queue.add(i, Pair(value, score))
        return
      }
    }
    queue.add(Pair(value, score))
  }
}
