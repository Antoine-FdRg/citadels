package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.player.Opponent;

import java.util.List;

/**
 * Represent the target of the magician
 *
 * @param targetOpponent        the target opponent
 * @param cardsToSwitchWithDeck the cards to switch with the deck
 */
public record MagicianTarget(Opponent targetOpponent, List<Card> cardsToSwitchWithDeck) {
}
