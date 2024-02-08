package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect.IUsingWarlordEffectStrategy;
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
    IUsingWarlordEffectStrategy mockUsingWarlordEffectStrategy;
    ICardChoosingStrategy mockCardChoosingStrategy;

    @BeforeEach
    void setUp() {
        customBotBuilder = new CustomBotBuilder(2, null, null, mock(Bank.class));
        mockPickingStrategy = mock(IPickingStrategy.class);
        mockCharacterChoosingStrategy = mock(ICharacterChoosingStrategy.class);
        mockUsingThiefEffectStrategy = mock(IUsingThiefEffectStrategy.class);
        mockUsingMurdererEffectStrategy = mock(IUsingMurdererEffectStrategy.class);
        mockUsingWarlordEffectStrategy = mock(IUsingWarlordEffectStrategy.class);
        mockCardChoosingStrategy = mock(ICardChoosingStrategy.class);
    }

    @Test
    void buildWithAllStrategiesSet() {
        customBotBuilder.setPickingStrategy(mockPickingStrategy);
        customBotBuilder.setCharacterChoosingStrategy(mockCharacterChoosingStrategy);
        customBotBuilder.setUsingThiefEffectStrategy(mockUsingThiefEffectStrategy);
        customBotBuilder.setUsingMurdererEffectStrategy(mockUsingMurdererEffectStrategy);
        customBotBuilder.setUsingWarlordEffectStrategy(mockUsingWarlordEffectStrategy);
        customBotBuilder.setCardChoosingStrategy(mockCardChoosingStrategy);

        CustomBot customBot = customBotBuilder.build();
        assertEquals(mockPickingStrategy, customBot.pickingStrategy);
        assertEquals(mockCharacterChoosingStrategy, customBot.characterChoosingStrategy);
        assertEquals(mockUsingThiefEffectStrategy, customBot.usingThiefEffectStrategy);
        assertEquals(customBotBuilder.nbGold, customBot.getNbGold());
        assertEquals(mockUsingMurdererEffectStrategy, customBot.usingMurdererEffectStrategy);
    }
}