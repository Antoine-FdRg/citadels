package com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect;

import com.seinksansdoozebank.fr.model.character.commoncharacters.WarlordTarget;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

/**
 * Represent a strategy to use the effect of the warlord
 */
public interface IUsingWarlordEffectStrategy {
    /**
     * Apply the Warlord Effect strategy
     *
     * @param player    the player to apply the strategy on
     * @param opponents the opponents
     * @return the chosen target
     */
    WarlordTarget apply(Player player, List<Opponent> opponents);
}
