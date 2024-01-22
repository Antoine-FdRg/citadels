package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;

public class CustomBotBuilder {
    private final IView view;
    private final Deck deck;
    final int nbGold;
    private IPickingStrategy pickingStrategy;

    private ICharacterChoosingStrategy characterChoosingStrategy;

    public CustomBotBuilder(int nbGold, IView view, Deck deck) {
        this.nbGold = nbGold;
        this.view = view;
        this.deck = deck;
    }

    public CustomBotBuilder setPickingStrategy(IPickingStrategy pickingStrategy) {
        this.pickingStrategy = pickingStrategy;
        return this;
    }

    public CustomBotBuilder setCharacterChoosingStrategy(ICharacterChoosingStrategy characterChoosingStrategy) {
        this.characterChoosingStrategy = characterChoosingStrategy;
        return this;
    }

    public CustomBot build() {
        if(this.pickingStrategy == null){
            throw new IllegalStateException("You must set a picking strategy for the custom bot");
        }
        if (this.characterChoosingStrategy == null) {
            throw new IllegalStateException("You must set a character choosing strategy for the custom bot");
        }
        return new CustomBot(nbGold, this.deck, this.view, this.pickingStrategy, this.characterChoosingStrategy);
    }
}
