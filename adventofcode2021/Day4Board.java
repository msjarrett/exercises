class Day4Board {
  final int SIZE = 5;
  final int SIZE2 = SIZE * SIZE;

  public Day4Board(java.util.Iterator<String> it) {
    if (it.next().length() > 0)
      throw new IllegalArgumentException("Missing separator");

    spaces = new int[SIZE2];
    marked = new boolean[SIZE2];

    int pos = 0;
    for (int i = 0; i < SIZE; i++) {
      // For some reason split() will create a predecessor entry if the string starts with
      // whitespace. How silly.
      String[] line = it.next().trim().split("\\s+");
      if (line.length != SIZE)
        throw new IllegalArgumentException("Invalid line.");
      for (String val : line) 
        spaces[pos++] = Integer.valueOf(val);
    }
  }

  public void mark(int move) {
    for (int i = 0; i < spaces.length; i++) {
      if (spaces[i] == move) {
        marked[i] = true;
        lastMarked = move;
      }
      // Return? Boards shouldn't have duplicates, but who trusts that?
    }
  }

  public boolean isWon() {
    // Check rows
    for (int i = 0; i < SIZE2; i+= SIZE) {
      boolean miss = false;
      for (int j = i; j < i + SIZE; j++) {
        if (!marked[j]) {
          miss = true;
          break;
        }
      }
      if (!miss)
        return true;
    }

    // Check cols
    for (int i = 0; i < SIZE; i++) {
      boolean miss = false;
      for (int j = i; j < SIZE2; j += SIZE) {
        if (!marked[j]) {
          miss = true;
          break;
        }
      }
      if (!miss)
        return true;
    }

    return false;
  }

  public int score() {
    int score = 0;
    for (int i = 0; i < spaces.length; i++) {
      if (!marked[i])
        score += spaces[i];
    }

    return score * lastMarked;
  }

  public String toString() {
    String board = "Board ";
    String marks = "Marks ";
    for (int i = 0; i < SIZE2; i++) {
      board += spaces[i] + " ";
      marks += (marked[i] ? "+" : "-") + " ";
    }
    return board + marks;
  }

  private int[] spaces;
  private boolean[] marked;
  private int lastMarked = -1;
}
