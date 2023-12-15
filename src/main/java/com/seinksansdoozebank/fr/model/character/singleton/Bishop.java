package com.seinksansdoozebank.fr.model.character.singleton;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

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
        // No action
    }

    @Override
    public void useEffect(Character character, District district) {
        // No action
    }
}
