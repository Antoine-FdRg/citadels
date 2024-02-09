package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;

/**
 * Represents the architect character
 */
public class Architect extends Character {

    /**
     * Architect constructor
     */
    public Architect() {
        super(Role.ARCHITECT);
    }

    @Override
    public void applyEffect() {
        this.getPlayer().useEffectArchitect();
    }
}
