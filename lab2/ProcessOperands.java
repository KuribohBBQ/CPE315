public class ProcessOperands {

    public static Operands processOperands(
            String name, String[] operands, int addr, LabelMap labelMap) {

        Operands ops = new Operands();

        switch (name) {
            case "add":
                ops.setRd(registerNum(operands[0]));
                ops.setRs(registerNum(operands[1]));
                ops.setRt(registerNum(operands[2]));
                break;
            case "and":
                ops.setRd(registerNum(operands[0]));
                ops.setRs(registerNum(operands[1]));
                ops.setRt(registerNum(operands[2]));
                break;
            case "sub":
                ops.setRd(registerNum(operands[0]));
                ops.setRs(registerNum(operands[1]));
                ops.setRt(registerNum(operands[2]));
                break;
            case "or":
                ops.setRd(registerNum(operands[0]));
                ops.setRs(registerNum(operands[1]));
                ops.setRt(registerNum(operands[2]));
                break;
            case "slt":
                ops.setRd(registerNum(operands[0]));
                ops.setRs(registerNum(operands[1]));
                ops.setRt(registerNum(operands[2]));
                break;

            case "sll":
                ops.setRd(registerNum(operands[0]));
                ops.setRt(registerNum(operands[1]));
                ops.setShamt(Integer.parseInt(operands[2]));
                break;

            case "addi":
                ops.setRt(registerNum(operands[0]));
                ops.setRs(registerNum(operands[1]));
                ops.setImmediate(Integer.decode(operands[2]));
                break;

            case "beq":
                ops.setRs(registerNum(operands[0]));
                ops.setRt(registerNum(operands[1]));
                ops.setLabel(operands[2]);
                break;
            case "bne":
                ops.setRs(registerNum(operands[0]));
                ops.setRt(registerNum(operands[1]));
                ops.setLabel(operands[2]);
                break;

            case "lw":
                break;
            case "sw":
                ops.setRt(registerNum(operands[0]));
                //extract value from between parenthesis
                String mem = operands[1];   // 8($a1)
                int left = mem.indexOf('(');
                int right = mem.indexOf(')');
                int offsetVal = Integer.parseInt(mem.substring(0, left));
                String baseReg = mem.substring(left + 1, right);
                ops.setRs(registerNum(baseReg));
                ops.setImmediate(offsetVal);
                break;

            case "j":
                ops.setLabel(operands[0]);
                break;
            case "jr":
                ops.setRs(registerNum(operands[0]));
                break;
            case "jal":
                ops.setLabel(operands[0]);
                break;

            default:
                throw new IllegalArgumentException("Unknown instruction: " + name);
        }

        return ops;
    }

    public static int registerNum(String register) {
        switch (register) {
            case "$zero":
                return 0;
            case "$0":
                return 0;
            case "$v0":
                return 2;
            case "$v1":
                return 3;
            case "$a0":
                return 4;
            case "$a1":
                return 5;
            case "$a2":
                return 6;
            case "$a3":
                return 7;
            case "$t0":
                return 8;
            case "$t1":
                return 9;
            case "$t2":
                return 10;
            case "$t3":
                return 11;
            case "$t4":
                return 12;
            case "$t5":
                return 13;
            case "$t6":
                return 14;
            case "$t7":
                return 15;
            case "$s0":
                return 16;
            case "$s1":
                return 17;
            case "$s2":
                return 18;
            case "$s3":
                return 19;
            case "$s4":
                return 20;
            case "$s5":
                return 21;
            case "$s6":
                return 22;
            case "$s7":
                return 23;
            case "$t8":
                return 24;
            case "$t9":
                return 25;
            case "$sp":
                return 29;
            case "$ra":
                return 31;
            default:
                throw new IllegalArgumentException("Unknown or bad register: " + register);
        }
    }
}