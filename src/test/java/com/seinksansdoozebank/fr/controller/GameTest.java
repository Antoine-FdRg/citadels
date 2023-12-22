package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.character.commonCharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commonCharacters.King;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Merchant;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void testCreateCharacters() {
        // Test the createCharacters method
        game.createCharacters();

        List<Character> availableCharacters = game.getAvailableCharacters();

        // Check if the correct number of characters is created
        assertEquals(4, availableCharacters.size()); // Adjust the expected size based on your implementation
    }

    @Test
    void testPlayersChooseCharacters() {
        // Test the playersChooseCharacters method
        List<Player> players = game.players;

        // Test that createCharacters create a list of characters
        game.createCharacters();
        List<Character> charactersList = List.of(
            new King(),
            new Bishop(),
            new Merchant(),
            new Condottiere()
        );

        // Test that the game available characters list is equal to charactersList
        assertEquals(charactersList, game.getAvailableCharacters());

        // Test that all players have choose a character
        game.playersChooseCharacters();

        // Check if all players have a character
        for (Player player : players) {
            assertNotNull(player.getCharacter());
        }

        // Check if all characters were removed from the available characters list
        assertEquals(0, game.getAvailableCharacters().size());

        // Reset the available characters list
        game.retrieveCharacters();

        // Check if the available characters list is equal to charactersList
        assertEquals(4, game.getAvailableCharacters().size());
    }
}
