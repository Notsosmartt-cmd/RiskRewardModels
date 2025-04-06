package test;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class TradeSimulatorSwing extends JPanel {
    private final List<Double> capitalHistory; // Stores capital over trades

    public TradeSimulatorSwing() {
        this.capitalHistory = runTradeSimulation();
    }

    private List<Double> runTradeSimulation() {
        SecureRandom secureRandom = new SecureRandom();
        List<Double> history = new ArrayList<>();

        double original = 25;
        double capital = original;
        double fees = 2;
        int trades = 500;
        double winrate = 0.5;

        for (int n = 0; n < trades; n++) {
            int randomNum = secureRandom.nextInt(10) + 1;
            boolean isWin = randomNum <= (winrate * 10);

            double riskedCapital = (capital > 100) ? capital * 0.25 : capital;
            double win = riskedCapital;
            double loss = riskedCapital * 0.5;

            double pnl = isWin ? win - fees : -loss - fees;
            capital += pnl;
            history.add(capital);

            System.out.println("Trade " + (n + 1) + ": Capital: " + capital + " (" + (isWin ? "Win" : "Loss") + ")");

            if (capital < 1) {
                System.out.println("Liquidated");
                break;
            }
            if (capital >= 100000) {
                System.out.println("done.");
                break;
            }
        }

        System.out.println(capital < original ? "Unprofitable" : "Profitable");
        System.out.println("Final Capital: " + capital);
        return history;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(50, height - 50, width - 50, height - 50); // X-axis
        g2.drawLine(50, height - 50, 50, 20); // Y-axis

        // Axis labels
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("Trades", width / 2, height - 10); // X-axis label
        g2.drawString("Capital", 10, height / 2); // Y-axis label

        // Draw graph
        if (capitalHistory.isEmpty()) return;

        double maxCapital = capitalHistory.stream().max(Double::compare).orElse(100.0);
        double minCapital = capitalHistory.stream().min(Double::compare).orElse(0.0);
        int graphHeight = height - 100;
        int graphWidth = width - 100;

        int lastX = 50, lastY = height - 50;
        for (int i = 0; i < capitalHistory.size(); i++) {
            int x = 50 + (i * graphWidth / capitalHistory.size());
            int y = height - 50 - (int) ((capitalHistory.get(i) - minCapital) / (maxCapital - minCapital) * graphHeight);

            g2.setColor(Color.BLUE);
            g2.drawLine(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
        }

        // Draw ticks for X-axis (Trades)
        int numTrades = capitalHistory.size();
        int xTickSpacing = graphWidth / numTrades;
        for (int i = 0; i < numTrades; i += numTrades / 10) {
            int x = 50 + (i * graphWidth / numTrades);
            g2.drawLine(x, height - 50, x, height - 45);
            g2.drawString(Integer.toString(i + 1), x - 10, height - 30);
        }

        // Draw ticks for Y-axis (Capital)
        int numTicks = 10;
        double capitalTickSpacing = (maxCapital - minCapital) / numTicks;
        for (int i = 0; i <= numTicks; i++) {
            int y = height - 50 - (int) ((i * capitalTickSpacing) / (maxCapital - minCapital) * graphHeight);
            g2.drawLine(45, y, 50, y);
            g2.drawString(String.format("%.2f", minCapital + i * capitalTickSpacing), 10, y + 5);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Trade Simulation");
            TradeSimulatorSwing panel = new TradeSimulatorSwing();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}




