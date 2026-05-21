import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

  private boolean pendingBranchSquash = false; //flag for detecting if there are instructions in pipes pending to be squashed
  private int pendingBranchTarget = -1;

  public Simulator(ProgramData progData) {
    this.emu = new Emulator(progData);
    this.progData = progData;

    this.if_id = new PipelineReg();
    this.id_exe = new PipelineReg();
    this.exe_mem = new PipelineReg();
    this.mem_wb = new PipelineReg();
  }

  void executeScript(String fname) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
      String command;
      while ((command = reader.readLine()) != null) {
        System.out.printf("mips> %s\n", command);

        if (command.equalsIgnoreCase("q")) {
          break;
        }

        processCommand(command);
      }
    }
   catch (IOException e) {
      e.printStackTrace();
    }
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
        emu.dumpRegState(this.pc); // use Emulator register state
        break;
      case "p":
        showPipelineRegs();
        break;
      case "s":
        if (cmdTokens.length > 1) {
          int instruction_cnt = 0;
          for (int i = 0; i < Integer.parseInt(cmdTokens[1]); i++) {
            instruction_cnt +=1;
            step();
          }
          System.out.println("\t" + instruction_cnt + " instruction(s) executed");
        }
        else {
<<<<<<< HEAD
          step();
=======
          emu.step(this.pc);
          stepOneCycle();
>>>>>>> main
          showPipelineRegs();
        }
        break;
      case "r":
        //run program until we reach end of instructions
        while(pc < progData.getInstList().size() || !allPipesEmpty())
        {
//          System.out.println("PC at " + this.pc);
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
    numCycles++;
    List<Instruction> instList = this.progData.getInstList();

    //count completed instruction leaving mem_web instead of at if_id
    //instructions that are squashed, empty or stall are not counted
    if (!mem_wb.getIsEmpty() && !mem_wb.getIsStall() && !mem_wb.getIsSquash() && !mem_wb.getInstName().equals("squash")
            && !mem_wb.getInstName().equals("stall") && !mem_wb.getInstName().equals("empty")) {
      numInst++;
    }

    //if a branch is pending to be squashed, squashed the first three pipes after moving instruction from exe_mem to mem_wb
    if (pendingBranchSquash) {
      mem_wb.copyFrom(exe_mem);

      //squash instructions in pipe
      exe_mem.setSquash();
      id_exe.setSquash();
      if_id.setSquash();

      pc = pendingBranchTarget; //make pc the value of branch target
      pendingBranchSquash = false; //since we finished squashing, reset flag back to false
      pendingBranchTarget = -1;

      return;
    }

    // only execute arithmetic instruction in EX by calling step method in emulator
    //only if instruction is not squashed and is not stalled
    if (id_exe.getInst() != null && !id_exe.getIsSquash() && !id_exe.getIsStall() && id_exe.getPc() >= 0) {

      emu.step(id_exe.getPc());

      id_exe.setBranchInfo(
              emu.branchRes.getBranchTaken(),
              emu.branchRes.getBranchAddr()
      );
    }

    //handles beq and bne
    boolean branchTaken =
            id_exe.getInst() != null
                    && (id_exe.getInstName().equals("beq") || id_exe.getInstName().equals("bne"))
                    && id_exe.getIsBranchTaken();

    // if branch was actually taken, then need to squash prev 3 instructions
    if (branchTaken) {
      pendingBranchSquash = true;
      pendingBranchTarget = id_exe.getBranchAddr();

      mem_wb.copyFrom(exe_mem);
      exe_mem.copyFrom(id_exe);
      id_exe.copyFrom(if_id);
      if_id.clearReg();

      if (pc < instList.size()) {
        Instruction inst = instList.get(pc);
        if_id.setInst(inst, false, -1, pc);
        pc++;
      }

      return;
    }

    //check if we need to stall
    boolean stall = detectStall(id_exe, if_id);

    if (stall) {
      //move instructions from other pipes forward
      mem_wb.copyFrom(exe_mem);
      exe_mem.copyFrom(id_exe);
      id_exe.setStall();

      return;
    }

    // Resolve j / jal / jr in ID stage
    //handle instructions that use jump
    //if true, execute jump instructions in if_id instead of id_exe
    if (if_id.getInst() != null && !if_id.getIsSquash() && !if_id.getIsStall() && (if_id.getInstName().equals("jr") || if_id.getInst().getType() == 'j')) {

      emu.step(if_id.getPc());
      int target = emu.branchRes.getBranchAddr();

      mem_wb.copyFrom(exe_mem);
      exe_mem.copyFrom(id_exe);
      id_exe.copyFrom(if_id);

      if_id.setSquash();

      pc = target;
      return;
    }

    //move instructions from pipes to the next corresponding pipe
    mem_wb.copyFrom(exe_mem);
    exe_mem.copyFrom(id_exe);
    id_exe.copyFrom(if_id);
    if_id.clearReg();

    //fetch instruction from inst list and put it in first pipe
    if (pc < instList.size()) {
      Instruction inst = instList.get(pc);
      if_id.setInst(inst, false, -1, pc);
      pc++;
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

  //if previous instruction was lw, and its destination register is being used as a source register in current instruction
  //STALL
  boolean detectStall(PipelineReg id_exe, PipelineReg if_id) {
    Instruction prev = id_exe.getInst();
    Instruction curr = if_id.getInst();

    if (prev == null || curr == null) return false;
    if (!prev.getName().equals("lw")) return false; //only care if prev inst was lw

    int destReg = getDestInst(prev);
    if (destReg == 0) return false;

    Operands currOps = curr.getOperands();
    String currName = curr.getName();

    switch (currName) {
      case "add":
      case "sub":
      case "and":
      case "or":
      case "slt":
      case "beq":
      case "bne":
        return destReg == currOps.getRs() || destReg == currOps.getRt();

      case "addi":
      case "lw":
        return destReg == currOps.getRs();

      case "sw":
        return destReg == currOps.getRs() || destReg == currOps.getRt();

      case "sll":
        return destReg == currOps.getRt();

      case "jr":
        return destReg == currOps.getRs();

      default:
        return false;
    }
  }

  void printCycleInfo()
  {
    int totalInst = numInst;
    double CPI = (double) this.numCycles / totalInst;
    System.out.println("\nProgram complete");
    System.out.printf("CPI = %.3f\t Cycles = %d\t Instructions = %d\n\n", CPI, this.numCycles, totalInst);
  }

  //when r command is used, keep running until all pipes are empty
  Boolean allPipesEmpty()
  {
    return this.if_id.getIsEmpty() && this.id_exe.getIsEmpty()
            && exe_mem.getIsEmpty() && this.mem_wb.getIsEmpty();
  }

}