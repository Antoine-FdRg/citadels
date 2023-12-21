package com.seinksansdoozebank.fr.model.character.commonCharacters;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.CliLogger;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class BishopTest {
    List<Card> citadel;
    Player player;
    Bishop bishop;
    IView view;
    Deck deck;

    @BeforeEach
    void setUp() {
        // Create a player
        view = mock(CliLogger.class);
        deck = mock(Deck.class);
        player = new RandomBot(2, deck, view);
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
