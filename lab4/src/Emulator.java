import java.util.List;
import java.util.Arrays;

class Emulator {
  int[] registers = new int[32];
  int[] dataMem = new int[8192];
  ProgramData progData;
  public BranchRes branchRes = new BranchRes();

  public class BranchRes {
    private boolean branchTaken;
    private int branchAddr;
    
    public BranchRes() {
      this.branchTaken = false;
    }

    public void setBranchTaken(boolean branchStatus) {
      this.branchTaken = branchStatus;
    }

    public boolean getBranchTaken() {
      return this.branchTaken;
    }

    public int getBranchAddr() {
      return this.branchAddr;
    }

    public void setJumpAddr(int branchAddr) {
      this.branchAddr = branchAddr;
    }
  }

  public Emulator(ProgramData progData) {
    this.progData = progData;
  }

  void processCommand(String command, int pc) {
    String[] cmdTokens = command.split("\\s+"); // split to account for s and m commands

    switch (cmdTokens[0]) {
      case "h":
        displayHelp();
        break;
      case "d":
        dumpRegState(pc);
        break;
      case "s":
        if (cmdTokens.length > 1) {
          int instruction_cnt = 0;
          for (int i = 0; i < Integer.parseInt(cmdTokens[1]); i++) {
            instruction_cnt +=1;
            step(pc);
          }
          System.out.println("\t" + instruction_cnt + " instruction(s) executed");
        }
        else {
          // System.out.println("\t1 instruction(s) executed"); // not needed for lab 4
          step(pc);
        }
        break;
      case "m":
        displayMem(Integer.parseInt(cmdTokens[1]), Integer.parseInt(cmdTokens[2]));
        break;
      case "c":
        clearState();
        break;
      default:
        break;
    }
  }

  void displayHelp() {
    System.out.println("\nh = show help");
    System.out.println("d = dump register state");
    System.out.println("s = single step through the program (i.e. execute 1 instruction and stop)");
    System.out.println("s num = step through num instructions of the program");
    System.out.println("r = run until the program ends");
    System.out.println("m num1 num2 = display data memory from location num1 to num2");
    System.out.println("c = clear all registers, memory, and the program counter to 0");
    System.out.println("q = exit the program\n");
  }

  void dumpRegState(int pc) {
    int numCols = 4;

    System.out.println("\npc = " + pc);
    for (int i = 0; i < this.registers.length; i++) {
      String regName = getRegName(i);

      if (regName.isEmpty()) { // some integers don't represent a mips register; skip them
        continue;
      }

      System.out.printf("%-3s = %d\t\t", regName, this.registers[i]);

      if ((i % numCols == 0) && (i != 0)) {
        System.out.println();
      }
    }

    System.out.print("\n\n");
  }

