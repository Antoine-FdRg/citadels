package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        cardCostThree = new Card(District.BARRACK);
        cardCostFive = new Card(District.FORTRESS);
        player = new RandomBot(10, deck, view);
        spyPlayer = spy(new RandomBot(10, deck, view));
    }

    @Test
    void testPickGold() {
        Player player = new RandomBot(10, deck, view);
        player.pickGold();
        assertEquals(12, player.getNbGold());
    }

    @Test
    void testPickGoldWith3Gold3ShouldGiveThePlayer3Gold3AndLogIt() {
        IView view = mock(Cli.class);
        Player player = new RandomBot(3, deck, view);
        player.pickGold(3);
        assertEquals(6, player.getNbGold());
        verify(view, times(1)).displayPlayerPicksGold(player, 3);
    }

    @Test
    void testPickGoldWith0GoldShouldGiveThePlayer0GoldAndLogNothing() {
        IView view = mock(Cli.class);
        Player player = new RandomBot(0, deck, view);
        player.pickGold(0);
        assertEquals(0, player.getNbGold());
        verify(view, times(0)).displayPlayerPicksGold(player, 0);
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
        when(spyPlayer.getCitadel()).thenReturn(List.of(cardCostThree));
        assertFalse(spyPlayer.canPlayCard(cardCostThree));
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
        when(spyPlayer.getCitadel()).thenReturn(List.of(cardCostThree, cardCostFive));
        int sum = cardCostThree.getDistrict().getCost() + cardCostFive.getDistrict().getCost();
        assertEquals(sum, spyPlayer.getScore());
    }

    @Test
    void retrieveCharacter() {
        List<Character> characters = new ArrayList<>();
        Character condottiere = new Condottiere();
        characters.add(condottiere);
        player.chooseCharacter(characters);
        player.reveal();
        Character retrievedCharacter = player.retrieveCharacter();

        assertEquals(condottiere, retrievedCharacter);
        assertThrows(IllegalStateException.class, () -> player.retrieveCharacter());
        assertNull(condottiere.getPlayer());
    }

    @Test
    void getScoreWithBonusTest() {
        List<Card> spyPlayerCitadel = new ArrayList<>(List.
                of(new Card(District.PORT),
                        new Card(District.PALACE)));
        when(spyPlayer.getCitadel()).
                thenReturn(spyPlayerCitadel);

        spyPlayer.addBonus(4);
        int sum = (new Card(District.PORT)).getDistrict().getCost() + (new Card(District.PALACE)).getDistrict().getCost() + spyPlayer.getBonus();
        assertEquals(sum, spyPlayer.getScore());
    }

    @Test
    void pickACard() {
        int handSize = spyPlayer.hand.size();
        when(deck.pick()).thenReturn(Optional.of(cardCostFive));
        this.spyPlayer.pickACard();
        assertEquals(handSize + 1, spyPlayer.hand.size());
    }

    @Test
    void switchHandBetweenTwoPlayersWithTwoFilledHand() {
        spyPlayer.hand.add(cardCostFive);
        RandomBot otherPlayer = new RandomBot(10, deck, view);
        otherPlayer.hand.add(cardCostThree);

        spyPlayer.switchHandWith(otherPlayer);

        assertEquals(cardCostThree, spyPlayer.hand.get(0));
        assertEquals(cardCostFive, otherPlayer.hand.get(0));
    }

    @Test
    void switchHandBetweenTwoPlayersWithOneFilledHand() {
        spyPlayer.hand.add(cardCostFive);
        RandomBot otherPlayer = new RandomBot(10, deck, view);

        spyPlayer.switchHandWith(otherPlayer);

        assertEquals(cardCostFive, otherPlayer.hand.get(0));
        assertTrue(spyPlayer.hand.isEmpty());
    }

    @Test
    void switchHandBetweenTwoPlayersWithTwoEmptyHand() {
        RandomBot otherPlayer = new RandomBot(10, deck, view);

        spyPlayer.switchHandWith(otherPlayer);

        assertTrue(spyPlayer.hand.isEmpty());
        assertTrue(otherPlayer.hand.isEmpty());
    }

    @Test
    void discardACard() {
        spyPlayer.hand.add(cardCostFive);
        spyPlayer.discardACard(cardCostFive);
        assertTrue(spyPlayer.hand.isEmpty());
        verify(deck, times(1)).discard(cardCostFive);
    }

    @Test
    void playerWithArchitectCharacterShouldGet3DistrictsAfterPlay() {
        Player spyPlayerSmart = spy(new SmartBot(10, deck, view));
        when(spyPlayerSmart.chooseCard()).thenReturn(Optional.empty());
        when(deck.pick()).thenReturn(Optional.of(new Card(District.MANOR)));
        spyPlayerSmart.chooseCharacter(new ArrayList<>(List.of(new Architect())));
        spyPlayerSmart.play();

        // assert between 2 and 3 districts are gained
        assertTrue(spyPlayerSmart.getHand().size() >= 2 && spyPlayerSmart.getHand().size() <= 3);
    }

    @Test
    void playCardsWithUncorrectBoundaries() {
        spyPlayer.chooseCharacter(new ArrayList<>(List.of(new Architect())));

        assertThrows(IllegalArgumentException.class, () -> spyPlayer.buyXCardsAndAddThemToCitadel(-1));
        assertThrows(IllegalArgumentException.class, () -> spyPlayer.buyXCardsAndAddThemToCitadel(0));
        assertThrows(IllegalArgumentException.class, () -> spyPlayer.buyXCardsAndAddThemToCitadel(5));
    }

    /**
     * We verify if a player has already 8 districts then he can't play another card
     */
    @Test
    void canPlayCardWhenAlreadyHaveEightDistrict() {
        List<Card> citadelle = new ArrayList<>(List.of(new Card(District.PALACE),
                new Card(District.MANOR),
                new Card(District.BARRACK),
                new Card(District.OBSERVATORY),
                new Card(District.UNIVERSITY),
                new Card(District.CORNER_SHOP),
                new Card(District.CASTLE),
                new Card(District.LIBRARY)));
        when(spyPlayer.getCitadel()).thenReturn(citadelle);
        assertFalse(spyPlayer.canPlayCard(new Card(District.PALACE)));
    }

    @Test
    void playCardWithAGivenCard() {
        spyPlayer.getHand().add(new Card(District.TEMPLE));
        doReturn(true).when(spyPlayer).canPlayCard(new Card(District.TEMPLE));
        spyPlayer.buyACardAndAddItToCitadel(new Card(District.TEMPLE));
        assertFalse(spyPlayer.getHand().contains(new Card(District.TEMPLE)));
        assertTrue(spyPlayer.getCitadel().contains(new Card(District.TEMPLE)));
    }

    @Test
    void playWithObservatoryInCitadelle() {
        when(spyPlayer.getCitadel()).thenReturn(List.of(new Card(District.TEMPLE), new Card(District.OBSERVATORY)));
        when(deck.pick()).thenReturn(Optional.of(new Card(District.MANOR)));
        spyPlayer.pickCardsKeepSomeAndDiscardOthers();
        verify(view, times(1)).displayPlayerHasGotObservatory(spyPlayer);
        verify(spyPlayer.deck, times(3)).pick();
    }

    @Test
    void numberOfCardsToPickWhenTheBotHasObservatoryInHisCitadelTest() {
        when(spyPlayer.getCitadel()).thenReturn(List.of(new Card(District.TEMPLE), new Card(District.OBSERVATORY)));
        assertEquals(3, spyPlayer.numberOfCardsToPick());
    }


    @Test
    void numberOfCardsToPickWhenTheBotHasNotObservatoryInHisCitadelTest() {
        when(spyPlayer.getCitadel()).thenReturn(List.of(new Card(District.TEMPLE)));
        assertEquals(2, spyPlayer.numberOfCardsToPick());
    }


    @Test
    void destroyDistrictThrowsExceptionWhenPlayerDoesntHaveTheDistrict() {
        Player player = new RandomBot(10, deck, view);
        Player player2 = new RandomBot(10, deck, view);
        player2.setCitadel(new ArrayList<>(List.of(new Card(District.TEMPLE))));
        assertThrows(IllegalArgumentException.class, () -> player.destroyDistrict(player2, District.TEMPLE));
    }

    @Test
    void discardFromHandWhenHandIsNotEmpty() {
        spyPlayer.getHand().add(new Card(District.TEMPLE));
        assertTrue(spyPlayer.discardFromHand(new Card(District.TEMPLE)));
        assertFalse(spyPlayer.getHand().contains(new Card(District.TEMPLE)));
        verify(deck, times(1)).discard(new Card(District.TEMPLE));
        verify(view, times(1)).displayPlayerDiscardCard(spyPlayer, new Card(District.TEMPLE));
    }

    @Test
    void discardFromHandWhenHandIsEmpty() {
        assertFalse(spyPlayer.discardFromHand(new Card(District.TEMPLE)));
        verify(deck, times(0)).discard(new Card(District.TEMPLE));
        verify(view, times(0)).displayPlayerDiscardCard(spyPlayer, new Card(District.TEMPLE));
    }

    @Test
    void usePrestigesEffectWithLaboratory() {
        // make a hand with a one cost card
        spyPlayer.getHand().add(new Card(District.TEMPLE));
        spyPlayer.setCitadel(new ArrayList<>(List.of(new Card(District.LABORATORY))));
        spyPlayer.usePrestigesEffect();
        verify(view, times(1)).displayPlayerUseLaboratoryEffect(spyPlayer);
    }

    @Test
    void usePrestigesEffectWithManufacture() {
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextBoolean()).thenReturn(true);
        ((RandomBot) spyPlayer).setRandom(mockRandom);

        // make a hand with a one card
        spyPlayer.getHand().add(new Card(District.TEMPLE));
        spyPlayer.setCitadel(new ArrayList<>(List.of(new Card(District.MANUFACTURE))));
        spyPlayer.usePrestigesEffect();
        verify(view, times(1)).displayPlayerUseManufactureEffect(spyPlayer);
    }

    @Test
    void testUseEffectOfCommonCharacterItsGettingGoldsFromDistrictType() {
        spyPlayer.setCitadel(new ArrayList<>(List.of(new Card(District.MARKET_PLACE))));
        Merchant merchant = new Merchant();
        player.chooseCharacter(new ArrayList<>(List.of(merchant)));
        when(spyPlayer.getCharacter()).thenReturn(merchant);
        merchant.setPlayer(spyPlayer);
        assertEquals(10, spyPlayer.getNbGold());
        spyPlayer.useCommonCharacterEffect();
        assertEquals(11, spyPlayer.getNbGold());
        verify(view, times(1)).displayGoldCollectedFromDisctrictType(any(), anyInt(), any());
    }

    @Test
    void testUseEffectOfCommonCharacterItsNotDisplayingThatIsGettingGoldsBecauseTheDistrictIsEmpty() {
        Merchant merchant = new Merchant();
        player.chooseCharacter(new ArrayList<>(List.of(merchant)));
        when(spyPlayer.getCharacter()).thenReturn(merchant);
        merchant.setPlayer(spyPlayer);
        assertEquals(10, spyPlayer.getNbGold());
        spyPlayer.useCommonCharacterEffect();
        assertEquals(10, spyPlayer.getNbGold());
        verify(view, times(0)).displayGoldCollectedFromDisctrictType(any(), anyInt(), any());
    }
}
