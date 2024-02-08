package com.seinksansdoozebank.fr.statistics;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.CardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.UsingCondottiereEffectToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.UsingMurdererEffectToFocusRusher;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.UsingThiefEffectToFocusRusher;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.logger.CustomStatisticsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class GameStatisticsAnalyzerTest {

    private Player player;
    private GameStatisticsAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        player = spy(new RandomBot(2, new Deck(), new Cli(), new Bank()));
        // Initialize the GameStatisticsAnalyzer instance
        analyzer = spy(new GameStatisticsAnalyzer(false)); // Assuming saveStatsToCsv is set to false for testing
    }

    @Test
    void testConstructorWithNumSessions() {
        int numSessions = 10;
        boolean saveStatsToCsv = true;
        GameStatisticsAnalyzer.CsvCategory csvCategory = GameStatisticsAnalyzer.CsvCategory.BEST_AGAINST_SECOND;

        GameStatisticsAnalyzer analyzer = new GameStatisticsAnalyzer(numSessions, saveStatsToCsv, csvCategory);

        assertEquals(numSessions, analyzer.getNumSessions());
        assertEquals(saveStatsToCsv, analyzer.isSaveStatsToCsv());
        assertEquals(csvCategory, analyzer.getCsvCategory());
        assertNotNull(analyzer.getPlayerStatisticsMap());
        assertTrue(analyzer.getPlayerStatisticsMap().isEmpty());
    }

    @Test
    void testConstructorWithoutNumSessions() {
        boolean saveStatsToCsv = true;

        GameStatisticsAnalyzer analyzer = new GameStatisticsAnalyzer(saveStatsToCsv);

        assertEquals(0, analyzer.getNumSessions());
        assertEquals(saveStatsToCsv, analyzer.isSaveStatsToCsv());
        assertEquals(GameStatisticsAnalyzer.CsvCategory.DEMO_GAME, analyzer.getCsvCategory());
        assertNotNull(analyzer.getPlayerStatisticsMap());
        assertTrue(analyzer.getPlayerStatisticsMap().isEmpty());
    }

    @Test
    void testRunDemo() {
        when(analyzer.isSaveStatsToCsv()).thenReturn(true, false);

        analyzer.runDemo();

        Mockito.verify(analyzer, Mockito.times(1)).analyzeGameResults(any());
        Mockito.verify(analyzer, Mockito.times(1)).logAggregatedStatistics();
    }

    @Test
    void testRunAndAnalyze() {
        when(analyzer.getNumSessions()).thenReturn(1);

        analyzer.runAndAnalyze(1, 1, 1, 1, 1, 1);

        Mockito.verify(analyzer, Mockito.times(1)).analyzeGameResults(any());
        Mockito.verify(analyzer, Mockito.times(1)).logAggregatedStatistics();
    }

    @Test
    void testAnalyzeGameResults() {
        // Mock necessary dependencies
        Player.resetIdCounter(); // Ensure Player ID counter is reset

        Game game = new GameBuilder(new Cli(), new Deck(), new Bank())
                .addRandomBot()
                .addRandomBot()
                .addBuilderBot()
                .addRichardBot()
                .addSmartBot()
                .addCustomBot(null, new ChoosingCharacterToTargetFirstPlayer(),
                        new UsingThiefEffectToFocusRusher(),
                        new UsingMurdererEffectToFocusRusher(),
                        new UsingCondottiereEffectToTargetFirstPlayer(),
                        new CardChoosingStrategy())
                .build();

        // Call the method to be tested
        analyzer.analyzeGameResults(game);

        // Verify that the expected methods were called
        Mockito.verify(analyzer, Mockito.times(1)).calculateAverageScore();
        assertEquals(6, analyzer.getPlayerStatisticsMap().size());
    }


    @Test
    void testIsTheSameBotInCsvFileFalse() throws CsvValidationException, IOException {
        // Mock the CSVReader class
        GameStatisticsAnalyzer.CsvCategory csvCategory = GameStatisticsAnalyzer.CsvCategory.BEST_AGAINST_SECOND;
        GameStatisticsAnalyzer analyzer = spy(new GameStatisticsAnalyzer(1, false, csvCategory));
        when(analyzer.getCsvCategory()).thenReturn(csvCategory);
        when(analyzer.getPlayerStatisticsMap()).thenReturn(new HashMap<>());

        // Mock the CSV file content
        String[] csvFileContent = new String[] {
                "Player,Total Games,Total Score,Average Score",
                "RandomBot 1,1,15,15.0",
                "BuilderBot 2,1,20,20.0",
                "RichardBot 3,1,25,25.0",
                "SmartBot 4,1,30,30.0",
                "CustomBot 5,1,35,35.0",
                "RandomBot 6,1,40,40.0"
        };

        // Mock the CSVReader instance
        CSVReader csvReader = mock(CSVReader.class);
        when(csvReader.readNext()).thenReturn(csvFileContent, (String[]) null);

        assertFalse(analyzer.isTheSameBotInCsvFile());
    }

    @Test
    void testGetFileName() {
        // Test the getFileName method for each enum value
        assertEquals("BestAgainstSecondAndOthers.csv", GameStatisticsAnalyzer.CsvCategory.BEST_AGAINST_SECOND.getFileName());
        assertEquals("BestBotsAgainstBestBot.csv", GameStatisticsAnalyzer.CsvCategory.BEST_BOTS_AGAINST.getFileName());
        assertEquals("gamestats.csv", GameStatisticsAnalyzer.CsvCategory.DEMO_GAME.getFileName());
    }

    @Test
    void testGetPlayerByName() {
        // Mock player statistics map
        Map<Player, PlayerStatistics> playerStatisticsMap = new HashMap<>();
        playerStatisticsMap.put(player, new PlayerStatistics());

        // Create GameStatisticsAnalyzer instance
        GameStatisticsAnalyzer analyzer = spy(new GameStatisticsAnalyzer(1, false, GameStatisticsAnalyzer.CsvCategory.DEMO_GAME));
        when(analyzer.getPlayerStatisticsMap()).thenReturn(playerStatisticsMap);

        // Call getPlayerByName method
        Player playerByName = analyzer.getPlayerByName(player.toString());

        // Verify that the correct player is returned
        assertEquals(player, playerByName);
    }

    @Test
    void testAggregateAverageScoreWith1Game() {
        // Mock player statistics
        PlayerStatistics playerStats = mock(PlayerStatistics.class);
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
        PlayerStatistics playerStats = mock(PlayerStatistics.class);
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
