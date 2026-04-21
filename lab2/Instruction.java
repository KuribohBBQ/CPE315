public class Instruction
{
    //constructor for instriction
    private String name;

    //Opcode and funct will be in hex 0x
    private int opcode;
    private int funct;

    private String label;
    
    private Operands operands;

    private int addr;


    public Instruction(String name, int opcode, int funct, Operands operands, int addr)
    {
        this.name = name;
        this.opcode = opcode;
        this.funct = funct;
        this.operands = operands;
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

    public Operands getOperands()
    {
        return operands;
    }

    public int getAddr()
    {
        return addr;
    }

    public void printInstr() {
        System.out.println("===== Instruction Fields =====");
        System.out.println("\t\tname: " + this.name);
        System.out.println("\t\topcode: " + this.opcode);
        System.out.println("\t\tfunct: " + this.funct);
        this.operands.printOperands();
        System.out.println("\t\taddr: " + this.addr);
        System.out.println();
    }
}
