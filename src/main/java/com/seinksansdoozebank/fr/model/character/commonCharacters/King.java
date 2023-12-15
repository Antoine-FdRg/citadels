package com.seinksansdoozebank.fr.model.character.commonCharacters;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public class King extends CommonCharacter {

    public King() {
        super(Role.KING, DistrictType.NOBILITY);
    }

    @Override
    public void useEffect() {
        // No action
    }

    @Override
    public void useEffect(Character character) {
        // No action
    }

    @Override
    public void useEffect(Character character, District district) {
        // No action
    }
}
