package com.seinksansdoozebank.fr.model.player.custombot.strategies;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils.isRoleInCharacterList;
import static org.junit.jupiter.api.Assertions.*;

class StrategyUtilsTest {
    List<Character> characters;
    Assassin assassin;
    Magician magician;
    Thief thief;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickCoin(Bank.MAX_COIN / 2);
        assassin = new Assassin();
        magician = new Magician();
        thief = new Thief();
        characters = List.of(assassin, magician, thief);
    }

    @Test
    void isRoleInCharacterListWithRoleInCharacterListShouldBeTrue() {
        assertTrue(isRoleInCharacterList(Role.ASSASSIN, characters));
        assertTrue(isRoleInCharacterList(Role.MAGICIAN, characters));
        assertTrue(isRoleInCharacterList(Role.THIEF, characters));
    }

    @Test
    void isRoleInCharacterListWithRoleNotInCharacterListShouldBeFalse() {
        List<Character> characters = List.of(new Assassin(), new Magician(), new Thief());
        assertFalse(isRoleInCharacterList(Role.CONDOTTIERE, characters));
        assertFalse(isRoleInCharacterList(Role.ARCHITECT, characters));
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

}