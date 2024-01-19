package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SmartBotTest {
    SmartBot spySmartBot;
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
        view = mock(Cli.class);
        deck = spy(new Deck());
        cardCostThree = new Card(District.DONJON);
        cardCostFive = new Card(District.FORTRESS);
        spySmartBot = spy(new SmartBot(10, deck, view));
        templeCard = new Card(District.TEMPLE);
        barrackCard = new Card(District.BARRACK);
        cardPort = new Card(District.PORT);
        cardManor = new Card(District.MANOR);
        dracoport = new Card(District.PORT_FOR_DRAGONS);
    }

    @Test
    void playWithEmptyChosenDistrictShouldPickDistrictAndBuild() {
        Optional<Card> optDistrictCardCostFive = Optional.of(cardCostFive);
        doReturn(optDistrictCardCostFive).when(spySmartBot).chooseCard();
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new King())));
        spySmartBot.play();

        verify(view, times(1)).displayPlayerStartPlaying(spySmartBot);
        verify(view, times(1)).displayPlayerRevealCharacter(spySmartBot);
        verify(spySmartBot, times(1)).pickTwoCardKeepOneDiscardOne();
        verify(spySmartBot, times(1)).playACard();
        verify(view, times(1)).displayPlayerPlaysCard(any(), any());
        verify(view, times(2)).displayPlayerInfo(spySmartBot);
    }

    @Test
    void playWithUnbuildableDistrictShouldPickGoldAndBuild() {
        when(spySmartBot.getHand()).thenReturn(List.of(cardCostFive));
        when(spySmartBot.hasACardToPlay()).thenReturn(false, false, true);
        doReturn(Optional.of(cardCostThree)).when(spySmartBot).playACard();
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new Bishop(), new King(), new Merchant(), new Condottiere())));
        spySmartBot.play();

        verify(view, times(1)).displayPlayerStartPlaying(spySmartBot);
        verify(view, times(1)).displayPlayerRevealCharacter(spySmartBot);
        verify(spySmartBot, times(1)).pickGold();
        verify(spySmartBot, times(1)).playACard();
        verify(view, times(1)).displayPlayerPlaysCard(any(), any());
        verify(view, times(2)).displayPlayerInfo(spySmartBot);
    }

    @Test
    void playWithABuildableDistrictShouldBuildAndPickSomething() {
        List<Card> hand = new ArrayList<>(List.of(cardCostFive));
        when(spySmartBot.getHand()).thenReturn(hand);
        when(spySmartBot.hasACardToPlay()).thenReturn(true);
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new Bishop(), new King(), new Merchant(), new Condottiere())));
        spySmartBot.play();

        verify(view, times(1)).displayPlayerStartPlaying(spySmartBot);
        verify(view, times(1)).displayPlayerRevealCharacter(spySmartBot);
        verify(spySmartBot, times(1)).playACard();
        verify(view, times(1)).displayPlayerPlaysCard(any(), any());
        verify(spySmartBot, times(1)).pickSomething();
        verify(view, times(2)).displayPlayerInfo(spySmartBot);
    }

    @Test
    void pickSomethingWithEmptyChoosenDistrict() {
        Optional<Card> optDistrict = Optional.empty();
        doReturn(optDistrict).when(spySmartBot).playACard();
        spySmartBot.pickSomething();

        verify(spySmartBot, times(0)).pickGold();
        verify(spySmartBot, times(1)).pickTwoCardKeepOneDiscardOne();
    }

    @Test
    void pickSomethingWithChoosenDistrictAndNotEnoughGold() {
        spySmartBot.decreaseGold(spySmartBot.getNbGold());
        Optional<Card> optDistrict = Optional.of(cardCostThree);
        doReturn(optDistrict).when(spySmartBot).chooseCard();
        spySmartBot.pickSomething();
        assertTrue(spySmartBot.getNbGold() < cardCostThree.getDistrict().getCost());
        verify(spySmartBot, times(1)).pickGold();
        verify(spySmartBot, times(0)).pickTwoCardKeepOneDiscardOne();
    }

    @Test
    void pickSomethingWithChoosenDistrictAndEnoughGold() {
        Optional<Card> optDistrict = Optional.of(cardCostThree);
        doReturn(optDistrict).when(spySmartBot).chooseCard();
        spySmartBot.pickSomething();
        assertTrue(spySmartBot.getNbGold() >= cardCostThree.getDistrict().getCost());
        verify(spySmartBot, times(0)).pickGold();
        verify(spySmartBot, times(1)).pickTwoCardKeepOneDiscardOne();
    }

    @Test
    void pickTwoDistrictKeepOneDiscardOneShouldKeepTheCheaperOne() {
        boolean handIsEmpty = spySmartBot.getHand().isEmpty();
        spySmartBot.pickTwoCardKeepOneDiscardOne();

        assertTrue(handIsEmpty);
        assertEquals(1, spySmartBot.getHand().size());
        verify(view, times(1)).displayPlayerPickCards(spySmartBot, 1);
        verify(deck, times(2)).pick();
        verify(deck, times(1)).discard(any(Card.class));
        assertTrue(spySmartBot.getHand().get(0).getDistrict().getCost() <= deck.getDeck().get(0).getDistrict().getCost());
    }

    @Test
    void chooseDistrictWithEmptyHand() {
        boolean handIsEmpty = spySmartBot.getHand().isEmpty();
        Optional<Card> chosenDistrict = spySmartBot.chooseCard();
        assertTrue(chosenDistrict.isEmpty());
        assertTrue(handIsEmpty);
    }

    @Test
    void chooseDistrictShouldReturnANotAlreadyBuiltDistrict() {
        spySmartBot.getHand().add(cardCostThree);
        spySmartBot.getHand().add(cardCostFive);
        when(spySmartBot.getCitadel()).thenReturn(List.of(cardCostThree));
        Optional<Card> chosenDistrict = spySmartBot.chooseCard();
        assertTrue(chosenDistrict.isPresent());
        assertEquals(cardCostFive, chosenDistrict.get());
    }

    @Test
    void getCheaperDistricWithEmptyListtShouldReturnEmptyOptional() {
        Optional<Card> cheaperCard = spySmartBot.getCheaperCard(List.of());
        assertTrue(cheaperCard.isEmpty());
    }

    @Test
    void getCheaperDistrictShouldReturnTheCheaperDistrict() {
        List<Card> districtList = List.of(cardCostFive, cardCostThree, cardCostFive);
        Optional<Card> cheaperCard = spySmartBot.getCheaperCard(districtList);
        assertTrue(cheaperCard.isPresent());
        assertEquals(cardCostThree, cheaperCard.get());
    }

    @Test
    void getDistrictTypeFrequencyList() {
        List<Card> districtList = List.of(cardCostFive, cardCostThree, cardCostFive);
        List<DistrictType> districtTypeFrequencyList = spySmartBot.getDistrictTypeFrequencyList(districtList);
        assertEquals(2, districtTypeFrequencyList.size());
        assertEquals(DistrictType.SOLDIERLY, districtTypeFrequencyList.get(0));
        assertEquals(DistrictType.PRESTIGE, districtTypeFrequencyList.get(1));
    }

    List<Character> createCharactersList() {
        Bishop bishop = new Bishop();
        King king = new King();
        Merchant merchant = new Merchant();
        Condottiere condottiere = new Condottiere();

        List<Character> characters = new ArrayList<>();
        characters.add(bishop);
        characters.add(king);
        characters.add(merchant);
        characters.add(condottiere);
        return characters;
    }

    @Test
    void chooseCharacterWhenMostOwnedDistrictTypeCharacterIsAvailable() {
        List<Character> characters = createCharactersList();
        Card manorCard = new Card(District.MANOR);
        Card castleCard = new Card(District.CASTLE);
        Card palaceCard = new Card(District.PALACE);
        Card laboratoryCard = new Card(District.LABORATORY);

        ArrayList<Card> citadel = new ArrayList<>();
        citadel.add(manorCard);
        citadel.add(castleCard);
        citadel.add(palaceCard);
        citadel.add(laboratoryCard);

        when(spySmartBot.getCitadel()).thenReturn(citadel);
        spySmartBot.chooseCharacter(characters);

        verify(view, times(1)).displayPlayerChooseCharacter(spySmartBot);
        assertEquals(characters.get(1), spySmartBot.getCharacter());
    }

    @Test
    void chooseCharacterWhenMostOwnedDistrictTypeCharacterIsNotAvailable() {
        List<Character> characters = createCharactersList();
        characters.remove(1); // Remove the king
        Card manorCard = new Card(District.MANOR);
        Card castleCard = new Card(District.CASTLE);
        Card palaceCard = new Card(District.PALACE);
        Card laboratoryCard = new Card(District.FORTRESS);

        ArrayList<Card> citadel = new ArrayList<>();
        citadel.add(manorCard);
        citadel.add(castleCard);
        citadel.add(palaceCard);
        citadel.add(laboratoryCard);

        when(spySmartBot.getCitadel()).thenReturn(citadel);
        spySmartBot.chooseCharacter(characters);

        verify(view, times(1)).displayPlayerChooseCharacter(spySmartBot);
        assertEquals(characters.get(2), spySmartBot.getCharacter());
    }

    @Test
    void chooseCharacterWhenMostOwnedDistrictTypeCharacterIsNotAvailableAndSecondNeither() {
        List<Character> characters = createCharactersList();
        characters.remove(1); // Remove the king
        characters.remove(2); // Remove the condottiere
        Card manorCard = new Card(District.MANOR);
        Card castleCard = new Card(District.CASTLE);
        Card palaceCard = new Card(District.PALACE);
        Card laboratoryCard = new Card(District.FORTRESS);
        Card barrackCard = new Card(District.BARRACK);
        Card cornerShopCard = new Card(District.CORNER_SHOP);

        ArrayList<Card> citadel = new ArrayList<>();
        citadel.add(manorCard);
        citadel.add(castleCard);
        citadel.add(palaceCard);
        citadel.add(laboratoryCard);
        citadel.add(barrackCard);
        citadel.add(cornerShopCard);

        when(spySmartBot.getCitadel()).thenReturn(citadel);
        spySmartBot.chooseCharacter(characters);

        verify(view, times(1)).displayPlayerChooseCharacter(spySmartBot);
        assertEquals(characters.get(1), spySmartBot.getCharacter());
    }

    @Test
    void testChooseColorCourtyardOfMiracleGetTheCorrectColor() {
        // Set a citadel with 4 district with different colors
        Card manorCard = new Card(District.TEMPLE);
        Card castleCard = new Card(District.MANOR);
        Card palaceCard = new Card(District.TAVERN);
        Card laboratoryCard = new Card(District.CEMETERY);
        // Add the Courtyard of miracle
        Card courtyardOfMiracleCard = new Card(District.COURTYARD_OF_MIRACLE);
        ArrayList<Card> citadel = new ArrayList<>(
                List.of(manorCard, castleCard, palaceCard, laboratoryCard, courtyardOfMiracleCard)
        );
        when(spySmartBot.getCitadel()).thenReturn(citadel);
        spySmartBot.chooseColorCourtyardOfMiracle();
        assertEquals(DistrictType.SOLDIERLY, spySmartBot.getColorCourtyardOfMiracleType());
    }

    @Test
    void choseAssassinTargetWithTargetInList() {
        Player architectPlayer = spy(new SmartBot(10, deck, view));
        when(architectPlayer.getCharacter()).thenReturn(new Architect());
        Player merchantPlayer = spy(new SmartBot(10, deck, view));
        when(merchantPlayer.getCharacter()).thenReturn(new Merchant());

        when(spySmartBot.getOpponents()).thenReturn(List.of(architectPlayer));

        Character target = spySmartBot.choseAssassinTarget();

        assertInstanceOf(Architect.class, target);
    }

    @Test
    void choseAssassinTargetWithTargetNotInList() {
        Player architectPlayer = spy(new SmartBot(10, deck, view));
        when(architectPlayer.getCharacter()).thenReturn(new Bishop());
        Player merchantPlayer = spy(new SmartBot(10, deck, view));
        when(merchantPlayer.getCharacter()).thenReturn(new Merchant());

        when(spySmartBot.getOpponents()).thenReturn(List.of(architectPlayer, merchantPlayer));

        Character target = spySmartBot.choseAssassinTarget();

        assertTrue(target instanceof Bishop || target instanceof Merchant);
    }

    @Test
    void playWithArchitect() {
        Optional<Card> optDistrict = Optional.of(templeCard);
        doReturn(optDistrict).when(spySmartBot).playACard();
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new Architect())));
        spySmartBot.play();

        verify(spySmartBot, atMost(3)).playACard();
    }

    @Test
    void useEffectTest() {
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new Architect())));
        spySmartBot.useEffect();
        verify(spySmartBot, atMost(1)).useEffectArchitectPickCards();
    }

    @Test
    void priceOfNumbersOfCardsTest() {
        List<Card> architectHand = new ArrayList<>(List.of(templeCard, barrackCard));
        when(spySmartBot.getHand()).thenReturn(architectHand);
        assertEquals(4, spySmartBot.getPriceOfNumbersOfCheaperCards(2));
    }

    @Test
    void architectTryToCompleteFiveDistrictTypesTest() {
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new Architect())));
        List<Card> architectHand = new ArrayList<>(List.of(templeCard, barrackCard, cardPort, cardManor));
        List<Card> architectCitadelle = new ArrayList<>(List.of(new Card(District.MARKET_PLACE)));
        when(spySmartBot.getHand()).thenReturn(architectHand);
        when(spySmartBot.getCitadel()).thenReturn(architectCitadelle);
        spySmartBot.architectTryToCompleteFiveDistrictTypes();
        //On vérifie que la méthode architectTryToCompleteFiveDistrictTypes est bien appelée
        verify(spySmartBot, times(1)).architectTryToCompleteFiveDistrictTypes();
        //On vérifie que le bot ne pose pas plus de 3 districts dans sa citadelle
        verify(view, atMost(3)).displayPlayerPlaysCard(any(), any());
    }

    @Test
    void architectTryToCompleteFiveDistrictTypesButHecantTest() {
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new Architect())));
        spySmartBot.decreaseGold(9);
        List<Card> architectHand = new ArrayList<>(List.of(barrackCard, templeCard, new Card(District.PALACE)));
        List<Card> architectCitadelle = new ArrayList<>(List.of(new Card(District.MARKET_PLACE)));
        when(spySmartBot.getHand()).thenReturn(architectHand);
        when(spySmartBot.getCitadel()).thenReturn(architectCitadelle);
        spySmartBot.architectTryToCompleteFiveDistrictTypes();
        //On vérifie que la méthode architectTryToCompleteFiveDistrictTypes est bien appelée
        verify(spySmartBot, times(1)).architectTryToCompleteFiveDistrictTypes();
        //Ici il ne peut pas poser de district car il n'a plus de gold
        verify(view, atMost(1)).displayPlayerPlaysCard(any(), any());
    }

    @Test
    void useEffectOfTheArchitectWhenDoesNotHaveFiveDistrictTypes() {
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new Architect())));
        List<Card> architectHand = new ArrayList<>(List.of(templeCard, barrackCard));
        List<Card> architectCitadelle = new ArrayList<>(List.of(new Card(District.MARKET_PLACE)));
        when(spySmartBot.getHand()).thenReturn(architectHand);
        when(spySmartBot.getCitadel()).thenReturn(architectCitadelle);
        spySmartBot.useEffectOfTheArchitect();
        verify(spySmartBot, atLeastOnce()).architectTryToCompleteFiveDistrictTypes();
    }

    /**
     * On vérifie que le smartBot joue bien la prestige qu'il a dans sa main  si il n'a pas 5 cartes dans sa citadelle
     */
    @Test
    void useEffectOfTheArchitectWhenHasAPrestigeCardTest() {
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new Architect())));
        List<Card> architectHand = new ArrayList<>(List.of(dracoport, cardPort));
        List<Card> architectCitadelle = spy(new ArrayList<>(List.of(new Card(District.MARKET_PLACE))));
        when(spySmartBot.getHand()).thenReturn(architectHand);
        when(spySmartBot.getCitadel()).thenReturn(architectCitadelle);
        spySmartBot.useEffectOfTheArchitect();
        verify(view, times(1)).displayPlayerPlaysCard(any(), any());
    }

    /**
     * On vérifie que le samrtBot ne joue pas la carte prestige qu'il a dans sa main car il n'a pas assez d'argent
     */
    @Test
    void useEffectOfTheArchitectWhenHasAPrestigeCardButCantPlayItTest() {
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(new Architect())));
        List<Card> architectHand = new ArrayList<>(List.of(dracoport, cardPort));
        List<Card> architectCitadelle = spy(new ArrayList<>(List.of(new Card(District.MARKET_PLACE))));
        when(spySmartBot.getHand()).thenReturn(architectHand);
        when(spySmartBot.getCitadel()).thenReturn(architectCitadelle);
        spySmartBot.decreaseGold(7);
        spySmartBot.useEffectOfTheArchitect();
        verify(view, times(0)).displayPlayerPlaysCard(any(), any());
    }

    /**
     * We check that the player will use a specific strategy using the magician effect :
     * - The player has no card, so he will switch his hand with the player that have the most cards
     */
    @Test
    void useEffectOfTheMagicianWhenThePlayerHasNoCardTest() {
        Magician magician = new Magician();
        magician.setPlayer(spySmartBot);
        // Set the player character to magician
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(magician)));
        // Set the Hand of the player to Empty
        spySmartBot.getHand().clear();
        when(spySmartBot.getHand()).thenReturn(new ArrayList<>());
        // Set the hand of the other player to 2 cards
        Player otherPlayer = spy(new SmartBot(10, deck, view));
        List<Card> otherPlayerHand = new ArrayList<>(List.of(new Card(District.CEMETERY), new Card(District.CASTLE)));
        List<Card> otherPlayerHandCopy = new ArrayList<>(List.of(new Card(District.CEMETERY), new Card(District.CASTLE)));
        otherPlayer.getHand().addAll(otherPlayerHand);
        when(otherPlayer.getHand()).thenReturn(otherPlayerHand);
        // Set the hand of another player to 1 card
        Player anotherPlayer = spy(new SmartBot(10, deck, view));
        anotherPlayer.getHand().add(new Card(District.CEMETERY));
        when(anotherPlayer.getHand()).thenReturn(List.of(new Card(District.CEMETERY)));
        // Set the opponents of the player
        when(spySmartBot.getOpponents()).thenReturn(List.of(otherPlayer, anotherPlayer));

        // Set the player to switch hand with the other player
        spySmartBot.useEffectMagician(magician);

        // Check that the magician effect is used
        verify(spySmartBot, times(1)).useEffectMagician(magician);
        // Check that the magician have the old hand of the other player
        assertEquals(otherPlayerHandCopy, spySmartBot.getHand());
        // Check that the other player have the old hand of the magician
        assertEquals(0, otherPlayer.getHand().size());
    }

    /**
     * We check that the player will use a specific strategy using the magician effect :
     * - The player has the most cards, so he will switch his cards that cost more than 2 golds with the deck
     */
    @Test
    void useEffectOfTheMagicianWhenThePlayerHasTheMostCardsTest() {
        Magician magician = new Magician();
        magician.setPlayer(spySmartBot);
        // Set the player character to magician
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(magician)));
        // Set the Hand of the player to 3 cards
        spySmartBot.getHand().clear();
        Card monasteryCard = new Card(District.MONASTERY);
        Card cathedralCard = new Card(District.CATHEDRAL);
        List<Card> playerList = new ArrayList<>(
                List.of(
                        new Card(District.TEMPLE),
                        new Card(District.CHURCH),
                        monasteryCard,
                        cathedralCard
                )
        );
        spySmartBot.getHand().addAll(playerList);
        when(spySmartBot.getHand()).thenReturn(playerList);
        // Set the hand of the other player and another player to 1 cards
        Player otherPlayer = spy(new SmartBot(10, deck, view));
        otherPlayer.getHand().add(new Card(District.CEMETERY));
        when(otherPlayer.getHand()).thenReturn(List.of(new Card(District.CEMETERY)));
        Player anotherPlayer = spy(new SmartBot(10, deck, view));
        anotherPlayer.getHand().add(new Card(District.CEMETERY));
        when(anotherPlayer.getHand()).thenReturn(List.of(new Card(District.CEMETERY)));

        // Set the opponents of the player
        when(spySmartBot.getOpponents()).thenReturn(List.of(otherPlayer, anotherPlayer));
        // Set the player to switch hand with the other player
        spySmartBot.useEffectMagician(magician);

        // Check that the magician effect is used
        verify(spySmartBot, times(1)).useEffectMagician(magician);
        // Check that the magician don't have anymore the instance of the monasteryCard and the cathedralCard
        assertFalse(spySmartBot.getHand().contains(monasteryCard));
        assertFalse(spySmartBot.getHand().contains(cathedralCard));
    }

    /**
     * We check that the player will use a specific strategy using the magician effect :
     * - The player has the same number of cards as the other player, so he will change the cards that cost more than 2 golds with the deck
     */
    @Test
    void useEffectOfTheMagicianWhenThePlayerHasTheSameNumberOfCardsTest() {
        Magician magician = new Magician();
        magician.setPlayer(spySmartBot);
        // Set the player character to magician
        spySmartBot.chooseCharacter(new ArrayList<>(List.of(magician)));
        // Set the Hand of the player to 3 cards
        spySmartBot.getHand().clear();
        Card monasteryCard = new Card(District.MONASTERY);
        Card cathedralCard = new Card(District.CATHEDRAL);
        List<Card> playerList = new ArrayList<>(
                List.of(
                        new Card(District.TEMPLE),
                        monasteryCard,
                        cathedralCard
                )
        );
        spySmartBot.getHand().addAll(playerList);
        when(spySmartBot.getHand()).thenReturn(playerList);
        // Set the hand of the other player and another player to 3 cards
        Player otherPlayer = spy(new SmartBot(10, deck, view));
        otherPlayer.getHand().addAll(List.of(new Card(District.CEMETERY), new Card(District.CEMETERY), new Card(District.CEMETERY)));
        when(otherPlayer.getHand()).thenReturn(List.of(new Card(District.CEMETERY), new Card(District.CEMETERY), new Card(District.CEMETERY)));
        Player anotherPlayer = spy(new SmartBot(10, deck, view));
        anotherPlayer.getHand().addAll(List.of(new Card(District.CEMETERY), new Card(District.CEMETERY), new Card(District.CEMETERY)));
        when(anotherPlayer.getHand()).thenReturn(List.of(new Card(District.CEMETERY), new Card(District.CEMETERY), new Card(District.CEMETERY)));

        // Set the opponents of the player
        when(spySmartBot.getOpponents()).thenReturn(List.of(otherPlayer, anotherPlayer));

        // Set the player to switch hand with the other player
        spySmartBot.useEffectMagician(magician);

        // Check that the magician effect is used
        verify(spySmartBot, times(1)).useEffectMagician(magician);
        // Check that the magician don't have anymore the instance of the monasteryCard and the cathedralCard
        assertFalse(spySmartBot.getHand().contains(monasteryCard));
        assertFalse(spySmartBot.getHand().contains(cathedralCard));
    }

}