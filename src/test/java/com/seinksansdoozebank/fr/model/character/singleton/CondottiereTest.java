package com.seinksansdoozebank.fr.model.character.singleton;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CondottiereTest {
    List<Card> citadel;
    Player player;
    Condottiere condottiere;

    @BeforeEach
    void setUp() {
        // Create a player
        player = new Player(2, null);
        // Create a list of districts for the citadel
        citadel = new ArrayList<>();
        // Add a district to the citadel
        citadel.add(new Card(District.TEMPLE));
        citadel.add(new Card(District.BARRACK));
        citadel.add(new Card(District.MARKET_PLACE));
        citadel.add(new Card(District.FORTRESS));
        citadel.add(new Card(District.JAIL));
        citadel.add(new Card(District.WATCH_TOWER));
        // Set the citadel to the player
        player.getCitadel().addAll(citadel);
        // Create a Bishop character
        condottiere = new Condottiere();
        // Set the player and the citadel to the character
        condottiere.setPlayer(player);
    }

    @Test
    void testGoldCollectedFromDistrictType() {
        // Perform the action
        condottiere.goldCollectedFromDisctrictType();

        // Check if the player's gold has been increased correctly
        assertEquals(6, player.getNbGold());
    }
}
