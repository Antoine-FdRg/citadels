package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;

public class Architect extends Character {

    public Architect() {
        super(Role.ARCHITECT);
    }


    @Override
    public void useEffect() {
        // No action
    }

}
