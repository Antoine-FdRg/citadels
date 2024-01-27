package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Opponent;

public class Condottiere extends CommonCharacter {

    public Condottiere() {
        super(Role.CONDOTTIERE, DistrictType.SOLDIERLY);
    }

    /**
     * The condottiere can choose to destroy a district of another player
     * Paying the cost of the district to the bank -1
     *
     * @param opponent the opponent to destroy the district
     *                 (the opponent must have a character revealed)
     *                 (the opponent can't be the bishop)
     *                 (the opponent can't have a complete citadel)
     *                 (the opponent can't destroy the donjon)
     * @param district the district to destroy
     */
    public void useEffect(Opponent opponent, District district) {
        if (this.getPlayer().getNbGold() < district.getCost() - 1) {
            throw new IllegalArgumentException("The player doesn't have enough gold to destroy the district");
        }
        if (opponent.getOpponentCharacter() instanceof Bishop) {
            throw new IllegalArgumentException("The player can't destroy the district of the bishop");
        }
        if (district.equals(District.DONJON)) {
            throw new IllegalArgumentException("The player can't destroy the donjon");
        }
        if (opponent.getCitadel().size() >= 8) {
            throw new IllegalArgumentException("The player can't destroy a district if the player has a complete citadel");
        }
        if (opponent.destroyDistrict(this.getPlayer(), district)) {
            this.getPlayer().decreaseGold(district.getCost() - 1);
        }
    }
}