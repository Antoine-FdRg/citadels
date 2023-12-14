package com.seinksansdoozebank.fr.model.character.singleton;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

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
        // No action
    }

    @Override
    public void useEffect(Character character, District district) {
        // No action
    }
}
