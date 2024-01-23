package com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;
import java.util.Random;

/**
 * Represents the strategy of the bot to use the murderer effect to focus the rusher
 * by trying to kill the Merchant or the Architect because they are the most important for the first player
 * so we suppose that the first player certainly has one of them
 */
public class UsingMurdererEffectToFocusRusher implements IUsingMurdererEffectStrategy {

    private static final Random random = new Random();

    @Override
    public void apply(Player player, Assassin murderer) {
        List<Character> characters = player.getAvailableCharacters().stream().filter(character -> character.getRole() != Role.ASSASSIN).toList();
        if (isRoleInCharacterList(Role.MERCHANT, characters)) {
            murderer.useEffect(getCharacterFromRoleInLIst(Role.MERCHANT, characters));
        } else if (isRoleInCharacterList(Role.ARCHITECT, characters)) {
            murderer.useEffect(getCharacterFromRoleInLIst(Role.ARCHITECT, player.getAvailableCharacters()));
        } else if (isRoleInCharacterList(Role.KING, player.getAvailableCharacters())) {
            murderer.useEffect(getCharacterFromRoleInLIst(Role.KING, characters));
        } else { //random
            murderer.useEffect(characters.get(random.nextInt(characters.size())));
        }
    }

    boolean isRoleInCharacterList(Role role, List<Character> characters) {
        return characters.stream().anyMatch(character -> character.getRole().equals(role));
    }

    Character getCharacterFromRoleInLIst(Role role, List<Character> characters) {
        return characters.stream().filter(character -> character.getRole().equals(role)).findFirst().orElseThrow();
    }

}
