import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class lab3 {
    // Constant array used to check if an operation is valid
    private static final String[] SUPPORTED_OPS = {"addi", "add", "and", "sub", "sll", "slt", "beq", "bne", "or", "lw", "sw", "jal", "jr", "j"};

    public static ProgramData assemble(String fname) {
        int pc = 0;

        //array to hold instruction
        List<Instruction> instList = new ArrayList<>();

        LabelMap labelMap = new LabelMap();

        // Pass 1
        try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String label = "";
                String instName = "";
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
                        instName = op;
                        line = line.substring(instName.length()); // grab the rest of line --> operands
                        line = line.trim();
                        break;
                    }
                }

                //unknown instruction
                if (instName.isEmpty()) {
                    //extract the instruction name for printing
                    String badInst = line.split("\\s+")[0];

                    //still add it so pass 2 can stop at the right place
                    Instruction inst = new Instruction(badInst, 0, -1, new Operands(), pc, 'u');
                    instList.add(inst);

                    pc += 1;
                    continue;
                }
                
                String[] instTokens = line.split("\\s+");
                Operands operands = ProcessOperands.processOperands(instName, instTokens, pc, labelMap);
                Instruction inst = ProcessInstruction.processInstruction(instName, operands, pc);
                instList.add(inst);
                
                pc += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ProgramData(instList, labelMap);
    }

    // private static int assembleIInst(Instruction inst, LabelMap labelMap) {
    //     // Format: op (6), rs (5), rt (5), immediate (16 - 5, 5, 6)
    //     Operands instOperands = inst.getOperands();
    //     if (!instOperands.getLabel().isEmpty()) {
    //         int labelAddr = labelMap.getAddr(instOperands.getLabel());
    //         int offset = labelAddr - (instOperands.getTarget() + 1); // relative offset calculated from pc+1
    //         instOperands.setImmediate(offset);
    //     }

    //     int maskedImm = instOperands.getImmediate() & 0xFFFF; // mask the immediate value to 16 bits

    //     return inst.getOpcode() << 26 |
    //             instOperands.getRs() << 21 |
    //             instOperands.getRt() << 16 |
    //             maskedImm;
    // }

    // private static int assembleJInst(Instruction inst, LabelMap labelMap) {
    //     // Format: op (6), address (26 - 5, 5, 5, 5, 6)
    //     Operands instOperands = inst.getOperands();
    //     int labelAddr = labelMap.getAddr(instOperands.getLabel());
    //     return inst.getOpcode() << 26 |
    //             labelAddr;
    // }

    public static void main(String[] args) {
        List<Instruction> instList;
        String fname = args[0];
        Path filePath = Paths.get(fname);

        if (Files.exists(filePath)) {
            // Interactive mode
            if (args.length == 1) {
                ProgramData progData = assemble(args[0]);
                Emulator emulator = new Emulator(progData);
                emulator.executeInteractive();
            }
            // Provide script
            else if (args.length == 2) {
                ProgramData progData = assemble(args[0]);
                Emulator emulator = new Emulator(progData);
                emulator.executeScript(args[1]);
            }
            else {
                throw new IllegalArgumentException("Usage: lab3 assembly_file.asm [script_file]");
            }
        }
        else {
            throw new IllegalArgumentException("No file given or file does not exist");
        }
    }
}
