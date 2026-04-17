import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class LabelMap {
  HashMap<String, Integer> labelMap = new HashMap<String, Integer>();

  // Add a label to the label mappings
  public void addLabel(String label, Integer addr) {
    labelMap.put(label, addr);
  }

  // Get the address associated with a label
  public Integer getAddr(String label) {
    // Throw error if label doesn't exist
    if (!labelMap.containsKey(label)) {
      throw new NoSuchElementException("Label " + label + "doesn't exist");
    }
    return labelMap.get(label);
  }

  public void printLabels() {
    labelMap.forEach((label, addr) -> System.out.println(label + ": " + addr));
  }
}
