import java.util.*;
import java.util.stream.Collectors;

public class Day11 implements Exercise2 {
  private static class Board {
    public Board(List<String> lines) {
      height = lines.size();
      width = lines.get(0).length();
      energy = new int[height][width];
      for (int i = 0; i < energy.length; i++) {
        for (int j = 0; j < lines.get(i).length(); j++) {
          energy[i][j] = lines.get(i).charAt(j) - '0';
        }
      }
    }

    /**
     * Run a step.
     * @return Number of flashes.
     */
    int step() {
      int totalFlashes = 0;
      int currentFlashes = 0;

      // Power up each octopus by 1.
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          energy[i][j]++;
        }
      }

      boolean[][] flashed = new boolean[height][width];

      // Keep iterating until flashes are done.
      do {
        currentFlashes = 0;
        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            if (energy[i][j] > 9 && !flashed[i][j]) {
              currentFlashes++;
              flashed[i][j] = true;

              // Increment neighbors.
              if (i > 0) {
                if (j > 0) energy[i - 1][j - 1]++;            // Up-Left
                energy[i - 1][j]++;                           // Up
                if (j < (width - 1)) energy[i - 1][j + 1]++;  // Up-Right
              }
              {
                if (j > 0) energy[i][j - 1]++;                // Left
                if (j < (width - 1)) energy[i][j + 1]++;      // Right
              }
              if (i < (height - 1)) {
                if (j > 0) energy[i + 1][j - 1]++;            // Down-Left
                energy[i + 1][j]++;                           // Down
                if (j < (width - 1)) energy[i + 1][j + 1]++;  // Down-Right
              }
            }  // if (!flashed)
          }
        }
        totalFlashes += currentFlashes;
      } while (currentFlashes > 0);

      // Reset any flashed.
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          if (energy[i][j] > 9) {
            energy[i][j] = 0;
          }
        }
      }

      return totalFlashes;
    }

    public String toString() {
      String out = "";
      for (int i = 0; i < height; i++) {
        out += Arrays.toString(energy[i]) + "\n";
      }
      return out;
    }

    private int[][] energy;
    private final int width;
    private final int height;
  }

  public void parse(java.util.stream.Stream<String> lines) {
    this.lines = lines.collect(Collectors.toList());
  }

  public long partA() {
    Board board = new Board(lines);
    long score = 0;
    for (int i = 0; i < 100; i++) {
      score += board.step();
    }
    return score;
  }

  public long partB() {
    Board board = new Board(lines);
    int steps = 0;
    while (board.step() != 100) {
      steps++;
    }
    return steps+1;
  }

  private List<String> lines;
}
