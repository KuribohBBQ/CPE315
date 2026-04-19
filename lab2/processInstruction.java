//process instruction
public class processInstruction
{
    public static Instruction processInstruction(String name)
    {
        int opcode;
        //default funct value since not all instructions use funct
        int funct = -1;
        switch (name) {

            //R-TYPE instructions that have funct
            case "and":
                opcode = 0x00;
                funct = 0x24;
                break;
            case "or":
                opcode = 0x00;
                funct = 0x25;
                break;
            case "add":
                opcode = 0x00;
                funct = 0x20;
                break;
            case "slt":
                opcode = 0x00;
                funct = 0x2A;
                break;
            case "sll":
                opcode = 0x00;
                funct = 0x00;
                break;
            case "jr":
                opcode = 0x00;
                funct = 0x08;
                break;

            //I-TYPE instructions have no funct
            case "addi":
                opcode = 0x08;
                break;
            case "beq":
                opcode = 0x04;
                break;
            case "bne":
                opcode = 0x05;
                break;
            case "lw":
                opcode = 0x23;
                break;
            case "sw":
                opcode = 0x2B;
                break;

            //J-TYPE have no funct
            case "j":
                opcode = 0x02;
                break;
            case "jal":
                opcode = 0x03;
                break;

            default:
                throw new IllegalArgumentException("Unknown instruction: " + name);
        }

        return new Instruction(name, opcode, funct);
    }
}