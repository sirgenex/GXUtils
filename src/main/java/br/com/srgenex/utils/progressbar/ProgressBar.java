package br.com.srgenex.utils.progressbar;

import org.bukkit.ChatColor;

public class ProgressBar {

    public static String generateProgressBar(int currentValue, int totalValue, int barLength, String completedColor, String uncompletedColor, String barSymbol) {
        double progress = (double) currentValue / totalValue;

        int completedBars = (int) (progress * barLength);
        int uncompletedBars = barLength - completedBars;

        StringBuilder progressBar = new StringBuilder();
        progressBar.append(completedColor);

        for (int i = 0; i < completedBars; i++) {
            progressBar.append(barSymbol);
        }

        progressBar.append(uncompletedColor);

        for (int i = 0; i < uncompletedBars; i++) {
            progressBar.append(barSymbol);
        }

        progressBar.append(ChatColor.RESET);
        return progressBar.toString();
    }

    public static String generateProgressBar(double currentValue, double totalValue, int barLength, String completedColor, String uncompletedColor, String barSymbol) {
        double progress = currentValue / totalValue;

        int completedBars = (int) (progress * barLength);
        int uncompletedBars = barLength - completedBars;

        StringBuilder progressBar = new StringBuilder();
        progressBar.append(completedColor);

        for (int i = 0; i < completedBars; i++) {
            progressBar.append(barSymbol);
        }

        progressBar.append(uncompletedColor);

        for (int i = 0; i < uncompletedBars; i++) {
            progressBar.append(barSymbol);
        }

        progressBar.append(ChatColor.RESET);
        return progressBar.toString();
    }

}
