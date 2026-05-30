// Group: Ryan Vu, Euclid Peregrin
// mem_stream.1 takes ~8-9 seconds
// mem_stream.2 takes ~8-9 seconds

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class lab6 {
  
  public static void main(String[] args) {
    if (args.length == 1) {
      String fname = args[0];
      Path filePath = Paths.get(fname);

      if (!Files.exists(filePath)) {
        throw new IllegalArgumentException("No file given or file does not exist");
      }

      CacheSimulator simConfigOne = new CacheSimulator(2048, 1, 1);
      CacheSimulator simConfigTwo = new CacheSimulator(2048, 1, 2);
      CacheSimulator simConfigThree = new CacheSimulator(2048, 1, 4);
      CacheSimulator simConfigFour = new CacheSimulator(2048, 2, 1);
      CacheSimulator simConfigFive = new CacheSimulator(2048, 4, 1);
      CacheSimulator simConfigSix = new CacheSimulator(2048, 4, 4);
      CacheSimulator simConfigSeven = new CacheSimulator(4096, 1, 1);

      System.out.println("Cache #1");
      System.out.println("Cache size: 2048B\tAssociativity: 1\tBlock size: 1");
      simConfigOne.runSim(fname);
      System.out.println("---------------------------");

      System.out.println("Cache #2");
      System.out.println("Cache size: 2048B\tAssociativity: 1\tBlock size: 2");
      simConfigTwo.runSim(fname);
      System.out.println("---------------------------");

      System.out.println("Cache #3");
      System.out.println("Cache size: 2048B\tAssociativity: 1\tBlock size: 4");
      simConfigThree.runSim(fname);
      System.out.println("---------------------------");

      System.out.println("Cache #4");
      System.out.println("Cache size: 2048B\tAssociativity: 2\tBlock size: 1");
      simConfigFour.runSim(fname);
      System.out.println("---------------------------");

      System.out.println("Cache #5");
      System.out.println("Cache size: 2048B\tAssociativity: 4\tBlock size: 1");
      simConfigFive.runSim(fname);
      System.out.println("---------------------------");

      System.out.println("Cache #6");
      System.out.println("Cache size: 2048B\tAssociativity: 4\tBlock size: 4");
      simConfigSix.runSim(fname);
      System.out.println("---------------------------");

      System.out.println("Cache #7");
      System.out.println("Cache size: 4096B\tAssociativity: 1\tBlock size: 1");
      simConfigSeven.runSim(fname);
      System.out.println("---------------------------");
    }
    else {
      throw new IllegalArgumentException("Usage: lab6 mem_stream_file");
    }
  }
}