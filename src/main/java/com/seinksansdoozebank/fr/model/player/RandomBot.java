package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomBot extends Player {

    public RandomBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public void playARound() {
        this.useCommonCharacterEffect();
        this.useEffect();
        int nbDistrictsToBuild = random.nextInt(this.getNbDistrictsCanBeBuild() + 1);
        if (random.nextBoolean()) {
            this.pickBeforePlaying(nbDistrictsToBuild);
        } else {
            this.playBeforePicking(nbDistrictsToBuild);
        }
    }

    /**
     * Represents the player's choice to pick something before playing
     *
     * @param nbDistrictsToBuild the number of districts the bot choose to build
     */
    protected void pickBeforePlaying(int nbDistrictsToBuild) {
        pickSomething();
        if (nbDistrictsToBuild > 0) {
            this.playCards(nbDistrictsToBuild);
        }
    }

    /**
     * Represents the player's choice to play something before picking
     *
     * @param nbDistrictsToBuild the number of districts the bot choose to build
     */
    protected void playBeforePicking(int nbDistrictsToBuild) {
        if (nbDistrictsToBuild > 0) {
            this.playCards(nbDistrictsToBuild);
        }
        pickSomething();
    }

    @Override
    protected void pickSomething() {
        if (random.nextBoolean()) {
            pickGold();
        } else {
            pickTwoCardKeepOneDiscardOne();
        }
    }

    @Override
    protected void pickTwoCardKeepOneDiscardOne() {
        this.view.displayPlayerPickCards(this, 1);
        Card card1 = this.deck.pick();
        Card card2 = this.deck.pick();
        if (random.nextBoolean()) {
            this.hand.add(card1);
            this.deck.discard(card2);
        } else {
            this.hand.add(card2);
            this.deck.discard(card1);
        }
    }

    @Override
    protected Optional<Card> chooseCard() {
        if (!this.hand.isEmpty()) {
            Card chosenCard;
            int cnt = 0;
            do {
                chosenCard = this.hand.get(random.nextInt(hand.size()));
                cnt++;
            } while (!this.canPlayCard(chosenCard) && cnt < 5);
            if (this.canPlayCard(chosenCard)) {
                return Optional.of(chosenCard);
            }
        }
        return Optional.empty();
    }

    @Override
    public Character chooseCharacter(List<Character> characters) {
        this.character = characters.get(random.nextInt(characters.size()));
        this.character.setPlayer(this);
        characters.remove(this.character);
        this.view.displayPlayerChooseCharacter(this);
        return this.character;
    }

    protected void useEffect() {
        if (this.character instanceof Merchant merchant) {
            merchant.useEffect();
        }
        // The strategy of the smart bot for condottiere will be to destroy the best district of the player which owns the highest number of districts
        else if (this.character instanceof Condottiere condottiere) {
            this.useEffectCondottiere(condottiere);
        } else if (this.character instanceof Architect) {
            this.useEffectArchitectPickCards();
        } else if (this.character instanceof Assassin assassin) {
            this.useEffectAssassin(assassin);
        } else if (this.character instanceof Magician magician) {
            this.useEffectMagician(magician);
        }
    }

    /**
     * The magician can exchange all it's card with another player or
     * can exchange some card with the same number of cards with the deck
     *
     * @param magician the magician character
     */
    @Override
    void useEffectMagician(Magician magician) {
        // if the value is 0, the bot is not using the magician effect, else it is using it
        if (random.nextBoolean()) {
            // if true exchange all the card with a player
            if (random.nextBoolean()) {
                // get a random player
                Opponent opponentToExchangeCards = this.getOpponents().get(random.nextInt(this.getOpponents().size()));
                // exchange all the cards with the player
                magician.useEffect(opponentToExchangeCards, List.of());
                this.view.displayPlayerUseMagicianEffect(this, opponentToExchangeCards);
                return;
            }
            // if false exchange some cards with the deck
            // exchange some cards with the deck
            int nbCardsToExchange = random.nextInt(this.getHand().size() + 1);
            // Choose the cards from the district to exchange
            Collections.shuffle(this.getHand());
            List<Card> cardsToExchange = this.getHand().stream()
                    .limit(nbCardsToExchange)
                    .toList();
            magician.useEffect(null, cardsToExchange);
            this.view.displayPlayerUseMagicianEffect(this, null);
        }
    }

    /**
     * Effect of assassin character (kill a player)
     *
     * @param assassin the assassin character
     */
    @Override
    void useEffectAssassin(Assassin assassin) {
        Character characterToKill = this.getAvailableCharacters().get(random.nextInt(this.getAvailableCharacters().size()));
        // try to kill the playerToKill and if throw retry until the playerToKill is dead
        while (!characterToKill.isDead()) {
            try {
                assassin.useEffect(characterToKill);
                view.displayPlayerUseAssassinEffect(this, characterToKill);
                break;
            } catch (IllegalArgumentException e) {
                characterToKill = this.getAvailableCharacters().get(random.nextInt(this.getAvailableCharacters().size()));
            }
        }
    }

    @Override
    void useEffectCondottiere(Condottiere condottiere) {
        // if the value is 0, the bot is not using the condottiere effect, else it is using it
        if (random.nextBoolean()) {
            // get a random player, and destroy a district of this player randomly
            Opponent opponentToDestroyDistrict = this.getOpponents().get(random.nextInt(this.getOpponents().size()));
            // if the player has no district, the bot will not use the condottiere effect
            // Or check if the player choose is not the bishop
            Character opponentCharacter = opponentToDestroyDistrict.getOpponentCharacter();
            if (opponentToDestroyDistrict.nbDistrictsInCitadel() <= 0 || opponentCharacter instanceof Bishop || opponentCharacter == null) {
                return;
            }
            // get the random district
            int index = random.nextInt(opponentToDestroyDistrict.nbDistrictsInCitadel());
            // get the district to destroy
            District districtToDestroy = opponentToDestroyDistrict.getCitadel().get(index).getDistrict();
            // Check if the number of golds of the player is enough to destroy the district
            if (this.getNbGold() >= districtToDestroy.getCost() - 1) {
                // destroy the district
                try {
                    condottiere.useEffect(opponentCharacter, districtToDestroy);
                } catch (IllegalArgumentException e) {
                    view.displayPlayerError(this, e.getMessage());
                }
            }
        }
    }

    public void chooseColorCourtyardOfMiracle() {
        // Set a random DistricType to the Courtyard of Miracle
        this.getCitadel().stream()
                .filter(card -> card.getDistrict().equals(District.COURTYARD_OF_MIRACLE))
                .findFirst()
                .ifPresent(card -> this.setColorCourtyardOfMiracleType(DistrictType.values()[random.nextInt(DistrictType.values().length)]));
    }

    @Override
    public boolean wantToUseManufactureEffect() {
        return this.getNbGold() > 3 && random.nextBoolean();
    }

    @Override
    public String toString() {
        return "Le bot aléatoire " + this.id;
    }

    public void setRandom(Random mockRandom) {
        this.random = mockRandom;
    }
}
