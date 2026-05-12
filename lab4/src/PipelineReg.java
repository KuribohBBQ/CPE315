public class PipelineReg {
  private Instruction inst;
  private boolean isEmpty;

  public PipelineReg() {
    this.isEmpty = true;
  }

  public boolean getIsEmpty() {
    return this.isEmpty;
  }

  public void setInst(Instruction inst) {
    this.inst = inst;
    this.isEmpty = false;
  }

  public void clearReg() {
    this.inst = null;
    this.isEmpty = true;
  }
}
