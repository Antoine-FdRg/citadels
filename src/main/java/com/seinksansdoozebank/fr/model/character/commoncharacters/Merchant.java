package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public class Merchant extends CommonCharacter {

    public Merchant() {
        super(Role.MERCHANT, DistrictType.TRADE_AND_CRAFTS);
    }

    /**
     * The merchant get 1 gold at the beginning of his turn
     */
    @Override
    public void useEffect() {
        this.getPlayer().increaseGold(1);
    }

    @Override
    public void useEffect(Character character) {

    }

    @Override
    public void useEffect(Character character, District district) {

    }

}
