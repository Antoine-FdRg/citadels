package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.commoncharacters.WarlordTarget;
import com.seinksansdoozebank.fr.model.character.specialscharacters.MagicianTarget;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Represents a player in the game
 */
public abstract class Player implements Opponent {
    /**
     * Counter to give a unique id to each player
     */
    private static int counter = 1;
    /**
     * The id of the player
     */
    protected final int id;
    private int nbGold;
    private int bonus;
    private boolean isFirstToHaveAllDistricts;
    final Deck deck;
    final Bank bank;
    final List<Card> hand;
    final List<Card> citadel;
    /**
     * The current view
     */
    protected final IView view;
    Random random = new Random();
    Character character;
    private int positionInDrawToPickACharacter;

    /**
     * List of all the players in the game
     */
    private List<Opponent> opponents;

    private List<Opponent> opponentsWhichHasChosenCharacterBefore;
    private List<Character> availableCharacters;
    private boolean lastCardPlacedCourtyardOfMiracle = false;
    private boolean characterIsRevealed = false;
    private DistrictType colorCourtyardOfMiracleType;
    private boolean hasPlayed;
    private List<Character> charactersNotInRound;
    private List<Character> charactersSeenInRound;
    private int nbCharacterChosenInARow;
    /**
     * Max number of character chosen in a row
     */
    public static final int NB_MAX_CHARACTER_CHOSEN_IN_A_ROW = 3;
    private Character lastCharacterChosen;
    private int numberOfDistrictsNeeded = Game.NORMAL_NB_DISTRICT_TO_WIN;


    /**
     * Player constructor
     *
     * @param nbGold the number of gold
     * @param deck   the deck
     * @param view   the view
     * @param bank   the bank
     */
    protected Player(int nbGold, Deck deck, IView view, Bank bank) {
        this.id = counter++;
        this.nbGold = nbGold;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.citadel = new ArrayList<>();
        this.opponents = new ArrayList<>();
        this.view = view;
        this.bonus = 0;
        this.isFirstToHaveAllDistricts = false;
        this.hasPlayed = false;
        this.bank = bank;
    }

    /**
     * Get the position in the draw to pick a character
     *
     * @return the position in the draw to pick a character
     */
    public int getPositionInDrawToPickACharacter() {
        return this.positionInDrawToPickACharacter;
    }

    /**
     * Set the position in the draw to pick a character
     *
     * @param rank the position in the draw to pick a character
     */
    public void setPositionInDrawToPickACharacter(int rank) {
        this.positionInDrawToPickACharacter = rank;
    }

    /**
     * Represents the player's turn
     */
    public void play() {
        if (this.getCharacter().isDead()) {
            throw new IllegalStateException("The player is dead, he can't play.");
        }
        view.displayPlayerStartPlaying(this);
        this.reveal();
        view.displayPlayerInfo(this);
        this.usePrestigesEffect();
        this.setHasPlayed(false);
        this.playARound();
        view.displayPlayerInfo(this);
    }

    /**
     * Use the effect of the prestige districts
     */
    public void usePrestigesEffect() {
        // for every prestiges that the player has, and has effect, we use it
        this.citadel.stream().filter(
                        card -> card.getDistrict().getDistrictType().equals(DistrictType.PRESTIGE) &&
                                card.getDistrict().getActiveEffect() != null)
                .forEach(card ->
                        card.getDistrict().useActiveEffect(this, this.view)
                );
    }

    /**
     * Play a round of the game
     */
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

    /**
     * Return true if the player has the courtyard of miracle in its citadel and it's not the last card
     *
     * @return true if the player has the courtyard of miracle in its citadel and it's not the last card
     */
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
     * Set if the player has played
     *
     * @param hasPlayed true if the player has played, false otherwise
     */
    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

    /**
     * Get if the player has played
     *
     * @return true if the player has played, false otherwise
     */
    public boolean hasPlayed() {
        return hasPlayed;
    }

