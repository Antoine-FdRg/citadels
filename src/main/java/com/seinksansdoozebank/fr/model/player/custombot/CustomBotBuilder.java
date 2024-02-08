package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect.IUsingWarlordEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;

public class CustomBotBuilder {
    private final IView view;
    private final Deck deck;
    private final Bank bank;
    final int nbGold;
    private IPickingStrategy pickingStrategy;
    private ICharacterChoosingStrategy characterChoosingStrategy;
    private IUsingThiefEffectStrategy usingThiefEffectStrategy;
    private IUsingMurdererEffectStrategy usingMurdererEffectStrategy;
    private IUsingWarlordEffectStrategy usingWarlordEffectStrategy;
    private ICardChoosingStrategy cardChoosingStrategy;

    public CustomBotBuilder(int nbGold, IView view, Deck deck, Bank bank) {
        this.nbGold = nbGold;
        this.view = view;
        this.deck = deck;
        this.bank = bank;
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

    public CustomBotBuilder setUsingWarlordEffectStrategy(IUsingWarlordEffectStrategy usingWarlordEffectStrategy) {
        this.usingWarlordEffectStrategy = usingWarlordEffectStrategy;
        return this;
    }

    public CustomBotBuilder setCardChoosingStrategy(ICardChoosingStrategy cardChoosingStrategy) {
        this.cardChoosingStrategy = cardChoosingStrategy;
        return this;
    }

    public CustomBot build() {
        return new CustomBot(nbGold, this.deck, this.view, this.bank,
                this.pickingStrategy,
                this.characterChoosingStrategy,
                this.usingThiefEffectStrategy,
                this.usingMurdererEffectStrategy,
                this.usingWarlordEffectStrategy,
                this.cardChoosingStrategy);
    }
}
