public class ProcessOperands {

    public static Operand processOperands(
            String name, String[] operands, int addr, LabelMap labelMap) {

        Operand ops = new Operand();

        switch (name) {
            case "add":
            case "and":
            case "or":
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

            case "jr":
                ops.setRs(registerNum(operands[0]));
                break;

            case "addi":
                ops.setRt(registerNum(operands[0]));
                ops.setRs(registerNum(operands[1]));
                ops.setImmediate(Integer.decode(operands[2]));
                break;

            case "beq":
            case "bne":
                ops.setRs(registerNum(operands[0]));
                ops.setRt(registerNum(operands[1]));
                int labelAddr = labelMap.getAddr(operands[2]);
                int offset = labelAddr - (addr + 1);
                ops.setImmediate(offset);
                break;

            case "lw":
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
            case "jal":
                //get address of label that is in label map
                int targetAddr = labelMap.getAddr(operands[0]);

                //jump instruction so we set target
                ops.setTarget(targetAddr);
                break;

            default:
                throw new IllegalArgumentException("Unknown instruction: " + name);
        }

        return ops;
    }

    public static int registerNum(String register) {
        switch (register) {
            case "$zero":
            case "$0":
                return 0;
            case "$a0":
                return 4;
            case "$a1":
                return 5;
            case "$t0":
                return 8;
            case "$t1":
                return 9;
            case "$t2":
                return 10;
            case "$t3":
                return 11;
            case "$s0":
                return 16;
            case "$ra":
                return 31;
            default:
                throw new IllegalArgumentException("Unknown or bad register: " + register);
        }
    }
}