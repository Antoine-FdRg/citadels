package com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Player;

/**
 * Represent a strategy to use the effect of the thief
 */
public interface IUsingThiefEffectStrategy {
    /**
     * Apply the Thief Effect strategy
     *
     * @param player the player to apply the strategy on
     * @return the chosenCharacter
     */
    Character apply(Player player);
}
