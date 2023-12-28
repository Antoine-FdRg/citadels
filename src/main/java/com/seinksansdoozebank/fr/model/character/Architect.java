package com.seinksansdoozebank.fr.model.character;

import com.seinksansdoozebank.fr.model.cards.District;
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
