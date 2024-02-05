package com.seinksansdoozebank.fr.statistics;


import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;
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
import com.seinksansdoozebank.fr.view.logger.CustomStatisticsLogger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.seinksansdoozebank.fr.view.logger.CustomLogger.resetAvailableColors;


public class GameStatisticsAnalyzer {
    private final int numSessions;
    private final Map<Player, PlayerStatistics> playerStatisticsMap;

    public GameStatisticsAnalyzer(int numSessions) {
        this.numSessions = numSessions;
        this.playerStatisticsMap = new HashMap<>();
    }

    public void runAndAnalyze() {
        for (int i = 0; i < numSessions; i++) {
            Game game = createGame(2, 2, 2);
            game.run();
            analyzeGameResults(game);
            Player.resetIdCounter();
        }

        // Log aggregated statistics
        logAggregatedStatistics();
    }

    private void analyzeGameResults(Game game) {
        List<Player> sortedPlayers = new ArrayList<>(game.getPlayers());
        sortedPlayers.sort(Comparator.comparingInt(Player::getScore).reversed());

        for (Player player : game.getPlayers()) {
            PlayerStatistics playerStats = playerStatisticsMap.computeIfAbsent(player, k -> new PlayerStatistics());
            playerStats.incrementTotalGames();

            if (sortedPlayers.indexOf(player) == 0) {
                playerStats.incrementGamesWon();
            } else if (sortedPlayers.indexOf(player) == game.getPlayers().size() - 1) {
                playerStats.incrementGamesLast();
            } else {
                playerStats.incrementGamesLost();
            }

            playerStats.addScore(player.getScore());
            playerStats.recordPlacement(sortedPlayers.indexOf(player) + 1);
        }
    }

    private void logAggregatedStatistics() {
        CustomStatisticsLogger.log(Level.INFO, "Aggregated Player Statistics:");

        // Header for the table
        StringBuilder header = new StringBuilder("| Player            | Total Games | Games Won | Games Lost | Games Last | Average Score | Winning Percentage | Detailed Placement                                             |\n");
        header.append("|-------------------|-------------|-----------|------------|------------|---------------|---------------------|----------------------------------------------------------------|\n");

        StringBuilder table = new StringBuilder(header);

        for (Map.Entry<Player, PlayerStatistics> entry : playerStatisticsMap.entrySet()) {
            Player player = entry.getKey();
            PlayerStatistics stats = entry.getValue();

            // Constructing the row for each player
            String row = String.format("| %-18s| %-12d| %-10d| %-11d| %-11d| %-14.3f| %-19.1f| %-63s|\n",
                    player.toString(), stats.getTotalGames(), stats.getGamesWon(),
                    stats.getGamesLost(), stats.getGamesLast(), stats.getAverageScore(),
                    stats.getWinningPercentage(), stats.getDetailedPlacement());

            table.append(row);
        }

        // Output the formatted table
        CustomStatisticsLogger.log(Level.INFO, table.toString());
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
        gameBuilder.addCustomBot(null,
                choosingCharacterToTargetFirstPlayer,
                usingThiefEffectToFocusRusher,
                usingMurdererEffectToFocusRusher,
                usingCondottiereEffectToTargetFirstPlayer,
                cardChoosingStrategy);
    }
}
