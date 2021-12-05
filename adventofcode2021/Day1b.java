public class Day1b implements Exercise {
  public void run(java.io.BufferedReader r) throws java.io.IOException {
    // Preload everything into an array.
    // Streaming would be nicer, but more complex to maintain trailing window.
    java.util.ArrayList<Integer> values = new java.util.ArrayList<Integer>();
    String line = r.readLine();
    while (line != null) {
      values.add(Integer.parseInt(line));
      line = r.readLine();
    }

    int thisSum = 0, lastSum = 0;
    for (int i = 0; i < 3; i++) {
      thisSum += values.get(i);
      lastSum += values.get(i);
    }

    int increases = 0;
    for (int i = 3; i < values.size(); i++) {
      thisSum -= values.get(i - 3);
      thisSum += values.get(i);

      if (thisSum > lastSum)
        increases++;

      lastSum -= values.get(i - 3);
      lastSum += values.get(i);
    }

    System.out.println("" + increases);
  }
}
