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
                //trim leading and trailing whitespace
                line = line.trim();
                //process each character
                for (int i = 0; i < line.length(); i++) {
                    char ch = line.charAt(i);
                    //skip # since it is a comment
                    if (ch == '#') {
                        break;
                    }
                    System.out.print(ch);
                }

                //move to next line after printing
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}