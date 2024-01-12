package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameFactoryTest {
    IView view;
    @BeforeEach
    void setUp() {
        view = mock(IView.class);
    }

    @Test
    void createGameOfRandomBotWithLessThan4BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRandomBot(view, 3));
    }

    @Test
    void createGameOfRandomBotWithMoreThan6BotThrowException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRandomBot(view, 7));
    }

    @Test
    void createGameOfRandomBotWith4Bot() {
        Game game = GameFactory.createGameOfRandomBot(view, 4);
        assertEquals(4, game.players.size());
    }
}