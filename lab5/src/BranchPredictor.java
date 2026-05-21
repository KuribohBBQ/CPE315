/* 
  2-bit predictor:
    - 0 (00): strongly NT
    - 1 (01): weakly NT
    - 2 (10): weakly T
    - 3 (11): strongly T
*/ 
public class BranchPredictor {
  private int ghr;
  private int ghrSize;
  private int[] predictor;
  private int totalPreds = 0;
  private int correctPreds = 0;

  public static final int STRONGLY_NT = 0;
  public static final int WEAKLY_NT = 1;
  public static final int WEAKLY_T = 2;
  public static final int STRONGLY_T = 3;

  public BranchPredictor(int ghrSize) {
    this.ghr = 0;
    this.ghrSize = ghrSize;
    this.predictor = new int[(int) Math.pow(2, ghrSize)];
  }

  // branchTaken represents whether or not the branch was actually taken
  public void updateGHR(boolean branchTaken) {
    int val = branchTaken ? 1 : 0;
    this.ghr = (this.ghr << 1) | val;
    this.ghr &= (int) (Math.pow(2, this.ghrSize) - 1); // clear all non-ghr bits
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

  public int getPred() {
    return this.predictor[this.ghr];
  }

  public void printPred() {
    System.out.printf("Predictor at GHR (%d): %d\n\n", this.ghr, getPred());
  }

  public void updatePred(boolean branchTaken) {
    int newPred = getPred();

    if (branchTaken) {
      if (newPred == STRONGLY_T || newPred == WEAKLY_T) {
        this.correctPreds++;
      }

      // if branch was taken and pred is already strongly taken, stay there
      // otherwise go up one state
      if (newPred == STRONGLY_T) {
        newPred = 3;
      }
      else {
        newPred++;
      }
      
    }
    // if branch was not taken and pred is already strongly not taken, stay there
      // otherwise go down one state
    else {
      if (newPred == STRONGLY_NT || newPred == WEAKLY_NT) {
        this.correctPreds++;
      }

      if (newPred == STRONGLY_NT) {
        newPred = 0;
      }
      else {
        newPred--;
      }
    }

    this.predictor[this.ghr] = newPred;
    this.totalPreds++;
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
