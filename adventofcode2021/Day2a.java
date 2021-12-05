public class Day2a implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    int depth = 0;
    int pos = 0;

    java.util.Iterator<Day2Command> it = lines.map(line -> new Day2Command(line)).iterator();
    while (it.hasNext()) {
      Day2Command c = it.next();
      switch (c.op) {
        case FORWARD:
          pos += c.distance;
          break;

        case DOWN:
          depth += c.distance;
          break;

        case UP:
          depth -= c.distance;
          break;
      }
    }
    return depth * pos;
  }
}
