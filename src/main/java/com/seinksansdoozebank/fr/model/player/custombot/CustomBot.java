package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.CondottiereTarget;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.IUsingCondottiereEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CustomBot extends RandomBot {

    IPickingStrategy pickingStrategy;
    ICharacterChoosingStrategy characterChoosingStrategy;
    IUsingThiefEffectStrategy usingThiefEffectStrategy;
    IUsingMurdererEffectStrategy usingMurdererEffectStrategy;
    IUsingCondottiereEffectStrategy usingCondottiereEffectStrategy;
    ICardChoosingStrategy cardChoosingStrategy;

    protected CustomBot(int nbGold, Deck deck, IView view,
                        IPickingStrategy pickingStrategy,
                        ICharacterChoosingStrategy characterChoosingStrategy,
                        IUsingThiefEffectStrategy usingThiefEffectStrategy,
                        IUsingMurdererEffectStrategy usingMurdererEffectStrategy,
                        IUsingCondottiereEffectStrategy usingCondottiereEffectStrategy,
                        ICardChoosingStrategy cardChoosingStrategy) {
        super(nbGold, deck, view);
        this.pickingStrategy = pickingStrategy;
        this.characterChoosingStrategy = characterChoosingStrategy;
        this.usingThiefEffectStrategy = usingThiefEffectStrategy;
        this.usingMurdererEffectStrategy = usingMurdererEffectStrategy;
        this.usingCondottiereEffectStrategy = usingCondottiereEffectStrategy;
        this.cardChoosingStrategy = cardChoosingStrategy;
    }

    public CustomBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    protected void pickSomething() {
        if (pickingStrategy == null) {
            this.randomPickSomething();
        } else {
            pickingStrategy.apply(this);
        }
    }

    protected void randomPickSomething() {
        super.pickSomething();
    }

    @Override
    protected Character chooseCharacterImpl(List<Character> characters) {
        if (characterChoosingStrategy == null) {
            return this.randomChooseCharacterImpl(characters);
        } else {
            return characterChoosingStrategy.apply(this, characters);
        }
    }

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

    protected Character randomUseMurdererEffect() {
        return super.useEffectAssassin();
    }

    @Override
    public CondottiereTarget chooseCondottiereTarget() {
        if (usingCondottiereEffectStrategy == null) {
            return this.randomUseCondottiereEffect();
        } else {
            return this.usingCondottiereEffectStrategy.apply(this);
        }
    }

    protected CondottiereTarget randomUseCondottiereEffect() {
        return super.chooseCondottiereTarget();
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
                Objects.equals(usingCondottiereEffectStrategy, customBot.usingCondottiereEffectStrategy);
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

    protected Optional<Card> randomChooseCard() {
        return super.chooseCard();
    }

    @Override
    public String toString() {
        return "Le bot custom " + this.id;
    }
}