  void step(int pc) {
    List<Instruction> instList = this.progData.getInstList(); // Get instruction list from ProgramData
    LabelMap labelMap = this.progData.getLabelMap(); // Get label map from ProgramData

    Instruction inst = instList.get(pc); // Get instruction based on pc

    String inst_name = inst.getName(); // instruction name
    Operands ops = inst.getOperands(); // instruction operands

    // get registers for each field
    int rd = ops.getRd();
    int rs = ops.getRs();
    int rt = ops.getRt();

    int imm = ops.getImmediate();
    int shamt = ops.getShamt();

    // get values from registers
    int rs_value = registers[rs];
    int rt_value = registers[rt];

    int labelAddr;
    int memIdx;

    this.branchRes.setBranchTaken(false);

    switch (inst_name) {
      case "and":
        
        int and_value = rs_value & rt_value; // and rs and rt values
        registers[rd] = and_value; // put value into register
        break;

      case "add":
        int add_values =  rs_value + rt_value;
        registers[rd] = add_values;
        break;

      case "or":
        int or_values = rs_value|rt_value; // bitwise OR
        registers[rd] = or_values;
        break;


      case "addi":
        int add_imm = imm + rs_value; // add immediate
        registers[rt] = add_imm;
        break;

      case "sll":
        int shifted_val = rt_value << shamt; // shift value in register rt by shamt
        registers[rd] = shifted_val;
        break;

      case "sub":
        int sub_val = rs_value - rt_value; // subtract value in rs by rt value then store in rd
        registers[rd] = sub_val;
        break;

      case "slt":
        // check if value in rs is less than value in rt
        if (rs_value < rt_value){
          registers[rd] = 1;
        }
        else {
          registers[rd] = 0;
        }
        break;

      case "beq":
        if (rs_value == rt_value) {
          labelAddr = labelMap.getAddr(ops.getLabel());
          int offset = labelAddr - (ops.getTarget() + 1); // relative offset calculated from pc+1
          this.branchRes.setBranchTaken(true); // save result of branch in branchRes for simulator use
          this.branchRes.setJumpAddr(pc + offset + 1);
        }
        break;
      case "bne":
        if (rs_value != rt_value) {
          labelAddr = labelMap.getAddr(ops.getLabel());
          int offset = labelAddr - (ops.getTarget() + 1); // relative offset calculated from pc+1
          this.branchRes.setBranchTaken(true); // save result of branch in branchRes for simulator use
          this.branchRes.setJumpAddr(pc + offset + 1);
        }
        break;
      case "lw":
        memIdx = rs_value + imm; // Get memory index referenced by immediate + rs
        registers[rt] = this.dataMem[memIdx]; // Load into register rt
        break;
      case "sw":
        memIdx = rs_value + imm; // Grab memory index referenced by immediate + rs
        this.dataMem[memIdx] = registers[rt]; // Save rt into this memory location
        break;
      case "j":
        labelAddr = labelMap.getAddr(ops.getLabel());
        this.branchRes.setBranchTaken(true); // save result of jump in branchRes for simulator use
        this.branchRes.setJumpAddr(labelAddr);
        break;
      case "jr":
        this.branchRes.setBranchTaken(true); // save result of jump in branchRes for simulator use
        this.branchRes.setJumpAddr(rs_value); 
        break;
      case "jal":
        labelAddr = labelMap.getAddr(ops.getLabel());
        registers[31] = pc + 1;
        this.branchRes.setBranchTaken(true); // save result of jump in branchRes for simulator use
        this.branchRes.setJumpAddr(labelAddr);
        break;
    }
  }

  void displayMem(int num1, int num2) {
    System.out.println();
    for (int i = num1; i < num2 + 1; i++) {
      System.out.printf("[%d] = %d\n", i, this.dataMem[i]);  
    }
    System.out.println();
  }

  void clearState() {
    System.out.println("\tSimulator reset");
    Arrays.fill(this.registers, 0);
    Arrays.fill(this.dataMem, 0);
  }

  String getRegName(int regNum) {
    switch (regNum) {
      case 0:
        return "$0";
      case 2:
        return "$v0";
      case 3:
        return "$v1";
      case 4:
        return "$a0";
      case 5:
        return "$a1";
      case 6:
        return "$a2";
      case 7:
        return "$a3";
      case 8:
        return "$t0";
      case 9:
        return "$t1";
      case 10:
        return "$t2";
      case 11:
        return "$t3";
      case 12:
        return "$t4";
      case 13:
        return "$t5";
      case 14:
        return "$t6";
      case 15:
        return "$t7";
      case 16:
        return "$s0";
      case 17:
        return "$s1";
      case 18:
        return "$s2";
      case 19:
        return "$s3";
      case 20:
        return "$s4";
      case 21:
        return "$s5";
      case 22:
        return "$s6";
      case 23:
        return "$s7";
      case 24:
        return "$t8";
      case 25:
        return "$t9";
      case 29:
        return "$sp";
      case 31:
        return "$ra";
      default:
        return "";
    }
  }
}