import java.io.BufferedReader;

public class Main {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java Main <exercise class> <input file>");
      return;
    }

    try {
      Class<?> exerciseClass = Class.forName(args[0]);
      BufferedReader reader = new BufferedReader(new java.io.FileReader(args[1]));
      Object o = exerciseClass.getConstructor().newInstance();
      if (o instanceof Exercise) {
        Exercise exercise = (Exercise)o;
        System.out.println("" + exercise.run(reader.lines()));
      } else {
        Exercise2 exercise = (Exercise2)o;
        exercise.parse(reader.lines());
        try {
          System.out.println("Part A: " + exercise.partA());
          System.out.println("Part B: " + exercise.partB());
        } catch(UnsupportedOperationException e) {
          // Is ok.
        }
      }
      reader.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
