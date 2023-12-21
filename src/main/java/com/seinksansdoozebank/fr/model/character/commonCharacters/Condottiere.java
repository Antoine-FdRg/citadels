package com.seinksansdoozebank.fr.model.character.commonCharacters;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public class Condottiere extends CommonCharacter {

    public Condottiere() {
        super(Role.CONDOTTIERE, DistrictType.SOLDIERLY);
    }

    /**
     * The condottiere can choose to destroy a district of another player
     * Paying the cost of the district to the bank -1
     */
    @Override
    public void useEffect() {
        this.getPlayer().increaseGold(1);
    }

    @Override
    public void useEffect(Character character) {
        // No action
    }

    @Override
    public void useEffect(Character character, District district) {
        // TODO: need to implement the strategy to destroy a district
    }
}