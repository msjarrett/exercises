public class Day7b implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    java.util.Iterator<String> it = lines.iterator();
    String[] crabList = it.next().split(",");
    if (it.hasNext())
      throw new IllegalArgumentException("Too many lines.");

    // Take the same approach as Day6; convert to a count array.
    int maxHorizontal = 0;
    for (String s : crabList)
      maxHorizontal = Math.max(maxHorizontal, Integer.parseInt(s));
    int[] crabs = new int[++maxHorizontal];
    for (String s : crabList)
      crabs[Integer.parseInt(s)]++;

    // Approach to calculating this?
    // 1. Iterate all horizontal positions. Calculate fuel usage for all crabs left and right.
    // 2. Is there a incremental left/right solution like part a?
    //    left[n] = (1) * crabs[n - 1] + (1 + 2) * crabs [n - 2] + (1 + 2 + 3) * crabs[n - 3] ...
    //    left[n - 1] = (1) * crabs[n - 2] + (1 + 2) * crabs[n - 3] ...
    //    left[n] - left[n - 1]  = 1 * crabs[n - 1] + 2 * crabs[n - 2] + 3 * crabs[n - 3]
    //
    //    ... we still have terms for each int, so it's still O(n^2) like #1, and isn't any more
    //    efficient to calculate.

    // Approach #1.
    // I was worried that they'd int overflow us again, but no, only 94M fuel.
    // Still runs instantly. Apparently it's cheap enough.
    long minFuel = Long.MAX_VALUE;
    for (int i = 0; i < maxHorizontal; i++) {
      long thisFuel = 0;
      // Measure left.
      for (int j = 1; (i - j) >= 0; j++) {
        thisFuel += crabs[i - j] * (j * (j + 1)) / 2;
      }
      // Measure right.
      for (int j = 1; (i + j) < maxHorizontal; j++) {
        thisFuel += crabs[i + j] * (j * (j + 1)) / 2;
      }
      minFuel = Math.min(minFuel, thisFuel);
    }
    System.out.println("Override " + minFuel);

    return (int)minFuel;
  }
}
