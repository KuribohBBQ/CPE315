import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Assembler {
    // Constant array used to check if an operation is valid
    private static final String[] SUPPORTED_OPS = {"addi", "add", "and", "sub", "sll", "slt", "beq", "bne", "or", "lw", "sw", "jal", "jr", "j"};

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
                String instrName = "";
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
                
                // Check if operation is supported.
                for (String op : SUPPORTED_OPS) {
                    // Need to check the beginning of the instruction bc there may not be whitespace
                    if (line.startsWith(op)) {
                        instrName = op;
                        line = line.substring(instrName.length()); // grab the rest of line --> operands
                        line = line.trim();
                        break;
                    }
                }

                if (instrName.isEmpty()) {
                    throw new IllegalArgumentException("Operation not supported: " + line);
                }
                
                String[] instrTokens = line.split("\\s+");
                Operands operands = ProcessOperands.processOperands(instrName, instrTokens, pc, labelMap);
                Instruction instr = ProcessInstruction.processInstruction(instrName, operands, pc);
                instrList.add(instr);
                
                pc += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Pass 2
        for (Instruction instr : instrList) {
            int assembledInt = -1;
            String assembledBin = "";

            // assemble R-type instructions
            if (instr.getType() == 'r') { 
                assembledInt = assembleRInst(instr);
            }
            // // assemble I-type instructions
            else if (instr.getType() == 'i') {
                assembledInt = assembleIInst(instr);
            }
            // assemble J-type instructions
            // else if (instr.getType() == 'j') {
                
            // }
            // else {
            //     throw new IllegalArgumentException("Invalid instruction type: " + instr.getType());
            // }

            assembledBin = String.format("%32s", Integer.toBinaryString(assembledInt)).replace(" ", "0");
            System.out.println(assembledBin);
        }
    }

    private static int assembleRInst(Instruction inst) {
        // Format: op (6), rs (5), rt (5), rd (5), shamt (5), funct (6)
        Operands instOperands = inst.getOperands();
        return inst.getOpcode() << 26 |
                instOperands.getRs() << 21 |
                instOperands.getRt() << 16 |
                instOperands.getRd() << 11 |
                instOperands.getShamt() << 6 |
                inst.getFunct();
    }

    private static int assembleIInst(Instruction inst) {
        // Format: op (6), rs (5), rt (5), immediate (16 - 5, 5, 6)
        Operands instOperands = inst.getOperands();
        return inst.getOpcode() << 26 |
                instOperands.getRs() << 21 |
                instOperands.getRt() << 16 |
                instOperands.getImmediate();
    }

    

    public static void main(String[] args) {
        // String fname = "test2.asm";
        // String fname = "test3.asm";
        String fname = "test1.asm";
        assemble(fname);
    }
}