package com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect;

import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;

/**
 * Represent a strategy to use the effect of the thief
 */
public interface IUsingThiefEffectStrategy {
    void apply(Player player, Thief thief);
}
