package com.seinksansdoozebank.fr.statistics;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PlayerStatisticsTest {

    @Test
    void testIncrementTotalGames() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.incrementTotalGames();
        assertEquals(1, playerStatistics.getTotalGames());
    }

    @Test
    void testIncrementGamesWon() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.incrementGamesWon();
        assertEquals(1, playerStatistics.getGamesWon());
    }

    @Test
    void testIncrementGamesLost() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.incrementGamesLost();
        assertEquals(1, playerStatistics.getGamesLost());
    }

    @Test
    void testAddScore() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.addScore(50);
        assertEquals(50, playerStatistics.getTotalScore());
    }

    @Test
    void testRecordPlacement() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.recordPlacement(1);
        playerStatistics.recordPlacement(3);
        playerStatistics.recordPlacement(1);

        Map<Integer, Integer> expectedPlacementCounts = new HashMap<>();
        expectedPlacementCounts.put(1, 2);
        expectedPlacementCounts.put(2, 0);
        expectedPlacementCounts.put(3, 1);
        expectedPlacementCounts.put(4, 0);
        expectedPlacementCounts.put(5, 0);
        expectedPlacementCounts.put(6, 0);

        assertEquals(expectedPlacementCounts, playerStatistics.getDetailedPlacement());
    }

    @Test
    void testWinningPercentage() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.setTotalGames(100);
        playerStatistics.setGamesWon(60);
        assertEquals(60.0, playerStatistics.getWinningPercentage());
    }

    // Add more tests as needed for other methods and edge cases
    @Test
    void testSetWinningPercentage() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.setTotalGames(100);
        playerStatistics.setGamesWon(60);

        assertEquals(60.0, playerStatistics.getWinningPercentage());
    }

    @Test
    void testSetDetailedPlacement() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        Map<Integer, Integer> placementCounts = new HashMap<>();
        placementCounts.put(1, 2);
        placementCounts.put(3, 1);

        playerStatistics.setDetailedPlacement(placementCounts);

        Map<Integer, Integer> expectedPlacementCounts = new HashMap<>();
        expectedPlacementCounts.put(1, 2);
        expectedPlacementCounts.put(2, 0);
        expectedPlacementCounts.put(3, 1);
        expectedPlacementCounts.put(4, 0);
        expectedPlacementCounts.put(5, 0);
        expectedPlacementCounts.put(6, 0);

        assertEquals(expectedPlacementCounts, playerStatistics.getDetailedPlacement());
    }

    @Test
    void testSetGamesLost() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.setGamesLost(5);
        assertEquals(5, playerStatistics.getGamesLost());
    }

    @Test
    void testToString() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.setTotalGames(100);
        playerStatistics.setGamesWon(60);
        playerStatistics.setGamesLost(40);
        playerStatistics.addScore(1500);
        playerStatistics.setAverageScore(15.0);

        String expectedToString = "PlayerStatistics{" +
                "totalGames=100, gamesWon=60, gamesLost=40, totalScore=1500, averageScore=15.0, winningPercentage=60.0, placementCounts={1=0, 2=0, 3=0, 4=0, 5=0, 6=0}" +
                '}';

        assertEquals(expectedToString, playerStatistics.toString());
    }
}