    /**
     * Represents the player's choice to draw x districts keep one and discard the other one
     * MUST CALL this.hand.add() AND this.deck.discard() AT EACH CALL
     */
    public void pickCardsKeepSomeAndDiscardOthers() {
        List<Card> pickedCards = new ArrayList<>();
        int numberOfCardsToPick = numberOfCardsToPick();
        for (int i = 0; i < numberOfCardsToPick; i++) {
            pickCardFromDeck(pickedCards);
        }
        if (pickedCards.isEmpty()) return;
        if ((!this.hasPlayed) && isLibraryPresent()) {
            this.view.displayPlayerKeepBothCardsBecauseOfLibrary(this);
            this.hand.addAll(pickedCards);
        } else {
            this.view.displayPlayerPickCards(this, 1);
            Card chosenCard = keepOneDiscardOthers(pickedCards);
            this.hand.add(chosenCard);
            pickedCards.stream().filter(card -> card.hashCode() != chosenCard.hashCode()).forEach(this.deck::discard);
        }
    }

    /**
     * Pick a card from the deck and add it to the list of picked cards
     *
     * @param pickedCards the list of picked cards
     */
    void pickCardFromDeck(List<Card> pickedCards) {
        Optional<Card> cardPick = this.deck.pick();
        cardPick.ifPresent(pickedCards::add);
    }

    /**
     * On regarde si dans la citadelle du joueur le player à l'observatoire, on retourne le nombre de cartes à piocher en fonction
     *
     * @return nombre de cartes à piocher
     */
    protected int numberOfCardsToPick() {
        Optional<Card> observatory = getCitadel().stream().filter(card -> card.getDistrict() == District.OBSERVATORY).findFirst();
        if (observatory.isPresent()) {
            this.view.displayPlayerHasGotObservatory(this);
            return 3;
        }
        return 2;
    }

    /**
     * The player keeps one card and discard the others
     *
     * @param pickedCards the list of cards to choose from
     * @return the card to keep
     */
    protected abstract Card keepOneDiscardOthers(List<Card> pickedCards);

    /**
     * Allow the player to pick a card from the deck (usefull when it needs to switch its hand with the deck)
     */
    public final void pickACard() {
        pickCardFromDeck(this.getHand());
    }

    /**
     * Represents the phase where the player discard a card from his hand
     *
     * @param card the card to discard
     */
    public final void discardACard(Card card) {
        this.getHand().remove(card);
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
        this.getHand().remove(chosenCard);
        // if the chose card is CourtyardOfMiracle, we set the attribute lastCardPlacedCourtyardOfMiracle to true
        this.lastCardPlacedCourtyardOfMiracle = chosenCard.getDistrict().equals(District.COURTYARD_OF_MIRACLE);
        this.citadel.add(chosenCard);
        this.returnGoldToBank(chosenCard.getDistrict().getCost());
        return optChosenCard;
    }

    /**
     * Represents the phase where the player build x districts chosen by chooseDistrict()
     *
     * @param numberOfCards the number of cards to play
     */
    public void buyXCardsAndAddThemToCitadel(int numberOfCards) {
        setHasPlayed(true);
        if (numberOfCards <= 0) {
            throw new IllegalArgumentException("Number of cards to play must be positive");
        } else if (numberOfCards > this.getNbDistrictsCanBeBuild()) {
            throw new IllegalArgumentException("Number of cards to play must be less than the number of districts the player can build");
        }
        for (int i = 0; i < numberOfCards; i++) {
            Optional<Card> card = playACard();
            card.ifPresent(value -> this.view.displayPlayerPlaysCard(this, value));
        }
    }

    /**
     * make the player play the Card given in argument by removing it from its hand, adding it to its citadel and decreasing golds
     *
     * @param card the card to play
     */
    public void buyACardAndAddItToCitadel(Card card) {
        if (!canPlayCard(card)) {
            return;
        }
        this.hand.remove(card);
        this.citadel.add(card);
        this.returnGoldToBank(card.getDistrict().getCost());
        this.view.displayPlayerPlaysCard(this, card);
    }


