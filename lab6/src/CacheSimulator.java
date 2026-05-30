import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class CacheSimulator {
  CacheLine cache[][];

  private int tagSize;
  private int indexSize;
  private int byteOffsetSize;

  private int numRows;
  private int numWays;

  private int hits;
  private int total;

  public static final int ADDR_SIZE = 32;
  public static final int WORD_SIZE = 4;
  public static final int BIT_THREE = 4;
  public static final int BIT_THREE_FOUR = 12;

  public CacheSimulator(int cacheSize, int numWays, int blocksPerSet) {
    this.numRows = (int) ((cacheSize / (blocksPerSet * WORD_SIZE)) / numWays);
    this.numWays = numWays;
    
    this.byteOffsetSize = (int) (Math.log(blocksPerSet * WORD_SIZE) / Math.log(2));
    this.indexSize = (int) (Math.log(this.numRows) / Math.log(2));
    this.tagSize = ADDR_SIZE - this.indexSize - this.byteOffsetSize;

    this.hits = 0;
    this.total = 0;

    this.cache = new CacheLine[this.numRows][numWays]; 
    for (int i = 0; i < this.numRows; i++) {
      for (int j = 0; j < numWays; j++) {
        this.cache[i][j] = new CacheLine();
      }
    }

    // debugging
    System.out.printf("tagSize = %d\n", this.tagSize);
    System.out.printf("indexSize = %d\n", this.indexSize);
    System.out.printf("byteOffset = %d\n", this.byteOffsetSize);

    System.out.printf("numRows = %d\n", this.numRows);
    System.out.printf("numWays = %d\n", this.numWays);
  }

  void runSim(String fname) {
    int tagMask = (1 << this.tagSize) - 1; // creates a mask of tagSize 1's
    int indexMask = (1 << this.indexSize) - 1; // creates a mask of indexSize 1's
    int byteOffsetMask = (1 << this.byteOffsetSize) - 1; // creates a mask of byteOffset 1's
    int wordOffset = 0;

    
    try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
      String line;
      while ((line = reader.readLine()) != null) {
        boolean hit = false;
        long addr = Long.parseLong(line.split("\\s+")[1], 16); // use long bc int is 32 bit SIGNED

        int tag = (int) ((addr >> (32 - this.tagSize)) & tagMask); // extract tag
        int index = (int) ((addr >> this.byteOffsetSize) & indexMask); // extract index
        int byteOffset = (int) (addr & byteOffsetMask); // extract byte offset
        
        // in the case where there is more than 1-word blocks, the upper bit(s) are used as a word offset
        if (this.byteOffsetSize == 3) {
          wordOffset = BIT_THREE & byteOffset; // extract the single bit used for the word offset
        }
        else if (this.byteOffsetSize == 4) {
          wordOffset = BIT_THREE_FOUR & byteOffset; // extract the two bits used for the word offset
        }
        
        for (int i = 0; i < this.numWays; i++) {
          // stuff 
          hit = this.cache[index][i].checkHit(tag);
          if (hit) {
            this.hits++;
          }
        }
        
        this.total++;
        hit = false;


        break;
      }
    }
   catch (IOException e) {
      e.printStackTrace();
    }

    printHitRate();

  }

  public int getHits() {
    return this.hits;
  }

  public int getTotal() {
    return this.total;
  }

  public void incHits() {
    this.hits++;
  }

  public void incTotal() {
    this.total++;
  }

  public void printHitRate() {
    double hitRate;
    if (this.total == 0) {
      hitRate = 0;
    }
    else {
      hitRate = (double) this.hits / this.total;
    }

    System.out.printf("Hits: %d Hit Rate: %.2f%%\n", this.hits, hitRate * 100);
  }
}
