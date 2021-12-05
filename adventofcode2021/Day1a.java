public class Day1a implements Exercise {
  public void run(java.io.BufferedReader r) throws java.io.IOException {
    int increases = 0;
    int lastValue = -1;

    String line = r.readLine();
    while (line != null) {
      int value = Integer.parseInt(line);
      if (lastValue != -1 && value > lastValue)
        increases++;
      lastValue = value;
      line = r.readLine();
    }
    System.out.println("" + increases);
  }
}
