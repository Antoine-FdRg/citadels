package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.IUsingCondottiereEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CustomBotBuilderTest {

    CustomBotBuilder customBotBuilder;
    IPickingStrategy mockPickingStrategy;
    ICharacterChoosingStrategy mockCharacterChoosingStrategy;
    IUsingThiefEffectStrategy mockUsingThiefEffectStrategy;
    IUsingMurdererEffectStrategy mockUsingMurdererEffectStrategy;
    IUsingCondottiereEffectStrategy mockUsingCondottiereEffectStrategy;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickXCoin(Bank.MAX_COIN / 2);
        customBotBuilder = new CustomBotBuilder(2, null, null);
        mockPickingStrategy = mock(IPickingStrategy.class);
        mockCharacterChoosingStrategy = mock(ICharacterChoosingStrategy.class);
        mockUsingThiefEffectStrategy = mock(IUsingThiefEffectStrategy.class);
        mockUsingMurdererEffectStrategy = mock(IUsingMurdererEffectStrategy.class);
        mockUsingCondottiereEffectStrategy = mock(IUsingCondottiereEffectStrategy.class);
    }

    @Test
    void buildWithAllStrategiesSet() {
        customBotBuilder.setPickingStrategy(mockPickingStrategy);
        customBotBuilder.setCharacterChoosingStrategy(mockCharacterChoosingStrategy);
        customBotBuilder.setUsingThiefEffectStrategy(mockUsingThiefEffectStrategy);
        customBotBuilder.setUsingMurdererEffectStrategy(mockUsingMurdererEffectStrategy);
        customBotBuilder.setUsingCondottiereEffectStrategy(mockUsingCondottiereEffectStrategy);

        CustomBot customBot = customBotBuilder.build();
        assertEquals(mockPickingStrategy, customBot.pickingStrategy);
        assertEquals(mockCharacterChoosingStrategy, customBot.characterChoosingStrategy);
        assertEquals(mockUsingThiefEffectStrategy, customBot.usingThiefEffectStrategy);
        assertEquals(customBotBuilder.nbGold, customBot.getNbGold());
    }
}