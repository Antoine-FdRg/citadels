package com.seinksansdoozebank.fr.view.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomFormatterTest {

    private CustomFormatter customFormatter;

    @BeforeEach
    void setUp() {
        customFormatter = new CustomFormatter();
    }

    @AfterEach
    void tearDown() {
        customFormatter = null;
    }

    @Test
    void testFormatWithParameters() {
        String message = "Hello, {0}!";
        String parameter = "World";
        LogRecord logRecord = new LogRecord(Level.INFO, message);
        logRecord.setParameters(new Object[]{parameter});

        String formattedMessage = customFormatter.format(logRecord);

        assertEquals("Hello, World!\n", formattedMessage);
    }

    @Test
    void testFormatWithoutParameters() {
        String message = "This is a test message.";
        LogRecord logRecord = new LogRecord(Level.INFO, message);

        String formattedMessage = customFormatter.format(logRecord);

        assertEquals("This is a test message.\n", formattedMessage);
    }
}
