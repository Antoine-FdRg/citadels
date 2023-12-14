package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        view.displayPlayerInfo(this);
        Optional<District> optChosenDistrict = this.chooseDistrict();
        if (optChosenDistrict.isPresent()) {
            District choosenDistrict = optChosenDistrict.get();
            if (this.canBuildDistrict(choosenDistrict)) {
                view.displayPlayerBuildDistrict(this, this.buildADistrict());
                this.pickSomething();
            } else {
                this.pickGold();
                view.displayPlayerBuildDistrict(this, this.buildADistrict());
            }
        } else {//la main est vide
            this.pickTwoDistrictKeepOneDiscardOne(); //
            view.displayPlayerBuildDistrict(this, this.buildADistrict());
        }
        view.displayPlayerInfo(this);
    }

    @Override
    protected void pickSomething() {
        Optional<District> optCheaperBuildableDistrict = this.chooseDistrict();
        if (optCheaperBuildableDistrict.isEmpty()) { //s'il n'y a pas de district le moins cher => la main est vide
            this.pickTwoDistrictKeepOneDiscardOne(); // => il faut piocher
        } else { //s'il y a un district le moins cher
            District cheaperDistrict = optCheaperBuildableDistrict.get();
            if (this.getNbGold() < cheaperDistrict.getCost()) { //si le joueur n'a pas assez d'or pour acheter le district le moins cher
                this.pickGold(); // => il faut piocher de l'or
            } else { //si le joueur a assez d'or pour construire le district le moins cher
                this.pickTwoDistrictKeepOneDiscardOne(); // => il faut piocher un quartier pour savoir combien d'or sera nécessaire
            }
        }
    }

    @Override
    protected void pickTwoDistrictKeepOneDiscardOne() {
        this.view.displayPlayerPickDistrict(this);
        //Pick two district
        District district1 = this.deck.pick();
        District district2 = this.deck.pick();
        //Keep the cheaper one and discard the other one
        if (district1.getCost() < district2.getCost()) {
            this.hand.add(district1);
            this.deck.discard(district2);
        } else {
            this.hand.add(district2);
            this.deck.discard(district1);
        }
    }

    @Override
    protected Optional<District> chooseDistrict() {
        //Gathering districts wich are not already built in player's citadel
        List<District> notBuiltDistrictList = this.hand.stream().filter(d -> !this.getCitadel().contains(d)).toList();
        //Choosing the cheaper one
        return this.getCheaperDistrict(notBuiltDistrictList);
    }

    /**
     * Returns the cheaper district in the hand if there is one or an empty optional
     *
     * @return the cheaper district in the hand if there is one or an empty optional
     */
    protected Optional<District> getCheaperDistrict(List<District> notBuiltDistrictList) {
        return notBuiltDistrictList.stream().min(Comparator.comparing(District::getCost));
    }

    @Override
    public String toString() {
        return "Le bot malin " + this.id;
    }

}
