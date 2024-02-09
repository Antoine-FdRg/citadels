package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.WarlordTarget;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect.IUsingWarlordEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The CustomBot class represents a bot on a random base that can be customized with different strategies
 */
public class CustomBot extends RandomBot {

    IPickingStrategy pickingStrategy;
    ICharacterChoosingStrategy characterChoosingStrategy;
    IUsingThiefEffectStrategy usingThiefEffectStrategy;
    IUsingMurdererEffectStrategy usingMurdererEffectStrategy;
    IUsingWarlordEffectStrategy usingWarlordEffectStrategy;
    ICardChoosingStrategy cardChoosingStrategy;

    /**
     * CustomBot constructor with strategies
     *
     * @param nbGold                      The number of gold pieces the player has.
     * @param deck                        The deck of cards.
     * @param view                        The view
     * @param bank                        The bank
     * @param pickingStrategy             The picking strategy
     * @param characterChoosingStrategy   The character choosing strategy
     * @param usingThiefEffectStrategy    The thief effect strategy
     * @param usingMurdererEffectStrategy the murderer effect strategy
     * @param usingWarlordEffectStrategy  the warlord effect strategy
     * @param cardChoosingStrategy        the card chosing strategy
     */
    protected CustomBot(int nbGold, Deck deck, IView view, Bank bank,
                        IPickingStrategy pickingStrategy,
                        ICharacterChoosingStrategy characterChoosingStrategy,
                        IUsingThiefEffectStrategy usingThiefEffectStrategy,
                        IUsingMurdererEffectStrategy usingMurdererEffectStrategy,
                        IUsingWarlordEffectStrategy usingWarlordEffectStrategy,
                        ICardChoosingStrategy cardChoosingStrategy) {
        super(nbGold, deck, view, bank);
        this.pickingStrategy = pickingStrategy;
        this.characterChoosingStrategy = characterChoosingStrategy;
        this.usingThiefEffectStrategy = usingThiefEffectStrategy;
        this.usingMurdererEffectStrategy = usingMurdererEffectStrategy;
        this.usingWarlordEffectStrategy = usingWarlordEffectStrategy;
        this.cardChoosingStrategy = cardChoosingStrategy;
    }

    /**
     * CustomBot constructor without strategies (random bot)
     *
     * @param nbGold The number of gold pieces the player has.
     * @param deck   The deck of cards.
     * @param view   The view
     * @param bank   The bank
     */
    public CustomBot(int nbGold, Deck deck, IView view, Bank bank) {
        super(nbGold, deck, view, bank);
    }

    @Override
    protected void pickSomething() {
        if (pickingStrategy == null) {
            this.randomPickSomething();
        } else {
            pickingStrategy.apply(this);
        }
    }

    /**
     * Pick something randomly
     */
    protected void randomPickSomething() {
        super.pickSomething();
    }

    @Override
    protected Character chooseCharacterImpl(List<Character> characters) {
        Character character;
        if (characterChoosingStrategy == null) {
            character = this.randomChooseCharacterImpl(characters);
        } else {
            character = characterChoosingStrategy.apply(this, characters);
        }
        this.setLastCharacterChosen(character);
        return character;

    }

    /**
     * Choose a character randomly
     * @param characters the available characters
     * @return the chosen character
     */
    protected Character randomChooseCharacterImpl(List<Character> characters) {
        return super.chooseCharacterImpl(characters);
    }

    @Override
    public Character useEffectThief() {
        if (usingThiefEffectStrategy == null) {
            return this.randomUseThiefEffect();
        } else {
            view.displayPlayerUseThiefEffect(this);
            return this.usingThiefEffectStrategy.apply(this);
        }
    }

    /**
     * Use the thief effect randomly
     * @return the character to steal from
     */
    protected Character randomUseThiefEffect() {
        return super.useEffectThief();
    }

    @Override
    public Character useEffectAssassin() {
        if (usingMurdererEffectStrategy == null) {
            return this.randomUseMurdererEffect();
        } else {
            return this.usingMurdererEffectStrategy.apply(this, view);
        }
    }

    /**
     * Use the murderer effect randomly
     * @return the chosen character
     */
    protected Character randomUseMurdererEffect() {
        return super.useEffectAssassin();
    }

    @Override
    public WarlordTarget chooseWarlordTarget(List<Opponent> opponentsFocusable) {
        if (usingWarlordEffectStrategy == null) {
            return this.randomUseWarlordEffect(opponentsFocusable);
        } else {
            return this.usingWarlordEffectStrategy.apply(this, opponentsFocusable);
        }
    }

    /**
     * Use the warlord effect randomly
     * @param opponentsFocusable The opponent the warlord can choose
     * @return The warlord target
     */
    protected WarlordTarget randomUseWarlordEffect(List<Opponent> opponentsFocusable) {
        return super.chooseWarlordTarget(opponentsFocusable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomBot customBot = (CustomBot) o;

        return customBot.getId() == this.getId() &&
                Objects.equals(characterChoosingStrategy, customBot.characterChoosingStrategy) &&
                Objects.equals(pickingStrategy, customBot.pickingStrategy) &&
                Objects.equals(usingThiefEffectStrategy, customBot.usingThiefEffectStrategy) &&
                Objects.equals(usingMurdererEffectStrategy, customBot.usingMurdererEffectStrategy) &&
                Objects.equals(usingWarlordEffectStrategy, customBot.usingWarlordEffectStrategy);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Optional<Card> chooseCard() {
        if (this.cardChoosingStrategy == null) {
            return this.randomChooseCard();
        }
        return this.cardChoosingStrategy.apply(this);
    }

    /**
     * Choose a card randomly
     * @return the chosen card
     */
    protected Optional<Card> randomChooseCard() {
        return super.chooseCard();
    }

    @Override
    public String toString() {
        return "Le bot custom " + this.id;
    }
}
