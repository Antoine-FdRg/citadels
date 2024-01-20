package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RandomBotTest {
    RandomBot spyRandomBot;
    IView view;
    Deck deck;
    Card cardCostThree;
    Card cardCostFive;

    @BeforeEach
    void setup() {
        view = mock(Cli.class);
        deck = spy(new Deck());
        cardCostThree = new Card(District.DONJON);
        cardCostFive = new Card(District.FORTRESS);
        spyRandomBot = spy(new RandomBot(10, deck, view));
    }

    @Test
    void play() {
        Optional<Card> optDistrict = Optional.of(cardCostThree);
        doReturn(optDistrict).when(spyRandomBot).playACard();
        spyRandomBot.chooseCharacter(new ArrayList<>(List.of(new Bishop())));
        spyRandomBot.play();

        verify(spyRandomBot, times(1)).pickSomething();
        verify(spyRandomBot, atMostOnce()).playACard();
        verify(view, times(1)).displayPlayerStartPlaying(spyRandomBot);
        verify(view, times(1)).displayPlayerRevealCharacter(spyRandomBot);
        verify(view, times(2)).displayPlayerInfo(spyRandomBot);
        verify(view, atMostOnce()).displayPlayerPlaysCard(any(), any());
    }

    @Test
    void playWhereCharacterIsDead() {
        King king = new King();
        when(spyRandomBot.getCharacter()).thenReturn(king);
        king.kill();
        assertThrows(IllegalStateException.class, () -> spyRandomBot.play());
    }

    @Test
    void playWithArchitect() {
        Optional<Card> optDistrict = Optional.of(cardCostThree);
        doReturn(optDistrict).when(spyRandomBot).playACard();
        spyRandomBot.chooseCharacter(new ArrayList<>(List.of(new Architect())));
        spyRandomBot.play();

        verify(spyRandomBot, times(1)).pickSomething();
        verify(spyRandomBot, atMost(3)).playACard();
        verify(view, times(1)).displayPlayerStartPlaying(spyRandomBot);
        verify(view, times(1)).displayPlayerRevealCharacter(spyRandomBot);
        verify(view, times(2)).displayPlayerInfo(spyRandomBot);
        verify(view, atMost(3)).displayPlayerPlaysCard(any(), any());
    }

    @Test
    void playWithAssassinWithOneGoodCharacterToKill() {
        Optional<Card> optDistrict = Optional.of(cardCostThree);
        doReturn(optDistrict).when(spyRandomBot).playACard();
        Assassin assassin = spy(new Assassin());
        spyRandomBot.chooseCharacter(new ArrayList<>(List.of(assassin)));
        List<Player> opponents = new ArrayList<>();
        RandomBot opponent = new RandomBot(10, deck, view);
        opponent.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));
        opponents.add(opponent);
        when(spyRandomBot.getOpponents()).thenReturn(opponents);
        spyRandomBot.play();

        verify(spyRandomBot, times(1)).pickSomething();
        verify(spyRandomBot, atMost(3)).playACard();
        verify(view, times(1)).displayPlayerStartPlaying(spyRandomBot);
        verify(view, times(1)).displayPlayerRevealCharacter(spyRandomBot);
        verify(view, times(2)).displayPlayerInfo(spyRandomBot);
        verify(view, atMost(1)).displayPlayerPlaysCard(any(), any());
        verify(assassin, times(1)).useEffect(opponent.getCharacter());
    }


    @Test
    void pickSomething() {
        spyRandomBot.pickSomething();
        verify(spyRandomBot, atMostOnce()).pickGold();
        verify(spyRandomBot, atMostOnce()).pickTwoCardKeepOneDiscardOne();
    }

    @Test
    void pickTwoDistrictKeepOneDiscardOne() {
        int handSizeBeforePicking = spyRandomBot.getHand().size();
        spyRandomBot.pickTwoCardKeepOneDiscardOne();

        verify(view, times(1)).displayPlayerPickCards(spyRandomBot, 1);

        verify(deck, times(2)).pick();
        assertEquals(handSizeBeforePicking + 1, spyRandomBot.getHand().size());
        verify(deck, times(1)).discard(any(Card.class));
    }

    @Test
    void chooseDistrictWithEmptyHand() {
        boolean handIsEmpty = spyRandomBot.getHand().isEmpty();
        Optional<Card> chosenDistrict = spyRandomBot.chooseCard();
        assertTrue(chosenDistrict.isEmpty());
        assertTrue(handIsEmpty);
    }

    @Test
    void chooseDistrictWithNonEmptyHandButNoDistrictToBuildShouldReturnEmptyOptional() {
        spyRandomBot.getHand().add(cardCostThree);
        spyRandomBot.getHand().add(cardCostFive);
        doReturn(false).when(spyRandomBot).canPlayCard(any(Card.class));

        Optional<Card> chosenDistrict = spyRandomBot.chooseCard();

        assertTrue(chosenDistrict.isEmpty());
    }

    @Test
    void chooseDistrictWithNonEmptyHandAndCanBuildDistrictTrueShouldReturnADistrictOfFromTheHand() {
        spyRandomBot.getHand().add(cardCostThree);
        spyRandomBot.getHand().add(cardCostFive);
        doReturn(true).when(spyRandomBot).canPlayCard(any(Card.class));

        Optional<Card> chosenDistrict = spyRandomBot.chooseCard();

        assertFalse(spyRandomBot.getHand().isEmpty());
        assertTrue(chosenDistrict.isPresent());
        assertTrue(spyRandomBot.getHand().contains(chosenDistrict.get()));
    }


    @Test
    void chooseCharacterSetLinkBetweenCharacterAndPlayer() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Bishop());
        characters.add(new King());
        characters.add(new Merchant());
        characters.add(new Condottiere());

        spyRandomBot.chooseCharacter(characters);

        Character character = spyRandomBot.getCharacter();
        assertEquals(character.getPlayer().getId(), spyRandomBot.getId());
        verify(view, times(1)).displayPlayerChooseCharacter(spyRandomBot);

    }

    @Test
    void chooseCharacter() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Bishop());
        characters.add(new King());
        characters.add(new Merchant());
        characters.add(new Condottiere());
        Player player = new RandomBot(10, deck, view);

        spyRandomBot.chooseCharacter(characters);
        player.chooseCharacter(characters);

        assertNotEquals(spyRandomBot.getCharacter(), player.getCharacter());
    }

    @Test
    void testRandomBotUseEffectCondottiere() {
        // Create a mock Random object that always returns true
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextBoolean()).thenReturn(true);

        // Set the mockRandom in the RandomBot for testing
        spyRandomBot.setRandom(mockRandom);
        spyRandomBot.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));
        List<Card> opponentCitadel = new ArrayList<>(List.of(new Card(District.MARKET_PLACE)));
        Player opponent = new SmartBot(10, deck, view);
        opponent.setCitadel(opponentCitadel);
        opponent.chooseCharacter(new ArrayList<>(List.of(new Merchant())));
        spyRandomBot.setOpponents(new ArrayList<>(List.of(opponent)));
        // Test the useEffect method
        spyRandomBot.useEffect();
        verify(spyRandomBot, times(1)).useEffect();
        verify(spyRandomBot, times(1)).useEffectCondottiere(any(Condottiere.class));
    }

    @Test
    void testRandomBotCantUseEffectCondottiere() {
        // Create a mock Random object that always returns true
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextBoolean()).thenReturn(true);

        // Set the mockRandom in the RandomBot for testing
        spyRandomBot.setRandom(mockRandom);
        spyRandomBot.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));
        List<Card> opponentCitadel = new ArrayList<>(List.of(new Card(District.MARKET_PLACE)));
        Player opponent = new SmartBot(10, deck, view);
        opponent.setCitadel(opponentCitadel);
        opponent.chooseCharacter(new ArrayList<>(List.of(new Bishop())));
        spyRandomBot.setOpponents(new ArrayList<>(List.of(opponent)));
        // Test the useEffect method
        int nbGold = spyRandomBot.getNbGold();
        spyRandomBot.useEffect();
        verify(spyRandomBot, times(1)).useEffect();
        verify(spyRandomBot, times(1)).useEffectCondottiere(any(Condottiere.class));
        assertEquals(nbGold, spyRandomBot.getNbGold());
    }


}