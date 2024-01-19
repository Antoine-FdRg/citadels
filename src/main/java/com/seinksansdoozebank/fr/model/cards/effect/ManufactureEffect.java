package com.seinksansdoozebank.fr.model.cards.effect;

import com.seinksansdoozebank.fr.model.player.Player;

public class ManufactureEffect extends ActiveEffect {

    /**
     * Once per turn, the player can pay three gold coins to draw three cards.
     *
     * @param player the player who use the effect
     */
    @Override
    public void use(Player player) {
        player.decreaseGold(3);
        for (int i = 0; i < 3; i++) {
            player.pickACard();
        }
    }
}
