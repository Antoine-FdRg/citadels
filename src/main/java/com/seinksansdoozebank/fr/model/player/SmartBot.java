package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Optional;

public class SmartBot extends Player{

    public SmartBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public Optional<District> play() {
        //TODO will be implemented in next commit
        return Optional.empty();
    }

    @Override
    protected void pickSomething() {
        //TODO will be implemented in next commit
    }

    @Override
    protected void pickADistrict() {
        //TODO will be implemented in next commit
    }

    @Override
    protected District chooseDistrict() {
        //TODO will be implemented in next commit
        return null;
    }
}
