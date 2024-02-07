package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public class Thief extends Character {

    public Thief() {
        super(Role.THIEF);
    }

    /**
     * the method set the attribute goldWillBeStolen to true for the character which will be stolen
     * @param character the character to steal
     */
    public void useEffect(Character character) {
        //We verify if the character is not the thief
        if (character == this) {
            throw new IllegalArgumentException("The thief cannot steal from himself ");
        }
        //We verify if the character is not the assassin
        if (character.getRole() == Role.ASSASSIN) {
            throw new IllegalArgumentException("The thief cannot steal from the assassin ");
        }
        //We verify if the character is not dead
        if (character.isDead()) {
            throw new IllegalStateException("This character is already dead");
        }
        //We set the attribute goldWillBeStolen to true.
        character.setSavedThief(this.getPlayer());
    }

    @Override
    public void applyEffect() {
        Character target = this.getPlayer().useEffectThief();
        if (target != null) {
            this.useEffect(target);
        }
    }
}
