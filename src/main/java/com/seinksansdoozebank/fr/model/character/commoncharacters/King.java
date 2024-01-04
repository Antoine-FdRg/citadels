package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public class King extends CommonCharacter {

    public King() {
        super(Role.KING, DistrictType.NOBILITY);
    }

    @Override
    public void useEffect() {
        // No action
    }

}
