package com.seinksansdoozebank.fr.model.cards.effect;

import com.seinksansdoozebank.fr.model.player.Player;

public interface ActiveEffect {
    void use(Player player);
}
