package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.logger.CustomLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static com.seinksansdoozebank.fr.view.Cli.ANSI_DEFAULT_STYLE_END;
import static com.seinksansdoozebank.fr.view.Cli.ANSI_DEFAULT_STYLE_START;
import static com.seinksansdoozebank.fr.view.TestHandler.stripAnsiCodes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CliTest {
    private Cli view;
    private Player player;
    private Handler testHandler;
    private final Card card = new Card(District.PORT);

    private static final Logger LOGGER = Logger.getLogger(CustomLogger.class.getName());

    @BeforeEach
    void setUp() {
        view = new Cli();
        player = spy(new RandomBot(10, new Deck(), view));
        testHandler = new TestHandler();
        LOGGER.addHandler(testHandler);
    }

    @AfterEach
    void tearDown() {
        LOGGER.removeHandler(testHandler);
        CustomLogger.resetAvailableColors();
    }

    /**
     * Test if the player plays a card, the view displays the right message
     */
    @Test
    void testDisplayPlayerPlaysCard() {
        view.displayPlayerPlaysCard(player, card);

        String expectedOutput = player + " pose un/e " + card.getDistrict().getName() + " qui lui coute " + card.getDistrict().getCost() + ", il lui reste " + player.getNbGold() + " pièces d'or.";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayWinner() {
        view.displayWinner(player);

        String expectedOutput = "\n" + player + " gagne avec un score de " + player.getScore() + ".";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerStartPlaying() {
        LOGGER.setLevel(Level.FINER);

        view.displayPlayerStartPlaying(player);

        String expectedOutput = "\n" + player + " commence son tour.";

        assertLogged(Level.FINE, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerPickCards() {
        int numberOfCards = 2;

        view.displayPlayerPickCards(player, numberOfCards);

        String expectedOutput = player + " pioche " + numberOfCards + " quartier(s).";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerPicksGold() {
        int numberOfGold = 3;

        view.displayPlayerPicksGold(player, numberOfGold);

        String expectedOutput = player + " pioche " + numberOfGold + " pièces d'or.";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerChooseCharacter() {
        view.displayPlayerChooseCharacter(player);

        String expectedOutput = player + " choisit un personnage.";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerRevealCharacter() {
        view.displayPlayerRevealCharacter(player);

        String expectedOutput = player + " se révèle être " + player.getCharacter() + ".";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerUseCondottiereDistrict() {
        Player defender = spy(new RandomBot(10, new Deck(), view));

        view.displayPlayerUseCondottiereDistrict(player, defender, card.getDistrict());

        String expectedOutput = player + " utilise l'effet du Condottiere pour détruire le quartier " + card.getDistrict().getName() + " de " + defender + " en payant " + (card.getDistrict().getCost() + 1) + " pièces d'or.";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerScore() {
        when(player.getScore()).thenReturn(10);
        view.displayPlayerScore(player);

        String expectedOutput = player + " fini la partie avec un score de " + player.getScore() + ".";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerGetBonus() {
        int pointsBonus = 5; // Mock bonus points
        String bonusName = "High Roller"; // Mock bonus name

        view.displayPlayerGetBonus(player, pointsBonus, bonusName);

        String expectedOutput = player + " gagne " + pointsBonus + " points bonus pour la raison " + bonusName + ".";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerUseAssassinEffect() {
        Assassin assassin = new Assassin();
        when(player.getCharacter()).thenReturn(assassin);
        King target = spy(new King());
        view.displayPlayerUseAssassinEffect(player, target);

        String expectedOutput = player + " utilise l'assassin pour tuer le " + target + ".";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerHand() {
        // Set up player's hand with mock cards
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(District.CASTLE));
        hand.add(new Card(District.TAVERN));
        when(player.getHand()).thenReturn(hand);

        view.displayPlayerHand(player);

        String expectedString = "\t- les cartes suivantes dans sa main : \n";
        StringBuilder expectedOutputBuilder = getExpectedOutputBuilder(hand, expectedString);

        assertLogged(Level.INFO, expectedOutputBuilder.toString(), false);
    }

    @Test
    void testDisplayPlayerCitadel() {
        // Set up player's citadel with mock cards
        List<Card> citadel = new ArrayList<>();
        citadel.add(new Card(District.MARKET_PLACE));
        citadel.add(new Card(District.MANOR));
        when(player.getCitadel()).thenReturn(citadel);

        view.displayPlayerCitadel(player);

        String expectedString = "\t- les quartiers suivants dans sa citadelle : \n";
        StringBuilder expectedOutputBuilder = getExpectedOutputBuilder(citadel, expectedString);

        assertLogged(Level.INFO, expectedOutputBuilder.toString(), false);
    }

    @Test
    void testDisplayPlayerInfo() {
        // Set up mock behavior for player
        when(player.getNbGold()).thenReturn(10);
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(District.CASTLE));
        hand.add(new Card(District.TAVERN));
        List<Card> citadel = new ArrayList<>();
        citadel.add(new Card(District.MARKET_PLACE));
        citadel.add(new Card(District.MANOR));
        when(player.getHand()).thenReturn(hand);
        when(player.getCitadel()).thenReturn(citadel);

        // Invoke method under test
        view.displayPlayerInfo(player);

        // Build expected output
        StringBuilder expectedOutputBuilder = new StringBuilder();
        expectedOutputBuilder.append(player).append(" possède : \n");
        expectedOutputBuilder.append("\t- ").append(player.getNbGold()).append(" pièces d'or.").append("\n");
        expectedOutputBuilder.append("\t- les cartes suivantes dans sa main : \n");
        for (Card card : hand) {
            expectedOutputBuilder.append("\t\t- ").append(card).append("\n");
        }
        expectedOutputBuilder.append("\t- les quartiers suivants dans sa citadelle : \n");
        for (Card card : citadel) {
            expectedOutputBuilder.append("\t\t- ").append(card).append("\n");
        }
        String expectedOutput = expectedOutputBuilder.toString();

        // Assert
        assertMultipleLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayUnusedCharacterInRound() {
        Bishop character = new Bishop(); // Mock character
        view.displayUnusedCharacterInRound(character);

        String expectedOutput = ANSI_DEFAULT_STYLE_START + "Le personnage " + character + " a été écarté pour cette manche." + ANSI_DEFAULT_STYLE_END;

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayGameFinished() {
        view.displayGameFinished();

        String expectedOutput = "\n\n### La partie est terminée ! ###";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayRound() {
        int roundNumber = 3; // Mock round number
        view.displayRound(roundNumber);

        String expectedOutput = "\n\n########## Début du round " + roundNumber + " ##########";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerError() {
        String errorMessage = "Invalid move"; // Mock error message
        view.displayPlayerError(player, errorMessage);

        String expectedOutput = player + " : " + errorMessage;

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerStrategy() {
        LOGGER.setLevel(Level.FINER);
        String strategyMessage = "Building strong citadel"; // Mock strategy message
        String expectedOutput = player + " : " + strategyMessage;

        view.displayPlayerStrategy(player, strategyMessage);

        assertLogged(Level.FINE, expectedOutput, false);
    }

    @Test
    void testDisplayStolenCharacter() {
        Bishop stolenCharacter = spy(new Bishop());
        when(stolenCharacter.getPlayer()).thenReturn(player);
        view.displayStolenCharacter(stolenCharacter);

        String expectedOutput = "Le " + stolenCharacter + " a été volé et perd " + stolenCharacter.getPlayer().getNbGold() + " pièces d'or ";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayActualNumberOfGold() {
        Condottiere condottiere = new Condottiere();
        when(player.getCharacter()).thenReturn(condottiere);
        when(player.getNbGold()).thenReturn(10);
        view.displayActualNumberOfGold(player);

        String expectedOutput = "Le " + player.getCharacter() + " a maintenant " + player.getNbGold() + " pièces d'or.";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerUseMagicianEffect() {
        Player targetPlayer = spy(new RandomBot(10, new Deck(), view));
        view.displayPlayerUseMagicianEffect(player, targetPlayer);

        String expectedOutput = "Le " + player + " utilise le magicien pour échanger sa main avec celle du " + targetPlayer + ".";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerHasGotObservatory() {
        view.displayPlayerHasGotObservatory(player);

        String expectedOutput = "Le " + player + " possède le district Observatoire il peut donc choisir parmi 3 cartes celle qui garde dans sa main.";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerUseThiefEffect() {
        view.displayPlayerUseThiefEffect(player);

        String expectedOutput = player + " vient de choisir le personnage qu'il volera.";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerDiscardCard() {
        Card card = new Card(District.TAVERN); // Mock card
        view.displayPlayerDiscardCard(player, card);

        String expectedOutput = player + " défausse " + card + ".";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerUseLaboratoryEffect() {
        view.displayPlayerUseLaboratoryEffect(player);

        String expectedOutput = player + " utilise le laboratoire pour défausser une carte et gagner une pièce d'or (s'il en reste dans la banque).";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayPlayerUseManufactureEffect() {
        view.displayPlayerUseManufactureEffect(player);

        String expectedOutput = player + " utilise la manufacture pour piocher 3 cartes (en perdant 3 pièces).";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayGoldCollectedFromDistrictType() {
        int nbGold = 2; // Mock gold amount
        Merchant merchant = spy(new Merchant());
        when(player.getCharacter()).thenReturn(merchant);
        view.displayGoldCollectedFromDistrictType(player, nbGold, card.getDistrict().getDistrictType());

        String expectedOutput = player + " gagne " + nbGold + " pièces d'or grâce à ses quartiers de type " + card.getDistrict().getDistrictType() + " et l'effet du " + player.getCharacter() + ".";

        assertLogged(Level.INFO, expectedOutput, false);
    }

    @Test
    void testDisplayGameStuck() {
        view.displayGameStuck();

        String expectedOutput = "### La partie semble bloquée, le calcul des points et des bonus va quand même être fait ###";

        assertLogged(Level.INFO, expectedOutput, false);
    }


    /**
     * Assert that the last log record is the expected one
     *
     * @param expectedLevel        the expected level
     * @param expectedMessageRegex the expected message regex
     * @param regex                true if the message is a regex pattern, false otherwise
     */
    private void assertLogged(Level expectedLevel, String expectedMessageRegex, boolean regex) {
        LogRecord lastLogRecord = ((TestHandler) testHandler).getLastLogRecord();

        this.assertLoggedLevel(expectedLevel);

        String logRecordMessage = lastLogRecord.getMessage();
        logRecordMessage = stripAnsiCodes(logRecordMessage);
        logRecordMessage = removeDoubleSinglesQuotes(new StringBuilder(logRecordMessage)).toString();
        expectedMessageRegex = stripAnsiCodes(expectedMessageRegex);
        expectedMessageRegex = removeDoubleSinglesQuotes(new StringBuilder(expectedMessageRegex)).toString();
        if (regex) {
            assertTrue(logRecordMessage.matches(expectedMessageRegex));
        } else {
            assertEquals(expectedMessageRegex, logRecordMessage);
        }
        ((TestHandler) testHandler).cleanLogRecords();
    }

    /**
     * Assert that the last logs records are the expected one
     *
     * @param expectedLevel        the expected level
     * @param expectedMessageRegex the expected message regex
     * @param regex                true if the message is a regex pattern, false otherwise
     */
    private void assertMultipleLogged(Level expectedLevel, String expectedMessageRegex, boolean regex) {
        this.assertLoggedLevel(expectedLevel);
        String allLogRecordsAsString = ((TestHandler) testHandler).getAllLogRecordsAsString();

        allLogRecordsAsString = stripAnsiCodes(allLogRecordsAsString);
        expectedMessageRegex = stripAnsiCodes(allLogRecordsAsString);
        expectedMessageRegex = removeDoubleSinglesQuotes(new StringBuilder(expectedMessageRegex)).toString();
        allLogRecordsAsString = removeDoubleSinglesQuotes(new StringBuilder(allLogRecordsAsString)).toString();

        if (regex) {
            assertTrue(allLogRecordsAsString.matches(expectedMessageRegex));
        } else {
            assertEquals(expectedMessageRegex, allLogRecordsAsString);
        }
        ((TestHandler) testHandler).cleanLogRecords();
    }

    private void assertLoggedLevel(Level expectedLevel) {
        LogRecord lastLogRecord = ((TestHandler) testHandler).getLastLogRecord();
        assertEquals(expectedLevel, lastLogRecord.getLevel());
    }

    private StringBuilder removeDoubleSinglesQuotes(StringBuilder sb) {
        return new StringBuilder(sb.toString().replaceAll("''", "'"));
    }

    private StringBuilder getExpectedOutputBuilder(List<Card> cards, String expectedString) {
        // Build expected output
        StringBuilder expectedOutputBuilder = new StringBuilder();
        expectedOutputBuilder.append(expectedString);
        for (Card card : cards) {
            expectedOutputBuilder.append("\t\t- ").append(card).append("\n");
        }
        // remove last \n
        expectedOutputBuilder.deleteCharAt(expectedOutputBuilder.length() - 1);
        // replace all '' by '
        expectedOutputBuilder = removeDoubleSinglesQuotes(expectedOutputBuilder);
        return expectedOutputBuilder;
    }
}
