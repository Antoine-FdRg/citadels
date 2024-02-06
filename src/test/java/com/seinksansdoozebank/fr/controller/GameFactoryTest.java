package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.RichardBot;
import com.seinksansdoozebank.fr.model.player.SmartBot;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBot;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void createGameOfSmartBotWith4Bot() {
        Game game = GameFactory.createGameOfSmartBot(view, 4);
        assertEquals(4, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(SmartBot.class, player));
    }

    @Test
    void createGameOfSmartBotWith5Bot() {
        Game game = GameFactory.createGameOfSmartBot(view, 5);
        assertEquals(5, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(SmartBot.class, player));
    }

    @Test
    void createGameOfSmartBotWith6Bot() {
        Game game = GameFactory.createGameOfSmartBot(view, 6);
        assertEquals(6, game.players.size());
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

    @Test
    void createGameOfCustomBotWith4Bot() {
        Game game = GameFactory.createGameOfCustomBot(view, 4);
        assertEquals(4, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(CustomBot.class, player));
    }

    @Test
    void createGameOfCustomBotWith5Bot() {
        Game game = GameFactory.createGameOfCustomBot(view, 5);
        assertEquals(5, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(CustomBot.class, player));
    }

    @Test
    void createGameOfCustomBotWith6Bot() {
        Game game = GameFactory.createGameOfCustomBot(view, 6);
        assertEquals(6, game.players.size());
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

    @Test
    void createGameOfRichardBotWith4Bot() {
        Game game = GameFactory.createGameOfRichardBot(view, 4);
        assertEquals(4, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(RichardBot.class, player));
    }

    @Test
    void createGameOfRichardBotWith5Bot() {
        Game game = GameFactory.createGameOfRichardBot(view, 5);
        assertEquals(5, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(RichardBot.class, player));
    }

    @Test
    void createGameOfRichardBotWith6Bot() {
        Game game = GameFactory.createGameOfRichardBot(view, 6);
        assertEquals(6, game.players.size());
        // Check the type of the players
        game.players.forEach(player -> assertInstanceOf(RichardBot.class, player));
    }

    @Test
    void createGameOfRichardBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRichardBot(view, 7));
    }

    @Test
    void createGameOfAllTypesOfBotWithLessThan4BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfAllTypeOfBot(view, 3));
    }

    @Test
    void createGameOfAllTypesOfBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfAllTypeOfBot(view, 7));
    }
}