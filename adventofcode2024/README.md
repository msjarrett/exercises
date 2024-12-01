# Advent of Code 2024

https://adventofcode.com/2024

I'm going to take it easy this year. Not racing for leaderboard position. Focusing on skiing and
hockey and time with the family this December. But I'll still get a few stars if I can.

New for 2024, I am going to update my development slightly.

Still using Kotlin, but trying with Android Studio instead of IntelliJ. Pretty much the same app
with a different name, but we'll see how it works for non-Android development.

Finally abandoning Gradle, in favor of [Bazel](https://bazel.build/). We use an internal variant
of Bazel at Google, so I expect it to be familiar. But really, anything that can replace Gradle
will just make me happy. Gradle is supposed to be so smart and so automatic, but I spend more time
fixing broken caches or forcing project refreshes when all I want to do is run something.

## Puzzle inputs

Puzzle inputs [are not supposed to be shared](https://adventofcode.com/2024/about) on GitHub.
I've mostly ignored this in the past, but this time, I went to the effort the make a real
downloader. Now puzzle inputs are kept in a local cache. `$HOME/aoc_cache/session` should include
the session token captured from your logged in AOC cookie.

## Other Changes

I stopped with the parse-A-B structure. In 2023, it almost never led to any optimization, and
just made development more complicated.

I stuck with `suspend fun` for now. I'll find a way to use it... one day.