public class BranchPredictor {
  private int ghr;
  private int[] predictor;
  private int totalPreds = 0;
  private int correctPreds = 0;

  public BranchPredictor(int ghrSize) {
    this.ghr = ghrSize;
    predictor = new int[(int) Math.pow(2, ghrSize)];
  }

  public void printGHR() {
    for (int i = ghr - 1; i >= 0; i--) {
      int bit = ghr & (1 << i);
      System.out.printf("%d bit  ");
    }
    System.out.println();
  }

  public int getTotalPreds() {
    return this.totalPreds;
  }

  public int getCorrectPreds() {
    return this.correctPreds;
  }

  public void incTotalPreds() {
    this.totalPreds++;
  }

  public void incCorrectPreds() {
    this.correctPreds++;
  }

  public long calcPredAcc() {
    long acc = (long) this.correctPreds / this.totalPreds;
    return acc;
  }

  public void printPredAcc() {
    long acc = calcPredAcc();
    System.out.printf("%.2f", acc);
  }
}
