import java.io.BufferedReader;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java Main <exercise class> <input file>");
      return;
    }

    try {
      Class<?> exerciseClass = Class.forName(args[0]);
      BufferedReader reader = new BufferedReader(new java.io.FileReader(args[1]));
      Exercise exercise = (Exercise)exerciseClass.getConstructor().newInstance();
      int result = exercise.run(reader.lines());
      System.out.println("" + result);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