    /**
     * Collect gold with the effect of the character if it is a common character
     */
    protected void useCommonCharacterEffect() {
        if (this.getCharacter() instanceof CommonCharacter commonCharacter) {
            int nbGoldSave = this.getNbGold();
            commonCharacter.goldCollectedFromDistrictType();
            if (this.getCharacter().getRole() == Role.MERCHANT) {
                view.displayGoldCollectedFromMerchant(this);
            }
            if (this.getNbGold() - nbGoldSave > 0)
                this.view.displayGoldCollectedFromDistrictType(this, this.getNbGold() - nbGoldSave, commonCharacter.getTarget());
        }
    }

    /**
     * Effect of architect character (pick 2 cards)
     */
    public void useEffectArchitect() {
        int i;
        for (i = 0; i < 2; i++) {
            Optional<Card> cardPick = this.deck.pick();
            if (cardPick.isEmpty()) break;
            cardPick.ifPresent(this.hand::add);
        }
        if (i == 0) return;
        view.displayPlayerPickCards(this, i);
    }

    /**
     * Use the effect of the magician
     *
     * @return the target of the magician
     */
    public abstract MagicianTarget useEffectMagician();

    /**
     * Use the effect of the assassin
     *
     * @return the character to kill
     */
    public abstract Character useEffectAssassin();

    abstract Character chooseAssassinTarget();

    /**
     * Choose a character to kill for warlord character
     *
     * @param opponentsFocusable the list of opponents to choose from
     * @return the chosen character
     */
    public abstract WarlordTarget chooseWarlordTarget(List<Opponent> opponentsFocusable);

    abstract Optional<Character> chooseThiefTarget();

    /**
     * Use the thief effect
     *
     * @return the character to steal from
     */
    public Character useEffectThief() {
        Optional<Character> victim = this.chooseThiefTarget();
        if (victim.isPresent()) {
            view.displayPlayerUseThiefEffect(this);
            return victim.get();
        }
        return null;
    }

    /**
     * Check if the player has a card to play
     *
     * @return true if the player has a card to play, false otherwise
     */
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
    public final boolean canPlayCard(Card card) {
        return card.getDistrict().getCost() <= this.getNbGold()
                && this.getCitadel().stream().noneMatch(c -> c.getDistrict().equals(card.getDistrict()));
    }

    /**
     * Decrease the number of gold of the player
     *
     * @param gold the number of gold coins to decrease
     */
    public void decreaseGold(int gold) {
        this.nbGold -= gold;
    }

    /**
     * Increase the number of gold of the player
     *
     * @param gold the number of gold coins to increase
     */
    public void increaseGold(int gold) {
        this.nbGold += gold;
    }

    /**
     * Return gold to the bank
     *
     * @param gold the number of gold coins to return
     */
    public void returnGoldToBank(int gold) {
        this.nbGold -= gold;
        this.bank.retrieveCoin(gold);
    }

    /**
     * Represents the player's choice to draw 2 gold coins from the bank
     */
    public final void pickGold() {
        int nbPickedGold = this.bank.pickXCoin();
        view.displayPlayerPicksGold(this, nbPickedGold);
        this.nbGold += nbPickedGold;
    }

    /**
     * Represents the player's choice to draw x gold coins
     *
     * @param nbOfGold the number of gold coins to pick
     */
    public final void pickGold(int nbOfGold) {
        int nbPickedGold = this.bank.pickXCoin(nbOfGold);
        if (nbPickedGold > 0) {
            this.nbGold += nbPickedGold;
        }
    }

    /**
     * Get the hand of the player
     *
     * @return the hand of the player
     */
    public List<Card> getHand() {
        return this.hand;
    }

    /**
     * Get the citadel of the player
     *
     * @return the citadel of the player
     */
    public List<Card> getCitadel() {
        return Collections.unmodifiableList(this.citadel);
    }

    /**
     * Set the citadel of the player
     *
     * @param citadel the citadel of the player
     */
    void setCitadel(List<Card> citadel) {
        this.citadel.clear();
        this.citadel.addAll(citadel);
    }

