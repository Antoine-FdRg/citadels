package com.seinksansdoozebank.fr.statistics;


import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.SmartBot;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBot;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.CardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.UsingCondottiereEffectToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.UsingMurdererEffectToFocusRusher;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.UsingThiefEffectToFocusRusher;
import com.seinksansdoozebank.fr.view.Cli;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static com.seinksansdoozebank.fr.view.logger.CustomLogger.resetAvailableColors;


public class GameStatisticsAnalyzer {
    private final int numSessions;
    private final List<Game> games;

    public GameStatisticsAnalyzer(int numSessions) {
        this.numSessions = numSessions;
        this.games = new ArrayList<>();
    }

    public void runAndAnalyze() {
        // Run multiple game sessions
        for (int i = 0; i < numSessions; i++) {
            resetAvailableColors();
            // Generate random numbers for each type of player
            int numRandomBots = getRandomNumber(0, 6);
            int numSmartBots = getRandomNumber(0, 6 - numRandomBots);
            int numCustomBots = 6 - numRandomBots - numSmartBots;
            System.out.println("Random Bots: " + numRandomBots);
            System.out.println("Smart Bots: " + numSmartBots);
            System.out.println("Custom Bots: " + numCustomBots);
            Game game = createGame(numRandomBots, numSmartBots, numCustomBots);
            game.run();
            games.add(game);
        }

        // Collect statistics
        Map<String, Integer> winsByPlayerType = new HashMap<>();
        Map<String, Double> averageScoresByPlayerType = new HashMap<>();
        Map<String, List<Integer>> placementsByPlayerType = new HashMap<>(); // New map to store placements

        for (Game game : games) {
            Player winner = game.getWinner();
            String playerType = getPlayerType(winner);
            winsByPlayerType.put(playerType, winsByPlayerType.getOrDefault(playerType, 0) + 1);
            averageScoresByPlayerType.put(playerType, averageScoresByPlayerType.getOrDefault(playerType, 0.0) + winner.getScore());

            // Add placements for all players in the game
            for (Player player : game.getPlayers()) {
                String currentPlayerType = getPlayerType(player);
                placementsByPlayerType.putIfAbsent(currentPlayerType, new ArrayList<>());
                placementsByPlayerType.get(currentPlayerType).add(game.getPlayers().indexOf(player) + 1); // Adding placement (index starts from 0)
            }
        }

        // Save statistics to Excel file
        saveToExcel(winsByPlayerType, averageScoresByPlayerType, placementsByPlayerType);

        // Perform statistical analysis
        performStatisticalAnalysis(winsByPlayerType, averageScoresByPlayerType, placementsByPlayerType);
    }

    private void performStatisticalAnalysis(Map<String, Integer> winsByPlayerType, Map<String, Double> averageScoresByPlayerType, Map<String, List<Integer>> placementsByPlayerType) {
        System.out.println("Statistical Analysis:");
        System.out.println("--------------------");

        for (Map.Entry<String, Integer> entry : winsByPlayerType.entrySet()) {
            String playerType = entry.getKey();
            int wins = entry.getValue();
            double averageScore = averageScoresByPlayerType.getOrDefault(playerType, 0.0);
            int totalGames = games.size();
            double winRate = wins / (double) totalGames * 100;

            int totalGamesWithPlayerType = placementsByPlayerType.get(playerType).size();
            System.out.println("Total Games with " + playerType + ": " + totalGamesWithPlayerType);

            System.out.println("Player Type: " + playerType);
            System.out.println("Total Games: " + totalGamesWithPlayerType);
            System.out.println("Wins: " + wins);
            System.out.println("Win Rate: " + winRate + "%");
            System.out.println("Average Score: " + averageScore);

            // Analyze placements
            List<Integer> placements = placementsByPlayerType.get(playerType);
            Map<Integer, Long> placementCounts = placements.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            System.out.println("Placements: " + placementCounts);

            System.out.println();
        }
    }

    private String getPlayerType(Player player) {
        // Determine player type based on the player instance
        if (player instanceof CustomBot customBot) {
            // for the Custom bot we need to determine the strategies it has
            return getCustomBotType(customBot);
        } else if (player instanceof RandomBot) {
            return "RandomBot";
        } else {
            return "SmartBot";
        }
    }

    private String getCustomBotType(CustomBot customBot) {
        // Determine the type of custom bot based on the strategies it has
        String type = "";
        if (customBot.getCharacterChoosingStrategy() != null) {
            type += "CharacterEffect-";
        }
        if (customBot.getUsingThiefEffectStrategy() != null) {
            type += "ThiefEffect-";
        }
        if (customBot.getUsingMurdererEffectStrategy() != null) {
            type += "MurderEffect-";
        }
        if (customBot.getUsingCondottiereEffectStrategy() != null) {
            type += "CondottiereEffect-";
        }
        if (customBot.getCardChoosingStrategy() != null) {
            type += "CardChoosingStrategy";
        }
        return type;
    }


