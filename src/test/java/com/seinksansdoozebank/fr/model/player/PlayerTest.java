package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
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

import java.util.Optional;

class PlayerTest {
    Player player;
    Player spyPlayer;
    IView view;
    Deck deck;
    District districtCostThree;
    District districtCostFive;

    @BeforeEach
    void setup() {
        view = mock(Cli.class);
        deck = mock(Deck.class);
        districtCostThree = District.FACTORY;
        districtCostFive = District.FORTRESS;
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
    void testBuildADistrictWithEmptyOptChosenDistrictShouldReturnEmpty() {
        when(spyPlayer.chooseDistrict()).thenReturn(Optional.empty());
        assertTrue(spyPlayer.buildADistrict().isEmpty());
    }

    @Test
    void testBuildADistrictWithUnbuildableChosenDistrictShouldReturnEmpty() {
        doReturn(false).when(spyPlayer).canBuildDistrict(any(District.class));
        assertTrue(spyPlayer.buildADistrict().isEmpty());
    }

    @Test
    void testBuildADistrictWithBuildableChosenDistrictShouldReturnDistrict() {
        spyPlayer.getHand().add(districtCostThree);
        doReturn(Optional.of(districtCostThree)).when(spyPlayer).chooseDistrict();
        doReturn(true).when(spyPlayer).canBuildDistrict(any(District.class));

        Optional<District> optBuiltDistrict = spyPlayer.buildADistrict();
        assertTrue(optBuiltDistrict.isPresent());
        District builtDistrict = optBuiltDistrict.get();

        assertFalse(spyPlayer.getHand().contains(districtCostThree));
        assertTrue(spyPlayer.getCitadel().contains(districtCostThree));
        verify(spyPlayer, times(1)).canBuildDistrict(districtCostThree);
        assertEquals(districtCostThree, builtDistrict);
    }

    @Test
    void testCanBuildDistrictWithTooExpensiveDistrictShouldReturnFalse() {
        spyPlayer.decreaseGold(10);
        District tooExpensiveDistrict = districtCostFive;
        assertFalse(spyPlayer.getCitadel().contains(tooExpensiveDistrict));
        assertFalse(spyPlayer.canBuildDistrict(tooExpensiveDistrict));
    }
    @Test
    void testCanBuildDistrictWithAlreadyBuiltDistrictShouldReturnFalse() {
        spyPlayer.getCitadel().add(districtCostThree);
        assertTrue(spyPlayer.getCitadel().contains(districtCostThree));
        assertTrue(districtCostThree.getCost() <= spyPlayer.getNbGold());
    }

    @Test
    void testCanBuildDistrictWithBuildableDistrictShouldReturnTrue() {
        assertFalse(spyPlayer.getCitadel().contains(districtCostThree));
        assertTrue(districtCostThree.getCost() <= spyPlayer.getNbGold());
        assertTrue(spyPlayer.canBuildDistrict(districtCostThree));
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
        player.getCitadel().add(districtCostThree);
        player.getCitadel().add(districtCostFive);
        int sum = districtCostThree.getCost() + districtCostFive.getCost();
        assertEquals(sum, player.getScore());
    }
}
