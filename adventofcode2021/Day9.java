import java.util.*;
import java.util.stream.Collectors;

public class Day9 implements Exercise2 {
  // First coord = row, second coord = col.
  private char[][] heightMap;
  private int rows;
  private int cols;

  public void parse(java.util.stream.Stream<String> lines) {
    heightMap = lines.map(line -> line.toCharArray()).collect(Collectors.toList())
        .toArray(new char[0][0]);
    rows = heightMap.length;
    cols = heightMap[0].length;
  }

  public long partA() {
    long score = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        char cur = heightMap[i][j];
        char left, right, up, down;
        // When looking for low points, edges are infinitely high.
        left = right = up = down = '0' + 10;

        if (i > 0) up = heightMap[i - 1][j];
        if (i < (rows - 1)) down = heightMap[i + 1][j];
        if (j > 0) left = heightMap[i][j - 1];
        if (j < (cols - 1)) right = heightMap[i][j + 1];

        if (cur < left && cur < right && cur < up && cur < down) {
          // This is a low point.
          int risk = (cur - '0') + 1;
          score += risk;
        }
      }
    }
    return score;
  }

  public long partB() {
    ArrayList<Integer> basinSizes = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        char cur = heightMap[i][j];
        char left, right, up, down;
        // When looking for low points, edges are infinitely high.
        left = right = up = down = '0' + 10;

        if (i > 0) up = heightMap[i - 1][j];
        if (i < (rows - 1)) down = heightMap[i + 1][j];
        if (j > 0) left = heightMap[i][j - 1];
        if (j < (cols - 1)) right = heightMap[i][j + 1];

        if (cur < left && cur < right && cur < up && cur < down) {
          // This is a low point.
          basinSizes.add(exploreBasin(i, j));
        }
      }
    }
    basinSizes.sort(null);
    return basinSizes.get(basinSizes.size() - 1) * basinSizes.get(basinSizes.size() - 2)
        * basinSizes.get(basinSizes.size() - 3);
  }

  private static class Point {
    public Point(int i, int j) {
      this.i = i;
      this.j = j;
    }

    public int i, j;
  }

  int exploreBasin(int i, int j) {
    int points = 0;
    boolean[][] explored = new boolean[rows][cols];
    Queue<Point> search = new ArrayDeque<>();
    search.add(new Point(i, j));

    while (!search.isEmpty()) {
      Point p = search.remove();

      // This point may have already been explored since it was added.
      if (explored[p.i][p.j]) continue;

      // We checked all other validity before queueing.
      points++;
      explored[p.i][p.j] = true;

      ArrayList<Point> toExplore = new ArrayList<>();
      if (p.i > 0) toExplore.add(new Point(p.i - 1, p.j));
      if (p.i < (rows - 1)) toExplore.add(new Point(p.i + 1, p.j));
      if (p.j > 0) toExplore.add(new Point(p.i, p.j - 1));
      if (p.j < (cols - 1)) toExplore.add(new Point(p.i, p.j + 1));

      for (Point p2 : toExplore) {
        if (explored[p2.i][p2.j]) continue;
        if (heightMap[p2.i][p2.j] == '9') continue;
        if (heightMap[p2.i][p2.j] <= heightMap[p.i][p.j]) continue;
        search.add(p2);
      }
    }
    return points;
  }
}
