package com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Player;

/**
 * Represent a strategy to use the effect of the thief
 */
public interface IUsingThiefEffectStrategy {
    Character apply(Player player);
}
