package com.seinksansdoozebank.fr.model.player.custombot.strategies.picking;

import com.seinksansdoozebank.fr.model.player.Player;

/**
 * Interface for picking strategies
 */
public interface IPickingStrategy {
    /**
     * Apply the picking strategy to the player
     * @param player the player to apply the strategy to
     */
    void apply(Player player);
}
