package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CustomBotTest {

    CustomBot customBot;
    IPickingStrategy mockPickingStrategy;
    ICharacterChoosingStrategy mockCharacterChoosingStrategy;

    @BeforeEach
    void setUp() {
        mockPickingStrategy = mock(IPickingStrategy.class);
        mockCharacterChoosingStrategy = mock(ICharacterChoosingStrategy.class);
        customBot = new CustomBot(2, null, null,
                mockPickingStrategy,
                mockCharacterChoosingStrategy);
    }

    @Test
    void pickSomethingUseThePickingStrategyMethod() {
        customBot.pickSomething();
        verify(mockPickingStrategy).apply(customBot);
    }
}