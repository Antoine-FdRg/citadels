package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public abstract class CommonCharacter extends Character {
    private final DistrictType target;

    protected CommonCharacter(Role role, DistrictType target) {
        super(role);
        this.target = target;
    }

    /**
     * For each district in the citadel of the target type, the player will collect one gold
     */
    public void goldCollectedFromDisctrictType() {
        int nbGold = 0;
        for (Card card : this.getPlayer().getCitadel()) {
            if (card.getDistrict().getDistrictType() == target) {
                nbGold++;
            }
        }
        this.getPlayer().increaseGold(nbGold);
    }

    /**
     * Get the target type of the character
     *
     * @return the target type of the character
     */
    public DistrictType getTarget() {
        return target;
    }
}
