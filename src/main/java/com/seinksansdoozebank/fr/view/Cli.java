package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

public class Cli implements IView {

    public void displayDistrict(Player player, District district) {
        System.out.println("Player " + player.getId() + " pose un district " + district.getName() + " et lui coute " + district.getCost() + " il lui reste " + player.getNbGold());
    }
}
