package com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing;


import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.Optional;

/**
 * Represent a strategy to choose a card to add to the citadel
 */
public interface ICardChoosingStrategy {

    /**
     * Choose among the cards in the player's hand the one to add to the citadel
     *
     * @param player the player who choose the card
     * @return the chosen card
     */
    Optional<Card> apply(Player player);
}
