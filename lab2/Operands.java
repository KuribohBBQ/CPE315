public class Operand {


    private int rs = 0;
    private int rt = 0;
    private int rd = 0;
    private int shamt = 0;
    private int immediate = 0;
    private int target = 0;

    //getter functions
    public int getRs()
    {
        return rs;
    }

    public int getRt()
    {
        return rt;
    }

    public int getRd()
    {
        return rd;
    }

    public int getShamt()
    {
        return shamt;
    }

    public int getImmediate()
    {
        return immediate;
    }

    public int getTarget()
    {
        return target;
    }


    //setter functions
    public void setRs(int rs)
    {
        this.rs = rs;
    }

    public void setRt(int rt)
    {
        this.rt = rt;
    }

    public void setRd(int rd)
    {
        this.rd = rd;
    }

    public void setShamt(int shamt)
    {
        this.shamt = shamt;
    }

    public void setImmediate(int immediate)
    {
        this.immediate = immediate;
    }

    public void setTarget(int target)
    {
        this.target = target;
    }
}