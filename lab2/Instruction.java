public class Instruction
{
    //constructor for instriction
    private String name;

    //Opcode and funct will be in hex 0x
    private int Opcode;
    private int funct;

    public Instruction(String name, int Opcode, int funct)
    {
        this.name = name;
        this.Opcode = Opcode;
        this.funct = funct;
    }

    public String getName()
    {
        return name;
    }


    public int getOpcode()
    {
        return Opcode;
    }

    public int getFunct()
    {
        return funct;
    }
}
