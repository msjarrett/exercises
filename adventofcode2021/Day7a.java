public class Day7a implements Exercise {
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
    // 2. Calculate incremental fuel usage from left and right.
    //    left[n] = left[n-1] + sum(crabs[0..n-1]), same for right but other direction.
    //    The fuel cost for any spot is then left[n] + right[n].
    // 3. I suspect the mean (basically center of mass) is actually going to be the most efficient.
    //    Papers online do confirm that mean minimizes squared error, but I can't prove it's the
    //    same for linear error.

    // Approach #2.
    int lastSum = 0;
    int count = 0;
    int[] left = new int[maxHorizontal];
    for (int i = 0; i < maxHorizontal; i++) {
      left[i] = lastSum = lastSum + count;
      count += crabs[i];
    }

    lastSum = 0;
    count = 0;
    int[] right = new int[maxHorizontal];
    for (int i = maxHorizontal - 1; i >= 0; i--) {
      right[i] = lastSum = lastSum + count;
      count += crabs[i];
    }

    int minFuel = Integer.MAX_VALUE;
    for (int i = 0; i < maxHorizontal; i++)
      minFuel = Math.min(minFuel, left[i] + right[i]);

    return minFuel;
  }
}
