package com.seinksansdoozebank.fr.model.cards.effect;

import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

public class ManufactureEffect implements ActiveEffect {

    /**
     * Once per turn, the player can pay three gold coins to draw three cards.
     *
     * @param player the player who use the effect
     */
    @Override
    public void use(Player player, IView view) {
        if (player.wantToUseManufactureEffect()) {
            player.decreaseGold(3);
            for (int i = 0; i < 3; i++) {
                player.pickACard();
            }
            view.displayPlayerUseManufactureEffect(player);
        }
    }
}
