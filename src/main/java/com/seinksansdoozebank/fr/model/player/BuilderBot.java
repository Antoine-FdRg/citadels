package com.seinksansdoozebank.fr.model.player;

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

public class BuilderBot extends SmartBot {

    public BuilderBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

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
     * Choose q districtType of Nobility or Commerce and Crafts
     * If none of the above, choose the cheapest one
     *
     * @param pickedCards the cards picked
     * @return the card to keep
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


    @Override
    protected Optional<Card> chooseCard() {
        List<Card> notAlreadyPlayedCardList = this.getHand().stream().filter(d -> !this.getCitadel().contains(d)).toList();
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

    protected Optional<Card> getMostExpensiveCard(List<Card> notAlreadyPlayedCardList) {
        return notAlreadyPlayedCardList.stream().max(Comparator.comparing(card -> card.getDistrict().getCost()));
    }

    @Override
    protected void useEffectOfTheArchitect() {
        int numberOfCardsNeededToFinishTheGame = 8 - this.getCitadel().size();
        int nbDistrictsCanBeBuild = this.getNbDistrictsCanBeBuild();
        if (this.getCitadel().size() >= 5 && this.getHand().size() >= 3 && getPriceOfNumbersOfCheaperCards(numberOfCardsNeededToFinishTheGame) >= this.getNbGold()) {
            this.buyXCardsAndAddThemToCitadel(nbDistrictsCanBeBuild);
        } else {
            Optional<Card> prestigeCard = this.getHand().stream().filter(card -> card.getDistrict().getDistrictType() == DistrictType.PRESTIGE).findFirst();
            if (prestigeCard.isPresent() && canPlayCard(prestigeCard.get())) {
                buyACardAndAddItToCitadel(prestigeCard.get());
            } else {
                this.buyXCardsAndAddThemToCitadel(nbDistrictsCanBeBuild);
            }
        }
    }

    @Override
    public String toString() {
        return "Le bot builder " + this.id;
    }
}
