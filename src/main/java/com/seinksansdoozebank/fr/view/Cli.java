package com.seinksansdoozebank.fr.view;

public class Cli implements IView {

    public void displayWinner(String winnerName, int score) {
        System.out.println("Le gagnant est " + winnerName + " avec un score de " + score);
    }
}
