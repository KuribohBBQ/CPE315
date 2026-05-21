import java.util.List;

public class ProgramData {
  private List<Instruction> instList;
  private LabelMap labelMap;

  public ProgramData(List<Instruction> instList, LabelMap labelMap) {
    this.instList = instList;
    this.labelMap = labelMap;
  }

  public List<Instruction> getInstList() {
    return instList;
  }

  public LabelMap getLabelMap() {
    return labelMap;
  }
}
