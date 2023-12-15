package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameTest {

    Game game;

    @BeforeEach
    public void setUp() {
        game = new Game(4);
    }

    @Test
    void getWinner() {
        Player p1 = mock(Player.class);
        when(p1.getScore()).thenReturn(5);
        Player p2 = mock(Player.class);
        when(p2.getScore()).thenReturn(4);
        Player p3 = mock(Player.class);
        when(p3.getScore()).thenReturn(7);
        Player p4 = mock(Player.class);
        when(p4.getScore()).thenReturn(6);
        game.setPlayers(List.of(p1, p2, p3, p4));
        assertEquals(p3, game.getWinner());
    }

    @Test
    void testCharactersChoice() {
        game.createCharacters();
        assertEquals(4, game.getAvailableCharacters().size());
    }
}
