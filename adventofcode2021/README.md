# Advent of Code 2021

https://adventofcode.com/2021

I think I will do this in Java this year. I happen to need more Java lately.

Some retrospective thoughts.

* I did the first three days at once, and sort of figured out the pattern.
  Always a part1/part2, always a numerical answer. I tried to standardize a
  bit of `Main` infrastructure around this.
* Around Day 3 I discovered `java.util.streams`. I really thought this was
  going to be some magical map-reduce nirvana, and tried to use it everywhere.
  They helped... a bit... in a few places,  but they actually helped a lot less
  than I expected.
* I struggled with the Part 1 / Part 2 split.
  * The first three days, the overlap seemed pretty minor, and it was easier to write classes for each.
  * Day 4 I ended up writing helper classes to support split solutions.
  * By Day 5, I was onto shared classes with tuned constants, and by Day 7 I'm back to split
    classes.
  * After Day8, I ended up creating an `Exercise2` with a partA()/partB().
* Day 6 threw me because they started to overflow ints. Java apparently has no
  cares for that, and I ended up having to dust off my `safeAdd()`. It hasn't
  shown up again, but the paranoia! In Exercise2 I made the return type
  `long`.
* Day 13, I tried Visual Studio Code! 
