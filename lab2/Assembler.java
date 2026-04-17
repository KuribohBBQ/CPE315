import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class Assembler {
    public LabelMap doFirstPass(List<String> lines) {
        LabelMap labelMap = new LabelMap();
    
        return labelMap;
    }

    public static void main(String[] args) {
        String fpath = "test2.asm";

        try (BufferedReader reader = new BufferedReader(new FileReader(fpath))) {
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

                // Move to next line after printing
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}