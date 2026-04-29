import java.util.List;
import java.util.Scanner;

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
        break;
      case "s":
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




}
