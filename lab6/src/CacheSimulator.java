public class CacheSimulator {
  CacheLine cache[][];

  private int tagSize;
  private int indexSize;
  private int byteOffset;

  private int numRows;
  private int numWays;

  private int hits;
  private int misses;

  public static final int ADDR_SIZE = 32;
  public static final int WORD_SIZE = 4;

  public CacheSimulator(int cacheSize, int numWays, int blocksPerSet) {
    this.numRows = (int) ((cacheSize / (blocksPerSet * WORD_SIZE)) / numWays);
    this.numWays = numWays;
    
    this.byteOffset = (int) (Math.log(blocksPerSet * WORD_SIZE) / Math.log(2));
    this.indexSize = (int) (Math.log(this.numRows) / Math.log(2));
    this.tagSize = ADDR_SIZE - this.indexSize - this.byteOffset;

    this.hits = 0;
    this.misses = 0;

    this.cache = new CacheLine[this.numRows][numWays]; 
    for (int i = 0; i < this.numRows; i++) {
      for (int j = 0; j < numWays; j++) {
        this.cache[i][j] = new CacheLine();
      }
    }

    // debugging
    System.out.printf("tagSize = %d\n", this.tagSize);
    System.out.printf("indexSize = %d\n", this.indexSize);
    System.out.printf("byteOffset = %d\n", this.byteOffset);

    System.out.printf("numRows = %d\n", this.numRows);
    System.out.printf("numWays = %d\n", this.numWays);
  }

  public int getHits() {
    return this.hits;
  }

  public int getMisses() {
    return this.misses;
  }

  public void incHits() {
    this.hits++;
  }

  public void incMisses() {
    this.misses++;
  }
}
