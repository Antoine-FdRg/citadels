package com.seinksansdoozebank.fr.model.character.singleton;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public class Merchant extends CommonCharacter {

    private final Player player;
    private final List<District> citadel;
    private static final Role role = Role.MERCHANT;
    private static final DistrictType target = DistrictType.TRADE_AND_CRAFTS;

    public Merchant(List<District> citadel, Player player) {
        super(role);
        this.citadel = citadel;
        this.player = player;
    }

    /**
     * The merchant get 1 gold at the beginning of his turn
     */
    @Override
    public void useEffect() {
        this.player.increaseGold(1);
    }

    /**
     * For each district in the citadel of the target type, the player will collect one gold
     */
    @Override
    public int goldCollectedFromDisctrictType() {
        int nbGold = 0;
        for (District district : this.citadel) {
            if (district.getDistrictType() == target) {
                nbGold++;
            }
        }
        return nbGold;
    }

    /**
     * Method called to perform the action of the character
     * This method will ask the user to choose an action to perform
     * The action will be performed by the character
     */
    @Override
    public void performAction() {
        this.useEffect();
        this.player.increaseGold(this.goldCollectedFromDisctrictType());
    }
}