    /**
     * Get the number of gold of the player
     *
     * @return the number of gold
     */
    public int getNbGold() {
        return this.nbGold;
    }

    /**
     * Get the id of the player
     *
     * @return the id of the player
     */
    public int getId() {
        return this.id;
    }

    /**
     * Reset the counter of the id of the player
     */
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
    public boolean getIsFirstToHaveAllDistricts() {
        return this.isFirstToHaveAllDistricts;
    }

    /**
     * setter, this method set the isFirstToHaveEightDistricts attribute to true
     */
    public void setIsFirstToHaveAllDistricts() {
        this.isFirstToHaveAllDistricts = true;
    }

    /**
     * Get the score of the player
     *
     * @return the score of the player
     */
    public final int getScore() {
        //calcule de la somme du cout des quartiers de la citadelle et rajoute les bonus s'il y en a
        return (getCitadel().stream().mapToInt(card -> card.getDistrict().getCost()).sum()) + getBonus();
    }

    /**
     * Choose a character from the list of characters passed in parameter by calling the method choseCharacterImpl
     *
     * @param characters the list of characters to choose from
     * @return the chosen character
     */
    public Character chooseCharacter(List<Character> characters) {
        this.character = this.chooseCharacterImpl(characters);
        this.character.setPlayer(this);
        this.charactersSeenInRound = new ArrayList<>(characters);
        this.charactersSeenInRound.remove(this.character);
        this.view.displayPlayerChooseCharacter(this);
        return this.character;
    }

    /**
     * Chose a character from the list of characters passed in parameter
     *
     * @param characters the list of characters to choose from
     * @return the chosen character
     */
    protected abstract Character chooseCharacterImpl(List<Character> characters);

    /**
     * Get the character of the player
     *
     * @return the character of the player
     */
    public Character getCharacter() {
        return this.character;
    }

    /**
     * Get the number of districts the player can build
     *
     * @return the number of districts the player can build
     */
    public int getNbDistrictsCanBeBuild() {
        return this.getCharacter().getRole().getNbDistrictsCanBeBuild();
    }

    /**
     * Retrieve the character of the player
     *
     * @return the character of the player
     */
    public Character retrieveCharacter() {
        if (this.character == null) {
            throw new IllegalStateException("No character to retrieve");
        }
        this.hide();
        Character characterToRetrieve = this.character;
        this.character = null;
        characterToRetrieve.resurrect();
        characterToRetrieve.setPlayer(null);
        return characterToRetrieve;
    }

    @Override
    public final void destroyDistrict(Player attacker, District targetedDistrict) {
        // if the targetedDistrict is in the citadel, we remove it and return the card removed
        Optional<Card> card = this.getCitadel().stream().filter(c -> c.getDistrict().equals(targetedDistrict)).findFirst();
        if (card.isPresent()) {
            Card cardDestroyed = card.get();
            this.citadel.remove(cardDestroyed);
            boolean someoneUseCemeteryToKeepDistrict = askOpponentForCemeteryEffect(cardDestroyed);
            if (!someoneUseCemeteryToKeepDistrict) {
                this.deck.discard(cardDestroyed);
                this.view.displayPlayerDiscardCard(this, cardDestroyed);
            }
            this.view.displayPlayerUseWarlordDistrict(attacker, this, targetedDistrict);
        } else {
            throw new IllegalArgumentException("The player doesn't have the targetedDistrict to destroy");
        }
    }

    boolean askOpponentForCemeteryEffect(Card cardToRetrieve) {
        boolean someoneUseCemeteryToKeepDistrict = false;
        for (Opponent opponent : this.getOpponents()) {
            if (opponent.getCitadel().stream().anyMatch(card -> card.getDistrict().equals(District.CEMETERY))
                    && opponent.isUsingCemeteryEffect(cardToRetrieve)) {
                someoneUseCemeteryToKeepDistrict = true;
                break;
            }
        }
        return someoneUseCemeteryToKeepDistrict;
    }

