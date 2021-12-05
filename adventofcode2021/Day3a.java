public class Day3a implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    // Count the number of 1's in each position.
    int[] onesCount = null;
    int samples = 0;
    java.util.Iterator<String> it = lines.iterator();
    while (it.hasNext()) {
      String line = it.next();
      if (onesCount == null) {
        onesCount = new int[line.length()];
      }
      samples++;
      for (int i = 0; i < onesCount.length; i++) {
        if (line.charAt(i) == '1')
          onesCount[i]++;
        else if (line.charAt(i) != '0')
          throw new IllegalArgumentException("Invalid bit.");
      }
    }

    // Epsilon and gamma are complements for the width of the number only.
    // Could use a bitmask, but easier to read to just calculate epsilon.
    int gamma = 0;
    int epsilon = 0;
    for (int i = 0; i < onesCount.length; i++) {
      gamma <<= 1;
      epsilon <<= 1;
      int zeroes = samples - onesCount[i];
      if (onesCount[i] > zeroes)
        gamma++;
      else if (onesCount[i] < zeroes)
        epsilon++;
      else
        throw new IllegalArgumentException("Ambiguous; equal prevalence of 1 and 0.");
    }
    return gamma * epsilon;
  }
}
