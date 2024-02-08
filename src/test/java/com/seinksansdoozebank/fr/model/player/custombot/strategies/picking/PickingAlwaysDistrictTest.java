package com.seinksansdoozebank.fr.model.player.custombot.strategies.picking;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBotBuilder;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class PickingAlwaysDistrictTest {

    PickingAlwaysDistrict pickingAlwaysDistrict;
    Player customBot;

    @BeforeEach
    void setUp() {
        pickingAlwaysDistrict = new PickingAlwaysDistrict();
        customBot = spy(new CustomBotBuilder(2, mock(IView.class), new Deck(), mock(Bank.class))
                .setPickingStrategy(pickingAlwaysDistrict)
                .setCharacterChoosingStrategy(mock(ICharacterChoosingStrategy.class))
                .build());
    }

    @Test
    void applyMakeThePlayerPickDistrict() {
        assertEquals(2, customBot.getNbGold());
        assertEquals(0, customBot.getHand().size());
        pickingAlwaysDistrict.apply(customBot);
        verify(customBot).pickCardsKeepSomeAndDiscardOthers();
        assertEquals(2, customBot.getNbGold());
        assertEquals(1, customBot.getHand().size());
    }
}