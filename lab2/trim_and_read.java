import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class trim_and_read {
    public static void main(String[] args) {
        String fpath = "example.asm";

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