package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.SmartBot;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Game {
    private static final int NB_PLAYER_MAX = 6;
    private static final int NB_PLAYER_MIN = 3;
    private static final int NB_GOLD_INIT = 2;
    private static final int NB_CARD_BY_PLAYER = 4;

    private boolean findFirstPlayerWithEightDistricts = false;
    private final Deck deck;
    protected List<Player> players;
    Player crownedPlayer;
    private final List<Character> availableCharacters;
    private final IView view;
    private int nbCurrentRound;
    private boolean finished;

    /**
     * Constructor of the Game class
     *
     * @param nbPlayers the number of players playing
     */
    public Game(int nbPlayers, IView view) {
        if (nbPlayers > NB_PLAYER_MAX || nbPlayers < NB_PLAYER_MIN) {
            throw new IllegalArgumentException("The number of players must be between " + NB_PLAYER_MIN + " and " + NB_PLAYER_MAX);
        }
        this.view = view;
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
        crownedPlayer = null;
        this.finished = false;
    }

    /**
     * Run the game
     */
    public void run() {
        this.init();
        this.nbCurrentRound = 1;
        while (!finished) {
            this.playARound();
        }
        view.displayGameFinished();
        updatePlayersBonus();
        view.displayWinner(this.getWinner());
    }

    /**
     * Play a round
     */
    protected void playARound() {
        view.displayRound(nbCurrentRound);
        orderPlayerBeforeChoosingCharacter();
        playersChooseCharacters();
        orderPlayerBeforePlaying();
        for (Player player : players) {
            if (!player.getCharacter().isDead()) {
                this.updateCrownedPlayer(player);
                checkPlayerStolen(player);
                player.play();
            }
            //We set the attribute to true if player is the first who has eight districts
            isTheFirstOneToHaveEightDistricts(player);
        }
        retrieveCharacters();
        finished = players.stream().anyMatch(player -> player.getCitadel().size() > 7);
        this.nbCurrentRound++;
    }

    void updateCrownedPlayer(Player player) {
        crownedPlayer = player.getCharacter().getRole().equals(Role.KING) ? player : crownedPlayer;
    }

    protected int getNbCurrentRound() {
        return nbCurrentRound;
    }

    void orderPlayerBeforePlaying() {
        players.sort(Comparator.comparing(player -> player.getCharacter().getRole()));
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
        players.sort(Comparator.comparing(Player::getId));
        if (crownedPlayer != null) {
            List<Player> orderedPlayers = new ArrayList<>();
            //récupération de l'index du roi dans la liste des joueurs
            int indexOfTheKingPlayer = players.indexOf(crownedPlayer);
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
    protected void init() {
        dealCards();
        createCharacters();
    }

    /**
     * Create the list of characters ordered
     */
    protected void createCharacters() {
        int nbPlayers = this.players.size();
        List<Character> notMandatoryCharacters = new ArrayList<>(List.of(
                new Assassin(),
                new Bishop(),
                new Merchant(),
                new Architect(),
                new Thief(),
                new Condottiere()));
        if (nbPlayers > notMandatoryCharacters.size()) {
            throw new UnsupportedOperationException("The number of players is too high for the number of characters implemented");
        }
        Collections.shuffle(notMandatoryCharacters);
        // the king must always be available
        availableCharacters.add(new King());
        //adding as much characters as there are players because the king is already added and the rules say that the number of characters must be equal to the number of players +1
        for (int i = 0; i < nbPlayers-1; i++) {
            availableCharacters.add(notMandatoryCharacters.get(i));
        }
        //remove the characters that are available from the list of not mandatory characters
        notMandatoryCharacters.removeAll(availableCharacters);
        //display the characters that are not in availableCharacters
        for (Character unusedCharacter : notMandatoryCharacters) {
            view.displayUnusedCharacterInRound(unusedCharacter);
        }
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
     *
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
     *
     * @param players the list of players
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Get the characters that are still available.
     *
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
            if (player.hasCourtyardOfMiracleAndItsNotTheLastCard()) {
                player.chooseColorCourtyardOfMiracle();
            }
            if (player.hasFiveDifferentDistrictTypes()) {
                player.addBonus(3);
                view.displayPlayerGetBonus(player, 3, "5 quartiers de types différents");
            }
            if (player.getCitadel().size() == 8) {
                if (player.getIsFirstToHaveEightDistricts()) {
                    player.addBonus(2);
                    view.displayPlayerGetBonus(player, 2, "premier joueur a atteindre 8 quartiers");
                }
                player.addBonus(2);
                view.displayPlayerGetBonus(player, 2, "8 quartiers");
            }
            view.displayPlayerScore(player);
        }
    }




    /**
     * @param role
     * @return an optional of Player with the given role
     */
    public Optional<Player> getPlayerByRole(Role role) {
        for (Player player : players) {
            if (player.getCharacter().getRole() == role) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }


    /**
     * we apply this if the player has savedThief==true
     *
     * @param player
     * @return a boolean
     */
    public void checkPlayerStolen(Player player) {
        if (player.getCharacter().getSavedThief() != null) {
            view.displayStolenCharacter(player.getCharacter());
            player.getCharacter().isStolen();
            if (getPlayerByRole(Role.THIEF).isPresent()) {
                view.displayActualNumberOfGold(getPlayerByRole(Role.THIEF).get());
            }
        }
    }
}
