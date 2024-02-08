package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.IUsingCondottiereEffectStrategy;
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

    GameBuilder gameBuilder;
    IView view;
    Deck deck;
    Bank bank;

    @BeforeEach
    void setUp() {
        view = mock(IView.class);
        deck = mock(Deck.class);
        bank = new Bank();
        gameBuilder = spy(new GameBuilder(view, deck, bank));
    }

    @Test
    void checkNbPlayersThrowsErrorWithTooManyPlayers() {
        when(gameBuilder.getPlayerListSize()).thenReturn(7);
        assertThrows(IllegalStateException.class, () -> gameBuilder.checkNbPlayers());
    }

    @Test
    void checkNbPlayersDoesNotThrowErrorWithLessThanSixPlayers() {
        when(gameBuilder.getPlayerListSize()).thenReturn(5);
        assertDoesNotThrow(() -> gameBuilder.checkNbPlayers());
    }

    @Test
    void addSmartBotMakesPlayerListSizeIncrementByOne() {
        int playerListSize = gameBuilder.getPlayerListSize();
        gameBuilder.addSmartBot();
        assertEquals(playerListSize + 1, gameBuilder.getPlayerListSize());
    }

    @Test
    void addRandomBotMakesPlayerListSizeIncrementByOne() {
        int playerListSize = gameBuilder.getPlayerListSize();
        gameBuilder.addRandomBot();
        assertEquals(playerListSize + 1, gameBuilder.getPlayerListSize());
    }

    @Test
    void addCustomBotMakesPlayerListSizeIncrementByOne() {
        IPickingStrategy pickingStrategy = mock(IPickingStrategy.class);
        ICharacterChoosingStrategy characterChoosingStrategy = mock(ICharacterChoosingStrategy.class);
        IUsingThiefEffectStrategy thiefEffectStrategy = mock(IUsingThiefEffectStrategy.class);
        IUsingMurdererEffectStrategy murdererEffectStrategy = mock(IUsingMurdererEffectStrategy.class);
        IUsingCondottiereEffectStrategy condottiereEffectStrategy = mock(IUsingCondottiereEffectStrategy.class);
        ICardChoosingStrategy cardChosingStrategy = mock(ICardChoosingStrategy.class);
        int playerListSize = gameBuilder.getPlayerListSize();
        gameBuilder.addCustomBot(pickingStrategy, characterChoosingStrategy, thiefEffectStrategy, murdererEffectStrategy, condottiereEffectStrategy, cardChosingStrategy);
        assertEquals(playerListSize + 1, gameBuilder.getPlayerListSize());
    }

    @Test
    void buildThrowsErrorWithNoPlayers() {
        assertThrows(IllegalStateException.class, () -> gameBuilder.build());
    }

    @Test
    void buildWorksWell() {
        int nbPlayers = 5;
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addSmartBot();
        }

        assertDoesNotThrow(() -> gameBuilder.build());
        Game game = gameBuilder.build();

        assertEquals(nbPlayers, game.players.size());
        List<Player> players = game.players;
        for (Player player : players) {
            assertEquals(2, player.getNbGold());
            assertEquals(nbPlayers-1,player.getOpponents().size());
        }
    }
}