public class Day6 implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    // Each element is the count of fish at that age.
    long[] fish = new long[9];
    java.util.Iterator<String> it = lines.iterator();
    String[] initialFish = it.next().split(",");
    if (it.hasNext())
      throw new IllegalArgumentException("Too many lines.");

    for (String s : initialFish) {
      fish[Integer.parseInt(s)]++;
    }

    // Advance generations: 80 for part A, 256 for part B..
    for (int gen = 0; gen < 256; gen++) {
      long birthers = fish[0];
      for (int i = 0; i < (fish.length - 1); i++) {
        fish[i] = fish[i+1];
      }

      fish[8] = birthers;
      fish[6] = safeAdd(fish[6], birthers);
    }

    // Apparently IntStream will overflow in sum without any cares. Amateurs.
    // return java.util.Arrays.stream(fish).sum();
    long totalFish = 0;
    for (long f : fish) {
      totalFish = safeAdd(totalFish, f);
    }
    System.out.println("Override for long: " + totalFish);
    return 0;
  }

  private static long safeAdd(long a, long b) {
    long result = a + b;
    if (result < a)
      throw new IllegalStateException("Overflow.");
    return result;
  }
}
