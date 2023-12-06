package com.seinksansdoozebank.fr.view;

public class Cli implements IView {

    public void displayWinner(String winnerName) {
        System.out.println("Le gagnant est " + winnerName);
    }
}
