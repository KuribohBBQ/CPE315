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
      System.out.printf("%-3d", bit);
    }

    System.out.println();
    
    for (int i = ghr - 1; i >= 0; i--) {
      System.out.printf("%-3d", i);
    }

    System.out.printf("\n\n");
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

  public double calcPredAcc() {
    if (this.totalPreds == 0) {
      return 0;
    }
    
    double acc = (double) this.correctPreds / this.totalPreds;
    return acc;
  }

  public void printPredAcc() {
    double acc = calcPredAcc();
    System.out.printf("accuracy %.2f%% (%d correct predictions, %d predictions)\n\n", acc * 100, this.correctPreds, this.totalPreds);
  }
}
