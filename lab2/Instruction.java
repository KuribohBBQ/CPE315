public class Instruction
{
    //constructor for instriction
    private String name;

    //Opcode and funct will be in hex 0x
    private int opcode;
    private int funct;

    public Instruction(String name, int opcode, int funct)
    {
        this.name = name;
        this.opcode = opcode;
        this.funct = funct;
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
}
