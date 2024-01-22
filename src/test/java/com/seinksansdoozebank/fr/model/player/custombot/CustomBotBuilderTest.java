package com.seinksansdoozebank.fr.model.player.custombot;

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

    @BeforeEach
    void setUp() {
        customBotBuilder = new CustomBotBuilder(2, null, null);
        mockPickingStrategy = mock(IPickingStrategy.class);
        mockCharacterChoosingStrategy = mock(ICharacterChoosingStrategy.class);
    }

    @Test
    void buildWithAllStrategiesSet() {
        customBotBuilder.setPickingStrategy(mockPickingStrategy);
        customBotBuilder.setCharacterChoosingStrategy(mockCharacterChoosingStrategy);
        CustomBot customBot = customBotBuilder.build();
        assertEquals(mockPickingStrategy, customBot.pickingStrategy);
        assertEquals(mockCharacterChoosingStrategy, customBot.characterChoosingStrategy);
        assertEquals(customBotBuilder.nbGold, customBot.getNbGold());
    }
}