package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;

public class CustomBotBuilder {
    private final IView view;
    private final Deck deck;
    IPickingStrategy pickingStrategy;

    public CustomBotBuilder(IView view, Deck deck) {
        this.view = view;
        this.deck = deck;
    }

    public CustomBotBuilder setPickingStrategy(IPickingStrategy pickingStrategy) {
        this.pickingStrategy = pickingStrategy;
        return this;
    }

    public CustomBot build() {
        if(this.pickingStrategy == null){
            throw new IllegalStateException("You must set a picking strategy for the custom bot");
        }
        return new CustomBot(2, this.deck, this.view, this.pickingStrategy);
    }
}
