package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
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

    private boolean findFirstPlayerWithEightDistricts = false;
    private final Deck deck;
    protected List<Player> players;
    private Optional<Player> kingPlayer;
    private final List<Character> availableCharacters;
    private final IView view;

    /**
     * Constructor of the Game class
     * @param nbPlayers the number of players playing
     */
    public Game(int nbPlayers) {
        this.view = new Cli();
        this.deck = new Deck();
        this.players = new ArrayList<>();
        players.add(new SmartBot(NB_GOLD_INIT, this.deck, this.view));
        for (int i = 0; i < nbPlayers - 1; i++) {
            players.add(new RandomBot(NB_GOLD_INIT, this.deck, this.view));
        }
        for (Player player : players) {
            List<Player> opponents = new ArrayList<>(players);
            opponents.remove(player);
            player.setOpponents(opponents);
        }
        availableCharacters = new ArrayList<>();
        kingPlayer = Optional.empty();
    }

    /**
     * Run the game
     */
    public void run() {
        this.init();
        boolean isGameFinished = false;
        int round = 1;
        while (!isGameFinished) {
            view.displayRound(round);
            playersChooseCharacters();
            orderPlayerBeforeChoosingCharacter();
            for (Player player : players) {
                if (player.getCharacter().isDead()) {
                    continue;
                }
                kingPlayer = player.isTheKing() ? Optional.of(player) : Optional.empty();
                player.play();
                //We set the attribute to true if player is the first who has eight districts
                isTheFirstOneToHaveEightDistricts(player);
            }
            retrieveCharacters();
            isGameFinished = players.stream().anyMatch(player -> player.getCitadel().size() > 7);
            round++;
        }
        //we add bonus to player who has specific citadel
        updatePlayersBonus();
        view.displayWinner(this.getWinner());
    }

    /**
     * Retrieve the characters from the players
     */
    void retrieveCharacters() {
        for (Player player : players) {
            availableCharacters.add(player.retrieveCharacter());
        }
    }

    /**
     * Order the players before choosing a character if a
     * player revealed himself being the king during the last round
     */
    void orderPlayerBeforeChoosingCharacter() {
        if (kingPlayer.isPresent()) {
            List<Player> orderedPlayers = new ArrayList<>();
            //récupération de l'index du roi dans la liste des joueurs
            int indexOfTheKingPlayer = players.indexOf(kingPlayer.get());
            for (int i = indexOfTheKingPlayer; i < players.size(); i++) {
                orderedPlayers.add((i - indexOfTheKingPlayer) % players.size(), players.get(i));
            }
            for (int i = 0; i < indexOfTheKingPlayer; i++) {
                orderedPlayers.add((i + players.size() - indexOfTheKingPlayer) % players.size(), players.get(i));
            }
            players = orderedPlayers;
        }
    }

    /**
     * Ask the player to choose their characters
     */
    protected void playersChooseCharacters() {
        for (Player player : players) {
            availableCharacters.remove(player.chooseCharacter(availableCharacters));
        }
    }

    /**
     * Initialize the game
     */
    private void init() {
        dealCards();
        createCharacters();
    }

    /**
     * Create the list of characters ordered
     */
    protected void createCharacters() {
        // availableCharacters.add(new Assassin());
        availableCharacters.add(new King());
        availableCharacters.add(new Bishop());
        availableCharacters.add(new Merchant());
        // availableCharacters.add(new Architect());
        availableCharacters.add(new Condottiere());
    }

    /**
     * Deal the cards to the players
     */
    private void dealCards() {
        for (int i = 0; i < NB_CARD_BY_PLAYER; i++) {
            for (Player player : players) {
                player.getHand().add(deck.pick());
            }
        }
    }

    /**
     * Get the winner of the game
     * @return The player who win the game
     */
    protected Player getWinner() {
        Player bestPlayer = players.get(0);
        for (Player currentPlayer : players) {
            if (currentPlayer.getScore() > bestPlayer.getScore()) {
                bestPlayer = currentPlayer;
            }
        }
        return bestPlayer;
    }

    /**
     * Set the list of players (For Test ONLY)
     * @param players the list of players
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Get the characters that are still available.
     * @return the list of characters available
     */
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
    public void updatePlayersBonus() {
        for (Player player : players) {
            // Check if the player contain the district COURTYARD_OF_MIRACLE
            if (player.getCitadel().stream().anyMatch(card -> card.getDistrict().getName().equals("Cour des miracles"))) {
                player.chooseColorCourtyardOfMiracle();
            }
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
