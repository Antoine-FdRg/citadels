package com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Comparator;
import java.util.Optional;

public class CardChoosingStrategy implements ICardChoosingStrategy {

    /**
     * Choose among the cards in the player's hand the one to add to the citadel
     * The chosen one is the card that is not the most expensive and that not the cheapest
     *
     * @param player the player who choose the card
     * @param view   the view to display the choice
     * @return the chosen card
     */
    @Override
    public Optional<Card> apply(Player player, IView view) {
        if (player.getHand().isEmpty()) {
            return Optional.empty();
        }
        // Get the mean of the cost of the cards in the player's hand
        int mean = player.getHand().stream().mapToInt(card -> card.getDistrict().getCost()).sum() / player.getHand().size();
        // Get the card that is the closest to the mean
        Optional<Card> cardToChoose = player.getHand().stream().min(Comparator.comparingInt(card -> Math.abs(card.getDistrict().getCost() - mean)));
        // Check that the player can play the card
        if (cardToChoose.isPresent() && player.canPlayCard(cardToChoose.get())) {
            return cardToChoose;
        }
        return Optional.empty();
    }
}
