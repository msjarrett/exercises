import java.util.*;

public class Day15 implements Exercise2 {
  public void parse(java.util.stream.Stream<String> lines) {
    risk = lines
      .map(s -> s.toCharArray())
      .map(ca -> {
          int[] ia = new int[ca.length];
          for (int i = 0; i < ia.length; i++)
            ia[i] = ca[i] - '0';
          return ia;
      })
      .toArray(int[][]::new);

    width = risk[0].length;
    height = risk.length;
  }

  private static class SearchNode {
    public final Point pos;
    public final int cost;

    public SearchNode(Point pos, int cost) {
      this.pos = pos; this.cost = cost;
    }
  };

  public long partA() {
    // I feel there's some sort of dynamic programming solution working back from the solution.
    // But this can also be done as a search.

    // Track the best risk-to-date reaching a node. We can stop searching for suboptimal solutions.
    int[][] bestRisk = new int[height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        bestRisk[y][x] = Integer.MAX_VALUE;
      }
    }

    Queue<SearchNode> search = new PriorityQueue<>((SearchNode o1, SearchNode o2) -> {
      // A* admissible heuristic: cartesian distance to goal.
      int s1 = (width - o1.pos.x) + (height - o1.pos.y);
      int s2 = (width - o2.pos.x) + (height - o2.pos.y);
      return s2 - s1;
    });
    search.add(new SearchNode(new Point(0, 0), 0));
    while (!search.isEmpty()) {
      SearchNode cur = search.remove();
      if (cur.cost >= bestRisk[cur.pos.y][cur.pos.x]) continue; // Found something better in meantime.
      bestRisk[cur.pos.y][cur.pos.x] = cur.cost;

      // First node to reach to end isn't necessarily the best unless we do an informed search.
      // But we don't need to explore further.
      if (cur.pos.x == width - 1 && cur.pos.y == height - 1) continue;

      for (Point p : cur.pos.adjacentCardinal(width, height)) {
        int newCost = cur.cost + risk[p.y][p.x];
        // Don't queue if we already know it's no good.
        if (newCost < bestRisk[p.y][p.x]) {
          search.add(new SearchNode(p, newCost)); 
        }
      }
    }

    return bestRisk[height - 1][width - 1];
  }

  public long partB() {
    // Clearly they're hoping to pressure my computation, but I can still calculate this in 24s naively.
    // A* reduced this to 1s.
    final int exp = 5;
    int[][] newRisk = new int[width * exp][height * exp];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        for (int a = 0; a < exp; a++) {
          for (int b = 0; b < exp; b++) {
            int newCost = risk[y][x] + a + b;
            while (newCost > 9) newCost -= 9;
            newRisk[b * height + y][a * width + x] = newCost;            
          }
        }
      }
    }
    risk = newRisk;
    width *= exp;
    height *= exp;
   /* for (int i = 0; i < height; i++) {
      System.out.println(java.util.Arrays.toString(risk[i]));
    }*/
    return partA();
  }

  private int[][] risk; // risk[y][x]
  private int width;
  private int height;
}
