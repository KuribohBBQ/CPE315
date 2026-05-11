public class Simulator {
  private Emulator emu;

  private int pc = 0;
  private int[] registers = new int[32];
  private ProgramData progData;

  private String if_id = "empty";
  private String id_exe = "empty";
  private String exe_mem = "empty";
  private String mem_wb = "empty";

  private int numCycles = 0;
}
