package com.seinksansdoozebank.fr.view.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The CustomStatisticsLogger class is a custom logger for the statistics
 */
public class CustomStatisticsLogger {

    private static final Logger logger = Logger.getLogger(CustomStatisticsLogger.class.getName());

    static {
        // Remove the default console handler
        logger.setUseParentHandlers(false);

        // Create a new console handler with the custom formatter
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomFormatter());

        // Add the custom console handler to the logger
        logger.addHandler(consoleHandler);
    }

    private CustomStatisticsLogger() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Log a message with the given level
     *
     * @param level   the level of the message
     * @param message the message to log
     */
    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    /**
     * Log a message with the given level and parameters
     * @param level the level of the message
     * @param message the message to log
     * @param params the parameters of the message
     */
    public static void log(Level level, String message, Object[] params) {
        logger.log(level, message, params);
    }

    /**
     * Log a message with the given level and parameter
     * @param level the level of the message
     * @param message the message to log
     * @param param the parameter of the message
     */
    public static void log(Level level, String message, Object param) {
        logger.log(level, message, param);
    }

    /**
     * Set the level of the logger
     * @param level the level to set
     */
    public static void setLevel(Level level) {
        logger.setLevel(level);
    }

}
