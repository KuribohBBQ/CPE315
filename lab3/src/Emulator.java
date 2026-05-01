import java.util.List;
import java.util.Scanner;

class Emulator {
  int pc = 0;
  int[] registers = new int[32];
  int[] dataMem = new int[8192];
  ProgramData progData;

  public Emulator(ProgramData progData) {
    this.progData = progData;
  }

  void executeInteractive() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.print("mips> ");
      String command = scanner.nextLine();

      if (command.equalsIgnoreCase("q")) {
        break;
      }

      processCommand(command, pc);
    }

    scanner.close();

  }

  void processCommand(String command, int pc) {
    String[] cmdTokens = command.split("\\s+"); // split to account for s and m commands

    switch (cmdTokens[0]) {
      case "h":
        displayHelp();
        break;
      case "d":
        dumpRegState();
        break;
      case "s":
        step();
        break;
      case "r":
        break;
      case "m":
        break;
      case "c":
        break;
      default:
        System.out.println("LOL");
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

  void dumpRegState() {
    // String format = "%-2s %3s %3s %3s%n";
    int numCols = 4;

    System.out.println("\npc = " + this.pc);
    for (int i = 0; i < this.registers.length; i++) {
      String regName = getRegName(i);

      if (regName.isEmpty()) {
        continue;
      }

      System.out.printf("%-3s = %d\t\t", regName, this.registers[i]);

      if ((i % numCols == 0) && (i != 0)) {
        System.out.println();
      }
    }

    System.out.print("\n\n");
  }

  void step() {
    System.out.println("OL!");
    List<Instruction> instList = this.progData.getInstList(); // Get instruction list from ProgramData
    LabelMap labelMap = this.progData.getLabelMap(); // Get label map from ProgramData

    Instruction inst = instList.get(pc); // Get instruction based on pc

    String inst_name = inst.getName(); // instruction name
    Operands ops = inst.getOperands(); // instruction operands

    int rd = ops.getRd();
    int rs = ops.getRt();
    int rt = ops.getRs();

    int imm = ops.getImmediate();
    int shamt = ops.getShamt();

    // get values from registers
    int rs_value = registers[rs];
    int rt_value = registers[rt];
    int ra_value = registers[31];


    int labelAddr;
    int memIdx;

    switch (inst_name) {
      case "and":
        //And rs and rt values
        int and_value = rs_value & rt_value;
        //put value into register
        registers[rd] = and_value;
        //add 1 to PC counter
        this.pc += 1;

        break;

      case "add":
        int add_values =  rs_value + rt_value;
        registers[rd] = add_values;

        //add 1 to PC counter
        this.pc += 1;
        break;

      case "or":
        //bitwise OR
        int or_values = rs_value|rt_value;
        registers[rd] = or_values;
        this.pc += 1;
        break;


      case "addi":
        //add immediate
        int add_imm = imm + rs_value;
        registers[rt] = add_imm;
        this.pc += 1;
        break;

      case "sll":
        //shift value in register rt by shamt
        int shifted_val = rt_value << shamt;
        registers[rd] = shifted_val;
        this.pc += 1;
        break;

      case "sub":
        //subtract value in rs by rt value then store in rd
        int sub_val = rs_value - rt_value;
        registers[rd] = sub_val;
        this.pc += 1;
        break;

      case "slt":
        this.pc += 1;
        //check if value in rs is less than value in rt
        if (rs_value < rt_value){
          registers[rd] = 1;
        }
        else {
          registers[rd] = 0;
        }
        break;

      case "beq":
        this.pc += 1;

        if (rs_value == rt_value) {
          labelAddr = labelMap.getAddr(ops.getLabel());
          int offset = labelAddr - (ops.getTarget() + 1); // relative offset calculated from pc+1
          this.pc += offset;
        }
        break;
      case "bne":
        this.pc += 1;

        if (rs_value != rt_value) {
          labelAddr = labelMap.getAddr(ops.getLabel());
          int offset = labelAddr - (ops.getTarget() + 1); // relative offset calculated from pc+1
          this.pc += offset;
        }
        break;
      case "lw":
        memIdx = rs_value + imm; // Get memory index referenced by immediate + rs
        registers[rt] = this.dataMem[memIdx]; // Load into register rt
        this.pc += 1;
        break;
      case "sw":
        memIdx = rs_value + imm; // Grab memory index referenced by immediate + rs
        this.dataMem[memIdx] = registers[rt]; // Save rt into this memory location
        this.pc += 1;
        break;
      case "j":
        labelAddr = labelMap.getAddr(ops.getLabel());
        this.pc = labelAddr;
        break;
      case "jr":
        this.pc = rs_value;
        break;
      case "jal":
        labelAddr = labelMap.getAddr(ops.getLabel());
        registers[31] = this.pc + 1;
        this.pc = labelAddr;
        break;
    }

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