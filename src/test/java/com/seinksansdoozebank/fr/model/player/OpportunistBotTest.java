package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class OpportunistBotTest {

    OpportunistBot spyOpportunistBot;
    IView view;
    Deck deck;
    Card cardCostThree;
    Card cardCostFive;
    Card templeCard;
    Card barrackCard;
    Card cardManor;
    Card cardPort;
    Card dracoport;

    @BeforeEach
    void setup() {
        Bank.reset();
        view = mock(Cli.class);
        deck = spy(new Deck());
        cardCostThree = new Card(District.DONJON);
        cardCostFive = new Card(District.FORTRESS);
        Player.resetIdCounter();
        spyOpportunistBot = spy(new OpportunistBot(10, deck, view));
        templeCard = new Card(District.TEMPLE);
        barrackCard = new Card(District.BARRACK);
        cardPort = new Card(District.PORT);
        cardManor = new Card(District.MANOR);
        dracoport = new Card(District.PORT_FOR_DRAGONS);
    }

    @Test
    void testChooseCharacterImplWithReligiousDistrict() {
        List<Character> characters = new ArrayList<>(List.of(
                new Bishop(),
                new Condottiere(),
                new Thief(),
                new Merchant()
        ));
        when(spyOpportunistBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.CHURCH))));

        Character chosenCharacter = spyOpportunistBot.chooseCharacter(characters);

        assertEquals(Role.BISHOP, chosenCharacter.getRole());
    }

    @Test
    void testChooseCharacterImplWithEnoughGoldAndNoReligiousDistrict() {
        List<Character> characters = new ArrayList<>(List.of(
                new Bishop(),
                new Condottiere(),
                new Thief(),
                new Merchant()
        ));
        when(spyOpportunistBot.getNbGold()).thenReturn(2);
        when(spyOpportunistBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.MARKET_PLACE))));

        Character chosenCharacter = spyOpportunistBot.chooseCharacter(characters);

        assertEquals(Role.CONDOTTIERE, chosenCharacter.getRole());
    }

    @Test
    void testChooseCharacterImplWithEnoughGoldAndNoReligiousDistrictAndOpponentWithEnoughGold() {
        List<Character> characters = new ArrayList<>(List.of(
                new Bishop(),
                new Condottiere(),
                new Thief(),
                new Merchant()
        ));
        RandomBot opponent = spy(new RandomBot(4, deck, view));
        when(spyOpportunistBot.getNbGold()).thenReturn(2);
        when(spyOpportunistBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.MARKET_PLACE))));
        when(spyOpportunistBot.getOpponents()).thenReturn(new ArrayList<>(List.of(opponent)));
        when(opponent.getNbGold()).thenReturn(4);

        Character chosenCharacter = spyOpportunistBot.chooseCharacter(characters);

        assertEquals(Role.THIEF, chosenCharacter.getRole());
    }

    @Test
    void testCharacterImplWithoutAnyCriteria() {
        List<Character> characters = new ArrayList<>(List.of(
                new Bishop(),
                new Condottiere(),
                new Thief(),
                new Merchant()
        ));

        Random randomMock = mock(Random.class);
        when(randomMock.nextInt(4)).thenReturn(3);
        spyOpportunistBot.setRandom(randomMock);

        when(spyOpportunistBot.getNbGold()).thenReturn(0);
        when(spyOpportunistBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.MARKET_PLACE))));

        Character chosenCharacter = spyOpportunistBot.chooseCharacter(characters);

        assertEquals(Role.MERCHANT, chosenCharacter.getRole());
    }

    @Test
    void testChooseCardWithReligiousDistrict() {
        when(spyOpportunistBot.getHand()).thenReturn(new ArrayList<>(List.of(templeCard, barrackCard, cardManor, cardPort, dracoport)));

        Optional<Card> chosenCard = spyOpportunistBot.chooseCard();
        assertTrue(chosenCard.isPresent());

        assertEquals(templeCard, chosenCard.get());
    }

    @Test
    void testChooseCardWithEmptyHand() {
        when(spyOpportunistBot.getHand()).thenReturn(new ArrayList<>());

        assertFalse(spyOpportunistBot.chooseCard().isPresent());
    }

    @Test
    void toStringTest() {
        assertEquals("Le bot opportuniste 1", spyOpportunistBot.toString());
    }
}
