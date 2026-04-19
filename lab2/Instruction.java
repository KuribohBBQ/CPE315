import java.util.Arrays;

public class Instruction
{
    //constructor for instriction
    private String name;

    //Opcode and funct will be in hex 0x
    private int opcode;
    private int funct;
    
    private String[] operands;

    // Label the instruction belongs to
    private String label;

    private int addr;


    public Instruction(String name, int opcode, int funct, String[] operands, String label, int addr)
    {
        this.name = name;
        this.opcode = opcode;
        this.funct = funct;
        this.operands = operands;
        this.label = label;
        this.addr = addr;
    }

    public String getName()
    {
        return name;
    }


    public int getOpcode()
    {
        return opcode;
    }

    public int getFunct()
    {
        return funct;
    }

    public String[] getOperands()
    {
        return operands;
    }

    public String getLabel()
    {
        return label;
    }

    public int getAddr()
    {
        return addr;
    }

    public void printInstr() {
        System.out.println("name: " + this.name);
        System.out.println("opcode: " + this.opcode);
        System.out.println("funct: " + this.funct);
        System.out.println("operands: " + Arrays.toString(this.operands));
        System.out.println("label: " + this.label);
        System.out.println("addr: " + this.addr);
        System.out.println();
    }
}
