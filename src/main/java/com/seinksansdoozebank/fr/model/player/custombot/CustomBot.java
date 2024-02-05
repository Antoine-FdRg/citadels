package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.IUsingCondottiereEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;

import java.util.List;
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
    public void useEffectThief(Thief thief) {
        if (usingThiefEffectStrategy == null) {
            this.randomUseThiefEffect(thief);
        } else {
            usingThiefEffectStrategy.apply(this, thief);
            view.displayPlayerUseThiefEffect(this);
        }
    }

    protected void randomUseThiefEffect(Thief thief) {
        super.useEffectThief(thief);
    }

    @Override
    public void useEffectAssassin(Assassin murderer) {
        if (usingMurdererEffectStrategy == null) {
            this.randomUseMurdererEffect(murderer);
        } else {
            this.usingMurdererEffectStrategy.apply(this, murderer, view);
        }
    }

    protected void randomUseMurdererEffect(Assassin murderer) {
        super.useEffectAssassin(murderer);
    }

    @Override
    public void useEffectCondottiere(Condottiere condottiere) {
        if (usingCondottiereEffectStrategy == null) {
            this.randomUseCondottiereEffect(condottiere);
        } else {
            this.usingCondottiereEffectStrategy.apply(this, condottiere);
        }
    }

    protected void randomUseCondottiereEffect(Condottiere condottiere) {
        super.useEffectCondottiere(condottiere);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomBot customBot) {
            return this.characterChoosingStrategy == customBot.characterChoosingStrategy
                    && this.pickingStrategy == customBot.pickingStrategy
                    && this.usingThiefEffectStrategy == customBot.usingThiefEffectStrategy
                    && this.usingMurdererEffectStrategy == customBot.usingMurdererEffectStrategy
                    && this.usingCondottiereEffectStrategy == customBot.usingCondottiereEffectStrategy;
        } else {
            return false;
        }
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

    public IPickingStrategy getPickingStrategy() {
        return pickingStrategy;
    }

    public ICharacterChoosingStrategy getCharacterChoosingStrategy() {
        return characterChoosingStrategy;
    }

    public IUsingThiefEffectStrategy getUsingThiefEffectStrategy() {
        return usingThiefEffectStrategy;
    }

    public IUsingMurdererEffectStrategy getUsingMurdererEffectStrategy() {
        return usingMurdererEffectStrategy;
    }

    public IUsingCondottiereEffectStrategy getUsingCondottiereEffectStrategy() {
        return usingCondottiereEffectStrategy;
    }

    public ICardChoosingStrategy getCardChoosingStrategy() {
        return cardChoosingStrategy;
    }
}
