//信号処理　最後の問題の計算式
public class Keisan {

    public static double calculateY(int n) {
        if (n == 0) {
            return 1.0;
        } else if (n == 1) {
            return -0.4;
        } else if (n == 2) {
            return 1.46;
        } else if (n == 3) {
            return -0.904;
        } else {
            return -0.4 * calculateY(n - 1) + 0.3 * calculateY(n - 2) - 0.2 * calculateY(n - 3);
        }
    }

    public static void main(String[] args) {
        int n = 21; //this input
        for (int i = 0; i < n; i++) {
            double result = calculateY(i);
            System.out.println("y" + i + " = " + result);
        }
    }
}