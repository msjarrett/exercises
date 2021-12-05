public class Day2a implements Exercise {
  public void run(java.io.BufferedReader r) throws java.io.IOException {
    int depth = 0;
    int pos = 0;

    String line = r.readLine();
    while (line != null) {
      String[] components = line.split(" ");
      if (components.length != 2) {
        throw new IllegalArgumentException("Invalid line.");
      }
      String command = components[0];
      int distance = Integer.parseInt(components[1]);

      if (command.equals("forward"))
        pos += distance;
      else if (command.equals("down"))
        depth += distance;
      else if (command.equals("up"))
        depth -= distance;
      else
        throw new IllegalArgumentException("Invalid command: " + command);

      line = r.readLine();
    }
    System.out.println("" + (depth * pos));
  }
}
