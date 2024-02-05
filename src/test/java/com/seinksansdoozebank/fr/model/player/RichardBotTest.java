package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
    void chooseAssassinTargetIfThiefIsPresentAndShouldPreventWealth() {
        // Configuration des joueurs et de leurs rôles
        Player thiefPlayer = spy(new SmartBot(10, deck, view));
        thiefPlayer.chooseCharacter(new ArrayList<>(List.of(new Thief())));

        // Configuration des conditions spécifiques du test
        richardBot.chooseCharacter(new ArrayList<>(List.of(new Assassin())));
        when(richardBot.getAvailableCharacters()).thenReturn(List.of(new Thief()));
        when(richardBot.shouldPreventWealth()).thenReturn(true); // Simuler une condition pour choisir le voleur
        // Exécution de la méthode à tester
        Character target = richardBot.chooseAssassinTarget();

        // Vérification que le personnage ciblé est le voleur, sous condition spécifique
        assertEquals(Role.THIEF, target.getRole(), "The target should be the Thief under specific conditions.");
    }

    @Test
    void chooseAssassinTargetIfCondottiereIsPresentAndThinkCondottiereWillBeChosenByTheLeadingOpponent() {
        // Configuration des joueurs et de leurs rôles
        Player condottierePlayer = spy(new SmartBot(10, deck, view));
        condottierePlayer.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));


        // Configuration des conditions spécifiques du test
        richardBot.chooseCharacter(new ArrayList<>(List.of(new Assassin())));
        when(richardBot.getOpponents()).thenReturn(List.of(condottierePlayer));
        when(richardBot.getAvailableCharacters()).thenReturn(List.of(new Condottiere()));
        when(richardBot.thinkCondottiereWillBeChosenByTheLeadingOpponent()).thenReturn(true); // Simuler une condition pour choisir le condottiere
        // Exécution de la méthode à tester
        Character target = richardBot.chooseAssassinTarget();

        // Vérification que le personnage ciblé est le condottiere, sous condition spécifique
        assertEquals(Role.CONDOTTIERE, target.getRole(), "The target should be the Condottiere under specific conditions.");
    }

    @Test
    void chooseAssassinTargetIfNoSpecificConditions() {
        Player kingPlayer = spy(new SmartBot(10, deck, view));
        kingPlayer.chooseCharacter(new ArrayList<>(List.of(new King())));

        // Configuration des conditions spécifiques du test
        richardBot.chooseCharacter(new ArrayList<>(List.of(new Assassin())));

        when(richardBot.getAvailableCharacters()).thenReturn(List.of(new King()));
        when(richardBot.getOpponents()).thenReturn(List.of(kingPlayer));

        when(richardBot.shouldPreventWealth()).thenReturn(false); // Simuler une condition pour choisir le voleur
        when(richardBot.thinkCondottiereWillBeChosenByTheLeadingOpponent()).thenReturn(false); // Simuler une condition pour choisir le condottiere
        // Exécution de la méthode à tester
        Character target = richardBot.chooseAssassinTarget();

        // Vérification que le personnage ciblé est le condottiere, sous condition spécifique
        assertEquals(Role.KING, target.getRole(), "The target should be the King under no specific conditions.");
    }

    @Test
    void shouldPreventWealth() {
        Player opponent = spy(new SmartBot(10, deck, view));
        opponent.increaseGold(8);
        when(richardBot.getOpponents()).thenReturn(List.of(opponent));
        boolean shouldPreventWealth = richardBot.shouldPreventWealth();
        assertTrue(shouldPreventWealth, "The bot should prevent wealth if an opponent has 7 or more gold.");
    }

    @Test
    void thinkCondottiereWillBeChosenByTheLeadingOpponent() {
        Player opponent = spy(new SmartBot(10, deck, view));
        opponent.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));
        when(richardBot.getOpponents()).thenReturn(List.of(opponent));
        when(opponent.isAboutToWin()).thenReturn(true);
        boolean thinkCondottiereWillBeChosenByTheLeadingOpponent = richardBot.thinkCondottiereWillBeChosenByTheLeadingOpponent();
        assertTrue(thinkCondottiereWillBeChosenByTheLeadingOpponent, "The bot should think the Condottiere will be chosen by the leading opponent if he is about to win.");
    }

    @Test
    void thinkThiefWillBeChosenByTheLeadingOpponent() {
        Player opponent = spy(new SmartBot(10, deck, view));
        opponent.chooseCharacter(new ArrayList<>(List.of(new Thief())));
        when(richardBot.getOpponents()).thenReturn(List.of(opponent));
        when(opponent.isAboutToWin()).thenReturn(true);
        boolean thinkThiefWillBeChosenByTheLeadingOpponent = richardBot.thinkThiefWillBeChosenByTheLeadingOpponent();
        assertTrue(thinkThiefWillBeChosenByTheLeadingOpponent, "The bot should think the Thief will be chosen by the leading opponent if he is about to win.");
    }
}