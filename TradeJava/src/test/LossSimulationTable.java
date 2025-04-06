package test;

public class LossSimulationTable {

    public static void main(String[] args) {
        int maxCapital = 10000;
        int step = 100;

        // Header
        System.out.printf("%-10s%-10s%-15s", "Capital", "Saved", "Risked");
        for (int i = 1; i <= 10; i++) {
            System.out.printf("%-12s", "Loss " + i);
        }
        System.out.println();

        for (int capital = step; capital <= maxCapital; capital += step) {
            double saved = capital / 2.0;
            double risked = capital - saved;

            System.out.printf("%-10.0f%-10.0f%-15.0f", (double) capital, saved, risked);

            double loss = (risked / 2.0)-2;
            for (int i = 0; i < 10; i++) {
                System.out.printf("%-12.2f", loss);
                loss /= 2.0;
            }
            System.out.println();
        }
    }
}