    public final boolean isUsingCemeteryEffect(Card card) {
        if (!this.getCharacter().getRole().equals(Role.WARLORD) && this.wantToUseCemeteryEffect(card)) {
            this.hand.add(card);
            this.returnGoldToBank(1);
            this.view.displayPlayerUseCemeteryEffect(this, card);
            return true;
        }
        return false;
    }

    /**
     * Check if the player wants to use the cemetery effect
     *
     * @param card the card to retrieve
     * @return true if the player wants to use the cemetery effect, false otherwise
     */
    protected abstract boolean wantToUseCemeteryEffect(Card card);

    /**
     * Get opponents of the player
     *
     * @return the opponents of the player
     */
    public List<Opponent> getOpponents() {
        return Collections.unmodifiableList(this.opponents);
    }

    /**
     * Set the opponents of the player
     *
     * @param opponents the opponents of the player
     */
    public void setOpponents(List<Opponent> opponents) {
        this.opponents = opponents;
    }

    /**
     * Get the opponents of the player which has chosen a character before
     *
     * @return the opponents of the player which has chosen a character before
     */
    public List<Opponent> getOpponentsWhichHasChosenCharacterBefore() {
        return opponentsWhichHasChosenCharacterBefore;
    }

    /**
     * Set the opponents of the player which has chosen a character before
     *
     * @param opponentsWhichHasChosenCharacterBefore the opponents of the player which has chosen a character before
     */
    public void setOpponentsWhichHasChosenCharacterBefore(List<Opponent> opponentsWhichHasChosenCharacterBefore) {
        this.opponentsWhichHasChosenCharacterBefore = opponentsWhichHasChosenCharacterBefore;
    }

    @Override
    public void switchHandWith(Player magician) {
        List<Card> handToSwitch = new ArrayList<>(this.getHand());
        this.getHand().clear();
        this.getHand().addAll(magician.getHand());
        magician.getHand().clear();
        magician.getHand().addAll(handToSwitch);
    }

    /**
     * Choose the color of the courtyard of miracle
     */
    public abstract void chooseColorCourtyardOfMiracle();

    /**
     * Get the last card placed in the courtyard of miracle
     *
     * @return true if the last card placed in the courtyard of miracle is the courtyard of miracle, false otherwise
     */
    public boolean isLastCardPlacedCourtyardOfMiracle() {
        return this.lastCardPlacedCourtyardOfMiracle;
    }

    /**
     * Set the last card placed in the courtyard of miracle
     *
     * @param lastCardPlacedCourtyardOfMiracle true if the last card placed in the courtyard of miracle is the courtyard of miracle, false otherwise
     */
    public void setLastCardPlacedCourtyardOfMiracle(boolean lastCardPlacedCourtyardOfMiracle) {
        this.lastCardPlacedCourtyardOfMiracle = lastCardPlacedCourtyardOfMiracle;
    }

    /**
     * Get the color of the courtyard of miracle
     *
     * @return the color of the courtyard of miracle
     */
    public DistrictType getColorCourtyardOfMiracleType() {
        return this.colorCourtyardOfMiracleType;
    }

    /**
     * Set the color of the courtyard of miracle
     *
     * @param colorCourtyardOfMiracleType the color of the courtyard of miracle
     */
    public void setColorCourtyardOfMiracleType(DistrictType colorCourtyardOfMiracleType) {
        this.colorCourtyardOfMiracleType = colorCourtyardOfMiracleType;
    }

    /**
     * Check if we want to use the manufacture effect
     *
     * @return true if we want to use the manufacture effect, false otherwise
     */
    public abstract boolean wantToUseManufactureEffect();

    /**
     * Check if the character is revealed
     *
     * @return true if the character is revealed, false otherwise
     */
    public boolean isCharacterIsRevealed() {
        return this.characterIsRevealed;
    }

    /**
     * Reveal the character
     */
    public void reveal() {
        if (this.characterIsRevealed) {
            throw new IllegalStateException("The player is already revealed");
        }
        view.displayPlayerRevealCharacter(this);
        this.characterIsRevealed = true;
    }

