package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.District;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class PlayerTest {
    List<District> hand = new ArrayList<>();
    List<District> citadel = new ArrayList<>();
    Player player;

    @BeforeEach
    void setup() {
        hand.add(new District(3));
        hand.add(new District(5));
        player = new Player(1, 10, hand, citadel);
    }

    @Test
    void testPlay() {
        District playedDistrict = player.play();
        assertTrue(citadel.contains(playedDistrict));
        assertFalse(hand.contains(playedDistrict));
        assertEquals(10 - playedDistrict.getCost(), player.nbGold);
    }

    @Test
    void testChooseDistrict() {
        District chosenDistrict = player.chooseDistrict();
        assertTrue(hand.contains(chosenDistrict));
    }

    @Test
    void testUpdateGold() {
        // Arrange
        Player player = new Player(1, 10, new ArrayList<>(), new ArrayList<>());

        // Act
        player.updateGold(3);

        // Assert
        assertEquals(7, player.nbGold);
    }

    @Test
    void testPlayerInitialization() {
        // Arrange
        List<District> hand = new ArrayList<>();
        hand.add(new District(3));
        List<District> citadel = new ArrayList<>();

        // Act
        Player player = new Player(1, 10, hand, citadel);

        // Assert
        assertEquals(1, player.id);
        assertEquals(10, player.nbGold);
        assertEquals(hand, player.hand);
        assertEquals(citadel, player.citadel);
    }
}
