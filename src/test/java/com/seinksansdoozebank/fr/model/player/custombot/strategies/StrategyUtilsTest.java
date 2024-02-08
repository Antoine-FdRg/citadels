package com.seinksansdoozebank.fr.model.player.custombot.strategies;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StrategyUtilsTest {
    List<Character> characters;
    Assassin assassin;
    Magician magician;
    Thief thief;

    @BeforeEach
    void setUp() {
        assassin = new Assassin();
        magician = new Magician();
        thief = new Thief();
        characters = List.of(assassin, magician, thief);
    }

    @Test
    void isRoleInCharacterListWithRoleInCharacterListShouldBeTrue() {
        assertTrue(StrategyUtils.isRoleInCharacterList(Role.ASSASSIN, characters));
        assertTrue(StrategyUtils.isRoleInCharacterList(Role.MAGICIAN, characters));
        assertTrue(StrategyUtils.isRoleInCharacterList(Role.THIEF, characters));
    }

    @Test
    void isRoleInCharacterListWithRoleNotInCharacterListShouldBeFalse() {
        List<Character> characters = List.of(new Assassin(), new Magician(), new Thief());
        assertFalse(StrategyUtils.isRoleInCharacterList(Role.CONDOTTIERE, characters));
        assertFalse(StrategyUtils.isRoleInCharacterList(Role.ARCHITECT, characters));
    }

    @Test
    void getCharacterFromRoleInLIstWithRoleInCharacterListShouldBeCharacter() {
        assertEquals(assassin, StrategyUtils.getCharacterFromRoleInLIst(Role.ASSASSIN, characters));
        assertEquals(magician, StrategyUtils.getCharacterFromRoleInLIst(Role.MAGICIAN, characters));
        assertEquals(thief, StrategyUtils.getCharacterFromRoleInLIst(Role.THIEF, characters));
    }

    @Test
    void getCharacterFromRoleInLIstWithRoleNotInCharacterListShouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> StrategyUtils.getCharacterFromRoleInLIst(Role.CONDOTTIERE, characters));
        assertThrows(NoSuchElementException.class, () -> StrategyUtils.getCharacterFromRoleInLIst(Role.ARCHITECT, characters));
    }


    @Test
    void getLeadingWithOpponentListShouldReturnOpponentO2() {
        Player mockPlayer = mock(Player.class);
        Opponent o1 = mock(Opponent.class);
        when(o1.nbDistrictsInCitadel()).thenReturn(1);
        Opponent o2 = mock(Opponent.class);
        when(o2.nbDistrictsInCitadel()).thenReturn(3);
        Opponent o3 = mock(Opponent.class);
        when(o3.nbDistrictsInCitadel()).thenReturn(2);

        Opponent leadingOpponent = StrategyUtils.getLeadingOpponent(List.of(o1, o2, o3));

        assertEquals(o2, leadingOpponent);
    }

    @Test
    void getLeadingOpponentWithEmptyOpponentListShouldThrowException() {
        List<Opponent> list = List.of();
        assertThrows(IllegalStateException.class, () -> StrategyUtils.getLeadingOpponent(list));
    }


}