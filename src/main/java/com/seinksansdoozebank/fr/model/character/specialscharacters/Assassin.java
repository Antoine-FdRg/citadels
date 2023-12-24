package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public class Assassin extends Character {

    public Assassin() {
        super(Role.ASSASSIN);
    }

    @Override
    public void useEffect() {
        // No action
    }

    /**
     * Kill the character
     * @param character the character to kill
     */
    @Override
    public void useEffect(Character character) {
        // Kill the specific character
        character.kill();
    }

    @Override
    public void useEffect(Character character, District district) {
        // No action
    }

}
