import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class CacheSimulator {
  CacheLine cache[][];
  private long counter;

  private int tagSize;
  private int indexSize;
  private int byteOffsetSize;

  private int numRows;
  private int numWays;

  private int hits;
  private int total;

  public static final int ADDR_SIZE = 32;
  public static final int WORD_SIZE = 4;

  public CacheSimulator(int cacheSize, int numWays, int blocksPerSet) {
    this.numRows = (int) ((cacheSize / (blocksPerSet * WORD_SIZE)) / numWays);
    this.numWays = numWays;
    
    this.byteOffsetSize = (int) (Math.log(blocksPerSet * WORD_SIZE) / Math.log(2));
    this.indexSize = (int) (Math.log(this.numRows) / Math.log(2));
    this.tagSize = ADDR_SIZE - this.indexSize - this.byteOffsetSize;

    this.hits = 0;
    this.total = 0;
    this.counter = 0;

    this.cache = new CacheLine[this.numRows][numWays]; 
    for (int i = 0; i < this.numRows; i++) {
      for (int j = 0; j < numWays; j++) {
        this.cache[i][j] = new CacheLine();
      }
    }
  }

  void runSim(String fname) {
    int tagMask = (1 << this.tagSize) - 1; // creates a mask of tagSize 1's
    int indexMask = (1 << this.indexSize) - 1; // creates a mask of indexSize 1's

    try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
      String line;
      while ((line = reader.readLine()) != null) {
        this.counter++;

        long lruTime = 0;
        int lruWay = 0;

        boolean hit = false;
        boolean emptyInsert = false;

        long addr = Long.parseLong(line.split("\\s+")[1], 16); // use long bc int is 32 bit SIGNED

        int tag = (int) ((addr >> (32 - this.tagSize)) & tagMask); // extract tag
        int index = (int) ((addr >> this.byteOffsetSize) & indexMask); // extract index

        for (int i = 0; i < this.numWays; i++) {
          // stuff 
          hit = this.cache[index][i].checkHit(tag);
          if (hit) {
            this.cache[index][i].setAccessTime(this.counter);
            this.hits++;
            break;
          }
        }
        
        // if miss, fill any way in that index
        if (!hit) {
          if (this.numWays == 1) {
            this.cache[index][0].setTag(tag);
          }
          else {
            for (int i = 0; i < this.numWays; i++) { 
              // find 
              if (i == 0) {
                lruTime = this.cache[index][i].getAccessTime();
                lruWay = 0;
              }
              else {
                // current way is oldest
                if (this.cache[index][i].getAccessTime() < lruTime) {
                  lruTime = this.cache[index][i].getAccessTime();
                  lruWay = i;
                } 
              }

              // don't need to replace LRU if there's an empty spot
              if (this.cache[index][i].getIsEmpty()) {
                this.cache[index][i].setTag(tag);
                this.cache[index][i].setAccessTime(this.counter);
                emptyInsert = true;
                break;
              }
            }

            // otherwise overwrite LRU with new address
            if (!emptyInsert) {
              this.cache[index][lruWay].setTag(tag);
              this.cache[index][lruWay].setAccessTime(this.counter); 
            }
          }
        }

        this.total++;
        hit = false;

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
