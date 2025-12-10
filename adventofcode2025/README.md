# Advent of Code 2025

<https://adventofcode.com/2025>

My fifth year of Advent of Code! A [slight format shift](https://adventofcode.com/2025/about#faq_num_days) this time: only 12 days, no global leaderboard. I'm looking forward to this, since I
often struggle to continue after Christmas.

**Don't forget to [donate to AoC++](https://adventofcode.com/2025/support)!**

## Rust

The past few years I've been using Kotlin. This year, I am switching to the Rust programming language.

Rust is one of the new hot languages. Compiled, type-safe, with modern features built in. Much like Node, the build and dependency system is
built into the tooling. As someone who grew up with C++, Rust seems like the natural next step. I'm not fully convinced that Rust got the
language syntax right, but I'm willing to spend this year's AoC to find out.

But as much as I'm moving onto Rust, I'm moving OFF of Kotlin.

* Kotlin tooling is terrible! **Gradle just doesn't work half the time.** I ended up moving to Bazel just to get reliable builds, but that
  comes at the cost of many other IDE tools. The Bazel tooling had its own issues - a bunch of half-finished migrations, Windows incompatibilities,
  widely varying support of plugins - but critically at least with Bazel when you tell something to compile and run it actually does it.
* Speaking of IDEs, if you don't use IDEA, it's challenging to get any developer support for Kotlin. Code complete is minimal. Debugging is very hard. However
  using IDEA locks you into a mess of Gradle and a half-dozen different JVMs based on incompatibilites with different parts of the
  IDE.
* I felt like I got all that I was looking for out of Kotlin. I've done it for three years now. I've seen the parts of Kotlin that
  fit well with AoC problems. Yes, functional collection transforms are REALLY good in AoC. Did it. Loved it. Played with some enum
  methods. Indexers. Even tried a few coroutine Flows (though never to much impact). I'm ready to move on.

## AI?

I will not be relying on any LLMs to solve or debug the algorithmic aspects of the puzzles. However I do have [Gemini Code Assist](https://codeassist.google/)
installed to answer Rust questions and help with general menial or structural tasks.
