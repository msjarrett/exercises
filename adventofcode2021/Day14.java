import java.util.*;
import java.util.stream.Collectors;

public class Day14 implements Exercise2 {
  private String baseTemplate;
  private Map<String, String> insertions;

  public void parse(java.util.stream.Stream<String> lines) {
    Iterator<String> it = lines.iterator();
    baseTemplate = it.next();
    it.next();
    insertions = new HashMap<String, String>();
    while (it.hasNext()) {
      String[] pairs = it.next().split(" -> ");
      insertions.put(pairs[0], pairs[1]);
    }
  }

  public long partA() {
    return run(10);
  }

  public long partB() {
    return run2(40);
  }

  private static class KeyCounter<T> {
    public void increment(T key, Long c) {
      Long l = counts.get(key);
      counts.put(key, l == null ? c : l + c);
    }
    
    public void decrement(T key, Long c) {
      Long l = counts.get(key);
      if (l == null || l < c)
        throw new IllegalStateException("Negative decrement.");
      counts.put(key, l - c);
    }

    public Map<T, Long> getCounts() {
      return counts;
    }

    public long getCount(T key) {
      return counts.get(key);
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      boolean first = true;
      var sortedKeys = counts.keySet().stream().sorted().collect(Collectors.toList());
      for (var key : sortedKeys) {
        if (first) first = false; else sb.append(" ");
        sb.append(key);
        sb.append('=');
        sb.append(counts.get(key));
      }
      sb.append("}");
      return sb.toString();
    }

    private Map<T, Long> counts = new HashMap<>();
  }

  // PartA implementation which actually generates the template itself.
  private long run(int rounds) {
    StringBuilder template = new StringBuilder(baseTemplate);

    // Steps
    for (int i = 0; i < rounds; i++) {
/*
      // TEMP
      KeyCounter<String> pairCounts = new KeyCounter<>();
      for (int k = 1; k < template.length(); k++) {
        pairCounts.increment(template.substring(k - 1, k + 1), 1L);
      }
      System.out.println(template);
      System.out.println(pairCounts);
      //TEMP
*/
      StringBuilder sb = new StringBuilder();
      sb.append(template.charAt(0));
      for (int j = 1; j < template.length(); j++) {
        String insert = insertions.get(template.substring(j - 1, j + 1));
        if (insert != null)
          sb.append(insert);
        sb.append(template.charAt(j));
      }
      template = sb;
    }

    // Generate counts.
    var counts = new KeyCounter<Character>();
    for (char c : template.toString().toCharArray())
      counts.increment(c, 1L);

    long max = counts.getCounts().values().stream().mapToLong(c -> c).max().getAsLong();
    long min = counts.getCounts().values().stream().mapToLong(c -> c).min().getAsLong();
    return max - min;
  }

  // PartB generates a template trillions of characters long.
  // Rather than track the string, we just count the pairs.
  // You can't generate the character count from the pairs count, but we can count the initial
  // string and we know the delta from each substitution operation.
  private long run2(int rounds) {
    KeyCounter<Character> charCounts = new KeyCounter<>();
    KeyCounter<String> pairCounts = new KeyCounter<>();

    // Generate initial pairs and counts.
    charCounts.increment(baseTemplate.charAt(0), 1L);
    for (int i = 1; i < baseTemplate.length(); i++) {
      charCounts.increment(baseTemplate.charAt(i), 1L);
      pairCounts.increment(baseTemplate.substring(i - 1, i + 1), 1L);
    }

    // Run translation rounds.
    for (int i = 0; i < rounds; i++) {
      //System.out.println(charCounts);
      //System.out.println(pairCounts);
      // We can't modify the map while iterating, so we need a new copy of the pair map each time.
      KeyCounter<String> newPairCounts = new KeyCounter<>();
      for (Map.Entry<String, Long> pairCount : pairCounts.getCounts().entrySet()) {
        String pair = pairCount.getKey();
        long count = pairCount.getValue();
        String insertion = insertions.get(pair);
        // Nothing actually changes unless there's an insertion.
        if (insertion != null) {
          newPairCounts.increment(pair.substring(0, 1) + insertion, count);
          newPairCounts.increment(insertion + pair.substring(1, 2), count);
          charCounts.increment(insertion.charAt(0), count);
        } else {
          newPairCounts.increment(pair, count);
        }
      }
      pairCounts = newPairCounts;
    }

    long max = charCounts.getCounts().values().stream().mapToLong(c -> c).max().getAsLong();
    long min = charCounts.getCounts().values().stream().mapToLong(c -> c).min().getAsLong();
    return max - min;
  }
}
