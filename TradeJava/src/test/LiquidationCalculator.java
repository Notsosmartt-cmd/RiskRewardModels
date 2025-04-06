package test;
public class LiquidationCalculator {
   public static void main(String[] args) {
       int simulations = 10;
       int liquidations = 0;
       long startTime = System.currentTimeMillis();
       for (int i = 0; i < simulations; i++) {
           if (runSimulation()) {
               liquidations++;
           }
       }
       long endTime = System.currentTimeMillis();
       double liquidationChance = (liquidations / (double) simulations) * 100;
       System.out.println("Chance of liquidation: " + liquidationChance + "%");
       System.out.println("Execution time: " + (endTime - startTime) + " ms");
       
       
   }
   public static boolean runSimulation() {
	    double original = 5000 ;
	    double capital = original;
	    double Rcapital = capital * 0.25;
	    double leverage = 25 * capital;
	    double Rleverage = 25 * Rcapital;
	    double loss = 0.02 * leverage;
	    double Rloss = 0.02 * Rleverage;
	    double win = 0.04 * leverage;
	    double Rwin = 0.04 * Rleverage;
	    double fees = 2;
	    double winrate = 0.5;
	    double pnl = 0;
	    int trades = 5000;

	    for (int n = 0; n < trades; n++) {
	        if (capital > 100) {
	            if (winrate == 0.3) {
	                int randomNum = (int) ((Math.random() * 3) + 1); // 30% win rate
	                pnl = (randomNum % 2 == 0) ? (Rwin + Rcapital) - fees : (Rcapital - Rloss) - fees;
	                
	            } else if (winrate == 0.6) {
	                int randomNum = (int) ((Math.random() * 3) + 1); // 60% win rate
	                pnl = (randomNum % 2 == 0) ? (Rcapital - Rloss) - fees : (Rwin + Rcapital) - fees;
	                
	            } else if (winrate == 0.5) {
	                int randomNum = (int) ((Math.random() * 4) + 1); // 50% win rate
	                pnl = (randomNum % 2 == 0) ? (Rwin + Rcapital) - fees : (Rcapital - Rloss) - fees;
	                
	            }
	            capital = pnl;
	            if (capital < 1) {
	                return true; // Liquidated
	            }
	        } else {
	            if (winrate == 0.3) {
	                int randomNum = (int) ((Math.random() * 3) + 1); // 30% win rate
	                pnl = (randomNum % 2 == 0) ? (win + capital) - fees : (capital - loss) - fees;
	                
	            } else if (winrate == 0.6) {
	                int randomNum = (int) ((Math.random() * 3) + 1); // 60% win rate
	                pnl = (randomNum % 2 == 0) ? (capital - loss) - fees : (win + capital) - fees;
	                
	            } else if (winrate == 0.5) {
	                int randomNum = (int) ((Math.random() * 4) + 1); // 50% win rate
	                pnl = (randomNum % 2 == 0) ? (win + capital) - fees : (capital - loss) - fees;
	                
	            }
	            capital = pnl;
	            if (capital < 1) {
	                return true; // Liquidated
	            }
	        }
	    }
	    return false; // Not liquidated if loop completes
	}

 
}