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
            emu.step(this.pc);
            step();
          }
          System.out.println("\t" + instruction_cnt + " instruction(s) executed");
        }
        else {
          System.out.println("\t1 instruction(s) executed");
          emu.step(this.pc);
          step();
          this.showPipelineRegs();
        }
        break;
      case "r":
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
    LabelMap labelMap = this.progData.getLabelMap(); // Get label map from ProgramData

    Instruction inst = instList.get(pc); // Get instruction based on pc

    String inst_name = inst.getName(); // instruction name
    Operands ops = inst.getOperands(); // instruction operands

    boolean stall = detectStall(id_exe, if_id);

    if (stall) {

      // move older stages forward
      mem_wb.copyFrom(exe_mem);
      exe_mem.copyFrom(id_exe);

      // insert visible stall bubble into EX
      id_exe.setStall();

      // DO NOT move if_id or increment pc
      return;
    }

    //pipe instructions should have default values at start
    Instruction mem_web_instr = this.mem_wb.getInst();
    Instruction exe_mem_instr = this.exe_mem.getInst();
    Instruction id_exe_instr = this.id_exe.getInst();
    Instruction if_id_instr = this.if_id.getInst();


    //if mem_wb pipe is empty, move instruction from exe_mem pipe to mem_wb pipe
    if (this.mem_wb.getIsEmpty()) {
      //if exe_mem is not empty(the previous pipe), move its instruction to mem_web, then make exe_mem pipeline empty by clearing it
      if (!this.exe_mem.getIsEmpty()){

        this.mem_wb.copyFrom(this.exe_mem); //set mem_wb pipe inst field to be exe_mem inst

        this.exe_mem.clearReg(); //clear exe_mem pipe
      }

    }
    //else if mem_wb pipe is not empty. Clear it and do same stuff as above
    else {
      this.mem_wb.clearReg();
      this.mem_wb.copyFrom(this.exe_mem);
      this.exe_mem.clearReg();
    }

    //if exe_mem pipe is empty, move instruction from id_exe pipe to exe_mem pipe
    if (this.exe_mem.getIsEmpty()) {
      //if id_exe pipe is not empty, move instruction from there to this pipe
      if (!this.id_exe.getIsEmpty()) {
        this.exe_mem.copyFrom(this.id_exe); //move id_exe pipe inst to exe_mem pipe

        this.id_exe.clearReg(); //clear id_exe pipe

      }
    }

    //if id_exe pipe is empty , move instruction from if_id pipe to id_exe pipe
    if (this.id_exe.getIsEmpty()){
      //if if_id pipe is not empty, move instruction form there to this pipe
      if (!this.if_id.getIsEmpty()) {
        this.id_exe.copyFrom(this.if_id);

        this.if_id.clearReg();
      }
    }

    //begin handling if_id
    // if previous was j, jr, or jal
    if (this.id_exe.getInst() != null) {
      if (this.id_exe.getInstName().equals("jr") || this.id_exe.getInst().getType() == 'j') {
        if (this.id_exe.getIsBranchTaken()) {
          this.if_id.setSquash();
          this.pc = this.id_exe.getBranchAddr();
          return;
        }
      }
    }

    // ...
    if (this.mem_wb.getInst() != null) {
      if (this.mem_wb.getInstName().equals("beq") || this.mem_wb.getInstName().equals("bne")) {
        if (this.mem_wb.getIsBranchTaken()) {
          // squash previous 3 instructions if branch was actually taken
          this.exe_mem.setSquash();
          this.id_exe.setSquash();
          this.if_id.setSquash();
          this.pc = this.mem_wb.getBranchAddr();
          return;
        }
      }
    }

    // instruction isn't a jump or branch
    this.if_id.setInst(inst, this.emu.branchRes.getBranchTaken(), this.emu.branchRes.getBranchAddr()); //set if_id pipe instruction to instruction at index pc in instruction list
    this.pc += 1; //increment pc counter by 1


  }

  void clearState() {
    Arrays.fill(this.registers, 0);
    this.if_id.clearReg();
    this.id_exe.clearReg();
    this.exe_mem.clearReg();
    this.mem_wb.clearReg();
    this.pc = 0;
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
    Instruction prev = id_exe.getInst();  // instruction in EX
    Instruction curr = if_id.getInst();   // instruction in ID

    if (prev == null || curr == null) return false;

    // Only care about lw
    if (!prev.getName().equals("lw")) return false;

    int dest = prev.getOperands().getRt(); // lw writes to rt

    int rs = curr.getOperands().getRs();
    int rt = curr.getOperands().getRt();

    if (dest == rs) {
      return true;
    }

    if (curr.getRtRead() && (dest == rt)) {
      return true;
    }

    return false;
  }

}
