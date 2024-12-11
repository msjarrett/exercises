# Advent of Code 2024

https://adventofcode.com/2024

I'm going to take it easy this year. Not racing for leaderboard position. Focusing on skiing and
hockey and time with the family this December. But I'll still get a few stars if I can.

Continuing with Kotlin, because it's a fun language for this.

## IDE and Environment

I hate Gradle! It fails the core of any software test: if you push the button to do the primary
thing your software does, it should work. Gradle often doesn't, usually because of some cache
issue.

So, instead I'm adopting [Bazel](https://bazel.build/). We use a variant of this at Google, so it's
familiar to me. I enjoy its explicit nature - rather than trying to configure around some broken
automagic, I just tell Bazel exactly what I want it to do, and it does it, every time.

It was perfect... almost. _Bazel works on Windows, but almost every extension doesn't._ Even the Kotlin
extension, the `kt_jvm_library` works but the `kt_jvm_test` relies on a shell script. Most of the
Maven importers don't work, though `rules_jvm_external` did. Restricted Java rules meant I was stuck
on JUnit4 instead of JUnit5. But even then, I was able to get a working app and test suites.

Instead of IntelliJ this year, I started with Android Studio on Windows. Does it make a difference?
Eh, not much. But, without Gradle, Android Studio didn't want to give me Kotlin SDK autocomplete.
There's a [Bazel plugin](https://ij.bazel.build/), but [Windows is not supported](https://ij.bazel.build/docs/bazel-support.html), again!
**So, I switched to Visual Studio Code**. Sure, there's practically no autocomplete, but the Bazel
plugin worked just fine, and I spent about a week working this way.

Visual Studio Code has another advantage: it can connect to Windows Subsystem for Linux. So
I set up WSL2 Debian. One deb package for `bazelisk`, and a few apt installs later, and my
dev environment is already better than Windows ever was. I can now run all the Bazel rules
I want with minimal issues.

... but I still don't have Kotlin autocomplete. `:'(`

## Puzzle inputs

Puzzle inputs [are not supposed to be shared](https://adventofcode.com/2024/about) on GitHub.
I've mostly ignored this in the past, but this time, I went to the effort the make a real
downloader. Now puzzle inputs are kept in a local cache. `$HOME/aoc_cache/session` should include
the session token captured from the cookie in a logged-in browser session.

## Other Changes

I stopped with the parse-A-B structure. In 2023, it almost never led to any optimization, and
just made development more complicated.

I stuck with `suspend fun` for now. I've never actually leveraged coroutines, but I'm committed to
finding a way to use it... one day.
