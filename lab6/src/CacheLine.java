// Class representing a single row (line) in a cache.
// So an array of these makes up a cache.
public class CacheLine {
  private int tag;
  private boolean isEmpty;
  private long accessTime;

  public CacheLine() {
    this.tag = -1; // initialize to default value
    this.isEmpty = true;
    this.accessTime = -1;
  }

  public int getTag() {
    return this.tag;
  }

  public void setTag(int tag) {
    this.tag = tag;
    this.isEmpty = false;
  }

  public long getAccessTime() {
    return this.accessTime;
  }

  public void setAccessTime(long accessTime) {
    this.accessTime = accessTime;
  }

  public boolean getIsEmpty() {
    return this.isEmpty;
  }

  boolean checkHit(int tag) {
    if (this.isEmpty == false && this.tag == tag) {
      return true;
    }

    return false;
  }

}
