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
        assertEquals(new Bishop(), orderedCharacters.get(4));
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


    @Test
    void shouldChooseAssassinTestTrue(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        assertTrue(richardBot.shouldChooseAssassin());
    }

    @Test
    void shouldChooseAssassinTestFalseWhenNoHandsAreEmpty(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        opponentsList.remove(opponentWithEmptyHand);
        assertFalse(richardBot.shouldChooseAssassin());
    }

    @Test
    void shouldChooseAssassinTestFalseWhenRichardBotHandHasNotALotOfCards(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.BARRACK))));
        opponentsList.remove(opponentWithEmptyHand);
        assertFalse(richardBot.shouldChooseAssassin());
    }

    @Test
    void shouldChooseMagicianTestTrue(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>());
        assertTrue(richardBot.shouldChooseMagician());
    }

    @Test
    void shouldChooseMagicianTestFalse(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        assertFalse(richardBot.shouldChooseMagician());
    }

    @Test
    void shouldChooseMerchantTestTrue(){
        richardBot.decreaseGold(10);
        assertTrue(richardBot.shouldChooseMerchant());
    }

    @Test
    void shouldChooseMerchantTestFalse(){
        assertFalse(richardBot.shouldChooseMerchant());
    }

    @Test
    void shouldChooseArchitectTestTrue(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        richardBot.decreaseGold(2);
        assertTrue(richardBot.shouldChooseArchitect());
    }

    @Test
    void shouldChooseArchitectTestFalseBecauseNoPlayersHaveMoreGoldThanRichardBot(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        assertFalse(richardBot.shouldChooseArchitect());
    }

    @Test
    void shouldChooseArchitectTestFalseBecauseRichardCantAffordMoreThanOneCard(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.PORT),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        richardBot.decreaseGold(9);
        assertFalse(richardBot.shouldChooseArchitect());
    }

    @Test
    void shouldChooseBishopTestTrue(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        assertTrue(richardBot.shouldChooseBishop());
    }

    @Test
    void shouldChooseBishopTestFalse(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        richardBot.decreaseGold(9);
        assertFalse(richardBot.shouldChooseBishop());
    }

    @Test
    void shouldChooseCondottiereTestTrue(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        richardBot.decreaseGold(9);
        assertTrue(richardBot.shouldChooseCondottiere());
    }

    @Test
    void shouldChooseCondottiereTestFalse(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        assertFalse(richardBot.shouldChooseCondottiere());
    }

    @Test
    void shouldChooseBecauseLastCardToBuyTestTrue(){
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        assertEquals(Optional.of(new Assassin()),richardBot.shouldChooseBecauseLastCardToBuy(charactersList));
    }

    @Test
    void shouldChooseBecauseLastCardToBuyTestFalse(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        assertEquals(Optional.empty(),richardBot.shouldChooseBecauseLastCardToBuy(charactersList));
    }


    @Test
    void chooseCharacterImplWhenShouldChooseBecauseLastCardToBuy(){
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        opponentsList.remove(opponentWithEmptyHand);
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot,times(1)).shouldChooseBecauseLastCardToBuy(any());
        verify(richardBot,times(0)).shouldChooseAssassin();
    }

    @Test
    void chooseCharacterBecauseShouldChooseAssassinTest(){
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.BARRACK))));
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot,times(1)).shouldChooseAssassin();
        assertEquals(new Assassin(),richardBot.chooseCharacterImpl(charactersList));
    }

    @Test
    void chooseCharacterBecauseShouldChooseMagicianTest(){
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE))));
        when(richardBot.getHand()).thenReturn(new ArrayList<>());
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot,times(1)).shouldChooseMagician();
    }

    @Test
    void chooseCharacterBecauseShouldChooseMerchantTest(){
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE))));
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.PORT))));
        richardBot.decreaseGold(9);
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot,times(1)).shouldChooseMerchant();
    }

    @Test
    void chooseCharacterBecauseShouldChooseArchitectTest(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.BARRACK))));
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY))));
        richardBot.decreaseGold(2);
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot,times(1)).shouldChooseArchitect();
    }

    @Test
    void chooseCharacterBecauseShouldChooseBishopTest(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY))));
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY))));
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot,times(1)).shouldChooseBishop();
    }

    @Test
    void chooseCharacterBecauseShouldChooseBCondottiereTest(){
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.PORT_FOR_DRAGONS))));
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY))));
        richardBot.decreaseGold(8);
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot,times(1)).shouldChooseCondottiere();
    }



}