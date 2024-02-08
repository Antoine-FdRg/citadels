package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Warlord;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.SmartBot;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class GameTest {

    Game gameWithFivePlayers;
    Game gameWithThreePlayers;
    Game gameWithFourPlayers;
    Game gameWithSixPlayers;
    Game gameWithPlayerThatHasCourtyardOfMiracleAndPlacedItInTheLastPosition;
    Player playerWIthEightDistrictsAndFiveDistrictTypes;
    Player playerWithNoBonus;
    Player playerWithEightDistricts;
    private Cli view;
    private Bank fourPlayersGameBank;
    List<Character> charactersList;

    @BeforeEach
    public void setUp() {
        view = mock(Cli.class);
        gameWithFivePlayers = spy(GameFactory.createGameOfRandomBot(view, new Bank(), 5));

        Bank threePlayersGameBank = new Bank();
        gameWithThreePlayers = GameFactory.createGameOfRandomBot(view, threePlayersGameBank, 4);
        //Set player 1 with eight districts in its citadel and five different districtTypes
        playerWIthEightDistrictsAndFiveDistrictTypes = spy(new RandomBot(5, new Deck(), view, threePlayersGameBank));
        List<Card> citadelWithEightDistrictsAndFiveDistrictTypes = new ArrayList<>(List.
                of(new Card(District.PORT),
                        new Card(District.PALACE),
                        new Card(District.TEMPLE),
                        new Card(District.FORTRESS),
                        new Card(District.MARKET_PLACE),
                        new Card(District.CEMETERY),
                        new Card(District.MANUFACTURE),
                        new Card(District.CASTLE)));
        when(playerWIthEightDistrictsAndFiveDistrictTypes.getCitadel()).
                thenReturn(citadelWithEightDistrictsAndFiveDistrictTypes);
        //Set player 2 with only two districts in its citadel
        playerWithNoBonus = spy(new RandomBot(5, new Deck(), view, threePlayersGameBank));
        List<Card> citadelWithNoBonusAssociated = new ArrayList<>(List.
                of(new Card(District.PORT),
                        new Card(District.PALACE)));
        when(playerWithNoBonus.getCitadel()).thenReturn(citadelWithNoBonusAssociated);
        //Set player 3 with eight district in its citadel and with less than 5 different districtTypes
        playerWithEightDistricts = spy(new RandomBot(5, new Deck(), view, threePlayersGameBank));

        List<Card> citadelWithEightDistricts = new ArrayList<>(List.
                of(new Card(District.TEMPLE),
                        new Card(District.CHURCH),
                        new Card(District.MONASTERY),
                        new Card(District.CATHEDRAL),
                        new Card(District.MANOR),
                        new Card(District.CASTLE),
                        new Card(District.PALACE),
                        new Card(District.TAVERN)));

        when(playerWithEightDistricts.getCitadel()).thenReturn(citadelWithEightDistricts);
        gameWithThreePlayers.setPlayers(List.of(playerWIthEightDistrictsAndFiveDistrictTypes, playerWithNoBonus, playerWithEightDistricts));

        fourPlayersGameBank = mock(Bank.class);
        gameWithFourPlayers = spy(GameFactory.createGameOfRandomBot(view, fourPlayersGameBank, 4));
        gameWithSixPlayers = spy(GameFactory.createGameOfRandomBot(view, mock(Bank.class), 6));

        charactersList = List.of(
                new Assassin(),
                new Thief(),
                new Magician(),
                new King(),
                new Bishop(),
                new Merchant(),
                new Architect(),
                new Warlord()
        );
    }

    @Test
    void getWinnerIsP3WithTheMostPoint() {
        Player p1 = mock(Player.class);
        when(p1.getScore()).thenReturn(5);
        when(p1.getIsFirstToHaveAllDistricts()).thenReturn(true);
        Player p2 = mock(Player.class);
        when(p2.getScore()).thenReturn(4);
        Player p3 = mock(Player.class);
        when(p3.getScore()).thenReturn(7);
        Player p4 = mock(Player.class);
        when(p4.getScore()).thenReturn(6);
        gameWithFivePlayers.setPlayers(
                new ArrayList<>(
                        List.of(p1, p2, p3, p4)
                )
        );
        assertEquals(p3, gameWithFivePlayers.getWinner());
    }

    @Test
    void playersChoseCharactersMakeAllPlayersChooseADifferentCharacter() {
        List<Player> players = gameWithFivePlayers.players;
        gameWithFivePlayers.getAvailableCharacters().addAll(charactersList);

        gameWithFivePlayers.playersChooseCharacters();

        for (Player player : players) {
            assertNotNull(player.getCharacter());
            for (Player player2 : players) {
                if (player != player2) {
                    assertNotEquals(player.getCharacter(), player2.getCharacter());
                }
            }
        }
    }

    @Test
    void retrieveCharactersRetrieveAllCharacters() {
        // Test the playersChooseCharacters method
        int nbCharactersInitial = charactersList.size();
        List<Player> players = gameWithFivePlayers.players;
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            Character currentCharacter = charactersList.get(i);
            currentPlayer.chooseCharacter(new ArrayList<>(List.of(currentCharacter)));
            currentPlayer.reveal();
        }

        // Reset the available characters list
        gameWithFivePlayers.retrieveCharacters();

        // Check if the available characters list is equal to charactersList
        assertEquals(nbCharactersInitial, charactersList.size());
    }


    /**
     * This test verify the method isFirstToHaveEightDistrict and verify the bonus associated to
     */
    @Test
    void isFirstBotToHaveEightDistrictsTest() {
        //playerWithEightDistrictsAndFiveDistrictTypes is the first who has eight districts in that game so +7 in bonus
        gameWithThreePlayers.isTheFirstOneToHaveAllDistricts(playerWIthEightDistrictsAndFiveDistrictTypes);
        //playerWithEightDistricts is not the first who has eight districts +2 in bonus
        gameWithThreePlayers.isTheFirstOneToHaveAllDistricts(playerWithEightDistricts);
        gameWithThreePlayers.updatePlayersBonus();

        assertEquals(7, playerWIthEightDistrictsAndFiveDistrictTypes.getBonus());
        assertEquals(2, playerWithEightDistricts.getBonus());
    }

    /**
     * This test verifies the method hasFiveDifferentDistrictTypes on a player who has got five different types of district
     */
    @Test
    void hasDifferentDistrictTypeTestWithPlayerHavingFiveTypesShouldReturnTrue() {
        //botWithFiveDifferentDistrictType
        gameWithThreePlayers.updatePlayersBonus();
        assertTrue(playerWIthEightDistrictsAndFiveDistrictTypes.hasFiveDifferentDistrictTypes());
    }

    /**
     * This test verifies the method hasFiveDifferentDistrictTypes on a player who has got five different types of district
     */
    @Test
    void hasDifferentDistrictTypeWithPlayerNotHavingFiveTypesShouldFalse() {
        gameWithThreePlayers.updatePlayersBonus();
        assertFalse(playerWithNoBonus.hasFiveDifferentDistrictTypes());
    }

    /**
     * this test verifies the method findBonusPlayer when the player has got eight district in his citadel and verifies the bonus associated to
     */
    @Test
    void botWithEightDistrictInItCitadelTest() {
        gameWithThreePlayers.updatePlayersBonus();
        assertEquals(2, playerWithEightDistricts.getBonus());
    }

    @Test
    void orderPlayerBeforePlaying() {
        gameWithFourPlayers.createCharacters();
        List<Character> availableCharacters = new ArrayList<>(gameWithFourPlayers.getAvailableCharacters());
        availableCharacters.sort(Comparator.comparing(Character::getRole));
        gameWithFourPlayers.playersChooseCharacters();
        availableCharacters.removeAll(gameWithFourPlayers.getAvailableCharacters());
        gameWithFourPlayers.orderPlayerBeforePlaying();
        int size = availableCharacters.size();
        assertEquals(size, gameWithFourPlayers.players.size());
        for (int i = 0; i < size; i++) {
            assertEquals(gameWithFourPlayers.players.get(i).getCharacter(), availableCharacters.get(i));
        }
    }

    @Test
    void runGameShouldHave65CardsBeforeAndAfter() {
        int nbCardInGame = gameWithFourPlayers.deck.getDeck().size();
        for (Player player : gameWithFourPlayers.players) {
            nbCardInGame += player.getHand().size();
            nbCardInGame += player.getCitadel().size();
        }
        assertEquals(65, nbCardInGame);
        gameWithFourPlayers.run();
        nbCardInGame = gameWithFourPlayers.deck.getDeck().size();
        for (Player player : gameWithFourPlayers.players) {
            nbCardInGame += player.getHand().size();
            nbCardInGame += player.getCitadel().size();
        }
        assertEquals(65, nbCardInGame);
    }

    @Test
    void runGameAndCheckThatNoCardAreDuplicated() {
        gameWithFourPlayers.run();
        Set<Card> allCards = new HashSet<>(gameWithFourPlayers.deck.getDeck());
        for (Player player : gameWithFourPlayers.players) {
            allCards.addAll(player.getHand());
            allCards.addAll(player.getCitadel());
        }
        assertEquals(65, allCards.size());
    }

    @Test
    void runGame() {
        gameWithFourPlayers.run();
        verify(gameWithFourPlayers, times(1)).init();
        int nbRoundPlayed = gameWithFourPlayers.getNbCurrentRound() - 1;
        verify(gameWithFourPlayers, times(nbRoundPlayed)).createCharacters();
        verify(view, times(nbRoundPlayed)).displayRound(anyInt());
        verify(gameWithFourPlayers, times(nbRoundPlayed)).playARound();
        verify(gameWithFourPlayers, atMost(nbRoundPlayed + 1)).isStuck();
        verify(view, atMost(1)).displayGameFinished();
        verify(view, atMost(1)).displayGameStuck();
        verify(gameWithFourPlayers, times(1)).updatePlayersBonus();
        verify(view, times(1)).displayWinner(any(Player.class));
    }

    @Test
    void runAStuckGameShouldDisplayStuckGame() {
        when(gameWithFourPlayers.isStuck()).thenReturn(true);
        gameWithFourPlayers.run();
        verify(gameWithFourPlayers, times(1)).init();
        verify(view, times(0)).displayGameFinished();
        verify(view, times(1)).displayGameStuck();
        verify(gameWithFourPlayers, times(1)).updatePlayersBonus();
        verify(view, times(1)).displayWinner(any(Player.class));
    }

    @Test
    void playARound() {
        gameWithFourPlayers.createCharacters();
        gameWithFourPlayers.playARound();
        verify(gameWithFourPlayers, times(1)).orderPlayerBeforeChoosingCharacter();
        verify(gameWithFourPlayers, times(1)).playersChooseCharacters();
        verify(gameWithFourPlayers, times(1)).orderPlayerBeforePlaying();
        verify(gameWithFourPlayers, atMost(gameWithFourPlayers.players.size())).isTheFirstOneToHaveAllDistricts(any(Player.class));
        verify(gameWithFourPlayers, atLeast(gameWithFourPlayers.players.size() - 1)).isTheFirstOneToHaveAllDistricts(any(Player.class));
        verify(gameWithFourPlayers, times(1)).retrieveCharacters();
        List<Player> players = gameWithFourPlayers.players;
        for (Player player : players) {
            assertNull(player.getCharacter());
        }
    }


    private Player getPlayerWithCourtyard() {
        Player playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition = spy(new SmartBot(5, new Deck(), view, mock(Bank.class)));
        ArrayList<Card> citadelWithFourDifferentDistrictAndTheCourtyardOfMiraclePlaceInTheLastPosition = new ArrayList<>(
                List.of(new Card(District.TEMPLE),
                        new Card(District.MANOR),
                        new Card(District.TAVERN),
                        new Card(District.CEMETERY),
                        new Card(District.COURTYARD_OF_MIRACLE))
        );
        when(playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition.getCitadel()).thenReturn(citadelWithFourDifferentDistrictAndTheCourtyardOfMiraclePlaceInTheLastPosition);
        return playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition;
    }

    @Test
    void testThatThePlayerWithFourDifferentDistrictAndTheCourtyardOfMiracleDontGetTheBonusBecauseHePlacedTheCourtyardOfMiracleInTheLastPosition() {
        gameWithPlayerThatHasCourtyardOfMiracleAndPlacedItInTheLastPosition = GameFactory.createGameOfRandomBot(view, mock(Bank.class), 4);
        Player playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition = getPlayerWithCourtyard();
        playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition.setLastCardPlacedCourtyardOfMiracle(true);
        gameWithPlayerThatHasCourtyardOfMiracleAndPlacedItInTheLastPosition.setPlayers(List.of(playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition));
        // Update the bonus of all players
        gameWithPlayerThatHasCourtyardOfMiracleAndPlacedItInTheLastPosition.updatePlayersBonus();
        // Check that the player with the courtyard of miracle get the bonus
        assertEquals(0, playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition.getBonus());
    }

    @Test
    void testHasCourtyardOfMiracleAndItsNotTheLastCardPlaced() {
        Player playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition = getPlayerWithCourtyard();
        playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition.setLastCardPlacedCourtyardOfMiracle(false);
        assertTrue(playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition.hasCourtyardOfMiracleAndItsNotTheLastCard());
    }

    @Test
    void testHasCourtyardOfMiracleAndItsTheLastCardPlaced() {
        Player playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition = getPlayerWithCourtyard();
        playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition.setLastCardPlacedCourtyardOfMiracle(true);
        assertFalse(playerWithFourDifferentDistrictAndTheCourtyardOfMiracleButPLacedInTheLastPosition.hasCourtyardOfMiracleAndItsNotTheLastCard());
    }

    @Test
    void testThatThePlayerWithFourDifferentDistrictAndTheCourtyardOfMiracleGetTheBonus() {
        Player playerWithFourDifferentDistrictAndTheCourtyardOfMiracle = spy(new SmartBot(5, new Deck(), view, fourPlayersGameBank));

        ArrayList<Card> citadelWithFourDifferentDistrictAndTheCourtyardOfMiracle = new ArrayList<>(
                List.of(new Card(District.TEMPLE),
                        new Card(District.MANOR),
                        new Card(District.TAVERN),
                        new Card(District.CEMETERY),
                        new Card(District.COURTYARD_OF_MIRACLE))
        );

        when(playerWithFourDifferentDistrictAndTheCourtyardOfMiracle.getCitadel()).thenReturn(citadelWithFourDifferentDistrictAndTheCourtyardOfMiracle);


        gameWithFourPlayers.setPlayers(List.of(playerWithFourDifferentDistrictAndTheCourtyardOfMiracle));
        // Update the bonus of all players
        gameWithFourPlayers.updatePlayersBonus();
        // Check that the player with the courtyard of miracle get the bonus
        assertEquals(3, playerWithFourDifferentDistrictAndTheCourtyardOfMiracle.getBonus());
    }

    @Test
    void crownedPlayerIsUpdatedWhilePlayingARoundANdKingIsAlive() {
        gameWithFourPlayers.getAvailableCharacters().addAll(List.of(new King(), new Bishop(), new Merchant(), new Warlord()));

        gameWithFourPlayers.playARound();

        assertEquals(gameWithFourPlayers.players.get(0), gameWithFourPlayers.crownedPlayer);
    }

    @Test
    void kingPlayerIsNotUpdatedWhilePlayingARoundANdKingIsDead() {
        King king = new King();
        Assassin assassin = new Assassin();
        assassin.useEffect(king);
        gameWithFourPlayers.getAvailableCharacters().addAll(List.of(king, new Bishop(), new Merchant(), new Warlord()));

        gameWithFourPlayers.playARound();

        verify(gameWithFourPlayers, times(gameWithFourPlayers.players.size() - 1)).updateCrownedPlayer(any(Player.class));
        assertNull(gameWithFourPlayers.crownedPlayer);
    }

    @Test
    void newGameWithTwoPlayers() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRandomBot(view, mock(Bank.class), 2));
    }

    @Test
    void newGameWithSevenPlayers() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGameOfRandomBot(view, mock(Bank.class), 7));
    }

    @Test
    void createCharactersWithFourPlayers() {
        gameWithFourPlayers.createCharacters();
        assertEquals(6, gameWithFourPlayers.getAvailableCharacters().size());
        assertTrue(gameWithFourPlayers.getAvailableCharacters().contains(new King()));
        verify(view, times(charactersList.size() - 6)).displayUnusedCharacterInRound(any(Character.class));
    }

    @Test
    void createCharactersWithFivePlayers() {
        gameWithFivePlayers.createCharacters();
        assertEquals(7, gameWithFivePlayers.getAvailableCharacters().size());
        verify(view, times(charactersList.size() - 7)).displayUnusedCharacterInRound(any(Character.class));
    }

    @Test
    void createCharactersWithSixPlayers() {
        gameWithSixPlayers.createCharacters();
        assertEquals(8, gameWithSixPlayers.getAvailableCharacters().size());
        assertTrue(gameWithSixPlayers.getAvailableCharacters().contains(new King()));
        verify(view, times(0)).displayUnusedCharacterInRound(any(Character.class));
    }

    /**
     * we verify that the method getPlayerWithRoleTest return the right Optional
     */
    @Test
    void getPlayerWithRoleTest() {
        //Création du player de type architecte
        Player playerArchitect = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        Architect architect = new Architect();
        architect.setPlayer(playerArchitect);
        when(playerArchitect.getCharacter()).thenReturn(architect);
        //Création du player de type voleur
        Player playerThief = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        Thief thief = new Thief();
        thief.setPlayer(playerThief);
        when(playerThief.getCharacter()).thenReturn(thief);

        gameWithFivePlayers.setPlayers(List.of(playerThief, playerArchitect));
        //The player architect is present in the game
        assertEquals(Optional.of(playerArchitect), gameWithFivePlayers.getPlayerByRole(Role.ARCHITECT));
        //The player merchant is not present in the game
        assertEquals(Optional.empty(), gameWithFivePlayers.getPlayerByRole(Role.MERCHANT));
    }

    /**
     * On vérifie qu'en appelant checkUniversityOrPortForDragonsInCitadel, il met les bons bonus au joueur, c'est-à-dire 2 par cartes
     */
    @Test
    void checkUniversityOrPortForDragonsInCitadelTest() {
        Player smartBotWithUniversity = spy(new SmartBot(3, new Deck(), view, fourPlayersGameBank));
        when(smartBotWithUniversity.getCitadel()).thenReturn(List.of(new Card(District.UNIVERSITY), new Card(District.PORT)));
        Player smartBotWithPortForDragons = spy(new SmartBot(3, new Deck(), view, fourPlayersGameBank));
        when(smartBotWithPortForDragons.getCitadel()).thenReturn(List.of(new Card(District.PORT_FOR_DRAGONS), new Card(District.TEMPLE)));
        Player smartBotWithNoPrestige = spy(new SmartBot(3, new Deck(), view, fourPlayersGameBank));
        when(smartBotWithNoPrestige.getCitadel()).thenReturn(List.of(new Card(District.TAVERN)));
        Player smartBotWithBothDistricts = spy(new SmartBot(3, new Deck(), view, fourPlayersGameBank));
        when(smartBotWithBothDistricts.getCitadel()).thenReturn(List.of(new Card(District.PORT_FOR_DRAGONS), new Card(District.UNIVERSITY)));
        gameWithFourPlayers.setPlayers(List.of(smartBotWithPortForDragons, smartBotWithUniversity, smartBotWithBothDistricts));

        for (Player player : gameWithFourPlayers.players) {
            gameWithFourPlayers.checkUniversityOrPortForDragonsInCitadel(player);
        }

        assertEquals(2, smartBotWithPortForDragons.getBonus());
        assertEquals(2, smartBotWithUniversity.getBonus());
        assertEquals(0, smartBotWithNoPrestige.getBonus());
        assertEquals(4, smartBotWithBothDistricts.getBonus());
        verify(view, times(4)).displayPlayerGetBonus(any(), anyInt(), anyString());

    }

    /**
     * On vérifie que l'appel à la méthode checkUniversityOrPortForDragonsInCitadel se fait systématiquement
     */
    @Test
    void checkIfUpdatePlayersBonusCallsSpellcheckUniversityOrPortForDragonsInCitadelTest() {
        gameWithFourPlayers.updatePlayersBonus();
        verify(gameWithFourPlayers, atLeast(4)).checkUniversityOrPortForDragonsInCitadel(any());
    }

    @Test
    void createCharactersWithTooMuchPlayers() {
        gameWithFourPlayers.players.add(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        gameWithFourPlayers.players.add(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        gameWithFourPlayers.players.add(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        gameWithFourPlayers.players.add(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        gameWithFourPlayers.players.add(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        assertThrows(UnsupportedOperationException.class, () -> gameWithFourPlayers.createCharacters());
    }

    @Test
    void isStuckWithStuckGameShouldReturnTrue() {
        Deck mockDeck = mock(Deck.class);
        when(mockDeck.getDeck()).thenReturn(new ArrayList<>());
        Game stuckGame = new GameBuilder(mock(IView.class), mockDeck, mock(Bank.class))
                .addRandomBot()
                .addRandomBot()
                .addRandomBot()
                .addRandomBot()
                .build();

        assertTrue(stuckGame.isStuck());
    }

    @Test
    void constructorGameWithTooMuchPlayers() {
        List<Player> players = new ArrayList<>();
        Bank bank = mock(Bank.class);
        players.add(new RandomBot(5, new Deck(), view, bank));
        players.add(new RandomBot(5, new Deck(), view, bank));
        players.add(new RandomBot(5, new Deck(), view, bank));
        players.add(new RandomBot(5, new Deck(), view, bank));
        players.add(new RandomBot(5, new Deck(), view, bank));
        players.add(new RandomBot(5, new Deck(), view, bank));
        players.add(new RandomBot(5, new Deck(), view, bank));
        players.add(new RandomBot(5, new Deck(), view, bank));
        Deck deck = new Deck();
        assertThrows(IllegalArgumentException.class, () -> new Game(view, deck, bank, players));
    }

    @Test
    void isStuckWithNotEmptyDeckShouldReturnFalse() {
        Deck mockDeck = mock(Deck.class);
        when(mockDeck.getDeck()).thenReturn(new ArrayList<>(List.of(new Card(District.TEMPLE))));
        Game stuckGame = new GameBuilder(mock(IView.class), mockDeck, mock(Bank.class))
                .addRandomBot()
                .addRandomBot()
                .addRandomBot()
                .addRandomBot()
                .build();

        assertFalse(stuckGame.isStuck());
    }

    @Test
    void isStuckWithRemainingCoinsShouldReturnFalse() {
        Bank bank = new Bank();
        Deck mockDeck = mock(Deck.class);
        when(mockDeck.getDeck()).thenReturn(new ArrayList<>());
        Game stuckGame = new GameBuilder(mock(IView.class), mockDeck, bank)
                .addRandomBot()
                .addRandomBot()
                .addRandomBot()
                .addRandomBot()
                .build();

        assertFalse(stuckGame.isStuck());
    }

    @Test
    void isStuckWithCardsInPlayersHandShouldReturnFalse() {
        Game stuckGame = new GameBuilder(mock(IView.class), new Deck(), mock(Bank.class))
                .addRandomBot()
                .addRandomBot()
                .addRandomBot()
                .addRandomBot()
                .build();
        stuckGame.init();
        assertFalse(stuckGame.isStuck());
    }

    @Test
    void testReorderPlayersByPointsWithNoTieWithSameOrder() {
        Player player1 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player1.getScore()).thenReturn(10);
        Player player2 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player2.getScore()).thenReturn(8);
        Player player3 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player3.getScore()).thenReturn(6);
        Player player4 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player4.getScore()).thenReturn(5);
        List<Player> players = List.of(player1, player2, player3, player4);
        when(gameWithFourPlayers.getPlayers()).thenReturn(
                new ArrayList<>(players)
        );
        gameWithFourPlayers.orderPlayersByPoints();
        assertEquals(players, gameWithFourPlayers.getPlayers());
    }

    @Test
    void testReorderPlayersByPointsWithNoTieWithoutSameOrder() {
        Player player1 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player1.getScore()).thenReturn(10);
        Player player2 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player2.getScore()).thenReturn(8);
        Player player3 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player3.getScore()).thenReturn(6);
        Player player4 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player4.getScore()).thenReturn(5);
        List<Player> players = List.of(player1, player2, player3, player4);
        when(gameWithFourPlayers.getPlayers()).thenReturn(
                new ArrayList<>(List.of(player4, player3, player2, player1))
        );
        gameWithFourPlayers.orderPlayersByPoints();
        assertEquals(players, gameWithFourPlayers.getPlayers());
    }

    @Test
    void testReorderPlayersByPointsWithTieInPoints() {
        Player player1 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player1.getScore()).thenReturn(10);
        when(player1.getCitadel()).thenReturn(List.of(new Card(District.MANOR), new Card(District.CEMETERY)));

        Player player2 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player2.getScore()).thenReturn(10);
        when(player2.getCitadel()).thenReturn(List.of(new Card(District.TAVERN)));

        Player player3 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player3.getScore()).thenReturn(6);
        when(player3.getCitadel()).thenReturn(List.of(new Card(District.CASTLE), new Card(District.CEMETERY), new Card(District.MANOR)));

        Player player4 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player4.getScore()).thenReturn(5);
        when(player4.getCitadel()).thenReturn(List.of(new Card(District.MARKET_PLACE), new Card(District.CEMETERY), new Card(District.MANOR), new Card(District.TAVERN)));

        List<Player> expectedOrder = List.of(player1, player2, player3, player4);

        when(gameWithFourPlayers.getPlayers()).thenReturn(new ArrayList<>(List.of(player4, player3, player2, player1)));
        gameWithFourPlayers.orderPlayersByPoints();

        assertEquals(expectedOrder, gameWithFourPlayers.getPlayers());
    }

    @Test
    void testReorderPlayersByPointsWithTieInPointsAndCitadelSize() {
        Player player1 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player1.getScore()).thenReturn(10);
        when(player1.getCitadel()).thenReturn(List.of(new Card(District.MANOR), new Card(District.CEMETERY))); // MANO Cost 3 // CEME Cost 5

        Player player2 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player2.getScore()).thenReturn(10);
        when(player2.getCitadel()).thenReturn(List.of(new Card(District.TAVERN), new Card(District.CEMETERY))); // TAVER Cost 1 // CEME Cost 5

        Player player3 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player3.getScore()).thenReturn(6);
        when(player3.getCitadel()).thenReturn(List.of(new Card(District.CASTLE), new Card(District.CEMETERY), new Card(District.MANOR)));

        Player player4 = spy(new RandomBot(5, new Deck(), view, fourPlayersGameBank));
        when(player4.getScore()).thenReturn(6);
        when(player4.getCitadel()).thenReturn(List.of(new Card(District.MARKET_PLACE), new Card(District.CEMETERY), new Card(District.MANOR), new Card(District.TAVERN)));

        List<Player> expectedOrder = List.of(player1, player2, player4, player3);

        when(gameWithFourPlayers.getPlayers()).thenReturn(new ArrayList<>(List.of(player4, player3, player2, player1)));
        gameWithFourPlayers.orderPlayersByPoints();

        assertEquals(expectedOrder, gameWithFourPlayers.getPlayers());
    }

}
