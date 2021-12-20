import java.util.*;
import java.util.stream.Collectors;

public class Day10 implements Exercise2 {
  private static final Map<Character, Character> closers = initCloserForOpener();
  private static Map<Character, Character> initCloserForOpener() {
    Map<Character, Character> map = new HashMap<>();
    map.put('(', ')');
    map.put('[', ']');
    map.put('{', '}');
    map.put('<', '>');
    return map;
  }

  public void parse(java.util.stream.Stream<String> lines) {
    this.lines = lines.collect(Collectors.toList());
  }

  public long partA() {
    long score = 0;
    for (String line : lines) {
      Deque<Character> blockStack = new ArrayDeque<>();
      char[] chars = line.toCharArray();
      for (char c : chars) {
        if (closers.containsKey(c)) {
          blockStack.push(closers.get(c));
        } else {
          char expected = blockStack.pop();
          if (expected != c) {
            // Corrupted line
            switch (c) {
              case ')': score += 3; break;
              case ']': score += 57; break;
              case '}': score += 1197; break;
              case '>': score += 25137; break;
            }
            break;
          }
        }
      } // chars
    } // lines
    return score;
  }

  public long partB() {
    var scores = new ArrayList<Long>();
    for (String line : lines) {
      Deque<Character> blockStack = new ArrayDeque<>();
      char[] chars = line.toCharArray();
      boolean corrupt = false;
      for (char c : chars) {
        if (closers.containsKey(c)) {
          blockStack.push(closers.get(c));
        } else {
          char expected = blockStack.pop();
          if (expected != c) {
            // Corrupted line
            corrupt = true;
            break;
          }
        }
      } // chars
      if (!corrupt) {
        // Incomplete line.
        long score = 0;
        while (!blockStack.isEmpty()) {
          char c = blockStack.pop();
          score *= 5;
          switch (c) {
            case ')': score += 1; break;
            case ']': score += 2; break;
            case '}': score += 3; break;
            case '>': score += 4; break;
          }
        }
        scores.add(score);
      }
    } // lines
    scores.sort(null);
    return scores.get(scores.size() / 2);
  }

  List<String> lines;
}
