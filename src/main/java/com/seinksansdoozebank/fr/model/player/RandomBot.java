package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.view.IView;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomBot extends Player {

    public RandomBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public void play() {
        if (this.getCharacter().isDead()) {
            throw new IllegalStateException("The player is dead, he can't play.");
        }
        view.displayPlayerStartPlaying(this);
        view.displayPlayerRevealCharacter(this);
        view.displayPlayerInfo(this);
        if (character instanceof CommonCharacter commonCharacter) {
            commonCharacter.goldCollectedFromDisctrictType();
        }
        this.useEffect();
        int nbDistrictsToBuild = random.nextInt(this.getNbDistrictsCanBeBuild() + 1);
        if (random.nextBoolean()) {
            pickSomething();
            if (nbDistrictsToBuild > 0) {
                this.playCards(nbDistrictsToBuild);
            }
        } else {
            if (nbDistrictsToBuild > 0) {
                this.playCards(nbDistrictsToBuild);
            }
            pickSomething();
        }
        view.displayPlayerInfo(this);
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
        }
    }

    /**
     * Effect of assassin character (kill a player)
     *
     * @param assassin the assassin character
     */
    private void useEffectAssassin(Assassin assassin) {
        Player playerToKill = this.getOpponents().get(random.nextInt(this.getOpponents().size()));
        // try to kill the playerToKill and if throw retry until the playerToKill is dead
        while (!playerToKill.getCharacter().isDead()) {
            try {
                assassin.useEffect(playerToKill.getCharacter());
                view.displayPlayerUseAssasinEffect(this, playerToKill.getCharacter());
                break;
            } catch (IllegalArgumentException e) {
                playerToKill = this.getOpponents().get(random.nextInt(this.getOpponents().size()));
            }
        }
    }

    void useEffectCondottiere(Condottiere condottiere) {
        // get a value 0 or 1
        boolean randomValue = random.nextBoolean();
        // if the value is 0, the bot is not using the condottiere effect, else it is using it
        if (randomValue) {
            // get a random player, and destroy a district of this player randomly
            Player playerToDestroyDistrict = this.getOpponents().get(random.nextInt(this.getOpponents().size()));
            // if the player has no district, the bot will not use the condottiere effect
            // Or check if the player choose is not the bishop
            if (playerToDestroyDistrict.getCitadel().isEmpty() || playerToDestroyDistrict.getCharacter() instanceof Bishop) {
                return;
            }
            // get the random district
            int index = random.nextInt(playerToDestroyDistrict.getCitadel().size());
            // get the district to destroy
            District districtToDestroy = playerToDestroyDistrict.getCitadel().get(index).getDistrict();
            // Check if the number of golds of the player is enough to destroy the district
            if (this.getNbGold() >= districtToDestroy.getCost() + 1) {
                // destroy the district
                try {
                    condottiere.useEffect(playerToDestroyDistrict.getCharacter(), districtToDestroy);
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
    public String toString() {
        return "Le bot aléatoire " + this.id;
    }

    public void setRandom(Random mockRandom) {
        this.random = mockRandom;
    }
}
