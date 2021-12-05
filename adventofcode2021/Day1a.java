public class Day1a implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    int increases = 0;
    int lastValue = -1;

    java.util.Iterator<String> it = lines.iterator();
    while (it.hasNext()) {
      int value = Integer.parseInt(it.next());
      if (lastValue != -1 && value > lastValue)
        increases++;
      lastValue = value;
    }
    return increases;
  }
}
