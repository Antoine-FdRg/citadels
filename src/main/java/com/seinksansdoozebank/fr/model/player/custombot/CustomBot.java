package com.seinksansdoozebank.fr.model.player.custombot;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.view.IView;

import java.util.List;

public class CustomBot extends RandomBot {

    IPickingStrategy pickingStrategy;
    ICharacterChoosingStrategy characterChoosingStrategy;
    IUsingThiefEffectStrategy usingThiefEffectStrategy;
    IUsingMurdererEffectStrategy usingMurdererEffectStrategy;

    protected CustomBot(int nbGold, Deck deck, IView view,
                        IPickingStrategy pickingStrategy,
                        ICharacterChoosingStrategy characterChoosingStrategy,
                        IUsingThiefEffectStrategy usingThiefEffectStrategy,
                        IUsingMurdererEffectStrategy usingMurdererEffectStrategy) {
        super(nbGold, deck, view);
        this.pickingStrategy = pickingStrategy;
        this.characterChoosingStrategy = characterChoosingStrategy;
        this.usingThiefEffectStrategy = usingThiefEffectStrategy;
        this.usingMurdererEffectStrategy = usingMurdererEffectStrategy;
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
            this.usingMurdererEffectStrategy.apply(this, murderer);
        }
    }

    protected void randomUseMurdererEffect(Assassin murderer) {
        super.useEffectAssassin(murderer);
    }

    @Override
    public String toString() {
        return "Le bot custom " + this.id;
    }
}
