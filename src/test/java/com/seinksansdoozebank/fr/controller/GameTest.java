package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    Game gameWithFivePlayers;
    Game gameWithThreePlayers;
    Game gameWithFourPlayers;
    Player playerWIthEightDistrictsAndFiveDistrictTypes;
    Player playerWithNoBonus;
    Player playerWithEightDistricts;
    private Cli view;
    List<Character> charactersList;

    @BeforeEach
    public void setUp() {
        view = mock(Cli.class);
        gameWithFivePlayers = spy(new Game(5, view));
        gameWithThreePlayers = new Game(3, view);
        gameWithFourPlayers = spy(new Game(4, view));

        //Set player 1 with eight districts in its citadel and five different districtTypes
        playerWIthEightDistrictsAndFiveDistrictTypes = spy(new RandomBot(5, new Deck(), view));

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
        playerWithNoBonus = spy(new RandomBot(5, new Deck(), view));

        List<Card> citadelWithNoBonusAssociated = new ArrayList<>(List.
                of(new Card(District.PORT),
                        new Card(District.PALACE)));

        when(playerWithNoBonus.getCitadel()).thenReturn(citadelWithNoBonusAssociated);

        //Set player 3 with eight district in its citadel and with less than 5 different districtTypes
        playerWithEightDistricts = spy(new RandomBot(5, new Deck(), view));

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

        charactersList = List.of(
                new Assassin(),
                new King(),
                new Bishop(),
                new Merchant(),
                new Condottiere()
        );

    }

    @Test
    void getWinnerIsP3WithTheMostPoint() {
        Player p1 = mock(Player.class);
        when(p1.getScore()).thenReturn(5);
        when(p1.getIsFirstToHaveEightDistricts()).thenReturn(true);
        Player p2 = mock(Player.class);
        when(p2.getScore()).thenReturn(4);
        Player p3 = mock(Player.class);
        when(p3.getScore()).thenReturn(7);
        Player p4 = mock(Player.class);
        when(p4.getScore()).thenReturn(6);
        gameWithFivePlayers.setPlayers(List.of(p1, p2, p3, p4));
        assertEquals(p3, gameWithFivePlayers.getWinner());
    }

    @Test
    void createCharactersCreatesAsMuchCharactersAsWanted() {
        gameWithFourPlayers.createCharacters();

        assertEquals(charactersList.size(), gameWithFourPlayers.getAvailableCharacters().size());
    }

    @Test
    void playersChoseCharactersMakeAllPlayersChooseACharacter() {
        List<Player> players = gameWithFivePlayers.players;
        gameWithFivePlayers.getAvailableCharacters().addAll(charactersList);

        gameWithFivePlayers.playersChooseCharacters();

        for (Player player : players) {
            assertNotNull(player.getCharacter());
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
        gameWithThreePlayers.isTheFirstOneToHaveEightDistricts(playerWIthEightDistrictsAndFiveDistrictTypes);
        //playerWithEightDistricts is not the first who has eight districts +2 in bonus
        gameWithThreePlayers.isTheFirstOneToHaveEightDistricts(playerWithEightDistricts);
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
        assertTrue(gameWithThreePlayers.hasFiveDifferentDistrictTypes(playerWIthEightDistrictsAndFiveDistrictTypes));
    }

    /**
     * This test verifies the method hasFiveDifferentDistrictTypes on a player who has got five different types of district
     */
    @Test
    void hasDifferentDistrictTypeWithPlayerNotHavingFiveTypesShouldFalse() {
        //botWithLessThanFiveDifferentDistrictType
        gameWithThreePlayers.updatePlayersBonus();
        assertFalse(gameWithThreePlayers.hasFiveDifferentDistrictTypes(playerWithNoBonus));
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
    void run() {
        gameWithFourPlayers.run();
        verify(gameWithFourPlayers, times(1)).init();
        int nbRoundPlayed = gameWithFourPlayers.getNbCurrentRound() - 1;
        verify(gameWithFourPlayers, times(nbRoundPlayed)).playARound();
        verify(gameWithFourPlayers, times(1)).updatePlayersBonus();
        verify(view, times(1)).displayWinner(any(Player.class));
    }

    @Test
    void playARound() {
        gameWithFourPlayers.createCharacters();
        gameWithFourPlayers.playARound();
        verify(view, times(1)).displayRound(anyInt());
        verify(gameWithFourPlayers, times(1)).orderPlayerBeforeChoosingCharacter();
        verify(gameWithFourPlayers, times(1)).playersChooseCharacters();
        verify(gameWithFourPlayers, times(1)).orderPlayerBeforePlaying();
        verify(gameWithFourPlayers, atMost(gameWithFourPlayers.players.size())).isTheFirstOneToHaveEightDistricts(any(Player.class));
        verify(gameWithFourPlayers, atLeast(gameWithFourPlayers.players.size() - 1)).isTheFirstOneToHaveEightDistricts(any(Player.class));
        verify(gameWithFourPlayers, times(1)).retrieveCharacters();
        for (Character character : gameWithFourPlayers.getAvailableCharacters()) {
            assertFalse(character.isDead());
        }
    }

    @Test
    void crownedPlayerIsUpdatedWhilePlayingARoundANdKingIsAlive() {
        gameWithFourPlayers.getAvailableCharacters().addAll(List.of(new King(), new Bishop(), new Merchant(), new Condottiere()));

        gameWithFourPlayers.playARound();

        assertEquals(gameWithFourPlayers.players.get(0), gameWithFourPlayers.crownedPlayer);
    }

    @Test
    void kingPlayerIsNotUpdatedWhilePlayingARoundANdKingIsDead() {
        King king = new King();
        Assassin assassin = new Assassin();
        assassin.useEffect(king);
        gameWithFourPlayers.getAvailableCharacters().addAll(List.of(king, new Bishop(), new Merchant(), new Condottiere()));

        gameWithFourPlayers.playARound();

        verify(gameWithFourPlayers, times(gameWithFourPlayers.players.size() - 1)).updateCrownedPlayer(any(Player.class));
        assertNull(gameWithFourPlayers.crownedPlayer);
    }

    @Test
    void newGameWithTwoPlayers() {
        assertThrows(IllegalArgumentException.class, () -> new Game(2, view));
    }

    @Test
    void newGameWithSevenPlayers() {
        assertThrows(IllegalArgumentException.class, () -> new Game(7, view));
    }

    @Test
    void createCharactersWithFourPlayers() {
        gameWithFourPlayers.createCharacters();
        assertEquals(5, gameWithFourPlayers.getAvailableCharacters().size());
        assertTrue(gameWithFourPlayers.getAvailableCharacters().contains(new King()));
    }

    @Test
    void createCharactersWithFivePlayers() {
        assertThrows(UnsupportedOperationException.class, () -> gameWithFivePlayers.createCharacters());
//        TODO UNCOMMENT these lines when a sixth character is added and remove the assertThrows one
//        gameWithFivePlayers.createCharacters();
//        assertEquals(5, gameWithFivePlayers.getAvailableCharacters().size());
//        assertTrue(gameWithFourPlayers.getAvailableCharacters().contains(new King()));
    }

    @Test
    void createCharactersWithSixPlayers() {
        Game gameWithSixPlayers = new Game(6, view);
        assertThrows(UnsupportedOperationException.class, gameWithSixPlayers::createCharacters);
//        TODO UNCOMMENT this line when a sixth character is added and remove the assertThrows one
//        gameWithSixPlayers.createCharacters();
//        assertEquals(6, gameWithSixPlayers.getAvailableCharacters().size());
//        assertTrue(gameWithFourPlayers.getAvailableCharacters().contains(new King()));
    }
}
