public class PipelineReg {
  private Instruction inst;
  private boolean isEmpty;
  private boolean isStall;
  private boolean isSquash;
  private boolean branchTaken;
  private int branchAddr;
  private int pc;
  // private boolean branchTaken;

  public PipelineReg() {
    this.inst = null;
    this.isEmpty = true;
    this.isStall = false;
    this.isSquash = false;
    this.branchTaken = false;
    this.branchAddr = -1;
    this.pc = -1;
  }

  public boolean getIsEmpty() {
    return this.isEmpty;
  }

  public void setInst(Instruction inst, boolean branchTaken, int branchAddr, int pc) {
    this.inst = inst;
    this.isEmpty = false;
    this.isStall = false;
    this.branchTaken = branchTaken;
    this.branchAddr = branchAddr;
    this.pc = pc;

  }

  public Instruction getInst()
  {
    return this.inst;
  }

  public int getPc(){
    return this.pc;
  }

  public void clearReg() {
    this.inst = null;
    this.isEmpty = true;
    this.isStall = false;
    this.isSquash = false;
    this.branchTaken = false;
    this.branchAddr = -1;
    this.pc = -1;
  }


  public void setStall() {
    this.inst = null;
    this.isEmpty = false;
    this.isStall = true;
    this.pc = -1;
    this.branchTaken = false;
    this.branchAddr = -1;
  }

  public boolean getIsSquash() {
    return this.isSquash;
  }

  public boolean getIsStall() {return this.isStall;}

  public void setSquash() {
    this.inst = null;
    this.isEmpty = false;
    this.isSquash = true;
    this.pc = -1;
    this.branchTaken = false;
    this.branchAddr = -1;
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
    this.pc = other.pc;
  }

  public boolean getIsBranchTaken() {
    return this.branchTaken;
  }

  public int getBranchAddr() {
    return this.branchAddr;
  }

  public void setBranchInfo(boolean branchTaken, int branchAddr) {
    this.branchTaken = branchTaken;
    this.branchAddr = branchAddr;
  }
}
