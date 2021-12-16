import java.util.*;
import java.util.stream.Collectors;

public class Day8 implements Exercise {
  private static class SevenSegment {
    public SevenSegment(String value) {
      segments = value.toCharArray();
      Arrays.sort(segments);
    }

    public char[] segments;

    public String toString() {
      return new String(segments);
    }

    public String map(Map<Character, Character> segmentMap) {
      char[] newSegments = new char[segments.length];
      for (int i = 0; i < segments.length; i++) {
        newSegments[i] = segmentMap.get(segments[i]);
      }
      Arrays.sort(newSegments);
      return new String(newSegments);
    }
  }

  private static class Line {
    public Line(String value) {
      String[] parts = value.split("[|]");
      if (parts.length != 2)
        throw new IllegalArgumentException("Bad line");
      digits = Arrays.stream(parts[0].trim().split(" ")).map(word -> new SevenSegment(word))
        .collect(Collectors.toList());
      if (digits.size() != 10)
        throw new IllegalArgumentException("Bad digits.");
      input = Arrays.stream(parts[1].trim().split(" ")).map(word -> new SevenSegment(word))
        .collect(Collectors.toList());
      if (input.size() != 4)
        throw new IllegalArgumentException("Bad input.");
    }

    public List<SevenSegment> digits;
    public List<SevenSegment> input;
  }

  public Day8() {
    realDigits = new HashMap<>();
    realDigits.put("abcefg", 0);
    realDigits.put("cf", 1);
    realDigits.put("acdeg", 2);
    realDigits.put("acdfg", 3);
    realDigits.put("bcdf", 4);
    realDigits.put("abdfg", 5);
    realDigits.put("abdefg", 6);
    realDigits.put("acf", 7);
    realDigits.put("abcdefg", 8);
    realDigits.put("abcdfg", 9);

    validDigits = realDigits.keySet();
  }

  public int run(java.util.stream.Stream<String> lines) {
    List<Line> input = lines.map(line -> new Line(line)).collect(Collectors.toList());
    return partB(input);
  }

  public int partA(List<Line> input) {
    int c = 0;
    int ln = 0;
    for (Line line : input) {
      for (SevenSegment s : line.input) {
        int len = s.segments.length;
        if (len == 2 || len == 3 || len == 4 || len == 7) {
          c++;
        }
      }
    }
    return c;
  }

  public int partB(List<Line> input) {
    int total = 0;
    for (Line line : input) {
      HashMap<Character, Character> segmentMap =
          recurseSearchMap(line.digits, new HashMap<Character, Character>());
      if (segmentMap == null)
        throw new IllegalStateException("No valid map.");
      int value =
          realDigits.get(line.input.get(0).map(segmentMap)) * 1000
          + realDigits.get(line.input.get(1).map(segmentMap)) * 100
          + realDigits.get(line.input.get(2).map(segmentMap)) * 10
          + realDigits.get(line.input.get(3).map(segmentMap));
      System.out.println(value);
      total += value;
    }

    return total;
  }

  private HashMap<Character, Character> recurseSearchMap(
      List<SevenSegment> digits,
      HashMap<Character, Character> segmentMap) {
    // Is our segmentMap valid?
    if (segmentMap.size() == 7) {
      for (SevenSegment digit : digits) {
        if (!validDigits.contains(digit.map(segmentMap))) {
          return null;
        }
      }
      // VALID!
      return segmentMap;
    }

    // Choose a missing value to try and map.
    char toMap;
    for (toMap = 'a'; toMap <= 'g'; toMap++) {
      if (!segmentMap.containsKey(toMap)) break; 
    }

    for (char toValue = 'a'; toValue <= 'g'; toValue++) {
      if (!segmentMap.containsValue(toValue)) {
        HashMap<Character, Character> newMap = (HashMap<Character, Character>)segmentMap.clone();
        newMap.put(toMap, toValue);
        HashMap<Character, Character> finalMap = recurseSearchMap(digits, newMap);
        if (finalMap != null)
          return finalMap;
      }
    }
    return null; // None of our child solutions worked.
  }

  Map<String, Integer> realDigits;
  Set<String> validDigits;
}
