package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.strategies.picking.IPickingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CustomBotTest {

    CustomBot customBot;
    IPickingStrategy mockPickingStrategy;

    @BeforeEach
    void setUp() {
        mockPickingStrategy = mock(IPickingStrategy.class);
        customBot = new CustomBot(2, null, null, mockPickingStrategy);
    }

    @Test
    void pickSomethingUseThePickingStrategyMethod() {
        customBot.pickSomething();
        verify(mockPickingStrategy).apply(customBot);
    }
}