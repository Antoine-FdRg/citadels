package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Game {
    protected static final int NB_PLAYER_MAX = 6;
    protected static final int NB_PLAYER_MIN = 4;
    private static final int NB_CARD_BY_PLAYER = 4;

    private boolean findFirstPlayerWithEightDistricts = false;
    private final Deck deck;
    protected List<Player> players;
    Player crownedPlayer;
    private List<Character> availableCharacters;
    private List<Character> charactersInTheRound;
    private final IView view;
    private int nbCurrentRound;
    private boolean finished;

    Game(IView view, Deck deck, List<Player> playerList) {
        if (playerList.size() > NB_PLAYER_MAX || playerList.size() < NB_PLAYER_MIN) {
            throw new IllegalArgumentException("The number of players must be between " + NB_PLAYER_MIN + " and " + NB_PLAYER_MAX);
        }
        this.view = view;
        this.deck = deck;
        this.players = playerList;
        this.availableCharacters = new ArrayList<>();
        this.crownedPlayer = null;
        this.finished = false;
    }

    /**
     * Run the game
     */
    public void run() {
        this.init();
        this.nbCurrentRound = 1;
        while (!finished) {
            createCharacters();
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
                player.setAvailableCharacters(charactersInTheRound);
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
    }

    /**
     * Create the list of characters ordered
     */
    protected void createCharacters() {
        int nbPlayers = this.players.size();
        availableCharacters = new ArrayList<>();
        List<Character> notMandatoryCharacters = new ArrayList<>(List.of(
                new Assassin(),
                new Thief(),
                new Magician(),
                new Bishop(),
                new Merchant(),
                new Architect(),
                new Condottiere()));
        if (nbPlayers + 1 > notMandatoryCharacters.size()) {
            throw new UnsupportedOperationException("The number of players is too high for the number of characters implemented");
        }
        Collections.shuffle(notMandatoryCharacters);
        // the king must always be available
        availableCharacters.add(new King());
        //adding as much characters as there are players because the king is already added and
        // the rules say that the number of characters must be equal to the number of players +1
        for (int i = 0; i < nbPlayers + 1; i++) {
            availableCharacters.add(notMandatoryCharacters.get(i));
        }
        charactersInTheRound = new ArrayList<>(availableCharacters);
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
     * @param role the role of the player we want to get
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
     * @param player player to check
     */
    public void checkPlayerStolen(Player player) {
        if (player.getCharacter().getSavedThief() != null) {
            player.getCharacter().isStolen();
            view.displayStolenCharacter(player.getCharacter());
            Optional<Player> playerByRole = getPlayerByRole(Role.THIEF);
            playerByRole.ifPresent(view::displayActualNumberOfGold);
        }
    }
}
