package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

public class Cli implements IView {

    public void displayDistrict(Player player, District district) {
        System.out.println("Player " + player.getId() + " pose un " + district.getName() + " qui lui coute " + district.getCost() + " il lui reste " + player.getNbGold() + " pièces d'or");
    }

    public void displayWinner(String winnerName, int score) {
        System.out.println("Le joueur " + winnerName + " gagne avec un score de " + score);
    }

    public void displayRound(int round) {
        System.out.println("Début du round " + round);
    }
}
