package com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

/**
 * Represent a strategy to use effect of the murderer
 */
public interface IUsingMurdererEffectStrategy {
    /**
     * Apply the murderer effect strategy
     *
     * @param player the player to apply the strategy on
     * @param view   the view
     * @return the chosen character
     */
    Character apply(Player player, IView view);
}
