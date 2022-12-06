package com.knottysoftware.exercises.adventofcode2022;

class RotatingBuffer<T>(val size: Int) {
    // Next to write
    private var i = 0

    // Items.
    // MutableList since can't Array<> with a non-reified type.
    private val v = mutableListOf<T>()

    public fun add(item: T) {
        if (v.size < size)
            v.add(item)
        else
            v[i++] = item       
        if (i == size) i = 0;
    }

    public fun contains(item: T): Boolean {
        for (i in 0 until v.size) {
            if (v[i] == item) return true;
        }
        return false;
    }
    
    public fun allUnique(): Boolean {
        for (i in (v.size - 1) downTo 0) {
            for (j in (i - 1) downTo 0) {
                if (v[i] == v[j]) return false;
            }
        }
        return true;
    }

    public override fun toString() = v.joinToString()
}

class Day6 : Exercise {
    private lateinit var input: String

    override fun parse(lines: Sequence<String>) {
        input = lines.first()
    }

    private fun uniqueIndex(size: Int): Long {
        // The problem 1-indexes, but the first marker is AFTER that character, so it evens out.
        var i = 0
        val buf = RotatingBuffer<Char>(size)
        for (c in input) {
            i++;
            buf.add(c)
            if (buf.allUnique() && i >= size) break
        }
        println("$buf")
        return i.toLong()
    }

    override fun partOne(): Long = uniqueIndex(4)
    override fun partTwo(): Long = uniqueIndex(14) 
}