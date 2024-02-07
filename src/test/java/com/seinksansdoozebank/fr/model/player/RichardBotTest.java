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
import com.seinksansdoozebank.fr.model.character.roles.Role;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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

    Player opponentWithEmptyHand;
    Player opponentWithMoreGoldThanRichard;

    Player opponentWithSevenDistrictsInCitadel;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickXCoin(Bank.MAX_COIN / 2);
        view = mock(Cli.class);
        deck = spy(new Deck());
        richardBot = spy(new RichardBot(10, deck, view));
        charactersList = new ArrayList<>(List.of(
                new Thief(),
                new Assassin(),
                new Magician(),
                new King(),
                new Bishop(),
                new Merchant(),
                new Architect(),
                new Condottiere()
        ));
        opponentWithEmptyHand = spy(new RandomBot(10, deck, view));
        opponentWithMoreGoldThanRichard = spy(new RandomBot(10, deck, view));
        opponentWithSevenDistrictsInCitadel=spy(new RandomBot(10,deck, view));
        opponentWithSevenDistrictsInCitadel.setPositionInDrawToPickACharacter(2);
        when(opponentWithSevenDistrictsInCitadel.getHandSize()).thenReturn(7);
        opponentsList = new ArrayList<>();
        opponentsList.add(opponentWithEmptyHand);
        opponentsList.add(opponentWithMoreGoldThanRichard);
        when(opponentWithEmptyHand.getHandSize()).thenReturn(0);
        when(opponentWithMoreGoldThanRichard.getHandSize()).thenReturn(3);
        when(richardBot.getOpponents()).thenReturn(opponentsList);
    }

    @Test
    void ordinateCharactersMethodTest() {
        List<Character> orderedCharacters = richardBot.ordinateCharacters(charactersList);
        assertEquals(new Assassin(), orderedCharacters.get(0));
        assertEquals(new Magician(), orderedCharacters.get(1));
        assertEquals(new Merchant(), orderedCharacters.get(2));
        assertEquals(new Architect(), orderedCharacters.get(3));
        assertEquals(new Bishop(), orderedCharacters.get(4));
        assertEquals(new Condottiere(), orderedCharacters.get(5));
    }

    @Test
    void numberOfEmptyHandsTest() {
        assertEquals(1, richardBot.numberOfEmptyHands(opponentsList));
    }

    @Test
    void numberOfPlayerWithMoreGoldTestTrue() {
        richardBot.decreaseGold(5);
        assertTrue(richardBot.numberOfPlayerWithMoreGold(opponentsList));
    }

    @Test
    void numberOfPlayerWithMoreGoldTestFalse() {
        richardBot.increaseGold(3);
        assertFalse(richardBot.numberOfPlayerWithMoreGold(opponentsList));
    }


    @Test
    void shouldChooseAssassinTestTrue() {
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
    void shouldChooseAssassinTestFalseWhenNoHandsAreEmpty() {
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
    void shouldChooseAssassinTestFalseWhenRichardBotHandHasNotALotOfCards() {
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.BARRACK))));
        opponentsList.remove(opponentWithEmptyHand);
        assertFalse(richardBot.shouldChooseAssassin());
    }

    @Test
    void shouldChooseMagicianTestTrue() {
        when(richardBot.getHand()).thenReturn(new ArrayList<>());
        assertTrue(richardBot.shouldChooseMagician());
    }

    @Test
    void shouldChooseMagicianTestFalse() {
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
    void shouldChooseMerchantTestTrue() {
        richardBot.decreaseGold(10);
        assertTrue(richardBot.shouldChooseMerchant());
    }

    @Test
    void shouldChooseMerchantTestFalse() {
        assertFalse(richardBot.shouldChooseMerchant());
    }

    @Test
    void shouldChooseArchitectTestTrue() {
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
    void shouldChooseArchitectTestFalseBecauseNoPlayersHaveMoreGoldThanRichardBot() {
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
    void shouldChooseArchitectTestFalseBecauseRichardCantAffordMoreThanOneCard() {
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.PORT),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        richardBot.decreaseGold(9);
        assertFalse(richardBot.shouldChooseArchitect());
    }

    @Test
    void shouldChooseBishopTestTrue() {
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
    void shouldChooseBishopTestFalse() {
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        richardBot.decreaseGold(9);
        assertFalse(richardBot.shouldChooseBishop());
    }

    @Test
    void shouldChooseCondottiereTestTrue() {
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        richardBot.decreaseGold(9);
        assertTrue(richardBot.shouldChooseCondottiere());
    }

    @Test
    void shouldChooseCondottiereTestFalse() {
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
    void shouldChooseBecauseLastCardToBuyTestTrue() {
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        assertEquals(Optional.of(new Assassin()), richardBot.shouldChooseBecauseLastCardToBuy(charactersList));
    }

    @Test
    void shouldChooseBecauseLastCardToBuyTestFalse() {
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        assertEquals(Optional.empty(), richardBot.shouldChooseBecauseLastCardToBuy(charactersList));
    }


    @Test
    void chooseCharacterImplWhenShouldChooseBecauseLastCardToBuy() {
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.PORT_FOR_DRAGONS),
                new Card(District.BARRACK))));
        opponentsList.remove(opponentWithEmptyHand);
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot, times(1)).shouldChooseBecauseLastCardToBuy(any());
        verify(richardBot, times(0)).shouldChooseAssassin();
    }

    @Test
    void chooseCharacterAndShouldChooseAssassinTest() {
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
        verify(richardBot, times(1)).shouldChooseAssassin();
        assertEquals(new Assassin(), richardBot.chooseCharacterImpl(charactersList));
    }

    @Test
    void chooseCharacterAndShouldChooseMagicianTest() {
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE))));
        when(richardBot.getHand()).thenReturn(new ArrayList<>());
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot, times(1)).shouldChooseMagician();
    }

    @Test
    void chooseCharacterAndShouldChooseMerchantTest() {
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE))));
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.PORT))));
        richardBot.decreaseGold(9);
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot, times(1)).shouldChooseMerchant();
    }

    @Test
    void chooseCharacterAndShouldChooseArchitectTest() {
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.TAVERN),
                new Card(District.PORT),
                new Card(District.CASTLE),
                new Card(District.FORTRESS),
                new Card(District.BARRACK))));
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY))));
        richardBot.decreaseGold(2);
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot, times(1)).shouldChooseArchitect();
    }

    @Test
    void chooseCharacterAndShouldChooseBishopTest() {
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY))));
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY))));
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot, times(1)).shouldChooseBishop();
    }

    @Test
    void chooseCharacterAndShouldChooseBCondottiereTest() {
        when(richardBot.getHand()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY),
                new Card(District.PORT_FOR_DRAGONS))));
        when(richardBot.getCitadel()).thenReturn(new ArrayList<>(List.of(new Card(District.LIBRARY))));
        richardBot.decreaseGold(8);
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot, times(1)).shouldChooseCondottiere();
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
    void chooseThiefTargetWhenNoOpponentIsAboutToWinShouldCallSuperMethod() {
        List<Character> availableCharacters = List.of(new Thief(), new Bishop(), new King(), new Condottiere());
        when(richardBot.getAvailableCharacters()).thenReturn(availableCharacters);
        when(richardBot.anOpponentIsAboutToWin()).thenReturn(false);

        richardBot.chooseThiefTarget();

        verify(richardBot).useSuperChoseThiefEffect();
    }

    @Test
    void chooseThiefTargetWhenOpponentIsAboutToWinAndNoBishopOrCondottiereShouldCallSuperMethod() {
        List<Character> availableCharacters = List.of(new Architect(), new King(), new Thief(), new Merchant());
        when(richardBot.getAvailableCharacters()).thenReturn(availableCharacters);
        when(richardBot.anOpponentIsAboutToWin()).thenReturn(true);

        richardBot.chooseThiefTarget();

        verify(richardBot).useSuperChoseThiefEffect();
    }

    @Test
    void chooseThiefTargetWhenOpponentIsAboutToWinAndBishopAvailableShouldReturnBishop() {
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
    void chooseThiefTargetWhenOpponentIsAboutToWinAndCondottiereAvailableShouldReturnCondottiere() {
        List<Character> availableCharacters = List.of(new Architect(), new King(), new Condottiere(), new Merchant());
        when(richardBot.getAvailableCharacters()).thenReturn(availableCharacters);
        when(richardBot.anOpponentIsAboutToWin()).thenReturn(true);

        Optional<Character> result = richardBot.chooseThiefTarget();

        Character expectedCharacter = new Condottiere();
        assertTrue(result.isPresent());
        verify(richardBot, never()).useSuperChoseThiefEffect();
        assertEquals(expectedCharacter, result.get());
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
        when(richardBot.thinkCondottiereHasBeenChosenByTheLeadingOpponent()).thenReturn(true); // Simuler une condition pour choisir le condottiere
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
        when(richardBot.thinkCondottiereHasBeenChosenByTheLeadingOpponent()).thenReturn(false); // Simuler une condition pour choisir le condottiere
        // Exécution de la méthode à tester
        Character target = richardBot.chooseAssassinTarget();

        // Vérification que le personnage ciblé est le condottiere, sous condition spécifique
        assertEquals(Role.KING, target.getRole(), "The target should be the King under no specific conditions.");
    }

    @Test
    void shouldPreventWealth() {
        Player opponent = spy(new SmartBot(0, deck, view));
        opponent.increaseGold(8);
        when(richardBot.getOpponents()).thenReturn(List.of(opponent));
        boolean shouldPreventWealth = richardBot.shouldPreventWealth();
        assertTrue(shouldPreventWealth, "The bot should prevent wealth if an opponent has 7 or more gold.");
    }

    @Test
    void shouldNotPreventWealth() {
        Player opponent = spy(new SmartBot(6, deck, view));
        when(richardBot.getOpponents()).thenReturn(List.of(opponent));
        boolean shouldPreventWealth = richardBot.shouldPreventWealth();
        assertFalse(shouldPreventWealth, "The bot should not prevent wealth if no opponent has 7 or more gold.");
    }

    @Test
    void thinkCondottiereHasBeenChosenByTheLeadingOpponent() {
        Player opponent = spy(new SmartBot(10, deck, view));
        opponent.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));
        when(richardBot.getOpponents()).thenReturn(List.of(opponent));
        when(opponent.isAboutToWin()).thenReturn(true);
        boolean thinkCondottiereWillBeChosenByTheLeadingOpponent = richardBot.thinkCondottiereHasBeenChosenByTheLeadingOpponent();
        assertTrue(thinkCondottiereWillBeChosenByTheLeadingOpponent, "The bot should think the Condottiere will be chosen by the leading opponent if he is about to win.");
    }

    @Test
    void thinkCondottiereHasNotBeenChosenByTheLeadingOpponent() {
        Player opponent = spy(new SmartBot(10, deck, view));
        opponent.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));
        when(richardBot.getOpponents()).thenReturn(List.of(opponent));
        when(opponent.isAboutToWin()).thenReturn(false);
        boolean thinkCondottiereWillBeChosenByTheLeadingOpponent = richardBot.thinkCondottiereHasBeenChosenByTheLeadingOpponent();
        assertFalse(thinkCondottiereWillBeChosenByTheLeadingOpponent, "The bot should think the Condottiere will not be chosen by the leading opponent if he is not about to win.");
    }

    @Test
    void thinkThiefHasBeenChosenByTheLeadingOpponentWhenThiefHasBeenSeen() {
        Player opponentThief = spy(new SmartBot(10, deck, view));
        opponentThief.chooseCharacter(new ArrayList<>(List.of(new Thief())));
        Player opponentCondottiere = spy(new SmartBot(10, deck, view));
        opponentCondottiere.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));
        when(richardBot.getOpponents()).thenReturn(List.of(opponentThief, opponentCondottiere));
        when(opponentThief.isAboutToWin()).thenReturn(true);
        when(richardBot.getOpponentsWhichHasChosenCharacterBefore()).thenReturn(List.of(opponentCondottiere));
        when(richardBot.getCharactersSeenInRound()).thenReturn(List.of(new Thief()));
        when(richardBot.getCharactersNotInRound()).thenReturn(List.of());
        boolean thinkThiefWillBeChosenByTheLeadingOpponent = richardBot.thinkThiefHasBeenChosenByTheLeadingOpponent();
        assertTrue(thinkThiefWillBeChosenByTheLeadingOpponent, "The bot should think the Thief has been chosen by the leading opponent if he is about to win.");
    }

    @Test
    void thinkThiefHasBeenChosenByTheLeadingOpponentWhenThiefHasntBeenSeen() {
        Player opponentThief = spy(new SmartBot(10, deck, view));
        opponentThief.chooseCharacter(new ArrayList<>(List.of(new Thief())));
        Player opponentCondottiere = spy(new SmartBot(10, deck, view));
        opponentCondottiere.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));
        when(richardBot.getOpponents()).thenReturn(List.of(opponentThief, opponentCondottiere));
        when(opponentThief.isAboutToWin()).thenReturn(true);
        when(richardBot.getOpponentsWhichHasChosenCharacterBefore()).thenReturn(List.of(opponentThief));
        when(richardBot.getCharactersSeenInRound()).thenReturn(List.of(new Condottiere()));
        when(richardBot.getCharactersNotInRound()).thenReturn(List.of());
        boolean thinkThiefWillBeChosenByTheLeadingOpponent = richardBot.thinkThiefHasBeenChosenByTheLeadingOpponent();
        assertTrue(thinkThiefWillBeChosenByTheLeadingOpponent, "The bot should think the Thief has been chosen by the leading opponent if he is about to win.");
    }

    @Test
    void thinkThiefHasBeenChosenByTheLeadingOpponentWhenThiefIsNotInRound() {
        Player opponentThief = spy(new SmartBot(10, deck, view));
        opponentThief.chooseCharacter(new ArrayList<>(List.of(new Thief())));
        Player opponentCondottiere = spy(new SmartBot(10, deck, view));
        opponentCondottiere.chooseCharacter(new ArrayList<>(List.of(new Condottiere())));
        when(richardBot.getOpponents()).thenReturn(List.of(opponentThief, opponentCondottiere));
        when(opponentThief.isAboutToWin()).thenReturn(true);
        when(richardBot.getOpponentsWhichHasChosenCharacterBefore()).thenReturn(List.of(opponentThief));
        when(richardBot.getCharactersSeenInRound()).thenReturn(List.of());
        when(richardBot.getCharactersNotInRound()).thenReturn(List.of(new Thief()));
        boolean thinkThiefWillBeChosenByTheLeadingOpponent = richardBot.thinkThiefHasBeenChosenByTheLeadingOpponent();
        assertFalse(thinkThiefWillBeChosenByTheLeadingOpponent, "The bot should think the Thief has not been chose because thief is not in round.");
    }

    @Test
    void useEffectMagicianWhenLeadingOpponentIsAboutToWin() {
        Player opponent = spy(new SmartBot(10, deck, view));
        Player opponent2 = spy(new SmartBot(10, deck, view));
        when(richardBot.getOpponents()).thenReturn(List.of(opponent, opponent2));
        Magician magician = new Magician();
        richardBot.chooseCharacter(new ArrayList<>(List.of(magician)));
        Card monasteryCard = new Card(District.MONASTERY);
        Card cathedralCard = new Card(District.CATHEDRAL);
        List<Card> richardBotHand = new ArrayList<>(List.of(monasteryCard, cathedralCard));
        when(richardBot.getHand()).thenReturn(richardBotHand);
        Card templeCard = new Card(District.TEMPLE);
        Card churchCard = new Card(District.CHURCH);
        Card portCard = new Card(District.PORT);
        when(opponent.getHand()).thenReturn(new ArrayList<>(List.of(templeCard)));
        when(opponent2.getHand()).thenReturn(new ArrayList<>(List.of(templeCard, churchCard)));

        when(opponent.getCitadel()).thenReturn(new ArrayList<>(
                List.of(templeCard,
                        churchCard,
                        portCard,
                        new Card(District.CASTLE),
                        new Card(District.CASTLE),
                        new Card(District.CASTLE),
                        new Card(District.CASTLE)
                )));
        richardBot.getCharacter().applyEffect();
        verify(opponent).switchHandWith(richardBot);
        assertTrue(richardBot.getHand().contains(templeCard));
        assertFalse(richardBot.getHand().contains(churchCard)); // check that we didn't switch with opponent2
        assertTrue(opponent.getHand().contains(monasteryCard));
        assertTrue(opponent.getHand().contains(cathedralCard));
    }

    @Test
    void useEffectMagicianWhenLeadingOpponentIsNotAboutToWin() {
        Player opponent = spy(new SmartBot(10, deck, view));
        Player opponent2 = spy(new SmartBot(10, deck, view));
        when(richardBot.getOpponents()).thenReturn(List.of(opponent, opponent2));
        Magician magician = new Magician();
        richardBot.chooseCharacter(new ArrayList<>(List.of(magician)));
        Card monasteryCard = new Card(District.MONASTERY);
        Card cathedralCard = new Card(District.CATHEDRAL);
        List<Card> richardBotHand = new ArrayList<>(List.of(monasteryCard, cathedralCard));
        when(richardBot.getHand()).thenReturn(richardBotHand);
        Card templeCard = new Card(District.TEMPLE);
        Card churchCard = new Card(District.CHURCH);
        Card portCard = new Card(District.PORT);
        when(opponent.getHand()).thenReturn(new ArrayList<>(List.of(templeCard)));
        when(opponent2.getHand()).thenReturn(new ArrayList<>(List.of(templeCard, churchCard)));

        when(opponent.getCitadel()).thenReturn(new ArrayList<>(
                List.of(templeCard,
                        churchCard,
                        portCard,
                        new Card(District.CASTLE),
                        new Card(District.CASTLE),
                        new Card(District.CASTLE)
                )));
        richardBot.getCharacter().applyEffect();
        verify(opponent2).switchHandWith(richardBot);
        assertTrue(richardBot.getHand().contains(templeCard));
        assertTrue(richardBot.getHand().contains(churchCard));
        assertTrue(opponent2.getHand().contains(monasteryCard));
        assertTrue(opponent2.getHand().contains(cathedralCard));
    }


    @Test
    void chooseCharacterWhenAnOpponentIsAboutToWin(){
        opponentsList.add(opponentWithSevenDistrictsInCitadel);
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot,times(1)).chooseCharacterWHenOpponentHasOneDistrictLeft(any(),any());
    }

    @Test
    void chooseCharacterWhenNoOpponentIsAboutToWin(){
        richardBot.chooseCharacterImpl(charactersList);
        verify(richardBot,never()).chooseCharacterWHenOpponentHasOneDistrictLeft(any(),any());
    }

    @Test
    void chooseCharacterWHenOpponentHasOneDistrictLeftWhenKingIsAvailable(){
        assertEquals(Optional.of(new King()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
    }

    @Test
    void chooseCharacterWHenOpponentHasOneDistrictLeftWhenKingIsNotAvailableButBishopCondottiereAssassinAvailables(){
        charactersList.remove(new King());
        richardBot.setPositionInDrawToPickACharacter(4);
        assertEquals(Optional.of(new Condottiere()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
        richardBot.setPositionInDrawToPickACharacter(0);
        assertEquals(Optional.of(new Condottiere()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
        richardBot.setPositionInDrawToPickACharacter(1);
        assertEquals(Optional.of(new Assassin()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
    }

    @Test
    void chooseCharacterWHenOpponentHasOneDistrictLeftWhenKingBishopAreNotAvailable(){
        charactersList.remove(new King());
        charactersList.remove(new Bishop());
        richardBot.setPositionInDrawToPickACharacter(4);
        assertEquals(Optional.of(new Assassin()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
        richardBot.setPositionInDrawToPickACharacter(0);
        assertEquals(Optional.of(new Assassin()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
        richardBot.setPositionInDrawToPickACharacter(1);
        assertEquals(Optional.of(new Condottiere()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));

    }

    @Test
    void chooseCharacterWHenOpponentHasOneDistrictLeftWhenKingCondottiereAreNotAvailable(){
        charactersList.remove(new King());
        charactersList.remove(new Condottiere());
        richardBot.setPositionInDrawToPickACharacter(4);
        assertEquals(Optional.of(new Assassin()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
        richardBot.setPositionInDrawToPickACharacter(0);
        assertEquals(Optional.of(new Assassin()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
        richardBot.setPositionInDrawToPickACharacter(1);
        assertEquals(Optional.of(new Magician()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
    }

    @Test
    void chooseCharacterWHenOpponentHasOneDistrictLeftWhenKingAssassinAreNotAvailable(){
        charactersList.remove(new King());
        charactersList.remove(new Assassin());
        richardBot.setPositionInDrawToPickACharacter(4);
        assertEquals(Optional.of(new Condottiere()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
        richardBot.setPositionInDrawToPickACharacter(0);
        assertEquals(Optional.of(new Condottiere()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
        richardBot.setPositionInDrawToPickACharacter(1);
        assertEquals(Optional.of(new Bishop()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
    }

    @Test
    void chooseCharacterWHenOpponentHasOneDistrictLeftAndOpponentSecondToPickCharacter(){
        opponentWithSevenDistrictsInCitadel.setPositionInDrawToPickACharacter(1);
        assertEquals(Optional.of(new Assassin()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
    }

    @Test
    void chooseCharacterWHenOpponentHasOneDistrictLeftAndOpponentFourthToPickCharacter(){
        opponentWithSevenDistrictsInCitadel.setPositionInDrawToPickACharacter(3);
        assertEquals(Optional.of(new Assassin()),richardBot.chooseCharacterWHenOpponentHasOneDistrictLeft(charactersList,opponentWithSevenDistrictsInCitadel));
    }

}