package com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;

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
        if (StrategyUtils.isRoleInCharacterList(Role.MERCHANT, characters)) {
            murderer.useEffect(StrategyUtils.getCharacterFromRoleInLIst(Role.MERCHANT, characters));
        } else if (StrategyUtils.isRoleInCharacterList(Role.ARCHITECT, characters)) {
            murderer.useEffect(StrategyUtils.getCharacterFromRoleInLIst(Role.ARCHITECT, player.getAvailableCharacters()));
        } else if (StrategyUtils.isRoleInCharacterList(Role.KING, player.getAvailableCharacters())) {
            murderer.useEffect(StrategyUtils.getCharacterFromRoleInLIst(Role.KING, characters));
        } else { //random
            murderer.useEffect(characters.get(random.nextInt(characters.size())));
        }
    }
}
