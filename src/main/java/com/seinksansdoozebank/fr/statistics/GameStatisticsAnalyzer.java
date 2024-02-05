package com.seinksansdoozebank.fr.statistics;


import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBot;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.CardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.IUsingCondottiereEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.UsingCondottiereEffectToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.UsingMurdererEffectToFocusRusher;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.UsingThiefEffectToFocusRusher;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.logger.CustomLogger;
import com.seinksansdoozebank.fr.view.logger.CustomStatisticsLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.seinksansdoozebank.fr.view.logger.CustomLogger.resetAvailableColors;


public class GameStatisticsAnalyzer {
    private final Random random = new Random();
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
            //CustomStatisticsLogger.log(Level.INFO, "Random Bots: {0}", new Object[]{numRandomBots});
            //CustomStatisticsLogger.log(Level.INFO, "Smart Bots: {0}", new Object[]{numSmartBots});
            //CustomStatisticsLogger.log(Level.INFO, "Custom Bots: {0}", new Object[]{numCustomBots});
            Game game = createGame(numRandomBots, numSmartBots, numCustomBots);
            CustomStatisticsLogger.log(Level.INFO, "Game {0}", new Object[]{i + 1});
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

        // Perform statistical analysis
        performStatisticalAnalysis(winsByPlayerType, averageScoresByPlayerType, placementsByPlayerType);
    }

    private void performStatisticalAnalysis(Map<String, Integer> winsByPlayerType, Map<String, Double> averageScoresByPlayerType, Map<String, List<Integer>> placementsByPlayerType) {
        CustomStatisticsLogger.log(Level.INFO, "Statistical Analysis:");
        CustomStatisticsLogger.log(Level.INFO, "--------------------");

        for (Map.Entry<String, Integer> entry : winsByPlayerType.entrySet()) {
            String playerType = entry.getKey();
            int wins = entry.getValue();
            double averageScore = averageScoresByPlayerType.getOrDefault(playerType, 0.0);
            int totalGames = games.size();
            double winRate = wins / (double) totalGames * 100;

            int totalGamesWithPlayerType = placementsByPlayerType.get(playerType).size();
            CustomStatisticsLogger.log(Level.INFO, "Total Games with {0}: {1}", new Object[]{playerType, totalGamesWithPlayerType});

            CustomStatisticsLogger.log(Level.INFO, "Player Type: {0}", new Object[]{playerType});
            CustomStatisticsLogger.log(Level.INFO, "Total Games: {0}", new Object[]{totalGamesWithPlayerType});
            CustomStatisticsLogger.log(Level.INFO, "Wins: {0}", new Object[]{wins});
            CustomStatisticsLogger.log(Level.INFO, "Win Rate: {0}%", new Object[]{winRate});
            CustomStatisticsLogger.log(Level.INFO, "Average Score: {0}", new Object[]{averageScore});

            // Analyze placements
            List<Integer> placements = placementsByPlayerType.get(playerType);
            Map<Integer, Long> placementCounts = placements.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            CustomStatisticsLogger.log(Level.INFO, "Placements: {0}", new Object[]{placementCounts});

            CustomStatisticsLogger.log(Level.INFO, "");
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
            type += "CardChoosingStrategy-";
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
        ICharacterChoosingStrategy choosingCharacterToTargetFirstPlayer = new ChoosingCharacterToTargetFirstPlayer();
        IUsingThiefEffectStrategy usingThiefEffectToFocusRusher = new UsingThiefEffectToFocusRusher();
        IUsingMurdererEffectStrategy usingMurdererEffectToFocusRusher = new UsingMurdererEffectToFocusRusher();
        IUsingCondottiereEffectStrategy usingCondottiereEffectToTargetFirstPlayer = new UsingCondottiereEffectToTargetFirstPlayer();
        ICardChoosingStrategy cardChoosingStrategy = new CardChoosingStrategy();
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

    private int getRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static void main(String[] args) {
        int numSessions = 100;
        CustomStatisticsLogger.setLevel(Level.INFO);
        CustomLogger.setLevel(Level.OFF);
        GameStatisticsAnalyzer analyzer = new GameStatisticsAnalyzer(numSessions);
        analyzer.runAndAnalyze();
    }
}
