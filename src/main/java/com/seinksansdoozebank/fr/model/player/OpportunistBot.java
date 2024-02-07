package com.seinksansdoozebank.fr.model.player;

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
    public OpportunistBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public Character chooseCharacterImpl(List<Character> characters) {
        int quartierReligieux = (int) this.getCitadel().stream().filter(d -> d.getDistrict().getDistrictType().equals(DistrictType.RELIGION)).count();
        Character choice = null;
        if (quartierReligieux > 0) {
            // Search for the Bishop character
            choice = characters.stream().filter(c -> c.getRole().equals(Role.BISHOP)).findFirst().orElse(null);
        }
        if (this.getNbGold() > 1 && choice == null) {
            // Search for the Condottiere character
            choice = characters.stream().filter(c -> c.getRole().equals(Role.CONDOTTIERE)).findFirst().orElse(null);
        }
        if (this.getNbGold() > 4) {
            // Search for the Thief character
            choice = characters.stream().filter(c -> c.getRole().equals(Role.THIEF)).findFirst().orElse(null);
        }
        if (choice == null) {
            choice = characters.get(random.nextInt(characters.size()));
        }
        return choice;
    }

    @Override
    protected Optional<Card> chooseCard() {
        List<Card> cards = new ArrayList<>(this.getHand());
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

