public class Day2b implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    int depth = 0;
    int pos = 0;
    int aim = 0;

    java.util.Iterator<Day2Command> it = lines.map(line -> new Day2Command(line)).iterator();
    while (it.hasNext()) {
      Day2Command c = it.next();
      switch (c.op) {
        case FORWARD:
          pos += c.distance;
          depth += aim * c.distance;
          break;
        case DOWN:
          aim += c.distance;
          break;
        case UP:
          aim -= c.distance;
          break;
      }
    }
    return depth * pos;
  }
}
