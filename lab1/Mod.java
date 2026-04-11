public class Mod {
  public static int mod(int num, int div) {
    return num & (div - 1);
  }
  
  public static void main(String[] args) {
    System.out.println(mod(22, 4));
  }
}