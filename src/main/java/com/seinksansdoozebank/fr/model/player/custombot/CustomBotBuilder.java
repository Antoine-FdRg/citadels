package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;

public class CustomBotBuilder {
    private final IView view;
    private final Deck deck;
    final int nbGold;
    private IPickingStrategy pickingStrategy;
    private ICharacterChoosingStrategy characterChoosingStrategy;
    private IUsingThiefEffectStrategy usingThiefEffectStrategy;

    private IUsingMurdererEffectStrategy usingMurdererEffectStrategy;

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

    public CustomBotBuilder setUsingThiefEffectStrategy(IUsingThiefEffectStrategy usingThiefEffectStrategy) {
        this.usingThiefEffectStrategy = usingThiefEffectStrategy;
        return this;
    }

    public CustomBotBuilder setUsingMurdererEffectStrategy(IUsingMurdererEffectStrategy usingMurdererEffectStrategy) {
        this.usingMurdererEffectStrategy = usingMurdererEffectStrategy;
        return this;
    }

    public CustomBot build() {
        return new CustomBot(nbGold, this.deck, this.view,
                this.pickingStrategy,
                this.characterChoosingStrategy,
                this.usingThiefEffectStrategy,
                this.usingMurdererEffectStrategy);
    }
}
