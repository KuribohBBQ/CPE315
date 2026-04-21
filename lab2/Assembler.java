import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Assembler {
    public static void assemble(String fname) {
        int pc = 0;

        //array to hold instruction
        List<Instruction> instrList = new ArrayList<>();

        LabelMap labelMap = new LabelMap();

        // Pass 1
        try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String label = "";
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
                    label = splitLine[0].trim();
                    line = splitLine[1].trim();

                    labelMap.addLabel(label, pc);
                    
                    if (line.isEmpty()) {
                        continue;
                    }

                }
                // Instruction exists
                line = line.replace(",", " "); // replace all commas with a whitespace
                String[] instrTokens = line.split("\\s+");
                String instrName = instrTokens[0];
                Operands operands = ProcessOperands.processOperands(instrName, Arrays.copyOfRange(instrTokens, 1, instrTokens.length), pc, labelMap);
                Instruction instr = ProcessInstruction.processInstruction(instrName, operands, pc);
                instrList.add(instr);
                
                pc += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Pass 2
        for (Instruction instr : instrList) {
            instr.printInstr();

            // assemble R-type instructions
            if (instr.getType() == 'r') { 
                int assembledInt = assembleRInst(instr);
                String assembledBin = String.format("%32s", Integer.toBinaryString(assembledInt)).replace(" ", "0");
                System.out.println(assembledBin);
            }
            // // assemble I-type instructions
            // else if (instr.getType() == 'i') {

            // }
            // // assemble J-type instructions
            // else if (instr.getType() == 'j') {
                
            // }
            // else {
            //     throw new IllegalArgumentException("Invalid instruction type: " + instr.getType());
            // }
        }
        
    }

    private static int assembleRInst(Instruction inst) {
        // Foramt: op (6), rs (5), rt (5), rd (5), shamt (5), funct (6)
        Operands instOperands = inst.getOperands();
        return inst.getOpcode() << 26 |
                instOperands.getRs() << 21 |
                instOperands.getRt() << 16 |
                instOperands.getRd() << 11 |
                instOperands.getShamt() << 6 |
                inst.getFunct();
    }

    // public int assembleIInst(Instruction inst) {
        // Foramt: op (6), rs (5), rt (5), immediate (16 - 5, 5, 6)
    // }

    public static void main(String[] args) {
        String fname = "test2.asm";
        // String fname = "test3.asm";
        // String fname = "temp.asm";
        assemble(fname);
    }
}