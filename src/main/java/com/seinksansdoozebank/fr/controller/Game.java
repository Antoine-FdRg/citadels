package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commonCharacters.King;
import com.seinksansdoozebank.fr.model.character.commonCharacters.Merchant;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.SmartBot;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int NB_GOLD_INIT = 2;
    private static final int NB_CARD_BY_PLAYER = 4;
    private final Deck deck;
    private List<Player> players;
    private final List<Character> availableCharacters;
    private final IView view;

    public Game(int nbPlayers) {
        this.view = new Cli();
        this.deck = new Deck();
        this.players = new ArrayList<>();
        players.add(new SmartBot(NB_GOLD_INIT, this.deck, this.view));
        for (int i = 0; i < nbPlayers-1; i++) {
            players.add(new RandomBot(NB_GOLD_INIT, this.deck, this.view));
        }
        availableCharacters  = new ArrayList<>();
    }

    public void run() {
        this.init();
        boolean isGameFinished = false;
        int round = 0;
        while (!isGameFinished) {
            view.displayRound(round + 1);
            for (Player player : players) {
                player.play();
                this.removeCharacter(player.chooseCharacter(availableCharacters));
            }
            retrieveCharacters();
            isGameFinished = players.stream().anyMatch(player -> player.getCitadel().size() > 7);
            round++;
        }
        view.displayWinner(getWinner());
    }

    /**
     * Retrieve the characters from the players
     */
    private void retrieveCharacters() {
        for (Player player : players) {
            availableCharacters.add(player.retrieveCharacter());
        }
    }


    private void init() {
        dealCards();
        createCharacters();
    }

    protected void createCharacters() {
        availableCharacters.add(new Bishop());
        availableCharacters.add(new King());
        availableCharacters.add(new Merchant());
        availableCharacters.add(new Condottiere());
    }

    private void removeCharacter(Character character) {
        availableCharacters.remove(character);
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

    public List<Character> getAvailableCharacters() {
        return availableCharacters;
    }
}
