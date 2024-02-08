package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.RichardBot;
import com.seinksansdoozebank.fr.model.player.SmartBot;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBot;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class GameFactoryTest {
    IView view;
    Bank bank;

    @BeforeEach
    void setUp() {
        view = mock(IView.class);
        bank = mock(Bank.class);
    }

    @Test
    void createGameOfRandomBotWithLessThan3BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRandomBot(view, bank, 2));
    }

    @ParameterizedTest
    @CsvSource({"3, 3", "4, 4", "5, 5", "6, 6"})
    void createGameOfRandomBotWithNBot(int nbPlayers, int expected) {
        Game game = GameFactory.createGameOfRandomBot(view, bank, nbPlayers);
        assertEquals(expected, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(RandomBot.class, player));
    }

    @Test
    void createGameOfRandomBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRandomBot(view, bank, 7));
    }

    @Test
    void createGameOfSmartBotWithLessThan3BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfSmartBot(view, bank, 2));
    }

    @ParameterizedTest
    @CsvSource({"3, 3", "4, 4", "5, 5", "6, 6"})
    void createGameOfSmartBotWithXBots(int nbPlayers, int expected) {
        Game game = GameFactory.createGameOfSmartBot(view, bank, nbPlayers);
        assertEquals(expected, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(SmartBot.class, player));
    }

    @Test
    void createGameOfSmartBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfSmartBot(view, bank, 7));
    }

    @Test
    void createGameOfCustomBotWithLessThan3BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfCustomBot(view, bank, 2));
    }

    @ParameterizedTest
    @CsvSource({"3, 3", "4, 4", "5, 5", "6, 6"})
    void createGameOfCustomBotWith4Bot(int nbPlayers, int expected) {
        Game game = GameFactory.createGameOfCustomBot(view, bank, nbPlayers);
        assertEquals(expected, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(CustomBot.class, player));
    }

    @Test
    void createGameOfCustomBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfCustomBot(view, bank, 7));
    }

    @Test
    void createGameOfRichardBotWithLessThan3BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRichardBot(view, bank, 2));
    }

    @ParameterizedTest
    @CsvSource({"3, 3", "4, 4", "5, 5", "6, 6"})
    void createGameOfRichardBotWith4Bot(int nbPlayers, int expected) {
        Game game = GameFactory.createGameOfRichardBot(view, bank, nbPlayers);
        assertEquals(expected, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(RichardBot.class, player));
    }

    @Test
    void createGameOfRichardBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRichardBot(view, bank, 7));
    }

    @ParameterizedTest
    @CsvSource(
            {
                    "1, 1, 1, 1, 1, 1, 6", // Game of 6 players
                    "1, 1, 1, 1, 1, 0, 5", // Game of 5 players
                    "1, 1, 1, 0, 1, 1, 5", // Game of 5 players
                    "1, 1, 1, 1, 0, 0, 4", // Game of 4 players
                    "1, 2, 0, 1, 0, 0, 4", // Game of 5 players
                    "1, 0, 0, 1, 4, 0, 6", // Game of 6 players
            })
    void createCustomGameWithNPlayers(int numRandomBots, int numSmartBots, int numCustomBots, int numRichardBots, int numBuilderBots, int numOpportunistBots, int expected) {
        Game game = GameFactory.createCustomGame(numRandomBots, numSmartBots, numCustomBots, numRichardBots, numBuilderBots, numOpportunistBots);
        assertEquals(expected, game.players.size());
    }

    @Test
    void createCustomGameWith7PlayersThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createCustomGame(1, 1, 1, 1, 1, 2));
    }

    @Test
    void createCustomGameWith2PlayersThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createCustomGame(0, 1, 0, 0, 1, 0));
    }
}