package com.seinksansdoozebank.fr.statistics;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class GameStatisticsAnalyzerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = spy(new RandomBot(2, new Deck(), new Cli()));
    }

    @Test
    void testAggregateAverageScoreWith1Game() {
        // Mock player statistics
        PlayerStatistics playerStats = Mockito.mock(PlayerStatistics.class);
        Mockito.when(playerStats.getTotalGames()).thenReturn(1); // Total games played before
        Mockito.when(playerStats.getAverageScore()).thenReturn(15.0); // Average score before

        // Mock player statistics map
        Map<Player, PlayerStatistics> playerStatisticsMap = new HashMap<>();
        playerStatisticsMap.put(player, playerStats);

        // Create GameStatisticsAnalyzer instance
        GameStatisticsAnalyzer analyzer = spy(new GameStatisticsAnalyzer(1, false, GameStatisticsAnalyzer.CsvCategory.DEMO_GAME));
        when(analyzer.getPlayerStatisticsMap()).thenReturn(playerStatisticsMap);

        // Call aggregateAverageScore method with loaded average score
        double loadedAverageScore = 18.0; // Loaded average score from CSV
        analyzer.aggregateAverageScore(loadedAverageScore);

        // Verify that the average score is correctly aggregated
        Mockito.verify(playerStats).setAverageScore(16.5); // Expected aggregated average score
    }

    @Test
    void testCalculateAverageScoreWith5Games() {
        // Mock player statistics
        PlayerStatistics playerStats = Mockito.mock(PlayerStatistics.class);
        Mockito.when(playerStats.getTotalGames()).thenReturn(5); // Total games played
        Mockito.when(playerStats.getTotalScore()).thenReturn(75); // Total score

        // Mock player statistics map
        Map<Player, PlayerStatistics> playerStatisticsMap = new HashMap<>();
        playerStatisticsMap.put(player, playerStats);

        // Create GameStatisticsAnalyzer instance
        GameStatisticsAnalyzer analyzer = spy(new GameStatisticsAnalyzer(3, false, GameStatisticsAnalyzer.CsvCategory.DEMO_GAME));
        when(analyzer.getPlayerStatisticsMap()).thenReturn(playerStatisticsMap);

        // Call calculateAverageScore method
        analyzer.calculateAverageScore();

        // Verify that the average score is correctly calculated
        Mockito.verify(playerStats).setAverageScore(15.0); // Expected average score
    }
}