    /**
     * Hide the character
     */
    public void hide() {
        if (!this.characterIsRevealed && !this.character.isDead()) {
            throw new IllegalStateException("The player is already hidden");
        }
        this.characterIsRevealed = false;
    }

    @Override
    public Character getOpponentCharacter() {
        if (this.isCharacterIsRevealed()) {
            return this.getCharacter();
        } else {
            return null;
        }
    }

    @Override
    public int nbDistrictsInCitadel() {
        return this.getCitadel().size();
    }

    /**
     * Get the available characters
     *
     * @return the available characters
     */
    public List<Character> getAvailableCharacters() {
        return availableCharacters;
    }

    /**
     * Set the available characters
     *
     * @param availableCharacters the available characters
     */
    public void setAvailableCharacters(List<Character> availableCharacters) {
        this.availableCharacters = availableCharacters;
    }

    /**
     * Set the characters not in the round
     *
     * @param charactersNotInRound the characters not in the round
     */
    public void setCharactersNotInRound(List<Character> charactersNotInRound) {
        this.charactersNotInRound = charactersNotInRound;
    }

    /**
     * Get the characters not in the round
     *
     * @return the characters not in the round
     */
    public List<Character> getCharactersNotInRound() {
        return this.charactersNotInRound;
    }

    /**
     * Get the characters seen in the round
     *
     * @return the characters seen in the round
     */
    public List<Character> getCharactersSeenInRound() {
        return this.charactersSeenInRound;
    }

    /**
     * Discard a card from the hand of the player (for laboratory effect)
     *
     * @param card the card to discard
     * @return true if the card has been discarded, false otherwise
     */
    public boolean discardFromHand(Card card) {
        if (this.hand.isEmpty()) {
            return false;
        }
        this.hand.remove(card);
        this.deck.discard(card);
        this.view.displayPlayerDiscardCard(this, card);
        return true;
    }

    /**
     * Choose a card to discard from the hand of the player (for laboratory effect)
     *
     * @return the card to discard
     */
    public abstract Card chooseCardToDiscardForLaboratoryEffect();

    /**
     * Check if the player has the library in its citadel
     *
     * @return true if the player has the library in its citadel, false otherwise
     */
    public boolean isLibraryPresent() {
        return this.getCitadel().stream().anyMatch(card -> card.getDistrict().equals(District.LIBRARY));
    }


    @Override
    public int getHandSize() {
        return this.getHand().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return this.getId() == player.getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    /**
     * method tells us if a player has 7 districts in his citadel
     *
     * @return a boolean
     */
    @Override
    public boolean isAboutToWin() {
        return this.getCitadel().size() == 7;
    }

    /**
     * Set the random
     *
     * @param mockRandom the random
     */
    public void setRandom(Random mockRandom) {
        this.random = mockRandom;
    }

    /**
     * Set the last character chosen
     *
     * @param lastCharacterChosen the last character chosen
     */
    protected void setLastCharacterChosen(Character lastCharacterChosen) {
        this.lastCharacterChosen = lastCharacterChosen;
    }

    /**
     * Get the last character chosen
     *
     * @return the last character chosen
     */
    public Character getLastCharacterChosen() {
        return this.lastCharacterChosen;
    }

    /**
     * Get the number of character chosen in a row
     *
     * @return the number of character chosen in a row
     */
    public int getNbCharacterChosenInARow() {
        return this.nbCharacterChosenInARow;
    }

    /**
     * Set the number of character chosen in a row
     *
     * @param nbCharacterChosenInARow the number of character chosen in a row
     */
    public void setNbCharacterChosenInARow(int nbCharacterChosenInARow) {
        this.nbCharacterChosenInARow = nbCharacterChosenInARow;
    }

    public void setNumberOfDistrictsNeeded(int numberOfDistrictsNeeded) {
        this.numberOfDistrictsNeeded = numberOfDistrictsNeeded;
    }

    public int getNumberOfDistrictsNeeded() {
        return this.numberOfDistrictsNeeded;
    }
}
