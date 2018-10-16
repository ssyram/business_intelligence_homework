public class Main {

    public static void main(String []args) {
        final double PARA = 0.92;
        final int AMOUNT = 10000;
        double d = 1;
        for (int i = 1; d >= 0.0001; ++i) {
            d = Math.pow(PARA, i);
            System.out.println(i + ": " + d * AMOUNT);
        }
    }
}
