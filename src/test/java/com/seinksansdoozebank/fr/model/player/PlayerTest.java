package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commonCharacters.King;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Merchant;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class PlayerTest {
    Player player;
    Player spyPlayer;
    IView view;
    Deck deck;
    Card cardCostThree;
    Card cardCostFive;

    @BeforeEach
    void setup() {
        view = mock(Cli.class);
        deck = mock(Deck.class);
        cardCostThree= new Card(District.BARRACK);
        cardCostFive= new Card(District.FORTRESS);
        player = new RandomBot(10, deck, view);
        spyPlayer = spy(new RandomBot(10, deck, view));
    }

    @Test
    void testPickGold(){
        Player player = new RandomBot(10, deck, view);
        player.pickGold();
        assertEquals(12, player.getNbGold());
    }

    @Test
    void testBuildADistrictWithEmptyOptChosenCardShouldReturnEmpty() {
        when(spyPlayer.chooseCard()).thenReturn(Optional.empty());
        assertTrue(spyPlayer.playACard().isEmpty());
    }

    @Test
    void testPlayACardWithUnplayableChosenCardShouldReturnEmpty() {
        doReturn(false).when(spyPlayer).canPlayCard(any(Card.class));
        assertTrue(spyPlayer.playACard().isEmpty());
    }

    @Test
    void testPlayACardWithPlayableChosenCardShouldReturnCard() {
        spyPlayer.getHand().add(cardCostThree);
        doReturn(Optional.of(cardCostThree)).when(spyPlayer).chooseCard();
        doReturn(true).when(spyPlayer).canPlayCard(any(Card.class));

        Optional<Card> optPlayedCard = spyPlayer.playACard();
        assertTrue(optPlayedCard.isPresent());
        Card playedCard = optPlayedCard.get();

        assertFalse(spyPlayer.getHand().contains(cardCostThree));
        assertTrue(spyPlayer.getCitadel().contains(cardCostThree));
        verify(spyPlayer, times(1)).canPlayCard(cardCostThree);
        assertEquals(cardCostThree, playedCard);
    }

    @Test
    void testCanPlayCardWithTooExpensiveCardShouldReturnFalse() {
        spyPlayer.decreaseGold(10);
        Card tooExpensiveCard = cardCostFive;
        assertFalse(spyPlayer.getCitadel().contains(tooExpensiveCard));
        assertFalse(spyPlayer.canPlayCard(tooExpensiveCard));
    }
    @Test
    void testCanPlayCardWithAlreadyPlayedCardShouldReturnFalse() {
        spyPlayer.getCitadel().add(cardCostThree);
        assertTrue(spyPlayer.getCitadel().contains(cardCostThree));
        assertTrue(cardCostThree.getDistrict().getCost() <= spyPlayer.getNbGold());
    }

    @Test
    void testCanPlayCardWithPlayableCardShouldReturnTrue() {
        assertFalse(spyPlayer.getCitadel().contains(cardCostThree));
        assertTrue(cardCostThree.getDistrict().getCost() <= spyPlayer.getNbGold());
        assertTrue(spyPlayer.canPlayCard(cardCostThree));
    }

    @Test
    void testDecreaseGold() {
        Player player = new RandomBot(10, deck, view);

        player.decreaseGold(3);

        assertEquals(7, player.getNbGold());
    }

    @Test
    void testResetIdCounter() {
        // Test resetting the ID counter for player
        Player.resetIdCounter();
        Player newPlayer = new RandomBot(10, deck, view);
        assertEquals(1, newPlayer.getId()); // Should start counting from 1 again
    }

    @Test
    void testGetScoreWithEmptyCitadel() {
        assertTrue(player.getCitadel().isEmpty());
        assertEquals(0, player.getScore());
    }

    @Test
    void testGetScoreWithSomeDistrictInCitadel() {
        player.getCitadel().add(cardCostThree);
        player.getCitadel().add(cardCostFive);
        int sum = cardCostThree.getDistrict().getCost() + cardCostFive.getDistrict().getCost();
        assertEquals(sum, player.getScore());
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

    @Test
    void isTheKingWithAPlayerBeingTheKing() {
        List<Character> characters = new ArrayList<>();
        characters.add(new King());

        player.chooseCharacter(characters);

        assertTrue(player.isTheKing());
    }
    @Test
    void isTheKingWithAPlayerNotBeingTheKing() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Bishop());

        player.chooseCharacter(characters);

        assertFalse(player.isTheKing());
    }
    @Test
    void isTheKingWithAPlayerWithNoCharacter() {
        assertFalse(player.isTheKing());
    }
}
