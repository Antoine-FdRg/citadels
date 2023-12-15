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

class KingTest {
    List<Card> citadel;
    Player player;
    King king;

    @BeforeEach
    void setUp() {
        // Create a player
        player = new Player(2, null);
        // Create a list of districts for the citadel
        citadel = new ArrayList<>();
        // Add a district to the citadel
        citadel.add(new Card(District.PALACE));
        citadel.add(new Card(District.CASTLE));
        citadel.add(new Card(District.MARKET_PLACE));
        citadel.add(new Card(District.MANOR));
        citadel.add(new Card(District.BARRACK));
        // Set the citadel to the player
        player.getCitadel().addAll(citadel);
        // Create a Bishop character
        king = new King();
        // Set the player and the citadel to the character
        king.setPlayer(player);
    }

    @Test
    void testGoldCollectedFromDistrictType() {
        // Perform the action
        king.goldCollectedFromDisctrictType();

        // Check if the player's gold has been increased correctly
        assertEquals(5, player.getNbGold());
    }
}
