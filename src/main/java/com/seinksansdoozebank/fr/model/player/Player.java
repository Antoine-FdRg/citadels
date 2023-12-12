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
    public Player(int nbGold, Deck deck, IView view) {
        this.id = counter++;
        this.nbGold = nbGold;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.citadel = new ArrayList<>();
        this.deck = new Deck();
        this.view = view;
    }

    /**
     * Represents the player's turn
     * @return the district built by the player
     */
    public abstract Optional<District> play();

    /**
     * Represents the player's choice between drawing 2 gold coins or a district
     */
    protected abstract void pickSomething();

    /**
     * Represents the player's choice to draw 2 gold coins
     */
    protected void pickGold() {
        view.displayPlayerPicksGold(this);
        this.nbGold+=2;
    }

    /**
     * Represents the player's choice to draw 2 districts and keep one
     */
    protected abstract void pickADistrict();

    protected District buildADistrict() {
        District districtToBuild = chooseDistrict();
        this.hand.remove(districtToBuild);
        this.citadel.add(districtToBuild);
        this.decreaseGold(districtToBuild.getCost());
        return districtToBuild;
    }

    protected abstract District chooseDistrict();

    protected boolean canBuildDistrict(District district) {
        //TODO verifier que le contains fait ce que je veux
        return district.getCost() <= this.nbGold && !this.citadel.contains(district);
    }

    void decreaseGold(int gold) {
        this.nbGold -= gold;
    }

    public void addDistrictToHand(District district) {
        this.hand.add(district);
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

    public int getScore() {
        //calcule de la somme du cout des quartiers de la citadelle
        return citadel.stream().mapToInt(District::getCost).sum();
    }

    @Override
    public String toString() {
        return "Le joueur "+this.id;
    }
}
