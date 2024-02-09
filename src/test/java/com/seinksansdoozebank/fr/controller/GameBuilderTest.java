package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect.IUsingWarlordEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class GameBuilderTest {

    GameBuilder normalGameBuilder;
    IView view;
    Deck deck;
    Bank bank;

    @BeforeEach
    void setUp() {
        view = mock(IView.class);
        deck = mock(Deck.class);
        bank = new Bank();
        normalGameBuilder = spy(new GameBuilder(view, deck, bank, Game.NORMAL_NB_DISTRICT_TO_WIN));
    }

    @Test
    void checkNbPlayersThrowsErrorWithTooManyPlayers() {
        when(normalGameBuilder.getPlayerListSize()).thenReturn(7);
        assertThrows(IllegalStateException.class, () -> normalGameBuilder.checkNbPlayers());
    }

    @Test
    void checkNbPlayersDoesNotThrowErrorWithLessThanSixPlayers() {
        when(normalGameBuilder.getPlayerListSize()).thenReturn(5);
        assertDoesNotThrow(() -> normalGameBuilder.checkNbPlayers());
    }

    @Test
    void addSmartBotMakesPlayerListSizeIncrementByOne() {
        int playerListSize = normalGameBuilder.getPlayerListSize();
        normalGameBuilder.addSmartBot();
        assertEquals(playerListSize + 1, normalGameBuilder.getPlayerListSize());
    }

    @Test
    void addRandomBotMakesPlayerListSizeIncrementByOne() {
        int playerListSize = normalGameBuilder.getPlayerListSize();
        normalGameBuilder.addRandomBot();
        assertEquals(playerListSize + 1, normalGameBuilder.getPlayerListSize());
    }

    @Test
    void addCustomBotMakesPlayerListSizeIncrementByOne() {
        IPickingStrategy pickingStrategy = mock(IPickingStrategy.class);
        ICharacterChoosingStrategy characterChoosingStrategy = mock(ICharacterChoosingStrategy.class);
        IUsingThiefEffectStrategy thiefEffectStrategy = mock(IUsingThiefEffectStrategy.class);
        IUsingMurdererEffectStrategy murdererEffectStrategy = mock(IUsingMurdererEffectStrategy.class);
        IUsingWarlordEffectStrategy warlordEffectStrategy = mock(IUsingWarlordEffectStrategy.class);
        ICardChoosingStrategy cardChosingStrategy = mock(ICardChoosingStrategy.class);
        int playerListSize = normalGameBuilder.getPlayerListSize();
        normalGameBuilder.addCustomBot(pickingStrategy, characterChoosingStrategy, thiefEffectStrategy, murdererEffectStrategy, warlordEffectStrategy, cardChosingStrategy);
        assertEquals(playerListSize + 1, normalGameBuilder.getPlayerListSize());
    }

    @Test
    void buildThrowsErrorWithNoPlayers() {
        assertThrows(IllegalStateException.class, () -> normalGameBuilder.build());
    }

    @Test
    void buildWorksWell() {
        int nbPlayers = 5;
        for (int i = 0; i < nbPlayers; i++) {
            normalGameBuilder.addSmartBot();
        }

        assertDoesNotThrow(() -> normalGameBuilder.build());
        Game game = normalGameBuilder.build();

        assertEquals(nbPlayers, game.players.size());
        List<Player> players = game.players;
        for (Player player : players) {
            assertEquals(2, player.getNbGold());
            assertEquals(nbPlayers-1,player.getOpponents().size());
        }
    }
}