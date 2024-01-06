package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomBot extends Player {

    public RandomBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public void play() {
        view.displayPlayerStartPlaying(this);
        view.displayPlayerRevealCharacter(this);
        view.displayPlayerInfo(this);
        this.useEffect();
        if (random.nextBoolean()) {
            pickSomething();
            view.displayPlayerPlaysCard(this, this.playACard());
        } else {
            view.displayPlayerPlaysCard(this, this.playACard());
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
        this.view.displayPlayerPickCard(this);
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
                condottiere.useEffect(playerToDestroyDistrict.getCharacter(), districtToDestroy);
            }
        }
    }

    @Override
    public String toString() {
        return "Le bot aléatoire " + this.id;
    }

    public void setRandom(Random mockRandom) {
        this.random = mockRandom;
    }
}
