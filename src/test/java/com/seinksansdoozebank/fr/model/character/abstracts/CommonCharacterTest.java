package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonCharacterTest {

    @Test
    void testEqualsWhenSameRole() {
        CommonCharacter bishop1 = new Bishop();
        CommonCharacter bishop2 = new Bishop();
        assertEquals(bishop1, bishop2);
    }

    @Test
    void testNotEqualsWhenNotSameRole() {
        CommonCharacter bishop1 = new Bishop();
        CommonCharacter king1 = new King();
        assertNotEquals(bishop1, king1);
    }

    @Test
    void testNotEqualsWhenNotObject() {
        CommonCharacter bishop1 = new Bishop();
        Character assassin = new Assassin();
        assertNotEquals(bishop1, assassin);
    }

    @Test
    void testHashCode() {
        CommonCharacter bishop1 = new Bishop();
        CommonCharacter bishop2 = new Bishop();
        assertEquals(bishop1.hashCode(), bishop2.hashCode());
    }
}