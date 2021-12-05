import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day3b implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    // Pre-read everything since we need to stream it twice.
    String[] samples = lines.toArray(String[]::new);

    // The data for o2 and co2 is by definition disjoint, no point processing together.
    int o2rating = getRating(java.util.Arrays.stream(samples), true, 0);
    int co2rating = getRating(java.util.Arrays.stream(samples), false, 0);
    return o2rating * co2rating;
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
