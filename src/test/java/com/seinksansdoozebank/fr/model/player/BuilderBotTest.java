package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.DoesNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BuilderBotTest {

    BuilderBot spyBuilderBot;
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
        spyBuilderBot = spy(new BuilderBot(10, deck, view));
        templeCard = new Card(District.TEMPLE);
        barrackCard = new Card(District.BARRACK);
        cardPort = new Card(District.PORT);
        cardManor = new Card(District.MANOR);
        dracoport = new Card(District.PORT_FOR_DRAGONS);
    }

    @Test
    void chooseCharacterWithNobilityDistrict() {
        List<Character> characters = new ArrayList<>(List.of(new King(), new Merchant(), new Architect()));
        when(spyBuilderBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.PALACE)))); // Add a Nobility district to the citadel

        Character chosenCharacter = spyBuilderBot.chooseCharacter(characters);

        assertEquals(Role.KING, chosenCharacter.getRole());
    }

    @Test
    void chooseCharacterWithMultipleCommerceDistricts() {
        List<Character> characters = new ArrayList<>(List.of(new Merchant()));
        when(spyBuilderBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.MARKET_PLACE), new Card(District.TAVERN))));

        Character chosenCharacter = spyBuilderBot.chooseCharacter(characters);

        assertEquals(Role.MERCHANT, chosenCharacter.getRole());
    }

    @Test
    void chooseCharacterWithAllTypeOfDistrictsShouldGetKing() {
        List<Character> characters = new ArrayList<>(List.of(new King(), new Merchant(), new Architect()));
        when(spyBuilderBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.PALACE), new Card(District.TAVERN), new Card(District.WATCH_TOWER), new Card(District.PORT))));

        Character chosenCharacter = spyBuilderBot.chooseCharacter(characters);

        assertEquals(Role.KING, chosenCharacter.getRole());
    }


    @Test
    void chooseCharacterWithoutAnyCriteria() {
        List<Character> characters = new ArrayList<>(List.of(new King(), new Merchant(), new Architect()));

        when(spyBuilderBot.getNbGold()).thenReturn(0);
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        spyBuilderBot.setRandom(mockRandom);

        Character chosenCharacter = spyBuilderBot.chooseCharacter(characters);

        assertEquals(Role.KING, chosenCharacter.getRole());
    }


    @Test
    void chooseCharacterWithArchitectCriteria() {
        List<Character> characters = new ArrayList<>(List.of(new Architect()));
        spyBuilderBot.getHand().add(cardPort); // Add a district to the hand
        spyBuilderBot.getHand().add(barrackCard); // Add another district to the hand
        spyBuilderBot.increaseGold(6); // Set gold to 6 to meet the criteria for Architect

        Character chosenCharacter = spyBuilderBot.chooseCharacter(characters);

        assertEquals(Role.ARCHITECT, chosenCharacter.getRole());
    }

    @Test
    void keepOneDiscardOthersWithNobilityDistrict() {
        List<Card> pickedCards = new ArrayList<>(List.of(cardManor, cardPort, new Card(District.PALACE)));

        Card chosenCard = spyBuilderBot.keepOneDiscardOthers(pickedCards);

        assertEquals(DistrictType.NOBILITY, chosenCard.getDistrict().getDistrictType());
    }

    @Test
    void keepOneDiscardOthersWithCommerceDistrict() {
        List<Card> pickedCards = new ArrayList<>(List.of(barrackCard, new Card(District.TAVERN), cardPort));

        Card chosenCard = spyBuilderBot.keepOneDiscardOthers(pickedCards);

        assertEquals(DistrictType.TRADE_AND_CRAFTS, chosenCard.getDistrict().getDistrictType());
    }

    @Test
    void keepOneDiscardOthersChooseCheapest() {
        List<Card> pickedCards = new ArrayList<>(List.of(cardCostFive, cardCostThree));

        Card chosenCard = spyBuilderBot.keepOneDiscardOthers(pickedCards);

        assertEquals(cardCostThree, chosenCard);
    }

    @Test
    void chooseCardWithCommonCharacter() {
        BuilderBot spyBuilderBot = spy(new BuilderBot(10, deck, view));
        CommonCharacter commonCharacter = mock(CommonCharacter.class);
        when(commonCharacter.getTarget()).thenReturn(DistrictType.TRADE_AND_CRAFTS);
        when(spyBuilderBot.getCharacter()).thenReturn(commonCharacter);

        Card tradeCard = new Card(District.TOWN_HALL);
        Card manorCard = new Card(District.MANOR);
        when(spyBuilderBot.getHand()).thenReturn(new ArrayList<>(List.of(tradeCard, manorCard)));
        when(spyBuilderBot.getCitadel()).thenReturn(new ArrayList<>(List.of(manorCard)));

        Optional<Card> chosenCard = spyBuilderBot.chooseCard();
        assertTrue(chosenCard.isPresent());
        assertEquals(tradeCard, chosenCard.get());
    }

    @Test
    void chooseCardWithoutCommonCharacter() {
        BuilderBot spyBuilderBot = spy(new BuilderBot(10, deck, view));
        // spyBuilderBot.setCharacter(mock(Character.class));
        when(spyBuilderBot.getCharacter()).thenReturn(mock(Character.class));

        Card tradeCard = new Card(District.TOWN_HALL);
        Card manorCard = new Card(District.MANOR);
        when(spyBuilderBot.getHand()).thenReturn(new ArrayList<>(List.of(tradeCard, manorCard)));
        when(spyBuilderBot.getCitadel()).thenReturn(new ArrayList<>(List.of(manorCard)));

        Optional<Card> chosenCard = spyBuilderBot.chooseCard();

        assertEquals(tradeCard, chosenCard.orElse(null));
    }
}
