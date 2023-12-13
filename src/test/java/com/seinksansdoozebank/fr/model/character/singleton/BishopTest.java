package com.seinksansdoozebank.fr.model.character.singleton;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BishopTest {
    List<District> citadel;
    Player player;
    Bishop bishop;

    @BeforeEach
    void setUp() {
        // Create a player
        player = new Player(2, null);
        // Create a list of districts for the citadel
        citadel = new ArrayList<>();
        // Add a district to the citadel
        citadel.add(District.TEMPLE);
        citadel.add(District.CHURCH);
        citadel.add(District.MARKET_PLACE);
        citadel.add(District.MONASTERY);
        citadel.add(District.CATHEDRAL);
        // Create a Bishop character
        bishop = new Bishop(citadel, player);
    }

    @Test
    void testGoldCollectedFromDistrictType() {
        // Perform the action
        bishop.performAction();

        // Check if the player's gold has been increased correctly
        assertEquals(6, player.getNbGold());
    }

    @Test
    void testToString() {
        // Perform the action
        bishop.performAction();

        // Check if the player's gold has been increased correctly
        assertEquals("Bishop gets 1 gold for each " + DistrictType.RELIGION + " district in his citadel\nBishop gets 4 gold(s)", bishop.toString());
    }
}
