package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CondottiereTest {
    List<Card> citadel;
    Player player;
    Condottiere condottiere;
    IView view;
    Deck deck;

    @BeforeEach
    void setUp() {
        // Create a player
        view = mock(Cli.class);
        deck = mock(Deck.class);
        player = spy(new RandomBot(2, deck, view));
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
        when(player.getCitadel()).thenReturn(citadel);
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

    @Test
    void cantDestroyDonjon() {
        Player otherPlayer = spy(new RandomBot(2, deck, view));
        Merchant merchant = new Merchant();
        otherPlayer.chooseCharacter(new ArrayList<>(List.of(merchant)));
        List<Card> citadel = new ArrayList<>();
        citadel.add(new Card(District.DONJON));
        when(otherPlayer.getCitadel()).thenReturn(citadel);
        assertThrows(IllegalArgumentException.class, () -> condottiere.useEffect(merchant, District.DONJON));
    }
}
