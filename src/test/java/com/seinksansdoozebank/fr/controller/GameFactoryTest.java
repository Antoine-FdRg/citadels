package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.player.RichardBot;
import com.seinksansdoozebank.fr.model.player.SmartBot;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBot;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameFactoryTest {
    IView view;
    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickXCoin(Bank.MAX_COIN / 2);
        view = mock(IView.class);
    }

    @Test
    void createGameOfRandomBotWithLessThan4BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRandomBot(view, 3));
    }

    @Test
    void createGameOfRandomBotWith4Bot() {
        Game game = GameFactory.createGameOfRandomBot(view, 4);
        assertEquals(4, game.players.size());
    }
    @Test
    void createGameOfRandomBotWith5Bot() {
        Game game = GameFactory.createGameOfRandomBot(view, 5);
        assertEquals(5, game.players.size());
    }
    @Test
    void createGameOfRandomBotWith6Bot() {
        Game game = GameFactory.createGameOfRandomBot(view, 6);
        assertEquals(6, game.players.size());
    }

    @Test
    void createGameOfRandomBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRandomBot(view, 7));
    }

    @Test
    void createGameOfSmartBotWithLessThan4BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfSmartBot(view, 3));
    }

    @ParameterizedTest
    @CsvSource({"4, 4", "5, 5", "6, 6"})
    void createGameOfSmartBotWith4Bot(int nbPlayers, int expected) {
        Game game = GameFactory.createGameOfSmartBot(view, nbPlayers);
        assertEquals(expected, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(SmartBot.class, player));
    }

    @Test
    void createGameOfSmartBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfSmartBot(view, 7));
    }

    @Test
    void createGameOfCustomBotWithLessThan4BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfCustomBot(view, 3));
    }

    @ParameterizedTest
    @CsvSource({"4, 4", "5, 5", "6, 6"})
    void createGameOfCustomBotWith4Bot(int nbPlayers, int expected) {
        Game game = GameFactory.createGameOfCustomBot(view, nbPlayers);
        assertEquals(expected, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(CustomBot.class, player));
    }

    @Test
    void createGameOfCustomBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfCustomBot(view, 7));
    }

    @Test
    void createGameOfRichardBotWithLessThan4BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRichardBot(view, 3));
    }

    @ParameterizedTest
    @CsvSource({"4, 4", "5, 5", "6, 6"})
    void createGameOfRichardBotWith4Bot(int nbPlayers, int expected) {
        Game game = GameFactory.createGameOfRichardBot(view, nbPlayers);
        assertEquals(expected, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(RichardBot.class, player));
    }

    @Test
    void createGameOfRichardBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRichardBot(view, 7));
    }
}