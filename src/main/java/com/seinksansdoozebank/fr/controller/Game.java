package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.singleton.Bishop;
import com.seinksansdoozebank.fr.model.character.singleton.Condottiere;
import com.seinksansdoozebank.fr.model.character.singleton.King;
import com.seinksansdoozebank.fr.model.character.singleton.Merchant;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int NB_GOLD_INIT = 30;
    private static final int NB_CARD_BY_PLAYER = 4;
    private final Deck deck;
    private List<Player> players;
    private List<Character> characters;
    private final IView view;

    public Game(int nbPlayers) {
        this.view = new Cli();
        this.deck = new Deck();
        this.players = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            players.add(new Player(NB_GOLD_INIT, view));
        }
    }

    public void run() {
        this.init();
        boolean isGameFinished = false;
        int round = 0;
        while (!isGameFinished) {
            view.displayRound(round + 1);
            // Intialize characters
            createCharacters();
            for (Player player : players) {
                view.displayPlayerStartPlaying(player);
                // Choose character and remove it from the list
                this.removeCharacter(player.chooseCharacter(characters));
                District district = player.play();
                view.displayPlayerPlaysDistrict(player, district);
                view.displayPlayerInfo(player);
            }
            isGameFinished = players.stream().anyMatch(player -> player.getCitadel().size() > 7);
            round++;
        }
        view.displayWinner(getWinner().toString(), getWinner().getScore());
    }


    private void init() {
        dealCards();
    }

    void createCharacters() {
        characters = new ArrayList<>();
        characters.add(new Bishop());
        characters.add(new King());
        characters.add(new Merchant());
        characters.add(new Condottiere());
    }

    private void removeCharacter(Character character) {
        characters.remove(character);
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

    public List<Character> getCharacters() {
        return characters;
    }
}
