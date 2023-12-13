package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {
    private static final int NB_GOLD_INIT = 30;
    private static final int NB_CARD_BY_PLAYER = 4;
    private final Deck deck;
    private List<Player> players;

    IView view;

    public Game(int nbPlayers) {
        this.view = new Cli();
        this.deck = new Deck();
        this.players = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            players.add(new RandomBot(NB_GOLD_INIT, this.deck, this.view));
        }
    }

    public void run() {
        this.init();
        boolean isGameFinished = false;
        int round = 0;
        while (!isGameFinished) {
            view.displayRound(round + 1);
            for (Player player : players) {
                Optional<District> district = player.play();
                view.displayPlayerPlaysDistrict(player, district);
                view.displayPlayerInfo(player);
            }
            isGameFinished = players.stream().anyMatch(player -> player.getCitadel().size() > 7);
            round++;
        }
        view.displayWinner(getWinner());
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

    public void setPlayers(List<Player> players) {
        this.players = players;

    }
}
