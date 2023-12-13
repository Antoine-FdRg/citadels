package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SmartBot extends Player {

    public SmartBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public Optional<District> play() {
        Optional<District> optChosenDistrict = this.chooseDistrict();
        if (optChosenDistrict.isPresent()) {
            District choosenDistrict = optChosenDistrict.get();
            if (this.canBuildDistrict(choosenDistrict)) {
                Optional<District> districtToBuild = this.buildADistrict();//build the district given by chooseDistrict
                this.pickSomething(); //the version where we already choose the district
                return districtToBuild;
            } else {
                this.pickGold(); //
                if (this.canBuildDistrict(choosenDistrict)) {
                    return this.buildADistrict();
                }
                return Optional.empty();
            }
        } else {//la main est vide
            this.pickADistrict(); //
            optChosenDistrict = this.chooseDistrict();
            if (optChosenDistrict.isPresent()) {
                District cheaperDistrict = optChosenDistrict.get();
                return this.buildADistrict();
            }
            return Optional.empty();
        }
    }

    @Override
    protected void pickSomething() {
        Optional<District> optCheaperBuildableDistrict = this.chooseDistrict();
        if (optCheaperBuildableDistrict.isEmpty()) { //s'il n'y a pas de district le moins cher => la main est vide
            this.pickADistrict(); // => il faut piocher
        } else { //s'il y a un district le moins cher
            District cheaperDistrict = optCheaperBuildableDistrict.get();
            if (this.getNbGold() < cheaperDistrict.getCost()) { //si le joueur n'a pas assez d'or pour acheter le district le moins cher
                pickGold(); // => il faut piocher de l'or
            } else { //si le joueur a assez d'or pour construire le district le moins cher
                this.pickADistrict(); // => il faut piocher un quartier pour savoir combien d'or sera nécessaire
            }
        }
    }

    @Override
    protected void pickADistrict() {
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
    private Optional<District> getCheaperDistrict(List<District> notBuiltDistrictList) {
        return notBuiltDistrictList.stream().min(Comparator.comparing(District::getCost));
    }
}
