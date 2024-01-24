package com.seinksansdoozebank.fr.model.cards.effect;

import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

public interface ActiveEffect {
    void use(Player player, IView view);
}
