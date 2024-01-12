package com.seinksansdoozebank.fr.model.character.commoncharacters;

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
        // No action
    }

    public void useEffect(Character character, District district) {
        if (this.getPlayer().getNbGold() < district.getCost() - 1) {
            throw new IllegalArgumentException("The player doesn't have enough gold to destroy the district");
        }
        if (character.getPlayer().equals(this.getPlayer())) {
            throw new IllegalArgumentException("The player can't destroy his own district");
        }
        if (character instanceof Bishop) {
            throw new IllegalArgumentException("The player can't destroy the district of the bishop");
        }
        if (district.equals(District.DONJON)) {
            throw new IllegalArgumentException("The player can't destroy the donjon");
        }
        if (character.getPlayer().destroyDistrict(this.getPlayer(), district)) {
            this.getPlayer().decreaseGold(district.getCost() - 1);
        }
    }
}