package com.seinksansdoozebank.fr.model.player;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;

import java.util.List;

import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public abstract class Player {
    private static int counter = 1;
    protected final int id;
    private int nbGold;
    protected Deck deck;
    protected final List<District> hand;
    private final List<District> citadel;
    protected final IView view;
    protected final Random random = new Random();
    protected Player(int nbGold, Deck deck, IView view) {
        this.id = counter++;
        this.nbGold = nbGold;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.citadel = new ArrayList<>();
        this.view = view;
    }

    /**
     * Represents the player's turn
     * MUST CALL view.displayPlayerPlaysDistrict() at the end of the turn with the district built by the player
     */
    public abstract void play();

    /**
     * Represents the player's choice between drawing 2 gold coins or a district
     */
    protected abstract void pickSomething();

    /**
     * Represents the player's choice to draw 2 gold coins
     */
    protected final void pickGold() {
        view.displayPlayerPicksGold(this);
        this.nbGold+=2;
    }

    /**
     * Represents the player's choice to draw 2 districts keep one and discard the other one
     * MUST CALL this.hand.add() AND this.deck.discard() AT EACH CALL
     */
    protected abstract void pickTwoDistrictKeepOneDiscardOne();

    /**
     * Represents the phase where the player build a district chosen by chooseDistrict()
     * @return the district built by the player
     */
    protected final Optional<District> buildADistrict() {
        Optional<District> optChosenDistrict = chooseDistrict();
        if (optChosenDistrict.isEmpty()|| !canBuildDistrict(optChosenDistrict.get())) {
            return Optional.empty();
        }
        District chosenDistrict = optChosenDistrict.get();
        this.hand.remove(chosenDistrict);
        this.citadel.add(chosenDistrict);
        this.decreaseGold(chosenDistrict.getCost());
        return optChosenDistrict;
    }

    /**
     * Choose a district to build from the hand
     * Is automatically called in buildADistrict() to build the choosen district if canBuildDistrict(<choosenDistrcit>) is true
     * @return the district to build
     */
    protected abstract Optional<District> chooseDistrict();

    /**
     * Verify if the player can build the district passed in parameter (he can build it if he has enough gold and if he doesn't already have it in his citadel)
     * @param district the district to build
     * @return true if the player can build the district passed in parameter, false otherwise
     */
    protected final boolean canBuildDistrict(District district) {
        return district.getCost() <= this.nbGold && !this.citadel.contains(district);
    }

    protected final void decreaseGold(int gold) {
        this.nbGold -= gold;
    }

    public List<District> getHand() {
        return this.hand;
    }

    public List<District> getCitadel() {
        return this.citadel;
    }

    public int getNbGold() {
        return this.nbGold;
    }

    public int getId() {
        return this.id;
    }

    public static void resetIdCounter() {
        counter = 1;
    }

    public final int getScore() {
        //calcule de la somme du cout des quartiers de la citadelle
        return citadel.stream().mapToInt(District::getCost).sum();
    }

    @Override
    public String toString() {
        return "Le joueur "+this.id;
    }
}
