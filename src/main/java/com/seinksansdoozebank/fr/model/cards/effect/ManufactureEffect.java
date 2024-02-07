package com.seinksansdoozebank.fr.model.cards.effect;

import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

public class ManufactureEffect implements ActiveEffect {

    public static final int NB_GOLD_TO_PAY_TO_USE_EFFECT = 3;

    /**
     * Once per turn, the player can pay three gold coins to draw three cards.
     *
     * @param player the player who use the effect
     */
    @Override
    public void use(Player player, IView view) {
        if (player.wantToUseManufactureEffect()) {
            player.returnGoldToBank(NB_GOLD_TO_PAY_TO_USE_EFFECT);
            for (int i = 0; i < NB_GOLD_TO_PAY_TO_USE_EFFECT; i++) {
                player.pickACard();
            }
            view.displayPlayerUseManufactureEffect(player);
        }
    }
}
