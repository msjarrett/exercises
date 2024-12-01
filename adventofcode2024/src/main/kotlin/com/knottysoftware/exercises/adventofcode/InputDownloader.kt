package com.knottysoftware.exercises.adventofcode

import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

private fun DownloadPuzzleInput(remote: URL, local: Path, session: String) {
    //println("Downloading $remote")
    Files.deleteIfExists(local)
    with (remote.openConnection()) {
        setRequestProperty("Cookie", "session=$session")
        Files.copy(getInputStream(), local)
    }
}

class PuzzleNotReadyException(year: Int, day: Int, cause: Throwable):
    IllegalStateException("Puzzle $year-$day is not ready to download yet", cause)

class SessionException(session: Path, reason: Throwable):
    IllegalArgumentException("Invalid session $session", reason)

fun GetPuzzleInput(year: Int, day: Int, useCached: Boolean = true): Path {
    val homeDir = Paths.get(System.getProperty("user.home"))
    if (!Files.isDirectory(homeDir))
        throw IllegalArgumentException("Can't find home directory: $homeDir")
    val cacheDir = homeDir.resolve("aoc_cache")

    val yearDir = cacheDir.resolve(year.toString())
    Files.createDirectories(yearDir)

    val puzzleInputPath = yearDir.resolve(day.toString())
    if (useCached && Files.isRegularFile(puzzleInputPath)) {
        //println("Cached puzzle!")
    } else {
        val sessionFile = cacheDir.resolve("session")
        try {
            // Stupid powershell makes UTF16 with a BOM.
            val session = Files.readAllLines(sessionFile, StandardCharsets.UTF_16)[0]
            DownloadPuzzleInput(
                URL("https://adventofcode.com/$year/day/$day/input"),
                puzzleInputPath,
                session
            )
        } catch (e: java.nio.file.NoSuchFileException ) {
            throw SessionException(sessionFile, e)
        } catch (e: java.io.FileNotFoundException) {
            throw PuzzleNotReadyException(year, day, e)
        } catch (e: java.io.IOException) {
            // I should use a better exception here.
            if (e.message!!.contains("HTTP response code: 400"))
                throw SessionException(sessionFile, e)
            throw e
        }
    }
    return puzzleInputPath
}

fun main(args: Array<String>) {
    if (args.size != 2)
        throw IllegalArgumentException("Usage: <app> <year> <day>")
    println(GetPuzzleInput(year = args[0].toInt(), day = args[1].toInt()))
}