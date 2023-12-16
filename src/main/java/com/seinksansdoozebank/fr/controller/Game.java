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
import java.util.Optional;

public class Game {
    private static final int NB_GOLD_INIT = 2;
    private static final int NB_CARD_BY_PLAYER = 4;
    private final Deck deck;
    private List<Player> players;
    Optional<Player> kingPlayer;
    private List<Character> availableCharacters;
    private final IView view;

    public Game(int nbPlayers) {
        this.view = new Cli();
        this.deck = new Deck();
        this.players = new ArrayList<>();
        players.add(new SmartBot(NB_GOLD_INIT, this.deck, this.view));
        for (int i = 0; i < nbPlayers-1; i++) {
            players.add(new RandomBot(NB_GOLD_INIT, this.deck, this.view));
        }
        kingPlayer = Optional.empty();
    }

    public void run() {
        this.init();
        boolean isGameFinished = false;
        int round = 0;
        while (!isGameFinished) {
            view.displayRound(round + 1);
            orderPlayerBeforeChoosingCharacter();
            createCharacters();
            for (Player player : players) {
                kingPlayer = player.isTheKing()? Optional.of(player) : Optional.empty();
                player.play();
                this.removeCharacter(player.chooseCharacter(availableCharacters));
            }
            isGameFinished = players.stream().anyMatch(player -> player.getCitadel().size() > 7);
            round++;
        }
        view.displayWinner(getWinner());
    }

    /**
     * Order the players before choosing a character if a
     * player revealed himself being the king during the last round
     */
    private void orderPlayerBeforeChoosingCharacter() {
        if(kingPlayer.isPresent()) {
            List<Player> orderedPlayers = new ArrayList<>();
            //récupération de l'index du roi dans la liste des joueurs
            int indexOfTheKingPlayer = players.indexOf(kingPlayer.get());
            for (int i = indexOfTheKingPlayer; i < players.size(); i++) {
                //ajout des joueurs dans l'ordre à partir du roi en les décalant de l'index du roi
                orderedPlayers.add((i - indexOfTheKingPlayer) % players.size(), players.get(i));
            }
            players = orderedPlayers;
        }
    }


    private void init() {
        dealCards();
    }

    void createCharacters() {
        availableCharacters = new ArrayList<>();
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
