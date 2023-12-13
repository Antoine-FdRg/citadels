package com.seinksansdoozebank.fr.model.character.singleton;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public class Condottiere extends CommonCharacter {

    private Player player;
    private List<District> citadel;
    private static final Role role = Role.CONDOTTIERE;
    private static final DistrictType target = DistrictType.SOLDIERLY;
    private int goldCollected = 0;

    public Condottiere() {
        super(role);
    }

    /**
     * Set the player of the character
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Set the citadel of the character
     * @param citadel the citadel to set
     */
    public void setCitadel(List<District> citadel) {
        this.citadel = citadel;
    }

    /**
     * The condottiere can choose to destroy a district of another player
     * Paying the cost of the district to the bank -1
     */
    @Override
    public void useEffect() {
        // TODO: need to implement the strategy to destroy a district
    }

    /**
     * For each district in the citadel of the target type, the player will collect one gold
     */
    @Override
    public void goldCollectedFromDisctrictType() {
        int nbGold = 0;
        for (District district : this.citadel) {
            if (district.getDistrictType() == target) {
                nbGold++;
            }
        }
        this.goldCollected = nbGold;
        this.player.increaseGold(nbGold);
    }

    /**
     * Method called to perform the action of the character
     * This method will ask the user to choose an action to perform
     * The action will be performed by the character
     */
    @Override
    public void performAction() {
        // TODO: need to add a condition if the player wants to destroy a district
        this.goldCollected = 0;
        this.useEffect();
        this.goldCollectedFromDisctrictType();
    }

    @Override
    public String toString() {
        return "The character Condottiere";
    }
}