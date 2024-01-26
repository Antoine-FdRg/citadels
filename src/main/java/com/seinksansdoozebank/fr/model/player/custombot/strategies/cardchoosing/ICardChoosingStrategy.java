package com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing;


import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Optional;

/**
 * Represent a strategy to choose a card to add to the citadel
 */
public interface ICardChoosingStrategy {

    Optional<Card> apply(Player player, IView view);
}
