package com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing;

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
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChoosingCharacterToTargetFirstPlayerTest {
    ChoosingCharacterToTargetFirstPlayer choosingCharacterToTargetFirstPlayer;
    Player mockPlayer;

    List<Character> characterList;
    Opponent leadingOpponent;

    @BeforeEach
    void setUp() {
        mockPlayer = mock(Player.class);
        choosingCharacterToTargetFirstPlayer = new ChoosingCharacterToTargetFirstPlayer();
        characterList = new ArrayList<>();
        leadingOpponent = mock(Opponent.class);

    }

    @Test
    void getLeadingWithOpponentListShouldReturnOpponentO2() {
        Opponent o1 = mock(Opponent.class);
        when(o1.nbDistrictsInCitadel()).thenReturn(1);
        Opponent o2 = mock(Opponent.class);
        when(o2.nbDistrictsInCitadel()).thenReturn(3);
        Opponent o3 = mock(Opponent.class);
        when(o3.nbDistrictsInCitadel()).thenReturn(2);
        when(mockPlayer.getOpponents()).thenReturn(List.of(o1, o2, o3));

        Opponent leadingOpponent = choosingCharacterToTargetFirstPlayer.getLeadingOpponent(mockPlayer);

        assertEquals(o2, leadingOpponent);
    }

    @Test
    void getLeadingOpponentWithEmptyOpponentListShouldThrowException() {
        when(mockPlayer.getOpponents()).thenReturn(List.of());

        assertThrows(IllegalStateException.class, () -> choosingCharacterToTargetFirstPlayer.getLeadingOpponent(mockPlayer));
    }

    @Test
    void isRoleInCharacterListWithRoleInCharacterListShouldBeTrue() {
        List<Character> characters = List.of(new Assassin(), new Magician(), new Thief());
        assertTrue(choosingCharacterToTargetFirstPlayer.isRoleInCharacterList(Role.ASSASSIN, characters));
        assertTrue(choosingCharacterToTargetFirstPlayer.isRoleInCharacterList(Role.MAGICIAN, characters));
        assertTrue(choosingCharacterToTargetFirstPlayer.isRoleInCharacterList(Role.THIEF, characters));
    }

    @Test
    void isRoleInCharacterListWithRoleNotInCharacterListShouldBeFalse() {
        List<Character> characters = List.of(new Assassin(), new Magician(), new Thief());
        assertFalse(choosingCharacterToTargetFirstPlayer.isRoleInCharacterList(Role.CONDOTTIERE, characters));
        assertFalse(choosingCharacterToTargetFirstPlayer.isRoleInCharacterList(Role.ARCHITECT, characters));
    }

    @Test
    void applyWithCondottiereInListSgouldReturnCondottiere() {
        characterList.add(new Assassin());
        characterList.add(new Magician());
        characterList.add(new Condottiere());

        assertEquals(new Condottiere(), choosingCharacterToTargetFirstPlayer.apply(mockPlayer, characterList));
    }

    @Test
    void applyWithLeadingOpponentHavingManyGoldAndThiefInListShouldReturnThief() {
        when(leadingOpponent.getNbGold()).thenReturn(4);
        when(mockPlayer.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Assassin());
        characterList.add(new Thief());
        characterList.add(new Merchant());

        assertEquals(new Thief(), choosingCharacterToTargetFirstPlayer.apply(mockPlayer, characterList));
    }

    @Test
    void applyWithLeadingOpponentHavingManyGoldAndNoThiefInListShouldReturnArchitect() {
        when(leadingOpponent.getNbGold()).thenReturn(4);
        when(mockPlayer.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Assassin());
        characterList.add(new Merchant());
        characterList.add(new Architect());

        assertEquals(new Architect(), choosingCharacterToTargetFirstPlayer.apply(mockPlayer, characterList));
    }

    @Test
    void applyWithLeadingOpponentHavingFewGoldAndAssassinInListShouldReturnAssassin() {
        when(leadingOpponent.getNbGold()).thenReturn(1);
        when(mockPlayer.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Assassin());
        characterList.add(new Merchant());
        characterList.add(new Architect());

        assertEquals(new Assassin(), choosingCharacterToTargetFirstPlayer.apply(mockPlayer, characterList));
    }

    @Test
    void applyWithLeadingOpponentHavingFewGoldAndNoAssassinInListShouldReturnMerchant() {
        when(leadingOpponent.getNbGold()).thenReturn(1);
        when(mockPlayer.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Thief());
        characterList.add(new Merchant());
        characterList.add(new King());

        assertEquals(new Merchant(), choosingCharacterToTargetFirstPlayer.apply(mockPlayer, characterList));
    }

    @Test
    void applyWithNoCharacterBetterThanKingShouldReturnKing() {
        when(leadingOpponent.getNbGold()).thenReturn(1);
        when(mockPlayer.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Bishop());
        characterList.add(new Magician());
        characterList.add(new King());

        assertEquals(new King(), choosingCharacterToTargetFirstPlayer.apply(mockPlayer, characterList));
    }

    @Test
    void applyWithNoCharacterBetterThanKingAndNoKingShouldReturnRandomCharacter() {
        when(leadingOpponent.getNbGold()).thenReturn(1);
        when(mockPlayer.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Bishop());
        characterList.add(new Magician());

        assertTrue(characterList.contains(choosingCharacterToTargetFirstPlayer.apply(mockPlayer, characterList)));
    }

}