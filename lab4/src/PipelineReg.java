public class PipelineReg {
  private Instruction inst;
  private boolean isEmpty;

  public PipelineReg() {
    this.inst = null;
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

  public String getInstName() {
    if (this.inst == null) {
      return "empty";
    }
    return this.inst.getName();
  }
}
