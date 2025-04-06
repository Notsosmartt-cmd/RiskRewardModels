package test;

import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

public class TradeSimulationAnalyzer {
    public static void main(String[] args) {
        int simulations = 1000000; // Number of simulations
        int liquidations = 0;
        TreeMap<String, Integer> capitalDistribution = new TreeMap<>();

        for (int i = 0; i < simulations; i++) {
            double finalCapital = runSimulation();
            String range = categorizeCapital(finalCapital);

            capitalDistribution.put(range, capitalDistribution.getOrDefault(range, 0) + 1);

            if (finalCapital < 1) {
                liquidations++;
            }
        }

        double liquidationChance = (liquidations / (double) simulations) * 100;

        // Print results in a table format
        printResultsTable(capitalDistribution, liquidationChance, simulations);
    }

    public static double runSimulation() {
        SecureRandom secureRandom = new SecureRandom();
        double original = 25;
        double capital = original;
        double fees = 2;
        int trades = 20;
        double winrate = 0.6;

        for (int n = 0; n < trades; n++) {
            int randomNum = secureRandom.nextInt(10) + 1;
            boolean isWin = randomNum <= (winrate * 10);

            double riskedCapital = (capital > 100) ? capital * 0.25 : capital;
            double win = riskedCapital;
            double loss = riskedCapital * 0.5;

            double pnl = isWin ? win - fees : -loss - fees;
            capital += pnl;

            if (capital < 1) {
                return 0;
            }
        }
        return capital;
    }

    private static String categorizeCapital(double capital) {
        if (capital < 1) return "Liquidated";
        if (capital < 25) return "$1 - $25";
        if (capital < 50) return "$25 - $50";
        if (capital < 100) return "$50 - $100";
        if (capital < 200) return "$100 - $200";
        if (capital < 500) return "$200 - $500";
        return "$500+";
    }

    private static void printResultsTable(Map<String, Integer> capitalDistribution, double liquidationChance, int simulations) {
        System.out.println("\n=== Simulation Results ===");
        System.out.println("+------------------+-----------+------------+");
        System.out.println("| Capital Range    | Frequency | Percentage |");
        System.out.println("+------------------+-----------+------------+");

        for (Map.Entry<String, Integer> entry : capitalDistribution.entrySet()) {
            double percentage = (entry.getValue() / (double) simulations) * 100;
            System.out.printf("| %-16s | %-9d | %-10.2f%% |\n", entry.getKey(), entry.getValue(), percentage);
        }

        System.out.println("+------------------+-----------+------------+");
        System.out.printf("| %-16s | %-9.2f%% |\n", "Liquidation Rate", liquidationChance);
        System.out.println("+------------------+-----------+------------+");
        System.out.printf("Total Simulations: %d\n", simulations);
    }
}
