package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OpportunistBot extends SmartBot {
    public OpportunistBot(int nbGold, Deck deck, IView view, Bank bank) {
        super(nbGold, deck, view, bank);
    }

    /**
     * Chooses a character based on the player's current state and strategy.
     * Prioritizes the Bishop if the player has at least one religious district.
     * If the player has more than 1 gold, selects the Warlord.
     * If any opponent has 4 or more gold, opts for the Thief.
     * Otherwise, makes a random choice from the available characters.
     *
     * @param characters A list of characters that can be chosen from.
     * @return The chosen character.
     */
    @Override
    public Character chooseCharacterImpl(List<Character> characters) {
        int quartierReligieux = (int) this.getCitadel().stream().filter(d -> d.getDistrict().getDistrictType().equals(DistrictType.RELIGION)).count();
        Character choice = null;
        if (quartierReligieux > 0) {
            // Search for the Bishop character
            choice = characters.stream().filter(c -> c.getRole().equals(Role.BISHOP)).findFirst().orElse(null);
        }
        if (this.getNbGold() > 1 && choice == null) {
            // Search for the Warlord character
            choice = characters.stream().filter(c -> c.getRole().equals(Role.WARLORD)).findFirst().orElse(null);
        }
        // if there is a player with equal or more than 4 gold, search for the Thief character
        if (this.getOpponents().stream().anyMatch(o -> o.getNbGold() >= 4)) {
            // Search for the Thief character
            choice = characters.stream().filter(c -> c.getRole().equals(Role.THIEF)).findFirst().orElse(null);
        }
        if (choice == null) {
            choice = characters.get(random.nextInt(characters.size()));
        }
        return choice;
    }

    /**
     * Chooses a religious district if available; otherwise, selects the least expensive district.
     *
     * @return The chosen district card.
     */
    @Override
    protected Optional<Card> chooseCard() {
        List<Card> cards = new ArrayList<>(this.getHand());
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        // sort the cards by cost
        cards.sort(Comparator.comparingInt(c -> c.getDistrict().getCost()));
        for (Card card : cards) {
            if (card.getDistrict().getDistrictType().equals(DistrictType.RELIGION)) {
                cards.removeIf(c -> !c.getDistrict().getDistrictType().equals(DistrictType.RELIGION));
                return Optional.of(cards.get(cards.size() - 1));
            }
        }

        return Optional.of(cards.get(cards.size() - 1));
    }

    @Override
    public String toString() {
        return "Le bot opportuniste " + this.id;
    }
}

