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

class PickingAlwaysGoldTest {

    PickingAlwaysGold pickingAlwaysGold;
    Player customBot;

    @BeforeEach
    void setUp() {
        pickingAlwaysGold = new PickingAlwaysGold();
        customBot = spy(new CustomBotBuilder(2,mock(IView.class), new Deck())
                .setPickingStrategy(pickingAlwaysGold)
                .setCharacterChoosingStrategy(mock(ICharacterChoosingStrategy.class))
                .build());
    }

    @Test
    void apply() {
        assertEquals(2, customBot.getNbGold());
        assertEquals(0, customBot.getHand().size());
        pickingAlwaysGold.apply(customBot);
        verify(customBot).pickGold();
        assertEquals(4, customBot.getNbGold());
        assertEquals(0, customBot.getHand().size());
    }
}