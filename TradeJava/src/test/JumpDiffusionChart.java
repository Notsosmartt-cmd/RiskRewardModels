package test;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JumpDiffusionChart extends JFrame {

    private final double[] prices;
    private final int width = 800;
    private final int height = 600;

    public JumpDiffusionChart(double[] prices) {
        this.prices = prices;
        setTitle("Stochastic Jump Diffusion Chart");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new ChartPanel());
    }

    class ChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Calculate bounds
            double maxPrice = Double.MIN_VALUE;
            double minPrice = Double.MAX_VALUE;
            for (double price : prices) {
                if (price > maxPrice) maxPrice = price;
                if (price < minPrice) minPrice = price;
            }

            // Add some padding
            double yRange = maxPrice - minPrice;
            minPrice -= yRange * 0.1;
            maxPrice += yRange * 0.1;

            // Draw axes
            g2.setColor(Color.BLACK);
            g2.drawLine(50, height - 50, width - 50, height - 50); // X-axis
            g2.drawLine(50, height - 50, 50, 50); // Y-axis

            // Draw price path
            g2.setColor(Color.BLUE);
            int prevX = 50;
            int prevY = (int) map(prices[0], minPrice, maxPrice, height - 50, 50);
            
            for (int i = 1; i < prices.length; i++) {
                int x = (int) map(i, 0, prices.length, 50, width - 50);
                int y = (int) map(prices[i], minPrice, maxPrice, height - 50, 50);
                g2.drawLine(prevX, prevY, x, y);
                prevX = x;
                prevY = y;
            }

            // Draw labels
            g2.setColor(Color.BLACK);
            g2.drawString("Price Movement Chart", width/2 - 50, 30);
            g2.drawString("Time", width/2 - 10, height - 20);
            
            // Rotate for Y-axis label
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.rotate(-Math.PI/2);
            g2d.drawString("Price", -height/2 - 30, 25);
            g2d.dispose();
        }

        private double map(double value, double start1, double end1, double start2, double end2) {
            return (value - start1) * (end2 - start2) / (end1 - start1) + start2;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }
    }

    public static void main(String[] args) {
        // Simulation parameters (same as before)
        double initialPrice = 100.0;
        double drift = 0.0001;
        double volatility = 0.01;
        double jumpIntensity = 0.05;
        double jumpMean = -0.03;
        double jumpVolatility = 0.02;
        int timeSteps = 500;

        double[] prices = new double[timeSteps];
        prices[0] = initialPrice;

        Random rand = new Random();

        for (int t = 1; t < timeSteps; t++) {
            double normalVar = rand.nextGaussian();
            double dailyReturn = drift + volatility * normalVar;

            if (rand.nextDouble() < jumpIntensity) {
                double jumpSize = jumpMean + jumpVolatility * rand.nextGaussian();
                dailyReturn += jumpSize;
            }

            prices[t] = prices[t-1] * Math.exp(dailyReturn);
            prices[t] = Math.max(prices[t], 0.01);
        }

        SwingUtilities.invokeLater(() -> {
            new JumpDiffusionChart(prices).setVisible(true);
        });
    }
}