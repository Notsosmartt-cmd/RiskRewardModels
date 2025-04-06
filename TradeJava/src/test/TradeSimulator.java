package test;

import java.security.SecureRandom;

public class TradeSimulator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        double original = 25;
        double capital = original;

        double fees = 2;
        int trades = 500;
        double winrate = 0.5; // Adjustable winrate

        for (int n = 0; n < trades; n++) {
            int randomNum = secureRandom.nextInt(10) + 1; // Generates number from 1 to 10
            boolean isWin = randomNum <= (winrate * 10); // Compare with winrate percentage

            double riskedCapital = (capital > 100) ? capital * 0.25 : capital; // Risk 25% if above 100, else full capital
            double win = riskedCapital; // 100% gain on win
            double loss = riskedCapital * 0.5; // -50% loss on loss

            double pnl = isWin ? win - fees : -loss - fees;
            capital += pnl; // Update capital

            // Remove the incorrect addition of risked capital after a win
            System.out.println("Trade " + (n + 1) + ": Capital: " + capital + " (" + (isWin ? "Win" : "Loss") + ")");

            if (capital < 1) {
                System.out.println("Liquidated");
                return;
            }
            if (capital >= 100000) {
                System.out.println("done.");
                return;
            }
        }

        System.out.println(capital < original ? "Unprofitable" : "Profitable");
        System.out.println("Final Capital: " + capital);
    }
}
