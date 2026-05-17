import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Simulator {
  private Emulator emu;

  private int pc = 0;
  private int[] registers = new int[32];
  private ProgramData progData;

  private PipelineReg if_id;
  private PipelineReg id_exe;
  private PipelineReg exe_mem;
  private PipelineReg mem_wb;

  private int numCycles = 0;
  private int numInst = 0;

  public Simulator(ProgramData progData) {
    this.emu = new Emulator(progData);
    this.progData = progData;

    this.if_id = new PipelineReg();
    this.id_exe = new PipelineReg();
    this.exe_mem = new PipelineReg();
    this.mem_wb = new PipelineReg();
  }

  void executeInteractive() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.print("mips> ");
      String command = scanner.nextLine();

      if (command.equalsIgnoreCase("q")) {
        break;
      }

      processCommand(command);
    }

    scanner.close();
  }

  void processCommand(String command) {
    String[] cmdTokens = command.split("\\s+"); // split to account for s and m commands

    switch (cmdTokens[0]) {
      case "h":
        displayHelp();
        break;
      case "d":
        emu.dumpRegState(); // use Emulator register state
        break;
      case "p":
        showPipelineRegs();
        break;
      case "s":
        if (cmdTokens.length > 1) {
          int instruction_cnt = 0;
          for (int i = 0; i < Integer.parseInt(cmdTokens[1]); i++) {
            instruction_cnt +=1;
            emu.step();
            step();
          }
          System.out.println("\t" + instruction_cnt + " instruction(s) executed");
        }
        else {
          System.out.println("\t1 instruction(s) executed");
          emu.step();
          step();
        }
        break;
      case "r":
        //run program until we reach end of instructions
        while(pc < progData.getInstList().size() || !allPipesEmpty())
        {
          step();
        }
        //after everything is processed, print results
        printCycleInfo();
        break;
      case "m":
        emu.displayMem(Integer.parseInt(cmdTokens[1]), Integer.parseInt(cmdTokens[2]));
        break;
      case "c":
        emu.clearState();
        clearState();
        break;
      default:
        break;
    }
  }

  void displayHelp() {
    System.out.println("\nh = show help");
    System.out.println("d = dump register state");
    System.out.println("p = show pipeline registers");
    System.out.println("s = step through a single clock cycle step (i.e. simulate 1 cycle and stop)");
    System.out.println("s num = step through num clock cycles");
    System.out.println("r = run until the program ends and display timing summary");
    System.out.println("m num1 num2 = display data memory from location num1 to num2");
    System.out.println("c = clear all registers, memory, and the program counter to 0");
    System.out.println("q = exit the program\n");
  }

  void showPipelineRegs() {
    System.out.println();
    System.out.printf("%-2s\t %-5s %-6s %-7s %-6s%n", "pc", "if/id", "id/exe", "exe/mem", "mem/wb");
    System.out.printf("%-2s\t %-5s %-6s %-7s %-6s%n%n", this.pc,
                                                            this.if_id.getInstName(),
                                                            this.id_exe.getInstName(),
                                                            this.exe_mem.getInstName(),
                                                            this.mem_wb.getInstName());
  }

  void step() {
    List<Instruction> instList = this.progData.getInstList(); // Get instruction list from ProgramData


    boolean stall = detectStall(id_exe, if_id);

    if (stall) {

      // move older stages forward
      mem_wb.copyFrom(exe_mem);
      exe_mem.copyFrom(id_exe);

      // insert visible stall bubble into EX
      id_exe.setStall();

      // DO NOT move if_id or increment pc
      this.numCycles++;
      return;
    }

    // Normal pipeline movement, from back to front
    mem_wb.copyFrom(exe_mem);
    exe_mem.copyFrom(id_exe);
    id_exe.copyFrom(if_id);
    if_id.clearReg();



    //begin handling if_id
    // if previous was j, jr, or jal
    if (this.id_exe.getInst() != null) {
      if (this.id_exe.getInstName().equals("jr") || this.id_exe.getInst().getType() == 'j') {
        if (this.id_exe.getIsBranchTaken()) {
          this.if_id.setSquash();
          this.pc = this.emu.branchRes.getJumpAddr();
          return;

        }
      }
    }

    // IF stage: fetch only if pc is still inside instruction list
    if (pc < instList.size()) {
      Instruction inst = instList.get(pc);
      if (this.id_exe.getInst() != null) {
        if (this.id_exe.getInstName().equals("jr") || this.id_exe.getInst().getType() == 'j') {
          if (this.id_exe.getIsBranchTaken()) {
            this.if_id.setSquash();
            this.pc = this.emu.branchRes.getJumpAddr();
            this.numCycles++;
            return;
          }
        }
      }
      //begin handling if_id
      // instruction isn't a jump or branch
      this.if_id.setInst(inst, this.emu.branchRes.getBranchTaken()); //set if_id pipe instruction to instruction at index pc in instruction list
      this.pc += 1; //increment pc counter by 1
    }
    this.numCycles++;
    if (!mem_wb.getIsEmpty() && !mem_wb.getIsSquash() && !mem_wb.getIsStall()) {
      this.numInst++;
    }
  }

  void clearState() {
    Arrays.fill(this.registers, 0);
    this.if_id.clearReg();
    this.id_exe.clearReg();
    this.exe_mem.clearReg();
    this.mem_wb.clearReg();
    this.pc = 0;
    this.numCycles = 0;
    this.numInst = 0;
  }

  int getDestInst(Instruction inst){
    if (inst == null){
      return -1;
    }
    String instName = inst.getName();
    Operands op = inst.getOperands();

    //destination registers depends on instruction
    switch (instName) {
      //instructions that use rd as destination
      case "add":
      case "sub":
      case "slt":
      case "and":
      case "or":
      case "sll":
        return op.getRd();

      //these instructions use rt as destination
      case "addi":
      case "lw":
        return op.getRt();

      //other instructions don't need destination, so default return -1
      default:
        return -1;
    }
  }

  boolean detectStall(PipelineReg id_exe, PipelineReg if_id) {
    Instruction prev = id_exe.getInst();  // instruction in EX, the prev instr
    Instruction curr = if_id.getInst();   // instruction in ID, the current instr

    if (prev == null || curr == null) return false;



    // Only care about lw
    if (!prev.getName().equals("lw")) return false;

    int destReg = getDestInst(prev); //get previous instruction destination register

    if (destReg == 0) {
      return false;
    }
    String currName = curr.getName();
    Operands currOps = curr.getOperands(); //get operands of current instruction
    //add, sub, and, or, slt, or use rd as destination register
    switch (currName){
      case "add":
      case "sub":
      case "and":
      case "or":
      case "slt":

        //if current instruction's rs or rt is equal previous instruction's rd, then return true
        return (destReg == currOps.getRs() || destReg == currOps.getRt());

      //lw and addi use rt as destination register, so check if current instruction use previous instruction's destination register
      //in rs
      case "lw":
      case "addi":
        return (destReg == currOps.getRs());

      case "sw":
        return destReg == currOps.getRs() || destReg == currOps.getRt();

      case "sll":
        return destReg == currOps.getRt();

      default:
        return false;

    }


  }

  void printCycleInfo()
  {
    double CPI = (double) this.numCycles / this.numInst;

    System.out.println("Program complete");
    System.out.printf("CPI = %.3f\t Cycles = %d\t Instructions = %d\n", CPI, this.numCycles, this.numInst);

  }

  Boolean allPipesEmpty()
  {
    return this.if_id.getIsEmpty() && this.id_exe.getIsEmpty()
            && exe_mem.getIsEmpty() && this.mem_wb.getIsEmpty();
  }


}