package com.seinksansdoozebank.fr.model.character.commonCharacters;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MerchantTest {
    List<Card> citadel;
    Player player;
    Merchant merchant;

    @BeforeEach
    void setUp() {
        // Create a player
        player = new Player(2, null);
        // Create a list of districts for the citadel
        citadel = new ArrayList<>();
        // Add a district to the citadel
        citadel.add(new Card(District.TAVERN));
        citadel.add(new Card(District.CORNER_SHOP));
        citadel.add(new Card(District.MARKET_PLACE));
        citadel.add(new Card(District.TRADING_POST));
        citadel.add(new Card(District.PORT));
        citadel.add(new Card(District.TOWN_HALL));
        citadel.add(new Card(District.BARRACK));
        citadel.add(new Card(District.BARRACK));
        // Set the citadel to the player
        player.getCitadel().addAll(citadel);
        // Create a Bishop character
        merchant = new Merchant();
        // Set the player and the citadel to the character
        merchant.setPlayer(player);
    }

    @Test
    void testGoldCollectedFromDistrictType() {
        // Perform the action
        merchant.goldCollectedFromDisctrictType();

        // Check if the player's gold has been increased correctly
        // 2 gold for the start + 6 for the 6 districts
        assertEquals(8, player.getNbGold());
    }

    @Test
    void testUseEffect() {
        // Perform the action
        merchant.useEffect();

        // Check if the player's gold has been increased correctly
        // 2 gold for the start + 1 for the new turn + 6 for the 6 districts
        assertEquals(3, player.getNbGold());
    }
}
