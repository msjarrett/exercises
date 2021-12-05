class Day2Command {
  public enum Op {
    FORWARD,
    DOWN,
    UP
  }

  public Day2Command(String line) {
    String[] components = line.split(" ");
    if (components.length != 2) {
      throw new IllegalArgumentException("Invalid line.");
    }
    if (components[0].equals("forward"))
      op = Op.FORWARD;
    else if (components[0].equals("down"))
      op = Op.DOWN;
    else if (components[0].equals("up"))
      op = Op.UP;
    else
      throw new IllegalArgumentException("Invalid operation.");
    this.distance = Integer.parseInt(components[1]);
  }

  public final Op op;
  public final int distance;
}
