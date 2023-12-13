package com.seinksansdoozebank.fr.model.player;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;

import java.util.List;

import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private static int counter = 1;
    private final int id;
    private int nbGold;
    private final List<Card> hand;
    private final List<Card> citadel;
    private final Random random = new Random();
    private boolean isStuck = false;
    private final IView view;

    public Player(int nbGold, IView view) {
        this.id = counter++;
        this.nbGold = nbGold;
        this.hand = new ArrayList<>();
        this.citadel = new ArrayList<>();
        this.view = view;
    }

    Card chooseCard() {
        return this.hand.get(random.nextInt(hand.size()));
    }

    public Card play() {
        int cnt = 0;
        view.displayPlayerStartPlaying(this);
        view.displayPlayerInfo(this);
        Card card = this.chooseCard();
        while (card.getDistrict().getCost() > this.nbGold && cnt < 5) {
            card = this.chooseCard();
            cnt++;
        }

        this.hand.remove(card);
        this.citadel.add(card);
        this.decreaseGold(card.getDistrict().getCost());
        return card;
    }

    void decreaseGold(int gold) {
        this.nbGold -= gold;
    }

    public void addCardToHand(Card card) {
        this.hand.add(card);
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

    public boolean isStuck() {
        return isStuck;
    }

    public int getScore() {
        //calcule de la somme du cout des quartiers de la citadelle
        return citadel.stream().mapToInt(card -> card.getDistrict().getCost()).sum();
    }

    @Override
    public String toString() {
        return "Le joueur "+this.id;
    }
}
