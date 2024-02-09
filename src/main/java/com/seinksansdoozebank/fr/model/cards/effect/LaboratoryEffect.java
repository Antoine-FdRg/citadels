package com.seinksansdoozebank.fr.model.cards.effect;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

/**
 * Effect of laboratory district
 */
public class LaboratoryEffect implements ActiveEffect {

    /**
     * The number of gold to pick with laboratory effect
     */
    private static final int NB_GOLD_PICK_BY_LABORATORY_EFFECT = 1;

    /**
     * Once per turn, the player can discard a card from his hand and get one gold.
     *
     * @param player the player who use the effect
     */
    @Override
    public void use(Player player, IView view) {
        if (player.getHand().isEmpty()) {
            return;
        }
        Card card = player.chooseCardToDiscardForLaboratoryEffect();
        if (card != null) {
            view.displayPlayerUseLaboratoryEffect(player);
            if (player.discardFromHand(card)) {
                player.pickGold(NB_GOLD_PICK_BY_LABORATORY_EFFECT);
            }
        }
    }
}

