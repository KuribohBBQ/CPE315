// Class representing a single row (line) in a cache.
// So an array of these makes up a cache.
public class CacheLine {
  private int tag;
  private boolean dirtyBit; // used for associativity

  public CacheLine() {
    this.tag = -1; // initialize to default value
    this.dirtyBit = false;
  }

  public int getTag() {
    return this.tag;
  }

}
