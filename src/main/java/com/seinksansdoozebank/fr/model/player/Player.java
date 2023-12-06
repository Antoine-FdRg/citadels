package com.seinksansdoozebank.fr.model.player;
import com.seinksansdoozebank.fr.model.cards.District;

import java.util.List;

public class Player {
    int id;
    int nbGold;
    List<District> hand;
    List<District> citadel;

    public int getScore(){
        //calcule de la somme du cout des quartiers de la citadelle
        return citadel.stream().mapToInt(District::getCost).sum();
    }

    public Player(int nbGold) {
        this.nbGold = nbGold;
    }
}
