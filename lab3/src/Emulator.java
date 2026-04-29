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

  void processCommand(String command, int pc) {
    switch (command) {
      default:
        System.out.println("LOL");
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

      processCommand(command, pc);
    }

    scanner.close();

  }




}
