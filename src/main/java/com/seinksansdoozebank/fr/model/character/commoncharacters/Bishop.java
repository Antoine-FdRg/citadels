package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.roles.Role;

/**
 * Represents the bishop character
 */
public class Bishop extends CommonCharacter {
    /**
     * Bishop constructor
     */
    public Bishop() {
        super(Role.BISHOP, DistrictType.RELIGION);
    }

    @Override
    public void applyEffect() {
        // No effect
    }
}
