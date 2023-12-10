package com.seinksansdoozebank.fr.model.character;


import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public abstract class Character {
    private Role role;

    public Character(Role role) {
    }

    public abstract void useEffect();

    public abstract void goldCollectedFromDisctrictType(DistrictType target);
}
