package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.singleton.Bishop;
import com.seinksansdoozebank.fr.model.character.singleton.Condottiere;
import com.seinksansdoozebank.fr.model.character.singleton.King;
import com.seinksansdoozebank.fr.model.character.singleton.Merchant;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

class PlayerTest {
    List<District> hand = new ArrayList<>();
    Player player;
    IView view;

    @BeforeEach
    void setup() {
        view = mock(Cli.class);
        District districtCostThree = District.PORT;
        District districtCostFive = District.FORTRESS;
        hand.add(districtCostThree);
        hand.add(districtCostFive);
        player = new Player(10,view);
        player.addDistrictToHand(districtCostThree);
        player.addDistrictToHand(districtCostFive);
    }

    @Test
    void testPlay() {
        District playedDistrict = player.play();
        assertFalse(player.getHand().contains(playedDistrict));
        assertEquals(10 - playedDistrict.getCost(), player.getNbGold());
    }

    @Test
    void testChooseDistrict() {
        District chosenDistrict = player.chooseDistrict();
        assertTrue(hand.contains(chosenDistrict));
    }

    @Test
    void testUpdateGold() {
        // Arrange
        Player player = new Player(10,view);

        // Act
        player.decreaseGold(3);

        // Assert
        assertEquals(7, player.getNbGold());
    }

    @Test
    void testPlayerInitialization() {
        // Arrange
        List<District> hand = new ArrayList<>();
        hand.add(District.PORT); //district with a cost of 3
        List<District> citadel = new ArrayList<>();

        // Act
        Player player = new Player(10,view);
        player.addDistrictToHand(District.PORT);

        // Assert
        assertEquals(10, player.getNbGold());
        assertEquals(hand, player.getHand());
        assertEquals(citadel, player.getCitadel());
    }

    @Test
    void testResetIdCounter() {
        // Test resetting the ID counter for player
        Player.resetIdCounter();
        Player newPlayer = new Player(10,view);
        assertEquals(1, newPlayer.getId()); // Should start counting from 1 again
    }

    @Test
    void testChooseCharacter() {
        // Arrange
        List<Character> characters = new ArrayList<>();
        characters.add(new Bishop());
        characters.add(new King());
        characters.add(new Merchant());
        characters.add(new Condottiere());

        // Act
        Character character = player.chooseCharacter(characters);

        // Assert
        assertTrue(characters.contains(character));
    }
}
