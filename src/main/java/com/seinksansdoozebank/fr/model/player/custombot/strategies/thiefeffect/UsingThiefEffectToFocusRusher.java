package com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;

import java.util.List;
import java.util.Random;

/**
 * Represents the strategy of the bot to use the thief effect to focus the rusher
 * by trying to kill the Architect or the Merchant because they are the most important for the first player
 * so we suppose that the first player certainly has one of them
 */
public class UsingThiefEffectToFocusRusher implements IUsingThiefEffectStrategy {

    private static final Random random = new Random();

    @Override
    public Character apply(Player player) {
        List<Character> characters = player.getAvailableCharacters().stream().filter(character -> character.getRole() != Role.ASSASSIN &&
                character.getRole() != Role.THIEF &&
                !character.isDead()).toList();
        if (StrategyUtils.isRoleInCharacterList(Role.ARCHITECT, characters)) {
            return StrategyUtils.getCharacterFromRoleInLIst(Role.ARCHITECT, characters);
        } else if (StrategyUtils.isRoleInCharacterList(Role.MERCHANT, characters)) {
            return StrategyUtils.getCharacterFromRoleInLIst(Role.MERCHANT, characters);
        } else if (StrategyUtils.isRoleInCharacterList(Role.KING, characters)) {
            return StrategyUtils.getCharacterFromRoleInLIst(Role.KING, characters);
        } else { //random
            return characters.get(random.nextInt(characters.size()));
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UsingThiefEffectToFocusRusher;
    }

    @Override
    public int hashCode() {
        return UsingThiefEffectToFocusRusher.class.getName().hashCode();
    }
}
