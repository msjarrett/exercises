package com.knottysoftware.exercises.adventofcode2022;

class Day7 : Exercise {
    private data class Directory(val name: String, val parent: Directory?) {
        val subdirs = mutableListOf<Directory>()
        val files = mutableListOf<Pair<String, Long>>()
        var transitiveSize: Long = 0
    }

    private fun flatten(root: Directory, list: MutableList<Directory>) {
        list.add(root)
        for (dir in root.subdirs) {
            flatten(dir, list)
        }
    }

    private val root = Directory("/", null)
    private var pwd = root

    private final val cdRegex = Regex("""\$ cd ([a-z/.]+)""")
    private final val lsRegex = Regex("""\$ ls""")
    private final val fileRegex = Regex("""(\d+) ([a-z.]+)""")
    private final val dirRegex = Regex("""dir ([a-z.]+)""")

    override fun parse(lines: Sequence<String>) {
        var lastCommand: String
        var thisCommand: String = ""
        var match: MatchResult?

        for (line in lines) {           
            lastCommand = thisCommand

            match = cdRegex.matchEntire(line)
            if (match != null) {
                thisCommand = "cd"
                val dir = match.groupValues[1]
                if (dir == "/") pwd = root
                else if (dir == "..") pwd = pwd.parent!!
                else pwd = pwd.subdirs.first { it.name == dir }
                continue
            }

            match = lsRegex.matchEntire(line)
            if (match != null) {
                thisCommand = "ls"
                require(lastCommand == "cd")
                continue
            }

            match = fileRegex.matchEntire(line)
            if (match != null) {
                require(lastCommand == "ls")
                val size = match.groupValues[1].toLong()
                pwd.files.add(Pair(match.groupValues[2], size))
                var localPwd: Directory? = pwd
                while (localPwd != null) {
                    localPwd.transitiveSize += size
                    localPwd = localPwd.parent
                }
                continue
            }

            match = dirRegex.matchEntire(line)
            if (match != null) {
                require(lastCommand == "ls")
                pwd.subdirs.add(Directory(match.groupValues[1], pwd))
                continue
            }
            require(false) // No regex matched
        }
    }

    override fun partOne(): Long {
        val dirs = mutableListOf<Directory>()
        flatten(root, dirs)
        return dirs.map { it.transitiveSize }.filter { it <= 100000 }.sum()
    }

    override fun partTwo(): Long {
        // Find smallest directory that would reduce total size to <= 40,000,000
        val minNeeded = root.transitiveSize - 40000000
        val dirs = mutableListOf<Directory>()
        flatten(root, dirs)
        return dirs.map { it.transitiveSize }.filter { it >= minNeeded }.minOrNull()!!
    }
}