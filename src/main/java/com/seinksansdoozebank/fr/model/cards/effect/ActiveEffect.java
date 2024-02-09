package com.seinksansdoozebank.fr.model.cards.effect;

import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

/**
 * Represents an active effect of a district
 */
public interface ActiveEffect {
    /**
     * Use the effect of the district
     *
     * @param player the player who uses the effect
     * @param view   the view
     */
    void use(Player player, IView view);
}
