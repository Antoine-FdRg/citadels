package com.seinksansdoozebank.fr.model.character.singleton;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CondottiereTest {
    List<District> citadel;
    Player player;
    Condottiere condottiere;

    @BeforeEach
    void setUp() {
        // Create a player
        player = new Player(2, null);
        // Create a list of districts for the citadel
        citadel = new ArrayList<>();
        // Add a district to the citadel
        citadel.add(District.TEMPLE);
        citadel.add(District.BARRACK);
        citadel.add(District.MARKET_PLACE);
        citadel.add(District.FORTRESS);
        citadel.add(District.JAIL);
        citadel.add(District.WATCH_TOWER);
        // Create a Bishop character
        condottiere = new Condottiere(citadel, player);
    }

    @Test
    void testGoldCollectedFromDistrictType() {
        // Perform the action
        condottiere.performAction();

        // Check if the player's gold has been increased correctly
        assertEquals(6, player.getNbGold());
    }

    @Test
    void testToString() {
        // Perform the action
        condottiere.performAction();

        // Check if the player's gold has been increased correctly
        assertEquals("Condottiere gets 1 gold for each " + DistrictType.SOLDIERLY + " district in his citadel\nCondottiere gets 4 gold(s)", condottiere.toString());
    }
}
