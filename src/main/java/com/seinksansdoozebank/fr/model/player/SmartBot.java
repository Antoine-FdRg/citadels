package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a smart bot which will try to build the cheaper district
 * in its hand in order to finish its citadel as fast as possible
 */
public class SmartBot extends Player {

    public SmartBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public void play() {
        view.displayPlayerStartPlaying(this);
        view.displayPlayerRevealCharacter(this);
        view.displayPlayerInfo(this);
        Optional<Card> optChosenCard = this.chooseCard();
        if (optChosenCard.isPresent()) {
            Card choosenCard = optChosenCard.get();
            if (this.canPlayCard(choosenCard)) {
                view.displayPlayerPlaysCard(this, this.playACard());
                this.character.useEffect();
                this.pickSomething();
            } else {
                this.character.useEffect();
                if (this.canPlayCard(choosenCard)) {
                    view.displayPlayerPlaysCard(this, this.playACard());
                } else {
                    this.pickGold();
                    view.displayPlayerPlaysCard(this, this.playACard());
                }
            }
        } else {//la main est vide
            this.pickTwoCardKeepOneDiscardOne(); //
            view.displayPlayerPlaysCard(this, this.playACard());
        }
        view.displayPlayerInfo(this);
    }

    @Override
    protected void pickSomething() {
        Optional<Card> optCheaperPlayableCard = this.chooseCard();
        if (optCheaperPlayableCard.isEmpty()) { //s'il n'y a pas de district le moins cher => la main est vide
            this.pickTwoCardKeepOneDiscardOne(); // => il faut piocher
        } else { //s'il y a un district le moins cher
            Card cheaperCard = optCheaperPlayableCard.get();
            if (this.getNbGold() < cheaperCard.getDistrict().getCost()) { //si le joueur n'a pas assez d'or pour acheter le district le moins cher
                this.pickGold(); // => il faut piocher de l'or
            } else { //si le joueur a assez d'or pour construire le district le moins cher
                this.pickTwoCardKeepOneDiscardOne(); // => il faut piocher un quartier pour savoir combien d'or sera nécessaire
            }
        }
    }

    @Override
    protected void pickTwoCardKeepOneDiscardOne() {
        this.view.displayPlayerPickCard(this);
        //Pick two district
        Card card1 = this.deck.pick();
        Card card2 = this.deck.pick();
        //Keep the cheaper one and discard the other one
        if (card1.getDistrict().getCost() < card2.getDistrict().getCost()) {
            this.hand.add(card1);
            this.deck.discard(card2);
        } else {
            this.hand.add(card2);
            this.deck.discard(card1);
        }
    }

    @Override
    protected Optional<Card> chooseCard() {
        // search for a card in the hand that is the same as the character's target
        if (this.character instanceof CommonCharacter commonCharacter) {
            DistrictType target = commonCharacter.getTarget();
            Optional<Card> optCard = this.hand.stream()
                    .filter(card -> card.getDistrict().getDistrictType() == target)
                    .min(Comparator.comparing(card -> card.getDistrict().getCost()));
            if (optCard.isPresent()) {
                return optCard;
            }
        }
        //Gathering districts wich are not already built in player's citadel
        List<Card> notAlreadyPlayedCardList = this.hand.stream().filter(d -> !this.getCitadel().contains(d)).toList();
        //Choosing the cheaper one
        return this.getCheaperCard(notAlreadyPlayedCardList);
    }

    /**
     * Returns the cheaper district in the hand if there is one or an empty optional
     *
     * @return the cheaper district in the hand if there is one or an empty optional
     */
    protected Optional<Card> getCheaperCard(List<Card> notAlreadyPlayedCardList) {
        return notAlreadyPlayedCardList.stream().min(Comparator.comparing(card -> card.getDistrict().getCost()));
    }

    @Override
    public void chooseCharacter(List<Character> characters) {
        // Choose the character by getting the frequency of each districtType in the citadel
        // and choosing the districtType with the highest frequency for the character

        List<DistrictType> districtTypeFrequencyList = getDistrictTypeFrequencyList(this.getCitadel());
        if (!districtTypeFrequencyList.isEmpty()) {
            // Choose the character with the mostOwnedDistrictType
            for (DistrictType districtType : districtTypeFrequencyList) {
                for (Character character : characters) {
                    if (character instanceof CommonCharacter commonCharacter) {
                        if (commonCharacter.getTarget() == districtType) {
                            this.character = commonCharacter;
                            this.character.setPlayer(this);
                            return;
                        }
                    }
                }
            }
        }
        // If no character has the mostOwnedDistrictType, choose a random character
        this.character = characters.get(random.nextInt(characters.size()));
        this.character.setPlayer(this);
    }

    /**
     * Returns a list of districtType sorted by frequency in the citadel
     *
     * @param citadel the citadel of the player
     * @return a list of districtType sorted by frequency in the citadel
     */
    protected List<DistrictType> getDistrictTypeFrequencyList(List<Card> citadel) {
        return citadel.stream()
                .map(card -> card.getDistrict().getDistrictType())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public String toString() {
        return "Le bot malin " + this.id;
    }

}
