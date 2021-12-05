public class Day1b implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    // Preload everything into an array.
    // Streaming would be nicer, but more complex to maintain trailing window.
    int[] values = lines.mapToInt(s -> Integer.parseInt(s)).toArray();

    int thisSum = 0, lastSum = 0;
    for (int i = 0; i < 3; i++) {
      thisSum += values[i];
      lastSum += values[i];
    }

    int increases = 0;
    for (int i = 3; i < values.length; i++) {
      thisSum -= values[i - 3];
      thisSum += values[i];

      if (thisSum > lastSum)
        increases++;

      lastSum -= values[i - 3];
      lastSum += values[i];
    }

    return increases;
  }
}
