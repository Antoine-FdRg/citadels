package com.seinksansdoozebank.fr.model.player;
import com.seinksansdoozebank.fr.model.cards.District;

import java.util.List;

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
    private boolean isStuck = false;

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
        int cnt = 0;
        District district = this.chooseDistrict();
        while (district.getCost() > this.nbGold && cnt < 5) {
            district = this.chooseDistrict();
            cnt++;
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

    public boolean isStuck() {
        return isStuck;
    }

    public int getScore() {
        //calcule de la somme du cout des quartiers de la citadelle
        return citadel.stream().mapToInt(District::getCost).sum();
    }
}
