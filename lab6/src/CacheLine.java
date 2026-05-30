// Class representing a single row (line) in a cache.
// So an array of these makes up a cache.
public class CacheLine {
  private int tag;
  private boolean isOccupied;

  public CacheLine() {
    this.tag = -1; // initialize to default value
    this.isOccupied = false;
  }

  public int getTag() {
    return this.tag;
  }

  boolean checkHit(int tag) {
    if (this.tag == tag) {
      // addToCache()
      return true;
    }

    return false;
  }

}
