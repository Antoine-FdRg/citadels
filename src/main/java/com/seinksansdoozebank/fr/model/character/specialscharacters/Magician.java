package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Opponent;

import java.util.List;

/**
 * The magician character
 */
public class Magician extends Character {
    /**
     * Magician constructor
     */
    public Magician() {
        super(Role.MAGICIAN);
    }

    /**
     * Switch the hand of the player with the hand of the target player
     * If the target player is not present, the player will switch his hand with the deck
     *
     * @param magicianTarget the target of the magician
     */
    public void useEffect(MagicianTarget magicianTarget) {
        Opponent targetOpponent = magicianTarget.targetOpponent();
        List<Card> cardsToSwitchWithDeck = magicianTarget.cardsToSwitchWithDeck();
        if (targetOpponent != null) { //picking card from target player's hand
            targetOpponent.switchHandWith(this.getPlayer());
        } else { //picking card from deck
            for (Card card : cardsToSwitchWithDeck) {
                this.getPlayer().discardACard(card);
                this.getPlayer().pickACard();
            }
        }
    }

    @Override
    public void applyEffect() {
        MagicianTarget magicianTarget = this.getPlayer().useEffectMagician();
        if (magicianTarget != null) {
            this.useEffect(magicianTarget);
        }
    }
}
