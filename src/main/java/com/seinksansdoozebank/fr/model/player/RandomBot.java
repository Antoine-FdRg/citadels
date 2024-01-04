package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.view.IView;

import java.util.List;
import java.util.Optional;

public class RandomBot extends Player {

    public RandomBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public void play() {
        view.displayPlayerStartPlaying(this);
        view.displayPlayerRevealCharacter(this);
        view.displayPlayerInfo(this);
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

    @Override
    public String toString() {
        return "Le bot aléatoire " + this.id;
    }
}
