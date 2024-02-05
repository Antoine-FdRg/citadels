package com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
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
    public void apply(Player player, Assassin murderer, IView view) {
        List<Character> characters = player.getAvailableCharacters().stream().filter(character -> character.getRole() != Role.ASSASSIN).toList();
        if (StrategyUtils.isRoleInCharacterList(Role.MERCHANT, characters)) {
            useAndDisplayMurderEffect(murderer, Role.MERCHANT, characters, view, player);
        } else if (StrategyUtils.isRoleInCharacterList(Role.ARCHITECT, characters)) {
            useAndDisplayMurderEffect(murderer, Role.ARCHITECT, characters, view, player);
        } else if (StrategyUtils.isRoleInCharacterList(Role.KING, player.getAvailableCharacters())) {
            useAndDisplayMurderEffect(murderer, Role.KING, characters, view, player);
        } else { //random
            Character targetCharacter = characters.get(random.nextInt(characters.size()));
            murderer.useEffect(targetCharacter);
            view.displayPlayerUseAssassinEffect(player, targetCharacter);
        }
    }

    private static void useAndDisplayMurderEffect(Assassin murderer, Role role, List<Character> characters, IView view, Player player) {
        Character targetCharacter = StrategyUtils.getCharacterFromRoleInLIst(role, characters);
        murderer.useEffect(targetCharacter);
        view.displayPlayerUseAssassinEffect(player, targetCharacter);
    }

    @Override
    public String toString() {
        return "UsingMurdererEffectToFocusRusher";
    }
}
