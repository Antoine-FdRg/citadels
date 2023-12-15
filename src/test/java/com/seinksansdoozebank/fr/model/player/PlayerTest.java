package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commonCharacters.King;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Merchant;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

class PlayerTest {
    List<Card> hand = new ArrayList<>();
    Player player;
    IView view;

    @BeforeEach
    void setup() {
        view = mock(Cli.class);
        District districtCostThree = District.PORT;
        District districtCostFive = District.FORTRESS;
        Card cardCostThree= new Card(districtCostThree);
        Card cardCostFive= new Card(districtCostFive);
        hand.add(cardCostThree);
        hand.add(cardCostFive);
        player = new Player(10,view);
        player.addCardToHand(cardCostThree);
        player.addCardToHand(cardCostFive);
    }

    @Test
    void testPlay() {
        Card playedDistrict = player.play();
        assertFalse(player.getHand().contains(playedDistrict));
        assertEquals(10 - playedDistrict.getDistrict().getCost(), player.getNbGold());
    }

    @Test
    void testChooseCard() {
        Card chosenCard = player.chooseCard();
        assertTrue(hand.contains(chosenCard));
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
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(District.PORT)); //district with a cost of 3
        List<Card> citadel = new ArrayList<>();

        // Act
        Player player = new Player(10,view);
        player.addCardToHand(new Card(District.PORT));

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

    @Test
    void testChooseCharacter() {
        // Arrange
        List<Character> characters = new ArrayList<>();
        characters.add(new Bishop());
        characters.add(new King());
        characters.add(new Merchant());
        characters.add(new Condottiere());

        // Act
        Character character = player.chooseCharacter(characters);

        // Assert
        assertTrue(characters.contains(character));
    }
}
