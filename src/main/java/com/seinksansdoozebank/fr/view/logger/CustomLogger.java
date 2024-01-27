package com.seinksansdoozebank.fr.view.logger;

import com.seinksansdoozebank.fr.model.player.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        throw new IllegalStateException("Utility class"); // sonarlint suggestion
    }

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

    protected static String applyColor(Player player, String message) {
        setPlayerColors(player);
        return playerColors.getOrDefault(player, "") + message + "\u001B[0m";
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    public static void log(Level level, String message, Object[] params) {
        for (Object param : params) {
            if (param instanceof Player player) {
                message = applyColor(player, message);
                message = MessageFormat.format(message, params);
                break;
            }
        }
        logger.log(level, message);
    }

    public static void log(Level level, String message, Object param) {
        if (param instanceof Player player) {
            message = applyColor(player, message);
            message = MessageFormat.format(message, param);
        }
        logger.log(level, message);
    }

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
}
