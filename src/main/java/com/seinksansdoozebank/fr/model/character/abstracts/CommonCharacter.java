package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.roles.Role;

/**
 * Represents a common character in the game
 */
public abstract class CommonCharacter extends Character {
    private final DistrictType target;

    /**
     * CommonCharacter constructor
     *
     * @param role   the role of the character
     * @param target the target type of the character
     */
    protected CommonCharacter(Role role, DistrictType target) {
        super(role);
        this.target = target;
    }

    /**
     * For each district in the citadel of the target type, the player will collect one gold
     */
    public void goldCollectedFromDistrictType() {
        int nbGold = 0;
        for (Card card : this.getPlayer().getCitadel()) {
            if (card.getDistrict().getDistrictType() == target || card.getDistrict().equals(District.SCHOOL_OF_MAGIC)) {
                nbGold++;
            }
        }
        this.getPlayer().pickGold(nbGold);
    }

    /**
     * Get the target type of the character
     *
     * @return the target type of the character
     */
    public DistrictType getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CommonCharacter commonCharacter) {
            return commonCharacter.getRole() == this.getRole();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getRole().hashCode();
    }
}
