package com.seinksansdoozebank.fr.view.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    public static void log(Level level, String message, Object[] params) {
        logger.log(level, message, params);
    }

    public static void log(Level level, String message, Object param) {
        logger.log(level, message, param);
    }

    public static void setLevel(Level level) {
        logger.setLevel(level);
    }

}
