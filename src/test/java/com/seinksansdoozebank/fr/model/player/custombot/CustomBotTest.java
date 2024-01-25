package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomBotTest {

    CustomBot spyCustomBot;
    IPickingStrategy mockPickingStrategy;
    ICharacterChoosingStrategy mockCharacterChoosingStrategy;
    IUsingThiefEffectStrategy mockUsingThiefEffectStrategy;
    IUsingMurdererEffectStrategy mockUsingMurdererEffectStrategy;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickCoin(15);
        mockPickingStrategy = mock(IPickingStrategy.class);
        mockCharacterChoosingStrategy = mock(ICharacterChoosingStrategy.class);
        mockUsingThiefEffectStrategy = mock(IUsingThiefEffectStrategy.class);
        mockUsingMurdererEffectStrategy = mock(IUsingMurdererEffectStrategy.class);
        spyCustomBot = spy(new CustomBot(2, new Deck(), mock(IView.class),
                mockPickingStrategy,
                mockCharacterChoosingStrategy,
                mockUsingThiefEffectStrategy,
                mockUsingMurdererEffectStrategy));
    }

    @Test
    void pickSomethingWithAPickingStrategyShouldUseThePickingStrategyMethod() {
        spyCustomBot.pickSomething();
        verify(mockPickingStrategy).apply(spyCustomBot);
    }

    @Test
    void pickSomethingWithoutAPickingStrategyShouldCallTheSuperMethod() {
        spyCustomBot.pickingStrategy = null;
        spyCustomBot.pickSomething();
        verify(spyCustomBot).randomPickSomething();
    }

    @Test
    void chooseCharacterImplWithAChoosingCharacterStrategyUseTheCharacterChoosingStrategyMethod() {
        spyCustomBot.chooseCharacterImpl(null);
        verify(mockCharacterChoosingStrategy).apply(spyCustomBot, null);
    }

    @Test
    void chooseCharacterImplWithoutACharacterChoosingStrategyShouldCallTheSuperMethod() {
        spyCustomBot.characterChoosingStrategy = null;
        spyCustomBot.chooseCharacterImpl(List.of(new King(), new Merchant()));
        verify(spyCustomBot).randomChooseCharacterImpl(any());
    }

    @Test
    void useThiefEffectWithAUsingThiefEffectStrategyShouldUseTheUsingThiefEffectStrategyMethod() {
        spyCustomBot.setAvailableCharacters(List.of(new Thief(), new King(), new Bishop()));
        Thief mockThief = mock(Thief.class);
        spyCustomBot.useEffectThief(mockThief);
        verify(mockUsingThiefEffectStrategy).apply(spyCustomBot, mockThief);
    }

    @Test
    void useThiefEffectWithoutAUsingThiefEffectStrategyShouldCallTheSuperMethod() {
        Thief mockThief = mock(Thief.class);
        spyCustomBot.usingThiefEffectStrategy = null;
        spyCustomBot.setAvailableCharacters(List.of(new Thief(), new King(), new Bishop()));
        spyCustomBot.useEffectThief(mockThief);
        verify(spyCustomBot).randomUseThiefEffect(any());
    }

    @Test
    void useAssassinEffectWithAUsingMurdererEffectStrategyShouldUseTheUsingMurdererEffectStrategyMethod() {
        spyCustomBot.setAvailableCharacters(List.of(new Assassin(), new King(), new Bishop()));
        Assassin mockAssassin = mock(Assassin.class);
        spyCustomBot.useEffectAssassin(mockAssassin);
        verify(mockUsingMurdererEffectStrategy).apply(spyCustomBot, mockAssassin);
    }

    @Test
    void useAssassinEffectWithoutAUsingMurdererEffectStrategyShouldCallTheSuperMethod() {
        Assassin mockAssassin = mock(Assassin.class);
        spyCustomBot.usingMurdererEffectStrategy = null;
        spyCustomBot.setAvailableCharacters(List.of(new Assassin(), new King(), new Bishop()));
        spyCustomBot.useEffectAssassin(mockAssassin);
        verify(spyCustomBot).randomUseMurdererEffect(any());
    }

    @Test
    void chooseCharacterLinksThePlayerAndTheCharacter() {
        ICharacterChoosingStrategy spyChoosingStrategy = spy(new ChoosingCharacterToTargetFirstPlayer());
        Player customBotWithARealChoosingStrat = new CustomBot(2, null, mock(IView.class),
                mockPickingStrategy,
                spyChoosingStrategy,
                mockUsingThiefEffectStrategy,
                mockUsingMurdererEffectStrategy);
        Opponent opponent = mock(Opponent.class);
        when(opponent.getNbGold()).thenReturn(2);
        customBotWithARealChoosingStrat.setOpponents(List.of(opponent));

        Character assassin = new Assassin();
        List<Character> characters = new ArrayList<>(List.of(assassin, new Merchant()));

        customBotWithARealChoosingStrat.chooseCharacter(characters);

        verify(spyChoosingStrategy).apply(customBotWithARealChoosingStrat, characters);
        assertTrue(characters.contains(customBotWithARealChoosingStrat.getCharacter()));
        assertEquals(customBotWithARealChoosingStrat.getCharacter(), assassin);
        assertEquals(customBotWithARealChoosingStrat, assassin.getPlayer());
    }
}