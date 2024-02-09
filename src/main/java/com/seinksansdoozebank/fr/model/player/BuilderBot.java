package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * BuilderBot is a SmartBot that prioritizes building a citadel.
 */
public class BuilderBot extends SmartBot {
    /**
     * BuilderBot constructor
     *
     * @param nbGold The number of gold pieces the player has.
     * @param deck   The deck of cards.
     * @param view   The view.
     * @param bank   The bank.
     */
    public BuilderBot(int nbGold, Deck deck, IView view, Bank bank) {
        super(nbGold, deck, view, bank);
    }

    /**
     * Chooses a character based on the current state of the player's citadel and hand.
     * Prioritizes characters related to Nobility, Commerce and Crafts, and Architect roles.
     * If no specific criteria are met, makes a random choice.
     *
     * @param characters The available characters to choose from.
     * @return The chosen character.
     */
    @Override
    public Character chooseCharacterImpl(List<Character> characters) {
        Character choice = null;
        int quartierCommerce = (int) this.getCitadel().stream().filter(d -> d.getDistrict().getDistrictType().equals(DistrictType.TRADE_AND_CRAFTS)).count();
        int quartierNoblesse = (int) this.getCitadel().stream().filter(d -> d.getDistrict().getDistrictType().equals(DistrictType.NOBILITY)).count();

        if (quartierNoblesse > 0) {
            choice = characters.stream().filter(c -> c.getRole().equals(Role.KING)).findFirst().orElse(null);
        }
        if (quartierCommerce > 1 && choice == null) {
            choice = characters.stream().filter(c -> c.getRole().equals(Role.MERCHANT)).findFirst().orElse(null);
        }
        if (this.getHand().size() > 2 && this.getNbGold() > 5 && choice == null) {
            choice = characters.stream().filter(c -> c.getRole().equals(Role.ARCHITECT)).findFirst().orElse(null);
        }
        if (choice == null) {
            choice = characters.get(random.nextInt(characters.size()));
        }
        return choice;
    }

    /**
     * Chooses a district card to keep from a list of picked cards.
     * Prioritizes Nobility and Commerce and Crafts cards. If none are found, chooses the cheapest card.
     *
     * @param pickedCards The list of cards to choose from.
     * @return The chosen card to keep.
     */
    @Override
    protected Card keepOneDiscardOthers(List<Card> pickedCards) {
        Optional<Card> nobilityCard = pickedCards.stream()
                .filter(card -> card.getDistrict().getDistrictType().equals(DistrictType.NOBILITY))
                .findFirst();

        if (nobilityCard.isPresent()) {
            return nobilityCard.get();
        }

        Optional<Card> commerceCard = pickedCards.stream()
                .filter(card -> card.getDistrict().getDistrictType().equals(DistrictType.TRADE_AND_CRAFTS))
                .findFirst();
        // If no Nobility or Commerce and Crafts card is found, choose the cheapest one
        return commerceCard.orElseGet(() -> pickedCards.stream()
                .min(Comparator.comparing(card -> card.getDistrict().getCost()))
                .orElseGet(() -> super.keepOneDiscardOthers(pickedCards)));
    }


    /**
     * Chooses a card to play based on the current character and available cards in hand.
     * Prioritizes playing cards matching the character's target district type or the most expensive card otherwise.
     *
     * @return An optional containing the chosen card to play.
     */
    @Override
    protected Optional<Card> chooseCard() {
        List<Card> notAlreadyPlayedCardList = this.getHand().stream().filter(cardHand -> this.getCitadel().stream().noneMatch(cardCitadel -> cardHand.getDistrict().equals(cardCitadel.getDistrict()))).toList();
        Optional<Card> cardToPlay;
        if (this.character instanceof CommonCharacter commonCharacter) {
            DistrictType target = commonCharacter.getTarget();
            cardToPlay = notAlreadyPlayedCardList.stream()
                    .filter(card -> card.getDistrict().getDistrictType().equals(target))
                    .max(Comparator.comparing(card -> card.getDistrict().getCost()));
        } else {
            cardToPlay = this.getMostExpensiveCard(notAlreadyPlayedCardList);
        }
        if (cardToPlay.isPresent() && this.canPlayCard(cardToPlay.get())) {
            return cardToPlay;
        } else {
            return this.getMostExpensiveCard(notAlreadyPlayedCardList);
        }
    }

    /**
     * Gets the most expensive card from a list of cards.
     *
     * @param notAlreadyPlayedCardList The list of cards to choose from.
     * @return An optional containing the most expensive card.
     */
    protected Optional<Card> getMostExpensiveCard(List<Card> notAlreadyPlayedCardList) {
        return notAlreadyPlayedCardList.stream().max(Comparator.comparing(card -> card.getDistrict().getCost()));
    }

    @Override
    public String toString() {
        return "Le bot builder " + this.id;
    }
}
