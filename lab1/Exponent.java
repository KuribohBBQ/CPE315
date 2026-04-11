public class Exponent {
  public static int exponent(int x, int y) {

    if (y == 1) {
      return x;
    }

    int res = x;
    int current = 0;

    while (y > 1) {
      for (int i = x; i > 1; i--) {
        current += res;
      }
      res += current;
      current = 0;
      y--;
    }

    return res;
  }

  public static void main(String[] args) {
    System.out.println(exponent(15, 7));
  }
}
