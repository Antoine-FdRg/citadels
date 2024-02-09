package com.seinksansdoozebank.fr.statistics;

import java.util.HashMap;
import java.util.Map;

/**
 * The player statistics
 */
public class PlayerStatistics {
    private int totalGames;
    private int gamesWon;
    private int gamesLost;
    private int totalScore;
    private double averageScore;
    private final Map<Integer, Integer> placementCounts;

    /**
     * PlayerStatistics constructor
     */
    public PlayerStatistics() {
        this.totalGames = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.totalScore = 0;
        this.placementCounts = new HashMap<>();
        // Initialize all positions to 0 by default
        for (int i = 1; i <= 6; i++) {
            placementCounts.put(i, 0);
        }
    }

    /**
     * Get the total number of games played
     *
     * @return the total number of games played
     */
    public int getTotalGames() {
        return totalGames;
    }

    /**
     * Get the number of games won
     * @return the number of games won
     */
    public int getGamesWon() {
        return gamesWon;
    }

    /**
     * Get the number of games lost
     * @return the number of games lost
     */
    public int getGamesLost() {
        return gamesLost;
    }

    /**
     * Get the winning percentage
     * @return the winning percentage
     */
    public double getWinningPercentage() {
        return totalGames > 0 ? (double) gamesWon / totalGames * 100 : 0;
    }

    /**
     * Set the total number of games played
     * @param totalGames the total number of games played
     */
    public void setTotalGames(int totalGames) {
        if (totalGames < 0) return;
        this.totalGames += totalGames;
    }

    /**
     * Set the number of games won
     * @param gamesWon the number of games won
     */
    public void setGamesWon(int gamesWon) {
        if (gamesWon < 0) return;
        this.gamesWon += gamesWon;
    }

    /**
     * Set the number of games lost
     * @param gamesLost the number of games lost
     */
    public void setGamesLost(int gamesLost) {
        if (gamesLost < 0) return;
        this.gamesLost += gamesLost;
    }

    /**
     * Get the average score
     * @return the average score
     */
    public double getAverageScore() {
        return this.averageScore;
    }

    /**
     * Set the average score
     * @param averageScore the average score
     */
    public void setAverageScore(double averageScore) {
        if (averageScore < 0) return;
        this.averageScore = averageScore;
    }

    /**
     * Set the detailed placement
     * @param placementCounts the detailed placement
     */
    public void setDetailedPlacement(Map<Integer, Integer> placementCounts) {
        // update placement counts
        for (Map.Entry<Integer, Integer> entry : placementCounts.entrySet()) {
            this.placementCounts.put(entry.getKey(), this.placementCounts.get(entry.getKey()) + entry.getValue());
        }
    }

    /**
     * Increment the total number of games played
     */
    public void incrementTotalGames() {
        totalGames++;
    }

    /**
     * Increment the number of games won
     */
    public void incrementGamesWon() {
        gamesWon++;
    }

    /**
     * Increment the number of games lost
     */
    public void incrementGamesLost() {
        gamesLost++;
    }

    /**
     * Add a score to the total score
     * @param score the score to add
     */
    public void addScore(int score) {
        if (score < 0) return;
        totalScore += score;
    }

    /**
     * Get the total score
     * @return the total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Record the placement
     * @param placement the placement to record
     */
    public void recordPlacement(int placement) {
        if (placement < 1 || placement > 6) return;
        placementCounts.put(placement, placementCounts.getOrDefault(placement, 0) + 1);
    }

    /**
     * Get the detailed placement
     * @return the detailed placement
     */
    public Map<Integer, Integer> getDetailedPlacement() {
        return placementCounts;
    }

    @Override
    public String toString() {
        return "PlayerStatistics{"
                + "totalGames=" + this.getTotalGames()
                + ", gamesWon=" + this.getGamesWon()
                + ", gamesLost=" + this.getGamesLost()
                + ", totalScore=" + this.getTotalScore()
                + ", averageScore=" + this.getAverageScore()
                + ", winningPercentage=" + this.getWinningPercentage()
                + ", placementCounts=" + getDetailedPlacement() + '}';
    }
}
