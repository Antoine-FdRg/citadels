package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;

public class CustomBot extends RandomBot {

    IPickingStrategy pickingStrategy;

    protected CustomBot(int nbGold, Deck deck, IView view, IPickingStrategy pickingStrategy) {
        super(nbGold, deck, view);
        this.pickingStrategy = pickingStrategy;
    }

    @Override
    protected void pickSomething() {
        pickingStrategy.apply(this);
    }

    @Override
    public String toString() {
        return "Le bot custom " + this.id;
    }
}
