package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

class PlayerTest {
    List<District> hand = new ArrayList<>();
    Player player;
    IView view;

    @BeforeEach
    void setup() {
        view = mock(Cli.class);
        hand.add(new District(3));
        hand.add(new District(5));
        player = new Player(10, view);
        player.addDistrictToHand(new District(3));
        player.addDistrictToHand(new District(5));
    }

    @Test
    void testPlay() {
        District playedDistrict = player.play();
        assertFalse(player.getHand().contains(playedDistrict));
        assertEquals(10 - playedDistrict.getCost(), player.getNbGold());
    }

    @Test
    void testChooseDistrict() {
        District chosenDistrict = player.chooseDistrict();
        assertTrue(hand.contains(chosenDistrict));
    }

    @Test
    void testUpdateGold() {
        // Arrange
        Player player = new Player(10,view);

        // Act
        player.decreaseGold(3);

        // Assert
        assertEquals(7, player.getNbGold());
    }

    @Test
    void testPlayerInitialization() {
        // Arrange
        List<District> hand = new ArrayList<>();
        hand.add(new District(3));
        List<District> citadel = new ArrayList<>();

        // Act
        Player player = new Player(10,view);
        player.addDistrictToHand(new District(3));

        // Assert
        assertEquals(10, player.getNbGold());
        assertEquals(hand, player.getHand());
        assertEquals(citadel, player.getCitadel());
    }

    @Test
    void testResetIdCounter() {
        // Test resetting the ID counter for player
        Player.resetIdCounter();
        Player newPlayer = new Player(10,view);
        assertEquals(1, newPlayer.getId()); // Should start counting from 1 again
    }
}
