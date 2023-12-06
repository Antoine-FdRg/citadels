package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.District;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private static int counter = 1;
    private int id;
    private int nbGold;
    private final List<District> hand;
    private final List<District> citadel;
    private final Random random = new Random();

    public Player(int nbGold) {
        this.id = counter++;
        this.nbGold = nbGold;
        this.hand = new ArrayList<>();
        this.citadel = new ArrayList<>();
    }

    District chooseDistrict() {
        return this.hand.get(random.nextInt(hand.size()));
    }

    public District play() {
        District district = this.chooseDistrict();
        // TODO : Boucle infinie possible si le joueur n'a pas assez d'or pour acheter un district
        while (district.getCost() > this.nbGold) {
            district = this.chooseDistrict();
        }
        this.hand.remove(district);
        this.citadel.add(district);
        this.decreaseGold(district.getCost());
        return district;
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
}
