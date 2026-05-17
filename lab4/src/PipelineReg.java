public class PipelineReg {
  private Instruction inst;
  private boolean isEmpty;
  private boolean isStall;
  private boolean isSquash;
  private boolean branchTaken;
  // private boolean branchTaken;

  public PipelineReg() {
    this.inst = null;
    this.isEmpty = true;
    this.isStall = false;
    this.isSquash = false;
    this.branchTaken = false;
  }

  public boolean getIsEmpty() {
    return this.isEmpty;
  }

  public void setInst(Instruction inst, boolean branchTaken) {
    this.inst = inst;
    this.isEmpty = false;
    this.isStall = false;
    this.branchTaken = branchTaken;
    // if (getInstName().equals("jr") || this.inst.getType() == 'j') {
    //   this.branchTaken = true;
    // }
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
  }


  public void setStall() {
    this.inst = null;
    this.isEmpty = false;
    this.isStall = true;
  }

  public boolean getIsSquash() {
    return this.isSquash;
  }

  public boolean getIsStall() {return this.isStall;}

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
  }

  public boolean getIsBranchTaken() {
    return this.branchTaken;
  }
}
