public class PipelineReg {
  private Instruction inst;
  private boolean isEmpty;
  private boolean isStall;

  public PipelineReg() {
    this.inst = null;
    this.isEmpty = true;
    this.isStall = false;
  }

  public boolean getIsEmpty() {
    return this.isEmpty;
  }

  public void setInst(Instruction inst) {
    this.inst = inst;
    this.isEmpty = false;
    this.isStall = false;
  }

  public Instruction getInst()
  {

    return this.inst;
  }

  public void clearReg() {
    this.inst = null;
    this.isEmpty = true;
    this.isStall = false;
  }


  public void setStall() {
    this.inst = null;
    this.isEmpty = false;
    this.isStall = true;
  }


  public String getInstName() {
    if (this.isStall) {
      return "stall";
    }

    if (this.inst == null) {
      return "empty";
    }
    return this.inst.getName();
  }

  //copies fields from target pipeline register
  public void copyFrom(PipelineReg other) {
    this.inst = other.inst;
    this.isEmpty = other.isEmpty;
    this.isStall = other.isStall;
  }
}
