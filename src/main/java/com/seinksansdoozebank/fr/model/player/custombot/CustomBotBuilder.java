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

/**
 * The CustomBotBuilder class is a builder for the CustomBot class
 */
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

    /**
     * CustomBotBuilder constructor
     *
     * @param nbGold The number of gold pieces the player has.
     * @param view   The view
     * @param deck   The deck of cards
     * @param bank   The bank
     */
    public CustomBotBuilder(int nbGold, IView view, Deck deck, Bank bank) {
        this.nbGold = nbGold;
        this.view = view;
        this.deck = deck;
        this.bank = bank;
    }

    /**
     * Set the picking strategy
     *
     * @param pickingStrategy the picking strategy
     * @return the CustomBotBuilder
     */
    public CustomBotBuilder setPickingStrategy(IPickingStrategy pickingStrategy) {
        this.pickingStrategy = pickingStrategy;
        return this;
    }

    /**
     * Set the character choosing strategy
     * @param characterChoosingStrategy the character choosing strategy
     * @return the CustomBotBuilder
     */
    public CustomBotBuilder setCharacterChoosingStrategy(ICharacterChoosingStrategy characterChoosingStrategy) {
        this.characterChoosingStrategy = characterChoosingStrategy;
        return this;
    }

    /**
     * Set the thief effect strategy
     * @param usingThiefEffectStrategy the thief effect strategy
     * @return the CustomBotBuilder
     */
    public CustomBotBuilder setUsingThiefEffectStrategy(IUsingThiefEffectStrategy usingThiefEffectStrategy) {
        this.usingThiefEffectStrategy = usingThiefEffectStrategy;
        return this;
    }

    /**
     * Set the murderer effect strategy
     * @param usingMurdererEffectStrategy the murderer effect strategy
     * @return the CustomBotBuilder
     */
    public CustomBotBuilder setUsingMurdererEffectStrategy(IUsingMurdererEffectStrategy usingMurdererEffectStrategy) {
        this.usingMurdererEffectStrategy = usingMurdererEffectStrategy;
        return this;
    }

    /**
     * Set the warlord effect strategy
     * @param usingWarlordEffectStrategy the warlord effect strategy
     * @return the CustomBotBuilder
     */
    public CustomBotBuilder setUsingWarlordEffectStrategy(IUsingWarlordEffectStrategy usingWarlordEffectStrategy) {
        this.usingWarlordEffectStrategy = usingWarlordEffectStrategy;
        return this;
    }

    /**
     * Set the card choosing strategy
     * @param cardChoosingStrategy the card choosing strategy
     * @return the CustomBotBuilder
     */
    public CustomBotBuilder setCardChoosingStrategy(ICardChoosingStrategy cardChoosingStrategy) {
        this.cardChoosingStrategy = cardChoosingStrategy;
        return this;
    }

    /**
     * Build the CustomBot
     * @return the CustomBot
     */
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
