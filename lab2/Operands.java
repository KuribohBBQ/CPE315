public class Operands {


    private int rs = 0;
    private int rt = 0;
    private int rd = 0;
    private int shamt = 0;
    private int immediate = 0;
    private int target = 0;
    private String label = "";

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

    public String getLabel()
    {
        return label;
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

    public void setLabel(String label)
    {
        this.label = label;
    }

    public void printOperands() {
        System.out.println("=== Operands Fields ===");
        System.out.println("\trs: " + this.rs);
        System.out.println("\trt: " + this.rt);
        System.out.println("\trd: " + this.rd);
        System.out.println("\tshamt: " + this.shamt);
        System.out.println("\timmediate: " + this.immediate);
        System.out.println("\ttarget: " + this.target);
        System.out.println("\tlabel: " + this.label);
    }
}
