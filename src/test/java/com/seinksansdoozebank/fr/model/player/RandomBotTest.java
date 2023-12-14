package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RandomBotTest {
    RandomBot spyRandomBot;
    IView view;
    Deck deck;
    District districtCostThree;
    District districtCostFive;

    @BeforeEach
    void setup() {
        view = mock(Cli.class);
        deck = spy(new Deck());
        districtCostThree = District.FACTORY;
        districtCostFive = District.FORTRESS;
        spyRandomBot = spy(new RandomBot(10, deck, view));
    }

    @Test
    void play() {
        Optional<District> optDistrict = Optional.of(districtCostThree);
        doReturn(optDistrict).when(spyRandomBot).buildADistrict();
        spyRandomBot.play();

        verify(spyRandomBot, times(1)).pickSomething();
        verify(spyRandomBot, atMostOnce()).buildADistrict();
        verify(view, times(1)).displayPlayerStartPlaying(spyRandomBot);
        verify(view, times(2)).displayPlayerInfo(spyRandomBot);
        verify(view, times(1)).displayPlayerBuildDistrict(spyRandomBot, optDistrict);
    }

    @Test
    void pickSomething() {
        spyRandomBot.pickSomething();
        verify(spyRandomBot, atMostOnce()).pickGold();
        verify(spyRandomBot, atMostOnce()).pickTwoDistrictKeepOneDiscardOne();
    }

    @Test
    void pickTwoDistrictKeepOneDiscardOne() {
        int handSizeBeforePicking = spyRandomBot.getHand().size();
        spyRandomBot.pickTwoDistrictKeepOneDiscardOne();

        verify(view, times(1)).displayPlayerPickDistrict(spyRandomBot);

        verify(deck, times(2)).pick();
        assertEquals(handSizeBeforePicking + 1, spyRandomBot.getHand().size());
        verify(deck, times(1)).discard(any(District.class));
    }

    @Test
    void chooseDistrictWithEmptyHand() {
        boolean handIsEmpty = spyRandomBot.getHand().isEmpty();
        Optional<District> chosenDistrict = spyRandomBot.chooseDistrict();
        assertTrue(chosenDistrict.isEmpty());
        assertTrue(handIsEmpty);
    }

    @Test
    void chooseDistrictWithNonEmptyHandButNoDistrictToBuildShouldReturnEmptyOptional() {
        spyRandomBot.getHand().add(districtCostThree);
        spyRandomBot.getHand().add(districtCostFive);
        doReturn(false).when(spyRandomBot).canBuildDistrict(any(District.class));

        Optional<District> chosenDistrict = spyRandomBot.chooseDistrict();

        assertTrue(chosenDistrict.isEmpty());
    }

    @Test
    void chooseDistrictWithNonEmptyHandAndCanBuildDistrictTrueShouldReturnADistrictOfFromTheHand() {
        spyRandomBot.getHand().add(districtCostThree);
        spyRandomBot.getHand().add(districtCostFive);
        doReturn(true).when(spyRandomBot).canBuildDistrict(any(District.class));

        Optional<District> chosenDistrict = spyRandomBot.chooseDistrict();

        assertFalse(spyRandomBot.getHand().isEmpty());
        assertTrue(chosenDistrict.isPresent());
        assertTrue(spyRandomBot.getHand().contains(chosenDistrict.get()));
    }
}