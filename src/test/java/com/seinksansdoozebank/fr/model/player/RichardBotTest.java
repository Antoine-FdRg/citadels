package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RichardBotTest {
    RichardBot richardBot;
    IView view;
    Deck deck;
    List<Character> charactersList;
    List<Opponent> opponentsList;

    Opponent opponentWithEmptyHand;
    Opponent opponentWithMoreGoldThanRichard;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickXCoin(Bank.MAX_COIN / 2);
        view = mock(Cli.class);
        deck = spy(new Deck());
        richardBot = spy(new RichardBot(10, deck, view));
        charactersList = List.of(
                new Thief(),
                new Assassin(),
                new Magician(),
                new King(),
                new Bishop(),
                new Merchant(),
                new Architect(),
                new Condottiere()
        );
        opponentWithEmptyHand=spy(new RandomBot(10,deck,view));
        opponentWithMoreGoldThanRichard=spy(new RandomBot(10,deck,view));
        opponentsList=new ArrayList<>();
        opponentsList.add(opponentWithEmptyHand);
        opponentsList.add(opponentWithMoreGoldThanRichard);
        when(opponentWithEmptyHand.getHandSize()).thenReturn(0);
        when(opponentWithMoreGoldThanRichard.getHandSize()).thenReturn(3);
        when(richardBot.getOpponents()).thenReturn(opponentsList);
    }

    @Test
    void ordinateCharactersMethodTest(){
        List<Character> orderedCharacters=richardBot.ordinateCharacters(charactersList);
        assertEquals(new Assassin(),orderedCharacters.get(0));
        assertEquals(new Magician(),orderedCharacters.get(1));
        assertEquals(new Merchant(),orderedCharacters.get(2));
        assertEquals(new Architect(),orderedCharacters.get(3));
        //assertEquals(new Bishop(),orderedCharacters.get(4));
        assertEquals(new Condottiere(),orderedCharacters.get(5));
    }

    @Test
    void numberOfEmptyHandsTest(){
        assertEquals(1,richardBot.numberOfEmptyHands(opponentsList));
    }

    @Test
    void numberOfPlayerWithMoreGoldTestTrue(){
        richardBot.decreaseGold(5);
        assertTrue(richardBot.numberOfPlayerWithMoreGold(opponentsList));
    }

    @Test
    void numberOfPlayerWithMoreGoldTestFalse(){
        richardBot.increaseGold(3);
        assertFalse(richardBot.numberOfPlayerWithMoreGold(opponentsList));
    }

}