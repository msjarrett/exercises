import java.util.*;
import java.util.stream.Collectors;

public class Day4a implements Exercise {
  public int run(java.util.stream.Stream<String> lines) {
    Iterator<String> it = lines.iterator();
    List<Integer> moves = Arrays.stream(it.next().split(",")).map(Integer::valueOf)
        .collect(Collectors.toList());

    ArrayList<Day4Board> boards = new java.util.ArrayList<>();
    while (it.hasNext()) {
      boards.add(new Day4Board(it));
    }

    for (Integer move : moves) {
      int maxScore = 0;
      for (Day4Board b : boards) {
        b.mark(move);
      }
      for (Day4Board b : boards) {
        if (b.isWon()) {
          int score = b.score();
          if (score > maxScore)
            maxScore = score;
        }
      }
      if (maxScore > 0)
        return maxScore;
    }

    return 0;
  }
}
