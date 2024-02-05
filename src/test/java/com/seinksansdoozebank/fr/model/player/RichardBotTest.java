package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RichardBotTest {
    RichardBot richardBot;
    IView view;
    Deck deck;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickXCoin(Bank.MAX_COIN / 2);
        view = mock(Cli.class);
        deck = spy(new Deck());
        richardBot = spy(new RichardBot(10, deck, view));
    }

    @Test
    void getOpponentsAboutToWinWithNoOpponentAboutToWinShouldReturnEmptyList() {
        Opponent opponent1 = mock(Opponent.class);
        when(opponent1.isAboutToWin()).thenReturn(false);
        Opponent opponent2 = mock(Opponent.class);
        when(opponent2.isAboutToWin()).thenReturn(false);
        List<Opponent> opponents = List.of(opponent1, opponent2);
        when(richardBot.getOpponents()).thenReturn(opponents);

        assertFalse(richardBot.anOpponentIsAboutToWin());
    }

    @Test
    void getOpponentsAboutToWinWithNoOpponentAboutToWinShouldReturnFalse() {
        Opponent opponent1 = mock(Opponent.class);
        when(opponent1.isAboutToWin()).thenReturn(false);
        Opponent opponent2 = mock(Opponent.class);
        when(opponent2.isAboutToWin()).thenReturn(true);
        List<Opponent> opponents = List.of(opponent1, opponent2);
        when(richardBot.getOpponents()).thenReturn(opponents);

        assertTrue(richardBot.anOpponentIsAboutToWin());
    }

    @Test
    void getOpponentsAboutToWinWithAnOpponentAboutToWinShouldReturnTrue() {
        Opponent opponent1 = mock(Opponent.class);
        when(opponent1.isAboutToWin()).thenReturn(true);
        Opponent opponent2 = mock(Opponent.class);
        when(opponent2.isAboutToWin()).thenReturn(false);
        List<Opponent> opponents = List.of(opponent1, opponent2);
        when(richardBot.getOpponents()).thenReturn(opponents);

        assertTrue(richardBot.anOpponentIsAboutToWin());
    }

    @Test
    void choseThiefTargetWhenNoOpponentIsAboutToWinShouldCallSuperMethod() {
        List<Character> availableCharacters = List.of(new Thief(), new Bishop(), new King(), new Condottiere());
        when(richardBot.getAvailableCharacters()).thenReturn(availableCharacters);
        when(richardBot.anOpponentIsAboutToWin()).thenReturn(false);

        richardBot.chooseThiefTarget();

        verify(richardBot).useSuperChoseThiefEffect();
    }

    @Test
    void choseThiefTargetWhenOpponentIsAboutToWinAndNoBishopOrCondottiereShouldCallSuperMethod() {
        List<Character> availableCharacters = List.of(new Architect(), new King(), new Thief(), new Merchant());
        when(richardBot.getAvailableCharacters()).thenReturn(availableCharacters);
        when(richardBot.anOpponentIsAboutToWin()).thenReturn(true);

        richardBot.chooseThiefTarget();

        verify(richardBot).useSuperChoseThiefEffect();
    }

    @Test
    void choseThiefTargetWhenOpponentIsAboutToWinAndBishopAvailableShouldReturnBishop() {
        List<Character> availableCharacters = List.of(new Condottiere(), new King(), new Bishop(), new Merchant());
        when(richardBot.getAvailableCharacters()).thenReturn(availableCharacters);
        when(richardBot.anOpponentIsAboutToWin()).thenReturn(true);

        Optional<Character> result = richardBot.chooseThiefTarget();

        Character expectedCharacter = new Bishop();
        assertTrue(result.isPresent());
        verify(richardBot, never()).useSuperChoseThiefEffect();
        assertEquals(expectedCharacter, result.get());
    }

    @Test
    void choseThiefTargetWhenOpponentIsAboutToWinAndCondottiereAvailableShouldReturnCondottiere() {
        List<Character> availableCharacters = List.of(new Architect(), new King(), new Condottiere(), new Merchant());
        when(richardBot.getAvailableCharacters()).thenReturn(availableCharacters);
        when(richardBot.anOpponentIsAboutToWin()).thenReturn(true);

        Optional<Character> result = richardBot.chooseThiefTarget();

        Character expectedCharacter = new Condottiere();
        assertTrue(result.isPresent());
        verify(richardBot, never()).useSuperChoseThiefEffect();
        assertEquals(expectedCharacter, result.get());
    }
}