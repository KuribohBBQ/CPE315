import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

class Emulator {
  int pc = 0;
  int[] registers = new int[32];
  int[] dataMem = new int[8192];
  List<Instruction> instList;

  public Emulator(List<Instruction> instList) {
    this.instList = instList;
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

    // If want to be strict with ensuring all commands are given as seen in lab spec
    // if (cmdTokens.length > 1 && 
    //   !(cmdTokens[0].equalsIgnoreCase("s") ||
    //     cmdTokens[0].equalsIgnoreCase("m"))) {
    //   System.out.println("Bad command");
    // }


    switch (cmdTokens[0]) {
      case "h":
        displayHelp();
        break;
      case "d":
        dumpRegState();
        break;
      case "s":
        // step();
        break;
      case "r":
        break;
      case "m":
        break;
      case "c":
        clearState();
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

  // void step() {
  //   System.out.println("OL!");
  //   Instruction inst = this.instList.get(pc); // Get instruction based on pc

  // }

  void clearState() {
    Arrays.fill(this.registers, 0);
    Arrays.fill(this.dataMem, 0);
    this.pc = 0;
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