    private Game createGame(int numRandomBots, int numSmartBots, int numCustomBots) {
        GameBuilder gameBuilder = new GameBuilder(new Cli(), new Deck());
        for (int i = 0; i < numRandomBots; i++) {
            gameBuilder.addRandomBot();
        }
        for (int i = 0; i < numSmartBots; i++) {
            gameBuilder.addSmartBot();
        }
        for (int i = 0; i < numCustomBots; i++) {
            generateARandomCustomBot(gameBuilder);
        }
        return gameBuilder.build();

    }

    private void generateARandomCustomBot(GameBuilder gameBuilder) {
        ChoosingCharacterToTargetFirstPlayer choosingCharacterToTargetFirstPlayer = new ChoosingCharacterToTargetFirstPlayer();
        UsingThiefEffectToFocusRusher usingThiefEffectToFocusRusher = new UsingThiefEffectToFocusRusher();
        UsingMurdererEffectToFocusRusher usingMurdererEffectToFocusRusher = new UsingMurdererEffectToFocusRusher();
        UsingCondottiereEffectToTargetFirstPlayer usingCondottiereEffectToTargetFirstPlayer = new UsingCondottiereEffectToTargetFirstPlayer();
        CardChoosingStrategy cardChoosingStrategy = new CardChoosingStrategy();
        // get a random bot with some, none or all strategies
        // get 5 numbers from 0 to 1
        int[] randomStrategies = new int[5];
        for (int i = 0; i < 5; i++) {
            randomStrategies[i] = getRandomNumber(0, 1);
        }
        gameBuilder.addCustomBot(null,
                randomStrategies[0] == 1 ? choosingCharacterToTargetFirstPlayer : null,
                randomStrategies[1] == 1 ? usingThiefEffectToFocusRusher : null,
                randomStrategies[2] == 1 ? usingMurdererEffectToFocusRusher : null,
                randomStrategies[3] == 1 ? usingCondottiereEffectToTargetFirstPlayer : null,
                randomStrategies[4] == 1 ? cardChoosingStrategy : null);
    }


    private void saveToExcel(Map<String, Integer> winsByPlayerType, Map<String, Double> averageScoresByPlayerType, Map<String, List<Integer>> placementsByPlayerType) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Game Statistics");

            // Create headers
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Player Type");
            headerRow.createCell(1).setCellValue("Total Games");
            headerRow.createCell(2).setCellValue("Wins");
            headerRow.createCell(3).setCellValue("Win Rate (%)");
            headerRow.createCell(4).setCellValue("Average Score");
            headerRow.createCell(5).setCellValue("Average Placement");
            headerRow.createCell(6).setCellValue("Max Placement");
            headerRow.createCell(7).setCellValue("Min Placement");

            // Populate data
            int rowNum = 1;
            for (Map.Entry<String, Integer> entry : winsByPlayerType.entrySet()) {
                Row row = sheet.createRow(rowNum++);
                String playerType = entry.getKey();
                int wins = entry.getValue();
                double totalGames = games.size();
                double winRate = (wins / totalGames) * 100;
                double averageScore = averageScoresByPlayerType.getOrDefault(playerType, 0.0);

                int totalGamesWithPlayerType = placementsByPlayerType.get(playerType).size();

                // Create cell for player type, wins, total games, win rate, and average score
                row.createCell(0).setCellValue(playerType);
                row.createCell(1).setCellValue(totalGamesWithPlayerType);
                row.createCell(2).setCellValue(wins);
                row.createCell(3).setCellValue(winRate);
                row.createCell(4).setCellValue(averageScore);

                // Get placements for the player type
                List<Integer> placements = placementsByPlayerType.get(playerType);
                // Calculate average placement
                double averagePlacement = placements.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                // Calculate max and min placements
                int maxPlacement = placements.stream().mapToInt(Integer::intValue).max().orElse(0);
                int minPlacement = placements.stream().mapToInt(Integer::intValue).min().orElse(0);

                // Create cell for average placement, max placement, and min placement
                row.createCell(5).setCellValue(averagePlacement);
                row.createCell(6).setCellValue(maxPlacement);
                row.createCell(7).setCellValue(minPlacement);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream("game_statistics.xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static void main(String[] args) {
        int numSessions = 100;
        GameStatisticsAnalyzer analyzer = new GameStatisticsAnalyzer(numSessions);
        analyzer.runAndAnalyze();
    }
}
