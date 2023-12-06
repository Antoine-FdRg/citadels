package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.Cli;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int NB_GOLD_INIT = 30;
    private static final int NB_CARD_BY_PLAYER = 4;
    private static final int NB_ROUND = 4;
    Deck deck;
    List<Player> players;

    Cli view;

    public Game(int nbPlayers) {
        view = new Cli();
        this.deck = new Deck();
        players = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            players.add(new Player(NB_GOLD_INIT));
        }
        this.init();
    }

    public void run() {
        boolean isGameFinished = false;
        int round = 0;
        while (!isGameFinished && round < NB_ROUND) {
            for (Player player : players) {
                player.play();
            }
            isGameFinished = players.stream().allMatch(player -> player.getHand().isEmpty());
            round++;
        }
        view.displayWinner(getWinner().toString());
    }


    private void init() {
        dealCards();
    }

    private void dealCards() {
        for (int i = 0; i < NB_CARD_BY_PLAYER; i++) {
            for (Player player : players) {
                player.getHand().add(deck.pick());
            }
        }
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
