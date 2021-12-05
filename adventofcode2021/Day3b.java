import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day3b implements Exercise {
  public void run(java.io.BufferedReader r) throws java.io.IOException {
    // Pre-read everything.
    ArrayList<String> samples = new ArrayList<String>();
    String line;
    while ((line = r.readLine()) != null)
      samples.add(line);

    // The data for o2 and co2 is by definition disjoint, no point processing together.
    int o2rating = getRating(samples.stream(), true, 0);
    int co2rating = getRating(samples.stream(), false, 0);
    System.out.println("" + o2rating * co2rating);
  }

  private static int getRating(Stream<String> samples, boolean isMax, int accumulate) {
    // Too much intermediate state to process inline. I think Stream should be possible, but for
    // now, just convert to an array rather than figuring out a stateful processor.
   String[] samplesArray = samples.toArray(String[]::new);
    if (samplesArray.length == 0)
      throw new IllegalArgumentException("Ambiguous input.");

    if (samplesArray.length == 1) {
      // There may be remaining digits to process if we didn't reduce them in prior rounds.
      for (int i = 0; i < samplesArray[0].length(); i++) {
        accumulate <<= 1;
        if (samplesArray[0].charAt(i) == '1')
          accumulate++;
      }
      return accumulate;
    }
    accumulate <<= 1;

    char keep;
    long ones = Arrays.stream(samplesArray).filter(s -> s.charAt(0) == '1').count();
    long zeroes = samplesArray.length - ones;
    if (ones >= zeroes)
      keep = isMax ? '1' : '0';
    else 
      keep = isMax ? '0' : '1';

    if (keep == '1')
      accumulate++;

    Stream<String> reduced = Arrays.stream(samplesArray)
        .filter(s -> s.charAt(0) == keep)
        .map(s -> s.substring(1));
    return getRating(reduced, isMax, accumulate);
  }
}
