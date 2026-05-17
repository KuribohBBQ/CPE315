public class PipelineReg {
  private Instruction inst;
  private boolean isEmpty;
  private boolean isStall;
  private boolean isSquash;
  private boolean branchTaken;
  private int branchAddr;
  // private boolean branchTaken;

  public PipelineReg() {
    this.inst = null;
    this.isEmpty = true;
    this.isStall = false;
    this.isSquash = false;
    this.branchTaken = false;
    this.branchAddr = -1;
  }

  public boolean getIsEmpty() {
    return this.isEmpty;
  }

  public void setInst(Instruction inst, boolean branchTaken, int branchAddr) {
    this.inst = inst;
    this.isEmpty = false;
    this.isStall = false;
    this.branchTaken = branchTaken;
    this.branchAddr = branchAddr;
  }

  public Instruction getInst()
  {
    return this.inst;
  }

  public void clearReg() {
    this.inst = null;
    this.isEmpty = true;
    this.isStall = false;
    this.isSquash = false;
    this.branchTaken = false;
    this.branchAddr = -1;
  }


  public void setStall() {
    this.inst = null;
    this.isEmpty = false;
    this.isStall = true;
  }

  public boolean getIsSquash() {
    return this.isSquash;
  }

  public void setSquash() {
    this.inst = null;
    this.isEmpty = false;
    this.isSquash = true;
  }


  public String getInstName() {
    if (this.isStall) {
      return "stall";
    }

    if (this.isSquash) {
      return "squash";
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
    this.isSquash = other.isSquash;
    this.branchTaken = other.branchTaken;
    this.branchAddr = other.branchAddr;
  }

  public boolean getIsBranchTaken() {
    return this.branchTaken;
  }

  public int getBranchAddr() {
    return this.branchAddr;
  }
}
