package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
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

    boolean findFirstPlayerWithEightDistricts = false;
    private final Deck deck;
    private List<Player> players;
    private List<Character> availableCharacters;
    private final IView view;

    public Game(int nbPlayers) {
        this.view = new Cli();
        this.deck = new Deck();
        this.players = new ArrayList<>();
        players.add(new SmartBot(NB_GOLD_INIT, this.deck, this.view));
        for (int i = 0; i < nbPlayers - 1; i++) {
            players.add(new RandomBot(NB_GOLD_INIT, this.deck, this.view));
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
                player.play();
                //We set the attribute to true if player is the first who has eight districts
                isTheFirstOneToHaveEightDistricts(player);
                this.removeCharacter(player.chooseCharacter(availableCharacters));
            }
            isGameFinished = players.stream().anyMatch(player -> player.getCitadel().size() > 7);
            round++;
        }
        //we add bonus to player who has specific citadel
        findBonusOfPlayer();
        view.displayWinner(getWinner());
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

    /**
     * Method which sets the attribute isFirstToHaveEightDistrict at true if it's the case
     *
     * @param player who added a card to his deck
     */
    public void isTheFirstOneToHaveEightDistricts(Player player) {
        if (player.getCitadel().size() == 8 && !findFirstPlayerWithEightDistricts) {
            //we mark the bot as true if it is first to have 8 districts
            player.setIsFirstToHaveEightDistricts();
            findFirstPlayerWithEightDistricts = true;
        }
    }

    /**
     * This method adds bonus to the attribute bonus of each player of the game if they have got one
     */
    public void findBonusOfPlayer() {
        for (Player player : players) {
            if (hasFiveDifferentDistrictTypes(player)) {
                player.addBonus(3);
            }
            if (player.getCitadel().size() == 8) {
                if (player.getIsFirstToHaveEightDistricts()) {
                    player.addBonus(2);
                }
                player.addBonus(2);
            }
        }
    }

    /**
     * if the bot has got in its citadel 5 different types of districts it returns true else return false
     *
     * @param player Player
     * @return a boolean
     */
    public boolean hasFiveDifferentDistrictTypes(Player player) {
        List<DistrictType> listDifferentDistrictType = new ArrayList<>();
        for (Card card : player.getCitadel()) {
            if (!listDifferentDistrictType.contains(card.getDistrict().getDistrictType())) {
                listDifferentDistrictType.add(card.getDistrict().getDistrictType());
            }
        }
        return (listDifferentDistrictType.size() == 5);
    }
}
