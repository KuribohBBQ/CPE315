import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Assembler {
    public static LabelMap doFirstPass(String fname) {
    // public static void doFirstPass(String fname) {
        int curAddr = 0;

        //array to hold instruction
        Instruction[] instructionArray = {};

        LabelMap labelMap = new LabelMap();
        try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Comment present in the line
                if (line.indexOf("#") != -1) {
                    line = line.substring(0, line.indexOf("#"));
                }

                // trim leading and trailing whitespace
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Label exists
                // Two cases:
                    // 1. Only label
                    // 2. Label followed by instruction
                if (line.indexOf(":") != -1) {
                    String[] splitLine = line.split(":", 2);
                    String label = splitLine[0].trim();
                    splitLine[1] = splitLine[1].trim();

                    labelMap.addLabel(label, curAddr);
                    
                    if (splitLine[1].isEmpty()) {
                        continue;
                    }
                }

                // Instruction exists
                curAddr += 4;
            }
            // labelMap.printLabels();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labelMap;
    }

    public static void main(String[] args) {
        // String fname = "test2.asm";
        String fname = "test3.asm";
        // String fname = "temp.asm";
        LabelMap labelMap = doFirstPass(fname);
        labelMap.printLabels();
    }
}