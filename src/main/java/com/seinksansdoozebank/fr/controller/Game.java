package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public class Game {
    Deck deck;
    List<Player> players;

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void run() {
        //TODO issue #5
    }

    private void init() {
        //TODO issue #3
    }

    protected Player getWinner() {
        Player bestPlayer = players.get(0);
        for (Player currentPlayer : players) {
            if (currentPlayer.getScore() > bestPlayer.getScore()) {
                bestPlayer = currentPlayer;
            }
        }
        return bestPlayer;
    }
}
