import java.util.*;
import java.util.stream.Collectors;

public class Day13 implements Exercise2 {
  private static class Fold {
    public enum FoldAxis {
      X,
      Y
    }

    public Fold(FoldAxis axis, int value) {
      this.axis = axis;
      this.value = value;
    }

    public Fold(String line) {
      String[] parts = line.substring(11).split("=");
      if (parts[0].equals("x"))
        axis = FoldAxis.X;
      else if (parts[0].equals("y"))
        axis = FoldAxis.Y;
      else
        throw new IllegalArgumentException("Invalid fold.");
      value = Integer.parseInt(parts[1]);
    }

    public String toString() {
      return "[" + axis + "=" + value + "]";
    }

    public final FoldAxis axis;
    public final int value;
  }

  private Set<Point> points = new HashSet<>();
  private List<Fold> folds = new ArrayList<>();

  public void parse(java.util.stream.Stream<String> lines) {
    Iterator<String> it = lines.iterator();
    boolean isFolds = false;
    while (it.hasNext()) {
      String line = it.next();
      if (line.isEmpty()) {
        isFolds = true;
      } else if (!isFolds) {
        points.add(new Point(line));
      } else {
        folds.add(new Fold(line));
      }
    }
  }

  public long partA() {
    Set<Point> newPoints = fold(points, folds.get(0));
    return newPoints.size();
  }

  public long partB() {
    Set<Point> points = this.points;
    for (Fold f : folds)
      points = fold(points, f);

    int maxX = points.stream().mapToInt(p -> p.x).max().getAsInt();
    int maxY = points.stream().mapToInt(p -> p.y).max().getAsInt();
    boolean[][] grid = new boolean[maxY + 1][maxX + 1];
    for (Point p : points) {
      grid[p.y][p.x] = true;
    }

    for (boolean[] line : grid) {
      char[] lineChars = new char[line.length];
      for (int i = 0; i < line.length; i++) {
        lineChars[i] = line[i] ? '*' : '.';
      }
      System.out.println(new String(lineChars));
    }
    return 0;
  }

  private static Set<Point> fold(Set<Point> points, Fold fold) {
    Set<Point> newPoints = new HashSet<>();
    for (var p : points) {
      Point newP = foldPoint(p, fold);
      // The problem statement doesn't give any guidadance on how to handle folds before the mid-point of the axis.
      // But it also doesn't provide any in the input as far as I can tell, so no way to test the expectation.
      // In theory, it could mean shifting the remaining points, which would change the problem quite a bit.
      if (newP.x < 0 || newP.y < 0)
        throw new IllegalStateException("Before-mid fold unhandled.");
      newPoints.add(newP);
    }
    return newPoints;
  }

  private static Point foldPoint(Point p, Fold f) {
    int value;
    if (f.axis == Fold.FoldAxis.X) value = p.x;
    else value = p.y;

    if (value == f.value)
      throw new IllegalStateException("Point on fold axis.");
    else if (value < f.value)
      return p;

    // The point is reflected - the point's distance d from the axis is applied in the opposite direction.
    // d = v - f
    // v' = f - d = f - v + f = 2f - v
    value = f.value * 2 - value;
    if (f.axis == Fold.FoldAxis.X) return new Point(value, p.y);
    else return new Point(p.x, value);
  }
}
