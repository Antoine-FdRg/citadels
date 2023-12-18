package com.seinksansdoozebank.fr.model.player;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;

import java.util.List;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public abstract class Player {
    private static int counter = 1;
    protected final int id;
    private int nbGold;
    protected Deck deck;
    protected final List<Card> hand;
    private final List<Card> citadel;
    protected final IView view;
    protected final Random random = new Random();
    protected Character character;

    protected Player(int nbGold, Deck deck, IView view) {
        this.id = counter++;
        this.nbGold = nbGold;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.citadel = new ArrayList<>();
        this.view = view;
    }

    /**
     * Represents the player's turn
     * MUST CALL view.displayPlayerPlaysDistrict() at the end of the turn with the district built by the player
     */
    public abstract void play();

    /**
     * Represents the player's choice between drawing 2 gold coins or a district
     */
    protected abstract void pickSomething();

    /**
     * Represents the player's choice to draw 2 gold coins
     */
    protected final void pickGold() {
        view.displayPlayerPicksGold(this);
        this.nbGold+=2;
    }

    /**
     * Represents the player's choice to draw 2 districts keep one and discard the other one
     * MUST CALL this.hand.add() AND this.deck.discard() AT EACH CALL
     */
    protected abstract void pickTwoCardKeepOneDiscardOne();

    /**
     * Represents the phase where the player build a district chosen by chooseDistrict()
     * @return the district built by the player
     */
    protected final Optional<Card> playACard() {
        Optional<Card> optChosenCard = chooseCard();
        if (optChosenCard.isEmpty()|| !canPlayCard(optChosenCard.get())) {
            return Optional.empty();
        }
        Card chosenCard = optChosenCard.get();
        this.hand.remove(chosenCard);
        this.citadel.add(chosenCard);
        this.decreaseGold(chosenCard.getDistrict().getCost());
        return optChosenCard;
    }

    /**
     * Choose a district to build from the hand
     * Is automatically called in buildADistrict() to build the choosen district if canBuildDistrict(<choosenDistrcit>) is true
     * @return the district to build
     */
    protected abstract Optional<Card> chooseCard();

    /**
     * Verify if the player can build the district passed in parameter (he can build it if he has enough gold and if he doesn't already have it in his citadel)
     * @param card the district to build
     * @return true if the player can build the district passed in parameter, false otherwise
     */
    protected final boolean canPlayCard(Card card) {
        return card.getDistrict().getCost() <= this.nbGold && !this.citadel.contains(card);
    }

    protected final void decreaseGold(int gold) {
        this.nbGold -= gold;
    }
    public void increaseGold(int gold) {
        this.nbGold += gold;
    }

    public List<Card> getHand() {
        return this.hand;
    }

    public List<Card> getCitadel() {
        return this.citadel;
    }

    public int getNbGold() {
        return this.nbGold;
    }

    public int getId() {
        return this.id;
    }

    public static void resetIdCounter() {
        counter = 1;
    }

    public final int getScore() {
        //calcule de la somme du cout des quartiers de la citadelle
        return citadel.stream().mapToInt(card -> card.getDistrict().getCost()).sum();
    }

    public Character chooseCharacter(List<Character> characters) {
        this.character = characters.get(random.nextInt(characters.size()));
        this.character.setPlayer(this);
        return this.character;
    }

    public Character getCharacter(){
        return this.character;
    }

    public Character retrieveCharacter() {
        if(this.character == null) {
            throw new IllegalStateException("No character to retrieve");
        }
        Character characterToRetrieve = this.character;
        this.character = null;
        characterToRetrieve.setPlayer(null);
        return characterToRetrieve;
    }

    public boolean isTheKing() {
        //TODO remove this if when player are able to choose a character
        if(this.character == null) {
            return false;
        }
        return Role.KING.equals(this.character.getRole());
    }

    @Override
    public String toString() {
        return "Le joueur "+this.id;
    }
}
