import java.util.HashMap;
import java.util.NoSuchElementException;

public class LabelMap {
  HashMap<String, Integer> label_map = new HashMap<String, Integer>();

  // Add a label to the label mappings
  public void addLabel(String label, Integer addr) {
    label_map.put(label, addr);
  }

  // Get the address associated with a label
  public Integer getAddr(String label) {
    // Throw error if label doesn't exist
    if (!label_map.containsKey(label)) {
      throw new NoSuchElementException("Label " + label + "doesn't exist");
    }
    return label_map.get(label);
  }
}
