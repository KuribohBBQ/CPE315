//process instruction
public class processInstruction
{
    public static Instruction processInstruction(String name)
    {
        int Opcode;
        //default funct value since not all instructions use funct
        int funct = -1;
        switch (name) {

            //R-TYPE instructions that have funct
            case "and":
                Opcode = 0x00;
                funct = 0x24;
                break;
            case "or":
                Opcode = 0x00;
                funct = 0x25;
                break;
            case "add":
                Opcode = 0x00;
                funct = 0x20;
                break;
            case "slt":
                Opcode = 0x00;
                funct = 0x2A;
                break;
            case "sll":
                Opcode = 0x00;
                funct = 0x00;
                break;
            case "jr":
                Opcode = 0x00;
                funct = 0x08;
                break;

            //I-TYPE instructions have no funct
            case "addi":
                Opcode = 0x08;
                break;
            case "beq":
                Opcode = 0x04;
                break;
            case "bne":
                Opcode = 0x05;
                break;
            case "lw":
                Opcode = 0x23;
                break;
            case "sw":
                Opcode = 0x2B;
                break;

            //J-TYPE have no funct
            case "j":
                Opcode = 0x02;
                break;
            case "jal":
                Opcode = 0x03;
                break;

            default:
                throw new IllegalArgumentException("Unknown instruction: " + name);
        }

        return new Instruction(name, Opcode, funct);
    }
}