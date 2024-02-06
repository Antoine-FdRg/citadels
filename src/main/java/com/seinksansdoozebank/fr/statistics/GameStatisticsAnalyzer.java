package com.seinksansdoozebank.fr.statistics;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.bank.Bank;
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
import com.seinksansdoozebank.fr.view.logger.CustomLogger;
import com.seinksansdoozebank.fr.view.logger.CustomStatisticsLogger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.seinksansdoozebank.fr.statistics.GameStatisticsAnalyzer.CsvCategory.DEMO_GAME;


public class GameStatisticsAnalyzer {

    public enum CsvCategory {
        BEST_AGAINST_SECOND("BestAgainstSecondAndOthers.csv"), BEST_BOTS_AGAINST("BestBotsAgainstBestBot.csv"),
        DEMO_GAME("gamestats.csv");

        private final String fileName;

        CsvCategory(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    private final int numSessions;
    private final Map<Player, PlayerStatistics> playerStatisticsMap;
    private final boolean saveStatsToCsv;
    private static final String FOLDER_DEFAULT_PATH = "src/main/resources/stats/";
    private final CsvCategory csvCategory;

    public GameStatisticsAnalyzer(int numSessions, boolean saveStatsToCsv, CsvCategory csvCategory) {
        this.numSessions = numSessions;
        this.playerStatisticsMap = new HashMap<>();
        this.saveStatsToCsv = saveStatsToCsv;
        this.csvCategory = csvCategory;
    }

    public GameStatisticsAnalyzer(boolean saveStatsToCsv) {
        this(0, saveStatsToCsv, DEMO_GAME);
    }

    public void runDemo() {
        CustomLogger.setLevel(saveStatsToCsv ? Level.OFF : Level.INFO);
        Bank.reset();
        Player.resetIdCounter();
        Game game = new GameBuilder(new Cli(), new Deck())
                .addRandomBot()
                .addRandomBot()
                .addRandomBot()
                .addRichardBot()
                .addSmartBot()
                .addCustomBot(null, new ChoosingCharacterToTargetFirstPlayer(),
                        new UsingThiefEffectToFocusRusher(),
                        new UsingMurdererEffectToFocusRusher(),
                        new UsingCondottiereEffectToTargetFirstPlayer(),
                        new CardChoosingStrategy())
                .build();
        game.run();
        analyzeGameResults(game);
        logAggregatedStatistics();
    }

    /**
     * Runs and analyzes multiple game sessions with the specified number of random bots, smart bots, and custom bots.
     * This method runs multiple game sessions, resets player IDs and the bank for each session,
     * creates a new game instance, runs the game, logs the completion of each game session,
     * analyzes the results of each game, logs aggregated statistics, and optionally saves statistics to a CSV file.
     *
     * @param numRandomBot The number of random bots to be included in each game session.
     * @param numSmartBot  The number of smart bots to be included in each game session.
     * @param numCustomBot The number of custom bots to be included in each game session.
     */
    public void runAndAnalyze(int numRandomBot, int numSmartBot, int numCustomBot) {
        CustomStatisticsLogger.setLevel(Level.INFO);
        CustomLogger.setLevel(Level.OFF);
        for (int i = 0; i < numSessions; i++) {
            Player.resetIdCounter();
            Bank.reset();
            Game game = createGame(numRandomBot, numSmartBot, numCustomBot);
            game.run();
            CustomStatisticsLogger.log(Level.INFO, "Game {0} completed", new Object[]{i + 1});
            analyzeGameResults(game);
        }

        // Log aggregated statistics
        logAggregatedStatistics();
    }

    /**
     * Analyzes the results of a game and updates player statistics accordingly.
     * This method analyzes the results of the provided game, including player scores and placements.
     * It updates the player statistics map based on the game results, including the total number of games played,
     * games won, games lost, games played in the last position, and the scores obtained by each player.
     * Additionally, it records the placement of each player in the game.
     *
     * @param game The game instance to analyze the results of.
     */
    public void analyzeGameResults(Game game) {
        List<Player> sortedPlayers = new ArrayList<>(game.getPlayers());
        sortedPlayers.sort(Comparator.comparingInt(Player::getScore).reversed());

        for (Player player : game.getPlayers()) {
            PlayerStatistics playerStats = playerStatisticsMap.computeIfAbsent(player, k -> new PlayerStatistics());
            playerStats.incrementTotalGames();

            if (sortedPlayers.indexOf(player) == 0) {
                playerStats.incrementGamesWon();
            } else {
                playerStats.incrementGamesLost();
            }

            playerStats.addScore(player.getScore());
            playerStats.recordPlacement(sortedPlayers.indexOf(player) + 1);

            // Increment the placement counts for each player
            playerStats.recordPlacement(sortedPlayers.indexOf(player) + 1);
        }
    }

    /**
     * Logs aggregated player statistics.
     * This method logs the aggregated player statistics, including player name, total games played, games won,
     * games lost, games played in the last session, average score, winning percentage, and detailed placement.
     * The statistics are logged as a formatted table.
     */
    void logAggregatedStatistics() {
        CustomStatisticsLogger.log(Level.INFO, "Aggregated Player Statistics:");
        // Header for the table
        StringBuilder table = getStringBuilder();
        // Iterate through players and construct the row for each player
        if (saveStatsToCsv) {
            loadStatsFromCsv();
        }
        for (Map.Entry<Player, PlayerStatistics> entry : playerStatisticsMap.entrySet()) {
            Player player = entry.getKey();
            PlayerStatistics stats = entry.getValue();

            // Constructing the row for each player with dynamic Pos columns
            List<String> placementDetails = new ArrayList<>();
            Map<Integer, Integer> detailedPlacement = stats.getDetailedPlacement();
            for (int i = 1; i <= this.playerStatisticsMap.size(); i++) {
                placementDetails.add(String.valueOf(detailedPlacement.getOrDefault(i, 0)));
            }
            StringBuilder row = new StringBuilder(String.format("| %-18s| %-12d| %-10d| %-11d| %-14.3f| %-19.1f|",
                    player.toString(), stats.getTotalGames(), stats.getGamesWon(),
                    stats.getGamesLost(), stats.getAverageScore(),
                    stats.getWinningPercentage()));

            for (String posDetail : placementDetails) {
                row.append(String.format(" %-6s|", posDetail));
            }

            row.append("\n");
            table.append(row);
        }
        if (saveStatsToCsv) {
            saveStatsIntoCsv();
        }
        // Output the formatted table
        CustomStatisticsLogger.log(Level.INFO, table.toString());
    }

    private StringBuilder getStringBuilder() {
        // Construct header with dynamic Pos columns
        StringBuilder header = new StringBuilder("| Player            | Total Games | Games Won | Games Lost | Average Score | Winning Percentage |");
        for (int i = 1; i <= this.playerStatisticsMap.size(); i++) {
            header.append(String.format(" Pos %d |", i));
        }
        header.append("\n");
        header.append("|-------------------|-------------|-----------|------------|---------------|--------------------|");
        header.append("-------|".repeat(this.playerStatisticsMap.size()));
        header.append("\n");
        return header;
    }

    /**
     * Creates a new game instance based on the specified number of random bots, smart bots, and custom bots.
     * This method initializes a game builder with a command-line interface and a deck, then adds the desired number
     * of random bots, smart bots, and custom bots to the game builder. Finally, it builds and returns the constructed game.
     *
     * @param numRandomBots The number of random bots to be added to the game.
     * @param numSmartBots  The number of smart bots to be added to the game.
     * @param numCustomBots The number of custom bots to be added to the game.
     * @return The newly created game instance.
     */
    Game createGame(int numRandomBots, int numSmartBots, int numCustomBots) {
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

    /**
     * Generates a random custom bot for the game using various strategies.
     * This method adds a custom bot to the game being built by the provided {@code gameBuilder}.
     * The bot is configured with different strategies for character choosing, using thief effect,
     * using murderer effect, using condottiere effect, and card choosing.
     *
     * @param gameBuilder The builder object for the game to which the custom bot will be added.
     */
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

    /**
     * Saves the player statistics into a CSV file.
     * Writes the player statistics including player name, total games played, games won,
     * games lost, games played in the last session, average score, winning percentage, and detailed placement
     * into a CSV file.
     */
    private void saveStatsIntoCsv() {
        createCsvFile();
        try (CSVWriter writer = new CSVWriter(new FileWriter(FOLDER_DEFAULT_PATH + this.csvCategory.getFileName(), false))) {
            // Write the header only if the file is empty
            String[] header = new String[]{"Player", "Total Games", "Games Won", "Games Lost", "Average Score", "Winning Percentage"};
            // Add the positions in function of the number of players
            for (int i = 1; i <= 6; i++) {
                header = Arrays.copyOf(header, header.length + 1);
                header[header.length - 1] = "Pos " + i;
            }
            writer.writeNext(header);

            // Check there is the same bot in the csv file

            // Write data for each player
            for (Map.Entry<Player, PlayerStatistics> entry : playerStatisticsMap.entrySet()) {
                Player player = entry.getKey();
                PlayerStatistics stats = entry.getValue();
                List<String> row = new ArrayList<>(Arrays.asList(
                        player.toString(),
                        String.valueOf(stats.getTotalGames()),
                        String.valueOf(stats.getGamesWon()),
                        String.valueOf(stats.getGamesLost()),
                        String.valueOf(stats.getAverageScore()),
                        String.valueOf(stats.getWinningPercentage()),
                        String.valueOf(stats.getDetailedPlacement().get(1)),
                        String.valueOf(stats.getDetailedPlacement().get(2)),
                        String.valueOf(stats.getDetailedPlacement().get(3)),
                        String.valueOf(stats.getDetailedPlacement().get(4)),
                        String.valueOf(stats.getDetailedPlacement().get(5)),
                        String.valueOf(stats.getDetailedPlacement().get(6))
                ));
                writer.writeNext(row.toArray(new String[0]));
            }
        } catch (IOException e) {
            CustomStatisticsLogger.log(Level.SEVERE, "Error occurred while writing to CSV file: {0}", new Object[]{e.getMessage()});
        }
    }

    private boolean isTheSameBotInCsvFile() {
        try (CSVReader reader = new CSVReader(new FileReader(FOLDER_DEFAULT_PATH + this.csvCategory.getFileName()))) {
            // Skip header
            reader.skip(1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // Extract data from CSV
                String playerName = nextLine[0];
                if (
                    // if one key (player.toString) of the map is not in the csv file
                        !playerStatisticsMap.keySet().stream().map(Player::toString).toList().contains(playerName)
                ) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            CustomStatisticsLogger.log(Level.SEVERE, "Error occurred while reading from CSV file: {0}", new Object[]{e.getMessage()});
        }
        return false;
    }

    /**
     * Creates a CSV file for storing player statistics if it doesn't exist.
     * If the folder specified by {@code FOLDER_DEFAULT_PATH} doesn't exist, it creates the folder.
     * Then, it checks if the CSV file specified by {@code CSV_FILE_NAME} exists,
     * and if not, it creates the file.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createCsvFile() {
        try {
            // Create the folder if it doesn't exist
            File folder = new File(FOLDER_DEFAULT_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Create the file if it doesn't exist
            File file = new File(FOLDER_DEFAULT_PATH + this.csvCategory.getFileName());
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            CustomStatisticsLogger.log(Level.SEVERE, "Error occurred while creating file: {0}", new Object[]{e.getMessage()});
        }
    }

    private void loadStatsFromCsv() {
        try {
            if (fileExists() && isTheSameBotInCsvFile()) {
                try (CSVReader reader = new CSVReader(new FileReader(FOLDER_DEFAULT_PATH + this.csvCategory.getFileName()))) {
                    // Skip header
                    reader.skip(1);
                    String[] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        // Extract data from CSV
                        String playerName = nextLine[0];
                        int totalGames = Integer.parseInt(nextLine[1]);
                        int gamesWon = Integer.parseInt(nextLine[2]);
                        int gamesLost = Integer.parseInt(nextLine[3]);
                        double averageScore = Double.parseDouble(nextLine[4]);
                        Map<Integer, Integer> detailedPlacement = new HashMap<>();
                        for (int i = 6; i < nextLine.length; i++) {
                            detailedPlacement.put(i - 5, Integer.parseInt(nextLine[i]));
                        }

                        // Update existing statistics or add new ones
                        Player player = getPlayerByName(playerName);
                        if (player != null) {
                            PlayerStatistics stats = playerStatisticsMap.get(player);
                            if (stats != null) {
                                stats.setTotalGames(totalGames);
                                stats.setGamesWon(gamesWon);
                                stats.setGamesLost(gamesLost);
                                stats.setAverageScore(averageScore, totalGames);
                                stats.setWinningPercentage(stats.getWinningPercentage());
                                stats.setDetailedPlacement(detailedPlacement);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            CustomStatisticsLogger.log(Level.SEVERE, "Error occurred while reading from CSV file: {0}", new Object[]{e.getMessage()});
        }
    }

    private Player getPlayerByName(String playerName) {
        // Implement logic to retrieve player instance by name
        // You may need to iterate over game players or use a player repository
        // For demonstration purpose, let's assume you have a list of players in the game
        // You may need to modify this logic based on how players are managed in your system
        for (Player player : playerStatisticsMap.keySet()) {
            if (player.toString().equals(playerName)) {
                return player;
            }
        }
        return null;
    }


    private boolean fileExists() {
        File folder = new File(FOLDER_DEFAULT_PATH);
        if (!folder.exists()) {
            return false;
        }

        File file = new File(FOLDER_DEFAULT_PATH + this.csvCategory.getFileName());
        return file.exists();
    }
}
