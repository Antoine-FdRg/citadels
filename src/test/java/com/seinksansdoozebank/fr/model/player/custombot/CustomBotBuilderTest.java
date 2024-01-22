package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.strategies.picking.IPickingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CustomBotBuilderTest {

    CustomBotBuilder customBotBuilder;
    IPickingStrategy mockPickingStrategy;

    @BeforeEach
    void setUp() {
        customBotBuilder = new CustomBotBuilder(2, null, null);
        mockPickingStrategy = mock(IPickingStrategy.class);
    }

    @Test
    void buildWithMissingStrategySet() {
        customBotBuilder.setPickingStrategy(null);
        assertThrows(IllegalStateException.class, () -> customBotBuilder.build());
    }

    @Test
    void buildWithAllStrategiesSet() {
        customBotBuilder.setPickingStrategy(mockPickingStrategy);
        CustomBot customBot = customBotBuilder.build();
        assertEquals(mockPickingStrategy, customBot.pickingStrategy);
        assertEquals(customBotBuilder.nbGold, customBot.getNbGold());
    }
}