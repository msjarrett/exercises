public class Day3a implements Exercise {
  public void run(java.io.BufferedReader r) throws java.io.IOException {
    // Count the number of 1's in each position.
    String line = r.readLine();
    int[] onesCount = new int[line.length()];
    int samples = 0;
    while (line != null) {
      samples++;
      for (int i = 0; i < onesCount.length; i++) {
        if (line.charAt(i) == '1')
          onesCount[i]++;
        else if (line.charAt(i) != '0')
          throw new IllegalArgumentException("Invalid bit.");
      }
      line = r.readLine();
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
    System.out.println("" + gamma * epsilon);
  }
}
