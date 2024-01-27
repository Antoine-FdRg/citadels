package com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect;

import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

/**
 * Represent a strategy to use effect of the murderer
 */
public interface IUsingMurdererEffectStrategy {
    void apply(Player player, Assassin murderer, IView view);
}
