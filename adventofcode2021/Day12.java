import java.util.*;
import java.util.stream.Collectors;

public class Day12 implements Exercise2 {
  public void parse(java.util.stream.Stream<String> lines) {
    edges = new HashMap<String, Set<String>>();
    Iterator<String> it = lines.iterator();
    while (it.hasNext()) {
      String line = it.next();

      String[] pieces = line.split("-");
      // Ensure start is always 0 and end always 1.
      boolean isTerminal = false;
      if (pieces[1].equals("start")) {
        String tmp = pieces[1];
        pieces[1] = pieces[0];
        pieces[0] = tmp;
      }
      if (pieces[0].equals("end")) {
        String tmp = pieces[1];
        pieces[1] = pieces[0];
        pieces[0] = tmp;
      }
      if (pieces[0].equals("start") || pieces[1].equals("end"))
        isTerminal = true;

      // Add described edge..
      if (!edges.containsKey(pieces[0]))
        edges.put(pieces[0], new HashSet<String>());
      edges.get(pieces[0]).add(pieces[1]);

      // Add reverse edge if not start or end.
      if (!isTerminal) {
        if (!edges.containsKey(pieces[1]))
          edges.put(pieces[1], new HashSet<String>());
        edges.get(pieces[1]).add(pieces[0]);
      }
    }
  }

  public long partA() {
    return run(false);
  }

  public long partB() {
    return run(true);
  }

  private long run(boolean partB) {
    List<List<String>> paths = new ArrayList<List<String>>();

    Queue<List<String>> search = new ArrayDeque<List<String>>();
    List<String> path = new ArrayList<String>();
    path.add("start");
    search.add(path);

    while (!search.isEmpty()) {
      path = search.remove();
      String current = path.get(path.size() - 1);
      if (current.equals("end")) {
        paths.add(path);
        continue;
      }

      for (String edge : edges.get(current)) {
        boolean[] revisit = new boolean[1];
        if (isValidEdge(path, edge, partB, revisit)) {
          List<String> newPath = new ArrayList<>(path);
          if (revisit[0]) {
            newPath.add(revisitTag);
          }
          newPath.add(edge);
          search.add(newPath);
        }
      }
    }

    return paths.size();
  }

  private static boolean isLarge(String node) {
    return node.toUpperCase().equals(node);
  }

  private static boolean isValidEdge(List<String> path, String edge, boolean partB,
                                     boolean[] revisit) {
    revisit[0] = false;
    if (isLarge(edge))
      return true;

    if (!path.contains(edge))
      return true;

    if (!partB)
      return false;

    if (path.contains(revisitTag))
      return false;

    revisit[0] = true;
    return true;
  }

  Map<String, Set<String>> edges;
  private static final String revisitTag = "***revisit***";
}
