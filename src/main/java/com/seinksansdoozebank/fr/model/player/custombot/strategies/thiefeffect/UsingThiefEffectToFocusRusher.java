package com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

/**
 * Represents the strategy of the bot to use the thief effect to focus the rusher
 * by trying to kill the Architect or the Merchant because they are the most important for the first player
 * so suppose that the first player certainly has one of them
 */
public class UsingThiefEffectToFocusRusher implements IUsingThiefEffectStrategy {
    @Override
    public void apply(Player player, Thief thief) {
        List<Character> characters = player.getAvailableCharacters().stream().filter(character -> character.getRole() != Role.ASSASSIN &&
                character.getRole() != Role.THIEF &&
                !character.isDead()).toList();
        if (isRoleInCharacterList(Role.ARCHITECT, characters)) {
            thief.useEffect(getCharacterFromRoleInLIst(Role.ARCHITECT, characters));
        } else if (isRoleInCharacterList(Role.MERCHANT, characters)) {
            thief.useEffect(getCharacterFromRoleInLIst(Role.MERCHANT, player.getAvailableCharacters()));
        } else if (isRoleInCharacterList(Role.KING, player.getAvailableCharacters())) {
            thief.useEffect(getCharacterFromRoleInLIst(Role.KING, characters));
        } else { //random
            thief.useEffect(characters.get(0));
        }
    }

    boolean isRoleInCharacterList(Role role, List<Character> characters) {
        return characters.stream().anyMatch(character -> character.getRole().equals(role));
    }

    Character getCharacterFromRoleInLIst(Role role, List<Character> characters) {
        return characters.stream().filter(character -> character.getRole().equals(role)).findFirst().orElseThrow();
    }
}
