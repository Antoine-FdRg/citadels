package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public class Bishop extends CommonCharacter {
    public Bishop() {
        super(Role.BISHOP, DistrictType.RELIGION);
    }

    @Override
    public void useEffect() {
        // No action
    }

    @Override
    public void useEffect(Character character) {

    }

    @Override
    public void useEffect(Character character, District district) {

    }
}
