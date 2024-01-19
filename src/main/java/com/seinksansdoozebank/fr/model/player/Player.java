package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;

import java.util.Collections;
import java.util.List;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public abstract class Player {
    private static int counter = 1;
    protected final int id;
    private int nbGold;
    private int bonus;
    private boolean isFirstToHaveEightDistricts;
    protected Deck deck;
    protected final List<Card> hand;
    private final List<Card> citadel;
    protected final IView view;
    protected Random random = new Random();
    protected Character character;
    private final List<Player> opponents = new ArrayList<>();
    private boolean lastCardPlacedCourtyardOfMiracle = false;
    private DistrictType colorCourtyardOfMiracleType;

    protected Player(int nbGold, Deck deck, IView view) {
        this.id = counter++;
        this.nbGold = nbGold;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.citadel = new ArrayList<>();
        this.view = view;
        this.bonus = 0;
        this.isFirstToHaveEightDistricts = false;
    }

    /**
     * Represents the player's turn
     * MUST CALL view.displayPlayerPlaysDistrict() at the end of the turn with the district built by the player
     */
    public void play() {
        if (this.getCharacter().isDead()) {
            throw new IllegalStateException("The player is dead, he can't play.");
        }
        view.displayPlayerStartPlaying(this);
        view.displayPlayerRevealCharacter(this);
        view.displayPlayerInfo(this);
        this.playARound();
        view.displayPlayerInfo(this);
    }

    public abstract void playARound();

    /**
     * Represents the player's choice between drawing 2 gold coins or a district
     */
    protected abstract void pickSomething();

    /**
     * if the bot has got in its citadel 5 different types of districts it returns true else return false
     *
     * @return a boolean
     */
    public boolean hasFiveDifferentDistrictTypes() {
        List<DistrictType> listDifferentDistrictType = new ArrayList<>();
        for (Card card : this.getCitadel()) {
            if (!listDifferentDistrictType.contains(card.getDistrict().getDistrictType())) {
                listDifferentDistrictType.add(card.getDistrict().getDistrictType());
            }
        }
        // if there is 4 different district types and there is a courtyard of miracle in the citadel, we add the last district type
        if (listDifferentDistrictType.size() == 4 && this.hasCourtyardOfMiracleAndItsNotTheLastCard()) {
            listDifferentDistrictType.add(this.getColorCourtyardOfMiracleType());
        }
        return (listDifferentDistrictType.size() == 5);
    }

    public boolean hasCourtyardOfMiracleAndItsNotTheLastCard() {
        return this.getCitadel().stream().anyMatch(card -> card.getDistrict().equals(District.COURTYARD_OF_MIRACLE))
                && !this.isLastCardPlacedCourtyardOfMiracle();
    }


    /**
     * @return list of districtType missing in the citadel of the player
     */
    public List<DistrictType> findDistrictTypesMissingInCitadel() {
        List<DistrictType> listOfDistrictTypeMissing = new ArrayList<>();
        for (DistrictType districtType : DistrictType.values()) {
            if (this.getCitadel().stream().anyMatch(card -> card.getDistrict().getDistrictType() == districtType)) {
                listOfDistrictTypeMissing.add(districtType);
            }
        }
        return listOfDistrictTypeMissing;
    }

    /**
     * Represents the player's choice to draw 2 gold coins
     */
    protected final void pickGold() {
        view.displayPlayerPicksGold(this);
        this.nbGold += 2;
    }

    /**
     * Represents the player's choice to draw 2 districts keep one and discard the other one
     * MUST CALL this.hand.add() AND this.deck.discard() AT EACH CALL
     */
    protected abstract void pickTwoCardKeepOneDiscardOne();

    /**
     * Allow the player to pick a card from the deck (usefull when it needs to switch its hand with the deck)
     */
    public final void pickACard() {
        this.hand.add(this.deck.pick());
    }

    public final void discardACard(Card card) {
        this.hand.remove(card);
        this.deck.discard(card);
    }

    /**
     * Represents the phase where the player build a district chosen by chooseDistrict()
     *
     * @return the district built by the player
     */
    protected final Optional<Card> playACard() {
        Optional<Card> optChosenCard = chooseCard();
        if (optChosenCard.isEmpty() || !canPlayCard(optChosenCard.get())) {
            return Optional.empty();
        }
        Card chosenCard = optChosenCard.get();
        this.hand.remove(chosenCard);
        // if the chose card is CourtyardOfMiracle, we set the attribute lastCardPlacedCourtyardOfMiracle to true
        this.lastCardPlacedCourtyardOfMiracle = chosenCard.getDistrict().equals(District.COURTYARD_OF_MIRACLE);
        this.citadel.add(chosenCard);
        this.decreaseGold(chosenCard.getDistrict().getCost());
        return optChosenCard;
    }

    public List<Card> playCards(int numberOfCards) {
        if (numberOfCards <= 0) {
            throw new IllegalArgumentException("Number of cards to play must be positive");
        } else if (numberOfCards > this.getNbDistrictsCanBeBuild()) {
            throw new IllegalArgumentException("Number of cards to play must be less than the number of districts the player can build");
        }
        List<Card> playedCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            Optional<Card> card = playACard();
            card.ifPresent(playedCards::add);
        }
        return playedCards;
    }

    /**
     * make the player play the Card given in argument by removing it from its hand, adding it to its citadel and decreasing golds
     *
     * @return the district built by the player
     */
    public List<Card> playCard(Card card) {
        if (!canPlayCard(card)) {
            return List.of();
        }
        this.hand.remove(card);
        this.citadel.add(card);
        this.decreaseGold(card.getDistrict().getCost());
        return List.of(card);
    }


    /**
     * Collect gold with the effect of the character if it is a common character
     */
    void useCommonCharacterEffect() {
        if (this.character instanceof CommonCharacter commonCharacter) {
            commonCharacter.goldCollectedFromDisctrictType();
        }
    }

    /**
     * Effect of architect character (pick 2 cards)
     */
    protected void useEffectArchitectPickCards() {
        this.hand.add(this.deck.pick());
        this.hand.add(this.deck.pick());
        view.displayPlayerPickCards(this, 2);
    }

    abstract void useEffectMagician(Magician magician);

    abstract void useEffectAssassin(Assassin assassin);

    abstract void useEffectCondottiere(Condottiere condottiere);

    protected boolean hasACardToPlay() {
        return this.hand.stream().anyMatch(this::canPlayCard);
    }

    /**
     * Choose a district to build from the hand
     *
     * @return the district to build
     */
    protected abstract Optional<Card> chooseCard();

    /**
     * Verify if the player can build the district passed in parameter (he can build it if he has enough gold and if he doesn't already have it in his citadel)
     *
     * @param card the district to build
     * @return true if the player can build the district passed in parameter, false otherwise
     */
    protected final boolean canPlayCard(Card card) {
        return card.getDistrict().getCost() <= this.nbGold && !this.getCitadel().contains(card) && this.getCitadel().size() < 8;
    }

    public void decreaseGold(int gold) {
        this.nbGold -= gold;
    }

    public void increaseGold(int gold) {
        this.nbGold += gold;
    }

    public List<Card> getHand() {
        return this.hand;
    }

    public List<Card> getCitadel() {
        return Collections.unmodifiableList(this.citadel);
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

    /**
     * We add bonus with the final state of the game to a specific player
     *
     * @param bonus point to add to a player
     */
    public void addBonus(int bonus) {
        this.bonus += bonus;
    }

    /**
     * getter
     *
     * @return bonus point of a player
     */
    public int getBonus() {
        return this.bonus;
    }

    /**
     * getter
     *
     * @return a boolean which specify if the player is the first to have 8Districts in his citadel
     */
    public boolean getIsFirstToHaveEightDistricts() {
        return this.isFirstToHaveEightDistricts;
    }

    /**
     * setter, this method set the isFirstToHaveEightDistricts attribute to true
     */
    public void setIsFirstToHaveEightDistricts() {
        this.isFirstToHaveEightDistricts = true;
    }

    public final int getScore() {
        //calcule de la somme du cout des quartiers de la citadelle et rajoute les bonus s'il y en a
        return (getCitadel().stream().mapToInt(card -> card.getDistrict().getCost()).sum()) + getBonus();
    }

    public abstract Character chooseCharacter(List<Character> characters);

    public Character getCharacter() {
        return this.character;
    }

    public int getNbDistrictsCanBeBuild() {
        return this.character.getRole().getNbDistrictsCanBeBuild();
    }

    public Character retrieveCharacter() {
        if (this.character == null) {
            throw new IllegalStateException("No character to retrieve");
        }
        Character characterToRetrieve = this.character;
        this.character = null;
        characterToRetrieve.resurrect();
        characterToRetrieve.setPlayer(null);
        return characterToRetrieve;
    }

    @Override
    public String toString() {
        return "Le joueur " + this.id;
    }

    public boolean destroyDistrict(Player attacker, District district) {
        if (this.citadel.removeIf(card -> card.getDistrict().equals(district))) {
            this.view.displayPlayerDestroyDistrict(attacker, this, district);
            return true;
        } else {
            throw new IllegalStateException("The player doesn't have the district to destroy");
        }
    }

    public List<Player> getOpponents() {
        return Collections.unmodifiableList(this.opponents);
    }

    public void setOpponents(List<Player> opponents) {
        this.opponents.addAll(opponents);
    }

    public void switchHandWith(Player player) {
        List<Card> handToSwitch = new ArrayList<>(this.getHand());
        this.hand.clear();
        this.hand.addAll(player.getHand());
        player.hand.clear();
        player.hand.addAll(handToSwitch);
    }

    public abstract void chooseColorCourtyardOfMiracle();

    public boolean isLastCardPlacedCourtyardOfMiracle() {
        return this.lastCardPlacedCourtyardOfMiracle;
    }

    public void setLastCardPlacedCourtyardOfMiracle(boolean lastCardPlacedCourtyardOfMiracle) {
        this.lastCardPlacedCourtyardOfMiracle = lastCardPlacedCourtyardOfMiracle;
    }

    public DistrictType getColorCourtyardOfMiracleType() {
        return this.colorCourtyardOfMiracleType;
    }

    public void setColorCourtyardOfMiracleType(DistrictType colorCourtyardOfMiracleType) {
        this.colorCourtyardOfMiracleType = colorCourtyardOfMiracleType;
    }


}
