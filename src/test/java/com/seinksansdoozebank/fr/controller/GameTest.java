package com.seinksansdoozebank.fr.controller;

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
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.seinksansdoozebank.fr.model.character.roles.Role.ARCHITECT;
import static com.seinksansdoozebank.fr.model.character.roles.Role.MERCHANT;
import static com.seinksansdoozebank.fr.model.character.roles.Role.THIEF;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    Game game;
    Game gameWithThreePlayer;
    Player playerWIthEightDistrictsAndFiveDistrictTypes;
    Player playerWithNoBonus;
    Player playerWithEightDistricts;
    private Cli view;

    @BeforeEach
    public void setUp() {
        view = mock(Cli.class);
        game = new Game(4);
        gameWithThreePlayer = new Game(3);

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

        gameWithThreePlayer.setPlayers(List.of(playerWIthEightDistrictsAndFiveDistrictTypes, playerWithNoBonus, playerWithEightDistricts));

    }

    @Test
    void getWinner() {
        Player p1 = mock(Player.class);
        when(p1.getScore()).thenReturn(5);
        when(p1.getIsFirstToHaveEightDistricts()).thenReturn(true);
        Player p2 = mock(Player.class);
        when(p2.getScore()).thenReturn(4);
        Player p3 = mock(Player.class);
        when(p3.getScore()).thenReturn(7);
        Player p4 = mock(Player.class);
        when(p4.getScore()).thenReturn(6);
        game.setPlayers(List.of(p1, p2, p3, p4));
        assertEquals(p3, game.getWinner());
    }

    @Test
    void testCharactersChoice() {
        game.createCharacters();
        assertEquals(4, game.getAvailableCharacters().size());
    }

    @Test
    void testCreateCharacters() {
        // Test the createCharacters method
        game.createCharacters();

        List<Character> availableCharacters = game.getAvailableCharacters();

        // Check if the correct number of characters is created
        assertEquals(4, availableCharacters.size()); // Adjust the expected size based on your implementation
    }

    @Test
    void testPlayersChooseCharacters() {
        // Test the playersChooseCharacters method
        List<Player> players = game.players;

        // Test that createCharacters create a list of characters
        game.createCharacters();
        List<Character> charactersList = List.of(
                new King(),
                new Bishop(),
                new Merchant(),
                new Condottiere()
        );

        // Test that the game available characters list is equal to charactersList
        assertEquals(charactersList, game.getAvailableCharacters());

        // Test that all players have chosen a character
        game.playersChooseCharacters();

        // Check if all players have a character
        for (Player player : players) {
            assertNotNull(player.getCharacter());
        }

        // Check if all characters were removed from the available characters list
        assertEquals(0, game.getAvailableCharacters().size());

        // Reset the available characters list
        game.retrieveCharacters();

        // Check if the available characters list is equal to charactersList
        assertEquals(4, game.getAvailableCharacters().size());
    }

    /**
     * This test verify the method isFirstToHaveEightDistrict and verify the bonus associated to
     */
    @Test
    void isFirstBotToHaveEightDistrictsTest() {
        //playerWithEightDistrictsAndFiveDistrictTypes is the first who has eight districts in that game so +7 in bonus
        gameWithThreePlayer.isTheFirstOneToHaveEightDistricts(playerWIthEightDistrictsAndFiveDistrictTypes);
        //playerWithEightDistricts is not the first who has eight districts +2 in bonus
        gameWithThreePlayer.isTheFirstOneToHaveEightDistricts(playerWithEightDistricts);
        gameWithThreePlayer.updatePlayersBonus();

        assertEquals(7, playerWIthEightDistrictsAndFiveDistrictTypes.getBonus());
        assertEquals(2, playerWithEightDistricts.getBonus());
    }

    /**
     * This test verifies the method hasFiveDifferentDistrictTypes on a player who has got five different types of district
     */
    @Test
    void hasDifferentDistrictTypeTestWithPlayerHavingFiveTypesShouldReturnTrue() {
        //botWithFiveDifferentDistrictType
        gameWithThreePlayer.updatePlayersBonus();
        assertTrue(gameWithThreePlayer.hasFiveDifferentDistrictTypes(playerWIthEightDistrictsAndFiveDistrictTypes));
    }

    /**
     * This test verifies the method hasFiveDifferentDistrictTypes on a player who has got five different types of district
     */
    @Test
    void hasDifferentDistrictTypeWithPlayerNotHavingFiveTypesShouldFalse() {
        //botWithLessThanFiveDifferentDistrictType
        gameWithThreePlayer.updatePlayersBonus();
        assertFalse(gameWithThreePlayer.hasFiveDifferentDistrictTypes(playerWithNoBonus));
    }

    /**
     * this test verifies the method findBonusPlayer when the player has got eight district in his citadel and verifies the bonus associated to
     */
    @Test
    void botWithEightDistrictInItCitadelTest() {

        gameWithThreePlayer.updatePlayersBonus();
        assertEquals(2, playerWithEightDistricts.getBonus());
    }

    /**
     * we verify that the method getPlayerWithRoleTest return the right Optional
     */
    @Test
    void getPlayerWithRoleTest() {
        //Création du player de type architecte
        Player playerArchitect = spy(new RandomBot(5, new Deck(), view));
        Architect architect = new Architect();
        architect.setPlayer(playerArchitect);
        when(playerArchitect.getCharacter()).thenReturn(architect);
        //Création du player de type voleur
        Player playerThief = spy(new RandomBot(5, new Deck(), view));
        Thief thief = new Thief();
        thief.setPlayer(playerThief);
        when(playerThief.getCharacter()).thenReturn(thief);

        game.setPlayers(List.of(playerThief, playerArchitect));
        //The player architect is present in the game
        assertEquals(Optional.of(playerArchitect), game.getPlayerWithRole(ARCHITECT));
        //The player merchant is not present in the game
        assertEquals(Optional.empty(), game.getPlayerWithRole(MERCHANT));
    }
}
