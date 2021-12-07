import java.util.*;
import java.util.stream.Collectors;

public class Day4b implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    Iterator<String> it = lines.iterator();
    List<Integer> moves = Arrays.stream(it.next().split(",")).map(Integer::valueOf)
        .collect(Collectors.toList());

    ArrayList<Day4Board> boards = new java.util.ArrayList<>();
    while (it.hasNext()) {
      boards.add(new Day4Board(it));
    }

    for (Integer move : moves) {
      Iterator<Day4Board> bit = boards.iterator();
      while(bit.hasNext()) {
        Day4Board b = bit.next();
        b.mark(move);
        if (b.isWon()) {
          if (boards.size() == 1) {
            return boards.get(0).score();
          }
          bit.remove();
        }
      }
    }

    return 0;
  }
}
