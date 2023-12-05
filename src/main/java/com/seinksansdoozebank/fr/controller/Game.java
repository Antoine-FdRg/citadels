package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int NB_GOLD_INIT = 30;
    private static final int NB_CARD_BY_PLAYER = 4;
    Deck deck;
    List<Player> players;

    public Game(int nbPlayers) {
        this.deck = new Deck();
        players = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            players.add(new Player(NB_GOLD_INIT));
        }
        this.init();
    }

    public void run() {
        //TODO issue #5
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
}
