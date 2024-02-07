package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.player.Opponent;

import java.util.List;

public record MagicianTarget(Opponent targetOpponent, List<Card> cardsToSwitchWithDeck) {
}
