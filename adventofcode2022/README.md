# Advent of Code 2022

https://adventofcode.com/2022

For 2022, I will be focusing exclusively on Kotlin development, because work
reasons. Other than doing a few of the Kotlin Koans, I'm completely new to the
language, so spending most of my time searching class documentation.

I discovered that JetBrains is running a [contest](https://blog.jetbrains.com/kotlin/2022/11/advent-of-code-2022-in-kotlin/)
as well. Maybe I can win something nice...

What I learned about Kotlin for each day:

* Day 1: Classic collection processing.
    * It bothered me that I couldn't find a way to split the lines into
      sublists based on blank lines. JetBrains [solved this](https://youtu.be/ntbsbqLCKDs?t=956)
      by reading the whole file as a String then splitting on "\n\n" but I
      don't like the idea of having to read the entire file at once.
* Day 2: Learned a lot about `when`, and a bit about enums.
* Day 3: [Inline classes](https://kotlinlang.org/docs/inline-classes.html), perfectly designed
  for "Char + more". Some collection subsetting. Also discovered `require`, which is very helpful.
* Day 4: A bit too simple, but used `IntRange`, and learned some of the range
  shortcuts. Some people used `IntRange.intersect`, but this is not
  efficient.
* Day 5: Another case where I wanted to split a list.
  But got to play with `Regex`, and also destructuring (both the regex match
  and data classes), and some more esoteric List manipulation (including
  `buildList` that led me down the "receivers" rabbit hole).
* Day 6: Going to play with arrays, but they don't work with "reified type parameters". Otherwise
  pretty normal algorithm.
* Day 7: Built trees with Kotlin! A few nullables. I was late to start, so I'll have to come back
  and clean up later.
    * I was unhappy with the four regexes checked procedurally. I feel there is some cleaner solution
      with `sealed class` but I'll have to experiment with this later.
    * I was hoping for a more Kotlin-y way to flatten a tree, while not spamming new lists on the
      heap. There seems to be at least a few options to pursue here, and at least I can try to
      work in `tailrec`.
     
      
