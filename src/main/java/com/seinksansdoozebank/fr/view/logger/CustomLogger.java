package com.seinksansdoozebank.fr.view.logger;

import com.seinksansdoozebank.fr.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The CustomLogger class is a custom logger for the game
 */
public class CustomLogger {

    private static final Logger logger = Logger.getLogger(CustomLogger.class.getName());

    private static final HashMap<Player, String> playerColors = new HashMap<>();
    private static final Random random = new Random();

    private static List<String> availableColors = new ArrayList<>(List.of(
            "\u001B[38;5;21m", // dark blue
            "\u001B[38;5;32m", // light blue
            "\u001B[38;5;129m", // purple
            "\u001B[38;5;199m", // pink
            "\u001B[38;5;28m", // dark green
            "\u001B[38;5;40m", // light green
            "\u001B[38;5;202m", // orange
            "\u001B[38;5;124m" // red
    ));


    static {
        // Remove the default console handler
        logger.setUseParentHandlers(false);

        // Create a new console handler with the custom formatter
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomFormatter());

        // Add the custom console handler to the logger
        logger.addHandler(consoleHandler);
    }

    private CustomLogger() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Set the color of the player
     *
     * @param player the player to set the color
     */
    protected static void setPlayerColors(Player player) {
        if (playerColors.containsKey(player)) {
            return;
        }
        int index = random.nextInt(availableColors.size());
        String randomColor = availableColors.get(index);
        // remove the color from the list so that it can't be used again
        availableColors.remove(index);
        playerColors.put(player, randomColor);
    }

    /**
     * Apply the color to the message
     * @param player the player
     * @param message the message
     * @return the message with the color
     */
    protected static String applyColor(Player player, String message) {
        setPlayerColors(player);
        return playerColors.getOrDefault(player, "") + message + "\u001B[0m";
    }

    /**
     * Log a message
     * @param level the level of the message
     * @param message the message
     */
    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    /**
     * Log a message with a player
     * @param level the level of the message
     * @param message the message
     * @param params the parameters of the message
     * @param colorPlayer the player to apply the color
     */
    public static void log(Level level, String message, Object[] params, Player colorPlayer) {
        message = applyColor(colorPlayer, message);
        logger.log(level, message, params);
    }

    /**
     * Log a message with a player
     * @param level the level of the message
     * @param message the message
     * @param param the parameter of the message
     */
    public static void log(Level level, String message, Object param) {
        if (param instanceof Player player) {
            message = applyColor(player, message);
        }
        logger.log(level, message, param);
    }

    /**
     * Reset the available colors
     */
    public static void resetAvailableColors() {
        availableColors = new ArrayList<>(List.of(
                "\u001B[38;5;21m", // dark blue
                "\u001B[38;5;32m", // light blue
                "\u001B[38;5;129m", // purple
                "\u001B[38;5;199m", // pink
                "\u001B[38;5;28m", // dark green
                "\u001B[38;5;40m", // light green
                "\u001B[38;5;202m", // orange
                "\u001B[38;5;124m" // red
        ));
    }

    /**
     * Set the level of the logger
     * @param level the level to set
     */
    public static void setLevel(Level level) {
        logger.setLevel(level);
    }
}
