# Advent of Code 2022

https://adventofcode.com/2022

For 2022, I will be focusing exclusively on Kotlin development, because work
reasons. Other than doing a few of the Kotlin Koans, I'm completely new to the
language, so spending most of my time searching class documentation.

I discovered that JetBrains is running a [contest](https://blog.jetbrains.com/kotlin/2022/11/advent-of-code-2022-in-kotlin/)
as well. Maybe I can win something nice...

## Retrospective

I finished for the first time this year! It definitely helped to have friends and colleagues
working on it at the same time. Also, I must give credit to my wife, who insisted I go back
and finish Day 19 even after I wanted to give up on it.

Among the days I started on time, my best ranking was 1344 (day 22 pt 2), and my
worst was 16042 (day 6 pt 1). Realistically, there wasn't a lot of chance of me ranking highly:
9pm is prime time for my kids (both young and adult), working with an unfamiliar
language (Kotlin). And honestly, I code more clean than fast - even if a `substring()` will do,
I can't help but reach for the regex.

Kotlin was a great language for the event, and I definitely enjoyed it.

*Kotlin upsides:*

* Collections are great. There's so many great tools to build and transform them in nearly endless
  ways, even while maintaining immutability.
* The functional programming syntax is very expressive! They add a lot of helpful shortcuts that
  allow for very writable and readable code.
* The Kotlin standard library has a lot of the basics built in - I never had to fall back to the JDK
  except for `java.io.File`.
* Kotlin enums have a lot of nice features. Not as nice as Rust though...

*Kotlin Downsides:*

* Integer safety is an absolute mess. They will steadfastly refuse even the most basic
  safe promotions (eg. `longVal == 0`), but have no defense whatsoever against overflow or underflow,
  which Advent of Code frequently tests.
* Null safety is great... until you start using maps or have optional members. My day 21 had 75
  instances of `!!`; it likely would have been beteter if I moved to sealed classes, though that
  may have just replaced `!!` with `as`. The automatic null verification actually made things worse,
  because changes in conditions could quicky generate warnings as the compiler realized some checks
  were not required.
* Variant support. I think this is the intent of sealed classes, but it feels heavyweight
  to define an entire class hierarchy to just access one of two things. The alternative is optional
  fields, but this was the cause of my nullability problems.
* Documentation is present, but can be difficult to use. Methods may have the basic description, doesn't
  always cover the helpful extras or gotchas.

Gradle was a dumpster fire. It was slow (firing up new daemons), buggy (cache corruption),
inconsistent (both in speed and reliability), and inscrutable (intermittently
copying my sources to a new directory). I can't imagine why anyone likes this, for any project.
Definitely going to look into [Bazel](https://bazel.build/) next year, and see if it's better.


## Daily commentary

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
* Day 8: Kotlin Arrays are the WORST! I did a simple 2D array solution to start, but then started
  breaking down into functions and actually using a few lambdas.
* Day 9: I tried to be smart by using an immutable data class. But without enforced usage of
  return values, oops, all the bugs.
* Day 10: Meh. Some lambdas.
* Day 11: This problem was unusually complex! Due to int pairs, I thought I'd get creative with
  value classes. But no, they were ok for `MonkeyId` but a big pain for `Item` since we have to
  do math on them (thus using the fake property). Used `inner` for the first time; nice improvement
  over Java. Definitely realizing I need to improve my infra.
* (adding an app overhaul)
    * Get rid of all the `toLong()` stuff. The interface can use `Any`, then I can return `Int`, `Long`
      or even `String` for some of the problems.
    * Adding a reparse method. This was needed for Day 11, and benefits anything that has a large input
      that could potentially be streamed instead of being held in memory.
    * Update Kotlin version (I updated with sdkman, but Gradle pulls its own). This eliminated some
      `RequireOptIn` warnings.
    * Figured out "resources", rather than hard-coding my local data path.
    * Added a simple regression test for the first few days.
* Day 12: Easy BFS. Mostly just ignored arrays. Got to reuse my earlier `Point`.
* Day 13: All the Kotlin stuff! `sealed class` for nodes, labelled break, a bona fide flatten, a sort,
  type-casting, even a nullable Boolean as a trinary flag. The parse took awhile, but the implementation code was
  beautifully expressive.
* Day 14: Reused `Point` again.
* Got the [ktfmt gradle plugin](https://github.com/cortinico/ktfmt-gradle) installed! It added a lot
  of newlines, and stripped lingering semicolons.
* Day 15: Part 1 was easy enough, but the trivial solution to part B is 16Tb allocation. So went with rows of
  `List<IntRange>`. This STILL OOMs in tests, but generates a solution. `IntRange` is still useless.
  AoC was kinda a jerk for putting input data in the problem statement instead of the input.txt.
* Day 16: Wow the adventure.
    * First, the regexes, great for matching text, but adding plurals was sneaky until I noticed it.
    * Implemented a search of the entire space. This obviously blew up for size.
    * Added a variety of pruning solutions to prevent revisiting nodes before releasing pressure. This was
      enough to get past part one.
    * No amount of pruning made this good enough for part 2. So by doing Dijstra's algorithm from each node,
      I could reduce the graph down from 58 nodes to 16 nodes with costed edges. This was much faster, but
      a BFS even over the reduced graph would run out of memory quickly.
    * DFS could run without taxing memory indefinitely, but would invariably end up low. But after about 16
      minutes of compute, **found an acceptable answer**.
    * Giving up for now.
    * Post-Day 17, went back and added informed search and score filtering. Now down to 2 minutes.
* Day 17: Pretty proud of part 1. Good simulator using existing point logic, properties, and constructors.
  Part 2 is a mess, pretty much every solution I've seen people just find block sizes through informed guessing.
  Even a few days later, most people seem to be looking, fairly adhoc, for the cycle.
* Went back and made a `SearchQueue` to specialize what I've been doing with ArrayDeque.
* Day 18: I kept expecting this to explode into a giant search space. But naive 3d-grid searching worked for both
  parts in trivially short runtimes.
* Day 19: I'll come back to this. I can't get part 1 to run in a reasonable runtime. Greedy is obviously problematic,
  and search appears to explode.
    * Mutability really burned me - `copy()` is shallow even for nested data classes!
    * After Day 20 I got this to work. I'm still not sure how - even without pruning, simply changing the search
      order dramatically improved runtime, which it shouldn't. For part 2 I implemented a really cheap pruning
      which was enough to solve the problem, albeit slowly.
* Day 20: I initially got distracted by Arrays (which are still awful). Then I realized it was a double-linked-list
  problem. Then I struggled with the behavior of the list head, until I realized that they start from the element
  with value 0, which makes the choice of head purely cosmetic. Then it was trivial.
    * Part 2, I only ever apply the key in the final result and during shifts. I do not trust Kotlin int math;
      they will happily overflow or underflow with no warning at all, which seems insane for a modern language.
* Day 21: Conceptually not too bad, a quick reverse dependency graph.
    * For part 2, I created an algebraic solver. There's a bug... somewhere... so I bisected to get the star.
      I'm now trying to find where the bug is, but it's very difficult to find.
* Day 22: Part 1 was easy enough, and I got to experiment with enumeration functions.
  Part 2 was nasty and input-dependent (unless you're solving cube folding in code, which is unlikely). But
  I had some fun with `buildMap` as a result.
* Day 23: pretty mechanical. Not much to say.
* Day 24: Finally a good A* candidate! I initially did part 2 with a single long search for the entire path,
  but then realized that there's no advantage to arriving to an intermediate exit late. I deleted my cool
  three-phase search, and simply ran part 1 three times, cutting my runtime 10x.
* Day 25: VICTORY! One last shot by the lack of safeint, but otherwise an easy enough challenge.
