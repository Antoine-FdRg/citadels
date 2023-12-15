package com.seinksansdoozebank.fr.model.character.commonCharacters;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BishopTest {
    List<Card> citadel;
    Player player;
    Bishop bishop;

    @BeforeEach
    void setUp() {
        // Create a player
        player = new Player(2, null);
        // Create a list of districts for the citadel
        citadel = new ArrayList<>();
        // Add a district to the citadel
        citadel.add(new Card(District.TEMPLE));
        citadel.add(new Card(District.CHURCH));
        citadel.add(new Card(District.MARKET_PLACE));
        citadel.add(new Card(District.MONASTERY));
        citadel.add(new Card(District.CATHEDRAL));
        // Set the citadel to the player
        player.getCitadel().addAll(citadel);
        // Create a Bishop character
        bishop = new Bishop();
        // Set the player and the citadel to the character
        bishop.setPlayer(player);
    }

    @Test
    void testGoldCollectedFromDistrictType() {
        // Perform the action
        bishop.goldCollectedFromDisctrictType();

        // Check if the player's gold has been increased correctly
        assertEquals(6, player.getNbGold());
    }
}
