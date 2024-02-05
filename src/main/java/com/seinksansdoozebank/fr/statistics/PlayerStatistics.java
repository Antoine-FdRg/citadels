package com.seinksansdoozebank.fr.statistics;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatistics {
    private int totalGames;
    private int gamesWon;
    private int gamesLost;
    private int gamesLast;
    private int totalScore;
    private final Map<Integer, Integer> placementCounts;

    public PlayerStatistics() {
        this.totalGames = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gamesLast = 0;
        this.totalScore = 0;
        this.placementCounts = new HashMap<>();
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getGamesLast() {
        return gamesLast;
    }

    public double getAverageScore() {
        return totalGames > 0 ? (double) totalScore / totalGames : 0;
    }

    public double getWinningPercentage() {
        return totalGames > 0 ? (double) gamesWon / totalGames * 100 : 0;
    }

    public void incrementTotalGames() {
        totalGames++;
    }

    public void incrementGamesWon() {
        gamesWon++;
    }

    public void incrementGamesLost() {
        gamesLost++;
    }

    public void incrementGamesLast() {
        gamesLast++;
    }

    public void addScore(int score) {
        totalScore += score;
    }

    public void recordPlacement(int placement) {
        placementCounts.put(placement, placementCounts.getOrDefault(placement, 0) + 1);
    }

    public String getDetailedPlacement() {
        StringBuilder detailedPlacement = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : placementCounts.entrySet()) {
            detailedPlacement.append("Games ").append(getPlacementName(entry.getKey()))
                    .append(": ").append(entry.getValue()).append(", ");
        }
        return detailedPlacement.toString();
    }

    private String getPlacementName(int placement) {
        return switch (placement) {
            case 1 -> "First";
            case 2 -> "Second";
            case 3 -> "Third";
            case 4 -> "Fourth";
            case 5 -> "Fifth";
            case 6 -> "Sixth";
            default -> "Unknown";
        };
    }
}
