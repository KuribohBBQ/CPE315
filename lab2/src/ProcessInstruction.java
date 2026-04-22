//process instruction
public class ProcessInstruction
{
    public static Instruction processInstruction(String name, Operands operands, int addr)
    {
        int opcode;
        //default funct value since not all instructions use funct
        int funct = -1;
        char type;
        switch (name) {

            //R-TYPE instructions that have funct
            case "and":
                opcode = 0x00;
                funct = 0x24;
                type = 'r';
                break;
            case "or":
                opcode = 0x00;
                funct = 0x25;
                type = 'r';
                break;
            case "add":
                opcode = 0x00;
                funct = 0x20;
                type = 'r';
                break;
            case "sub":
                opcode = 0x00;
                funct = 0x22;
                type = 'r';
                break;
            case "slt":
                opcode = 0x00;
                funct = 0x2A;
                type = 'r';
                break;
            case "sll":
                opcode = 0x00;
                funct = 0x00;
                type = 'r';
                break;
            case "jr":
                opcode = 0x00;
                funct = 0x08;
                type = 'r';
                break;

            //I-TYPE instructions have no funct
            case "addi":
                opcode = 0x08;
                type = 'i';
                break;
            case "beq":
                opcode = 0x04;
                type = 'i';
                break;
            case "bne":
                opcode = 0x05;
                type = 'i';
                break;
            case "lw":
                opcode = 0x23;
                type = 'i';
                break;
            case "sw":
                opcode = 0x2B;
                type = 'i';
                break;

            //J-TYPE have no funct
            case "j":
                opcode = 0x02;
                type = 'j';
                break;
            case "jal":
                opcode = 0x03;
                type = 'j';
                break;

            default:
                opcode = 0x00;
                //u is for UNKNOWN
                type = 'u';
//                throw new IllegalArgumentException("Unknown instruction: " + name);
        }

        return new Instruction(name, opcode, funct, operands, addr, type);
    }
}
