import java.util.regex.*;
import java.util.stream.Collectors;

public class Day5 implements Exercise2 {
  private static class LineSegment {
    private static Pattern linePattern = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");

    public LineSegment(String line) {
      Matcher m = linePattern.matcher(line);
      if (!m.matches())
        throw new IllegalArgumentException("Unmatched line: " + line);
      x1 = Integer.parseInt(m.group(1));
      y1 = Integer.parseInt(m.group(2));
      x2 = Integer.parseInt(m.group(3));
      y2 = Integer.parseInt(m.group(4));
    }

    public String toString() {
      return "{(" + x1 + "," + y1 + ") -> (" + x2 + "," + y2 + ")}";
    }

    public int x1, y1, x2, y2;
  }

  private static class Board {
    public Board(int x, int y) {
      board = new int[x][y];
    }

    public void markSegment(LineSegment s, boolean diagonals) { 
      // Vertical
      if (s.x1 == s.x2) {
        int start = Math.min(s.y1, s.y2);
        int end = Math.max(s.y1, s.y2);
        for (int i = start; i <= end; i++) {
          board[s.x1][i]++;
        }
      }
      // Horizontal
      else if (s.y1 == s.y2) {
        int start = Math.min(s.x1, s.x2);
        int end = Math.max(s.x1, s.x2);
        for (int i = start; i <= end; i++) {
          board[i][s.y1]++;
        }
      }
      // Diagonal
      else if (diagonals) {
        // Can't directionalize, so just explore until we find our destination.
        int x = s.x1, y = s.y1;
        int dx = s.x2 > s.x1 ? 1 : -1;
        int dy = s.y2 > s.y1 ? 1 : -1;
        while (x != s.x2 && y != s.y2) {
          board[x][y]++;
          x += dx;
          y += dy;
        }
        // Don't forget the endpoint
        board[x][y]++;
      }
    }

    public String toString() {
      String line = "";
      for (int y = 0; y < board[0].length; y++) {
        for (int x = 0; x < board.length; x++) {
          line += board[x][y] + " ";
        }
        line += "\n";
      }
      return line;
    }

    public int[][] board;
  }

  public void parse(java.util.stream.Stream<String> lines) {
    segments = lines.map(s -> new LineSegment(s)).collect(Collectors.toList());
    maxX = 0; maxY = 0;
    for (LineSegment s : segments) {
      maxX = Math.max(maxX, s.x1);
      maxX = Math.max(maxX, s.x2);
      maxY = Math.max(maxY, s.y1);
      maxY = Math.max(maxY, s.y2);
    }
    maxX++; maxY++;
  }

  public long partA() {
    return run(false);
  }

  public long partB() {
    return run(true);
  }

  private int run(boolean diagonal) {
    Board b = new Board(maxX, maxY);
    for (LineSegment s : segments) {
      // For 5b, include diagonals.
      b.markSegment(s, diagonal);
    }

    int score = 0;
    for (int x = 0; x < maxX; x++) {
      for (int y = 0; y < maxY; y++) {
        if (b.board[x][y] >= 2)
          score++;
      }
    }

    return score;
  }

  private int maxX, maxY;
  java.util.List<LineSegment> segments;
}
