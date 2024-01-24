package com.seinksansdoozebank.fr.model.player.custombot.strategies;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;

import java.util.List;

public class StrategyUtils {

    public static boolean isRoleInCharacterList(Role role, List<Character> characters) {
        return characters.stream().anyMatch(character -> character.getRole().equals(role));
    }

    public static Character getCharacterFromRoleInLIst(Role role, List<Character> characters) {
        return characters.stream().filter(character -> character.getRole().equals(role)).findFirst().orElseThrow();
    }
}
