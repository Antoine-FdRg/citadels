package com.seinksansdoozebank.fr.view.logger;


import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.IView;
import com.seinksansdoozebank.fr.view.TestHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.seinksansdoozebank.fr.view.TestHandler.stripAnsiCodes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class CustomLoggerTest {

    private Player player;
    private static final Logger LOGGER = Logger.getLogger(CustomLogger.class.getName());
    private Handler testHandler;

    @BeforeEach
    void setUp() {
        IView view = mock(IView.class);
        player = new RandomBot(10, new Deck(), view, new Bank());
        testHandler = new TestHandler();
        LOGGER.addHandler(testHandler);
    }

    @AfterEach
    void tearDown() {
        LOGGER.removeHandler(testHandler);
    }

    static Stream<Arguments> generateLoggingLevels() {
        return Stream.of(Arguments.of(Level.INFO), Arguments.of(Level.WARNING), Arguments.of(Level.SEVERE));
    }

    @ParameterizedTest
    @MethodSource("generateLoggingLevels")
    void testLoggingLevelInfo(Level level) {
        CustomLogger.log(level, "Test log message");
        assertLogged(level, "Test log message");
    }

    @Test
    void testLogWithMultipleParameter() {
        Card card = new Card(District.PORT);
        String expectedMessage = "Test log message with player " + player + " and card " + card;
        CustomLogger.log(Level.INFO, "Test log message with player {0} and card {1}", new Object[]{player, card}, player);
        assertLogged(Level.INFO, expectedMessage);
    }

    @Test
    void testLogWithOneParameters() {
        String expectedMessage = "Test log message with player " + player;
        CustomLogger.log(Level.INFO, "Test log message with player {0}", player);
        assertLogged(Level.INFO, expectedMessage);
    }

    /**
     * Assert that the last log record is the expected one
     *
     * @param expectedLevel   the expected level
     * @param expectedMessage the expected message
     */
    private void assertLogged(Level expectedLevel, String expectedMessage) {
        LogRecord lastLogRecord = ((TestHandler) testHandler).getLastLogRecord();
        assertEquals(expectedLevel, lastLogRecord.getLevel());
        assertEquals(expectedMessage, stripAnsiCodes(lastLogRecord.getMessage()));
        ((TestHandler) testHandler).cleanLogRecords();
    }
}
