package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.District;

import java.util.List;
import java.util.Random;

public class Player {
    int id;
    int nbGold;
    List<District> hand;
    List<District> citadel;
    Random random = new Random();

    public Player(int id, int nbGold, List<District> hand, List<District> citadel) {
        this.id = id;
        this.nbGold = nbGold;
        this.hand = hand;
        this.citadel = citadel;
    }

    District chooseDistrict() {
        return this.hand.get(random.nextInt(hand.size()));
    }

    public District play() {
        District district = this.chooseDistrict();
        this.hand.remove(district);
        this.citadel.add(district);
        this.updateGold(district.getCost());
        return district;
    }

    void updateGold(int gold) {
        this.nbGold -= gold;
    }
}
