package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SmartBotTest {
    SmartBot spySmartBot;
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
        spySmartBot = spy(new SmartBot(10, deck, view));
    }

    @Test
    void playWithEmptyChosenDistrictShouldPickDistrictAndBuild() {
        Optional<District> optDistrict = Optional.empty();
        doReturn(optDistrict).when(spySmartBot).chooseDistrict();

        spySmartBot.play();

        verify(view, times(1)).displayPlayerStartPlaying(spySmartBot);
        verify(spySmartBot, times(1)).pickTwoDistrictKeepOneDiscardOne();
        verify(spySmartBot, times(1)).buildADistrict();
        verify(view, times(1)).displayPlayerBuildDistrict(spySmartBot, optDistrict);
        verify(view, times(2)).displayPlayerInfo(spySmartBot);
    }

    @Test
    void playWithUnbuildableDistrictShouldPickGoldAndBuild() {
        Optional<District> optDistrict = Optional.of(districtCostThree);
        doReturn(optDistrict).when(spySmartBot).chooseDistrict();
        doReturn(false).when(spySmartBot).canBuildDistrict(any(District.class));
        doReturn(Optional.of(districtCostThree)).when(spySmartBot).buildADistrict();

        spySmartBot.play();

        verify(view, times(1)).displayPlayerStartPlaying(spySmartBot);
        verify(spySmartBot, times(1)).pickGold();
        verify(spySmartBot, times(1)).buildADistrict();
        verify(view, times(1)).displayPlayerBuildDistrict(spySmartBot,Optional.of(districtCostThree));
        verify(view, times(2)).displayPlayerInfo(spySmartBot);
    }

    @Test
    void playWithUBuildableDistrictShouldBuildAndPickSomething() {
        Optional<District> optDistrict = Optional.of(districtCostThree);
        doReturn(optDistrict).when(spySmartBot).chooseDistrict();
        doReturn(true).when(spySmartBot).canBuildDistrict(any(District.class));

        spySmartBot.play();

        verify(view, times(1)).displayPlayerStartPlaying(spySmartBot);
        verify(spySmartBot, times(1)).buildADistrict();
        verify(view, times(1)).displayPlayerBuildDistrict(spySmartBot, optDistrict);
        verify(spySmartBot, times(1)).pickSomething();
        verify(view, times(2)).displayPlayerInfo(spySmartBot);
    }

    @Test
    void pickSomethingWithEmptyChoosenDistrict() {
        Optional<District> optDistrict = Optional.empty();
        doReturn(optDistrict).when(spySmartBot).buildADistrict();
        spySmartBot.pickSomething();

        verify(spySmartBot, times(0)).pickGold();
        verify(spySmartBot, times(1)).pickTwoDistrictKeepOneDiscardOne();
    }

    @Test
    void pickSomethingWithChoosenDistrictAndNotEnoughGold() {
        spySmartBot.decreaseGold(spySmartBot.getNbGold());
        Optional<District> optDistrict = Optional.of(districtCostThree);
        doReturn(optDistrict).when(spySmartBot).chooseDistrict();
        spySmartBot.pickSomething();
        assertTrue(spySmartBot.getNbGold() < districtCostThree.getCost());
        verify(spySmartBot, times(1)).pickGold();
        verify(spySmartBot, times(0)).pickTwoDistrictKeepOneDiscardOne();
    }

    @Test
    void pickSomethingWithChoosenDistrictAndEnoughGold() {
        Optional<District> optDistrict = Optional.of(districtCostThree);
        doReturn(optDistrict).when(spySmartBot).chooseDistrict();
        spySmartBot.pickSomething();
        assertTrue(spySmartBot.getNbGold() >= districtCostThree.getCost());
        verify(spySmartBot, times(0)).pickGold();
        verify(spySmartBot, times(1)).pickTwoDistrictKeepOneDiscardOne();
    }

    @Test
    void pickTwoDistrictKeepOneDiscardOneShouldkeepTheCheaperOne() {
        boolean handIsEmpty = spySmartBot.getHand().isEmpty();
        spySmartBot.pickTwoDistrictKeepOneDiscardOne();

        assertTrue(handIsEmpty);
        assertEquals(1, spySmartBot.getHand().size());
        verify(view, times(1)).displayPlayerPickDistrict(spySmartBot);
        verify(deck, times(2)).pick();
        verify(deck, times(1)).discard(any(District.class));
        assertTrue(spySmartBot.getHand().get(0).getCost()<= deck.getDeck().get(0).getCost());
    }

    @Test
    void chooseDistrictWithEmptyHand() {
        boolean handIsEmpty = spySmartBot.getHand().isEmpty();
        Optional<District> chosenDistrict = spySmartBot.chooseDistrict();
        assertTrue(chosenDistrict.isEmpty());
        assertTrue(handIsEmpty);
    }

    @Test
    void chooseDistrictShouldReturnANotAlreadyBuiltDistrict() {
        spySmartBot.getHand().add(districtCostThree);
        spySmartBot.getHand().add(districtCostFive);
        spySmartBot.getCitadel().add(districtCostThree);
        Optional<District> chosenDistrict = spySmartBot.chooseDistrict();
        assertTrue(chosenDistrict.isPresent());
        assertEquals(districtCostFive, chosenDistrict.get());
    }

    @Test
    void getCheaperDistricWithEmptyListtShouldReturnEmptyOptional() {
        Optional<District> cheaperDistrict = spySmartBot.getCheaperDistrict(List.of());
        assertTrue(cheaperDistrict.isEmpty());
    }

    @Test
    void getCheaperDistrictShouldReturnTheCheaperDistrict() {
        List<District> districtList = List.of(districtCostFive, districtCostThree, districtCostFive);
        Optional<District> cheaperDistrict = spySmartBot.getCheaperDistrict(districtList);
        assertTrue(cheaperDistrict.isPresent());
        assertEquals(districtCostThree, cheaperDistrict.get());
    }
}