import java.util.List;

public class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x; this.y = y;
    }

    public Point(String line) {
        String[] points = line.split(",");
        x = Integer.parseInt(points[0]);
        y = Integer.parseInt(points[1]);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object o) {
        return o instanceof Point && ((Point)o).x == x && ((Point)o).y == y;
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + x;
        hash = 31 * hash + y;
        return hash;
    }

    public List<Point> adjacentCardinal(int w, int h) {
        List<Point> points = new java.util.ArrayList<>();
        if (x > 0) points.add(new Point(x - 1, y));
        if (x < w - 1) points.add(new Point(x + 1, y));
        if (y > 0) points.add(new Point(x, y - 1));
        if (y < h - 1) points.add(new Point(x, y + 1));
        return points;
    }
}