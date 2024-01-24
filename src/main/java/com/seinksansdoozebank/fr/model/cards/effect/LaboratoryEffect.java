package com.seinksansdoozebank.fr.model.cards.effect;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

public class LaboratoryEffect implements ActiveEffect {

    /**
     * Once per turn, the player can discard a card from his hand and get one gold.
     *
     * @param player the player who use the effect
     */
    @Override
    public void use(Player player, IView view) {
        Card card = player.chooseCardToDiscardForLaboratoryEffect();
        if (card != null && (player.discardFromHand(card))) {
            player.increaseGold(1);
            view.displayPlayerUseLaboratoryEffect(player);
        }
    }
}

