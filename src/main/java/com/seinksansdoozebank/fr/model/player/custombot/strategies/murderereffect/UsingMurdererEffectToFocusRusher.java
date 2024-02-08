package com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;
import com.seinksansdoozebank.fr.view.IView;

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
    public Character apply(Player player, IView view) {
        List<Character> characters = player.getAvailableCharacters().stream().filter(character -> character.getRole() != Role.ASSASSIN).toList();
        if (StrategyUtils.isRoleInCharacterList(Role.MERCHANT, characters)) {
            return useAndDisplayMurderEffect(Role.MERCHANT, characters, view, player);
        } else if (StrategyUtils.isRoleInCharacterList(Role.ARCHITECT, characters)) {
            return useAndDisplayMurderEffect(Role.ARCHITECT, characters, view, player);
        } else if (StrategyUtils.isRoleInCharacterList(Role.KING, characters)) {
            return useAndDisplayMurderEffect(Role.KING, characters, view, player);
        } else { //random
            Character targetCharacter = characters.get(random.nextInt(characters.size()));
            view.displayPlayerUseAssassinEffect(player, targetCharacter);
            return targetCharacter;
        }
    }

    private static Character useAndDisplayMurderEffect(Role role, List<Character> characters, IView view, Player player) {
        Character targetCharacter = StrategyUtils.getCharacterFromRoleInList(role, characters);
        view.displayPlayerUseAssassinEffect(player, targetCharacter);
        return targetCharacter;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UsingMurdererEffectToFocusRusher;
    }

    @Override
    public int hashCode() {
        return UsingMurdererEffectToFocusRusher.class.getName().hashCode();
    }
}
