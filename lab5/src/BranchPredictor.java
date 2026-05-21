public class BranchPredictor {
  private int ghr;
  private int ghrSize;
  private int[] predictor;
  private int totalPreds = 0;
  private int correctPreds = 0;

  public BranchPredictor(int ghrSize) {
    this.ghr = 0;
    this.ghrSize = ghrSize;
    predictor = new int[(int) Math.pow(2, ghrSize)];
  }

  public void updateGHR(int actualPred) {
    this.ghr = (this.ghr << 1) | actualPred;
  }

  public void printGHR() {
    for (int i = ghrSize - 1; i >= 0; i--) {
      int bit = ghr & (1 << i);
      bit = (bit >= 1) ? 1 : 0;
      System.out.printf("%-3d", bit);
    }

    System.out.println();
    
    for (int i = ghrSize - 1; i >= 0; i--) {
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
    System.out.printf("\naccuracy %.2f%% (%d correct predictions, %d predictions)\n\n", acc * 100, this.correctPreds, this.totalPreds);
  }
}
