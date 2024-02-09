package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Opponent;

import java.util.List;

/**
 * The warlord character
 */
public class Warlord extends CommonCharacter {
    /**
     * Warlord constructor
     */
    public Warlord() {
        super(Role.WARLORD, DistrictType.SOLDIERLY);
    }

    @Override
    public void applyEffect() {
        List<Opponent> opponentsFocusableForWarlord = getOpponentsFocusableForWarlord(this.getPlayer().getOpponents());
        if (opponentsFocusableForWarlord.isEmpty()) {
            return;
        }
        WarlordTarget warlordTarget = this.getPlayer().chooseWarlordTarget(opponentsFocusableForWarlord);
        if (warlordTarget != null) {
            this.useEffect(warlordTarget);
        }
    }

    /**
     * Get the opponents that the warlord can destroy a district
     *
     * @param playerOpponents the opponents of the player
     * @return the opponents that the warlord can destroy a district
     */
    public static List<Opponent> getOpponentsFocusableForWarlord(List<Opponent> playerOpponents) {
        return playerOpponents.stream()
                .filter(opponent ->
                        opponent.getCitadel().size() < 8
                                && (opponent.getOpponentCharacter() == null || !opponent.getOpponentCharacter().getRole().equals(Role.BISHOP))).toList();
    }

    /**
     * The warlord can choose to destroy a district of another player
     * Paying the cost of the district to the bank -1
     *
     * @param warlordTarget the opponent to destroy the district
     *                          (the opponent must have a character revealed)
     *                          (the opponent can't be the bishop)
     *                          (the opponent can't have a complete citadel)
     *                          (the opponent can't destroy the donjon)
     *                          district the district to destroy
     */
    public void useEffect(WarlordTarget warlordTarget) {
        if (warlordTarget == null) {
            throw new IllegalArgumentException("The player must choose a target");
        }
        Opponent opponent = warlordTarget.opponent();
        District district = warlordTarget.district();
        if (this.getPlayer().getNbGold() < district.getCost() - 1) {
            throw new IllegalArgumentException("The player doesn't have enough gold to destroy the district");
        }
        if (opponent.getOpponentCharacter() != null && opponent.getOpponentCharacter().getRole().equals(Role.BISHOP)) {
            throw new IllegalArgumentException("The player can't destroy the district of the bishop");
        }
        if (district.equals(District.DONJON)) {
            throw new IllegalArgumentException("The player can't destroy the donjon");
        }
        if (opponent.getCitadel().size() >= 8) {
            throw new IllegalArgumentException("The player can't destroy a district if the player has a complete citadel");
        }
        opponent.destroyDistrict(this.getPlayer(), district);
        this.getPlayer().returnGoldToBank(district.getCost() - 1);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Warlord;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}