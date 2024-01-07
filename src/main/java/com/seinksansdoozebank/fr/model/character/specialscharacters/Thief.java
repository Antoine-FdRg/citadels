package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public class Thief extends Character {

    protected Thief(Role role) {
        super(Role.THIEF);
    }

    @Override
    public void useEffect() {

    }


    /**
     * the method set the attribute goldWillBeStolen to true for the character which will be stolen
     * @param character
     */
    public void useEffect(Character character) {
        //We verify if the character is not the thief
        if (character == this) {
            throw new IllegalArgumentException("The thief cannot still from itself ");
        }
        //We verify if the character is not the assassin
        if (character.getRole() == Role.ASSASSIN) {
            throw new IllegalArgumentException("The thief cannot still from the assassin ");
        }
        //We verify if the character is not dead
        if (character.isDead()) {
            throw new IllegalStateException("This character is already dead");
        }
        //We set the attribute goldWillBeStolen to true.
        character.setGoldWillBeStolen(true);
    }
}
