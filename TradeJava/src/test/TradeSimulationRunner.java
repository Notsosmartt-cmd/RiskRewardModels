package test;

public class TradeSimulationRunner {

    public static void main(String[] args) {
        int simulations = 10000000; // Adjustable number of simulations
        int liquidations = 0;

        for (int i = 0; i < simulations; i++) {
            double finalCapital = runSimulation();
            if (finalCapital < 1 || finalCapital < 25) { // Consider liquidated if capital is below 1 or below original capital
                liquidations++;
                System.out.println("Simulation " + (i + 1) + ": Liquidated");
            } else {
                System.out.println("Simulation " + (i + 1) + ": Final Capital: " + finalCapital + " (Profitable)");
            }
        }

        double liquidationChance = (liquidations / (double) simulations) * 100;
        System.out.println("Chance of Liquidation: " + liquidationChance + "%");
    }

    public static double runSimulation() {
        double original = 25;
        double capital = original;

        double fees = 2;
        int trades = 20;
        double winrate = 0.5; // Adjustable winrate

        for (int n = 0; n < trades; n++) {
            int randomNum = (int) ((Math.random() * 100) + 1); // Generates number from 1 to 100
            boolean isWin = randomNum <= (winrate * 100); // Compare with winrate percentage

            double riskedCapital = capital > 100 ? capital * 0.25 : capital;

            double win = riskedCapital; // 100% gain on win
            double loss = riskedCapital * 0.5; // -50% loss on loss

            double pnl = isWin ? win - fees : -loss - fees;
            capital += pnl;

            if (isWin && capital > 100) {
                capital += riskedCapital; // Add back risked capital after a win
            }

            if (capital < 1) {
                return 0; // Liquidated
            }
        }

        return capital;
    }
}
