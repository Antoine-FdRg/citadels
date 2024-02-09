package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.roles.Role;

/**
 * The king character
 */
public class King extends CommonCharacter {

    /**
     * King constructor
     */
    public King() {
        super(Role.KING, DistrictType.NOBILITY);
    }

    @Override
    public void applyEffect() {
        // The king doesn't have any effect
    }
}
