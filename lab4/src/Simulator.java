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

    this.if_id.setInst(inst);
  }

  void clearState() {
    Arrays.fill(this.registers, 0);
    this.if_id.clearReg();
    this.id_exe.clearReg();
    this.exe_mem.clearReg();
    this.mem_wb.clearReg();
    this.pc = 0;
  }

}
